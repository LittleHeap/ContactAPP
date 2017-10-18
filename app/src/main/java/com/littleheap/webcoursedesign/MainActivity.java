package com.littleheap.webcoursedesign;

import android.content.Intent;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.littleheap.webcoursedesign.fragment.ContactFragment;
import com.littleheap.webcoursedesign.fragment.UserFragment;
import com.littleheap.webcoursedesign.ui.InfoAdd;
import com.littleheap.webcoursedesign.ui.SettingActivity;
import com.littleheap.webcoursedesign.utils.DataBase;
import com.littleheap.webcoursedesign.utils.MyDatabaseHelper;
import com.littleheap.webcoursedesign.utils.ShareUtils;
import com.littleheap.webcoursedesign.utils.StaticClass;

import java.io.*;
import java.net.*;
import java.util.*;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //TabLayout
    private TabLayout mTabLayout;
    //ViewPager
    private ViewPager mViewPager;
    //Title
    private List<String> mTitle;
    //Fragment
    private List<Fragment> mFragment;
    //悬浮窗
    private FloatingActionButton fab_setting, fab_refresh, fab_add;
    //通讯组件
    public PrintStream ps = null;
    public BufferedReader br = null;
    public String message = "";
    public String user = "";
    //数据库操作
    public static MyDatabaseHelper dbHelper;
    //网络连接

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //获取当前用户名
        user = ShareUtils.getString(this, "user", "");

        //防止网络异常
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork()
                .penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
                .penaltyLog().penaltyDeath().build());
        //去阴影
        getSupportActionBar().setElevation(0);
        //测试当前情况
//        Toast.makeText(this, ShareUtils.getString(this, "user", ""),Toast.LENGTH_LONG).show();
        //初始化数据库
        initDataBase();
        //连接服务器
        try {
            initConnect();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        //插入数据
//        insertDataBase("Contact_"+user, "Tom", "13844171631");
//        //更新号码数据
//        updateDataBase_number("Contact_"+user, "Tom", "15211136990");
//        //查询数据
//        Log.i("message",selectDataBase("Contact_"+user));

        //初始化UI数据
        initData();
        //初始化UI界面
        initView();
    }

    //获取当前用户通讯录
    public void getContact() {
        ps.println("getcontact&" + user);
    }

    //初始化本地数据库
    public void initDataBase() {
        dbHelper = new MyDatabaseHelper(this, "Contact.db", null, 2);
        dbHelper.getWritableDatabase();
    }

    private void initConnect() throws Exception {
        //与服务器建立连接
        try {
            //初始化Socket
            StaticClass.socket= new Socket();
            //设置3秒延迟
            StaticClass.socket.connect(new InetSocketAddress("10.0.2.2", 9999), 300);
            Toast.makeText(this,"验证成功，连接服务器成功",Toast.LENGTH_SHORT).show();
        }catch (Exception e) {
            e.printStackTrace();
            StaticClass.socket.close();
            dbHelper.close();
            Toast.makeText(this,"验证成功，连接服务器失败",Toast.LENGTH_SHORT).show();
            finish();
        }

        //初始化数据流
        ps = new PrintStream(StaticClass.socket.getOutputStream());
        br = new BufferedReader(new InputStreamReader(StaticClass.socket.getInputStream()));
        //开线程死循环不断接受信息
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        message = br.readLine();
                        Log.i("msg", message);
                        String msg0 = message.split("&")[0];
                        String msg1 = message.split("&")[1];
//                        String msg2 = message.split("&")[2];
                        switch (msg0) {
                            //收到联系人列表原始数据信息
                            case "getcontact":
                                StaticClass.CONTACT = msg1;
                                String[] msg = msg1.split(";");
                                //将最新获取的数据写入数据库
                                for (int i = 0; i < msg.length; i++) {
                                    String[] info = msg[i].split(":");
//                                    insertDataBase("Contact_" + user, info[0], info[1]);
                                    DataBase.insertDataBase(dbHelper,"Contact_" + user, info[0], info[1]);
                                }
                                break;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        //向服务器发送信息
//        ps.println("hello i am app");
        getContact();
    }


    private void initData() {
        mTitle = new ArrayList<>();
        mTitle.add("通讯录");
        mTitle.add("个人中心");

        mFragment = new ArrayList<>();
        mFragment.add(new ContactFragment());
        mFragment.add(new UserFragment());

    }

    private void initView() {
        //浮动setting按钮
        fab_setting = (FloatingActionButton) findViewById(R.id.fab_setting);
        fab_setting.setOnClickListener(this);
        fab_refresh = (FloatingActionButton) findViewById(R.id.fab_refresh);
        fab_refresh.setOnClickListener(this);
        fab_add = (FloatingActionButton) findViewById(R.id.fab_add);
        fab_add.setOnClickListener(this);

        //隐藏
        fab_setting.setVisibility(View.GONE);
        fab_refresh.setVisibility(View.VISIBLE);
        fab_add.setVisibility(View.VISIBLE);

        mTabLayout = (TabLayout) findViewById(R.id.mTabLayout);
        mViewPager = (ViewPager) findViewById(R.id.mViewPager);

        //预加载
        mViewPager.setOffscreenPageLimit(mFragment.size());
        //mViewPager滑动监听
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {//第一页隐藏
                    fab_setting.setVisibility(View.GONE);
                    fab_refresh.setVisibility(View.VISIBLE);
                    fab_add.setVisibility(View.VISIBLE);
                } else {
                    fab_setting.setVisibility(View.GONE);
                    fab_refresh.setVisibility(View.GONE);
                    fab_add.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //设置Tablayout适配器
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            //选中的item
            @Override
            public Fragment getItem(int position) {
                return mFragment.get(position);
            }

            //返回item的个数
            @Override
            public int getCount() {
                return mFragment.size();
            }

            //设置标题
            @Override
            public CharSequence getPageTitle(int position) {
                return mTitle.get(position);
            }
        });
        //绑定
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_setting:
                startActivity(new Intent(this, SettingActivity.class));
                break;
            case R.id.fab_refresh:
//                StaticClass.CONTACT = selectDataBase("Contact_" + user);
                StaticClass.CONTACT = DataBase.selectDataBase(dbHelper,"Contact_" + user);
                ps.println("update#"+user+"&"+StaticClass.CONTACT);
                Toast.makeText(this,"Synchronize",Toast.LENGTH_SHORT).show();
                break;
            case R.id.fab_add:
                Intent intent = new Intent(MainActivity.this, InfoAdd.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //退出时同步
        StaticClass.CONTACT = DataBase.selectDataBase(dbHelper,"Contact_" + user);
        ps.println("update#"+user+"&"+StaticClass.CONTACT);
        Toast.makeText(this,"Synchronize",Toast.LENGTH_SHORT).show();
    }
}
