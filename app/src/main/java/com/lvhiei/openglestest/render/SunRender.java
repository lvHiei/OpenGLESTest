package com.lvhiei.openglestest.render;

import android.content.Context;
import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by mj on 17-9-29.
 */

public class SunRender extends BaseRender{
    private static final String vertex_shader = "\n" +
            "attribute vec4 a_Position;                  \n" +
            "attribute vec2 a_TextureCoordinates;                  \n" +
            "uniform  mat4 u_sunMatrix;                  \n" +
            "uniform  mat4 u_earthMatrix;                  \n" +
            "uniform  mat4 u_moonMatrix;                  \n" +
            "                            \n" +
            "uniform  float r_sun;                  \n" +
            "uniform  float r_earth;                  \n" +
            "uniform  float r_moon;     \n" +
            "             \n" +
            "varying  vec2 v_TextureCoordinates;                  \n" +
            "varying  vec4 v_Position;                  \n" +
            "varying  float v_r_sun;                  \n" +
            "varying  float v_r_earth;                  \n" +
            "varying  float v_r_moon;                  \n" +
            "                       \n" +
            "bool isEqual(float x, float y)\n" +
            "{\n" +
            "    return abs(x - y) < 0.01;\n" +
            "}                       \n" +
            "                       \n" +
            "bool isSun(vec3 pos)                       \n" +
            "{                                             \n" +
            "     vec3 zero = vec3(0.0, 0.0, 0.0);                                           \n" +
            "     float dist = distance(pos, zero);                                           \n" +
            "     return isEqual(dist, r_sun);                                           \n" +
            "}                                             \n" +
            "                                    \n" +
            "bool isEarth(vec3 pos)                       \n" +
            "{                                             \n" +
            "     vec3 zero = vec3(0.0, 0.0, 0.0);                                           \n" +
            "     float dist = distance(pos, zero);                                           \n" +
            "     return isEqual(dist, r_earth);                                           \n" +
            "}                                            bool isMoon(vec3 pos)                       \n" +
            "{                                             \n" +
            "     vec3 zero = vec3(0.0, 0.0, 0.0);                                           \n" +
            "     float dist = distance(pos, zero);                                           \n" +
            "     return isEqual(dist, r_moon);                                           \n" +
            "} \n" +
            "                                    \n" +
            "vec4 getPosition(vec4 pos)                       \n" +
            "{   \n" +
            "    vec4 ret = pos;\n" +
            "\n" +
            "    if(isSun(pos.xyz))\n" +
            "    {\n" +
            "        ret = u_sunMatrix * pos;\n" +
            "    }\n" +
            "    else if(isEarth(pos.xyz))\n" +
            "    {\n" +
            "        ret = u_earthMatrix * pos;\n" +
            "    }\n" +
            "    else if(isMoon(pos.xyz))\n" +
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
            "    v_Position = a_Position;\n" +
            "    v_TextureCoordinates = a_TextureCoordinates;               \n" +
            "    v_r_sun = r_sun;               \n" +
            "    v_r_earth = r_earth;               \n" +
            "    v_r_moon = r_moon;               \n" +
            "}"
            ;



