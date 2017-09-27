package com.lvhiei.openglestest.render;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by mj on 17-9-27.
 */


public class TextureSquareRender implements IGLESRenderer{

    protected static final String vertex_shader = "\n" +
            "attribute vec4 a_Position;     \n" +
            "attribute vec2 a_TextureCoordinates;     \n" +
            "varying vec2 v_TextureCoordinates;     \n" +
            "uniform  mat4 u_Matrix;     \n" +
            "          \n" +
            "void main()                      \n" +
            "{                                \n" +
            "    v_TextureCoordinates = a_TextureCoordinates;  \n" +
            "    gl_Position = u_Matrix * a_Position;  \n" +
            "}";



    protected static final String frag_shader = "\n" +
            "precision mediump float;   \n" +
            "                                          \n" +
            "uniform sampler2D  u_TextureUnit;                                 \n" +
            "varying vec2 v_TextureCoordinates;                                 \n" +
            "    \n" +
            "void main()                           \n" +
            "{                                 \n" +
            "    gl_FragColor = texture2D(u_TextureUnit, v_TextureCoordinates);                                           \n" +
//            "    gl_FragColor = vec4(0.0,1.0,0.0,1.0);                                           \n" +
            "}";


    private static final float[] Points = {
            // triangle fan
            -0.5f, 0.5f,
            -0.5f, -0.5f,
            0.5f, -0.5f,
            0.5f, 0.5f,

//            // triangle strip
//            -0.5f, -0.5f,
//            0.5f, -0.5f,
//            -0.5f, 0.5f,
//            0.5f, 0.5f,
    };


    private static final float[] textureCoordinate = {
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,
    };

    private FloatBuffer mVertexCoordinate;
    private FloatBuffer mTextureCoordinate;
    private int mProgram;
    private int mTextureId;
    private int mTextureLoc;
    private float[] mProjectionMatrix = new float[16];

    private Context mContext;


    public TextureSquareRender(Context context){
        mContext = context;
        initCoordinate();
    }

    private void initCoordinate(){
        mVertexCoordinate = ByteBuffer.allocateDirect(Points.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();

        mVertexCoordinate.put(Points);
        mVertexCoordinate.position(0);

        mTextureCoordinate = ByteBuffer.allocateDirect(textureCoordinate.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();

        mTextureCoordinate.put(textureCoordinate);
        mTextureCoordinate.position(0);
    }

    private void projectionMatrix(int width, int height){
        final float aspectRatio = width > height ?
                (float) width / (float) height :
                (float) height / (float) width;
        if(width > height){
            Matrix.orthoM(mProjectionMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, -1f, 1f);
        }else{
            Matrix.orthoM(mProjectionMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, -1f, 1f);
//            Matrix.orthoM(mProjectionMatrix, 0, -1f, 1f, -1, 1, -1f, 1f);
        }
    }


    @Override
    public void setGLSurface(GLSurfaceView surface) {

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        projectionMatrix(width, height);

        mProgram = OpenGLUtils.loadProgram(vertex_shader, frag_shader);
        GLES20.glUseProgram(mProgram);
        GLES20.glViewport(0, 0, width, height);
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        int apos_loc = GLES20.glGetAttribLocation(mProgram, "a_Position");
        GLES20.glVertexAttribPointer(apos_loc, 2, GLES20.GL_FLOAT, false, 0, mVertexCoordinate);
        GLES20.glEnableVertexAttribArray(apos_loc);

        int atex_loc = GLES20.glGetAttribLocation(mProgram, "a_TextureCoordinates");
        GLES20.glVertexAttribPointer(atex_loc, 2, GLES20.GL_FLOAT, false, 0, mTextureCoordinate);
        GLES20.glEnableVertexAttribArray(atex_loc);

        int matrix_pos = GLES20.glGetUniformLocation(mProgram, "u_Matrix");
        GLES20.glUniformMatrix4fv(matrix_pos, 1, false, mProjectionMatrix, 0);

        mTextureLoc = GLES20.glGetUniformLocation(mProgram, "u_TextureUnit");

        mTextureId = OpenGLUtils.loadTexture(mContext, "hiei.bmp");
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureId);
        GLES20.glUniform1f(mTextureLoc, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 4);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
    }
}
