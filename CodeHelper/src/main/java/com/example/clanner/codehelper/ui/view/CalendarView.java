package com.example.clanner.codehelper.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.example.clanner.codehelper.R;
import com.example.clanner.codehelper.utils.Constant;
import com.example.clanner.codehelper.utils.RenderUtil;

import java.util.Calendar;

/**
 * Created by Clanner on 2016/6/26.
 */
public class CalendarView extends View implements CalendarViewImp, View.OnTouchListener {

    //选中的年份
    private int selectYear;
    //选中的月份
    private int selectMonth;
    private Calendar calendar;
    //日历为6*7格式(六行七列)
    private int[] date = new int[42];
    //屏幕宽度
    private int screenWidth;
    //当前月份的第一天
    private int curStartIndex;
    //当前月份的最后一天
    private int curEndIndex;
    private boolean[] data = new boolean[32];
    private int todayIndex;
    private int actionDownIndex = -1;
    private OnItemClickListener onItemClickListener;
    private OnRefreshListener onRefreshListener;
    private int mode;

    private float cellWidth;
    private float cellHeight;
    private String[] weekText;
    private int textColor = Constant.TEXT_COLOR;
    private int backgroundColor = Constant.BACKGROUND_COLOR;
    private Paint textPaint;
    private Paint weekTextPaint;
    private Paint grayPaint;
    private Paint whitePaint;
    private Paint bluePaint;
    private Paint blackPaint;

    public CalendarView(Context context) {
        this(context, null);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CalendarView);
        mode = typedArray.getInt(R.styleable.CalendarView_mode, Constant.MODE_SHOW_DATA_OF_THIS_MONTH);
        calendar = Calendar.getInstance();
        selectYear = calendar.get(Calendar.YEAR);
        selectMonth = calendar.get(Calendar.MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;

        cellWidth = screenWidth / 7f;
        cellHeight = cellWidth * 0.7f;
        setBackgroundColor(backgroundColor);
        weekText = getResources().getStringArray(Constant.WEEK_TEXT[0]);
        setOnTouchListener(this);
        textPaint = RenderUtil.getPaint(textColor);
        textPaint.setTextSize(cellHeight * 0.4f);

        weekTextPaint = RenderUtil.getPaint(textColor);
        weekTextPaint.setTextSize(cellHeight * 0.4f);
        weekTextPaint.setTypeface(Typeface.DEFAULT_BOLD);

        whitePaint = RenderUtil.getPaint(Color.WHITE);
        blackPaint = RenderUtil.getPaint(Color.BLACK);
        bluePaint = RenderUtil.getPaint(Color.BLUE);
        grayPaint = RenderUtil.getPaint(Color.GRAY);

        init();
    }

    private static int leap(int year) {
        return (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) ? 1 : 0;
    }

    private void init() {
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int monthStart = -1;
        if (dayOfWeek >= 2 && dayOfWeek <= 7) {
            monthStart = dayOfWeek - 2;
        } else if (dayOfWeek == 1) {
            monthStart = 6;
        }
        curStartIndex = monthStart;
        date[monthStart] = 1;
        int daysOfMonth = daysOfCurrentMonth();
        for (int i = 1; i < daysOfMonth; i++) {
            date[monthStart + i] = i + 1;
        }
        curEndIndex = monthStart + daysOfMonth;
        if (mode == Constant.MODE_SHOW_DATA_OF_THIS_MONTH) {
            Calendar tmp = Calendar.getInstance();
            todayIndex = tmp.get(Calendar.DAY_OF_MONTH) + monthStart - 1;
        }
    }

    /**
     * y is bigger than the head of the calendar, meaning that the coordination may represent a day
     * of the calendar
     */
    private boolean coordIsCalendarCell(float y) {
        return y > cellHeight;
    }

    /**
     * calculate the index of date[] according to the coordination
     */
    private int getIndexByCoordinate(float x, float y) {
        int m = (int) (Math.floor(x / cellWidth) + 1);
        int n = (int) (Math.floor((y - cellHeight) / cellHeight) + 1);
        return (n - 1) * 7 + m - 1;
    }

