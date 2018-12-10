package com.lvhiei.openglestest;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.lvhiei.openglestest.render.BallRender;
import com.lvhiei.openglestest.render.BaseRender;
import com.lvhiei.openglestest.render.CircleRender;
import com.lvhiei.openglestest.render.ColorCubeRender;
import com.lvhiei.openglestest.render.CubeRender;
import com.lvhiei.openglestest.render.FirstProgramRender;
import com.lvhiei.openglestest.render.IGLESRenderer;
import com.lvhiei.openglestest.render.MatrixUtil;
import com.lvhiei.openglestest.render.RotateCubeRender;
import com.lvhiei.openglestest.render.ScaleCubeRender;
import com.lvhiei.openglestest.render.SolarSystemRender;
import com.lvhiei.openglestest.render.SquareRender;
import com.lvhiei.openglestest.render.SunRender;
import com.lvhiei.openglestest.render.TestBlitFrameBufferRender;
import com.lvhiei.openglestest.render.TextureBallRender;
import com.lvhiei.openglestest.render.TextureCubeRender;
import com.lvhiei.openglestest.render.TextureSquareRender;
import com.lvhiei.openglestest.render.TranstateCubeRender;
import com.lvhiei.openglestest.render.TriangleRender;

public class GLES20Activity extends Activity {

    private int mRenderId;
    private GLSurfaceView mSurfaceView;
    private BaseRender mRender;
    private boolean mbLandScape;

    private TextView mTextview;
    private String mText = "";

    private float mPreviousX = 0;
    private float mPreviousY = 0;
    private float mPreviousZ = 0;
    private int mScaledCount = 0;
    private boolean mbReduce = true;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()){
                case MotionEvent.ACTION_MOVE:
                {
                    if(mRenderId == R.id.btn_translateColorCube){
                        float dx = x - mPreviousX;
                        float dy = y - mPreviousY;
                        float tx = dx > 0 ? 0.1f : dx == 0 ? 0.0f : -0.1f;
                        float ty = dy > 0 ? 0.1f : dy == 0 ? 0.0f : -0.1f;

                        mPreviousX = x;
                        mPreviousY = y;

                        mRender.getMatrixUtil().setTranstate(0.0f, ty, 0.0f);
//                        mRender.getMatrixUtil().setTranstate(0.0f, 0.0f, ty);
                        mSurfaceView.requestRender();
                    }
                    break;
                }
                case MotionEvent.ACTION_DOWN:
                {
                    if(mRenderId == R.id.btn_rotateColorCube){
                        mRender.getMatrixUtil().setRotate(30, 0.0f, 1.0f, 0.0f);
                        mSurfaceView.requestRender();
                    }else if(mRenderId == R.id.btn_scaleColorCube){
                        if(++mScaledCount % 5 == 0){
                            mbReduce = !mbReduce;
                        }

                        float scalex = 0.8f;

                        if(!mbReduce){
                            scalex = (float) (1.0 / scalex);
                        }

                        mRender.getMatrixUtil().setScale(scalex, scalex, scalex);
                        mRender.getMatrixUtil().setRotate(30f, 0.0f, 1.0f, 0.0f);
                        mSurfaceView.requestRender();
                    }else if(mRenderId == R.id.btn_drawBall){
                        mRender.getMatrixUtil().setRotate(30, 0.0f, 1.0f, 0.0f);
                        mSurfaceView.requestRender();
                    }else if(mRenderId == R.id.btn_textureCube){
                        mRender.getMatrixUtil().setRotate(30, 0.0f, 1.0f, 0.0f);
                        mSurfaceView.requestRender();
                    }else if(mRenderId == R.id.btn_textureBall){
                        mRender.getMatrixUtil().setRotate(30, 0.0f, 1.0f, 0.0f);
                        mSurfaceView.requestRender();
                    }else if(mRenderId == R.id.btn_drawSolarSystem){
                        ((SolarSystemRender)mRender).addEyeY();
                        genText();
                    }else if(mRenderId == R.id.btn_textureSquare){
                        mRender.getMatrixUtil().setRotate(5, 0, 0, 1);
                        mSurfaceView.requestRender();
                    }
                    break;
                }
            }


            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gles20);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mbLandScape = false;

        Bundle bundle = getIntent().getExtras();
        mRenderId = bundle.getInt("renderid");

        mTextview = (TextView) findViewById(R.id.tv_showEyes);

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
        mSurfaceView.setOnTouchListener(mTouchListener);
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
//                mbLandScape = true;
                break;
            case R.id.btn_drawColorCube:
                mRender = new ColorCubeRender();
//                mbLandScape = true;
                break;
            case R.id.btn_translateColorCube:
                mRender = new TranstateCubeRender();
//                mbLandScape = true;
                break;
            case R.id.btn_rotateColorCube:
                mRender = new RotateCubeRender();
//                mbLandScape = true;
                break;
            case R.id.btn_scaleColorCube:
                mRender = new ScaleCubeRender();
//                mbLandScape = true;
                break;
            case R.id.btn_drawBall:
                mRender = new BallRender();
//                mbLandScape = true;
                break;

            case R.id.btn_textureSquare:
                mRender = new TextureSquareRender(this);
//                mbLandScape = true;
                break;
            case R.id.btn_testBlitFrameBuffer:
                mRender = new TestBlitFrameBufferRender(this);
//                mbLandScape = true;
                break;
            case R.id.btn_textureCube:
                mRender = new TextureCubeRender(this);
//                mbLandScape = true;
                break;
            case R.id.btn_textureBall:
                mRender = new TextureBallRender(this);
                mbLandScape = true;
                break;
            case R.id.btn_drawSun:
                mRender = new SunRender(this);
                mbLandScape = true;
                break;
            case R.id.btn_drawSolarSystem:
                mRender = new SolarSystemRender(this);
                genText();
                mbLandScape = true;
                break;
            default:
                mRender = new FirstProgramRender();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRender.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mRender.onPause();
    }

    protected void genText(){
        SolarSystemRender render = (SolarSystemRender) mRender;
        if(render == null){
            return;
        }
        mText = String.format("eye:(%.0f,%.0f,%.0f),nf:(%.0f,%.0f)", render.getEyeX(), render.getEyeY(), render.getEyeZ(), render.getNear(), render.getFar());
        mTextview.setText(mText);
        mTextview.setVisibility(View.VISIBLE);
    }
}
