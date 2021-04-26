package com.lionbiterclacclac;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.lionbiterclacclac.utils.Constants;
import com.lionbiterclacclac.utils.I18n;
import com.lionbiterclacclac.utils.SPController;
import com.lionbiterclacclac.utils.SharedValues;
import com.lionbiterclacclac.utils.SystemUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Main-Activity";

    private boolean onStartGame;

    private SPController spController;
    private boolean firstInit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemUtils.enableFullScreenUI(this);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {

        String lan = SharedValues.getString(getApplicationContext(), Constants.KEY_LANGUAGE, Constants.LAN_ENG);
        I18n.loadLanguage(this, lan);

        findViewById(R.id.play).setOnClickListener(this);
        findViewById(R.id.settings).setOnClickListener(this);
        findViewById(R.id.records).setOnClickListener(this);

        onStartGame = false;
        firstInit = true;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.play){

        }else if(view.getId() == R.id.settings){

        }else if(view.getId() == R.id.records){

        }else {
            Log.d(TAG, "Unimplemented call");
        }
    }

    private void onSettingsClick() {
        spController.play(Constants.BUTTON);

        //Settings settingsFrag = new Settings();
        //settingsFrag.show(getSupportFragmentManager(), "Setting-Page");
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
        if(onStartGame){

        }else{
            spController.releaseSP();
        }

        super.onPause();
    }

    @Override
    protected void onResume() {
        onStartGame = false;

        if(firstInit){
            firstInit = false;
            spController = SPController.getInstance(getApplicationContext());
        }else{
            boolean music = SharedValues.getBoolean(this, Constants.KEY_MUSIC, true);
            spController.setBackgroundMusic(music);
        }

        super.onResume();
    }
}