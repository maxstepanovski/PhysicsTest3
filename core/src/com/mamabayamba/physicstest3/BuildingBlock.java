package com.mamabayamba.physicstest3;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Created by макс on 25.03.2017.
 */

public class BuildingBlock {
    private Camera camera;
    private Body body;
    private boolean isPlaced;
    private Sprite sprite;

    public BuildingBlock(Body body, Sprite sprite, Camera camera) {
        this.body = body;
        this.isPlaced = false;
        this.sprite = sprite;
        this.camera = camera;
    }

    public Body getBody() {
        return body;
    }

    public boolean isPlaced() {
        return isPlaced;
    }

    public void setPlaced(boolean placed) {
        isPlaced = placed;
    }

    public Sprite getActualSprite(){
        Vector3 bodyPosition = new Vector3(body.getPosition().x, body.getPosition().y, 0);
        Vector3 projectedBodyPosition = camera.project(bodyPosition);
        sprite.setPosition(projectedBodyPosition.x - sprite.getWidth()/2, projectedBodyPosition.y - sprite.getHeight()/2);
        sprite.setRotation((float) Math.toDegrees(body.getAngle()));
        return sprite;
    }

    public void respawn(Vector2 position){
        body.setTransform(position, body.getAngle());
        body.setLinearVelocity(0,0);
    }
}
