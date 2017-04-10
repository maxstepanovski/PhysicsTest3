package com.mamabayamba.physicstest3;

/**
 * Created by макс on 10.04.2017.
 */

public class Timer {
    private long startingTimeMillis;
    private boolean isRunning;

    public Timer() {
        this.startingTimeMillis = 0;
        isRunning = false;
    }

    public void start(){
        if(!isRunning){
            startingTimeMillis = System.currentTimeMillis();
            isRunning = true;
        }
    }

    public long getElapsedTime(){
        return (System.currentTimeMillis() - startingTimeMillis)/1000;
    }

    public void reset(){
        startingTimeMillis = 0;
        isRunning = false;
    }

    public boolean isRunning() {
        return isRunning;
    }
}
