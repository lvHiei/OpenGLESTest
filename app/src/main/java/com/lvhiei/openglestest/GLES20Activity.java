package com.lvhiei.openglestest;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.WindowManager;

import com.lvhiei.openglestest.render.FirstProgramRender;
import com.lvhiei.openglestest.render.IGLESRenderer;
import com.lvhiei.openglestest.render.TriangleRender;

public class GLES20Activity extends Activity {

    private int mRenderId;
    private GLSurfaceView mSurfaceView;
    private IGLESRenderer mRender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gles20);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Bundle bundle = getIntent().getExtras();
        mRenderId = bundle.getInt("renderid");

        initGLSurfaceView();
    }

    private void initGLSurfaceView(){
        mSurfaceView = (GLSurfaceView) findViewById(R.id.myGLSurface);

        mSurfaceView.setEGLContextClientVersion(2);
        createRender();
        mSurfaceView.setRenderer(mRender);
        mSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        mRender.setGLSurface(mSurfaceView);
    }

    private void createRender() {
        switch (mRenderId){
            case R.id.btn_firstGLESPrograme:
                mRender = new FirstProgramRender();
                break;
            case R.id.btn_drawTriangle:
                mRender = new TriangleRender();
                break;
            default:
                mRender = new FirstProgramRender();
                break;
        }
    }
}
