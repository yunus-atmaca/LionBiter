package com.lionbiterclacclac.utils;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import static com.lionbiterclacclac.Game.isMouthOpened;

public class AnimUpdateListener implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {
    private CreatedView createdView;
    private float position;

    private boolean isInFirstIf;
    private boolean isInSecondIf;
    private boolean isInThirdIf;

    private boolean isMouthOpen;

    private GameListener listener;

    public AnimUpdateListener(CreatedView createdView, float position, GameListener listener) {
        this.createdView = createdView;
        this.position = position;
        this.isInFirstIf = false;
        this.isInSecondIf = false;
        this.isInThirdIf = false;
        this.isMouthOpen = false;
        this.listener = listener;
    }

    @Override
    public void onAnimationUpdate(ValueAnimator valueAnimator) {
        float value = (float) valueAnimator.getAnimatedValue();

        if (value < position && value > position - 100) {
            if (!isInFirstIf) {
                isInFirstIf = true;
                if (!isMouthOpened) {
                    if (createdView.getName().equals(Constants.BOMB)) {
                        Log.d("MY-LISTENER", "BOUNCE: " + createdView.getName());
                        ImageView imageView = createdView.getView();
                        valueAnimator.cancel();
                        this.listener.onBounceBomb(imageView);
                    }
                } else {
                    if (!isMouthOpen)
                        isMouthOpen = true;
                }
            }
        }

        if (value < position && value > position - 300) {
            if (isMouthOpened) {
                if (!isMouthOpen)
                    isMouthOpen = true;
            }

        }

        if (value >= position && value <= position + 300) {

            if (!isInSecondIf) {
                isInSecondIf = true;
                if (!isMouthOpened && isMouthOpen) {
                    ImageView imageView = createdView.getView();
                    if (createdView.getName().equals(Constants.BOMB)) {
                        Log.d("MY-LISTENER", "GAME OVER - BOMB: " + createdView.getName());
                        this.listener.onGameOver(imageView, true);
                    } else {
                        Log.d("MY-LISTENER", "SCORE: " + createdView.getName());
                        imageView.setVisibility(View.GONE);
                        valueAnimator.cancel();
                        this.listener.onScoreUpdate(imageView);
                    }
                }
            }
        }

        if (value > position + 400) {
            if (!isInThirdIf) {
                isInThirdIf = true;
                Log.d("MY-LISTENER", "GAME-OVER: " + createdView.getName());
                this.listener.onGameOver(createdView.getView(), false);
            }
        }
    }

    @Override
    public void onAnimationStart(Animator animator) {

    }

    @Override
    public void onAnimationEnd(Animator animator) {
        this.listener.onRemove(createdView.getView());
    }

    @Override
    public void onAnimationCancel(Animator animator) {

    }

    @Override
    public void onAnimationRepeat(Animator animator) {

    }

    public interface GameListener {
        void onScoreUpdate(ImageView imageView);

        void onGameOver(ImageView imageView, boolean isItBomb);

        void onBounceBomb(ImageView imageView);

        void onRemove(ImageView imageView);
    }
}
