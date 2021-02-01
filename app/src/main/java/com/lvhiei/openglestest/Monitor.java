package com.lvhiei.openglestest;

import android.os.Process;
import android.util.Log;

import com.lvhiei.openglestest.log.ATLog;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class Monitor {
    private static final String TAG = Monitor.class.getName();
    private static ATLog logger = new ATLog(TAG);

    public static void getFdList(){
        try{
            String absName = getProcFile("fd");
            execCommand("ls -al " + absName);
        }catch (Exception e){
            logger.e(Log.getStackTraceString(e));
        }
    }

    public static void getProcInfo(){
        String procPath =  "/proc/" + Process.myPid();
        File fileTree = new File(procPath);
        if(!fileTree.exists() || !fileTree.isDirectory()){
            return;
        }

        if(fileTree.canRead()){
            logger.info("can read %s", procPath);
        }

        for(File file : fileTree.listFiles()){
            if(!file.isDirectory()){
                String cmd = "cat " + file.getAbsoluteFile();
                execCommand(cmd);
            }else{
                String cmd = "ls -al " + file.getAbsoluteFile();
                execCommand(cmd);
            }
        }

    }


    public static String getProcFile(String name){
        String absname = "/proc/" + Process.myPid() + "/" + name + "/";
        File file = new File(absname);
        if(file.canRead()){
            logger.info("file %s canRead", absname);
        }
        return absname;
    }

    public static void execCommand(String cmd){
        Runtime runtime = Runtime.getRuntime();
        try {
            //Process中封装了返回的结果和执行错误的结果
            java.lang.Process process = runtime.exec(cmd);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuffer respBuff = new StringBuffer();
            char[] buff = new char[1024];
            int ch = 0;
            while ((ch = reader.read(buff)) != -1) {
                respBuff.append(buff, 0, ch);
            }
            reader.close();
            logger.info("exec cmd %s got %s", cmd, respBuff.toString());
            System.out.print(respBuff.toString());
        } catch (IOException e) {
            logger.e(Log.getStackTraceString(e));
        }
    }
}
