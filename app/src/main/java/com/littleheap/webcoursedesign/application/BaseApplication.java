package com.littleheap.webcoursedesign.application;

import android.app.Application;
import com.littleheap.webcoursedesign.utils.StaticClass;

import cn.bmob.v3.Bmob;

/**
 * Created by Administrator on 2017/7/19 0019.
 */

public class BaseApplication extends Application {

    //创建Application
    @Override
    public void onCreate() {
        super.onCreate();
        //Bmob
        Bmob.initialize(this, StaticClass.BMOB_APP_ID);
    }
}
