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
            "attribute vec4 mercury_Position;                  \n" +
            "attribute vec4 venus_Position;                  \n" +
            "attribute vec4 earth_Position;                  \n" +
            "attribute vec4 mars_Position;                  \n" +
            "attribute vec4 jupiter_Position;                  \n" +
            "attribute vec4 saturn_Position;                  \n" +
            "attribute vec4 uranus_Position;                  \n" +
//            "attribute vec4 _Position;                  \n" +
            "attribute vec2 sun_TextureCoordinates;                  \n" +
            "attribute vec2 mercury_TextureCoordinates;                  \n" +
            "attribute vec2 venus_TextureCoordinates;                  \n" +
            "attribute vec2 earth_TextureCoordinates;                  \n" +
            "attribute vec2 mars_TextureCoordinates;                  \n" +
            "attribute vec2 jupiter_TextureCoordinates;                  \n" +
            "attribute vec2 saturn_TextureCoordinates;                  \n" +
            "attribute vec2 uranus_TextureCoordinates;                  \n" +
//            "attribute vec2 _TextureCoordinates;                  \n" +
            "\n" +
            "uniform  mat4 u_sunMatrix;                  \n" +
            "uniform  mat4 u_mercuryMatrix;                  \n" +
            "uniform  mat4 u_venusMatrix;                  \n" +
            "uniform  mat4 u_earthMatrix;                  \n" +
            "uniform  mat4 u_marsMatrix;                  \n" +
            "uniform  mat4 u_jupiterMatrix;                  \n" +
            "uniform  mat4 u_saturnMatrix;                  \n" +
            "uniform  mat4 u_uranusMatrix;                  \n" +
//            "uniform  mat4 u_Matrix;                  \n" +
            "uniform  mat4 u_moonMatrix;                  \n" +
            "uniform  float planet_type;                  \n" +
            "                            \n" +
            "varying  vec2 v_TextureCoordinates;                  \n" +
            "varying float planet_v;                                              \n" +
            "\n" +
            "const float SUN_TYPE = 0.0;                  \n" +
            "const float MERCURY_TYPE = 1.0;                  \n" +
            "const float VENUS_TYPE = 2.0;                  \n" +
            "const float EARTH_TYPE = 3.0;                  \n" +
            "const float MARS_TYPE = 4.0;                  \n" +
            "const float JUPITER_TYPE = 5.0;                  \n" +
            "const float SATURN_TYPE = 6.0;                  \n" +
            "const float URANUS_TYPE = 7.0;                  \n" +
            "                       \n" +
            "bool isEqual(float x, float y)\n" +
            "{\n" +
            "    return abs(x - y) < 0.01;\n" +
            "}                       \n" +
            "                       \n" +
            "void main()                                   \n" +
            "{                                                         \n" +
            "    if(isEqual(planet_type, SUN_TYPE))\n" +
            "    {\n" +
            "       gl_Position = u_sunMatrix * sun_Position;\n" +
            "       v_TextureCoordinates = sun_TextureCoordinates;\n" +
            "    }\n" +
            "    else if(isEqual(planet_type, MERCURY_TYPE))\n" +
            "    {\n" +
            "       gl_Position = u_mercuryMatrix * mercury_Position;\n" +
            "       v_TextureCoordinates = mercury_TextureCoordinates;\n" +
            "    }\n" +
            "    else if(isEqual(planet_type, VENUS_TYPE))\n" +
            "    {\n" +
            "       gl_Position = u_venusMatrix * venus_Position;\n" +
            "       v_TextureCoordinates = venus_TextureCoordinates;\n" +
            "    }\n" +
            "    else if(isEqual(planet_type, EARTH_TYPE))\n" +
            "    {\n" +
            "       gl_Position = u_earthMatrix * earth_Position;\n" +
            "       v_TextureCoordinates = earth_TextureCoordinates;\n" +
            "    }\n" +
            "    else if(isEqual(planet_type, MARS_TYPE))\n" +
            "    {\n" +
            "       gl_Position = u_marsMatrix * mars_Position;\n" +
            "       v_TextureCoordinates = mars_TextureCoordinates;\n" +
            "    }\n" +
            "    else if(isEqual(planet_type, JUPITER_TYPE))\n" +
            "    {\n" +
            "       gl_Position = u_jupiterMatrix * jupiter_Position;\n" +
            "       v_TextureCoordinates = jupiter_TextureCoordinates;\n" +
            "    }\n" +
            "    else if(isEqual(planet_type, SATURN_TYPE))\n" +
            "    {\n" +
            "       gl_Position = u_saturnMatrix * saturn_Position;\n" +
            "       v_TextureCoordinates = saturn_TextureCoordinates;\n" +
            "    }\n" +
            "    else if(isEqual(planet_type, URANUS_TYPE))\n" +
            "    {\n" +
            "       gl_Position = u_uranusMatrix * uranus_Position;\n" +
            "       v_TextureCoordinates = uranus_TextureCoordinates;\n" +
            "    }\n" +
            "    planet_v = planet_type;               \n" +
            "}"
            ;

    private static final String frag_shader = "\n" +
            "precision mediump float;                \n" +
            "                                                       \n" +
            "uniform sampler2D u_sunTextureUnit;                                              \n" +
            "uniform sampler2D u_mercuryTextureUnit;                                              \n" +
            "uniform sampler2D u_venusTextureUnit;                                              \n" +
            "uniform sampler2D u_earthTextureUnit;                                              \n" +
            "uniform sampler2D u_marsTextureUnit;                                              \n" +
            "uniform sampler2D u_jupiterTextureUnit;                                              \n" +
            "uniform sampler2D u_saturnTextureUnit;                                              \n" +
            "uniform sampler2D u_uranusTextureUnit;                                              \n" +
