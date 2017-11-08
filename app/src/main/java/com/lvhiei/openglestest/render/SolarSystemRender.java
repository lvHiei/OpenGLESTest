package com.lvhiei.openglestest.render;

import android.content.Context;
import android.opengl.GLES20;

import com.lvhiei.openglestest.tool.Planet;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by mj on 17-11-8.
 */


public class SolarSystemRender extends BaseRender {
    private static final String vertex_shader = "\n" +
            "attribute vec4 a_Position;                  \n" +
            "attribute vec2 a_TextureCoordinates;                  \n" +
            "uniform  mat4 u_sunMatrix;                  \n" +
            "uniform  mat4 u_earthMatrix;                  \n" +
            "uniform  mat4 u_moonMatrix;                  \n" +
            "uniform  float c_type;                  \n" +
            "                            \n" +
            "             \n" +
            "varying  vec2 v_TextureCoordinates;                  \n" +
            "varying float planet_v;                                              \n" +
            "\n" +
            "const float SUN_V = 0.0;                  \n" +
            "const float EARTH_V = 1.0;                  \n" +
            "const float MOON_V = 2.0;                  \n" +
            "                       \n" +
            "bool isSun()                       \n" +
            "{                                             \n" +
            "     return c_type == SUN_V;                                           \n" +
            "}                                             \n" +
            "                                    \n" +
            "bool isEarth()                       \n" +
            "{                                             \n" +
            "     return c_type == EARTH_V;;                                           \n" +
            "}                                            \n" +
            "bool isMoon()                       \n" +
            "{                                             \n" +
            "     return c_type == MOON_V;;                                           \n" +
            "} \n" +
            "                                    \n" +
            "vec4 getPosition(vec4 pos)                       \n" +
            "{   \n" +
            "    vec4 ret = pos;\n" +
            "\n" +
            "    if(isSun())\n" +
            "    {\n" +
            "        ret = u_sunMatrix * pos;\n" +
            "    }\n" +
            "    else if(isEarth())\n" +
            "    {\n" +
            "        ret = u_earthMatrix * pos;\n" +
            "    }\n" +
            "    else if(isMoon())\n" +
            "    {\n" +
            "        ret = u_moonMatrix * pos;\n" +
            "    }\n" +
            "    \n" +
            "    return ret;\n" +
            "}                                             \n" +
            "                       \n" +
            "void main()                                   \n" +
            "{                                                         \n" +
            "    gl_Position = getPosition(a_Position);\n" +
//            "    if(c_type == SUN_V){\n" +
//            "       gl_Position = u_sunMatrix * sun_Position;\n" +
//            "       v_TextureCoordinates = sun_TextureCoordinates;\n" +
//            "    }else{\n" +
//            "       gl_Position = u_earthMatrix * earth_Position;\n" +
//            "       v_TextureCoordinates = earth_TextureCoordinates;\n" +
//            "    }\n" +
            "    v_TextureCoordinates = a_TextureCoordinates;               \n" +
            "    planet_v = c_type;               \n" +
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
            "const float SUN_V = 0.0;                  \n" +
            "const float EARTH_V = 1.0;                  \n" +
            "const float MOON_V = 2.0;                  \n" +
            "\n" +
            "\n" +
            "bool isSun()                       \n" +
            "{                                             \n" +
            "     return planet_v == SUN_V;                                           \n" +
            "}                                             \n" +
            "                                    \n" +
            "bool isEarth()                       \n" +
            "{                                             \n" +
            "     return planet_v == EARTH_V;                                           \n" +
            "}                                           \n" +
            "\n" +
            "bool isMoon()                       \n" +
            "{                                             \n" +
            "     return planet_v == MOON_V;                                           \n" +
            "} \n" +
            "                 \n" +
            "void main()                                        \n" +
            "{   \n" +
            "    if(isSun())\n" +
            "    {\n" +
            "        gl_FragColor = texture2D(u_sunTextureUnit, v_TextureCoordinates);                                                        \n" +
            "    }\n" +
            "    else if(isEarth())\n" +
            "    {\n" +
            "        gl_FragColor = texture2D(u_earthTextureUnit, v_TextureCoordinates);                                                        \n" +
            "    }\n" +
            "    else if(isMoon())\n" +
            "    {\n" +
            "        gl_FragColor = texture2D(u_moonTextureUnit, v_TextureCoordinates);                                                        \n" +
            "    }else\n" +
            "    {\n" +
            "       gl_FragColor = vec4(0.0, 1.0, 0.0, 1.0);\n" +
            "    }                                           \n" +
            "}    "
            ;


