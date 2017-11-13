package com.lvhiei.openglestest.render;

import android.content.Context;
import android.opengl.GLES20;
import android.widget.Toast;

import com.lvhiei.openglestest.log.ATLog;
import com.lvhiei.openglestest.tool.Moon;
import com.lvhiei.openglestest.tool.Planet;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by mj on 17-11-8.
 */


public class SolarSystemRender extends BaseRender {
    protected static final String vertex_shader = "\n" +
            "attribute vec4 sun_Position;                  \n" +
            "attribute vec4 mercury_Position;                  \n" +
            "attribute vec4 venus_Position;                  \n" +
            "attribute vec4 earth_Position;                  \n" +
            "attribute vec4 mars_Position;                  \n" +
            "attribute vec4 jupiter_Position;                  \n" +
            "attribute vec4 saturn_Position;                  \n" +
            "attribute vec4 uranus_Position;                  \n" +
            "attribute vec4 neptune_Position;                  \n" +
            "\n" +
            "attribute vec4 moon_Position;                  \n" +
            "\n" +
            "attribute vec2 sun_TextureCoordinates;                  \n" +
            "attribute vec2 mercury_TextureCoordinates;                  \n" +
            "attribute vec2 venus_TextureCoordinates;                  \n" +
            "attribute vec2 earth_TextureCoordinates;                  \n" +
            "attribute vec2 mars_TextureCoordinates;                  \n" +
            "attribute vec2 jupiter_TextureCoordinates;                  \n" +
            "attribute vec2 saturn_TextureCoordinates;                  \n" +
            "attribute vec2 uranus_TextureCoordinates;                  \n" +
            "attribute vec2 neptune_TextureCoordinates;                  \n" +
            "\n" +
            "attribute vec2 moon_TextureCoordinates;                  \n" +
            "\n" +
            "uniform  mat4 u_sunMatrix;                  \n" +
            "uniform  mat4 u_mercuryMatrix;                  \n" +
            "uniform  mat4 u_venusMatrix;                  \n" +
            "uniform  mat4 u_earthMatrix;                  \n" +
            "uniform  mat4 u_marsMatrix;                  \n" +
            "uniform  mat4 u_jupiterMatrix;                  \n" +
            "uniform  mat4 u_saturnMatrix;                  \n" +
            "uniform  mat4 u_uranusMatrix;                  \n" +
            "uniform  mat4 u_neptuneMatrix;                  \n" +
            "\n" +
            "uniform  mat4 u_moonMatrix;                  \n" +
            "\n" +
            "uniform  float planet_type;                  \n" +
            "                            \n" +
            "varying  vec2 v_TextureCoordinates;                  \n" +
            "varying float planet_v;                                              \n" +
            "\n" +
            "const float SUN_TYPE = 0.0;                  \n" +
            "const float MERCURY_TYPE = 1.0;                  \n" +
            "const float VENUS_TYPE = 2.0;                  \n" +
            "const float EARTH_TYPE = 3.0;                  \n" +
            "const float MARS_TYPE = 4.0;                  \n" +
            "const float JUPITER_TYPE = 5.0;                  \n" +
            "const float SATURN_TYPE = 6.0;                  \n" +
            "const float URANUS_TYPE = 7.0;                  \n" +
            "const float NEPTUNE_TYPE = 8.0;                  \n" +
            "\n" +
            "const float MOON_TYPE = 9.0;                  \n" +
            "                       \n" +
            "bool isEqual(float x, float y)\n" +
            "{\n" +
            "    return abs(x - y) < 0.01;\n" +
            "}                       \n" +
            "                       \n" +
            "void main()                                   \n" +
            "{                                                         \n" +
            "    if(isEqual(planet_type, SUN_TYPE))\n" +
            "    {\n" +
            "       gl_Position = u_sunMatrix * sun_Position;\n" +
            "       v_TextureCoordinates = sun_TextureCoordinates;\n" +
            "    }\n" +
            "    else if(isEqual(planet_type, MERCURY_TYPE))\n" +
            "    {\n" +
            "       gl_Position = u_mercuryMatrix * mercury_Position;\n" +
            "       v_TextureCoordinates = mercury_TextureCoordinates;\n" +
            "    }\n" +
            "    else if(isEqual(planet_type, VENUS_TYPE))\n" +
            "    {\n" +
            "       gl_Position = u_venusMatrix * venus_Position;\n" +
            "       v_TextureCoordinates = venus_TextureCoordinates;\n" +
            "    }\n" +
            "    else if(isEqual(planet_type, EARTH_TYPE))\n" +
            "    {\n" +
            "       gl_Position = u_earthMatrix * earth_Position;\n" +
            "       v_TextureCoordinates = earth_TextureCoordinates;\n" +
            "    }\n" +
            "    else if(isEqual(planet_type, MARS_TYPE))\n" +
            "    {\n" +
            "       gl_Position = u_marsMatrix * mars_Position;\n" +
            "       v_TextureCoordinates = mars_TextureCoordinates;\n" +
            "    }\n" +
            "    else if(isEqual(planet_type, JUPITER_TYPE))\n" +
            "    {\n" +
            "       gl_Position = u_jupiterMatrix * jupiter_Position;\n" +
            "       v_TextureCoordinates = jupiter_TextureCoordinates;\n" +
            "    }\n" +
            "    else if(isEqual(planet_type, SATURN_TYPE))\n" +
            "    {\n" +
            "       gl_Position = u_saturnMatrix * saturn_Position;\n" +
            "       v_TextureCoordinates = saturn_TextureCoordinates;\n" +
            "    }\n" +
            "    else if(isEqual(planet_type, URANUS_TYPE))\n" +
            "    {\n" +
            "       gl_Position = u_uranusMatrix * uranus_Position;\n" +
            "       v_TextureCoordinates = uranus_TextureCoordinates;\n" +
            "    }\n" +
            "    else if(isEqual(planet_type, NEPTUNE_TYPE))\n" +
            "    {\n" +
            "       gl_Position = u_neptuneMatrix * neptune_Position;\n" +
            "       v_TextureCoordinates = neptune_TextureCoordinates;\n" +
            "    }\n" +
            "    else if(isEqual(planet_type, MOON_TYPE))\n" +
            "    {\n" +
            "       gl_Position = u_moonMatrix * moon_Position;\n" +
            "       v_TextureCoordinates = moon_TextureCoordinates;\n" +
            "    }\n" +
            "    planet_v = planet_type;               \n" +
            "}"
            ;

