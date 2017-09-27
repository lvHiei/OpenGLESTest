package com.lvhiei.openglestest.render;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by mj on 17-9-26.
 */


public class ColorCubeRender extends CubeRender{

    protected static final String vertex_shader = "\n" +
            "attribute vec4 a_Position;     \n" +
            "attribute vec4 a_Color;     \n" +
            "varying  vec4 v_Color;     \n" +
            "uniform  mat4 u_Matrix;     \n" +
            "          \n" +
            "void main()                      \n" +
            "{                                \n" +
            "    gl_Position = u_Matrix * a_Position;  \n" +
            "    v_Color = a_Color;  \n" +
            "}";



    protected static final String frag_shader = "\n" +
            "precision mediump float;   \n" +
            "                                          \n" +
            "varying vec4 v_Color;                                 \n" +
            "    \n" +
            "void main()                           \n" +
            "{                                 \n" +
            "    gl_FragColor = v_Color;                                           \n" +
            "}";

    protected static final float[] colors = {
            // front
            1.0f, 1.0f, 1.0f, 1.0f,
            1.0f, 0.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 0.0f, 1.0f,
            1.0f, 0.0f, 0.0f, 1.0f,

            // back
            1.0f, 1.0f, 1.0f, 1.0f,
            0.0f, 0.0f, 1.0f, 1.0f,
            0.0f, 0.0f, 1.0f, 1.0f,
            0.0f, 0.0f, 1.0f, 1.0f,
            0.0f, 0.0f, 1.0f, 1.0f,
            0.0f, 0.0f, 1.0f, 1.0f,

            // left
            1.0f, 1.0f, 1.0f, 1.0f,
            1.0f, 0.0f, 1.0f, 1.0f,
            1.0f, 0.0f, 1.0f, 1.0f,
            1.0f, 0.0f, 1.0f, 1.0f,
            1.0f, 0.0f, 1.0f, 1.0f,
            1.0f, 0.0f, 1.0f, 1.0f,

            // right
            1.0f, 1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,

            // up
            1.0f, 1.0f, 1.0f, 1.0f,
            0.0f, 1.0f, 0.0f, 1.0f,
            0.0f, 1.0f, 0.0f, 1.0f,
            0.0f, 1.0f, 0.0f, 1.0f,
            0.0f, 1.0f, 0.0f, 1.0f,
            0.0f, 1.0f, 0.0f, 1.0f,

            // bottom
            1.0f, 1.0f, 1.0f, 1.0f,
            0.0f, 1.0f, 1.0f, 1.0f,
            0.0f, 1.0f, 1.0f, 1.0f,
            0.0f, 1.0f, 1.0f, 1.0f,
            0.0f, 1.0f, 1.0f, 1.0f,
            0.0f, 1.0f, 1.0f, 1.0f,

//
//            // front
//            1.0f, 1.0f, 1.0f, 0.0f,
//            1.0f, 0.0f, 0.0f, 0.0f,
//            1.0f, 0.0f, 0.0f, 0.0f,
//            1.0f, 0.0f, 0.0f, 0.0f,
//            1.0f, 0.0f, 0.0f, 0.0f,
//            1.0f, 0.0f, 0.0f, 0.0f,
//
//            // back
//            1.0f, 1.0f, 1.0f, 0.0f,
//            0.0f, 0.0f, 1.0f, 0.0f,
//            0.0f, 0.0f, 1.0f, 0.0f,
//            0.0f, 0.0f, 1.0f, 0.0f,
//            0.0f, 0.0f, 1.0f, 0.0f,
//            0.0f, 0.0f, 1.0f, 0.0f,
//
//            // left
//            1.0f, 1.0f, 1.0f, 0.0f,
//            1.0f, 0.0f, 1.0f, 0.0f,
//            1.0f, 0.0f, 1.0f, 0.0f,
//            1.0f, 0.0f, 1.0f, 0.0f,
//            1.0f, 0.0f, 1.0f, 0.0f,
//            1.0f, 0.0f, 1.0f, 0.0f,
//
//            // right
//            1.0f, 1.0f, 1.0f, 0.0f,
//            1.0f, 1.0f, 0.0f, 0.0f,
//            1.0f, 1.0f, 0.0f, 0.0f,
//            1.0f, 1.0f, 0.0f, 0.0f,
//            1.0f, 1.0f, 0.0f, 0.0f,
//            1.0f, 1.0f, 0.0f, 0.0f,
//
//            // up
//            1.0f, 1.0f, 1.0f, 0.0f,
//            0.0f, 1.0f, 0.0f, 0.0f,
//            0.0f, 1.0f, 0.0f, 0.0f,
//            0.0f, 1.0f, 0.0f, 0.0f,
//            0.0f, 1.0f, 0.0f, 0.0f,
//            0.0f, 1.0f, 0.0f, 0.0f,
//
//            // bottom
//            1.0f, 1.0f, 1.0f, 0.0f,
//            0.0f, 1.0f, 1.0f, 0.0f,
//            0.0f, 1.0f, 1.0f, 0.0f,
//            0.0f, 1.0f, 1.0f, 0.0f,
//            0.0f, 1.0f, 1.0f, 0.0f,
//            0.0f, 1.0f, 1.0f, 0.0f,
    };

    protected FloatBuffer mColorBuffer;

    public ColorCubeRender(){
        super();

        mColorBuffer = ByteBuffer.allocateDirect(colors.length * 4)
                        .order(ByteOrder.nativeOrder())
                        .asFloatBuffer();

        mColorBuffer.put(colors);
        mColorBuffer.position(0);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        projectFrustumMatrix(width, height);


        mProgram = OpenGLUtils.loadProgram(vertex_shader, frag_shader);
        GLES20.glUseProgram(mProgram);
        GLES20.glViewport(0, 0, width, height);
//        GLES20.glDepthRangef(20, 100);
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        int apos_loc = GLES20.glGetAttribLocation(mProgram, "a_Position");
        GLES20.glVertexAttribPointer(apos_loc, 3, GLES20.GL_FLOAT, false, 0, mVertexCoordinate);
        GLES20.glEnableVertexAttribArray(apos_loc);

        int color_pos = GLES20.glGetAttribLocation(mProgram, "a_Color");
        GLES20.glVertexAttribPointer(color_pos, 4, GLES20.GL_FLOAT, false, 0, mColorBuffer);
        GLES20.glEnableVertexAttribArray(color_pos);

        int matrix_pos = GLES20.glGetUniformLocation(mProgram, "u_Matrix");
        GLES20.glUniformMatrix4fv(matrix_pos, 1, false, MatrixUtil.getFinalMatrix(), 0);
    }

}
