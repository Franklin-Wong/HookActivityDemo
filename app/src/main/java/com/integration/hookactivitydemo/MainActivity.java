package com.integration.hookactivitydemo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String FRAGMENTS_TAG = "fragment";
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_show)
    TextView tvShow;

    private Fragment mFragment;
    private FragmentManager.FragmentLifecycleCallbacks mCallbacks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

//        addFragment();

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);


    }

    /**
     * 保存状态
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    private void addFragment() {

        FragmentManager manager = getSupportFragmentManager();
        //返回一个BackStackRecord回退栈
        FragmentTransaction transaction = manager.beginTransaction();
        initFragment();
        transaction.add(R.id.mContent,mFragment,"one");
        //
        transaction.addToBackStack("stack");
//        transaction.remove(mFragment);
        //不允许状态丢失 的提交
        transaction.commit();

    }

    private void initFragment() {

        mFragment = new Fragment();

    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.btn_hook1)
    public void onBtnHook1Clicked() {

    }
    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.btn_hook2)
    public void onBtnHook2Clicked() {
        File file = new File("a.txt");
        try {
            FileInputStream inputStream = new FileInputStream(file);
            inputStream.read();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.btn_hook3)
    public void onBtnHook3Clicked() {

        Intent intent = new Intent(this,TargetActivity.class);
        startActivity(intent);
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.btn_hook4)
    public void onBtnHook4Clicked() {
        Log.i(TAG, "onBtnHook4Clicked: " + Build.VERSION.SDK_INT);

        HookHelper.hookAMSAIDL();
        HookHelper.hookHandler();
        Intent intent = new Intent(this,TargetActivity.class);
        startActivity(intent);
    }
}