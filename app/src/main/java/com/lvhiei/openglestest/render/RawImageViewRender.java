package com.lvhiei.openglestest.render;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class RawImageViewRender extends BaseRender {
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
//            // triangle fan
//            -0.5f, 0.5f,
//            -0.5f, -0.5f,
//            0.5f, -0.5f,
//            0.5f, 0.5f,

            // triangle strip
            -1.0f, -1.0f,
            1.0f, -1.0f,
            -1.0f, 1.0f,
            1.0f, 1.0f,
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
    private int mImageWidth;
    private int mImageHeight;
    private int mImageSize;
    private ByteBuffer mRGBABuffer = null;

    private String mRawdataPath = "/sdcard/opengltest/";
    private String mRawDataName = "raw.rgba";
    private String mRawData = mRawdataPath + mRawDataName;
    FileInputStream inputStream = null;


    public RawImageViewRender(Context context, int width, int height){
        mContext = context;
        mImageWidth = width;
        mImageHeight = height;
        mImageSize = mImageWidth * mImageHeight * 4;
        mRGBABuffer = ByteBuffer.allocate(mImageSize);
        initCoordinate();
        initRawImage();
    }

    public void updateTexture(){
        if(null == inputStream){
            return;
        }

        try {
            inputStream.read(mRGBABuffer.array(), 0, mRGBABuffer.capacity());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeRawImage(){
        if(null != inputStream){
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void initRawImage(){
        File file = new File(mRawdataPath);
        if(!file.exists()){
            file.mkdir();
        }

        if(inputStream == null){
            try {
                inputStream = new FileInputStream(mRawData);
                inputStream.read(mRGBABuffer.array(), 0, mRGBABuffer.capacity());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        super.onSurfaceChanged(gl, width, height);
        projectionMatrix(width, height);
        GLES20.glUniformMatrix4fv(mMatrixLoc, 1, false, mMatrixUtil.getFinalMatrix(), 0);
//        GLES20.glUniformMatrix4fv(mMatrixLoc, 1, false, mProjectionMatrix, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glViewport(0, 0, mImageWidth, mImageHeight);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureId);
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, mImageWidth, mImageHeight,
                0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, mRGBABuffer);
        GLES20.glUniform1i(mTextureLoc, 0);
        GLES20.glUniformMatrix4fv(mMatrixLoc, 1, false, mMatrixUtil.getIdentityMatrix(), 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
    }

    @Override
    protected void localReleaseGL() {
        super.localReleaseGL();
        if(0 != mTextureId){
            OpenGLUtils.deleteTexture(mTextureId);
            mTextureId = 0;
        }
        closeRawImage();
    }
}
