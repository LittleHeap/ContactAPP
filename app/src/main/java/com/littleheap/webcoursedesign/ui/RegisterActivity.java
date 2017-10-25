package com.littleheap.webcoursedesign.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.littleheap.webcoursedesign.R;
import com.littleheap.webcoursedesign.entity.MyUser;
import com.littleheap.webcoursedesign.utils.ShareUtils;
import com.littleheap.webcoursedesign.utils.StaticClass;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    //输入框内容
    EditText et_user, et_age, et_desc, et_pass, et_password, et_email;
    RadioGroup mRadioGroup;
    Button btnRegister;
    //性别
    private boolean sex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();
    }

    private void initView() {
        et_user = (EditText) findViewById(R.id.et_user);
        et_age = (EditText) findViewById(R.id.et_age);
        et_desc = (EditText) findViewById(R.id.et_desc);
        et_pass = (EditText) findViewById(R.id.et_pass);
        et_password = (EditText) findViewById(R.id.et_password);
        et_email = (EditText) findViewById(R.id.et_email);

        mRadioGroup = (RadioGroup) findViewById(R.id.mRadioGroup);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnRegister:
                //获取输入框数据
                final String name = et_user.getText().toString().trim();
                String age = et_age.getText().toString().trim();
                String desc = et_desc.getText().toString().trim();
                String pass = et_pass.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                String email = et_email.getText().toString().trim();

                //判断是否为空
                if (!TextUtils.isEmpty(name) &
                        !TextUtils.isEmpty(age) &
                        !TextUtils.isEmpty(pass) &
                        !TextUtils.isEmpty(password) &
                        !TextUtils.isEmpty(email)) {
                    //判断两次密码输入是否一致
                    if (pass.equals(password)) {
                        //判断性别
                        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                                if (i == R.id.rb_boy) {
                                    sex = true;
                                } else if (i == R.id.rb_girl) {
                                    sex = true;
                                }
                            }
                        });
                        //判断简介是否为空
                        if (TextUtils.isEmpty(desc)) {
                            desc = "这个人很懒，什么都没有留下";
                        }
                        //数据信息获取判定完毕，开始注册
                        MyUser user = new MyUser();
                        user.setUsername(name);
                        user.setPassword(password);
                        user.setEmail(email);
                        user.setAge(Integer.parseInt(age));
                        user.setSex(sex);
                        user.setDesc(desc);

                        ShareUtils.putString(this, "user", name.toString().trim());

                        user.signUp(new SaveListener<MyUser>() {
                            @Override
                            public void done(MyUser myUser, BmobException e) {
                                if (e == null) {
                                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                    //初始化Socket
                                    StaticClass.socket= new Socket();
                                    //设置3秒延迟
                                    try {
                                        StaticClass.socket.connect(new InetSocketAddress("10.0.2.2", 9999), 300);
                                        PrintStream ps = new PrintStream(StaticClass.socket.getOutputStream());
                                        ps.println("register&" + name);
                                        ps.close();
                                        StaticClass.socket.close();
                                    } catch (IOException e1) {
                                        e1.printStackTrace();
                                    }
                                    finish();
                                } else {
                                    Toast.makeText(RegisterActivity.this, "注册失败：" + e.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(this, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "输入框不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
