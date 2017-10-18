package com.littleheap.webcoursedesign.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.littleheap.webcoursedesign.MainActivity;
import com.littleheap.webcoursedesign.R;
import com.littleheap.webcoursedesign.entity.MyUser;
import com.littleheap.webcoursedesign.utils.ShareUtils;
import com.littleheap.webcoursedesign.utils.StaticClass;
import com.littleheap.webcoursedesign.view.CustomDialog;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_register, btn_login;
    private EditText et_name, et_code;
    private CheckBox keep_password;
    private TextView forget_password;
    private CustomDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
    }

    private void initView() {

        //用户名
        et_name = (EditText) findViewById(R.id.et_name);
        //密码
        et_code = (EditText) findViewById(R.id.et_code);
        //登录按钮
        btn_login = (Button) findViewById(R.id.btn_login);
        //记住密码选项
        keep_password = (CheckBox) findViewById(R.id.keep_password);
        //忘记密码按钮
        forget_password = (TextView) findViewById(R.id.tv_forget);
        //注册按钮
        btn_register = (Button) findViewById(R.id.btn_register);

        //按钮监听事件
        btn_register.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        forget_password.setOnClickListener(this);

        //缓冲
        dialog = new CustomDialog(this, 200, 200, R.layout.dialog_loading, R.style.Theme_dialog, Gravity.CENTER, R.style.pop_anim_style);
        //屏幕外点击无效
        dialog.setCancelable(false);

        //设置记住密码选中状态
        Boolean isCheck = ShareUtils.getBoolean(this, "keep_password", false);
        keep_password.setChecked(isCheck);
        if (isCheck) {
            et_name.setText(ShareUtils.getString(this, "name", ""));
            et_code.setText(ShareUtils.getString(this, "code", ""));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_register://注册
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.btn_login://登录
                //1.获取输入框内的用户名和密码
                String name = et_name.getText().toString().trim();
                String code = et_code.getText().toString().trim();
                //2.判断是否为空
                if (!TextUtils.isEmpty(name) & !TextUtils.isEmpty(code)) {
                    //显示正在登陆
                    dialog.show();
                    final MyUser user = new MyUser();
                    user.setUsername(name);
                    user.setPassword(code);

                    //记录当前用户
                    ShareUtils.putString(this, "user", et_name.getText().toString().trim());

                    //不联网操作
//                    dialog.dismiss();
//                    startActivity(new Intent(LoginActivity.this, MainActivity.class));

                    //联网操作
                    user.login(new SaveListener<MyUser>() {
                        @Override
                        public void done(MyUser myUser, BmobException e) {
                            //消失正在登陆
                            dialog.dismiss();
                            //判断是否异常
                            if (e == null) {
                                //判断邮箱是否验证成功
                                if (user.getEmailVerified()) {
                                    //user.getEmailVerified()邮箱确认返回值
                                    //保存当前用户名
                                    ShareUtils.putString(LoginActivity.this, "user", et_name.getText().toString().trim());
                                    //记住密码
                                    keepPassword();
                                    //登录成功跳转进入主类
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                } else {
                                    Toast.makeText(LoginActivity.this, "请前往邮箱验证", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    Toast.makeText(this, "输入框不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_forget://忘记密码
                startActivity(new Intent(this, ForgetPasswordActivity.class));
                break;
        }
    }

    public void keepPassword(){
        //保存状态
        ShareUtils.putBoolean(this, "keep_password", keep_password.isChecked());

        //是否记住密码
        if (keep_password.isChecked()) {
            ShareUtils.putString(this, "name", et_name.getText().toString().trim());
            ShareUtils.putString(this, "code", et_code.getText().toString().trim());
        } else {
            ShareUtils.deleShare(this, "name");
            ShareUtils.deleShare(this, "code");
        }
    }

    //销毁操作
    @Override
    protected void onDestroy() {
        super.onDestroy();

        //记录当前用户
        ShareUtils.putString(this, "user", et_name.getText().toString().trim());

    }
}