    /**
     * whether the index is legal
     *
     * @param i the index in date[]
     * @return
     */
    private boolean isLegalIndex(int i) {
        return !isIllegalIndex(i);
    }

    /**
     * whether the index is illegal
     *
     * @param i the index in date[]
     * @return
     */
    private boolean isIllegalIndex(int i) {
        return i < curStartIndex || i >= curEndIndex;
    }

    /**
     * calculate the x position according to the index in date[]
     *
     * @param i the index in date[]
     * @return
     */
    private int getXByIndex(int i) {
        return i % 7 + 1;
    }

    /**
     * calculate the y position according to the index in date[]
     *
     * @param i the index in date[]
     * @return
     */
    private int getYByIndex(int i) {
        return i / 7 + 1;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(screenWidth, MeasureSpec.EXACTLY);
        heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(measureHeight(), MeasureSpec.EXACTLY);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private int measureHeight() {
        /**
         * the weekday of the first day of the month, Sunday's result is 1 and Monday 2 and Saturday 7, etc.
         */
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        /**
         * the number of days of current month
         */
        int daysOfMonth = daysOfCurrentMonth();
        /**
         * calculate the total lines, which equals to 1 (head of the calendar) + 1 (the first line) + n/7 + (n%7==0?0:1)
         * and n means numberOfDaysExceptFirstLine
         */
        int numberOfDaysExceptFirstLine = -1;
        if (dayOfWeek >= 2 && dayOfWeek <= 7) {
            numberOfDaysExceptFirstLine = daysOfMonth - (8 - dayOfWeek + 1);
        } else if (dayOfWeek == 1) {
            numberOfDaysExceptFirstLine = daysOfMonth - 1;
        }
        int lines = 2 + numberOfDaysExceptFirstLine / 7 + (numberOfDaysExceptFirstLine % 7 == 0 ? 0 : 1);
        return (int) (cellHeight * lines);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /**
         * render the head
         */
        float baseline = RenderUtil.getBaseline(0, cellHeight, weekTextPaint);
        for (int i = 0; i < 7; i++) {
            float weekTextX = RenderUtil.getStartX(cellWidth * i + cellWidth * 0.5f, weekTextPaint, weekText[i]);
            canvas.drawText(weekText[i], weekTextX, baseline, weekTextPaint);
        }

        if (mode == Constant.MODE_CALENDAR) {
            for (int i = curStartIndex; i < curEndIndex; i++) {
                drawText(canvas, i, textPaint, "" + date[i]);
            }
        } else if (mode == Constant.MODE_SHOW_DATA_OF_THIS_MONTH) {
            for (int i = curStartIndex; i < curEndIndex; i++) {
                if (i < todayIndex) {
                    if (data[date[i]]) {
                        drawCircle(canvas, i, bluePaint, cellHeight * 0.37f);
                        drawCircle(canvas, i, whitePaint, cellHeight * 0.31f);
                        drawCircle(canvas, i, blackPaint, cellHeight * 0.1f);
                    } else {
                        drawCircle(canvas, i, grayPaint, cellHeight * 0.1f);
                    }
                } else if (i == todayIndex) {
                    if (data[date[i]]) {
                        drawCircle(canvas, i, bluePaint, cellHeight * 0.37f);
                        drawCircle(canvas, i, whitePaint, cellHeight * 0.31f);
                        drawCircle(canvas, i, blackPaint, cellHeight * 0.1f);
                    } else {
                        drawCircle(canvas, i, grayPaint, cellHeight * 0.37f);
                        drawCircle(canvas, i, whitePaint, cellHeight * 0.31f);
                        drawCircle(canvas, i, blackPaint, cellHeight * 0.1f);
                    }
                } else {
                    drawText(canvas, i, textPaint, "" + date[i]);
                }
            }
        }
    }

    /**
     * draw text, around the middle of the cell decided by the index
     */
    private void drawText(Canvas canvas, int index, Paint paint, String text) {
        if (isIllegalIndex(index)) {
            return;
        }
        int x = getXByIndex(index);
        int y = getYByIndex(index);
        float top = cellHeight + (y - 1) * cellHeight;
        float bottom = top + cellHeight;
        float baseline = RenderUtil.getBaseline(top, bottom, paint);
        float startX = RenderUtil.getStartX(cellWidth * (x - 1) + cellWidth * 0.5f, paint, text);
        canvas.drawText(text, startX, baseline, paint);
    }

    /**
     * draw circle, around the middle of the cell decided by the index
     */
    private void drawCircle(Canvas canvas, int index, Paint paint, float radius) {
        if (isIllegalIndex(index)) {
            return;
        }
        int x = getXByIndex(index);
        int y = getYByIndex(index);
        float centreY = cellHeight + (y - 1) * cellHeight + cellHeight * 0.5f;
        float centreX = cellWidth * (x - 1) + cellWidth * 0.5f;
        canvas.drawCircle(centreX, centreY, radius, paint);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (coordIsCalendarCell(y)) {
                    int index = getIndexByCoordinate(x, y);
                    if (isLegalIndex(index)) {
                        actionDownIndex = index;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (coordIsCalendarCell(y)) {
                    int actionUpIndex = getIndexByCoordinate(x, y);
                    if (isLegalIndex(actionUpIndex)) {
                        if (actionDownIndex == actionUpIndex) {
                            actionDownIndex = -1;
                            int day = date[actionUpIndex];
                            if (onItemClickListener != null) {
                                onItemClickListener.onItemClick(day);
                            }
                        }
                    }
                }
                break;
        }
        return true;
    }

    @Override
    public void refresh0(int year, int month) {
        if (mode == Constant.MODE_CALENDAR) {
            selectYear = year;
            selectMonth = month;
            calendar.set(Calendar.YEAR, selectYear);
            calendar.set(Calendar.MONTH, selectMonth - 1);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            init();
            invalidate();
            if (onRefreshListener != null) {
                onRefreshListener.onRefresh();
            }
        }
    }

    @Override
    public void refresh1(boolean[] data) {
        /**
         * the month and year may change (eg. Jan 31st becomes Feb 1st after refreshing)
         */
        if (mode == Constant.MODE_SHOW_DATA_OF_THIS_MONTH) {
            calendar = Calendar.getInstance();
            selectYear = calendar.get(Calendar.YEAR);
            selectMonth = calendar.get(Calendar.MONTH) + 1;
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            for (int i = 1; i <= daysOfCurrentMonth(); i++) {
                if (i < data.length) {
                    this.data[i] = data[i];
                } else {
                    this.data[i] = false;
                }
            }
            init();
            invalidate();
            if (onRefreshListener != null) {
                onRefreshListener.onRefresh();
            }
        }
    }

    @Override
    public int daysCompleteTheTask() {
        int k = 0;
        for (int i = 1; i <= daysOfCurrentMonth(); i++) {
            k += data[i] ? 1 : 0;
        }
        return k;
    }

    @Override
    public void setWeekTextStyle(int style) {
        if (style >= 0 && style <= 3) {
            weekText = getResources().getStringArray(Constant.WEEK_TEXT[style]);
        }
    }

    @Override
    public void setWeekTextColor(int color) {
        weekTextPaint.setColor(color);
    }

    @Override
    public void setCalendarTextColor(int color) {
        textPaint.setColor(color);
    }

    @Override
    public void setWeekTextSizeScale(float scale) {
        if (scale >= 0 && scale <= 1) {
            weekTextPaint.setTextSize(cellHeight * 0.5f * scale);
        }
    }

    @Override
    public void setTextSizeScale(float scale) {
        if (scale >= 0 && scale <= 1) {
            textPaint.setTextSize(cellHeight * 0.5f * scale);
        }
    }

    @Override
    public void setMode(int mode) {
        if (mode == Constant.MODE_SHOW_DATA_OF_THIS_MONTH || mode == Constant.MODE_CALENDAR) {
            this.mode = mode;
        }
    }

    @Override
    public int getYear() {
        return selectYear;
    }

    @Override
    public int getMonth() {
        return selectMonth;
    }

    @Override
    public int daysOfCurrentMonth() {
        return Constant.DAYS_OF_MONTH[leap(selectYear)][selectMonth];
    }

    @Override
    public Calendar getCalendar() {
        return calendar;
    }

    @Override
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }
}
