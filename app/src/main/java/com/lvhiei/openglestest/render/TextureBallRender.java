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
 * Created by mj on 17-9-28.
 */


public class TextureBallRender extends BallRender {

    private static final String vertex_shader = "\n" +
            "attribute vec4 a_Position;     \n" +
            "attribute vec2 a_TextureCoordinates;     \n" +
            "uniform  mat4 u_Matrix;     \n" +
            "varying  vec2 v_TextureCoordinates;     \n" +
            "          \n" +
            "void main()                      \n" +
            "{                                \n" +
            "    gl_Position = u_Matrix * a_Position;  \n" +
            "    v_TextureCoordinates = a_TextureCoordinates;  \n" +
            "}";



    private static final String frag_shader = "\n" +
            "precision mediump float;   \n" +
            "                                          \n" +
            "uniform sampler2D u_TextureUnit;                                 \n" +
            "varying vec2 v_TextureCoordinates;                                 \n" +
            "    \n" +
            "void main()                           \n" +
            "{                                 \n" +
            "    gl_FragColor = texture2D(u_TextureUnit, v_TextureCoordinates);                                           \n" +
            "}";


    private FloatBuffer mTextureCoordinate;
    private int mTextureId;
    private int mTextureLoc;

    private Context mContext;

    public TextureBallRender(Context context){
        super();
        mContext = context;
    }

    @Override
    protected void initVertexCoordinate() {
        ArrayList<Float> alVertix = new ArrayList<Float>();// 存放顶点坐标的ArrayList
        ArrayList<Float> alTextureCoord = new ArrayList<Float>();// 存放顶点坐标的ArrayList
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
        vCount = alVertix.size() / COORDS_PER_VERTEX;// 顶点的数量
        // 将alVertix中的坐标值转存到一个float数组中
        float vertices[] = new float[vCount * COORDS_PER_VERTEX];
        for (int i = 0; i < alVertix.size(); i++) {
            vertices[i] = alVertix.get(i);
        }

        mVertexCoordinate = ByteBuffer.allocateDirect(vertices.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        mVertexCoordinate.put(vertices);
        mVertexCoordinate.position(0);

        float[] textures = new float[alTextureCoord.size()];
        for(int i = 0; i < alTextureCoord.size(); ++i){
            textures[i] = alTextureCoord.get(i);
        }

        mTextureCoordinate = ByteBuffer.allocateDirect(textures.length * 4)
                            .order(ByteOrder.nativeOrder())
                            .asFloatBuffer();
        mTextureCoordinate.put(textures);
        mTextureCoordinate.position(0);
    }


    @Override
    protected void localReleaseGL() {
        super.localReleaseGL();
        if(0 != mTextureId){
            OpenGLUtils.deleteTexture(mTextureId);
            mTextureId = 0;
        }
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

        mMatrixLoc = GLES20.glGetUniformLocation(mProgram, "u_Matrix");

        mTextureLoc = GLES20.glGetUniformLocation(mProgram, "u_TextureUnit");

        mTextureId = OpenGLUtils.loadTexture(mContext, "earth.bmp");
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        super.onSurfaceChanged(gl, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureId);
        GLES20.glUniform1f(mTextureLoc, 0);
        GLES20.glUniformMatrix4fv(mMatrixLoc, 1, false, MatrixUtil.getFinalMatrix(), 0);

        super.onDrawFrame(gl);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

    }
}
