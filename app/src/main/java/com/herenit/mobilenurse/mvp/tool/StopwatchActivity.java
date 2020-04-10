package com.herenit.mobilenurse.mvp.tool;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.herenit.arms.base.adapter.lv.CommonAdapter;
import com.herenit.arms.base.adapter.lv.ViewHolder;
import com.herenit.arms.utils.ArmsUtils;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.criteria.enums.TitleBarTypeEnum;
import com.herenit.mobilenurse.mvp.base.BasicBusinessActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;

/**
 * author: HouBin
 * date: 2019/10/12 17:03
 * desc:秒表
 */
public class StopwatchActivity extends BasicBusinessActivity {

    @BindView(R2.id.dsecView)
    TextView dsecshow;
    @BindView(R2.id.secView)
    TextView secshow;
    @BindView(R2.id.minView)
    TextView minshow;
    @BindView(R2.id.hourView)
    TextView hourshow;
    private int dsec = 0, sec = 0, min = 0, hou = 0;

    private List<String> timeList = new ArrayList<>();
    @BindView(R2.id.startButton)
    Button button_start;
    @BindView(R2.id.resetButton)
    Button button_reset;
    @BindView(R2.id.recordButton)
    Button button_finish;

    private ToolStopwatchAdapter adapter;
    @BindView(R2.id.lv_toolStopwatch)
    ListView listView;
    private boolean isstop = true;

