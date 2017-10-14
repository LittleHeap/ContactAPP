package com.littleheap.webcoursedesign.ui;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.littleheap.webcoursedesign.MainActivity;
import com.littleheap.webcoursedesign.R;
import com.littleheap.webcoursedesign.fragment.ContactFragment;
import com.littleheap.webcoursedesign.utils.MyDatabaseHelper;
import com.littleheap.webcoursedesign.utils.ShareUtils;
import com.littleheap.webcoursedesign.utils.StaticClass;

public class InfoAdd extends AppCompatActivity implements View.OnClickListener {

    private EditText add_person,add_number;
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
        dbHelper = MainActivity.dbHelper;
        add_person = (EditText) findViewById(R.id.add_person);
        add_number = (EditText) findViewById(R.id.add_number);
        btn_add = (Button) findViewById(R.id.btn_add);
        btn_add.setOnClickListener(this);
    }

    public void insertDataBase(String database, String person, String number) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("person", person);
        values.put("number", number);
        db.insert(database, null, values);
        values.clear();
    }

    public String selectDataBase(String database) {
        String whole = "";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(database, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String _person = cursor.getString(cursor.getColumnIndex("person"));
                String _number = cursor.getString(cursor.getColumnIndex("number"));
                whole = whole + _person + ":" + _number + ";";
            } while (cursor.moveToNext());
        }
        cursor.close();
        return whole;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_add:
                insertDataBase("Contact_"+ ShareUtils.getString(this,"user",""), add_person.getText().toString(), add_number.getText().toString());
                StaticClass.CONTACT=selectDataBase("Contact_"+ShareUtils.getString(this,"user",""));
                ContactFragment.updateList();
                Toast.makeText(this,"Add",Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }
}
