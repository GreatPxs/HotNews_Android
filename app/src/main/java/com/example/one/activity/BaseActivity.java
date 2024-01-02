package com.example.one.activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;

import android.widget.Toast;

import androidx.annotation.Nullable;
import com.example.one.R;

public class BaseActivity extends AppCompatActivity {
    public Context main_context;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main_context = this;


        // 设置点击事件监听器


    }
    public void showToast(String msg)
    {
        Toast.makeText(main_context,msg,Toast.LENGTH_SHORT).show();
    }
}
