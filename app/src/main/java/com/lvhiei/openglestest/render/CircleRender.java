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
 * Created by mj on 17-9-25.
 */


public class CircleRender extends BaseRender{

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


    private float x;
    private float y;
    private float r;

    private final int nTriangleCount = 40;

    private FloatBuffer mVertexCoordinate;
    private float[] mProjectionMatrix = new float[16];
    private int mMatrixLoc;

    public CircleRender(){
        x = 0.0f;
        y = 0.0f;
        r = 0.5f;
        initVertexCoordinate();
    }


    private void initVertexCoordinate(){
        // 顶点的个数，我们分割count个三角形，有count+1个点，再加上圆心共有count+2个点
        final int nodeCount = nTriangleCount + 2;
        float circleCoords[] = new float[nodeCount * 4];
        // x y
        int offset = 0;
        circleCoords[offset++] = x;// 中心点
        circleCoords[offset++] = y;
        for (int i = 0; i < nTriangleCount + 1; i++) {
            float angleInRadians = ((float) i / (float) nTriangleCount)
                    * ((float) Math.PI * 2f);
            circleCoords[offset++] = x + r * (float)Math.sin(angleInRadians);
            circleCoords[offset++] = y + r * (float)Math.cos(angleInRadians);
        }

        mVertexCoordinate = ByteBuffer.allocateDirect(circleCoords.length * 4)
                                        .order(ByteOrder.nativeOrder())
                                        .asFloatBuffer();
        mVertexCoordinate.put(circleCoords);
        mVertexCoordinate.position(0);
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
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        mProgram = OpenGLUtils.loadProgram(vertex_shader, frag_shader);
        GLES20.glUseProgram(mProgram);
//        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        int apos_loc = GLES20.glGetAttribLocation(mProgram, "a_Position");
        GLES20.glVertexAttribPointer(apos_loc, 2, GLES20.GL_FLOAT, false, 0, mVertexCoordinate);
        GLES20.glEnableVertexAttribArray(apos_loc);

        int color_pos = GLES20.glGetUniformLocation(mProgram, "u_Color");
        GLES20.glUniform4f(color_pos, 0.0f, 1.0f, 0.0f, 1.0f);

        mMatrixLoc = GLES20.glGetUniformLocation(mProgram, "u_Matrix");
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        super.onSurfaceChanged(gl, width, height);
        projectionMatrix(width, height);
        GLES20.glUniformMatrix4fv(mMatrixLoc, 1, false, mProjectionMatrix, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, nTriangleCount + 2);
    }
}
