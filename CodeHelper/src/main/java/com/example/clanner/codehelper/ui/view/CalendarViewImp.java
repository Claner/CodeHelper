package com.example.clanner.codehelper.ui.view;

import java.util.Calendar;

/**
 * Created by Clanner on 2016/6/26.
 */
public interface CalendarViewImp {

    void refresh0(int year, int month);

    void refresh1(boolean data[]);

    int daysCompleteTheTask();

    void setWeekTextStyle(int style);

    void setWeekTextColor(int color);

    void setCalendarTextColor(int color);

    void setWeekTextSizeScale(float scale);

    void setTextSizeScale(float scale);

    void setMode(int mode);

    int getYear();

    int getMonth();

    int daysOfCurrentMonth();

    Calendar getCalendar();

    void setOnItemClickListener(OnItemClickListener onItemClickListener);

    void setOnRefreshListener(OnRefreshListener onRefreshListener);

    interface OnItemClickListener {
        void onItemClick(int day);
    }

    interface OnRefreshListener {
        void onRefresh();
    }
}