    private Timer mDsecTimer = null;
    private TimerTask mDsecTimerTask = null;
    private Timer mSecTimer = null;
    private TimerTask mSecTimerTask = null;
    private Timer mMinTimer = null;
    private TimerTask mMinTimerTask = null;
    private Timer mHouTimer = null;
    private TimerTask mHouTimerTask = null;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    updateView();
                    break;
                case 0:
                    break;
            }
        }

    };

    @Override
    protected void init(Bundle savedInstanceState) {
        setTitleBarLeftImage(R.mipmap.ic_arrow_back_white_24dp);
        setTitleBarLeftOnClickListener(new View.OnClickListener() {//返回键
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setTitleBarCenterText(ArmsUtils.getString(mContext, R.string.title_activity_stopwatch));

        button_start.setOnClickListener(new startButtonListener());
        button_reset.setOnClickListener(new resetButtonListener());
        button_finish.setOnClickListener(new finishButtonListener());
        adapter = new ToolStopwatchAdapter(mContext, timeList);
        listView.setAdapter(adapter);
    }

    @Override
    protected TitleBarTypeEnum titleBarType() {
        return TitleBarTypeEnum.IMG_TV_NULL;
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_stopwatch;
    }

    private void updateView() {
        if (dsec < 10)
            dsecshow.setText("0" + dsec);
        else
            dsecshow.setText("" + dsec);
        if (sec < 10)
            secshow.setText("0" + sec);
        else
            secshow.setText("" + sec);
        if (min < 10)
            minshow.setText("0" + min);
        else
            minshow.setText("" + min);
        if (hou < 10)
            hourshow.setText("0" + hou);
        else
            hourshow.setText("" + hou);
    }


    private class startButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (isstop) {
                button_start.setText("暂停");
                isstop = false;
                startTimer();
            } else {
                button_start.setText("开始");
                isstop = true;
                stopTimer();
            }
        }
    }

    /**
     * 停止计时器
     */
    private void stopTimer() {

        if (mDsecTimer != null) {
            mDsecTimer.cancel();
            mDsecTimer = null;
        }
        if (mDsecTimerTask != null) {
            mDsecTimerTask.cancel();
            mDsecTimerTask = null;
        }


        if (mSecTimer != null) {
            mSecTimer.cancel();
            mSecTimer = null;
        }
        if (mSecTimerTask != null) {
            mSecTimerTask.cancel();
            mSecTimerTask = null;
        }


        if (mMinTimer != null) {
            mMinTimer.cancel();
            mMinTimer = null;
        }
        if (mMinTimerTask != null) {
            mMinTimerTask.cancel();
            mMinTimerTask = null;
        }


        if (mHouTimer != null) {
            mHouTimer.cancel();
            mHouTimer = null;
        }
        if (mHouTimerTask != null) {
            mHouTimerTask.cancel();
            mHouTimerTask = null;
        }
    }

    /**
     * 开始计时器
     */
    private void startTimer() {

        //设置百分之一秒
        if (mDsecTimer == null) {
            mDsecTimer = new Timer();
        }
        if (mDsecTimerTask == null) {
            mDsecTimerTask = new TimerTask() {
                @Override
                public void run() {
                    dsec++;
                    if (dsec >= 100)
                        dsec = 0;
                    handler.sendEmptyMessage(1);
                }
            };
        }
        if (mDsecTimer != null && mDsecTimerTask != null)

            mDsecTimer.schedule(mDsecTimerTask, 10, 10);


        //设置秒
        if (mSecTimer == null) {
            mSecTimer = new Timer();
        }
        if (mSecTimerTask == null) {
            mSecTimerTask = new TimerTask() {
                @Override
                public void run() {
                    sec++;
                    if (sec >= 60)
                        sec = 0;
                    handler.sendEmptyMessage(1);
                }
            };
        }
        if (mSecTimer != null && mSecTimerTask != null)
            mSecTimer.schedule(mSecTimerTask, 1000 - (dsec * 10), 1000);

        //设置分
        if (mMinTimer == null) {
            mMinTimer = new Timer();
        }
        if (mMinTimerTask == null) {
            mMinTimerTask = new TimerTask() {
                @Override
                public void run() {
                    min++;
                    if (min >= 60)
                        min = 0;
                    handler.sendEmptyMessage(1);
                }
            };
        }
        if (mMinTimer != null && mMinTimerTask != null)
            mMinTimer.schedule(mMinTimerTask, (1000 * 60) - (sec * 1000), 1000 * 60);

        //设置时
        if (mHouTimer == null) {
            mHouTimer = new Timer();
        }
        if (mHouTimerTask == null) {
            mHouTimerTask = new TimerTask() {
                @Override
                public void run() {
                    hou++;
                    handler.sendEmptyMessage(1);
                }
            };
        }
        if (mHouTimer != null && mHouTimerTask != null)
            mHouTimer.schedule(mHouTimerTask, (1000 * 60 * 60) - (min * 1000 * 60), 1000 * 60 * 60);
    }

    private class resetButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            isstop = true;
            button_start.setText("开始");
            stopTimer();
            dsec = 0;
            sec = 0;
            min = 0;
            hou = 0;
            updateView();
        }
    }

    private class finishButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            button_start.setText("开始");
            isstop = true;
            if ("00".equals(dsecshow.getText().toString().trim()) && "00".equals(secshow.getText().toString().trim())
                    && "00".equals(minshow.getText().toString().trim()) && "00".equals(hourshow.getText().toString().trim()))
                return;
            String time = hourshow.getText().toString().trim() + ":" + minshow.getText().toString().trim() + ":" + secshow.getText().toString().trim() + ":" + dsecshow.getText().toString().trim();
            timeList.add(0, time);
            adapter.notifyDataSetChanged();
            stopTimer();
        }
    }


    private class ToolStopwatchAdapter extends CommonAdapter<String> {

        public ToolStopwatchAdapter(Context context, List<String> datas) {
            super(context, datas, R.layout.item_tool_stopwatch);
        }

        @Override
        protected void convert(ViewHolder holder, String item, int position) {
            if (position == 0) {
                holder.setBackgroundColor(R.id.tv_item_toolStopwatch_time, ArmsUtils.getColor(mContext, R.color.green_selected_bg));
                holder.setTextColor(R.id.tv_item_toolStopwatch_time, ArmsUtils.getColor(mContext, R.color.white));
            } else {
                holder.setBackgroundColor(R.id.tv_item_toolStopwatch_time, ArmsUtils.getColor(mContext, R.color.bg_lightGrayB));
                holder.setTextColor(R.id.tv_item_toolStopwatch_time, ArmsUtils.getColor(mContext, R.color.light_black));
            }
            holder.setText(R.id.tv_item_toolStopwatch_time, timeList.get(position));
        }
    }

    @Override
    protected void onDestroy() {
        stopTimer();
        super.onDestroy();
    }
}