//            "uniform sampler2D u_TextureUnit;                                              \n" +
            "uniform sampler2D u_moonTextureUnit;                                              \n" +
            "varying vec2 v_TextureCoordinates;                                              \n" +
            "varying float planet_v;                                              \n" +
            "\n" +
            "const float SUN_TYPE = 0.0;                  \n" +
            "const float MERCURY_TYPE = 1.0;                  \n" +
            "const float VENUS_TYPE = 2.0;                  \n" +
            "const float EARTH_TYPE = 3.0;                  \n" +
            "const float MARS_TYPE = 4.0;                  \n" +
            "const float JUPITER_TYPE = 5.0;                  \n" +
            "const float SATURN_TYPE = 6.0;                  \n" +
            "const float URANUS_TYPE = 7.0;                  \n" +
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
            "    else if(isEqual(planet_v, MERCURY_TYPE))\n" +
            "    {\n" +
            "        gl_FragColor = texture2D(u_mercuryTextureUnit, v_TextureCoordinates);                                                        \n" +
            "    }\n" +
            "    else if(isEqual(planet_v, VENUS_TYPE))\n" +
            "    {\n" +
            "        gl_FragColor = texture2D(u_venusTextureUnit, v_TextureCoordinates);                                                        \n" +
            "    }\n" +
            "    else if(isEqual(planet_v, EARTH_TYPE))\n" +
            "    {\n" +
            "        gl_FragColor = texture2D(u_earthTextureUnit, v_TextureCoordinates);                                                        \n" +
            "    }\n" +
            "    else if(isEqual(planet_v, MARS_TYPE))\n" +
            "    {\n" +
            "        gl_FragColor = texture2D(u_marsTextureUnit, v_TextureCoordinates);                                                        \n" +
            "    }\n" +
            "    else if(isEqual(planet_v, JUPITER_TYPE))\n" +
            "    {\n" +
            "        gl_FragColor = texture2D(u_jupiterTextureUnit, v_TextureCoordinates);                                                        \n" +
            "    }\n" +
            "    else if(isEqual(planet_v, SATURN_TYPE))\n" +
            "    {\n" +
            "        gl_FragColor = texture2D(u_saturnTextureUnit, v_TextureCoordinates);                                                        \n" +
            "    }\n" +
            "    else if(isEqual(planet_v, URANUS_TYPE))\n" +
            "    {\n" +
            "        gl_FragColor = texture2D(u_uranusTextureUnit, v_TextureCoordinates);                                                        \n" +
            "    }\n" +