    protected static final String frag_shader = "\n" +
            "precision mediump float;                \n" +
            "                                                       \n" +
            "uniform sampler2D u_sunTextureUnit;                                              \n" +
            "uniform sampler2D u_mercuryTextureUnit;                                              \n" +
            "uniform sampler2D u_venusTextureUnit;                                              \n" +
            "uniform sampler2D u_earthTextureUnit;                                              \n" +
            "uniform sampler2D u_marsTextureUnit;                                              \n" +
            "uniform sampler2D u_jupiterTextureUnit;                                              \n" +
            "uniform sampler2D u_saturnTextureUnit;                                              \n" +
            "uniform sampler2D u_uranusTextureUnit;                                              \n" +
            "uniform sampler2D u_neptuneTextureUnit;                                              \n" +
            "\n" +
            "uniform sampler2D u_moonTextureUnit;                                              \n" +
            "\n" +
            "varying vec2 v_TextureCoordinates;                                              \n" +
            "varying float planet_v;                                              \n" +
            "\n" +
            "const float SUN_TYPE = 0.0;                  \n" +
            "const float MERCURY_TYPE = 1.0;                  \n" +
            "const float VENUS_TYPE = 2.0;                  \n" +
            "const float EARTH_TYPE = 3.0;                  \n" +
            "const float MARS_TYPE = 4.0;                  \n" +
            "const float JUPITER_TYPE = 5.0;                  \n" +
            "const float SATURN_TYPE = 6.0;                  \n" +
            "const float URANUS_TYPE = 7.0;                  \n" +
            "const float NEPTUNE_TYPE = 8.0;                  \n" +
            "\n" +
            "const float MOON_TYPE = 9.0;                  \n" +
            "\n" +
            "bool isEqual(float x, float y)\n" +
            "{\n" +
            "    return abs(x - y) < 0.01;\n" +
            "}                       \n" +
            "                 \n" +
            "void main()                                        \n" +
            "{   \n" +
            "    if(isEqual(planet_v, SUN_TYPE))\n" +
            "    {\n" +
            "        gl_FragColor = texture2D(u_sunTextureUnit, v_TextureCoordinates);                                                        \n" +
            "    }\n" +
            "    else if(isEqual(planet_v, MERCURY_TYPE))\n" +
            "    {\n" +
            "        gl_FragColor = texture2D(u_mercuryTextureUnit, v_TextureCoordinates);                                                        \n" +
            "    }\n" +
            "    else if(isEqual(planet_v, VENUS_TYPE))\n" +
            "    {\n" +
            "        gl_FragColor = texture2D(u_venusTextureUnit, v_TextureCoordinates);                                                        \n" +
            "    }\n" +
            "    else if(isEqual(planet_v, EARTH_TYPE))\n" +
            "    {\n" +
            "        gl_FragColor = texture2D(u_earthTextureUnit, v_TextureCoordinates);                                                        \n" +
            "    }\n" +
            "    else if(isEqual(planet_v, MARS_TYPE))\n" +
            "    {\n" +
            "        gl_FragColor = texture2D(u_marsTextureUnit, v_TextureCoordinates);                                                        \n" +
            "    }\n" +
            "    else if(isEqual(planet_v, JUPITER_TYPE))\n" +
            "    {\n" +
            "        gl_FragColor = texture2D(u_jupiterTextureUnit, v_TextureCoordinates);                                                        \n" +
            "    }\n" +
            "    else if(isEqual(planet_v, SATURN_TYPE))\n" +
            "    {\n" +
            "        gl_FragColor = texture2D(u_saturnTextureUnit, v_TextureCoordinates);                                                        \n" +
            "    }\n" +
            "    else if(isEqual(planet_v, URANUS_TYPE))\n" +
            "    {\n" +
            "        gl_FragColor = texture2D(u_uranusTextureUnit, v_TextureCoordinates);                                                        \n" +
            "    }\n" +
            "    else if(isEqual(planet_v, NEPTUNE_TYPE))\n" +
            "    {\n" +
            "        gl_FragColor = texture2D(u_neptuneTextureUnit, v_TextureCoordinates);                                                        \n" +
            "    }\n" +
            "    else if(isEqual(planet_v, MOON_TYPE))\n" +
            "    {\n" +
            "        gl_FragColor = texture2D(u_moonTextureUnit, v_TextureCoordinates);                                                        \n" +
            "    }\n" +
            "    else\n" +
            "    {\n" +
            "       gl_FragColor = vec4(0.0, 1.0, 0.0, 1.0);\n" +
            "    }                                           \n" +
            "}    "
            ;

