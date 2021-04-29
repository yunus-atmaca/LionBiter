package com.lionbiterclacclac;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lionbiterclacclac.utils.AnimUpdateListener;
import com.lionbiterclacclac.utils.Constants;
import com.lionbiterclacclac.utils.CreatedView;
import com.lionbiterclacclac.utils.GestureHandler;
import com.lionbiterclacclac.utils.SPController;
import com.lionbiterclacclac.utils.SPManager;
import com.lionbiterclacclac.utils.SharedValues;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Game extends AppCompatActivity implements
        View.OnClickListener,
        Animation.AnimationListener,
        AnimUpdateListener.GameListener,
        GestureHandler.OnTouchListener {

    private static final String TAG = "Game-Activity";

    private RelativeLayout root;
    private LinearLayoutCompat scoreTable;

    private ImageView endGameLion;
    private TextView scoreText;
    private TextView yourScoreText;
    private LinearLayoutCompat endGameLayout;
    private ImageView lion;
    private ImageView lionMouth;

    private boolean vibroOn;
    private String lan;

    private Handler handler;
    private Runnable runnable;

    private float toValue;
    public static boolean isMouthOpened;
    public static boolean isLongPressed;

    private ArrayList<CreatedView> createdViews;
    private Queue<ImageView> imagesWillBeDeleted;

    private float reasonablePosition;
    private boolean isGameOver;
    private int score;

    private boolean onBack;
    private boolean firstInit;
    private SPManager spManager;

    private int interval = 2000;
    private int animDuration = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        getAppValues();
        init();
    }

    private void init() {
        root = findViewById(R.id.root);
        root.setOnTouchListener(new GestureHandler(this, this));
        handler = new Handler();

        onBack = false;
        firstInit = true;
        isMouthOpened = false;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;

        float marginBottomPx = convertDpToPixel(85);
        float marginTopPx = convertDpToPixel(5);

        reasonablePosition = height - marginBottomPx + marginTopPx;
        toValue = height + 400;

        lion = findViewById(R.id.lion);
        lionMouth = findViewById(R.id.lion_mouth);
        endGameLion = findViewById(R.id.end_game_lion);
        scoreText = findViewById(R.id.scoreText);
        yourScoreText = findViewById(R.id.yourScoreText);
        endGameLayout = findViewById(R.id.endGameLayout);
        scoreTable = findViewById(R.id.scoreTable);

        ImageView restart = findViewById(R.id.restart);
        restart.setOnClickListener(this);
        ImageView mainMenu = findViewById(R.id.mainmenu);
        mainMenu.setOnClickListener(this);

        restart.setImageResource(lan.equals(Constants.LAN_ENG) ? R.drawable.restart : R.drawable.restart_ru);
        mainMenu.setImageResource(lan.equals(Constants.LAN_ENG) ? R.drawable.mainmenu : R.drawable.mainmenu_ru);

        startGame();
    }

    private void startGame() {
        score = 0;
        isGameOver = false;
        createdViews = new ArrayList<>();
        imagesWillBeDeleted = new LinkedList<>();

        runnable = new Runnable() {
            @Override
            public void run() {
                if (isGameOver)
                    return;

                createViewsAndAnimations();
                handler.postDelayed(this, getNewIntervalTime());
            }
        };

        handler.postDelayed(runnable, getNewIntervalTime());
    }

    private int getNewIntervalTime() {
        int minInterval = 700;

        if (interval > 1000) {
            interval -= 75;
        }

        int random = new Random().nextInt(interval - minInterval);

        return minInterval + random;
    }

    private int getNewAnimTime() {
        if(animDuration > 1250){
            animDuration -= 100;
        }

        return animDuration;
    }

    private float convertDpToPixel(float dp) {
        return dp * ((float) getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    private void createViewsAndAnimations() {
        CreatedView createdView = createImageView();
        AnimUpdateListener listener = new AnimUpdateListener(createdView, reasonablePosition, this);

        ObjectAnimator animation = ObjectAnimator.ofFloat(createdView.getView(), "translationY", toValue);
        animation.addListener(listener);
        animation.setDuration(getNewAnimTime());
        animation.addUpdateListener(listener);
        animation.start();
    }

    private CreatedView createImageView() {
        ImageView imageView = new ImageView(this);
        RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams((int) convertDpToPixel(110), (int) convertDpToPixel(110));
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.topMargin = (int) convertDpToPixel(-115);
        imageView.setLayoutParams(params);

        int randInt = new Random().nextInt(100);

        String name = "";
        if (randInt % 3 == 0) {
            imageView.setImageResource(R.drawable.ic_footb);
            name = Constants.BALL;
        } else if (randInt % 3 == 1) {
            imageView.setImageResource(R.drawable.ic_footb_1);
            name = Constants.BALL;
        } else {
            imageView.setImageResource(R.drawable.ic_bomb);
            name = Constants.BOMB;
        }

        root.addView(imageView);
        CreatedView createdView = new CreatedView(name, imageView);
        createdViews.add(createdView);
        return createdView;
    }

    private void getAppValues() {
        vibroOn = SharedValues.getBoolean(this, Constants.KEY_VIBRO, true);
        lan = SharedValues.getString(this, Constants.KEY_LANGUAGE, Constants.LAN_ENG);
    }

    private void bounceTheView(ImageView view) {
        int randIntForX = new Random().nextInt(100);
        int randIntForY = new Random().nextInt(100);

        float toXDelta = -1000;
        float toYDelta = -300 - randIntForY;

        if (randIntForX % 2 == 0) {
            toXDelta *= -1;
        }

        imagesWillBeDeleted.add(view);
        TranslateAnimation anim = new TranslateAnimation(0, toXDelta, 0, toYDelta);
        anim.setDuration(400);
        anim.setAnimationListener(this);

        view.startAnimation(anim);
    }

    private void deleteAllImageView() {
        try {
            for (int i = 0; i < createdViews.size(); ++i) {
                removeView(createdViews.get(i).getView());
            }
        } catch (Exception ignored) {
        }
    }

    private void setGameOverValues() {
        try {
            setScoreToApp();

            lion.setImageResource(R.drawable.lion_close);
            yourScoreText.setText(getString(R.string.yourScore) + ": " + scoreText.getText());
            lion.setVisibility(View.GONE);
            lionMouth.setVisibility(View.GONE);
            scoreTable.setVisibility(View.GONE);

            endGameLayout.setVisibility(View.VISIBLE);
            endGameLion.setVisibility(View.VISIBLE);

            handler.removeCallbacksAndMessages(null);
            handler.removeCallbacks(runnable);
        } catch (Exception ignored) {
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.restart) {
            onRestartGame();
        } else if (view.getId() == R.id.mainmenu) {
            spManager.play(Constants.BUTTON);
            onBackGame();
            this.finish();
        } else {
            Log.d(TAG, "Unimplemented call");
        }
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        ImageView view = imagesWillBeDeleted.poll();
        removeView(view);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    private void updateScore(ImageView imageView) {
        ++score;
        runOnUiThread(() -> {
            scoreText.setText(String.valueOf(score));
            removeView(imageView);
        });
    }

    private void removeView(ImageView view) {
        if (view != null) {
            view.setVisibility(View.GONE);
            root.removeView(view);
        }
    }

    private void setScoreToApp() {
        int score1 = SharedValues.getInt(this, Constants.KEY_SCORE_1, 0);
        int score2 = SharedValues.getInt(this, Constants.KEY_SCORE_2, 0);
        int score3 = SharedValues.getInt(this, Constants.KEY_SCORE_3, 0);


        if (score > score1) {
            SharedValues.setInt(this, Constants.KEY_SCORE_1, score);
            SharedValues.setInt(this, Constants.KEY_SCORE_2, score1);
            SharedValues.setInt(this, Constants.KEY_SCORE_3, score2);
        } else if (score > score2) {
            SharedValues.setInt(this, Constants.KEY_SCORE_2, score);
            SharedValues.setInt(this, Constants.KEY_SCORE_3, score2);
        } else if (score > score3) {
            SharedValues.setInt(this, Constants.KEY_SCORE_3, score);
        }
    }

    private void onRestartGame() {
        spManager.play(Constants.BUTTON);

        lion.setVisibility(View.VISIBLE);
        scoreTable.setVisibility(View.VISIBLE);

        lionMouth.setVisibility(View.INVISIBLE);
        endGameLayout.setVisibility(View.GONE);
        endGameLion.setVisibility(View.GONE);

        scoreText.setText(String.valueOf(0));

        startGame();
    }

    private void onPauseGame() {
        isGameOver = true;

        spManager.setSoundOn(false);
        spManager.setBackgroundMusic(false);

        deleteAllImageView();

        setGameOverValues();
    }

    private void onBackGame() {
        isGameOver = true;

        spManager.stop(Constants.GAME_OVER);

        handler.removeCallbacksAndMessages(null);
        handler.removeCallbacks(runnable);

        onBack = true;
    }

    @Override
    public void onScoreUpdate(ImageView imageView) {
        spManager.play(Constants.BITE_BALL);
        updateScore(imageView);
    }

    @Override
    public void onGameOver(ImageView imageView, boolean isItBomb) {
        if (isGameOver)
            return;

        isGameOver = true;

        spManager.play(Constants.GAME_OVER);
        deleteAllImageView();
        if (isItBomb) {
            lionMouth.setVisibility(View.VISIBLE);

            new Handler().postDelayed(() -> setGameOverValues(), 1000);

        } else {
            setGameOverValues();
        }
    }

    @Override
    public void onBounceBomb(ImageView imageView) {
        bounceTheView(imageView);
    }

    @Override
    public void onRemove(ImageView imageView) {

    }

    @Override
    public void onDown() {
        if (isGameOver)
            return;

        isLongPressed = false;
        isMouthOpened = true;

        lion.setImageResource(R.drawable.lion_open);
    }

    @Override
    public void onUp() {
        if (isGameOver)
            return;

        isLongPressed = false;
        isMouthOpened = false;

        lion.setImageResource(R.drawable.lion_close);
        spManager.play(Constants.CLOSE_MOUTH);
    }

    @Override
    public void onLongPress() {
        if (isGameOver)
            return;

        isLongPressed = true;
        isMouthOpened = false;

        lion.setImageResource(R.drawable.lion_close);
        spManager.play(Constants.CLOSE_MOUTH);
    }

    @Override
    public void onBackPressed() {
        onBackGame();
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        onBack = false;

        if (firstInit) {
            firstInit = false;
            spManager = SPManager.instance(this);
        } else {
            boolean music = SharedValues.getBoolean(this, Constants.KEY_SOUND, true);
            spManager.setSoundOn(music);
            spManager.setBackgroundMusic(music);
        }

        super.onResume();
    }

    @Override
    protected void onPause() {
        if (onBack) {

        } else {
            onPauseGame();
        }

        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (onBack) {
            super.onDestroy();
            return;
        }

        spManager.releaseSP();
        super.onDestroy();
        finishAffinity();
        System.exit(0);
    }
}