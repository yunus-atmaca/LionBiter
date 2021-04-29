package com.lionbiterclacclac;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.lionbiterclacclac.utils.Constants;
import com.lionbiterclacclac.utils.SPManager;
import com.lionbiterclacclac.utils.SharedValues;

public class Records extends DialogFragment implements View.OnClickListener, Alert.AlertListener {

    private static final String TAG = "Records-Fragment";

    private View root;

    private ImageView title;
    private TextView score1Text, score2Text, score3Text;

    private String lan;
    private int score1, score2, score3;

    private SPManager spManager;
    private RecordListener listener;

    public Records(RecordListener listener) {
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
        root = inflater.inflate(R.layout.records, container, false);

        getAppValues();
        init();

        return root;
    }

    private void init() {
        spManager = SPManager.instance(getContext());

        root.findViewById(R.id.delete).setOnClickListener(this);
        root.findViewById(R.id.backRecord).setOnClickListener(this);

        title = root.findViewById(R.id.title);
        score1Text = root.findViewById(R.id.score1Text);
        score2Text = root.findViewById(R.id.score2Text);
        score3Text = root.findViewById(R.id.score3Text);

        score1Text.setText(String.valueOf(score1));
        score2Text.setText(String.valueOf(score2));
        score3Text.setText(String.valueOf(score3));

        title.setImageResource(lan.equals(Constants.LAN_ENG) ? R.drawable.ic_result_text_en : R.drawable.ic_result_text_ru);
    }

    private void getAppValues() {
        if (getContext() == null)
            return;

        lan = SharedValues.getString(getContext(), Constants.KEY_LANGUAGE, Constants.LAN_ENG);
        score1 = SharedValues.getInt(getContext(), Constants.KEY_SCORE_1, 0);
        score2 = SharedValues.getInt(getContext(), Constants.KEY_SCORE_2, 0);
        score3 = SharedValues.getInt(getContext(), Constants.KEY_SCORE_3, 0);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.delete) {
            spManager.play(Constants.BUTTON);

            onDeleteClick();
        } else if (view.getId() == R.id.backRecord) {
            spManager.play(Constants.BUTTON);
            this.listener.onBack();

            dismiss();
            onDestroy();
        } else {
            Log.d(TAG, "Unimplemented call");
        }
    }

    private void onDeleteClick() {
        Alert alert = new Alert(this);
        alert.show(getChildFragmentManager(), "Alert-View");
    }

    private void removeScores() {
        if (getContext() == null)
            return;

        SharedValues.setInt(getContext(), Constants.KEY_SCORE_1, 0);
        SharedValues.setInt(getContext(), Constants.KEY_SCORE_2, 0);
        SharedValues.setInt(getContext(), Constants.KEY_SCORE_3, 0);

        score1Text.setText(String.valueOf(0));
        score2Text.setText(String.valueOf(0));
        score3Text.setText(String.valueOf(0));
    }

    @Override
    public void onModalResult(boolean res) {
        if (res) {
            removeScores();
        }
    }

    public interface RecordListener {
        void onBack();
    }

}
