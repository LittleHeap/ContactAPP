package com.littleheap.webcoursedesign.ui;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.littleheap.webcoursedesign.MainActivity;
import com.littleheap.webcoursedesign.R;
import com.littleheap.webcoursedesign.fragment.ContactFragment;
import com.littleheap.webcoursedesign.utils.DataBase;
import com.littleheap.webcoursedesign.utils.MyDatabaseHelper;
import com.littleheap.webcoursedesign.utils.ShareUtils;
import com.littleheap.webcoursedesign.utils.StaticClass;

public class InfoContent extends AppCompatActivity implements View.OnClickListener {

    private EditText ed_person, ed_number;
    private Button btn_call, btn_update, btn_delete;
    private String info = "";
    //数据库操作
    private MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_content);

        initView();
    }

    private void initView() {
        //获取数据库操作
        dbHelper = MainActivity.dbHelper;
        //姓名编辑
        ed_person = (EditText) findViewById(R.id.ed_person);
        //号码编辑
        ed_number = (EditText) findViewById(R.id.ed_number);
        //打电话按钮
        btn_call = (Button) findViewById(R.id.btn_call);
        //更新号码按钮
        btn_update = (Button) findViewById(R.id.btn_update);
        //删除号码按钮
        btn_delete = (Button) findViewById(R.id.btn_delete);
        //添加点击事件
        btn_call.setOnClickListener(this);
        btn_update.setOnClickListener(this);
        btn_delete.setOnClickListener(this);

        //初始化Intent
        Intent intent = getIntent();
        //获取显示联系人和号码
        info = intent.getStringExtra("title");
        String[] infos = info.split(":");
        ed_person.setText(infos[0]);
        ed_number.setText(infos[1]);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_call://打电话操作
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + ed_number.getText()));
                startActivity(intent);
                break;
            case R.id.btn_update://更新号码操作
                DataBase.updateDataBase_number(dbHelper, "Contact_" + ShareUtils.getString(this, "user", ""), ed_person.getText().toString(), ed_number.getText().toString());
                Toast.makeText(this, "Update", Toast.LENGTH_SHORT).show();
                StaticClass.CONTACT = DataBase.selectDataBase(dbHelper, "Contact_" + ShareUtils.getString(this, "user", ""));
                //更新通讯录UI
                ContactFragment.updateList();
                finish();
                break;
            case R.id.btn_delete://删除操作
                DataBase.deleteDataBase(dbHelper, "Contact_" + ShareUtils.getString(this, "user", ""), ed_person.getText().toString());
                Toast.makeText(this, "Delete", Toast.LENGTH_SHORT).show();
                StaticClass.CONTACT = DataBase.selectDataBase(dbHelper, "Contact_" + ShareUtils.getString(this, "user", ""));
                //更新通讯录UI
                ContactFragment.updateList();
                finish();
                break;
        }

    }
}
