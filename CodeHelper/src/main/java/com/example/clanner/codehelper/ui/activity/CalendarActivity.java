package com.example.clanner.codehelper.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.clanner.codehelper.R;
import com.example.clanner.codehelper.utils.CommonUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Clanner on 2016/6/26.
 */
public class CalendarActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected int setContentViewId() {
        return R.layout.activity_calendar;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initActionBar(toolbar, "自定义日历", true);
    }

    @OnClick({R.id.button0, R.id.button1})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button0:
                CommonUtils.StartActivity(this,Demo0Activity.class);
                break;
            case R.id.button1:
                CommonUtils.StartActivity(this,Demo1Activity.class);
                break;
        }
    }
}
