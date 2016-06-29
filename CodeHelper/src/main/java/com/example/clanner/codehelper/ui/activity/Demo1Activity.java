package com.example.clanner.codehelper.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.clanner.codehelper.R;
import com.example.clanner.codehelper.ui.view.CalendarView;
import com.example.clanner.codehelper.ui.view.CalendarViewImp;

import java.util.Calendar;

import butterknife.BindView;

/**
 * Created by Clanner on 2016/6/29.
 */
public class Demo1Activity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.run_this_month_num)
    TextView runThisMonthTextView;
    @BindView(R.id.run_this_month_img)
    ImageView runThisMonthImageView;
    @BindView(R.id.run_this_month)
    RelativeLayout runThisMonthRelativeLayout;
    @BindView(R.id.separator)
    View separator;
    @BindView(R.id.calendarView)
    CalendarView calendarView;

    private boolean calendarViewFold = false;

    @Override
    protected int setContentViewId() {
        return R.layout.activity_demo1;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initActionBar(toolbar, R.string.demo1_value, true);

        runThisMonthRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarViewFold = !calendarViewFold;
                if (calendarViewFold) {
                    calendarView.setVisibility(View.GONE);
                    separator.setVisibility(View.GONE);
                    runThisMonthImageView.setImageResource(R.mipmap.right);
                } else {
                    calendarView.setVisibility(View.VISIBLE);
                    separator.setVisibility(View.VISIBLE);
                    runThisMonthImageView.setImageResource(R.drawable.down);
                }
            }
        });

        calendarView.setOnRefreshListener(new CalendarViewImp.OnRefreshListener() {
            @Override
            public void onRefresh() {
                runThisMonthTextView.setText(calendarView.daysCompleteTheTask() + " days");
            }
        });

        int days = calendarView.daysOfCurrentMonth();
        boolean data[] = new boolean[days + 1];
        int today = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        for (int i = 1; i <= days; i++) {
            if (i <= today) {
                data[i] = (Math.random() > 0.5);
            } else {
                data[i] = false;
            }
        }
        calendarView.refresh1(data);
    }
}
