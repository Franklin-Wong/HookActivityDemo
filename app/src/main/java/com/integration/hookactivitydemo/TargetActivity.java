package com.integration.hookactivitydemo;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Wang
 * @version 1.0
 * @date 2021/8/26 - 13:40
 * @Description com.integration.hookactivitydemo
 */
public class TargetActivity extends AppCompatActivity {
    @BindView(R.id.tv_show)
    TextView tvShow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target);
        ButterKnife.bind(this);
    }


}
