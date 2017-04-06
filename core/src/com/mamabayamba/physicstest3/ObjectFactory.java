package com.mamabayamba.physicstest3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import java.util.Random;

/**
 * Created by макс on 21.03.2017.
 */

public class ObjectFactory {
    private World world;
    private Texture flyingIslandTexture;
    private TextureRegion sandTile, cosmoTile, whiteTile;
    private TextureRegion[][] sandRegions, cosmoRegions, whiteRegions;
    private float pixelsInMeters;

    public ObjectFactory(World world){
        this.world = world;
        this.pixelsInMeters = MyGdxGame.pixelsInMeters;
        this.sandTile = new TextureRegion(new Texture(Gdx.files.internal("sand_tile.png")));
        this.cosmoTile = new TextureRegion(new Texture(Gdx.files.internal("cosmo_tile.png")));
        this.whiteTile = new TextureRegion(new Texture(Gdx.files.internal("white_tile.png")));
        this.flyingIslandTexture = new Texture(Gdx.files.internal("ground.png"));
        sandRegions = sandTile.split(100, 100);
        cosmoRegions = cosmoTile.split(100,100);
        whiteRegions = whiteTile.split(100,100);
    }

    public BuildingBlock createBlock(float x, float y){
        BodyDef definition = new BodyDef();
        definition.type = BodyDef.BodyType.DynamicBody;
        definition.position.set(new Vector2(x,y));
        Random random = new Random();
        Body body = world.createBody(definition);

        PolygonShape polygon = new PolygonShape();
        float width = random.nextFloat()*0.8f+0.7f;
        float height = random.nextFloat()*0.8f+0.7f;
        polygon.setAsBox(width, height);

        FixtureDef fixtureDefinition = new FixtureDef();
        fixtureDefinition.shape = polygon;
        fixtureDefinition.restitution = 0.0f;
        fixtureDefinition.density = 1.0f;
        fixtureDefinition.friction = 0.9f;
        Fixture fixture = body.createFixture(fixtureDefinition);
        polygon.dispose();
        Image image;

        switch(random.nextInt(2)){
            case 0:
                image = new Image(sandRegions[random.nextInt(2)][random.nextInt(2)]);
                break;
            case 1:
                image = new Image(cosmoRegions[random.nextInt(2)][random.nextInt(2)]);
                break;
            case 2:
                image = new Image(whiteRegions[random.nextInt(2)][random.nextInt(2)]);
                break;
            default:
                image = new Image(whiteRegions[random.nextInt(2)][random.nextInt(2)]);
                break;
        }

        image.setSize(2*width*pixelsInMeters, 2*height*pixelsInMeters);
        BuildingBlock block = new BuildingBlock(body, image);
        return block;
    }

    public BuildingBlock createGround(float xCoordinate, float yCoordinate, float xWidth, float yHeight){
        BodyDef definition = new BodyDef();
        definition.type = BodyDef.BodyType.StaticBody;
        definition.position.set(new Vector2(xCoordinate, yCoordinate));
        Body body = world.createBody(definition);

        PolygonShape polygon = new PolygonShape();
        polygon.setAsBox(xWidth, yHeight);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.friction = 0.5f;
        fixtureDef.density = 1f;
        fixtureDef.restitution = 0f;
        fixtureDef.shape = polygon;

        Fixture fixture = body.createFixture(fixtureDef);
        polygon.dispose();

        Image image = new Image(flyingIslandTexture);
        image.setSize(2*xWidth*pixelsInMeters, 2*yHeight*pixelsInMeters);
        Vector3 position = new Vector3(body.getPosition().x, body.getPosition().y, 0);
        MyGdxGame.camera.project(position);
        image.setPosition(position.x-image.getWidth()/2, position.y-image.getHeight()/2);
        BuildingBlock block = new BuildingBlock(body, image);
        return block;
    }
}
