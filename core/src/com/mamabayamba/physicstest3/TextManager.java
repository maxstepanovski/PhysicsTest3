package com.mamabayamba.physicstest3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by макс on 09.04.2017.
 */

public class TextManager {
    private static final String PREFERENCES = "myPreferences";
    private static final String HEIGHT_RECORD = "heightRecord";
    private static final String HEIGHT_RECORD_LABEL = "Height record: ";
    private static final String HEIGHT_LABEL = "Tower height: ";
    private static final String BLOCK_COUNT_LABEL = "Blocks: ";
    private static final String BLOCKS = " blocks stacked!";
    private EffectManager effectManager;
    private BitmapFont mainFont, giantFont;
    private GlyphLayout towerHeight, blocks, heightRecord, temporarySign;
    private float padding;
    private float record;
    private Preferences prefs;
    private Timer blockTimer;
    private int currentBlocks;

    public TextManager() {
        effectManager = EffectManager.getEffectManager();
        padding = MyGdxGame.pixelsInMeters;
        mainFont = new BitmapFont(Gdx.files.internal("gamefont.fnt"));
        giantFont = new BitmapFont(Gdx.files.internal("giantfont.fnt"));
        towerHeight = new GlyphLayout();
        blocks = new GlyphLayout();
        heightRecord = new GlyphLayout();
        temporarySign = new GlyphLayout();
        prefs = Gdx.app.getPreferences(PREFERENCES);
        blockTimer = new Timer();
        currentBlocks = 0;
    }

    public void drawText(SpriteBatch batch, float height, int blockCount){
        updateStatistics(height, blockCount);
        mainFont.draw(batch, towerHeight, padding, Gdx.graphics.getHeight()-padding);
        mainFont.draw(batch, blocks, padding, Gdx.graphics.getHeight()-2*padding-towerHeight.height);
        mainFont.draw(batch, heightRecord, padding, Gdx.graphics.getHeight()-3*padding-towerHeight.height-blocks.height);

        if(blockTimer.isRunning() && blockTimer.getElapsedTime() < 3){
            giantFont.draw(
                    batch,
                    temporarySign,
                    Gdx.graphics.getWidth()/2-temporarySign.width/2,
                    Gdx.graphics.getHeight()/2-temporarySign.height/2);
        }else{
            blockTimer.reset();
        }
    }

    private void updateStatistics(float height, int blockCount){
        record = getRecord();
        if(record > height){
            heightRecord.setText(mainFont, HEIGHT_RECORD_LABEL+round(record, 2));
        }else{
            heightRecord.setText(mainFont, HEIGHT_RECORD_LABEL+round(height, 2));
            prefs.putFloat(HEIGHT_RECORD, (float) round(height, 2));
            prefs.flush();
        }
        towerHeight.setText(mainFont, HEIGHT_LABEL+round(height, 2));
        blocks.setText(mainFont, BLOCK_COUNT_LABEL+blockCount);
    }


    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private float getRecord(){
        return prefs.getFloat(HEIGHT_RECORD, 0f);
    }

    public void setBlockSign(int blocks){
        if(blocks != currentBlocks){
            blockTimer.start();
            temporarySign.setText(giantFont, blocks+BLOCKS);
            currentBlocks = blocks;
            effectManager.addFireworksEffectToPool();
        }
    }

}
