package com.mw.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by hezhengnan on 2015/6/16.
 */
public class CameraController implements GestureDetector.GestureListener{
    private float velX, velY;
    private boolean flinging = false;
    private float initialScale = 1;
    private OrthographicCamera camera;
    private float tapX=-1,tapY=-1;
    public CameraController(OrthographicCamera camera){
        this.camera = camera;
    }
    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        flinging = false;
        initialScale = camera.zoom;
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        Gdx.app.log("GestureDetectorTest", "tap at " + x + ", " + y + ", count: " + count);
        tapX = x;
        tapY = y;
        if(onTouchListener != null){
            onTouchListener.onTap(x,y);
        }
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        Gdx.app.log("GestureDetectorTest", "long press at " + x + ", " + y);
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        Gdx.app.log("GestureDetectorTest", "fling " + velocityX + ", " + velocityY);
        flinging = true;
        velX = camera.zoom * velocityX * 0.5f;
        velY = camera.zoom * velocityY * 0.5f;
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        camera.position.add(-deltaX * camera.zoom, deltaY * camera.zoom, 0);
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        Gdx.app.log("GestureDetectorTest", "pan stop at " + x + ", " + y);
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        float ratio = initialDistance / distance;
        camera.zoom = initialScale * ratio;
        System.out.println(camera.zoom);
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    public void update () {
        if (flinging) {
            velX *= 0.98f;
            velY *= 0.98f;
            camera.position.add(-velX * Gdx.graphics.getDeltaTime(), velY * Gdx.graphics.getDeltaTime(), 0);
            if (Math.abs(velX) < 0.01f) velX = 0;
            if (Math.abs(velY) < 0.01f) velY = 0;
        }
    }

    public float getTapX() {
        return tapX;
    }

    public float getTapY() {
        return tapY;
    }

    public interface OnTouchListener{
        void onTap(float x, float y);
    }
    private OnTouchListener onTouchListener;

    public void setOnTouchListener(OnTouchListener onTouchListener) {
        this.onTouchListener = onTouchListener;
    }
}