    private static final String frag_shader = "\n" +
            "precision mediump float;                \n" +
            "                                                       \n" +
            "uniform sampler2D u_sunTextureUnit;                                              \n" +
            "uniform sampler2D u_earthTextureUnit;                                              \n" +
            "uniform sampler2D u_moonTextureUnit;                                              \n" +
            "varying vec2 v_TextureCoordinates;                                              \n" +
            "varying vec4 v_Position;                                              \n" +
            "varying float v_r_sun;                                              \n" +
            "varying float v_r_earth;                                              \n" +
            "varying float v_r_moon;                                              \n" +
            "\n" +
            "\n" +
            "bool isEqual(float x, float y)\n" +
            "{\n" +
            "    return abs(x - y) < 0.01;\n" +
            "}                       \n" +
            "                       \n" +
            "bool isSun(vec3 pos)                       \n" +
            "{                                             \n" +
            "     vec3 zero = vec3(0.0, 0.0, 0.0);                                           \n" +
            "     float dist = distance(pos, zero);                                           \n" +
            "     return isEqual(dist, v_r_sun);                                           \n" +
            "}                                             \n" +
            "                                    \n" +
            "bool isEarth(vec3 pos)                       \n" +
            "{                                             \n" +
            "     vec3 zero = vec3(0.0, 0.0, 0.0);                                           \n" +
            "     float dist = distance(pos, zero);                                           \n" +
            "     return isEqual(dist, v_r_earth);                                           \n" +
            "}                                           \n" +
            "\n" +
            "bool isMoon(vec3 pos)                       \n" +
            "{                                             \n" +
            "     vec3 zero = vec3(0.0, 0.0, 0.0);                                           \n" +
            "     float dist = distance(pos, zero);                                           \n" +
            "     return isEqual(dist, v_r_moon);                                           \n" +
            "} \n" +
            "                 \n" +
            "void main()                                        \n" +
            "{   \n" +
            "    if(isSun(v_Position.xyz))\n" +
            "    {\n" +
            "        gl_FragColor = texture2D(u_sunTextureUnit, v_TextureCoordinates);                                                        \n" +
            "    }\n" +
            "    else if(isEarth(v_Position.xyz))\n" +
            "    {\n" +
            "        gl_FragColor = texture2D(u_earthTextureUnit, v_TextureCoordinates);                                                        \n" +
            "    }\n" +
            "    else if(isMoon(v_Position.xyz))\n" +
            "    {\n" +
            "        gl_FragColor = texture2D(u_moonTextureUnit, v_TextureCoordinates);                                                        \n" +
            "    }else{gl_FragColor = vec4(0.0, 1.0, 0.0, 1.0);}                                           \n" +
            "}    "
        ;


    protected static final float UNIT_SIZE = 1.0f;// 单位尺寸
    protected final int angleSpan = 10;// 将球进行单位切分的角度
    protected FloatBuffer mVertexCoordinate;// 顶点坐标
    // 数组中每个顶点的坐标数
    protected static final int COORDS_PER_VERTEX = 3;


    protected float sun_r = 0.4f;
    protected float earth_r = 0.1f;
    protected float moon_r = 0.05f;

    protected int sun_vCount = 0;
    protected int earth_vCount = 0;
    protected int moon_vCount = 0;
    protected int vCount = 0;// 顶点个数，先初始化为0

    protected int sun_tCount = 0;
    protected int earth_tCount = 0;
    protected int moon_tCount = 0;
    protected int tCount = 0;// 纹理顶点个数，先初始化为0

    private FloatBuffer mTextureCoordinate;

    private int mSunTextureId;
    private int mEarthTextureId;
    private int mMoonTextureId;

    private int mSunTextureLoc;
    private int mEarthTextureLoc;
    private int mMoonTextureLoc;

    private int mSunMatrixLoc = -1;
    private int mEarthMatrixLoc = -1;
    private int mMoonMatrixLoc = -1;

    private int mSunRLoc = -1;
    private int mEarthRLoc = -1;
    private int mMoonRLoc = -1;


    protected MatrixUtil mSunMatrix;
    protected MatrixUtil mEarthMatrix;
    protected MatrixUtil mMoonMatrix;

    private ArrayList<Float> mSunAlVertix = new ArrayList<>();
    private ArrayList<Float> mEarthAlVertix = new ArrayList<>();
    private ArrayList<Float> mMoonAlVertix = new ArrayList<>();

    private ArrayList<Float> mSunAlTexCoord = new ArrayList<>();
    private ArrayList<Float> mEarthAlTexCoord = new ArrayList<>();
    private ArrayList<Float> mMoonAlTexCoord = new ArrayList<>();

    protected Context mContext;


