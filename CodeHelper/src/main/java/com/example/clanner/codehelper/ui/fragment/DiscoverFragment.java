package com.example.clanner.codehelper.ui.fragment;

import android.os.Bundle;
import android.view.View;

import com.example.clanner.codehelper.R;
import com.example.clanner.codehelper.ui.activity.AboutActivity;
import com.example.clanner.codehelper.ui.activity.CalendarActivity;
import com.example.clanner.codehelper.ui.activity.TestActivity;
import com.example.clanner.codehelper.utils.CommonUtils;

import butterknife.OnClick;

/**
 * Created by Clanner on 2016/6/20.
 */
public class DiscoverFragment extends BaseFragment {

    public static DiscoverFragment newInstance() {
        return new DiscoverFragment();
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        getSwipeBackLayout().setEnableGesture(false);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_discover;
    }

    @OnClick({R.id.btn_sponsor, R.id.btn_usage, R.id.btn_rateapp, R.id.btn_uodate, R.id.btn_opensource, R.id.btn_about})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_sponsor:
                CommonUtils.StartActivity(getActivity(), CalendarActivity.class);
                break;
            case R.id.btn_usage:
                Test();
                break;
            case R.id.btn_rateapp:
                break;
            case R.id.btn_uodate:
                break;
            case R.id.btn_opensource:
                break;
            case R.id.btn_about:
                CommonUtils.StartActivity(getActivity(), AboutActivity.class);
                break;
        }
    }

    private void Test() {
        CommonUtils.StartActivity(getActivity(), TestActivity.class);
    }
}
