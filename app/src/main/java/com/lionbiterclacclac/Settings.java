package com.lionbiterclacclac;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.lionbiterclacclac.utils.BackgroundMusicHelper;
import com.lionbiterclacclac.utils.Constants;
import com.lionbiterclacclac.utils.I18n;
import com.lionbiterclacclac.utils.SPManager;
import com.lionbiterclacclac.utils.SharedValues;

public class Settings extends DialogFragment implements View.OnClickListener {

    private static final String TAG = "Setting-Fragment";

    private View root;

    private ImageView title;
    private TextView lanText;
    private TextView vibroText;
    private TextView soundText;
    private TextView lan_text;
    private ImageView soundButton;
    private ImageView vibroButton;
    private ImageView soundIcon;
    private ImageView vibroIcon;

    private boolean soundOn;
    private boolean vibroOn;
    private String lan;

    private SPManager spManager;
    private BackgroundMusicHelper musicHelper;
    private final SettingsListener listener;

    public Settings(SettingsListener listener) {
        this.listener = listener;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getDialog() == null)
            return;

        getDialog().getWindow().setWindowAnimations(R.style.anim_slide);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new Dialog(getActivity(), android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen) {
            @Override
            public void onBackPressed() {
                spManager.play(Constants.BUTTON);
                listener.onBack();
                super.onBackPressed();
            }
        };
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.settings, container, false);

        getAppValues();
        init();

        return root;
    }

    private void init() {
        spManager = SPManager.instance(getContext());
        musicHelper = BackgroundMusicHelper.getIns(getContext());

        root.findViewById(R.id.back).setOnClickListener(this);
        root.findViewById(R.id.lan_left).setOnClickListener(this);
        root.findViewById(R.id.lan_right).setOnClickListener(this);

        soundButton = root.findViewById(R.id.soundButton);
        soundButton.setOnClickListener(this);
        vibroButton = root.findViewById(R.id.vibroButton);
        vibroButton.setOnClickListener(this);

        soundIcon = root.findViewById(R.id.soundIcon);
        vibroIcon = root.findViewById(R.id.vibroIcon);

        title = root.findViewById(R.id.title);
        lanText = root.findViewById(R.id.lanText);
        soundText = root.findViewById(R.id.soundText);
        vibroText = root.findViewById(R.id.vibroText);
        lan_text = root.findViewById(R.id.lan_text);

        title.setImageResource(lan.equals(Constants.LAN_ENG) ? R.drawable.settings_text_eng : R.drawable.settings_text_ru);
        soundButton.setImageResource(soundOn ? R.drawable.ic_switch_on : R.drawable.ic_switch_off);
        vibroButton.setImageResource(vibroOn ? R.drawable.ic_switch_on : R.drawable.ic_switch_off);
    }

    private void getAppValues() {
        if (getContext() == null)
            return;

        soundOn = SharedValues.getBoolean(getContext(), Constants.KEY_SOUND, true);
        vibroOn = SharedValues.getBoolean(getContext(), Constants.KEY_VIBRO, true);
        lan = SharedValues.getString(getContext(), Constants.KEY_LANGUAGE, Constants.LAN_ENG);
    }

    private void setUIAccordingToLan() {
        if (lan.equals(Constants.LAN_ENG)) {
            title.setImageResource(R.drawable.settings_text_eng);
        } else {
            title.setImageResource(R.drawable.settings_text_ru);
        }

        lanText.setText(R.string.lan);
        lan_text.setText(R.string.lanText);
        soundText.setText(R.string.sound);
        vibroText.setText(R.string.vibro);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.back) {
            spManager.play(Constants.BUTTON);
            listener.onBack();
            dismiss();
            onDestroy();
        } else if (view.getId() == R.id.lan_left) {
            spManager.play(Constants.BUTTON);

            onSwitchLanguage();
        } else if (view.getId() == R.id.lan_right) {
            spManager.play(Constants.BUTTON);

            onSwitchLanguage();
        } else if (view.getId() == R.id.soundButton) {
            soundClicked();
        } else if (view.getId() == R.id.vibroButton) {
            spManager.play(Constants.BUTTON);

            vibroClicked();
        } else {
            Log.d(TAG, "Unimplemented call");
        }
    }

    private void soundClicked() {
        if (soundOn) {
            spManager.setSoundOn(false);
            musicHelper.release();

            soundOn = false;
            soundButton.setImageResource(R.drawable.ic_switch_off);
            soundText.setTextColor(ContextCompat.getColor(getContext(), R.color.onColor));
            soundIcon.setImageResource(R.drawable.ic_sound_off);
        } else {
            spManager.setSoundOn(true);
            spManager.play(Constants.BUTTON);
            musicHelper.start();

            soundOn = true;
            soundButton.setImageResource(R.drawable.ic_switch_on);
            soundText.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
            soundIcon.setImageResource(R.drawable.ic_sound);
        }
    }

    private void vibroClicked() {
        if (vibroOn) {
            vibroOn = false;
            vibroButton.setImageResource(R.drawable.ic_switch_off);
            vibroText.setTextColor(ContextCompat.getColor(getContext(), R.color.onColor));
            vibroIcon.setImageResource(R.drawable.ic_vibro_off);
        } else {
            vibroOn = true;
            vibroButton.setImageResource(R.drawable.ic_switch_on);
            vibroText.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
            vibroIcon.setImageResource(R.drawable.ic_vibro);

            vibrate();
        }
    }

    private void vibrate() {
        Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            v.vibrate(500);
        }
    }


    private void onSwitchLanguage() {
        if (lan.equals(Constants.LAN_ENG)) {
            lan = Constants.LAN_RU;
            I18n.loadLanguage(getActivity(), Constants.LAN_RU);
        } else {
            lan = Constants.LAN_ENG;
            I18n.loadLanguage(getActivity(), Constants.LAN_ENG);
        }

        setUIAccordingToLan();
        this.listener.onLanguageChanged(lan);
    }

    @Override
    public void onDestroy() {
        if (getContext() == null) {
            super.onDestroy();
            return;
        }

        SharedValues.setBoolean(getContext(), Constants.KEY_SOUND, soundOn);
        SharedValues.setBoolean(getContext(), Constants.KEY_VIBRO, vibroOn);
        SharedValues.setString(getContext(), Constants.KEY_LANGUAGE, lan);

        super.onDestroy();
    }

    public interface SettingsListener {
        void onLanguageChanged(String lan);

        void onBack();
    }
}
