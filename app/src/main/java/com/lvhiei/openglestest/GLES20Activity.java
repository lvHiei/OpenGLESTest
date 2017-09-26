package com.lvhiei.openglestest;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.WindowManager;

import com.lvhiei.openglestest.render.CircleRender;
import com.lvhiei.openglestest.render.ColorCubeRender;
import com.lvhiei.openglestest.render.CubeRender;
import com.lvhiei.openglestest.render.FirstProgramRender;
import com.lvhiei.openglestest.render.IGLESRenderer;
import com.lvhiei.openglestest.render.SquareRender;
import com.lvhiei.openglestest.render.TriangleRender;

public class GLES20Activity extends Activity {

    private int mRenderId;
    private GLSurfaceView mSurfaceView;
    private IGLESRenderer mRender;
    private boolean mbLandScape;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gles20);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mbLandScape = false;

        Bundle bundle = getIntent().getExtras();
        mRenderId = bundle.getInt("renderid");

        initGLSurfaceView();

        if(mbLandScape){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
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
            case R.id.btn_drawSquare:
                mRender = new SquareRender();
                break;
            case R.id.btn_drawCircle:
                mRender = new CircleRender();
                break;
            case R.id.btn_drawCube:
                mRender = new CubeRender();
                mbLandScape = true;
                break;
            case R.id.btn_drawColorCube:
                mRender = new ColorCubeRender();
                mbLandScape = true;
                break;
            default:
                mRender = new FirstProgramRender();
                break;
        }
    }
}
