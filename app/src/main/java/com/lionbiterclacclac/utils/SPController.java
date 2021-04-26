package com.lionbiterclacclac.utils;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.util.Log;

public class SPController implements SoundPool.OnLoadCompleteListener {

    private static final String TAG = "SPController";
    private static final int NUMBER_OF_SOUND = 6;
    private static SPController ins = null;

    private SoundPool sp;
    private int background, button, biteBall, gameOver, closeMouth;

    private int backgroundMusic;
    private boolean isBackgroundPlaying;
    private boolean isLoaded;

    private boolean soundOn;

    private final Context context;

    private SPController(Context context){
        soundOn = SharedValues.getBoolean(context, Constants.KEY_SOUND, true);
        isBackgroundPlaying = false;
        isLoaded = true;
        this.context = context;

        initializeSP();
    }

    public static SPController getInstance(Context context){
        if (ins == null)
            ins = new SPController(context);

        return ins;
    }

    private void initializeSP(){
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        sp = new SoundPool.Builder()
                .setMaxStreams(NUMBER_OF_SOUND)
                .setAudioAttributes(attributes)
                .build();

        sp.setOnLoadCompleteListener(this);

        button = sp.load(context, Constants.BUTTON, 1);
        biteBall = sp.load(context, Constants.BITE_BALL, 1);
        gameOver = sp.load(context, Constants.GAME_OVER, 1);
        closeMouth = sp.load(context, Constants.CLOSE_MOUTH, 1);
        background = sp.load(context, Constants.BACKGROUND, 1);
    }

    public void setBackgroundMusic(boolean music) {
        if(sp == null){
            initializeSP();
            soundOn = music;
            return;
        }

        if(!isLoaded)
            return;

        if (music) {
            if(!isBackgroundPlaying) {
                isBackgroundPlaying = true;
                backgroundMusic = sp.play(background, 1, 1, 0, -1, 1);
            }

            soundOn = true;
        } else {
            if(isBackgroundPlaying){
                isBackgroundPlaying = false;
                sp.stop(backgroundMusic);
            }
            soundOn = false;
        }
    }

    public void setSoundOn(boolean sound){
        soundOn = sound;
    }

    public void play(int sound) {
        if(!soundOn)
            return;

        if(sp == null){
            initializeSP();
        }

        switch (sound) {
            case Constants.BUTTON:
                sp.play(button, 1, 1, 0, 0, 1);
                break;
            case Constants.CLOSE_MOUTH:
                sp.play(closeMouth, 1, 1, 0, 0, 1);
                break;
            case Constants.BITE_BALL:
                sp.play(biteBall, 1, 1, 0, 0, 1);
                break;
            case Constants.GAME_OVER:
                sp.play(gameOver, 1, 1, 0, 0, 1);
                break;
            default:
                Log.d(TAG, "Undefined sound");
                break;
        }
    }

    public void releaseSP(){
        if (sp != null) {
            sp.release();
            sp = null;
            isBackgroundPlaying = false;
            isLoaded = false;
        }
    }

    @Override
    public void onLoadComplete(SoundPool soundPool, int i, int i1) {
        if(i == background){
            isLoaded = true;
            if(soundOn){
                if(!isBackgroundPlaying) {
                    isBackgroundPlaying = true;
                    backgroundMusic = sp.play(background, 1, 1, 0, -1, 1);
                }
            }
        }
    }
}
