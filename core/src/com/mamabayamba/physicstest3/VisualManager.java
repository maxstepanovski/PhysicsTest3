package com.mamabayamba.physicstest3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;

/**
 * Created by макс on 28.03.2017.
 */

public class VisualManager {
    private static float statsX;
    private static float towerHeightY;
    private static float blockCounterY;
    private BitmapFont arialFont;
    private Stage stage;
    private Label.LabelStyle style;
    private Label towerHeightLabel;
    private Label blockCounterLabel;
    private Image background;

    public VisualManager(Camera camera){
        this.arialFont = new BitmapFont(Gdx.files.internal("arial16.fnt"));
        this.stage = new Stage();
        this.background = new Image(new Texture(Gdx.files.internal("sky.png")));
        Gdx.input.setInputProcessor(stage);
    }

    public void createStatisticsHUD() {
        background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        background.setPosition(0,0);
        stage.addActor(background);
        statsX = MyGdxGame.pixelsInMeters;
        towerHeightY = Gdx.graphics.getHeight()-2*MyGdxGame.pixelsInMeters;
        blockCounterY = Gdx.graphics.getHeight()-4*MyGdxGame.pixelsInMeters;
        style = new Label.LabelStyle(arialFont, Color.BLACK);
        towerHeightLabel = new Label("Tower height: ", style);
        towerHeightLabel.setPosition(statsX, towerHeightY);
        stage.addActor(towerHeightLabel);
        blockCounterLabel = new Label("Blocks placed: ", style);
        blockCounterLabel.setPosition(statsX, blockCounterY);
        stage.addActor(blockCounterLabel);
    }

    public void updateStatisticsHUD(float towerHeight, int blockCounter){
        Array<Actor> actors = stage.getActors();
        if(!actors.contains(background, false)){
            stage.addActor(background);
        }
        String a = "Tower height: "+towerHeight;
        String b = "Blocks placed: "+blockCounter;
        towerHeightLabel.setText(a);
        blockCounterLabel.setText(b);
    }

    public void updateVisuals(Array<BuildingBlock> blocks){
        Array<Actor> actors = stage.getActors();
        Gdx.app.log("happy", " "+actors.size);
        for(BuildingBlock block: blocks){
            Image image = block.getActualImage();
            if(!actors.contains(image, false)){
                stage.addActor(image);
            }
        }
    }

    public void draw(){
        stage.act();
        stage.draw();
    }

    public void dispose() {
//        stage.getActors().clear();
        stage.clear();
    }
}
