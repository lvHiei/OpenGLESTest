package com.lvhiei.openglestest.render;

import android.opengl.GLSurfaceView;

/**
 * Created by mj on 17-9-25.
 */


public interface IGLESRenderer extends GLSurfaceView.Renderer {
    void setGLSurface(GLSurfaceView surface);
    void onPause();
    void onResume();
}
