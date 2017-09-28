package com.lvhiei.openglestest.render;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by mj on 17-9-27.
 */


public class BaseRender implements IGLESRenderer {
    protected GLSurfaceView mSurfaceView;

    protected int mProgram = 0;

    protected MatrixUtil mMatrixUtil = new MatrixUtil();

    @Override
    public void setGLSurface(GLSurfaceView surface) {
        mSurfaceView = surface;
    }

    @Override
    public void onPause() {
        mSurfaceView.queueEvent(new Runnable() {
            @Override
            public void run() {
                localReleaseGL();
                if(0 != mProgram){
                    GLES20.glDeleteProgram(mProgram);
                }
                mProgram = 0;
            }
        });
        mSurfaceView.onPause();
    }

    @Override
    public void onResume() {
        mSurfaceView.onResume();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {

    }

    protected void localReleaseGL(){

    }

    public MatrixUtil getMatrixUtil(){
        return mMatrixUtil;
    }
}
