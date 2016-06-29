package com.example.clanner.codehelper.ui.fragment;

import android.os.Bundle;
import android.view.View;

import com.example.clanner.codehelper.R;

import butterknife.OnClick;

/**
 * Created by Clanner on 2016/6/20.
 */
public class ResourceCenterFragment extends BaseFragment {

    public static ResourceCenterFragment newInstance() {
        return new ResourceCenterFragment();
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        getSwipeBackLayout().setEnableGesture(false);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_resourcecenter;
    }

    @OnClick({R.id.btn_java_tv, R.id.btn_java, R.id.btn_android_tv, R.id.btn_android, R.id.btn_php, R.id.btn_python_tv, R.id.btn_python, R.id.btn_javascript_tv, R.id.btn_javascript, R.id.btn_c_tv, R.id.btn_c, R.id.btn_angular_tv, R.id.btn_angular, R.id.btn_scala_tv, R.id.btn_scala, R.id.btn_http2_tv, R.id.btn_http2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_java:
                toast("btn_java");
                break;
            case R.id.btn_android:
                toast("btn_android");
                break;
            case R.id.btn_php:
                toast("btn_php");
                break;
            case R.id.btn_python:
                toast("btn_python");
                break;
            case R.id.btn_javascript:
                toast("btn_javascript");
                break;
            case R.id.btn_c:
                toast("btn_c");
                break;
            case R.id.btn_angular:
                toast("btn_angular");
                break;
            case R.id.btn_scala:
                toast("btn_scala");
                break;
            case R.id.btn_http2:
                toast("btn_http2");
                break;
        }
    }
}
