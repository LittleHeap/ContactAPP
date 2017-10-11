package com.littleheap.webcoursedesign.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.littleheap.webcoursedesign.R;
import com.littleheap.webcoursedesign.ui.InfoContent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by wangs on 2017/10/11.
 */

public class ContactFragment extends Fragment implements View.OnClickListener {
    //通讯录列表
    private ListView listView;
    //通讯录名单数组
    private String[] contact_list;
    //数据数量
    private int dataSize;
    //字典哈希
    List<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, null);
        findView(view);
        return view;
    }

    private void findView(View view) {
        //数据库获取信息标题
        getDatas();
        //将信息标题显示在列表中
        setDatas(view);
        //设置列表点击事件
        setClick();
    }

    //获取数据
    public void getDatas() {
        //获取数据库通讯录列表
        dataSize = 18;
        contact_list = new String[]{
                "父亲",
                "母亲",
                "Tom",
                "Peter",
                "Marry",
                "吴彦祖",
                "韩寒",
                "郭敬明",
                "张韶涵",
                "周杰伦",
                "桂纶镁",
                "黄涛",
                "辛迪",
                "甘地",
                "扎克伯格",
                "卡耐基",
                "洛克菲勒",
                "丁志国",
        };
    }

    //设置数据
    private void setDatas(View view) {
        for (int i = 1; i <= dataSize; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("itemNumber", String.valueOf(i));
            map.put("itemTitle", contact_list[i-1]);
            mylist.add(map);
        }
        listView = view.findViewById(R.id.contact_listView);

        //配置适配器
        SimpleAdapter adapter = new SimpleAdapter(getContext(),
                mylist ,//数据源
                R.layout.listview_item,//显示布局
                new String[]{"itemNumber", "itemTitle"}, //数据源的属性字段
                new int[]{R.id.listView_Number, R.id.listView_Title}); //布局里的控件id

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

    @Override
    public void onClick(View view) {

    }
}
