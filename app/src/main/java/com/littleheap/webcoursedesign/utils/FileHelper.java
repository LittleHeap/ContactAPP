package com.littleheap.webcoursedesign.utils;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by wangs on 2017/10/13.
 */

public class FileHelper {
    private Context mContext;

    //空参数构造函数，传入的值为空时，不出错
    public FileHelper() {
    }

    public FileHelper(Context mContext) {
        super();
        this.mContext = mContext;
    }

    /*
    * 定义文件保存的方法，写入到文件中，所以是输出流
    * */
    public void save(String name, String content) throws Exception {
        //Context.MODE_PRIVATE权限，只有自身程序才能访问，而且写入的内容会覆盖文本内原有内容
        FileOutputStream output = mContext.openFileOutput(name, Context.MODE_PRIVATE);
        output.write(content.getBytes());  //将String字符串以字节流的形式写入到输出流中
        output.close();         //关闭输出流
    }


    /*
    * 定义文件读取的方法
    * */
    public String read(String filename) throws IOException {
        //打开文件输入流
        FileInputStream input = mContext.openFileInput(filename);
        //定义1M的缓冲区
        byte[] temp = new byte[1024];
        //定义字符串变量
        StringBuilder sb = new StringBuilder("");
        int len = 0;
        //读取文件内容，当文件内容长度大于0时，
        while ((len = input.read(temp)) > 0) {
            //把字条串连接到尾部
            sb.append(new String(temp, 0, len));
        }
        //关闭输入流
        input.close();
        //返回字符串
        return sb.toString();
    }
}
