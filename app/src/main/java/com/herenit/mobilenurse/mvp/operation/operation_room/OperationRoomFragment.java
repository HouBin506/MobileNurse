package com.herenit.mobilenurse.mvp.operation.operation_room;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.widget.TextView;

import com.herenit.arms.mvp.BasePresenter;
import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.criteria.entity.OperationScheduled;
import com.herenit.mobilenurse.custom.widget.progressbar.BaseProgressBar;
import com.herenit.mobilenurse.mvp.base.BasicCommonFragment;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Timer;

/**
 * author: HouBin
 * date: 2019/10/10 17:52
 * desc:
 */
public abstract class OperationRoomFragment<P extends BasePresenter> extends BasicCommonFragment<P> {
    protected static final String KEY_EMERGENCYNOTIFICATION = "emergency_notification";
    protected static final String NOTIFICATION_CLICK_ACTION = "notification_click_action";
    protected static final String NOTIFICATION_DELETE_ACTION = "notification_delete_action";
    protected static final int EXITSYSTEM = 0;


    private static long currentTime = 0;
    private Timer timer;
    public static final int ERROR_CODE = 4444444;

    protected Handler mHandler;


    /**
     * 是否可以播放声音
     */
    private boolean playBeep = true;
    /**
     * 是否可以播放震动
     */
    private boolean playVibrator = true;

    private MediaPlayer mediaPlayer;

    private NotificationManager mNotificationManager;

    public Handler getHandler() {
        return mHandler;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mContext.registerReceiver(mEmergencyNotificationReceiver, new IntentFilter(NOTIFICATION_DELETE_ACTION));
        mContext.registerReceiver(mEmergencyNotificationReceiver, new IntentFilter(NOTIFICATION_CLICK_ACTION));
        getActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);//注册可调节音量大小
        initMediaPlayer();//初始化声音播放器
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mContext.unregisterReceiver(mEmergencyNotificationReceiver);
        if (mNotificationManager != null)
            mNotificationManager.cancelAll();
        if (timer != null)
            timer.cancel();
    }


    protected void sendMessage(Handler handler, Message msg, Integer what,
                               Object obj) throws Exception {
        msg = Message.obtain();
        msg.what = what;
        msg.obj = obj;
        handler.sendMessage(msg);
    }


    /**
     * 初始化媒体播放器
     *
     * @return
     */
    private void initMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        //注册事件。当播放完毕一次后，重新指向流文件的开头，以准备下次播放。
        // When the beep has finished playing, rewind to queue up another one.
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer player) {
                player.seekTo(0);
            }
        });
        AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
        try {
            mediaPlayer.setDataSource(file.getFileDescriptor(),
                    file.getStartOffset(), file.getLength());
            file.close();
            mediaPlayer.setVolume(0.5f, 0.5f);
            mediaPlayer.prepare();
        } catch (IOException ioe) {
            mediaPlayer = null;
        }
    }


    protected void remindEmergencyOperation(List<OperationScheduled> emergencyOperations) {
        if (emergencyOperations == null || emergencyOperations.size() == 0)
            return;
        OperationScheduled model = emergencyOperations.get(0);
        String message = model.getBedLabel() + " " + model.getName() + " " + model.getOperationName();
        showEmergencyNotification(message);
        playerRemind();
    }


    //通知的ID，为了分开显示，需要根据Id区分
    private int nId = 0;

    /**
     * 发送急诊未确认通知
     *
     * @param message
     */
    private void showEmergencyNotification(String message) {
        //为了版本兼容，使用v7包的ＢＵＩＬＤＥＲ
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);
        //状态栏显示的提示，有的手机不显示
        builder.setTicker("急诊未确认");
        //通知栏标题
        builder.setContentTitle("发现未确认的急诊手术");
        //通知栏内容
        builder.setContentText(message);
        //通知内容摘要
//        builder.setSubText(sMessage.getId());
        //在通知右侧的时间下面用来展示一些其他信息
//        builder.setContentInfo("其他");
        //用来显示同种通知的数量，如果设置了ContentInfo属性，则NUmber属性会被覆盖，因为二者显示的位置相同
//        builder.setNumber(3);
        //可以点击通知栏的删除按钮
//        builder.setAutoCancel(true);
        //系统状态栏显示的小图标
        builder.setSmallIcon(R.mipmap.ic_operation_uncheck);
        //通知下拉显示的大图标
        builder.setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_operation_checked));
        //INTENT
        Intent contentIntent = new Intent(NOTIFICATION_CLICK_ACTION);
        contentIntent.putExtra(KEY_EMERGENCYNOTIFICATION, nId);
        builder.setContentIntent(PendingIntent.getBroadcast(mContext, nId, contentIntent, 0));//
        Intent deleteIntent = new Intent(NOTIFICATION_DELETE_ACTION);
        deleteIntent.putExtra(KEY_EMERGENCYNOTIFICATION, nId);
        builder.setDeleteIntent(PendingIntent.getBroadcast(mContext, nId, deleteIntent, 0));
        //通知默认的声音，震动，呼吸灯
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        Notification notification = builder.build();
        if (mNotificationManager == null)
            mNotificationManager = (NotificationManager) mContext.getSystemService(mContext.NOTIFICATION_SERVICE);
        mNotificationManager.notify(nId, notification);
        nId++;
    }

    protected void playerRemind() {
        AudioManager audioService = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        if (audioService.getRingerMode() == AudioManager.RINGER_MODE_NORMAL) {//普通
            playBeep = true;
            playVibrator = true;
        } else if (audioService.getRingerMode() == AudioManager.RINGER_MODE_SILENT) {//静音
            playBeep = false;
            playVibrator = false;
        } else if (audioService.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE) {//震动
            playVibrator = true;
            playBeep = false;
        }
        startBeep();
        startVibrator();
    }


    /**
     * 开启震动
     */
    private void startVibrator() {
        if (!playVibrator) {
            return;
        }
        Vibrator vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
        //震动一次
        //第一个参数，指代一个震动的频率数组。每两个为一组，每组的第一个为等待时间，第二个为震动时间。
        // 比如 [2000,500,100,400],会先等待2000毫秒，震动500，再等待100，震动400
        //第二个参数，repest指代从 第几个索引（第一个数组参数） 的位置开始循环震动。
        vibrator.vibrate(new long[]{300, 500}, 0);
    }


    /**
     * 停止震动
     */
    private void stopVibrator() {
        Vibrator vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.cancel();
    }


    /**
     * 播放声音
     */
    private void startBeep() {
        if (playBeep && mediaPlayer != null)
            mediaPlayer.start();
//        else
//            logger.info("当前模式为静音或者震动");
    }

    /**
     * 停止声音播放
     */
    private void stopBeep() {
        if (playBeep && mediaPlayer != null)
            mediaPlayer.stop();
    }

    /**
     * 关闭单次消息提醒
     */
    protected void stopRemind() {
        stopBeep();
        stopVibrator();
    }

    private final BroadcastReceiver mEmergencyNotificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null || context == null)
                return;
//            String action = intent.getAction();
//            if (!action.equals(NOTIFICATION_CLICK_ACTION))
//                return;
            if (mNotificationManager != null) {
                mNotificationManager.cancelAll();
            }
            stopRemind();
        }
    };
}
