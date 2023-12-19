package com.example.one.fragments;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.one.R;
import com.example.one.toutiao_webview;
import com.example.one.weibo_webview;
import com.example.one.zhihu_webview;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
public class toutiao extends Fragment{
    private View view;
    protected List<String> toutiao_titles = new ArrayList<>();
    private List<String> toutiao_urls = new ArrayList<>();

    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_toutiao,container,false);
        SharedPreferences sp = Objects.requireNonNull(getActivity()).getSharedPreferences("toutiao", Activity.MODE_PRIVATE);
        String titles_listJson = sp.getString("toutiao_json_title","");
        String urls_listJson = sp.getString("toutiao_json_url","");
        if(!titles_listJson.equals(""))
        {
            Gson gson = new Gson();
            toutiao_titles = gson.fromJson(titles_listJson,new TypeToken<List<String>>(){}.getType());
        }
        if(!urls_listJson.equals(""))
        {
            Gson gson = new Gson();
            toutiao_urls = gson.fromJson(urls_listJson,new TypeToken<List<String>>(){}.getType());
        }
        ListView mListView = (ListView) view.findViewById(R.id.toutiao_listview); // 这个是找到我们的listview标签
        toutiao.MyBaseAdapter mAdapter = new toutiao.MyBaseAdapter(getActivity()); // 这个是创建一个适配器的对象（这个适配器是我们继承父类的子类）
        mListView.setAdapter(mAdapter); // 这个是将我们的这个listview的适配器设置为我们创建的这个适配器
        return view;
    }

    public class MyBaseAdapter extends BaseAdapter {

        private LayoutInflater layoutInflater;
        private Context context;


        public MyBaseAdapter(Context context) {
            this.context = context;
            this.layoutInflater = LayoutInflater.from(context);
        }

        public final class TouTiao {
            public TextView toutiao_number;
            public TextView toutiao_title;
            public LinearLayout toutiao_button;
        }

        @Override
        public int getCount() {
            return toutiao_titles.size();
        }

        /**
         * 获得某一位置的数据
         */
        @Override
        public Object getItem(int position) {
            return toutiao_titles.get(position);
        }

        /**
         * 获得唯一标识
         */
        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            toutiao.MyBaseAdapter.TouTiao toutiaos = null;
            if (convertView == null) {
                toutiaos = new toutiao.MyBaseAdapter.TouTiao();
                //获得组件，实例化组件
                convertView = layoutInflater.inflate(R.layout.toutiao_item, null);
                toutiaos.toutiao_number = (TextView) convertView.findViewById(R.id.toutiao_number);
                toutiaos.toutiao_title = (TextView) convertView.findViewById(R.id.toutiao_title);
                toutiaos.toutiao_button = (LinearLayout) convertView.findViewById(R.id.toutiao_button);
                convertView.setTag(toutiaos);
            } else {
                toutiaos = (toutiao.MyBaseAdapter.TouTiao) convertView.getTag();
            }
            //绑定数据
            if (position >= 9) {
                toutiaos.toutiao_number.setText(String.valueOf(position+1)+".");
            }
            else toutiaos.toutiao_number.setText(String.valueOf(position+1)+"." + "  ");
            toutiaos.toutiao_title.setText(toutiao_titles.get(position));
            toutiaos.toutiao_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), toutiao_webview.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("position",position);
                    intent.putExtra("toutiao",bundle);
                    startActivity(intent);
                }
            });
            return convertView;
        }
    }
}


