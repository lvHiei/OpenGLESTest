package com.lvhiei.openglestest.render;

import android.graphics.Point;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by mj on 17-9-27.
 */


public class BallRender extends BaseRender{

    protected static final String vertex_shader = "\n" +
            "attribute vec4 a_Position;     \n" +
            "uniform  mat4 u_Matrix;     \n" +
            "varying vec4 vPosition;     \n" +
            "          \n" +
            "void main()                      \n" +
            "{                                \n" +
            "    gl_Position = u_Matrix * a_Position;  \n" +
            "    vPosition = a_Position;  \n" +
            "}";



    protected static final String frag_shader = "\n" +
            "precision mediump float;   \n" +
            "                                          \n" +
            "varying vec4 vPosition;                                 \n" +
            "    \n" +
            "void main()                           \n" +
            "{                                 \n" +
            "   float uR = 0.6;//球的半径  \n" +
            "   vec4 color;  \n" +
            "   float n = 8.0;//分为n层n列n行  \n" +
            "   float span = 2.0*uR/n;//正方形长度  \n" +
            "   //计算行列层数  \n" +
            "   int i = int((vPosition.x + uR)/span);//行数  \n" +
            "   int j = int((vPosition.y + uR)/span);//层数  \n" +
            "   int k = int((vPosition.z + uR)/span);//列数  \n" +
            "   int colorType = int(mod(float(i+j+k),2.0));  \n" +
            "   if(colorType == 1) {//奇数时为绿色  \n" +
            "        color = vec4(0.2,1.0,0.129,0);  \n" +
            "   }  \n" +
            "   else {//偶数时为白色  \n" +
            "        color = vec4(1.0,1.0,1.0,0);//白色  \n" +
            "   }  \n" +
            "   //将计算出的颜色给此片元  \n" +
            "   gl_FragColor=color; \n" +
            "}";


    private static final float UNIT_SIZE = 1.0f;// 单位尺寸
    private float r = 0.6f; // 球的半径
    final int angleSpan = 10;// 将球进行单位切分的角度
    private FloatBuffer mVertexCoordinate;// 顶点坐标
    private int vCount = 0;// 顶点个数，先初始化为0
    // 数组中每个顶点的坐标数
    private static final int COORDS_PER_VERTEX = 3;

    protected int mMatrixLoc = -1;


    public BallRender(){
        initVertexCoordinate();
    }

    private void initVertexCoordinate(){
        ArrayList<Float> alVertix = new ArrayList<Float>();// 存放顶点坐标的ArrayList
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

                alVertix.add(x1);
                alVertix.add(y1);
                alVertix.add(z1);
                alVertix.add(x2);
                alVertix.add(y2);
                alVertix.add(z2);
                alVertix.add(x3);
                alVertix.add(y3);
                alVertix.add(z3);
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
    }

    protected void projectFrustumMatrix(int width, int height){
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
            MatrixUtil.setProjectFrustum(-aspectRatio, aspectRatio, -1.0f, 1.0f, near, far);
        }else{
            MatrixUtil.setProjectFrustum(-1.0f, 1.0f, -aspectRatio, aspectRatio, near, far);
        }

        MatrixUtil.setCamera(cx, cy, cz, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
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
        GLES20.glVertexAttribPointer(apos_loc, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, 0, mVertexCoordinate);
        GLES20.glEnableVertexAttribArray(apos_loc);
        mMatrixLoc = GLES20.glGetUniformLocation(mProgram, "u_Matrix");
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        super.onSurfaceChanged(gl, width, height);
        projectFrustumMatrix(width, height);
        GLES20.glUniformMatrix4fv(mMatrixLoc, 1, false, MatrixUtil.getFinalMatrix(), 0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT|GLES20.GL_DEPTH_BUFFER_BIT);

        GLES20.glUniformMatrix4fv(mMatrixLoc, 1, false, MatrixUtil.getFinalMatrix(), 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount);
    }
}
