package com.mamabayamba.physicstest3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by макс on 09.04.2017.
 */

public class TextManager {
    private BitmapFont plainFont;
    private GlyphLayout towerHeight, blocks;
    private float padding;

    public TextManager() {
        padding = MyGdxGame.pixelsInMeters;
        plainFont = new BitmapFont(Gdx.files.internal("arial16.fnt"));
        plainFont.setColor(Color.BLACK);
        towerHeight = new GlyphLayout();
        blocks = new GlyphLayout();
    }

    public void updateStatistics(float height, int blockCount){
        towerHeight.setText(plainFont, "Tower height: "+round(height, 2));
        blocks.setText(plainFont, "Blocks: "+blockCount);
    }

    public void drawStatistics(SpriteBatch batch){
        plainFont.draw(batch, towerHeight, padding, Gdx.graphics.getHeight()-padding);
        plainFont.draw(batch, blocks, padding, Gdx.graphics.getHeight()-2*padding-towerHeight.height);
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
