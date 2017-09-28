package com.lvhiei.openglestest.render;

import android.content.Context;
import android.opengl.GLES20;

import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by mj on 17-9-28.
 */


public class TextureCubeRender extends BaseRender {

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




    private FloatBuffer mVertexCoordinate;
    private FloatBuffer mTextureCoordinate;
    private int mTextureId;
    private int mTextureLoc;
    private float[] mProjectionMatrix = new float[16];

    private Context mContext;
    protected int mMatrixLoc;

    public TextureCubeRender(Context context){
        mContext = context;
    }


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        mProgram = OpenGLUtils.loadProgram(vertex_shader, frag_shader);
        GLES20.glUseProgram(mProgram);
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        int apos_loc = GLES20.glGetAttribLocation(mProgram, "a_Position");
        GLES20.glVertexAttribPointer(apos_loc, 2, GLES20.GL_FLOAT, false, 0, mVertexCoordinate);
        GLES20.glEnableVertexAttribArray(apos_loc);

        int atex_loc = GLES20.glGetAttribLocation(mProgram, "a_TextureCoordinates");
        GLES20.glVertexAttribPointer(atex_loc, 2, GLES20.GL_FLOAT, false, 0, mTextureCoordinate);
        GLES20.glEnableVertexAttribArray(atex_loc);

        mMatrixLoc = GLES20.glGetUniformLocation(mProgram, "u_Matrix");

        mTextureLoc = GLES20.glGetUniformLocation(mProgram, "u_TextureUnit");

        mTextureId = OpenGLUtils.loadTexture(mContext, "hiei.bmp");
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        super.onSurfaceChanged(gl, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        super.onDrawFrame(gl);
    }

    @Override
    protected void localReleaseGL() {
        super.localReleaseGL();
    }


}
