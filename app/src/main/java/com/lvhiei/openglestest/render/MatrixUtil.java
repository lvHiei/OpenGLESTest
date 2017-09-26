package com.lvhiei.openglestest.render;

import android.opengl.Matrix;

/**
 * Created by mj on 17-9-26.
 */


public class MatrixUtil {

    private static float[] mProjectMatrix = new float[16];
    private static float[] mVMatrix = new float[16];
    private static float[] mFinalMatrix = new float[16];

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

    public static float[] getFinalMatrix(){
        Matrix.multiplyMM(mFinalMatrix, 0, mProjectMatrix, 0, mVMatrix, 0);
        return mFinalMatrix;
    }
}
