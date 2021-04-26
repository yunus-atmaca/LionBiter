package com.lionbiterclacclac;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lionbiterclacclac.utils.Constants;
import com.lionbiterclacclac.utils.SPController;
import com.lionbiterclacclac.utils.SharedValues;

public class Game extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Game-Activity";

    private ImageView endGameLion;
    private TextView scoreText;
    private TextView yourScoreText;
    private LinearLayoutCompat endGameLayout;

    private boolean vibroOn;
    private String lan;

    SPController spController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        getAppValues();
        init();
    }

    private void init(){
        spController = SPController.getInstance(this);

        endGameLion = findViewById(R.id.end_game_lion);
        scoreText = findViewById(R.id.scoreText);
        yourScoreText = findViewById(R.id.yourScoreText);
        endGameLayout = findViewById(R.id.endGameLayout);

        ImageView restart = findViewById(R.id.restart);
        restart.setOnClickListener(this);
        ImageView mainMenu = findViewById(R.id.mainmenu);
        mainMenu.setOnClickListener(this);

        restart.setImageResource(lan.equals(Constants.LAN_ENG) ? R.drawable.restart : R.drawable.restart_ru);
        mainMenu.setImageResource(lan.equals(Constants.LAN_ENG) ? R.drawable.mainmenu : R.drawable.mainmenu_ru);
    }

    private void getAppValues() {
        vibroOn = SharedValues.getBoolean(this, Constants.KEY_VIBRO, true);
        lan = SharedValues.getString(this, Constants.KEY_LANGUAGE, Constants.LAN_ENG);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.restart){

        }else if(view.getId() == R.id.mainmenu){

        }else{
            Log.d(TAG, "Unimplemented call");
        }
    }
}