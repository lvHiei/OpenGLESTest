package com.lvhiei.openglestest;

import android.content.Context;
import android.os.Process;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class Monitor {

    public static void getFdList(){
        try{
            String fds = "/proc/" + Process.myPid() + "/fd/";
            File file = new File(fds);
            if(file.canRead()){
                Log.e("lvhieie", "getFdList canRead " + fds);
            }

            Runtime mRuntime = Runtime.getRuntime();
            try {
                //Process中封装了返回的结果和执行错误的结果
                java.lang.Process mProcess = mRuntime.exec("ls -al " + fds);
                BufferedReader mReader = new BufferedReader(new InputStreamReader(mProcess.getInputStream()));
                StringBuffer mRespBuff = new StringBuffer();
                char[] buff = new char[1024];
                int ch = 0;
                while ((ch = mReader.read(buff)) != -1) {
                    mRespBuff.append(buff, 0, ch);
                }
                mReader.close();
                Log.e("lvhieie", "getFdList fdlist " + mRespBuff.toString());
//                System.out.print(mRespBuff.toString());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if(file.isDirectory()){

            }
        }catch (Exception e){
            Log.e("lvhieie", Log.getStackTraceString(e));
        }
    }}
