package com.mamabayamba.physicstest3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by макс on 28.03.2017.
 */

public class HUDManager {
    private static float statsX;
    private static float towerHeightY;
    private static float blockCounterY;
    private BitmapFont arialFont;
    private Stage stage;
    private Label.LabelStyle style;
    private Label towerHeightLabel;
    private Label blockCounterLabel;

    public HUDManager(Camera camera){
        this.arialFont = new BitmapFont(Gdx.files.internal("arial16.fnt"));
        this.stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        statsX = MyGdxGame.pixelsInMeters;
        towerHeightY = Gdx.graphics.getHeight()-2*MyGdxGame.pixelsInMeters;
        blockCounterY = Gdx.graphics.getHeight()-4*MyGdxGame.pixelsInMeters;
        style = new Label.LabelStyle(arialFont, Color.CYAN);
        towerHeightLabel = new Label("Tower height: ", style);
        towerHeightLabel.setPosition(statsX, towerHeightY);
        stage.addActor(towerHeightLabel);
        blockCounterLabel = new Label("Blocks placed: ", style);
        blockCounterLabel.setPosition(statsX, blockCounterY);
        stage.addActor(blockCounterLabel);
    }

    public void updateStatisticsHUD(float towerHeight, int blockCounter){
        String a = "Tower height: "+towerHeight;
        String b = "Blocks placed: "+blockCounter;
        towerHeightLabel.setText(a);
        blockCounterLabel.setText(b);
    }

    public void draw(){
        stage.draw();
    }
}
