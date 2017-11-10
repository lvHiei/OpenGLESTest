package com.lvhiei.openglestest.tool;

import android.content.Context;

import com.lvhiei.openglestest.render.SunRender;

/**
 * Created by mj on 17-11-9.
 */


public class Moon extends Planet {
    protected Planet mPlanet;   // 该卫星所绕的行星
    protected Point mLastPoint = null;

    public Moon(Context context, int type, int texPos, String textName, String tname, String mname, String posName, String tcName, String typeName, float r, int angleSpan) {
        super(context, type, texPos, textName, tname, mname, posName, tcName, typeName, r, angleSpan);
    }

    public Moon(Context context, int type, int texPos, String textName, String tname, String mname, String posName, String tcName, String typeName, float r, int angleSpan, float t_r, int tcount, int rotateAngle) {
        super(context, type, texPos, textName, tname, mname, posName, tcName, typeName, r, angleSpan, t_r, tcount, rotateAngle);
    }

    public void setPlanet(Planet planet){
        mPlanet = planet;
    }

    @Override
    public void setTranslate() {
        if(mPlanet == null){
            return;
        }

        Point pp = new Point(mPlanet.getThisPoint());

        Point pm = new Point(mTracks.get(mTrackIdx));

        pm.add(pp);
        Point spm = new Point(pm);

        if(mLastPoint != null){
            spm.minus(mLastPoint);
        }

        mMatrixUtil.initTranslate();
        mMatrixUtil.setRotate(mRotateAngle, mRotateVec.x, mRotateVec.y, mRotateVec.z);
        mMatrixUtil.setTranstate(pm.x, pm.y, pm.z);

//        mMatrixUtil.setTranstate(spm.x, spm.y, spm.z);

        mLastPoint = pm;

        ++mTrackIdx;
        if(mTrackIdx >= mTracks.size()){
            mTrackIdx = 0;
        }
    }
}
