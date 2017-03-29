package com.mamabayamba.physicstest3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
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
    private World world;
    private Camera camera;
    private Array<BuildingBlock> blocks;
    private Body ground, leftWall, rightWall;
    private ObjectFactory factory;
    private HUDManager hudManager;
    private int blockCounter;
    private boolean newGameNeeded;


    public GameLogicManager(World world, Camera camera){
        this.world = world;
        this.camera = camera;
        this.factory = new ObjectFactory(world);
        this.hudManager = new HUDManager(camera);
        this.newGameNeeded = false;
        blocks = new Array<BuildingBlock>();
        blockCounter = 0;
    }

    public void newGame(){
        BuildingBlock block = new BuildingBlock(factory.createBlock(camera.viewportWidth/2, 5));
        blocks.add(block);
        blockCounter++;
        blocks.add(new BuildingBlock(factory.createBlock(camera.viewportWidth/2+10, 5)));
        blockCounter++;
        ground = factory.createGround(camera.viewportWidth/2, 0, camera.viewportWidth/2, 1);
        leftWall = factory.createBoarder(camera.viewportWidth/2 - 5, 11f, 0.1f, 10f);
        rightWall = factory.createBoarder(camera.viewportWidth/2 + 5, 11f, 0.1f, 10f);
    }

    public void checkPlacedBlocks(){
        Array<Contact> contacts = world.getContactList();
        for(BuildingBlock block: blocks){
            if(block.isPlaced() == false &&
                    block.getBody().getPosition().x < (camera.viewportWidth/2 + 5) &&
                    block.getBody().getPosition().x > (camera.viewportWidth/2 - 5) &&
                    !block.getBody().isAwake()){
                for(Contact contact: contacts){
                    if(contact.getFixtureA().getBody().equals(block.getBody()) ||
                            contact.getFixtureB().getBody().equals(block.getBody())){
                        if(contact.getFixtureA().getBody().equals(rightWall) ||
                                contact.getFixtureB().getBody().equals(rightWall)){
                            block.setPlaced(false);
                            return;
                        }else if(contact.getFixtureA().getBody().equals(leftWall) ||
                                contact.getFixtureB().getBody().equals(leftWall)){
                            block.setPlaced(false);
                            return;
                        }
                    }
                }
                block.setPlaced(true);
                Gdx.app.log("happy", "block placed!");
            }
        }
        return;
    }

    public void checkIfNewGameNeeded(Contact givenContact){
        if(givenContact.getFixtureA().getBody().equals(rightWall) ||
                givenContact.getFixtureB().getBody().equals(rightWall) ||
                givenContact.getFixtureA().getBody().equals(leftWall) ||
                givenContact.getFixtureB().getBody().equals(leftWall)) {
            for(BuildingBlock block: blocks){
                if(givenContact.getFixtureA().getBody().equals(block.getBody()) ||
                        givenContact.getFixtureB().getBody().equals(block.getBody())){
                    if(block.isPlaced()){
                        Gdx.app.log("happy", "game over!");
                        newGameNeeded = true;
                    }
                }
            }
        }
    }

    public void spawnNewBlockIfNeeded(){
        int overallBlocks = blocks.size;
        int placedBlocks = 0;
        for(BuildingBlock block: blocks){
            if(block.isPlaced())
                placedBlocks++;
        }
        if(overallBlocks == placedBlocks){
            blocks.add(new BuildingBlock(factory.createBlock(camera.viewportWidth/2 + 8, 5)));
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
            controlledBlock.setLinearVelocity(0,0);
        }else if(currentX < 0){
            controlledBlock.setTransform(leftBoarder, currentY, currentAngle);
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

    public void endGame(){
        for(BuildingBlock block: blocks){
            world.destroyBody(block.getBody());
        }
        world.destroyBody(rightWall);
        world.destroyBody(leftWall);
        world.destroyBody(ground);
        if(blocks.size > 0)
            blocks.clear();
        blockCounter = 0;
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
        hudManager.updateStatisticsHUD((float) round(maxHeight,1), placedBlockCounter);
        hudManager.draw();
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}
