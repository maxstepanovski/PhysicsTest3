package com.mamabayamba.physicstest3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by макс on 28.03.2017.
 */

public class GameLogicManager {
    public static final float HORIZONTAL_FORCE = 70.0f;
    public static final float VERTICAL_FORCE = 140.0f;
    public static Vector2 startingPosition;
    private World world;
    private Camera camera;
    private Array<BuildingBlock> blocks;
    private Array<BuildingBlock> miscellaneous;
    private Sprite background;
    private BuildingBlock flyingIsland, startingIsland;
    private ObjectFactory factory;
    private int blockCounter;
    private boolean newGameNeeded;


    public GameLogicManager(World world, Camera camera){
        this.world = world;
        this.camera = camera;
        this.factory = new ObjectFactory(world, camera);
        this.newGameNeeded = false;
        blocks = new Array<BuildingBlock>();
        miscellaneous = new Array<BuildingBlock>();
        blockCounter = 0;
        startingPosition = new Vector2(camera.viewportWidth/2 + 15, 5);
        this.background = new Sprite(new Texture(Gdx.files.internal("sky.png")));
        this.background.setPosition(0,0);
        this.background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void newGame(){
        BuildingBlock firstBlock = factory.createBlock(camera.viewportWidth/2, 5);
        firstBlock.setPlaced(true);
        blocks.add(firstBlock);
        blockCounter++;
        blocks.add(factory.createBlock(startingPosition.x, startingPosition.y));
        blockCounter++;
        flyingIsland = factory.createGround(camera.viewportWidth/2, 0, 5, 1);
        miscellaneous.add(flyingIsland);
        startingIsland = factory.createGround(startingPosition.x, 0, 2, 1);
        miscellaneous.add(startingIsland);
    }

    public void endGame(){
        for(BuildingBlock block: blocks){
            world.destroyBody(block.getBody());
        }
        for(BuildingBlock block:miscellaneous){
            world.destroyBody(block.getBody());
        }
        if(blocks.size > 0)
            blocks.clear();
        if(miscellaneous.size > 0)
            miscellaneous.clear();
        blockCounter = 0;
    }

    public void checkIfAllBlocksPlaced(){
        Array<Contact> contacts = world.getContactList();
        boolean onStartingPosition = false;
        BuildingBlock controlledBlock = blocks.get(blockCounter-1);
        if(!controlledBlock.getBody().isAwake()){
            for(Contact contact: contacts){
                if( (contact.getFixtureA().getBody().equals(controlledBlock.getBody()) || contact.getFixtureB().getBody().equals(controlledBlock.getBody()) ) &&
                        (contact.getFixtureA().getBody().equals(startingIsland.getBody()) || contact.getFixtureB().getBody().equals(startingIsland.getBody())) ){
                    onStartingPosition = true;
                }
            }
            if(!onStartingPosition){
                controlledBlock.setPlaced(true);
            }
        }
    }

    public void checkIfNewGameNeeded(){
        for(BuildingBlock block: blocks){
            if(block.isPlaced() &&
                    block.getBody().getPosition().y < -5){
                newGameNeeded = true;
            }
        }
    }

    public void checkIfNewBlockNeeded(){
        int overallBlocks = blocks.size;
        int placedBlocks = 0;
        for(BuildingBlock block: blocks){
            if(block.isPlaced())
                placedBlocks++;
        }
        if(overallBlocks == placedBlocks){
            blocks.add(factory.createBlock(startingPosition.x, startingPosition.y));
            blockCounter++;
        }
    }

    public void checkBoarders(){
        Body controlledBlock = blocks.get(blockCounter-1).getBody();
        float currentX = controlledBlock.getPosition().x;
        float currentY = controlledBlock.getPosition().y;
        float currentAngle = controlledBlock.getAngle();
        float leftBoarder = 0;
        float rightBoarder = camera.viewportWidth;
        if(currentX > rightBoarder){
            controlledBlock.setTransform(rightBoarder, currentY, currentAngle);
            controlledBlock.setLinearVelocity(0,controlledBlock.getLinearVelocity().y);
        }else if(currentX < 0){
            controlledBlock.setTransform(leftBoarder, currentY, currentAngle);
            controlledBlock.setLinearVelocity(0,controlledBlock.getLinearVelocity().y);
        }
        if(currentY < -5){
            controlledBlock.setTransform(startingPosition, currentAngle);
            controlledBlock.setLinearVelocity(0,0);
        }
    }

    public void handleInput(){
        Body controlledBlock = blocks.get(blockCounter-1).getBody();
        float x = Gdx.input.getX();
        float y = Gdx.input.getY();
        Vector3 touch = new Vector3();
        camera.unproject(touch.set(x, y, 0));
        float maxMultiplierX = camera.viewportWidth/2;
        float maxMultiplierY = camera.viewportHeight/2;
        Vector2 blockCenter = controlledBlock.getPosition();
        if(touch.x > controlledBlock.getPosition().x && touch.y < controlledBlock.getPosition().y){
            controlledBlock.applyForceToCenter(-((touch.x-blockCenter.x)/maxMultiplierX)*HORIZONTAL_FORCE, ((blockCenter.y-touch.y)/maxMultiplierY)*VERTICAL_FORCE, true);
        }else if(touch.x < controlledBlock.getPosition().x && touch.y < controlledBlock.getPosition().y){
            controlledBlock.applyForceToCenter(((blockCenter.x-touch.x)/maxMultiplierX)*HORIZONTAL_FORCE, ((blockCenter.y-touch.y)/maxMultiplierY)*VERTICAL_FORCE, true);
        }
    }

    public void updateCamera(){
        Body controlledBlock = blocks.get(blockCounter-1).getBody();
        camera.position.set(controlledBlock.getPosition().x, controlledBlock.getPosition().y, 0);
        camera.update();
    }

    public boolean isNewGameNeeded() {
        return newGameNeeded;
    }

    public void setNewGameNeeded(boolean newGameNeeded) {
        this.newGameNeeded = newGameNeeded;
    }

    public void updateHUD(){
        int placedBlockCounter = 0;
        float maxHeight = 0;
        for(BuildingBlock block: blocks){
            if(block.isPlaced()){
                placedBlockCounter++;
                if(block.getBody().getPosition().y > maxHeight)
                    maxHeight = block.getBody().getPosition().y;
            }
        }
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public void drawTextures(SpriteBatch batch){
        background.draw(batch);
        for(BuildingBlock block: miscellaneous){
            block.getActualSprite().draw(batch);
        }
        for(BuildingBlock block: blocks){
            block.getActualSprite().draw(batch);
        }
    }
}
