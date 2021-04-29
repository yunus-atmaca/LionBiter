package com.lionbiterclacclac;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.lionbiterclacclac.utils.Constants;
import com.lionbiterclacclac.utils.I18n;
import com.lionbiterclacclac.utils.SPController;
import com.lionbiterclacclac.utils.SPManager;
import com.lionbiterclacclac.utils.SharedValues;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, Settings.SettingsListener, Records.RecordListener {

    private static final String TAG = "Main-Activity";

    private ImageView play, settings, records;

    private boolean onStartGame;
    private SPManager spManager;
    private boolean firstInit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            onPlayClick();
        } else if (view.getId() == R.id.settings) {
            onSettingsClick();
        } else if (view.getId() == R.id.records) {
            onRecordClick();
        } else {
            Log.d(TAG, "Unimplemented call");
        }
    }

    private void onPlayClick() {
        setButtonsClickable(false);
        spManager.play(Constants.BUTTON);
        onStartGame = true;

        Intent myIntent = new Intent(MainActivity.this, Game.class);
        MainActivity.this.startActivity(myIntent);
    }

    private void onSettingsClick() {
        setButtonsClickable(false);
        spManager.play(Constants.BUTTON);

        Settings settingsFrag = new Settings(this);
        settingsFrag.show(getSupportFragmentManager(), "Setting-Page");
    }

    private void onRecordClick() {
        setButtonsClickable(false);
        spManager.play(Constants.BUTTON);

        Records records = new Records(this);
        records.show(getSupportFragmentManager(), "Record-Page");
    }

    private void setButtonsClickable(boolean clickable) {
        play.setClickable(clickable);
        settings.setClickable(clickable);
        records.setClickable(clickable);
    }

    @Override
    protected void onDestroy() {
        spManager.releaseSP();
        super.onDestroy();

        finishAndRemoveTask();
        System.exit(0);
    }

    @Override
    protected void onPause() {
        if (!onStartGame) {
            spManager.setSoundOn(false);
            spManager.setBackgroundMusic(false);
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        onStartGame = false;
        setButtonsClickable(true);

        if (firstInit) {
            firstInit = false;
            spManager = SPManager.instance(getApplicationContext());
        } else {
            boolean soundOn = SharedValues.getBoolean(this, Constants.KEY_SOUND, true);
            spManager.setSoundOn(soundOn);
            spManager.setBackgroundMusic(soundOn);
        }
        super.onResume();
    }

    @Override
    public void onLanguageChanged(String lan) {
        play.setImageResource(lan.equals(Constants.LAN_ENG) ? R.drawable.play : R.drawable.play_ru);
        settings.setImageResource(lan.equals(Constants.LAN_ENG) ? R.drawable.settings : R.drawable.settings_ru);
        records.setImageResource(lan.equals(Constants.LAN_ENG) ? R.drawable.records : R.drawable.records_ru);
    }

    @Override
    public void onBack() {
        setButtonsClickable(true);
    }
}