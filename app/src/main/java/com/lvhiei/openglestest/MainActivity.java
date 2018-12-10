package com.lvhiei.openglestest;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private int mRenderId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        initCtrl();
    }


    private View.OnClickListener mListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            switch (v.getId()){
//                case R.id.btn_firstGLESPrograme:
//                    break;
//                default:
//                    break;
//            }
            mRenderId = v.getId();
            Intent intent = new Intent(MainActivity.this, GLES20Activity.class);

            Bundle bundle = new Bundle();
            bundle.putInt("renderid", mRenderId);

            intent.putExtras(bundle);

            startActivity(intent);
        }
    };

    private void initCtrl(){
        findViewById(R.id.btn_firstGLESPrograme).setOnClickListener(mListener);
        findViewById(R.id.btn_drawTriangle).setOnClickListener(mListener);
        findViewById(R.id.btn_drawSquare).setOnClickListener(mListener);
        findViewById(R.id.btn_drawCircle).setOnClickListener(mListener);
        findViewById(R.id.btn_drawCube).setOnClickListener(mListener);
        findViewById(R.id.btn_drawColorCube).setOnClickListener(mListener);
        findViewById(R.id.btn_translateColorCube).setOnClickListener(mListener);
        findViewById(R.id.btn_rotateColorCube).setOnClickListener(mListener);
        findViewById(R.id.btn_scaleColorCube).setOnClickListener(mListener);
        findViewById(R.id.btn_drawBall).setOnClickListener(mListener);
        findViewById(R.id.btn_textureSquare).setOnClickListener(mListener);
        findViewById(R.id.btn_testBlitFrameBuffer).setOnClickListener(mListener);
        findViewById(R.id.btn_textureCube).setOnClickListener(mListener);
        findViewById(R.id.btn_textureBall).setOnClickListener(mListener);
        findViewById(R.id.btn_drawSun).setOnClickListener(mListener);
        findViewById(R.id.btn_drawSolarSystem).setOnClickListener(mListener);
    }















//
//    /**
//     * A native method that is implemented by the 'native-lib' native library,
//     * which is packaged with this application.
//     */
//    public native String stringFromJNI();
//
//    // Used to load the 'native-lib' library on application startup.
//    static {
//        System.loadLibrary("native-lib");
//    }
}
