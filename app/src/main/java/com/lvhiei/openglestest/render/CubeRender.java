package com.lvhiei.openglestest.render;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by mj on 17-9-26.
 */


public class CubeRender extends BaseRender {

    private static final String vertex_shader = "\n" +
            "attribute vec4 a_Position;     \n" +
            "uniform  mat4 u_Matrix;     \n" +
            "          \n" +
            "void main()                      \n" +
            "{                                \n" +
            "    gl_Position = u_Matrix * a_Position;  \n" +
            "}";



    private static final String frag_shader = "\n" +
            "precision mediump float;   \n" +
            "                                          \n" +
            "uniform vec4 u_Color;                                 \n" +
            "    \n" +
            "void main()                           \n" +
            "{                                 \n" +
//        "    gl_FragColor = vec4(1.0, 0.0, 0.0, 1.0);                                           \n" +
            "    gl_FragColor = u_Color;                                           \n" +
            "}";


    // draw triangle fan
    protected final float[] vertices = {
            // front z is 1.0f
            0.0f, 0.0f, 1.0f,
            -1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            1.0f, -1.0f, 1.0f,
            -1.0f, -1.0f, 1.0f,
            -1.0f, 1.0f, 1.0f,


            // back z is -1.0f
            0.0f, 0.0f, -1.0f,
            -1.0f, 1.0f, -1.0f,
            1.0f, 1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,
            -1.0f, -1.0f, -1.0f,
            -1.0f, 1.0f, -1.0f,


            // left x is -1.0f
            -1.0f, 0.0f, 0.0f,
            -1.0f, 1.0f, 1.0f,
            -1.0f, 1.0f, -1.0f,
            -1.0f, -1.0f, -1.0f,
            -1.0f, -1.0f, 1.0f,
            -1.0f, 1.0f, 1.0f,

            // right x is 1.0f
            1.0f, 0.0f, 0.0f,
            1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,
            1.0f, -1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,

            // up y is 1.0f
            0.0f, 1.0f, 0.0f,
            1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, -1.0f,
            -1.0f, 1.0f, -1.0f,
            -1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,


            // down y is -1.0f
            0.0f, -1.0f, 0.0f,
            1.0f, -1.0f, 1.0f,
            1.0f, -1.0f, -1.0f,
            -1.0f, -1.0f, -1.0f,
            -1.0f, -1.0f, 1.0f,
            1.0f, -1.0f, 1.0f,
    };

    protected FloatBuffer mVertexCoordinate;
    protected int mMatrixLoc;

    public CubeRender(){
        mVertexCoordinate = ByteBuffer.allocateDirect(vertices.length * 4)
                            .order(ByteOrder.nativeOrder())
                            .asFloatBuffer();
        mVertexCoordinate.put(vertices);
        mVertexCoordinate.position(0);
    }


    protected void projectFrustumMatrix(int width, int height){
        final float aspectRatio = width > height ?
                (float) width / (float) height :
                (float) height / (float) width;

//        final float near = 20.0f;
//        final float far = 100.0f;
//
//        final float cx = 0.0f;
//        final float cy = 5.0f;
//        final float cz = 45.0f;


        final float near = 20.0f;
        final float far = 100.0f;

        final float cx = -16.0f;
        final float cy = 8.0f;
        final float cz = 45.0f;


        if(width > height){
            mMatrixUtil.setProjectFrustum(-aspectRatio, aspectRatio, -1.0f, 1.0f, near, far);
        }else{
            mMatrixUtil.setProjectFrustum(-1.0f, 1.0f, -aspectRatio, aspectRatio, near, far);
        }

        mMatrixUtil.setCamera(cx, cy, cz, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
    }


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        mProgram = OpenGLUtils.loadProgram(vertex_shader, frag_shader);
        GLES20.glUseProgram(mProgram);
//        GLES20.glDepthRangef(20, 100);
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
//        //打开背面剪裁
//        GLES20.glEnable(GLES20.GL_CULL_FACE);

        int apos_loc = GLES20.glGetAttribLocation(mProgram, "a_Position");
        GLES20.glVertexAttribPointer(apos_loc, 3, GLES20.GL_FLOAT, false, 0, mVertexCoordinate);
        GLES20.glEnableVertexAttribArray(apos_loc);

        int color_pos = GLES20.glGetUniformLocation(mProgram, "u_Color");
        GLES20.glUniform4f(color_pos, 0.0f, 1.0f, 0.0f, 1.0f);

        mMatrixLoc = GLES20.glGetUniformLocation(mProgram, "u_Matrix");
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        super.onSurfaceChanged(gl, width, height);
        projectFrustumMatrix(width, height);
        GLES20.glUniformMatrix4fv(mMatrixLoc, 1, false, mMatrixUtil.getFinalMatrix(), 0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT|GLES20.GL_DEPTH_BUFFER_BIT);

        int first = 0;
        for(int i = 0; i < 6; i++){
            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, first, 6);
            first += 6;
        }
    }
}
