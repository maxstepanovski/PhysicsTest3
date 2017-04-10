package com.mamabayamba.physicstest3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.utils.Array;

/**
 * Created by макс on 09.04.2017.
 */

public class EffectManager {
    private static final String EFFECTS_DIRECTORY = "";
    private static final String DUST = "dust.p";
    private static final String FIREWORKS = "fireworks.p";
    private Array<ParticleEffectPool.PooledEffect> effects;
    private ParticleEffectPool dustEffectPool, fireworksEffectPool;
    private ParticleEffect dustEffect, fireworksEffect;
    private Vector2[] fireworksPositions = {
            new Vector2(Gdx.graphics.getWidth()/4, Gdx.graphics.getHeight()/4),
            new Vector2(Gdx.graphics.getWidth()/4, (Gdx.graphics.getHeight()/4)*3),
            new Vector2((Gdx.graphics.getWidth()/4)*3, (Gdx.graphics.getHeight()/4)*3),
            new Vector2((Gdx.graphics.getWidth()/4)*3, (Gdx.graphics.getHeight()/4)),
    };
    private static boolean isCreated;
    private static EffectManager manager;

    public static EffectManager getEffectManager(){
        if(isCreated){
            return manager;
        }else{
            manager = new EffectManager();
            return manager;
        }
    }

    private EffectManager() {
        effects = new Array<ParticleEffectPool.PooledEffect>();
        dustEffect = new ParticleEffect();
        dustEffect.load(Gdx.files.internal(DUST), Gdx.files.internal(EFFECTS_DIRECTORY));
        dustEffectPool = new ParticleEffectPool(dustEffect, 1, 20);
        fireworksEffect = new ParticleEffect();
        fireworksEffect.load(Gdx.files.internal(FIREWORKS), Gdx.files.internal(EFFECTS_DIRECTORY));
        fireworksEffectPool = new ParticleEffectPool(fireworksEffect, 1, 5);
        isCreated = true;
    }

    public void drawEffects(SpriteBatch batch){
        for (int i = 0; i < effects.size; i++) {
            ParticleEffectPool.PooledEffect effect = effects.get(i);
            effect.draw(batch, 1/60f);
            if (effect.isComplete()) {
                effect.free();
                effects.removeIndex(i);
            }
        }
    }

    public void addDustEffectToPool(Contact contact, Camera camera){
        int contactCount = contact.getWorldManifold().getNumberOfContactPoints();
        for(int i=0;i<contactCount;++i){
            Vector3 contactPosition = new Vector3(contact.getWorldManifold().getPoints()[i].x, contact.getWorldManifold().getPoints()[i].y, 0);
            Vector3 projected = camera.project(contactPosition);
            ParticleEffectPool.PooledEffect effect = dustEffectPool.obtain();
            effect.setPosition(projected.x, projected.y);
            effects.add(effect);
        }
    }

    public void addFireworksEffectToPool(){
        for(int i=0; i < 4; ++i){
            ParticleEffectPool.PooledEffect effect = fireworksEffectPool.obtain();
            effect.setPosition(fireworksPositions[i].x, fireworksPositions[i].y);
            effects.add(effect);
        }
    }
}
