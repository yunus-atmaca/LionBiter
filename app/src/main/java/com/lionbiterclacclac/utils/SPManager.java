package com.lionbiterclacclac.utils;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.util.Log;

public class SPManager  {
    private static final String TAG = "SPManager";
    private static final int NUMBER_OF_SOUND = 4;

    private static SPManager ins = null;

    private final Context context;
    private boolean soundOn;

    private SoundPool sp;
    private int  button, biteBall, gameOver, closeMouth;
    private int  buttonId, biteBallId, gameOverId, closeMouthId;

    private SPManager(Context context) {
        soundOn = SharedValues.getBoolean(context, Constants.KEY_SOUND, true);;
        this.context = context;

        initializeSP();
    }

    public static SPManager instance(Context context) {
        if (ins == null)
            ins = new SPManager(context);

        return ins;
    }


    private void initializeSP() {
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        sp = new SoundPool.Builder()
                .setMaxStreams(NUMBER_OF_SOUND)
                .setAudioAttributes(attributes)
                .build();

        button = sp.load(context, Constants.BUTTON, 1);
        biteBall = sp.load(context, Constants.BITE_BALL, 1);
        gameOver = sp.load(context, Constants.GAME_OVER, 1);
        closeMouth = sp.load(context, Constants.CLOSE_MOUTH, 1);
    }

    public void setSoundOn(boolean sound) {
        soundOn = sound;
    }

    public void play(int sound) {
        if (!soundOn)
            return;

        if (sp == null) {
            initializeSP();
        }

        switch (sound) {
            case Constants.BUTTON:
                buttonId = sp.play(button, 1, 1, 0, 0, 1);
                break;
            case Constants.CLOSE_MOUTH:
                closeMouthId = sp.play(closeMouth, 1, 1, 0, 0, 1);
                break;
            case Constants.BITE_BALL:
                biteBallId = sp.play(biteBall, 1, 1, 0, 0, 1);
                break;
            case Constants.GAME_OVER:
                gameOverId = sp.play(gameOver, 1, 1, 0, 0, 1);
                break;

            default:
                Log.d(TAG, "Undefined sound");
                break;
        }
    }

    public void stop(int sound) {
        if (sp == null)
            return;

        switch (sound) {
            case Constants.BUTTON:
                sp.stop(buttonId);
                break;
            case Constants.CLOSE_MOUTH:
                sp.stop(closeMouthId);
                break;
            case Constants.BITE_BALL:
                sp.stop(biteBallId);
                break;
            case Constants.GAME_OVER:
                sp.stop(gameOverId);
                break;
            default:
                Log.d(TAG, "Undefined sound");
                break;
        }

    }

    public void releaseSP() {
        if (sp != null) {
            sp.release();
            sp = null;
        }
    }
}
