package com.lvhiei.openglestest.render;

import android.content.Context;
import android.opengl.GLES20;

import com.lvhiei.openglestest.tool.Planet;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by mj on 17-11-8.
 */


public class SolarSystemRender extends BaseRender {
    private static final String vertex_shader = "\n" +
            "attribute vec4 sun_Position;                  \n" +
            "attribute vec4 earth_Position;                  \n" +
            "attribute vec2 sun_TextureCoordinates;                  \n" +
            "attribute vec2 earth_TextureCoordinates;                  \n" +
            "\n" +
            "uniform  mat4 u_sunMatrix;                  \n" +
            "uniform  mat4 u_earthMatrix;                  \n" +
            "uniform  mat4 u_moonMatrix;                  \n" +
            "uniform  float planet_type;                  \n" +
            "                            \n" +
            "varying  vec2 v_TextureCoordinates;                  \n" +
            "varying float planet_v;                                              \n" +
            "\n" +
            "const float SUN_TYPE = 0.0;                  \n" +
            "const float EARTH_TYPE = 1.0;                  \n" +
            "const float MOON_TYPE = 2.0;                  \n" +
            "                       \n" +
            "bool isEqual(float x, float y)\n" +
            "{\n" +
            "    return abs(x - y) < 0.01;\n" +
            "}                       \n" +
            "                       \n" +
            "void main()                                   \n" +
            "{                                                         \n" +
            "    if(isEqual(planet_type, SUN_TYPE)){\n" +
            "       gl_Position = u_sunMatrix * sun_Position;\n" +
            "       v_TextureCoordinates = sun_TextureCoordinates;\n" +
            "    }else if(isEqual(planet_type, EARTH_TYPE)){\n" +
            "       gl_Position = u_earthMatrix * earth_Position;\n" +
            "       v_TextureCoordinates = earth_TextureCoordinates;\n" +
            "    }\n" +
            "    planet_v = planet_type;               \n" +
            "}"
            ;

    private static final String frag_shader = "\n" +
            "precision mediump float;                \n" +
            "                                                       \n" +
            "uniform sampler2D u_sunTextureUnit;                                              \n" +
            "uniform sampler2D u_earthTextureUnit;                                              \n" +
            "uniform sampler2D u_moonTextureUnit;                                              \n" +
            "varying vec2 v_TextureCoordinates;                                              \n" +
            "varying float planet_v;                                              \n" +
            "\n" +
            "const float SUN_TYPE = 0.0;                  \n" +
            "const float EARTH_TYPE = 1.0;                  \n" +
            "const float MOON_TYPE = 2.0;                  \n" +
            "\n" +
            "bool isEqual(float x, float y)\n" +
            "{\n" +
            "    return abs(x - y) < 0.01;\n" +
            "}                       \n" +
            "                 \n" +
            "void main()                                        \n" +
            "{   \n" +
            "    if(isEqual(planet_v, SUN_TYPE))\n" +
            "    {\n" +
            "        gl_FragColor = texture2D(u_sunTextureUnit, v_TextureCoordinates);                                                        \n" +
            "    }\n" +
            "    else if(isEqual(planet_v, EARTH_TYPE))\n" +
            "    {\n" +
            "        gl_FragColor = texture2D(u_earthTextureUnit, v_TextureCoordinates);                                                        \n" +
            "    }\n" +
            "    else if(isEqual(planet_v, MOON_TYPE))\n" +
            "    {\n" +
            "        gl_FragColor = texture2D(u_moonTextureUnit, v_TextureCoordinates);                                                        \n" +
            "    }else\n" +
            "    {\n" +
            "       gl_FragColor = vec4(0.0, 1.0, 0.0, 1.0);\n" +
            "    }                                           \n" +
            "}    "
            ;


    protected final int angleSpan = 10;// 将球进行单位切分的角度

    protected float sun_r = 0.4f;       // 太阳半径
    protected float earth_r = 0.05f;    // 地球半径
    protected float moon_r = 0.05f;     // 月亮半径