    public SunRender(Context context) {
        super();
        mContext = context;

        mSunMatrix = mMatrixUtil;
        mEarthMatrix = new MatrixUtil();
        mMoonMatrix = new MatrixUtil();

        initAllCoordinate();
    }

    protected void initCoordinate(float r, ArrayList<Float> alVertix, ArrayList<Float> alTextureCoord){

        for (int vAngle = 0; vAngle < 180; vAngle = vAngle + angleSpan)// 垂直方向angleSpan度一份
        {
            for (int hAngle = 0; hAngle <= 360; hAngle = hAngle + angleSpan)// 水平方向angleSpan度一份
            {
                // 纵向横向各到一个角度后计算对应的此点在球面上的坐标
                float x0 = (float) (r * UNIT_SIZE
                        * Math.sin(Math.toRadians(vAngle)) * Math.cos(Math
                        .toRadians(hAngle)));
                float y0 = (float) (r * UNIT_SIZE
                        * Math.sin(Math.toRadians(vAngle)) * Math.sin(Math
                        .toRadians(hAngle)));
                float z0 = (float) (r * UNIT_SIZE * Math.cos(Math
                        .toRadians(vAngle)));

                float x1 = (float) (r * UNIT_SIZE
                        * Math.sin(Math.toRadians(vAngle)) * Math.cos(Math
                        .toRadians(hAngle + angleSpan)));
                float y1 = (float) (r * UNIT_SIZE
                        * Math.sin(Math.toRadians(vAngle)) * Math.sin(Math
                        .toRadians(hAngle + angleSpan)));
                float z1 = (float) (r * UNIT_SIZE * Math.cos(Math
                        .toRadians(vAngle)));

                float x2 = (float) (r * UNIT_SIZE
                        * Math.sin(Math.toRadians(vAngle + angleSpan)) * Math
                        .cos(Math.toRadians(hAngle + angleSpan)));
                float y2 = (float) (r * UNIT_SIZE
                        * Math.sin(Math.toRadians(vAngle + angleSpan)) * Math
                        .sin(Math.toRadians(hAngle + angleSpan)));
                float z2 = (float) (r * UNIT_SIZE * Math.cos(Math
                        .toRadians(vAngle + angleSpan)));

                float x3 = (float) (r * UNIT_SIZE
                        * Math.sin(Math.toRadians(vAngle + angleSpan)) * Math
                        .cos(Math.toRadians(hAngle)));
                float y3 = (float) (r * UNIT_SIZE
                        * Math.sin(Math.toRadians(vAngle + angleSpan)) * Math
                        .sin(Math.toRadians(hAngle)));
                float z3 = (float) (r * UNIT_SIZE * Math.cos(Math
                        .toRadians(vAngle + angleSpan)));


                float s0 = hAngle / 360.0f;
                float s1 = (hAngle + angleSpan) / 360.0f;
                float t0 = 1 - vAngle / 180.0f;
                float t1 = 1 - (vAngle + angleSpan) / 180.0f;


                // 将计算出来的XYZ坐标加入存放顶点坐标的ArrayList
                alVertix.add(x1);
                alVertix.add(y1);
                alVertix.add(z1);
                alVertix.add(x3);
                alVertix.add(y3);
                alVertix.add(z3);
                alVertix.add(x0);
                alVertix.add(y0);
                alVertix.add(z0);

                alTextureCoord.add(s1);// x1 y1 z1对应纹理坐标
                alTextureCoord.add(t0);
                alTextureCoord.add(s0);// x3 y3 z3对应纹理坐标
                alTextureCoord.add(t1);
                alTextureCoord.add(s0);// x0 y0 z0对应纹理坐标
                alTextureCoord.add(t0);


                alVertix.add(x1);
                alVertix.add(y1);
                alVertix.add(z1);
                alVertix.add(x2);
                alVertix.add(y2);
                alVertix.add(z2);
                alVertix.add(x3);
                alVertix.add(y3);
                alVertix.add(z3);

                alTextureCoord.add(s1);// x1 y1 z1对应纹理坐标
                alTextureCoord.add(t0);
                alTextureCoord.add(s1);// x2 y2 z2对应纹理坐标
                alTextureCoord.add(t1);
                alTextureCoord.add(s0);// x3 y3 z3对应纹理坐标
                alTextureCoord.add(t1);
            }
        }
    }