    protected static final String vertex_shader_oneAttribute = "\n" +
            "attribute vec4 planet_Position;                  \n" +
            "\n" +
            "attribute vec2 planet_TextureCoordinates;                  \n" +
            "\n" +
            "uniform  mat4 u_sunMatrix;                  \n" +
            "uniform  mat4 u_mercuryMatrix;                  \n" +
            "uniform  mat4 u_venusMatrix;                  \n" +
            "uniform  mat4 u_earthMatrix;                  \n" +
            "uniform  mat4 u_marsMatrix;                  \n" +
            "uniform  mat4 u_jupiterMatrix;                  \n" +
            "uniform  mat4 u_saturnMatrix;                  \n" +
            "uniform  mat4 u_uranusMatrix;                  \n" +
            "uniform  mat4 u_neptuneMatrix;                  \n" +
            "\n" +
            "uniform  mat4 u_moonMatrix;                  \n" +
            "\n" +
            "uniform  float planet_type;                  \n" +
            "                            \n" +
            "varying  vec2 v_TextureCoordinates;                  \n" +
            "varying float planet_v;                                              \n" +
            "\n" +
            "const float SUN_TYPE = 0.0;                  \n" +
            "const float MERCURY_TYPE = 1.0;                  \n" +
            "const float VENUS_TYPE = 2.0;                  \n" +
            "const float EARTH_TYPE = 3.0;                  \n" +
            "const float MARS_TYPE = 4.0;                  \n" +
            "const float JUPITER_TYPE = 5.0;                  \n" +
            "const float SATURN_TYPE = 6.0;                  \n" +
            "const float URANUS_TYPE = 7.0;                  \n" +
            "const float NEPTUNE_TYPE = 8.0;                  \n" +
            "\n" +
            "const float MOON_TYPE = 9.0;                  \n" +
            "                       \n" +
            "bool isEqual(float x, float y)\n" +
            "{\n" +
            "    return abs(x - y) < 0.01;\n" +
            "}                       \n" +
            "                       \n" +
            "void main()                                   \n" +
            "{                                                         \n" +
            "    if(isEqual(planet_type, SUN_TYPE))\n" +
            "    {\n" +
            "       gl_Position = u_sunMatrix * planet_Position;\n" +
            "    }\n" +
            "    else if(isEqual(planet_type, MERCURY_TYPE))\n" +
            "    {\n" +
            "       gl_Position = u_mercuryMatrix * planet_Position;\n" +
            "    }\n" +
            "    else if(isEqual(planet_type, VENUS_TYPE))\n" +
            "    {\n" +
            "       gl_Position = u_venusMatrix * planet_Position;\n" +
            "    }\n" +
            "    else if(isEqual(planet_type, EARTH_TYPE))\n" +
            "    {\n" +
            "       gl_Position = u_earthMatrix * planet_Position;\n" +
            "    }\n" +
            "    else if(isEqual(planet_type, MARS_TYPE))\n" +
            "    {\n" +
            "       gl_Position = u_marsMatrix * planet_Position;\n" +
            "    }\n" +
            "    else if(isEqual(planet_type, JUPITER_TYPE))\n" +
            "    {\n" +
            "       gl_Position = u_jupiterMatrix * planet_Position;\n" +
            "    }\n" +
            "    else if(isEqual(planet_type, SATURN_TYPE))\n" +
            "    {\n" +
            "       gl_Position = u_saturnMatrix * planet_Position;\n" +
            "    }\n" +
            "    else if(isEqual(planet_type, URANUS_TYPE))\n" +
            "    {\n" +
            "       gl_Position = u_uranusMatrix * planet_Position;\n" +
            "    }\n" +
            "    else if(isEqual(planet_type, NEPTUNE_TYPE))\n" +
            "    {\n" +
            "       gl_Position = u_neptuneMatrix * planet_Position;\n" +
            "    }\n" +
            "    else if(isEqual(planet_type, MOON_TYPE))\n" +
            "    {\n" +
            "       gl_Position = u_moonMatrix * planet_Position;\n" +
            "    }\n" +
            "    v_TextureCoordinates = planet_TextureCoordinates;               \n" +
            "    planet_v = planet_type;               \n" +
            "}"
            ;

