package com.herenit.mobilenurse.app.utils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.support.annotation.RawRes;

import com.herenit.mobilenurse.R;import com.herenit.mobilenurse.R2;
import com.herenit.mobilenurse.app.MobileNurseApplication;

import java.io.IOException;

/**
 * 媒体工具类
 */
public class SoundPlayUtils {

    // SoundPool对象
    public static SoundPool mSoundPlayer = new SoundPool(10,
            AudioManager.STREAM_SYSTEM, 5);
    public static SoundPlayUtils soundPlayUtils;
    // 上下文
    static Context mContext;

    /**
     * 初始化
     *
     * @param context
     */
    public static SoundPlayUtils init(Context context) {
        if (soundPlayUtils == null) {
            soundPlayUtils = new SoundPlayUtils();
        }

        // 初始化声音
        mContext = context;

        mSoundPlayer.load(mContext, R.raw.excute_success, 1);// 1
        mSoundPlayer.load(mContext, R.raw.buzz, 1);// 2
        mSoundPlayer.load(mContext, R.raw.sound_ok, 1);// 3
//        mSoundPlayer.load(mContext, R.raw.ding, 1);// 4
//        mSoundPlayer.load(mContext, R.raw.gone, 1);// 5
//        mSoundPlayer.load(mContext, R.raw.popup, 1);// 6
//        mSoundPlayer.load(mContext, R.raw.water, 1);// 7
//        mSoundPlayer.load(mContext, R.raw.ying, 1);// 8

        return soundPlayUtils;
    }

    /**
     * 播放声音
     *
     * @param soundID 1,执行成功；2，警告； 3，OK
     */
    public static void play(int soundID) {
        mSoundPlayer.play(soundID, 1, 1, 0, 0, 1);
    }


}
