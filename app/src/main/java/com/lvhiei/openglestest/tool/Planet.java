package com.lvhiei.openglestest.tool;

import android.content.Context;
import android.opengl.GLES20;

import com.lvhiei.openglestest.render.MatrixUtil;
import com.lvhiei.openglestest.render.OpenGLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by mj on 17-11-8.
 */


public class Planet {
    protected static final float UNIT_SIZE = 1.0f;// 单位尺寸
    protected static final int COORDS_PER_VERTEX = 3;
    protected static final int COORDS_PER_TEXCOORD = 2;


    protected float mRadius;                    // 行星半径
    protected int mAngleSpan;                   // 将球进行单位切分的角度

    protected float mTrackRadius;               // 轨迹半径
    protected int mTrackTriangleCount;          // 轨迹单位三角个数

    protected int mRotateAngle;                 // 行星自转角度

    protected int mPlanetType;                  // 行星类型

    protected FloatBuffer mVertexCoord;         // 顶点坐标
    protected FloatBuffer mTextureCoord;        // 纹理坐标
    private ArrayList<Float> mVertexs;          // 顶点坐标
    private ArrayList<Float> mTexCoords;        // 纹理坐标
    protected int mVertexCount;                 // 顶点坐标个数
    protected int mTextureCount;                // 纹理坐标个数

    protected ArrayList<Point> mTracks;         // 行星公转运行轨迹
    protected int mTrackIdx;                    // 行星公转轨迹索引

    protected MatrixUtil mMatrixUtil;           // 转换矩阵

    protected int mTexturePosition;             // 纹理位置(第几个纹理)
    protected int mTextureId;                   // 纹理id
    protected String mTexturePicName;           // 纹理图片名称
    protected int mPostionLoc;                  // 顶点坐标 attribute位置
    protected int mTextureCoordLoc;             // 纹理坐标 attribute位置
    protected int mTextureLoc;                  // 纹理id uniform位置
    protected int mMatrixLoc;                   // 转换矩阵 uniform位置
    protected int mTypeLoc;                     // 行星类型 uniform位置
    protected String maPositionName;            // 顶点坐标attribute 名
    protected String maTextureCoodName;         // 纹理坐标attribute 名
    protected String muTextureName;             // 纹理uniform 名
    protected String muMatrixName;              // 转换矩阵uniform 名
    protected String muTypeName;                // 行星类型uniform 名

    protected Vector3 mRotateVec;               // 自传轴


    protected Context mContext;

    public Planet(Context context, int type, int texPos, String textName, String tname, String mname, String posName, String tcName, String typeName, float r, int angleSpan){
        this(context, type, texPos, textName, tname, mname, posName, tcName, typeName, r, angleSpan, 0.0f, 0, 0);
    }

    public Planet(Context context, int type, int texPos, String textName, String tname, String mname, String posName, String tcName, String typeName, float r, int angleSpan, float t_r, int tcount, int rotateAngle){
        mContext = context;
        mPlanetType = type;
        mTexturePosition = texPos;
        mTexturePicName = textName;
        muTextureName = tname;
        muMatrixName = mname;
        maPositionName = posName;
        maTextureCoodName = tcName;
        muTypeName = typeName;
        mRadius = r;
        mAngleSpan = angleSpan;
        mTrackRadius = t_r;
        mTrackTriangleCount = tcount;
        mRotateAngle = rotateAngle;

        mVertexs = new ArrayList<>();
        mTexCoords = new ArrayList<>();
        mTracks = new ArrayList<>();

        mVertexCount = 0;
        mTextureCount = 0;
        mTrackIdx = 0;

        mMatrixUtil = new MatrixUtil();

        mTextureId = 0;
        mTextureLoc = -1;
        mMatrixLoc = -1;

        mRotateVec = new Vector3(0.0f, 1.0f, 0.0f);
    }

