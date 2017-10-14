package com.littleheap.webcoursedesign.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
        dbHelper = MainActivity.dbHelper;
        ed_person = (EditText) findViewById(R.id.ed_person);
        ed_number = (EditText) findViewById(R.id.ed_number);
        btn_call = (Button) findViewById(R.id.btn_call);
        btn_update = (Button) findViewById(R.id.btn_update);
        btn_delete = (Button) findViewById(R.id.btn_delete);
        btn_call.setOnClickListener(this);
        btn_update.setOnClickListener(this);
        btn_delete.setOnClickListener(this);

        Intent intent = getIntent();
        info = intent.getStringExtra("title");
        String[] infos = info.split(":");
        ed_person.setText(infos[0]);
        ed_number.setText(infos[1]);

    }

    public void updateDataBase_number(String database, String person, String number) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("number", number);
        db.update(database, values, "person = ?", new String[]{person});
        values.clear();
    }
    public void deleteDataBase(String database, String person) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(database,  "person = ?", new String[]{person});
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
        switch (view.getId()) {
            case R.id.btn_call:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:"+ed_number.getText()));
                startActivity(intent);
                break;
            case R.id.btn_update:
                updateDataBase_number("Contact_"+ ShareUtils.getString(this,"user",""), ed_person.getText().toString(), ed_number.getText().toString());
                Toast.makeText(this,"Update",Toast.LENGTH_SHORT).show();
                StaticClass.CONTACT=selectDataBase("Contact_"+ShareUtils.getString(this,"user",""));
                ContactFragment.updateList();
                finish();
                break;
            case R.id.btn_delete:
                deleteDataBase("Contact_"+ ShareUtils.getString(this,"user",""), ed_person.getText().toString());
                Toast.makeText(this,"Delete",Toast.LENGTH_SHORT).show();
                StaticClass.CONTACT=selectDataBase("Contact_"+ShareUtils.getString(this,"user",""));
                ContactFragment.updateList();
                finish();
                break;
        }

    }
}
