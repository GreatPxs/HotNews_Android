package com.example.one.fragments;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.one.R;
import com.example.one.bilibili_webview;
import com.example.one.weibo_webview;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
public class bilibili extends Fragment{
    private View view;
    protected List<String> bilibili_titles = new ArrayList<>();
    private List<String> bilibili_urls = new ArrayList<>();

    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_bilibili,container,false);
        SharedPreferences sp = Objects.requireNonNull(getActivity()).getSharedPreferences("bilibili", Activity.MODE_PRIVATE);
        String titles_listJson = sp.getString("bilibili_json_title","");
        String urls_listJson = sp.getString("bilibili_json_url","");
        if(!titles_listJson.equals(""))
        {
            Gson gson = new Gson();
            bilibili_titles = gson.fromJson(titles_listJson,new TypeToken<List<String>>(){}.getType());
        }
        if(!urls_listJson.equals(""))
        {
            Gson gson = new Gson();
            bilibili_urls = gson.fromJson(urls_listJson,new TypeToken<List<String>>(){}.getType());
        }
        ListView mListView = (ListView) view.findViewById(R.id.bilibili_listview); // 这个是找到我们的listview标签
        bilibili.MyBaseAdapter mAdapter = new bilibili.MyBaseAdapter(getActivity()); // 这个是创建一个适配器的对象（这个适配器是我们继承父类的子类）
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

        public final class BiliBili {
            public TextView bilibili_number;
            public TextView bilibili_title;
            public LinearLayout bilibili_button;
        }

        @Override
        public int getCount() {
            return bilibili_titles.size();
        }

        /**
         * 获得某一位置的数据
         */
        @Override
        public Object getItem(int position) {
            return bilibili_titles.get(position);
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
            bilibili.MyBaseAdapter.BiliBili bilibilis = null;
            if (convertView == null) {
                bilibilis = new bilibili.MyBaseAdapter.BiliBili();
                //获得组件，实例化组件
                convertView = layoutInflater.inflate(R.layout.bilibili_item, null);
                bilibilis.bilibili_number = (TextView) convertView.findViewById(R.id.bilibili_number);
                bilibilis.bilibili_title = (TextView) convertView.findViewById(R.id.bilibili_title);
                bilibilis.bilibili_button = (LinearLayout) convertView.findViewById(R.id.bilibili_button);
                convertView.setTag(bilibilis);
            } else {
                bilibilis = (bilibili.MyBaseAdapter.BiliBili) convertView.getTag();
            }
            //绑定数据
            if (position >= 9) {
                bilibilis.bilibili_number.setText(String.valueOf(position+1)+".");
            }
            else bilibilis.bilibili_number.setText(String.valueOf(position+1)+"." + "  ");
            bilibilis.bilibili_title.setText(bilibili_titles.get(position));
            bilibilis.bilibili_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), bilibili_webview.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("position",position);
                    intent.putExtra("bilibili",bundle);
                    startActivity(intent);
                }
            });
            return convertView;
        }
    }
}