    protected static final String frag_shader_oneAttribute = "\n" +
            "precision mediump float;                \n" +
            "                                                       \n" +
            "uniform sampler2D u_sunTextureUnit;                                              \n" +
            "uniform sampler2D u_mercuryTextureUnit;                                              \n" +
            "uniform sampler2D u_venusTextureUnit;                                              \n" +
            "uniform sampler2D u_earthTextureUnit;                                              \n" +
            "uniform sampler2D u_marsTextureUnit;                                              \n" +
            "uniform sampler2D u_jupiterTextureUnit;                                              \n" +
            "uniform sampler2D u_saturnTextureUnit;                                              \n" +
            "uniform sampler2D u_uranusTextureUnit;                                              \n" +
            "uniform sampler2D u_neptuneTextureUnit;                                              \n" +
            "\n" +
            "uniform sampler2D u_moonTextureUnit;                                              \n" +
            "\n" +
            "varying vec2 v_TextureCoordinates;                                              \n" +
            "varying float planet_v;                                              \n" +
            "\n" +
            "const float SUN_TYPE = 0.0;                  \n" +
            "const float MERCURY_TYPE = 1.0;                  \n" +
            "const float VENUS_TYPE = 2.0;                  \n" +
            "const float EARTH_TYPE = 3.0;                  \n" +
            "const float MARS_TYPE = 4.0;                  \n" +
            "const float JUPITER_TYPE = 5.0;                  \n" +
            "const float SATURN_TYPE = 6.0;                  \n" +
            "const float URANUS_TYPE = 7.0;                  \n" +
            "const float NEPTUNE_TYPE = 8.0;                  \n" +
            "\n" +
            "const float MOON_TYPE = 9.0;                  \n" +
            "\n" +
            "bool isEqual(float x, float y)\n" +
            "{\n" +
            "    return abs(x - y) < 0.01;\n" +
            "}                       \n" +
            "                 \n" +
            "void main()                                        \n" +
            "{   \n" +
            "    if(isEqual(planet_v, SUN_TYPE))\n" +
            "    {\n" +
            "        gl_FragColor = texture2D(u_sunTextureUnit, v_TextureCoordinates);                                                        \n" +
            "    }\n" +
            "    else if(isEqual(planet_v, MERCURY_TYPE))\n" +
            "    {\n" +
            "        gl_FragColor = texture2D(u_mercuryTextureUnit, v_TextureCoordinates);                                                        \n" +
            "    }\n" +
            "    else if(isEqual(planet_v, VENUS_TYPE))\n" +
            "    {\n" +
            "        gl_FragColor = texture2D(u_venusTextureUnit, v_TextureCoordinates);                                                        \n" +
            "    }\n" +
            "    else if(isEqual(planet_v, EARTH_TYPE))\n" +
            "    {\n" +
            "        gl_FragColor = texture2D(u_earthTextureUnit, v_TextureCoordinates);                                                        \n" +
            "    }\n" +
            "    else if(isEqual(planet_v, MARS_TYPE))\n" +
            "    {\n" +
            "        gl_FragColor = texture2D(u_marsTextureUnit, v_TextureCoordinates);                                                        \n" +
            "    }\n" +
            "    else if(isEqual(planet_v, JUPITER_TYPE))\n" +
            "    {\n" +
            "        gl_FragColor = texture2D(u_jupiterTextureUnit, v_TextureCoordinates);                                                        \n" +
            "    }\n" +
            "    else if(isEqual(planet_v, SATURN_TYPE))\n" +
            "    {\n" +
            "        gl_FragColor = texture2D(u_saturnTextureUnit, v_TextureCoordinates);                                                        \n" +
            "    }\n" +
            "    else if(isEqual(planet_v, URANUS_TYPE))\n" +
            "    {\n" +
            "        gl_FragColor = texture2D(u_uranusTextureUnit, v_TextureCoordinates);                                                        \n" +
            "    }\n" +
            "    else if(isEqual(planet_v, NEPTUNE_TYPE))\n" +
            "    {\n" +
            "        gl_FragColor = texture2D(u_neptuneTextureUnit, v_TextureCoordinates);                                                        \n" +
            "    }\n" +
            "    else if(isEqual(planet_v, MOON_TYPE))\n" +
            "    {\n" +
            "        gl_FragColor = texture2D(u_moonTextureUnit, v_TextureCoordinates);                                                        \n" +
            "    }\n" +
            "    else\n" +
            "    {\n" +
            "       gl_FragColor = vec4(0.0, 1.0, 0.0, 1.0);\n" +
            "    }                                           \n" +
            "}    "
            ;


    protected static final int angleSpan = 10;// 将球进行单位切分的角度

    // 行星半径
    protected static final float SUN_RADIUS = 0.4f;                // 太阳半径
    protected static final float MERCURY_RADIUS = 0.02f;           // 水星半径
    protected static final float VENUS_RADIUS = 0.05f;             // 金星半径
    protected static final float EARTH_RADIUS = 0.05f;             // 地球半径
    protected static final float MARS_RADIUS = 0.03f;              // 火星半径
    protected static final float JUPITER_RADIUS = 0.1f;            // 木星半径
    protected static final float SATURN_RADIUS = 0.095f;           // 土星半径
    protected static final float URANUS_RADIUS = 0.07f;            // 天王星半径
    protected static final float NEPTUNE_RADIUS = 0.065f;          // 海王星半径

    protected static final float MOON_RADIUS = 0.015f;             // 月亮半径

