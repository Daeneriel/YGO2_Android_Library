package cn.unicorn369.library;

import android.app.Activity;
//import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;

import java.io.File;

import cn.unicorn369.library.R;
import cn.unicorn369.library.utils.IOUtils;
//import cn.unicorn369.library.utils.MD5Util;
import cn.unicorn369.library.utils.OpenFileUtil;

public class API {
    private static Activity unityActivity;
    public static String SDCARD_DIR = Environment.getExternalStorageDirectory().toString();
    public static String GAME_DIR = SDCARD_DIR + "/ygocore/";

    public static Activity getActivity() {
        if (null == unityActivity) {
            try {
                Class<?> classtype = Class.forName("com.unity3d.player.UnityPlayer");
                Activity activity = (Activity)classtype.getDeclaredField("currentActivity").get(classtype);
                unityActivity = activity;
            } catch (ClassNotFoundException e) {

            } catch (IllegalAccessException e) {

            } catch (NoSuchFieldException e) {

            }
        }
        return unityActivity;
    }

    //显示Unity发送过来的内容
    public void showToast(String content) {
        Toast.makeText(getActivity(), content, Toast.LENGTH_SHORT).show();
    }

    //获取游戏目录 (用于YGOPro2获取SD卡路径)
    public String GamePath(String path) {
        GAME_DIR = SDCARD_DIR + path;
        createFolder(GAME_DIR);
        return GAME_DIR;
    }

    //判断安卓版本 (用于判断YGOPro2是否支持使用libgdiplus)
    public boolean APIVersion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return true;
        } else {
			return false;
        }
    }

    //打开文件 (用于YGOPro2中 "代码编辑" 功能)
    public void openFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            Toast.makeText(getActivity(), R.string.Tips_Not_File, Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
            }
            Toast.makeText(getActivity(), "Open File: " + filePath, Toast.LENGTH_SHORT).show();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_VIEW);
            unityActivity.startActivity(OpenFileUtil.openFile(filePath));
        }
    }

    //删除文件
    public void delete(String path) {
        IOUtils.delete(new File(path));
    }

    //创建文件夹
    public void createFolder(String path) {
        IOUtils.createFolder(new File(path));
    }

    //加入QQ群
    public void doJoinQQGroup(String key) {//jo.Call("doJoinQQGroup", key);
        joinQQGroup(key);
    }
    public boolean joinQQGroup(String key) {//jo.Call<bool>("joinQQGroup", key);
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        try {
            Toast.makeText(getActivity(), R.string.Tips_Open_QQ, Toast.LENGTH_SHORT).show();
            unityActivity.startActivity(intent);
            return true;
        } catch (Exception e) {
            Toast.makeText(getActivity(), R.string.Tips_Not_QQ, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    //重启APP
    public void doRestart() {
        new Thread(){
            public void run(){
                Intent launch=unityActivity.getBaseContext().getPackageManager().getLaunchIntentForPackage(unityActivity.getBaseContext().getPackageName());
                launch.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                unityActivity.startActivity(launch);
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        }.start();
        unityActivity.finish();
    }

}
