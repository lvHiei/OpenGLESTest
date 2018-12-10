package com.lvhiei.openglestest.render;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by mj on 18-12-10.
 */


public class TestBlitFrameBufferRender extends BaseRender {


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
            "    gl_Position = a_Position;  \n" +
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
//            -0.5f, 0.5f,
//            -0.5f, -0.5f,
//            0.5f, -0.5f,
//            0.5f, 0.5f,

            -1.0f, 1.0f,
            -1.0f, -1.0f,
            1.0f, -1.0f,
            1.0f, 1.0f,

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
    private int mTextureId;
    private int mTextureLoc;
    private float[] mProjectionMatrix = new float[16];

    private Context mContext;
    protected int mMatrixLoc;

    private static final int FBO_SIZE = 1;
    private static final int RBO_SIZE = 3;

    private int[] mFBO = new int[FBO_SIZE];
    private int[] mRBOs = new int[RBO_SIZE];
    private int fWidth = 520;
    private int fHeight = 520;

    public TestBlitFrameBufferRender(Context context) {
        mContext = context;
        initCoordinate();
    }

    private void initCoordinate(){
//
//        for(int i = 0; i < textureCoordinate.length; ++i){
//            if(i % 2 == 0){
//                textureCoordinate[i] = 2 * textureCoordinate[i];
//            }
//        }

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
//        width = width/2;
        final float aspectRatio = width > height ?
                (float) width / (float) height :
                (float) height / (float) width;
        if(width > height){
            Matrix.orthoM(mProjectionMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, -1f, 1f);
        }else{
            Matrix.orthoM(mProjectionMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, -1f, 1f);
//            Matrix.orthoM(mProjectionMatrix, 0, -1f, 1f, -1, 1, -1f, 1f);
        }

        mMatrixUtil.setProjectOrthom(-1f, 1f, -aspectRatio, aspectRatio, -1f, 1f);
        mMatrixUtil.setRotate(0, 0, 0, 1.0f);
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
//        mTextureId = OpenGLUtils.loadRepeatTexture(mContext, "hiei.bmp");
//        mTextureId = OpenGLUtils.loadMirrorRepeatTexture(mContext, "hiei.bmp");
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
    }

    int mViewWidth;
    int mViewHeight;
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        super.onSurfaceChanged(gl, width, height);
        projectionMatrix(width, height);
        mViewWidth = width;
        mViewHeight = height;
//        fWidth = mViewWidth;
//        fHeight = mViewHeight;


        GLES30.glGenFramebuffers(mFBO.length, mFBO, 0);
        GLES30.glGenRenderbuffers(mRBOs.length, mRBOs, 0);
        for(int i = 0; i < mRBOs.length; ++i){
            GLES30.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, mRBOs[i]);
            GLES30.glRenderbufferStorage(GLES20.GL_RENDERBUFFER, GLES30.GL_RGBA8, fWidth, fHeight);
        }

//        GLES30.glBindFramebuffer(GLES30.GL_DRAW_FRAMEBUFFER, mFBO[0]);
        GLES30.glBindFramebuffer(GLES30.GL_DRAW_FRAMEBUFFER, mFBO[0]);
        for(int i = 0; i < mRBOs.length; ++i){
            GLES30.glFramebufferRenderbuffer(GLES30.GL_DRAW_FRAMEBUFFER,
                    GLES30.GL_COLOR_ATTACHMENT0 + i, GLES30.GL_RENDERBUFFER, mRBOs[i]);
        }
        GLES30.glBindFramebuffer(GLES30.GL_DRAW_FRAMEBUFFER, 0);

        GLES20.glUniformMatrix4fv(mMatrixLoc, 1, false, mMatrixUtil.getFinalMatrix(), 0);
//        GLES20.glUniformMatrix4fv(mMatrixLoc, 1, false, mProjectionMatrix, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES30.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        int[] fboBuffers = new int[RBO_SIZE];
        for(int i = 0; i < mRBOs.length; ++i){
            fboBuffers[i] = GLES30.GL_COLOR_ATTACHMENT0 + i;
        }

        int[] windowBuffers = {
                GLES30.GL_BACK,
        };

        GLES30.glBindFramebuffer(GLES30.GL_DRAW_FRAMEBUFFER, mFBO[0]);
        GLES30.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        GLES30.glDrawBuffers(fboBuffers.length, fboBuffers, 0);

        GLES20.glViewport(0, 0, fWidth, fHeight);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureId);
        GLES20.glUniform1i(mTextureLoc, 0);
        GLES20.glUniformMatrix4fv(mMatrixLoc, 1, false, mMatrixUtil.getFinalMatrix(), 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 4);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

//        GLES30.glBindFramebuffer(GLES30.GL_DRAW_FRAMEBUFFER, 0);
//        GLES30.glDrawBuffers(1, windowBuffers, 0);
//        GLES20.glViewport(0, 0, mViewWidth, mViewHeight);

        GLES30.glBindFramebuffer(GLES30.GL_READ_FRAMEBUFFER, mFBO[0]);
        GLES30.glReadBuffer(GLES30.GL_COLOR_ATTACHMENT0);

        GLES30.glBindFramebuffer(GLES30.GL_DRAW_FRAMEBUFFER, 0);
        GLES30.glDrawBuffers(1, windowBuffers, 0);

        GLES20.glViewport(0, 0, mViewWidth, mViewHeight);
        GLES30.glBlitFramebuffer(0, 0, fWidth, fHeight,
//                                0, 0, fWidth/3, fHeight/3,
                0, 0, mViewWidth, mViewHeight,
                GLES30.GL_COLOR_BUFFER_BIT, GLES30.GL_NEAREST);

        GLES30.glBindFramebuffer(GLES30.GL_READ_FRAMEBUFFER, mFBO[0]);
        GLES30.glReadBuffer(GLES30.GL_COLOR_ATTACHMENT1);

        GLES30.glBindFramebuffer(GLES30.GL_DRAW_FRAMEBUFFER, 0);

        GLES30.glBlitFramebuffer(0, 0, fWidth, fHeight,
                0, fHeight/2, fWidth/3, fHeight/2 + fHeight/3,
                GLES30.GL_COLOR_BUFFER_BIT, GLES30.GL_NEAREST);

        GLES30.glBindFramebuffer(GLES30.GL_READ_FRAMEBUFFER, mFBO[0]);
        GLES30.glReadBuffer(GLES30.GL_COLOR_ATTACHMENT2);

        GLES30.glBindFramebuffer(GLES30.GL_DRAW_FRAMEBUFFER, 0);

        GLES30.glBlitFramebuffer(0, 0, fWidth, fHeight,
                fWidth/2, fHeight/2, fWidth/2 + fWidth/3,fHeight/2 + fHeight/3,
                GLES30.GL_COLOR_BUFFER_BIT, GLES30.GL_NEAREST);
        GLES30.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
    }
}
