package com.mamabayamba.physicstest3;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Created by макс on 25.03.2017.
 */

public class BuildingBlock {
    private Body body;
    private boolean isPlaced;
    private Image image;

    public BuildingBlock(Body body, Image image) {
        this.body = body;
        this.isPlaced = false;
        this.image = image;
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

    public Image getActualImage(){
        image.setOrigin(image.getWidth()/2, image.getHeight()/2);
        image.setRotation((float) Math.toDegrees(this.body.getAngle()));
        Vector3 position = new Vector3(body.getPosition().x, body.getPosition().y, 0);
        MyGdxGame.camera.project(position);
        image.setPosition(position.x-image.getWidth()/2, position.y-image.getHeight()/2);
        return image;
    }

    public Image getImage() {
        return image;
    }

}
