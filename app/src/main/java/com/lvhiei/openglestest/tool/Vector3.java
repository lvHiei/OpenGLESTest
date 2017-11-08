package com.lvhiei.openglestest.tool;

/**
 * Created by mj on 17-11-8.
 */


public class Vector3 {
    public Vector3(){
        this(0.0f, 0.0f, 0.0f);
    }

    public Vector3(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float x;
    public float y;
    public float z;
}