    protected void initAllCoordinate() {
        initCoordinate(sun_r, mSunAlVertix, mSunAlTexCoord);
        initCoordinate(earth_r, mEarthAlVertix, mEarthAlTexCoord);
        initCoordinate(moon_r, mMoonAlVertix, mMoonAlTexCoord);

        sun_vCount = mSunAlVertix.size() / COORDS_PER_VERTEX;// 顶点的数量
        earth_vCount = mEarthAlVertix.size() / COORDS_PER_VERTEX;// 顶点的数量
        moon_vCount = mMoonAlVertix.size() / COORDS_PER_VERTEX;// 顶点的数量

        vCount = sun_vCount + earth_vCount + moon_vCount;

        // 将alVertix中的坐标值转存到一个float数组中
        float vertices[] = new float[vCount * COORDS_PER_VERTEX];
        int offset = 0;
        for (int i = 0; i < mSunAlVertix.size(); i++) {
            vertices[offset + i] = mSunAlVertix.get(i);
        }

        offset += mSunAlVertix.size();
        for (int i = 0; i < mEarthAlVertix.size(); i++) {
            vertices[offset + i] = mEarthAlVertix.get(i);
        }

        offset += mEarthAlVertix.size();
        for (int i = 0; i < mMoonAlVertix.size(); i++) {
            vertices[offset + i] = mMoonAlVertix.get(i);
        }


        mVertexCoordinate = ByteBuffer.allocateDirect(vertices.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        mVertexCoordinate.put(vertices);
        mVertexCoordinate.position(0);

        sun_tCount = mSunAlTexCoord.size() / COORDS_PER_VERTEX;// 顶点的数量
        earth_tCount = mEarthAlTexCoord.size() / COORDS_PER_VERTEX;// 顶点的数量
        moon_tCount = mMoonAlTexCoord.size() / COORDS_PER_VERTEX;// 顶点的数量

        tCount = sun_tCount + earth_tCount + moon_tCount;


        float[] textures = new float[tCount * COORDS_PER_VERTEX];


        offset = 0;
        for (int i = 0; i < mSunAlTexCoord.size(); i++) {
            textures[offset + i] = mSunAlTexCoord.get(i);
        }

        offset += mSunAlTexCoord.size();
        for (int i = 0; i < mEarthAlTexCoord.size(); i++) {
            textures[offset + i] = mEarthAlTexCoord.get(i);
        }

        offset += mEarthAlTexCoord.size();
        for (int i = 0; i < mMoonAlTexCoord.size(); i++) {
            textures[offset + i] = mMoonAlTexCoord.get(i);
        }


        mTextureCoordinate = ByteBuffer.allocateDirect(textures.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        mTextureCoordinate.put(textures);
        mTextureCoordinate.position(0);
    }


    protected void projectFrustumMatrix(MatrixUtil matrixUtil, int width, int height){
        final float aspectRatio = width > height ?
                (float) width / (float) height :
                (float) height / (float) width;

//        final float near = 20.0f;
//        final float far = 100.0f;

        final float cx = 0.0f;
        final float cy = 0.0f;
        final float cz = 30.0f;


        final float near = 20.0f;
        final float far = 100.0f;

//        final float cx = -16.0f;
//        final float cy = 8.0f;
//        final float cz = 45.0f;


        if(width > height){
            matrixUtil.setProjectFrustum(-aspectRatio, aspectRatio, -1.0f, 1.0f, near, far);
        }else{
            matrixUtil.setProjectFrustum(-1.0f, 1.0f, -aspectRatio, aspectRatio, near, far);
        }

        matrixUtil.setCamera(cx, cy, cz, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
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
        GLES20.glVertexAttribPointer(atex_loc, 2, GLES20.GL_FLOAT, false, 0, mTextureCoordinate);
        GLES20.glEnableVertexAttribArray(atex_loc);

        mSunMatrixLoc = GLES20.glGetUniformLocation(mProgram, "u_sunMatrix");
        mEarthMatrixLoc = GLES20.glGetUniformLocation(mProgram, "u_earthMatrix");
        mMoonMatrixLoc = GLES20.glGetUniformLocation(mProgram, "u_moonMatrix");

        mSunRLoc = GLES20.glGetUniformLocation(mProgram, "r_sun");
        mEarthRLoc = GLES20.glGetUniformLocation(mProgram, "r_earth");
        mMoonRLoc = GLES20.glGetUniformLocation(mProgram, "r_moon");

        mSunTextureLoc = GLES20.glGetUniformLocation(mProgram, "u_sunTextureUnit");
        mEarthTextureLoc = GLES20.glGetUniformLocation(mProgram, "u_earthTextureUnit");
        mMoonTextureLoc = GLES20.glGetUniformLocation(mProgram, "u_moonTextureUnit");

        mEarthTextureId = OpenGLUtils.loadTexture(mContext, "earth.bmp");
        mSunTextureId = OpenGLUtils.loadTexture(mContext, "sun.bmp");
        mMoonTextureId = OpenGLUtils.loadTexture(mContext, "moon.bmp");

        GLES20.glUniform1f(mSunRLoc, sun_r);
        GLES20.glUniform1f(mEarthRLoc, earth_r);
        GLES20.glUniform1f(mMoonRLoc, moon_r);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        super.onSurfaceChanged(gl, width, height);
        projectFrustumMatrix(mSunMatrix, width, height);
        projectFrustumMatrix(mEarthMatrix, width, height);
        projectFrustumMatrix(mMoonMatrix, width, height);

        mEarthMatrix.setTranstate(0.7f, 0.0f, 0.0f);
        mMoonMatrix.setTranstate(1.0f, 0.0f, 0.0f);

        GLES20.glUniformMatrix4fv(mSunMatrixLoc, 1, false, mSunMatrix.getFinalMatrix(), 0);
        GLES20.glUniformMatrix4fv(mEarthMatrixLoc, 1, false, mEarthMatrix.getFinalMatrix(), 0);
        GLES20.glUniformMatrix4fv(mMoonMatrixLoc, 1, false, mMoonMatrix.getFinalMatrix(), 0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT|GLES20.GL_DEPTH_BUFFER_BIT);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mSunTextureId);
        GLES20.glUniform1i(mSunTextureLoc, 0);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mEarthTextureId);
        GLES20.glUniform1i(mEarthTextureLoc, 1);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE2);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mMoonTextureId);
        GLES20.glUniform1i(mMoonTextureLoc, 2);

        GLES20.glUniformMatrix4fv(mSunMatrixLoc, 1, false, mSunMatrix.getFinalMatrix(), 0);
        GLES20.glUniformMatrix4fv(mEarthMatrixLoc, 1, false, mEarthMatrix.getFinalMatrix(), 0);
        GLES20.glUniformMatrix4fv(mMoonMatrixLoc, 1, false, mMoonMatrix.getFinalMatrix(), 0);

        int start = 0;
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, start, sun_vCount);

        start += sun_vCount;
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, start, earth_vCount);

        start += earth_vCount;
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, start, moon_vCount);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
    }

    @Override
    protected void localReleaseGL() {
        super.localReleaseGL();

        if(mSunTextureId != 0){
            OpenGLUtils.deleteTexture(mSunTextureId);
            mSunTextureId = 0;
        }

        if(mEarthTextureId != 0){
            OpenGLUtils.deleteTexture(mEarthTextureId);
            mEarthTextureId = 0;
        }

        if(mMoonTextureId != 0){
            OpenGLUtils.deleteTexture(mMoonTextureId);
            mMoonTextureId = 0;
        }
    }
}
