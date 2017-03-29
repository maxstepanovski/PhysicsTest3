package com.mamabayamba.physicstest3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * Created by макс on 28.03.2017.
 */

public class TextManager {
    private BitmapFont arialFont;

    public TextManager(){
        this.arialFont = new BitmapFont(Gdx.files.internal("arial.fnt"));
    }

    public void displayText(String text, float x, float y, Batch batch){

    }
}
