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
import com.example.one.baidu_webview;
import com.example.one.weibo_webview;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
public class baidu extends Fragment{
    private View view;
    protected List<String> baidu_titles = new ArrayList<>();
    protected List<String> baidu_hots = new ArrayList<>();
    private List<String> baidu_urls = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_baidu,container,false);
        SharedPreferences sp = Objects.requireNonNull(getActivity()).getSharedPreferences("baidu", Activity.MODE_PRIVATE);
        String titles_listJson = sp.getString("baidu_json_title","");
        String querys_listJson = sp.getString("baidu_json_hot","");
        String urls_listJson = sp.getString("baidu_json_url","");
        if(!titles_listJson.equals(""))
        {
            Gson gson = new Gson();
            baidu_titles = gson.fromJson(titles_listJson,new TypeToken<List<String>>(){}.getType());
        }
        if(!querys_listJson.equals(""))
        {
            Gson gson = new Gson();
            baidu_hots = gson.fromJson(querys_listJson,new TypeToken<List<String>>(){}.getType());
        }
        if(!urls_listJson.equals(""))
        {
            Gson gson = new Gson();
            baidu_urls = gson.fromJson(urls_listJson,new TypeToken<List<String>>(){}.getType());
        }
        ListView mListView = (ListView) view.findViewById(R.id.baidu_listview); // 这个是找到我们的listview标签
        baidu.MyBaseAdapter mAdapter = new baidu.MyBaseAdapter(getActivity()); // 这个是创建一个适配器的对象（这个适配器是我们继承父类的子类）
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

        public final class BaiDu {
            public TextView baidu_number;
            public TextView baidu_title;
            public TextView baidu_hot;
            public LinearLayout baidu_button;
        }

        @Override
        public int getCount() {
            return baidu_titles.size();
        }

        /**
         * 获得某一位置的数据
         */
        @Override
        public Object getItem(int position) {
            return baidu_titles.get(position);
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
            baidu.MyBaseAdapter.BaiDu baidus = null;
            if (convertView == null) {
                baidus = new baidu.MyBaseAdapter.BaiDu();
                //获得组件，实例化组件
                convertView = layoutInflater.inflate(R.layout.baidu_item, null);
                baidus.baidu_number = (TextView) convertView.findViewById(R.id.baidu_number);
                baidus.baidu_title = (TextView) convertView.findViewById(R.id.baidu_title);
                baidus.baidu_hot = (TextView) convertView.findViewById(R.id.baidu_hot);
                baidus.baidu_button = (LinearLayout) convertView.findViewById(R.id.baidu_button);
                convertView.setTag(baidus);
            } else {
                baidus = (baidu.MyBaseAdapter.BaiDu) convertView.getTag();
            }
            //绑定数据
            if (position >= 9) {
                baidus.baidu_number.setText(String.valueOf(position+1)+".");
            }
            else baidus.baidu_number.setText(String.valueOf(position+1)+"." + "  ");
            baidus.baidu_title.setText(baidu_titles.get(position));
            baidus.baidu_hot.setText(baidu_hots.get(position));
            baidus.baidu_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), baidu_webview.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("position",position);
                    intent.putExtra("baidu",bundle);
                    startActivity(intent);
                }
            });
            return convertView;
        }
    }
}
