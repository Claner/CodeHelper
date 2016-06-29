package com.example.clanner.codehelper.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.MyTimePickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.example.clanner.codehelper.R;
import com.example.clanner.codehelper.ui.view.CalendarView;
import com.example.clanner.codehelper.ui.view.CalendarViewImp;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Clanner on 2016/6/29.
 */
public class Demo0Activity extends BaseActivity {
    private static String[] MONTH_NAME = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sept", "Oct", "Nov", "Dec"};
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.prev)
    Button prevButton;
    @BindView(R.id.year_month_textview)
    TextView yearMonthTextView;
    @BindView(R.id.next)
    Button nextButton;
    @BindView(R.id.calendarView)
    CalendarView calendarView;
    private MyTimePickerView timePickerView;

    @Override
    protected int setContentViewId() {
        return R.layout.activity_demo0;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initActionBar(toolbar, R.string.demo0_value, true);
        timePickerView = new MyTimePickerView(Demo0Activity.this);
        timePickerView.setTitle("Select Year/Month");
        timePickerView.setCyclic(false);
        timePickerView.setCancelable(false);
        timePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                Calendar c = Calendar.getInstance();
                c.setTime(date);
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH) + 1;
                calendarView.refresh0(year, month);
            }
        });

        yearMonthTextView.setText(getYearMonthText(calendarView.getYear(), calendarView.getMonth()));

        calendarView.setWeekTextStyle(3);

        calendarView.setOnRefreshListener(new CalendarViewImp.OnRefreshListener() {
            @Override
            public void onRefresh() {
                yearMonthTextView.setText(getYearMonthText(calendarView.getYear(), calendarView.getMonth()));
            }
        });

        calendarView.setOnItemClickListener(new CalendarViewImp.OnItemClickListener() {
            @Override
            public void onItemClick(int day) {
                int year = calendarView.getYear();
                int month = calendarView.getMonth();
                Toast.makeText(Demo0Activity.this, year + "-" + month + "-" + day, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getYearMonthText(int year, int month) {
        return new StringBuilder().append(MONTH_NAME[month - 1]).append(", ").append(year).toString();
    }

    @OnClick({R.id.prev, R.id.year_month_textview, R.id.next})
    public void onClick(View view) {
        Calendar c;
        switch (view.getId()) {
            case R.id.prev:
                c = calendarView.getCalendar();
                c.set(Calendar.MONTH, c.get(Calendar.MONTH) - 1);
                calendarView.refresh0(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1);
                break;
            case R.id.year_month_textview:
                timePickerView.setTime(calendarView.getCalendar().getTime());
                timePickerView.show();
                break;
            case R.id.next:
                c = calendarView.getCalendar();
                c.set(Calendar.MONTH, c.get(Calendar.MONTH) + 1);
                calendarView.refresh0(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1);
                break;
        }
    }
}
