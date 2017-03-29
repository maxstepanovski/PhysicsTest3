package com.mamabayamba.physicstest3;

import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by макс on 25.03.2017.
 */

public class BuildingBlock {
    private Body body;
    private boolean isPlaced;

    public BuildingBlock(Body body) {
        this.body = body;
        this.isPlaced = false;
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
}
