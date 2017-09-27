package com.lvhiei.openglestest.render;

import android.opengl.Matrix;

/**
 * Created by mj on 17-9-26.
 */


public class MatrixUtil {

    private static float[] mProjectMatrix = new float[16];
    private static float[] mVMatrix = new float[16];
    private static float[] mtMatrix = new float[16];
    private static float[] mrMatrix = new float[16];
    private static float[] msMatrix = new float[16];
    private static float[] mFinalMatrix = new float[16];

    static {
        initTRS();
    }

    public static void initTRS(){
        Matrix.setIdentityM(mtMatrix, 0);
        Matrix.setIdentityM(mrMatrix, 0);
        Matrix.setIdentityM(msMatrix, 0);
    }

    // 设置摄像头位置 c[xyz] 摄像头位置 t[xyz] 目标点位置 up[xyz] 摄像头up向量
    public static void setCamera(float cx, float cy, float cz,
                                 float tx, float ty, float tz,
                                 float upx, float upy, float upz)
    {
        Matrix.setLookAtM(mVMatrix, 0, cx, cy, cz, tx, ty, tz, upx, upy, upz);
    }

    /**
     * 生成投影矩阵
     * @param left      近平面的 left
     * @param right     近平面的 right
     * @param bottom    近平面的 bottom
     * @param top       近平面的 top
     * @param near      近平面的 z
     * @param far       远平面的 z
     */
    public static void setProjectFrustum(float left, float right, float bottom, float top, float near, float far){
        Matrix.frustumM(mProjectMatrix, 0, left, right, bottom, top, near, far);
    }

    /**
     * 平移
     * @param x     x轴平移量
     * @param y     y轴平移量
     * @param z     z轴平移量
     */
    public static void setTranstate(float x, float y, float z){
        Matrix.translateM(mtMatrix, 0, x, y, z);
    }

    public static float[] getTranslateFinalMatrix(){
        Matrix.multiplyMM(mFinalMatrix, 0, mVMatrix, 0, mtMatrix, 0);
        Matrix.multiplyMM(mFinalMatrix, 0, mProjectMatrix, 0, mFinalMatrix, 0);
        return mFinalMatrix;
    }

    /**
     *  旋转
     * @param angle     旋转角度
     * @param x         旋转中心x分量
     * @param y         旋转中心y分量
     * @param z         旋转中心z分量
     */
    public static void setRotate(float angle, float x, float y, float z){
        Matrix.rotateM(mrMatrix, 0, angle, x, y, z);
    }

    public static float[] getRotateFinalMatrix(){
        Matrix.multiplyMM(mFinalMatrix, 0, mVMatrix, 0, mrMatrix, 0);
        Matrix.multiplyMM(mFinalMatrix, 0, mProjectMatrix, 0, mFinalMatrix, 0);
        return mFinalMatrix;
    }

    /**
     * 缩放
     * @param x     x轴方向缩放率
     * @param y     y轴方向缩放率
     * @param z     z轴方向缩放率
     */
    public static void setScale(float x, float y, float z){
        Matrix.scaleM(msMatrix, 0, x, y, z);
    }

    public static float[] getScaleFinalMatrix(){
        Matrix.multiplyMM(mFinalMatrix, 0, mVMatrix, 0, msMatrix, 0);
        Matrix.multiplyMM(mFinalMatrix, 0, mProjectMatrix, 0, mFinalMatrix, 0);
        return mFinalMatrix;
    }

    public static float[] getFinalMatrix(){
        Matrix.multiplyMM(mFinalMatrix, 0, mrMatrix, 0, mtMatrix, 0);
        Matrix.multiplyMM(mFinalMatrix, 0, msMatrix, 0, mFinalMatrix, 0);
        Matrix.multiplyMM(mFinalMatrix, 0, mVMatrix, 0, mFinalMatrix, 0);
        Matrix.multiplyMM(mFinalMatrix, 0, mProjectMatrix, 0, mFinalMatrix, 0);
        return mFinalMatrix;
    }

}