    protected FloatBuffer mVertexCoordinate;// 顶点坐标
    // 数组中每个顶点的坐标数
    protected static final int COORDS_PER_VERTEX = 3;
    protected static final int COORDS_PER_TEXCOORD = 2;


    protected float sun_r = 0.6f;       // 太阳半径
    protected float earth_r = 0.15f;    // 地球半径
    protected float moon_r = 0.05f;     // 月亮半径

    protected float distance_se = 1.5f; // 地球中心距离太阳中心距离
    protected float distance_em = 0.3f; // 月球中心距离地球中心距离

    private FloatBuffer mTextureCoordinate;     // 纹理坐标

    protected Context mContext;

    protected final int neTriangleCount = 3000;                       // 地球公转轨迹的三角个数
    protected final int nmTriangleCount = 250;                       // 月亮公转轨迹的三角个数


    protected Object mLock = new Object();
    protected boolean mWantStop = false;
    protected int mDrawSep = 20;

    protected int mEarthRotateAngle = 15;       // 地球自传角度

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

    private Planet mSun;
    private Planet mEarth;

    public SolarSystemRender(Context context) {
        super();
        mContext = context;

        mSun = new Planet(mContext, 0, 0, "sun.bmp", "u_sunTextureUnit", "u_sunMatrix", sun_r, 10);
        mEarth = new Planet(mContext, 1, 1, "earth.bmp", "u_earthTextureUnit", "u_earthMatrix", earth_r, 10, distance_se, neTriangleCount, mEarthRotateAngle);

        initAllCoordinate();

        initAllTracks();
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

        int mVertexCount = (mSun.getVertexs().size() + mEarth.getVertexs().size())/COORDS_PER_VERTEX;
        // 将mVertexs中的坐标值转存到一个float数组中
        float vertices[] = new float[mVertexCount * COORDS_PER_VERTEX];
        for (int i = 0; i < mSun.getVertexs().size(); i++) {
            vertices[i] = mSun.getVertexs().get(i);
        }

        int offset = mSun.getVertexs().size();
        for (int i = 0; i < mEarth.getVertexs().size(); i++) {
            vertices[i + offset] = mEarth.getVertexs().get(i);
        }

        int mTextureCount = (mSun.getTextures().size() + mEarth.getTextures().size())/COORDS_PER_TEXCOORD;
        // 将mVertexs中的坐标值转存到一个float数组中
        float textures[] = new float[mTextureCount * COORDS_PER_TEXCOORD];
        for (int i = 0; i < mSun.getTextures().size(); i++) {
            textures[i] = mSun.getTextures().get(i);
        }

        offset = mSun.getTextures().size();
        for (int i = 0; i < mEarth.getTextures().size(); i++) {
            textures[i + offset] = mEarth.getTextures().get(i);
        }

        mVertexCoordinate = ByteBuffer.allocateDirect(vertices.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        mVertexCoordinate.put(vertices);
        mVertexCoordinate.position(0);

        mTextureCoordinate = ByteBuffer.allocateDirect(textures.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        mTextureCoordinate.put(textures);
        mTextureCoordinate.position(0);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        mProgram = OpenGLUtils.loadProgram(vertex_shader, frag_shader);
        GLES20.glUseProgram(mProgram);
//        GLES20.glDepthRangef(20, 100);
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        int apos_loc = GLES20.glGetAttribLocation(mProgram, "a_Position");
        GLES20.glVertexAttribPointer(apos_loc, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, 0, mVertexCoordinate);
        GLES20.glEnableVertexAttribArray(apos_loc);

        int atex_loc = GLES20.glGetAttribLocation(mProgram, "a_TextureCoordinates");
        GLES20.glVertexAttribPointer(atex_loc, COORDS_PER_TEXCOORD, GLES20.GL_FLOAT, false, 0, mTextureCoordinate);
        GLES20.glEnableVertexAttribArray(atex_loc);

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

        mSun.onDrawFrame(gl, 0);
        mEarth.onDrawFrame(gl, mSun.getVertexs().size()/3);

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
