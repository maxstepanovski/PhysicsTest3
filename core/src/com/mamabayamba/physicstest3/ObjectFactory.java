package com.mamabayamba.physicstest3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import java.util.Random;

/**
 * Created by макс on 21.03.2017.
 */

public class ObjectFactory {
    private static World world;

    public ObjectFactory(World world){
        this.world = world;
    }

    public Body createBlock(float x, float y){
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

        return body;
    }

    public Body createBoarder(float xCoordinate, float yCoordinate, float xWidth, float yHeight){
        BodyDef definition = new BodyDef();
        definition.type = BodyDef.BodyType.StaticBody;
        definition.position.set(new Vector2(xCoordinate, yCoordinate));
        Body body = world.createBody(definition);

        PolygonShape polygon = new PolygonShape();
        polygon.setAsBox(xWidth, yHeight);

        FixtureDef fixtureDefinition = new FixtureDef();
        fixtureDefinition.shape = polygon;
        fixtureDefinition.isSensor = true;
        Fixture fixture = body.createFixture(fixtureDefinition);
        polygon.dispose();

        return body;
    }

    public Body createGround(float xCoordinate, float yCoordinate, float xWidth, float yHeight){
        BodyDef definition = new BodyDef();
        definition.type = BodyDef.BodyType.StaticBody;
        definition.position.set(new Vector2(xCoordinate, yCoordinate));
        Body body = world.createBody(definition);

        PolygonShape polygon = new PolygonShape();
        polygon.setAsBox(xWidth, yHeight);

        Fixture fixture = body.createFixture(polygon, 1.0f);
        polygon.dispose();
        return body;
    }

    public TextButton createButton(String textSign, float xCoordinate, float yCoordinate){
        BitmapFont font = new BitmapFont();
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        TextButton button = new TextButton(textSign, textButtonStyle);
        button.setPosition(xCoordinate, yCoordinate);
        button.setSize(3f*MyGdxGame.pixelsInMeters, 1.5f*MyGdxGame.pixelsInMeters);
        return button;
    }
}