//            "    else if(isEqual(planet_v, MOON_TYPE))\n" +
//            "    {\n" +
//            "        gl_FragColor = texture2D(u_moonTextureUnit, v_TextureCoordinates);                                                        \n" +
//            "    }\n" +
            "    else\n" +
            "    {\n" +
            "       gl_FragColor = vec4(0.0, 1.0, 0.0, 1.0);\n" +
            "    }                                           \n" +
            "}    "
            ;


    protected static final int angleSpan = 10;// 将球进行单位切分的角度

    // 行星半径
    protected static final float SUN_RADIUS = 0.4f;                // 太阳半径
    protected static final float MERCURY_RADIUS = 0.02f;           // 水星半径
    protected static final float VENUS_RADIUS = 0.03f;             // 金星半径
    protected static final float EARTH_RADIUS = 0.05f;             // 地球半径
    protected static final float MARS_RADIUS = 0.065f;             // 火星半径
    protected static final float JUPITER_RADIUS = 0.1f;            // 木星半径
    protected static final float SATURN_RADIUS = 0.085f;           // 土星半径
    protected static final float URANUS_RADIUS = 0.07f;            // 天王星半径

    // 行星公转半径
    protected static final float MECURY_TRACK_RADIUS = 0.5f;       // 水星中心距离太阳中心距离
    protected static final float VENUS_TRACK_RADIUS = 0.65f;       // 金星中心距离太阳中心距离
    protected static final float EARTH_TRACK_RADIUS = 0.8f;        // 地球中心距离太阳中心距离
    protected static final float MARS_TRACK_RADIUS = 0.9f;         // 火星中心距离太阳中心距离
    protected static final float JUPITER_TRACK_RADIUS = 1.1f;      // 木星中心距离太阳中心距离
    protected static final float SATURN_TRACK_RADIUS = 1.25f;      // 土星中心距离太阳中心距离
    protected static final float URANUS_TRACK_RADIUS = 1.4f;       // 天王星中心距离太阳中心距离

    // 行星公转速度(越大越慢)
    protected static final int MERCURY_TRIANGLE_COUNT = 300;       // 水星公转轨迹的三角个数
    protected static final int VENUS_TRIANGLE_COUNT = 800;         // 金星公转轨迹的三角个数
    protected static final int EARTH_TRIANGLE_COUNT = 1500;        // 地球公转轨迹的三角个数
    protected static final int MARS_TRIANGLE_COUNT = 1750;         // 火星公转轨迹的三角个数
    protected static final int JUPITER_TRIANGLE_COUNT = 2000;      // 木星公转轨迹的三角个数
    protected static final int SATURN_TRIANGLE_COUNT = 2300;       // 土星公转轨迹的三角个数
    protected static final int URANUS_TRIANGLE_COUNT = 2800;       // 天王星公转轨迹的三角个数

    // 行星自传速度(越大越快)
    protected static final int MERCURY_ROTATE_ANGLE = 40;          // 水星自传角度
    protected static final int VENUS_ROTATE_ANGLE = 35;            // 水星自传角度
    protected static final int EARTH_ROTATE_ANGLE = 30;            // 地球自传角度
    protected static final int MARS_ROTATE_ANGLE = 28;             // 火星自传角度
    protected static final int JUPITER_ROTATE_ANGLE = 25;          // 木星自传角度
    protected static final int SATURN_ROTATE_ANGLE = 23;           // 土星自传角度
    protected static final int URANUS_ROTATE_ANGLE = 20;           // 天王星自传角度

