package com.lvhiei.openglestest.render;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by mj on 17-9-25.
 */


public class TriangleRender extends BaseRender{

    private static final String vertex_shader = "\n" +
            "attribute vec4 a_Position;     \n" +
            "          \n" +
            "void main()                      \n" +
            "{                                \n" +
            "    gl_Position = a_Position;  \n" +
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


    private static final float[] trianglePoints = {
            0.0f, 0.5f,
            -0.5f, -0.5f,
            0.5f, -0.5f,
//
//            0.0f, 0.0f,
//            -0.5f, -0.5f,
//            0.5f, -0.5f,
    };


    private FloatBuffer mVertexCoordinate;

    public TriangleRender(){
        mVertexCoordinate = ByteBuffer.allocateDirect(trianglePoints.length * 4)
                            .order(ByteOrder.nativeOrder())
                            .asFloatBuffer();

        mVertexCoordinate.put(trianglePoints);
        mVertexCoordinate.position(0);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        mProgram = OpenGLUtils.loadProgram(vertex_shader, frag_shader);
        GLES20.glUseProgram(mProgram);
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        int apos_loc = GLES20.glGetAttribLocation(mProgram, "a_Position");
        GLES20.glVertexAttribPointer(apos_loc, 2, GLES20.GL_FLOAT, false, 0, mVertexCoordinate);
        GLES20.glEnableVertexAttribArray(apos_loc);

        int color_pos = GLES20.glGetUniformLocation(mProgram, "u_Color");
        GLES20.glUniform4f(color_pos, 0.0f, 1.0f, 0.0f, 1.0f);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
    }
}
