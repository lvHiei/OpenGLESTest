package com.lvhiei.openglestest.tool;

import com.lvhiei.openglestest.render.SunRender;

/**
 * Created by mj on 17-11-8.
 */


public class Point{
    public Point(Point p){
        this.x = p.x;
        this.y = p.y;
        this.z = p.z;
    }

    public Point(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void add(Point p){
        this.x += p.x;
        this.y += p.y;
        this.z += p.z;
    }

    public void minus(Point p){
        this.x -= p.x;
        this.y -= p.y;
        this.z -= p.z;
    }

    public void cp(Point p){
        this.x = p.x;
        this.y = p.y;
        this.z = p.z;
    }

    public float x;
    public float y;
    public float z;
}