    protected float mEarthTrackRadius = 0.8f; // 地球中心距离太阳中心距离
    protected float distance_em = 0.3f; // 月球中心距离地球中心距离

    protected Context mContext;

    protected final int neTriangleCount = 3000;                       // 地球公转轨迹的三角个数
    protected final int nmTriangleCount = 250;                       // 月亮公转轨迹的三角个数


    protected int mEarthRotateAngle = 15;       // 地球自传角度
    protected Object mLock = new Object();
    protected boolean mWantStop = false;

    protected int mDrawSep = 20;

    protected boolean mbStartedDraw = false;

    protected Thread mRendererThread;
    protected Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            synchronized (mLock){
                while (!mWantStop){
                    try {
                        draw();
                        mLock.wait(mDrawSep);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    private String[][] mPlanetParams = {
            {"sun.bmp", "u_sunTextureUnit", "u_sunMatrix", "sun_Position", "sun_TextureCoordinates", "planet_type", },
            {"earth.bmp", "u_earthTextureUnit", "u_earthMatrix", "earth_Position", "earth_TextureCoordinates", "planet_type", },
    };

    private Planet mSun;
    private Planet mEarth;

    public SolarSystemRender(Context context) {
        super();
        mContext = context;

        int row = 0;
        mSun = createSun(row, row, sun_r, angleSpan);
        ++row;
        mEarth = createPlanet(row, row, earth_r, angleSpan, mEarthTrackRadius, neTriangleCount, mEarthRotateAngle);

        initAllCoordinate();

        initAllTracks();
    }

    protected Planet createPlanet(int pos, int row, float r, int angleSpan, float t_r, int triangleCount, int rotateDegree){
        int colume = 0;
        return new Planet(mContext, pos, pos,
                mPlanetParams[row][colume++], mPlanetParams[row][colume++],
                mPlanetParams[row][colume++], mPlanetParams[row][colume++],
                mPlanetParams[row][colume++], mPlanetParams[row][colume++], r, angleSpan, t_r, triangleCount, rotateDegree);
    }

    protected Planet createSun(int pos, int row, float r, int angleSpan){
        int colume = 0;
        return new Planet(mContext, pos, pos,
                mPlanetParams[row][colume++], mPlanetParams[row][colume++],
                mPlanetParams[row][colume++], mPlanetParams[row][colume++],
                mPlanetParams[row][colume++], mPlanetParams[row][colume++], r, angleSpan);
    }

    protected void initAllTracks(){
        // x y
        float x = 0.0f;
        float y = 0.0f;
        float z = 0.0f;

        mEarth.initTracks(x, y, z);
    }

    protected void initAllCoordinate() {
        mSun.initCoordinates();
        mEarth.initCoordinates();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        mProgram = OpenGLUtils.loadProgram(vertex_shader, frag_shader);
        GLES20.glUseProgram(mProgram);
//        GLES20.glDepthRangef(20, 100);
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        mSun.onSurfaceCreated(gl, config, mProgram);
        mEarth.onSurfaceCreated(gl, config, mProgram);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        super.onSurfaceChanged(gl, width, height);

        mSun.onSurfaceChanged(gl, width, height);
        mEarth.onSurfaceChanged(gl, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT|GLES20.GL_DEPTH_BUFFER_BIT);

        mSun.onDrawFrame(gl);
        mEarth.onDrawFrame(gl);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

        mbStartedDraw = true;
    }

    @Override
    protected void localReleaseGL() {
        super.localReleaseGL();
        mSun.releaseGL();
        mEarth.releaseGL();
    }

    @Override
    public void onPause() {
        mWantStop = true;
        synchronized (mLock){
            mLock.notify();
        }

        try {
            mRendererThread.join();
            mRendererThread = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mbStartedDraw = false;

        if(null == mRendererThread){
            mWantStop = false;
            mRendererThread = new Thread(mRunnable);
            mRendererThread.start();
        }
    }

    protected void draw(){
        if(!mbStartedDraw){
            return;
        }

        if(null != mSurfaceView){
            mSurfaceView.requestRender();
        }
    }

}
