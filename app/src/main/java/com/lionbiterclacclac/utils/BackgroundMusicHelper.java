package com.lionbiterclacclac.utils;

import android.content.Context;
import android.media.MediaPlayer;

import com.lionbiterclacclac.R;

public class BackgroundMusicHelper implements MediaPlayer.OnPreparedListener {

    private static BackgroundMusicHelper ins = null;

    private MediaPlayer player;
    private boolean soundOn;

    private Context context;

    private BackgroundMusicHelper(Context context) {
        soundOn = SharedValues.getBoolean(context, Constants.KEY_SOUND, true);
        this.context = context;

        initPlayer();
    }

    public void initPlayer() {
        player = MediaPlayer.create(context, R.raw.background);
        player.setOnPreparedListener(this);
        player.setLooping(true);
    }

    public static BackgroundMusicHelper getIns(Context context) {
        if (ins == null)
            ins = new BackgroundMusicHelper(context);

        return ins;
    }

    public void start() {
        if (player == null) {
            initPlayer();
            return;
        }

        if (soundOn) {
            player.start();
        }
    }

    public void pause() {
        if (player != null)
            player.pause();
    }

    public void stop() {
        if (player != null)
            player.stop();
    }

    public void setSound(boolean sound) {
        soundOn = sound;
    }

    public void release() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        if (soundOn) {
            player.start();
        }
    }
}
