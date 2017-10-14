package com.littleheap.webcoursedesign.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.littleheap.webcoursedesign.R;
import com.littleheap.webcoursedesign.ui.InfoContent;
import com.littleheap.webcoursedesign.utils.StaticClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by wangs on 2017/10/11.
 */

public class ContactFragment extends Fragment{
    //通讯录列表
    private static ListView listView;
    //通讯录名单数组
    private static String[] contact_list;
    //字典哈希
    public static List<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
    public static HashMap<String, String> map;
    public static SimpleAdapter adapter;
    public static Context context ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //获取上下文
        context = getContext();
        View view = inflater.inflate(R.layout.fragment_contact, null);
        findView(view);
        return view;
    }

    private void findView(View view) {
        listView = view.findViewById(R.id.contact_listView);
        //将信息标题显示在列表中
        setDatas();
        //设置列表点击事件
        setClick();
    }

    public static void updateList(){
        getMap();
        adapter = new SimpleAdapter(context,
                mylist ,//数据源
                R.layout.listview_item,//显示布局
                new String[]{"itemNumber", "itemTitle"}, //数据源的属性字段
                new int[]{R.id.listView_Number, R.id.listView_Title}); //布局里的控件id;
        listView.setAdapter(adapter);
    }

    //获取数据
    public static void getDatas() {
        contact_list = StaticClass.CONTACT.split(";");
    }

    //获取键值对
    private static void getMap(){
        getDatas();
        mylist.clear();
        for (int i = 1; i <= contact_list.length; i++) {
            if(contact_list[i-1].equals("")){
                break;
            }
            map = new HashMap<String, String>();
            map.put("itemNumber", String.valueOf(i));
            map.put("itemTitle", contact_list[i-1]);
            mylist.add(map);
        }
    }

    //获取adapter
    private void getAdapter(){
        //配置适配器
        adapter = new SimpleAdapter(getContext(),
                mylist ,//数据源
                R.layout.listview_item,//显示布局
                new String[]{"itemNumber", "itemTitle"}, //数据源的属性字段
                new int[]{R.id.listView_Number, R.id.listView_Title}); //布局里的控件id
    }

    //设置数据
    private void setDatas() {
        getMap();
        getAdapter();
        //添加并且显示
        listView.setAdapter(adapter);
    }

    private void setClick() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //获取点击信息标题
                String title = contact_list[position];
                //跳转至信息内容页
                Intent intent = new Intent(getActivity(), InfoContent.class);
                //将信息标题值传递过去
                intent.putExtra("title", title);
                startActivity(intent);
            }
        });
    }
}
