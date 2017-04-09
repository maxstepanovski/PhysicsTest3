package com.mamabayamba.physicstest3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.utils.Array;

/**
 * Created by макс on 09.04.2017.
 */

public class EffectManager {
    private static final String EFFECTS_DIRECTORY = "";
    private static final String DUST = "dust.p";
    private Array<ParticleEffectPool.PooledEffect> effects;
    private ParticleEffectPool dustEffectPool;
    private ParticleEffect dustEffect;

    public EffectManager() {
        effects = new Array<ParticleEffectPool.PooledEffect>();
        dustEffect = new ParticleEffect();
        dustEffect.load(Gdx.files.internal(DUST), Gdx.files.internal(EFFECTS_DIRECTORY));
        dustEffectPool = new ParticleEffectPool(dustEffect, 1, 20);
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
}
