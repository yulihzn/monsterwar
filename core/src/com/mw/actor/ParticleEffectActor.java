package com.mw.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by hezhengnan on 2015/12/3.
 */
public class ParticleEffectActor extends Actor {
    private ParticleEffect effect;
    private float ppX=0,ppY=0;

    public float getPpX() {
        return ppX;
    }

    public void setPpX(float positionX) {
        this.ppX = positionX;
    }

    public float getPpY() {
        return ppY;
    }

    public void setPpY(float positionY) {
        this.ppY = positionY;
    }
    public void setPposition(float positionX,float positionY) {
        this.ppY = positionY;
        this.ppX = positionX;
    }

    public ParticleEffectActor(ParticleEffect effect) {
        this.effect = effect;
    }

    public ParticleEffect getEffect() {
        return effect;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        effect.draw(batch);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        effect.setPosition(ppX,ppY);
        effect.update(delta);

    }
}