//    protected float distance_em = 0.3f; // 月球中心距离地球中心距离

    protected Context mContext;

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

    private String[][] mPlanetSParams = {
            {"sun.bmp", "u_sunTextureUnit", "u_sunMatrix", "sun_Position", "sun_TextureCoordinates", "planet_type", },
            {"mercury.bmp", "u_mercuryTextureUnit", "u_mercuryMatrix", "mercury_Position", "mercury_TextureCoordinates", "planet_type", },
            {"venus.bmp", "u_venusTextureUnit", "u_venusMatrix", "venus_Position", "venus_TextureCoordinates", "planet_type", },
            {"earth.bmp", "u_earthTextureUnit", "u_earthMatrix", "earth_Position", "earth_TextureCoordinates", "planet_type", },
            {"mars.bmp", "u_marsTextureUnit", "u_marsMatrix", "mars_Position", "mars_TextureCoordinates", "planet_type", },
            {"jupiter.bmp", "u_jupiterTextureUnit", "u_jupiterMatrix", "jupiter_Position", "jupiter_TextureCoordinates", "planet_type", },
            {"saturn.bmp", "u_saturnTextureUnit", "u_saturnMatrix", "saturn_Position", "saturn_TextureCoordinates", "planet_type", },
            {"uranus.bmp", "u_uranusTextureUnit", "u_uranusMatrix", "uranus_Position", "uranus_TextureCoordinates", "planet_type", },
    };

    private float[][] mPlanetFParams = {
            {SUN_RADIUS, 0.0f,},
            {MERCURY_RADIUS, MECURY_TRACK_RADIUS, },
            {VENUS_RADIUS, VENUS_TRACK_RADIUS, },
            {EARTH_RADIUS, EARTH_TRACK_RADIUS, },
            {MARS_RADIUS, MARS_TRACK_RADIUS, },
            {JUPITER_RADIUS, JUPITER_TRACK_RADIUS, },
            {SATURN_RADIUS, SATURN_TRACK_RADIUS, },
            {URANUS_RADIUS, URANUS_TRACK_RADIUS, },
    };

    private int[][] mPlanetIParams = {
            {angleSpan, 0, 0},
            {angleSpan, MERCURY_TRIANGLE_COUNT, MERCURY_ROTATE_ANGLE},
            {angleSpan, VENUS_TRIANGLE_COUNT, VENUS_ROTATE_ANGLE},
            {angleSpan, EARTH_TRIANGLE_COUNT, EARTH_ROTATE_ANGLE},
            {angleSpan, MARS_TRIANGLE_COUNT, MARS_ROTATE_ANGLE},
            {angleSpan, JUPITER_TRIANGLE_COUNT, JUPITER_ROTATE_ANGLE},
            {angleSpan, SATURN_TRIANGLE_COUNT, SATURN_ROTATE_ANGLE},
            {angleSpan, URANUS_TRIANGLE_COUNT, URANUS_ROTATE_ANGLE},
    };

    private Planet mSun;
    private Planet mMercury;
    private Planet mVenus;
    private Planet mEarth;
    private Planet mMars;
    private Planet mJupiter;
    private Planet mSaturn;
    private Planet mUranus;

    public SolarSystemRender(Context context) {
        super();
        mContext = context;

        createPlanets();

        initAllCoordinate();

        initAllTracks();
    }

    protected void createPlanets(){
        int row = 0;
        mSun = createPlanet(row++);
        mMercury = createPlanet(row++);
        mVenus = createPlanet(row++);
        mEarth = createPlanet(row++);
        mMars = createPlanet(row++);
        mJupiter = createPlanet(row++);
        mSaturn = createPlanet(row++);
        mUranus = createPlanet(row++);
    }

    protected Planet createPlanet(int row){
        int scolume = 0;
        int fcolume = 0;
        int icolume = 0;
        return new Planet(mContext, row, row,
                mPlanetSParams[row][scolume++], mPlanetSParams[row][scolume++],
                mPlanetSParams[row][scolume++], mPlanetSParams[row][scolume++],
                mPlanetSParams[row][scolume++], mPlanetSParams[row][scolume++],

                mPlanetFParams[row][fcolume++], mPlanetIParams[row][icolume++],
                mPlanetFParams[row][fcolume++], mPlanetIParams[row][icolume++],
                mPlanetIParams[row][icolume++]);
    }


    protected void initAllTracks(){
        // x y
        float x = 0.0f;
        float y = 0.0f;
        float z = 0.0f;

        mMercury.initTracks(x, y, z);
        mVenus.initTracks(x, y, z);
        mEarth.initTracks(x, y, z);
        mMars.initTracks(x, y, z);
        mJupiter.initTracks(x, y, z);
        mSaturn.initTracks(x, y, z);
        mUranus.initTracks(x, y, z);
    }

    protected void initAllCoordinate() {
        mSun.initCoordinates();
        mMercury.initCoordinates();
        mVenus.initCoordinates();
        mEarth.initCoordinates();
        mMars.initCoordinates();
        mJupiter.initCoordinates();
        mSaturn.initCoordinates();
        mUranus.initCoordinates();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        mProgram = OpenGLUtils.loadProgram(vertex_shader, frag_shader);
        GLES20.glUseProgram(mProgram);
//        GLES20.glDepthRangef(20, 100);
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        mSun.onSurfaceCreated(gl, config, mProgram);
        mMercury.onSurfaceCreated(gl, config, mProgram);
        mVenus.onSurfaceCreated(gl, config, mProgram);
        mEarth.onSurfaceCreated(gl, config, mProgram);
        mMars.onSurfaceCreated(gl, config, mProgram);
        mJupiter.onSurfaceCreated(gl, config, mProgram);
        mSaturn.onSurfaceCreated(gl, config, mProgram);
        mUranus.onSurfaceCreated(gl, config, mProgram);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        super.onSurfaceChanged(gl, width, height);

        mSun.onSurfaceChanged(gl, width, height);
        mMercury.onSurfaceChanged(gl, width, height);
        mVenus.onSurfaceChanged(gl, width, height);
        mEarth.onSurfaceChanged(gl, width, height);
        mMars.onSurfaceChanged(gl, width, height);
        mJupiter.onSurfaceChanged(gl, width, height);
        mSaturn.onSurfaceChanged(gl, width, height);
        mUranus.onSurfaceChanged(gl, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT|GLES20.GL_DEPTH_BUFFER_BIT);

        mSun.onDrawFrame(gl);
        mMercury.onDrawFrame(gl);
        mVenus.onDrawFrame(gl);
        mEarth.onDrawFrame(gl);
        mMars.onDrawFrame(gl);
        mJupiter.onDrawFrame(gl);
        mSaturn.onDrawFrame(gl);
        mUranus.onDrawFrame(gl);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

        mbStartedDraw = true;
    }

    @Override
    protected void localReleaseGL() {
        super.localReleaseGL();
        mSun.releaseGL();
        mMercury.releaseGL();
        mVenus.releaseGL();
        mEarth.releaseGL();
        mMars.releaseGL();
        mJupiter.releaseGL();
        mSaturn.releaseGL();
        mUranus.releaseGL();
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
