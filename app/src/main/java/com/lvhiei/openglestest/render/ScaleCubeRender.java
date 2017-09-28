package com.lvhiei.openglestest.render;

import android.opengl.GLES20;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by mj on 17-9-27.
 */


public class ScaleCubeRender extends ColorCubeRender {

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glUniformMatrix4fv(mMatrixLoc, 1, false, MatrixUtil.getScaleFinalMatrix(), 0);

        super.onDrawFrame(gl);
    }
}