    public void initCoordinates(){

        mVertexs.clear();
        mTexCoords.clear();

        for (int vAngle = 0; vAngle < 180; vAngle = vAngle + mAngleSpan)// 垂直方向mAngleSpan度一份
        {
            for (int hAngle = 0; hAngle <= 360; hAngle = hAngle + mAngleSpan)// 水平方向mAngleSpan度一份
            {
                // 纵向横向各到一个角度后计算对应的此点在球面上的坐标
                float x0 = (float) (mRadius * UNIT_SIZE
                        * Math.sin(Math.toRadians(vAngle)) * Math.cos(Math
                        .toRadians(hAngle)));
                float y0 = (float) (mRadius * UNIT_SIZE
                        * Math.sin(Math.toRadians(vAngle)) * Math.sin(Math
                        .toRadians(hAngle)));
                float z0 = (float) (mRadius * UNIT_SIZE * Math.cos(Math
                        .toRadians(vAngle)));

                float x1 = (float) (mRadius * UNIT_SIZE
                        * Math.sin(Math.toRadians(vAngle)) * Math.cos(Math
                        .toRadians(hAngle + mAngleSpan)));
                float y1 = (float) (mRadius * UNIT_SIZE
                        * Math.sin(Math.toRadians(vAngle)) * Math.sin(Math
                        .toRadians(hAngle + mAngleSpan)));
                float z1 = (float) (mRadius * UNIT_SIZE * Math.cos(Math
                        .toRadians(vAngle)));

                float x2 = (float) (mRadius * UNIT_SIZE
                        * Math.sin(Math.toRadians(vAngle + mAngleSpan)) * Math
                        .cos(Math.toRadians(hAngle + mAngleSpan)));
                float y2 = (float) (mRadius * UNIT_SIZE
                        * Math.sin(Math.toRadians(vAngle + mAngleSpan)) * Math
                        .sin(Math.toRadians(hAngle + mAngleSpan)));
                float z2 = (float) (mRadius * UNIT_SIZE * Math.cos(Math
                        .toRadians(vAngle + mAngleSpan)));

                float x3 = (float) (mRadius * UNIT_SIZE
                        * Math.sin(Math.toRadians(vAngle + mAngleSpan)) * Math
                        .cos(Math.toRadians(hAngle)));
                float y3 = (float) (mRadius * UNIT_SIZE
                        * Math.sin(Math.toRadians(vAngle + mAngleSpan)) * Math
                        .sin(Math.toRadians(hAngle)));
                float z3 = (float) (mRadius * UNIT_SIZE * Math.cos(Math
                        .toRadians(vAngle + mAngleSpan)));


                float s0 = hAngle / 360.0f;
                float s1 = (hAngle + mAngleSpan) / 360.0f;
                float t0 = 1 - vAngle / 180.0f;
                float t1 = 1 - (vAngle + mAngleSpan) / 180.0f;


                // 将计算出来的XYZ坐标加入存放顶点坐标的ArrayList
                mVertexs.add(x1);
                mVertexs.add(y1);
                mVertexs.add(z1);
                mVertexs.add(x3);
                mVertexs.add(y3);
                mVertexs.add(z3);
                mVertexs.add(x0);
                mVertexs.add(y0);
                mVertexs.add(z0);

                mTexCoords.add(s1);// x1 y1 z1对应纹理坐标
                mTexCoords.add(t0);
                mTexCoords.add(s0);// x3 y3 z3对应纹理坐标
                mTexCoords.add(t1);
                mTexCoords.add(s0);// x0 y0 z0对应纹理坐标
                mTexCoords.add(t0);


                mVertexs.add(x1);
                mVertexs.add(y1);
                mVertexs.add(z1);
                mVertexs.add(x2);
                mVertexs.add(y2);
                mVertexs.add(z2);
                mVertexs.add(x3);
                mVertexs.add(y3);
                mVertexs.add(z3);

                mTexCoords.add(s1);// x1 y1 z1对应纹理坐标
                mTexCoords.add(t0);
                mTexCoords.add(s1);// x2 y2 z2对应纹理坐标
                mTexCoords.add(t1);
                mTexCoords.add(s0);// x3 y3 z3对应纹理坐标
                mTexCoords.add(t1);
            }
        }

        mVertexCount = mVertexs.size()/COORDS_PER_VERTEX;
        // 将mVertexs中的坐标值转存到一个float数组中
        float vertices[] = new float[mVertexCount * COORDS_PER_VERTEX];
        for (int i = 0; i < mVertexs.size(); i++) {
            vertices[i] = mVertexs.get(i);
        }

        mTextureCount = mTexCoords.size()/COORDS_PER_TEXCOORD;
        // 将mVertexs中的坐标值转存到一个float数组中
        float textures[] = new float[mTextureCount * COORDS_PER_TEXCOORD];
        for (int i = 0; i < mTexCoords.size(); i++) {
            textures[i] = mTexCoords.get(i);
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

    /**
     * 初始化轨迹顶点
     * @param x 圆心x
     * @param y 圆心y
     * @param z 圆心z
     */
    public void initTracks(float x, float y, float z){
        mTracks.clear();

        // 轨迹在xz平面
        for (int i = 0; i < mTrackTriangleCount + 1; i++) {
            float angleInRadians = ((float) i / (float) mTrackTriangleCount)
                    * ((float) Math.PI * 2f);
            float tx = x + mTrackRadius * (float)Math.cos(angleInRadians);
            float ty = y;
            float tz = z + mTrackRadius * (float)Math.sin(angleInRadians);
            mTracks.add(new Point(tx, ty, tz));
        }

    }

    public ArrayList<Float> getVertexs(){
        return mVertexs;
    }

    public ArrayList<Float> getTextures(){
        return mTexCoords;
    }

    public int getPlanetType(){
        return mPlanetType;
    }

    public void projectFrustumMatrix(int width, int height){
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
            mMatrixUtil.setProjectFrustum(-aspectRatio, aspectRatio, -1.0f, 1.0f, near, far);
        }else{
            mMatrixUtil.setProjectFrustum(-1.0f, 1.0f, -aspectRatio, aspectRatio, near, far);
        }

        mMatrixUtil.setCamera(cx, cy, cz, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config, int program){
        mPostionLoc = GLES20.glGetAttribLocation(program, maPositionName);
        GLES20.glVertexAttribPointer(mPostionLoc, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, 0, mVertexCoord);
        GLES20.glEnableVertexAttribArray(mPostionLoc);

        mTextureCoordLoc = GLES20.glGetAttribLocation(program, maTextureCoodName);
        GLES20.glVertexAttribPointer(mTextureCoordLoc, COORDS_PER_TEXCOORD, GLES20.GL_FLOAT, false, 0, mTextureCoord);
        GLES20.glEnableVertexAttribArray(mTextureCoordLoc);

        mTextureLoc = GLES20.glGetUniformLocation(program, muTextureName);
        mMatrixLoc = GLES20.glGetUniformLocation(program, muMatrixName);
        mTypeLoc = GLES20.glGetUniformLocation(program, muTypeName);

        mTextureId = OpenGLUtils.loadTexture(mContext, mTexturePicName);
    }

    public void onSurfaceChanged(GL10 gl, int width, int height){
        projectFrustumMatrix(width, height);
    }

    public void onDrawFrame(GL10 gl){
        setTranslate();

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + mTexturePosition);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureId);
        GLES20.glUniform1i(mTextureLoc, mTexturePosition);
        GLES20.glUniform1f(mTypeLoc, mPlanetType*1.0f);
        GLES20.glUniformMatrix4fv(mMatrixLoc, 1, false, mMatrixUtil.getFinalMatrix(), 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, mVertexCount);
    }

    public void releaseGL(){
        if(mTextureId != 0){
            OpenGLUtils.deleteTexture(mTextureId);
            mTextureId = 0;
        }
    }

    public void setTranslate(){
        if(0 == mTracks.size()){
            return;
        }

        Point pe = new Point(mTracks.get(mTrackIdx));

        mMatrixUtil.initTranslate();
        mMatrixUtil.setRotate(mRotateAngle, mRotateVec.x, mRotateVec.y, mRotateVec.z);
        mMatrixUtil.setTranstate(pe.x, pe.y, pe.z);

        ++mTrackIdx;
        if(mTrackIdx >= mTracks.size()){
            mTrackIdx = 0;
        }
    }

    public void setRotateVector(Vector3 vector){
        mRotateVec.x = vector.x;
        mRotateVec.y = vector.y;
        mRotateVec.z = vector.z;
    }
}