    // 行星公转半径
    protected static final float SUN_TRACK_RADIUS = 0.0f;          // 太阳公转半径
    protected static final float MECURY_TRACK_RADIUS = 0.5f;       // 水星中心距离太阳中心距离
    protected static final float VENUS_TRACK_RADIUS = 0.65f;       // 金星中心距离太阳中心距离
    protected static final float EARTH_TRACK_RADIUS = 0.85f;        // 地球中心距离太阳中心距离
    protected static final float MARS_TRACK_RADIUS = 1.0f;         // 火星中心距离太阳中心距离
    protected static final float JUPITER_TRACK_RADIUS = 1.45f;      // 木星中心距离太阳中心距离
    protected static final float SATURN_TRACK_RADIUS = 1.85f;      // 土星中心距离太阳中心距离
    protected static final float URANUS_TRACK_RADIUS = 2.2f;       // 天王星中心距离太阳中心距离
    protected static final float NEPTUNE_TRACK_RADIUS = 2.5f;      // 海王星中心距离太阳中心距离

    protected static final float MOON_TRACK_RADIUS = 0.100f;      // 月亮中心距离地球中心距离

    // 行星公转速度(越大越慢)
    protected static final int SUN_TRIANGLE_COUNT = 0;             // 太阳公转轨迹的三角个数
    protected static final int MERCURY_TRIANGLE_COUNT = 300;       // 水星公转轨迹的三角个数
    protected static final int VENUS_TRIANGLE_COUNT = 880;         // 金星公转轨迹的三角个数
    protected static final int EARTH_TRIANGLE_COUNT = 1200;        // 地球公转轨迹的三角个数
    protected static final int MARS_TRIANGLE_COUNT = 2300;         // 火星公转轨迹的三角个数
    protected static final int JUPITER_TRIANGLE_COUNT = 5000;      // 木星公转轨迹的三角个数
    protected static final int SATURN_TRIANGLE_COUNT = 7000;       // 土星公转轨迹的三角个数
    protected static final int URANUS_TRIANGLE_COUNT = 9000;       // 天王星公转轨迹的三角个数
    protected static final int NEPTUNE_TRIANGLE_COUNT = 12000;     // 海王星公转轨迹的三角个数

    protected static final int MOON_TRIANGLE_COUNT = 160;           // 月亮公转轨迹的三角个数

    // 行星自传速度(越大越快)
    protected static final int SUN_ROTATE_ANGLE = 8;               // 太阳自传角度
    protected static final int MERCURY_ROTATE_ANGLE = 4;          // 水星自传角度
    protected static final int VENUS_ROTATE_ANGLE = 1;            // 水星自传角度
    protected static final int EARTH_ROTATE_ANGLE = 45;            // 地球自传角度
    protected static final int MARS_ROTATE_ANGLE = 45;             // 火星自传角度
    protected static final int JUPITER_ROTATE_ANGLE = 90;          // 木星自传角度
    protected static final int SATURN_ROTATE_ANGLE = 92;           // 土星自传角度
    protected static final int URANUS_ROTATE_ANGLE = 87;           // 天王星自传角度
    protected static final int NEPTUNE_ROTATE_ANGLE = 88;          // 海王星自传角度

    protected static final int MOON_ROTATE_ANGLE = 0;              // 月亮自传角度

    protected Context mContext;

    protected Object mLock = new Object();
    protected boolean mWantStop = false;

    protected int mDrawSep = 20;

    protected boolean mbStartedDraw = false;

