package com.littleheap.webcoursedesign.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.littleheap.webcoursedesign.MainActivity;
import com.littleheap.webcoursedesign.R;
import com.littleheap.webcoursedesign.fragment.ContactFragment;
import com.littleheap.webcoursedesign.utils.DataBase;
import com.littleheap.webcoursedesign.utils.MyDatabaseHelper;
import com.littleheap.webcoursedesign.utils.ShareUtils;
import com.littleheap.webcoursedesign.utils.StaticClass;

public class InfoAdd extends AppCompatActivity implements View.OnClickListener {

    private EditText add_person, add_number;
    private Button btn_add;
    //数据库操作
    private MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_add);

        initView();
    }

    private void initView() {
        //获取数据库操作
        dbHelper = MainActivity.dbHelper;
        //新联系人姓名
        add_person = (EditText) findViewById(R.id.add_person);
        //新联系人号码
        add_number = (EditText) findViewById(R.id.add_number);
        //添加按钮
        btn_add = (Button) findViewById(R.id.btn_add);
        btn_add.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add://添加按钮
                //将新联系人数据插入后台数据库
                DataBase.insertDataBase(dbHelper, "Contact_" + ShareUtils.getString(this, "user", ""), add_person.getText().toString(), add_number.getText().toString());
                //更新联系人数据字符串
                StaticClass.CONTACT = DataBase.selectDataBase(dbHelper, "Contact_" + ShareUtils.getString(this, "user", ""));
                //更新通讯录UI
                ContactFragment.updateList();
                Toast.makeText(this, "Add", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }
}
