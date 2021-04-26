package com.lionbiterclacclac;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.lionbiterclacclac.utils.Constants;
import com.lionbiterclacclac.utils.I18n;
import com.lionbiterclacclac.utils.SPController;
import com.lionbiterclacclac.utils.SharedValues;
import com.lionbiterclacclac.utils.SystemUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, Settings.SettingsListener {

    private static final String TAG = "Main-Activity";

    private ImageView play, settings, records;

    private boolean onStartGame;

    private SPController spController;
    private boolean firstInit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //SystemUtils.enableFullScreenUI(this);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        firstInit = true;

        String lan = SharedValues.getString(getApplicationContext(), Constants.KEY_LANGUAGE, Constants.LAN_ENG);
        I18n.loadLanguage(this, lan);

        play = findViewById(R.id.play);
        play.setOnClickListener(this);

        settings = findViewById(R.id.settings);
        settings.setOnClickListener(this);

        records = findViewById(R.id.records);
        records.setOnClickListener(this);

        onLanguageChanged(lan);

        onStartGame = false;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.play) {

        } else if (view.getId() == R.id.settings) {
            onSettingsClick();
        } else if (view.getId() == R.id.records) {
            onRecordClick();
        } else {
            Log.d(TAG, "Unimplemented call");
        }
    }

    private void onSettingsClick() {
        spController.play(Constants.BUTTON);

        Settings settingsFrag = new Settings(this);
        settingsFrag.show(getSupportFragmentManager(), "Setting-Page");
    }

    private void onRecordClick() {
        spController.play(Constants.BUTTON);

        Records records = new Records();
        records.show(getSupportFragmentManager(), "Record-Page");
    }

    @Override
    protected void onDestroy() {
        spController.releaseSP();
        super.onDestroy();

        finishAndRemoveTask();
        System.exit(0);
    }

    @Override
    protected void onPause() {
        if (onStartGame) {

        } else {
            spController.releaseSP();
        }

        super.onPause();
    }

    @Override
    protected void onResume() {
        onStartGame = false;

        if (firstInit) {
            firstInit = false;
            spController = SPController.getInstance(getApplicationContext());
        } else {
            boolean music = SharedValues.getBoolean(this, Constants.KEY_SOUND, true);
            spController.setBackgroundMusic(music);
        }

        super.onResume();
    }

    @Override
    public void onLanguageChanged(String lan) {
        play.setImageResource(lan.equals(Constants.LAN_ENG) ? R.drawable.play : R.drawable.play_ru);
        settings.setImageResource(lan.equals(Constants.LAN_ENG) ? R.drawable.settings : R.drawable.settings_ru);
        records.setImageResource(lan.equals(Constants.LAN_ENG) ? R.drawable.records : R.drawable.records_ru);
    }
}