    protected Thread mRendererThread;
    protected Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            synchronized (mLock){
                while (!mWantStop){
                    try {
                        draw();
                        mLock.wait(mDrawSep);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    protected float mEyeX = 0.0f;
    protected float mEyeY = 0.0f;
    protected float mEyeZ = 30.0f;

    protected float mNear = 20.0f;
    protected float mFar = 100.0f;



    protected String[][] mPlanetSParams = {
            {"sun.bmp", "u_sunTextureUnit", "u_sunMatrix", "sun_Position", "sun_TextureCoordinates", "planet_type", },
            {"mercury.bmp", "u_mercuryTextureUnit", "u_mercuryMatrix", "mercury_Position", "mercury_TextureCoordinates", "planet_type", },
            {"venus.bmp", "u_venusTextureUnit", "u_venusMatrix", "venus_Position", "venus_TextureCoordinates", "planet_type", },
            {"earth.bmp", "u_earthTextureUnit", "u_earthMatrix", "earth_Position", "earth_TextureCoordinates", "planet_type", },
            {"mars.bmp", "u_marsTextureUnit", "u_marsMatrix", "mars_Position", "mars_TextureCoordinates", "planet_type", },
            {"jupiter.bmp", "u_jupiterTextureUnit", "u_jupiterMatrix", "jupiter_Position", "jupiter_TextureCoordinates", "planet_type", },
            {"saturn.bmp", "u_saturnTextureUnit", "u_saturnMatrix", "saturn_Position", "saturn_TextureCoordinates", "planet_type", },
            {"uranus.bmp", "u_uranusTextureUnit", "u_uranusMatrix", "uranus_Position", "uranus_TextureCoordinates", "planet_type", },
            {"neptune.bmp", "u_neptuneTextureUnit", "u_neptuneMatrix", "neptune_Position", "neptune_TextureCoordinates", "planet_type", },
            {"moon.bmp", "u_moonTextureUnit", "u_moonMatrix", "moon_Position", "moon_TextureCoordinates", "planet_type", },
    };

    protected float[][] mPlanetFParams = {
            {SUN_RADIUS, SUN_TRACK_RADIUS,},
            {MERCURY_RADIUS, MECURY_TRACK_RADIUS, },
            {VENUS_RADIUS, VENUS_TRACK_RADIUS, },
            {EARTH_RADIUS, EARTH_TRACK_RADIUS, },
            {MARS_RADIUS, MARS_TRACK_RADIUS, },
            {JUPITER_RADIUS, JUPITER_TRACK_RADIUS, },
            {SATURN_RADIUS, SATURN_TRACK_RADIUS, },
            {URANUS_RADIUS, URANUS_TRACK_RADIUS, },
            {NEPTUNE_RADIUS, NEPTUNE_TRACK_RADIUS, },
            {MOON_RADIUS, MOON_TRACK_RADIUS, },
    };

    protected int[][] mPlanetIParams = {
            {angleSpan, SUN_TRIANGLE_COUNT, SUN_ROTATE_ANGLE},
            {angleSpan, MERCURY_TRIANGLE_COUNT, MERCURY_ROTATE_ANGLE},
            {angleSpan, VENUS_TRIANGLE_COUNT, VENUS_ROTATE_ANGLE},
            {angleSpan, EARTH_TRIANGLE_COUNT, EARTH_ROTATE_ANGLE},
            {angleSpan, MARS_TRIANGLE_COUNT, MARS_ROTATE_ANGLE},
            {angleSpan, JUPITER_TRIANGLE_COUNT, JUPITER_ROTATE_ANGLE},
            {angleSpan, SATURN_TRIANGLE_COUNT, SATURN_ROTATE_ANGLE},
            {angleSpan, URANUS_TRIANGLE_COUNT, URANUS_ROTATE_ANGLE},
            {angleSpan, NEPTUNE_TRIANGLE_COUNT, NEPTUNE_ROTATE_ANGLE},
            {angleSpan, MOON_TRIANGLE_COUNT, MOON_ROTATE_ANGLE},
    };


    protected final int P_TEXTURES = mPlanetSParams.length;
    protected final int P_VERTEX_UNIFORMS = P_TEXTURES * 2 + 1;
    protected final int P_FRAGMETN_UNIFORMS = P_TEXTURES;
    protected final int P_ATTRIBUTES = P_TEXTURES * 2;
    protected final int P_VARYINGS = 2;
    protected final int P_TEXUTRE_SIZE = 1440;

    protected boolean mbUseMutiAttributes = true;
    private FloatBuffer mVertexCoord;
    private FloatBuffer mTextureCoord;

    protected ATLog mLog = new ATLog(this.getClass().getName());

    protected Planet mSun;
    protected Planet mMercury;
    protected Planet mVenus;
    protected Planet mEarth;
    protected Planet mMars;
    protected Planet mJupiter;
    protected Planet mSaturn;
    protected Planet mUranus;
    protected Planet mNeptune;

    protected Moon mMoon;

    protected Planet[] mPlanets;



    public SolarSystemRender(Context context) {
        super();
        mContext = context;

        createPlanets();

        initAllCoordinate();

        initAllTracks();
    }

    protected void createPlanets(){
        int row = 0;
        mSun = createPlanet(row++);
        mMercury = createPlanet(row++);
        mVenus = createPlanet(row++);
        mEarth = createPlanet(row++);
        mMars = createPlanet(row++);
        mJupiter = createPlanet(row++);
        mSaturn = createPlanet(row++);
        mUranus = createPlanet(row++);
        mNeptune = createPlanet(row++);

        mMoon = createMoon(row++, mEarth);

        mPlanets = new Planet[row];
        row = 0;
        mPlanets[row++] = mSun;
        mPlanets[row++] = mMercury;
        mPlanets[row++] = mVenus;
        mPlanets[row++] = mEarth;
        mPlanets[row++] = mMars;
        mPlanets[row++] = mJupiter;
        mPlanets[row++] = mSaturn;
        mPlanets[row++] = mUranus;
        mPlanets[row++] = mNeptune;

        mPlanets[row++] = mMoon;
    }

    protected Planet createPlanet(int row){
        int scolume = 0;
        int fcolume = 0;
        int icolume = 0;
        return new Planet(mContext, row, row,
                mPlanetSParams[row][scolume++], mPlanetSParams[row][scolume++],
                mPlanetSParams[row][scolume++], mPlanetSParams[row][scolume++],
                mPlanetSParams[row][scolume++], mPlanetSParams[row][scolume++],

                mPlanetFParams[row][fcolume++], mPlanetIParams[row][icolume++],
                mPlanetFParams[row][fcolume++], mPlanetIParams[row][icolume++],
                mPlanetIParams[row][icolume++]);
    }

    protected Moon createMoon(int row, Planet parentPlanet){
        int scolume = 0;
        int fcolume = 0;
        int icolume = 0;
        Moon moon =  new Moon(mContext, row, row,
                mPlanetSParams[row][scolume++], mPlanetSParams[row][scolume++],
                mPlanetSParams[row][scolume++], mPlanetSParams[row][scolume++],
                mPlanetSParams[row][scolume++], mPlanetSParams[row][scolume++],

                mPlanetFParams[row][fcolume++], mPlanetIParams[row][icolume++],
                mPlanetFParams[row][fcolume++], mPlanetIParams[row][icolume++],
                mPlanetIParams[row][icolume++]);

        moon.setPlanet(parentPlanet);
        return moon;
    }

    protected void initAllTracks(){
        // x y
        float x = 0.0f;
        float y = 0.0f;
        float z = 0.0f;

//        mMercury.initTracks(x, y, z);
//        mVenus.initTracks(x, y, z);
//        mEarth.initTracks(x, y, z);
//        mMars.initTracks(x, y, z);
//        mJupiter.initTracks(x, y, z);
//        mSaturn.initTracks(x, y, z);
//        mUranus.initTracks(x, y, z);
//        mNeptune.initTracks(x, y, z);
//
//        mMoon.initTracks(x, y, z);

        for(int i = 0; i < mPlanets.length; ++i){
            mPlanets[i].initTracks(x, y, z);
        }
    }

    protected void initAllCoordinate() {
//        mSun.initCoordinates();
//        mMercury.initCoordinates();
//        mVenus.initCoordinates();
//        mEarth.initCoordinates();
//        mMars.initCoordinates();
//        mJupiter.initCoordinates();
//        mSaturn.initCoordinates();
//        mUranus.initCoordinates();
//        mNeptune.initCoordinates();
//
//        mMoon.initCoordinates();

        for(int i = 0; i < mPlanets.length; ++i){
            mPlanets[i].initCoordinates();
        }
    }

    protected void onUseOneAttribute(){
        int vertexSize = 0;
        int textureSize = 0;
        for(int i = 0; i < mPlanets.length; ++i){
            vertexSize += mPlanets[i].getVertexs().size();
            textureSize += mPlanets[i].getTextures().size();
        }

        float vertices[] = new float[vertexSize];
        float textures[] = new float[textureSize];
        int voffset = 0;
        int toffset = 0;
        for(int k = 0; k < mPlanets.length; ++k){
            ArrayList<Float> vertexArr = mPlanets[k].getVertexs();
            ArrayList<Float> textureArr = mPlanets[k].getTextures();
            for (int i = 0; i < vertexArr.size(); i++) {
                vertices[i + voffset] = vertexArr.get(i);
            }

            for (int i = 0; i < textureArr.size(); i++) {
                textures[i + toffset] = textureArr.get(i);
            }

            voffset += vertexArr.size();
            toffset += textureArr.size();
        }

        mVertexCoord = ByteBuffer.allocateDirect(vertices.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        mVertexCoord.put(vertices);
        mVertexCoord.position(0);

        mTextureCoord = ByteBuffer.allocateDirect(textures.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        mTextureCoord.put(textures);
        mTextureCoord.position(0);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        if(!isSupported()){
            String err = String.format("your phone is not supported,err:%s", mErrMsg);
            Toast.makeText(mContext, err, Toast.LENGTH_LONG).show();
        }

        if(mbUseMutiAttributes){
            mProgram = OpenGLUtils.loadProgram(vertex_shader, frag_shader);
        }else{
            mProgram = OpenGLUtils.loadProgram(vertex_shader_oneAttribute, frag_shader_oneAttribute);
            onUseOneAttribute();

            int mPostionLoc = GLES20.glGetAttribLocation(mProgram, "planet_Position");
            GLES20.glVertexAttribPointer(mPostionLoc, 3, GLES20.GL_FLOAT, false, 0, mVertexCoord);
            GLES20.glEnableVertexAttribArray(mPostionLoc);

            int mTextureCoordLoc = GLES20.glGetAttribLocation(mProgram, "planet_TextureCoordinates");
            GLES20.glVertexAttribPointer(mTextureCoordLoc, 2, GLES20.GL_FLOAT, false, 0, mTextureCoord);
            GLES20.glEnableVertexAttribArray(mTextureCoordLoc);
        }
        GLES20.glUseProgram(mProgram);
//        GLES20.glDepthRangef(20, 100);
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

//        mSun.onSurfaceCreated(gl, config, mProgram);
//        mMercury.onSurfaceCreated(gl, config, mProgram);
//        mVenus.onSurfaceCreated(gl, config, mProgram);
//        mEarth.onSurfaceCreated(gl, config, mProgram);
//        mMars.onSurfaceCreated(gl, config, mProgram);
//        mJupiter.onSurfaceCreated(gl, config, mProgram);
//        mSaturn.onSurfaceCreated(gl, config, mProgram);
//        mUranus.onSurfaceCreated(gl, config, mProgram);
//        mNeptune.onSurfaceCreated(gl, config, mProgram);
//
//        mMoon.onSurfaceCreated(gl, config, mProgram);

        for(int i = 0; i < mPlanets.length; ++i){
            mPlanets[i].onSurfaceCreated(gl, config, mProgram);
        }

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        super.onSurfaceChanged(gl, width, height);
        projectFrustumMatrix(width, height);
//        mSun.onSurfaceChanged(gl, width, height);
//        mMercury.onSurfaceChanged(gl, width, height);
//        mVenus.onSurfaceChanged(gl, width, height);
//        mEarth.onSurfaceChanged(gl, width, height);
//        mMars.onSurfaceChanged(gl, width, height);
//        mJupiter.onSurfaceChanged(gl, width, height);
//        mSaturn.onSurfaceChanged(gl, width, height);
//        mUranus.onSurfaceChanged(gl, width, height);
//        mNeptune.onSurfaceChanged(gl, width, height);
//
//        mMoon.onSurfaceChanged(gl, width, height);

        for(int i = 0; i < mPlanets.length; ++i){
            mPlanets[i].onSurfaceChanged(gl, width, height);
        }
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT|GLES20.GL_DEPTH_BUFFER_BIT);

//        mSun.onDrawFrame(gl);
//        mMercury.onDrawFrame(gl);
//        mVenus.onDrawFrame(gl);
//        mEarth.onDrawFrame(gl);
//        mMars.onDrawFrame(gl);
//        mJupiter.onDrawFrame(gl);
//        mSaturn.onDrawFrame(gl);
//        mUranus.onDrawFrame(gl);
//        mNeptune.onDrawFrame(gl);
//
//        mMoon.onDrawFrame(gl);
        int offset = 0;
        for(int i = 0; i < mPlanets.length; ++i){
            mPlanets[i].onDrawFrame(gl, offset);
            offset += mPlanets[i].getVertexPointCount();
        }

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

        mbStartedDraw = true;
    }

    @Override
    protected void localReleaseGL() {
        super.localReleaseGL();
//        mSun.releaseGL();
//        mMercury.releaseGL();
//        mVenus.releaseGL();
//        mEarth.releaseGL();
//        mMars.releaseGL();
//        mJupiter.releaseGL();
//        mSaturn.releaseGL();
//        mUranus.releaseGL();
//        mNeptune.releaseGL();
//
//        mMoon.releaseGL();

        for(int i = 0; i < mPlanets.length; ++i){
            mPlanets[i].releaseGL();
        }
    }

    @Override
    public void onPause() {
        mWantStop = true;
        synchronized (mLock){
            mLock.notify();
        }

        try {
            mRendererThread.join();
            mRendererThread = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mbStartedDraw = false;

        if(null == mRendererThread){
            mWantStop = false;
            mRendererThread = new Thread(mRunnable);
            mRendererThread.start();
        }
    }


    @Override
    public boolean isSupported() {
        int maxvu = OpenGLUtils.getMaxVectexUniforms();
        int maxfu = OpenGLUtils.getMaxFragmentUniforms();
        int maxvarying = OpenGLUtils.getMaxVaryings();
        int maxts = OpenGLUtils.getMaxTextureSize();
        int maxat = OpenGLUtils.getMaxVertexAttributes();
        int maxtis = OpenGLUtils.getMaxFragmentTextureImages();

        if(maxvu < P_VERTEX_UNIFORMS){
            mErrMsg = String.format("maxvu less than needed, max:%d,need:%d", maxvu, P_VERTEX_UNIFORMS);
            mLog.error(mErrMsg);
            return false;
        }

        if(maxfu < P_FRAGMETN_UNIFORMS){
            mErrMsg = String.format("maxfu less than needed, max:%d,need:%d", maxfu, P_FRAGMETN_UNIFORMS);
            mLog.error(mErrMsg);
            return false;
        }

        if(maxvarying < P_VARYINGS){
            mErrMsg = String.format("maxvarying less than needed, max:%d,need:%d", maxvarying, P_VARYINGS);
            mLog.error(mErrMsg);
            return false;
        }

        if(maxtis < P_TEXTURES){
            mErrMsg = String.format("maxtis less than needed, max:%d,need:%d", maxtis, P_TEXTURES);
            mLog.error(mErrMsg);
            return false;
        }

        if(maxts < P_TEXUTRE_SIZE){
            mErrMsg = String.format("maxts less than needed, max:%d,need:%d", maxts, P_TEXUTRE_SIZE);
            mLog.error(mErrMsg);
            return false;
        }

        if(maxat < P_ATTRIBUTES){
            mLog.warn("maxat less than needed, max:%d,need:%d, change to oneAttribute", maxat, P_ATTRIBUTES);
            mbUseMutiAttributes = false;
            Planet.setEnableMutiAttribute(mbUseMutiAttributes);
        }

        return true;
    }

    protected void draw(){
        if(!mbStartedDraw){
            return;
        }

        if(null != mSurfaceView){
            mSurfaceView.requestRender();
        }
    }

    public void projectFrustumMatrix(int width, int height){
        final float aspectRatio = width > height ?
                (float) width / (float) height :
                (float) height / (float) width;
//        final float cx = -16.0f;
//        final float cy = 8.0f;
//        final float cz = 45.0f;


        if(width > height){
            MatrixUtil.setProjectFrustum(-aspectRatio, aspectRatio, -1.0f, 1.0f, mNear, mFar);
        }else{
            MatrixUtil.setProjectFrustum(-1.0f, 1.0f, -aspectRatio, aspectRatio, mNear, mFar);
        }

        MatrixUtil.setCamera(mEyeX, mEyeY, mEyeZ, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
    }

    public void setEye(float x, float y, float z){
        mEyeX = x;
        mEyeY = y;
        mEyeZ = z;
        MatrixUtil.setCamera(mEyeX, mEyeY, mEyeZ, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
    }

    public void addEyeY(){
        mEyeY += 1.0f;
        setEye(mEyeX, mEyeY, mEyeZ);
    }

    public float getEyeX(){
        return mEyeX;
    }

    public float getEyeY(){
        return mEyeY;
    }

    public float getEyeZ(){
        return mEyeZ;
    }

    public float getNear(){
        return mNear;
    }

    public float getFar(){
        return mFar;
    }

}
