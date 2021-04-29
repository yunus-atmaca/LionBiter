package com.lionbiterclacclac;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.lionbiterclacclac.utils.Constants;
import com.lionbiterclacclac.utils.SPManager;

import java.util.Objects;

public class Alert extends DialogFragment implements View.OnClickListener {

    private static final String TAG = "Alert-Fragment";

    private final AlertListener listener;
    private SPManager spManager;

    public Alert(AlertListener listener) {
        this.listener = listener;
        spManager = SPManager.instance(getContext());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getDialog() == null) {
            return;
        }
        getDialog().getWindow().setWindowAnimations(R.style.anim_fade);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.alert, container, false);

        root.findViewById(R.id.yes).setOnClickListener(this);
        root.findViewById(R.id.no).setOnClickListener(this);

        ((TextView) root.findViewById(R.id.message)).setText(getText(R.string.alertText));

        Objects.requireNonNull(getDialog()).getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        return root;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.yes) {
            yesClicked();
        } else if (view.getId() == R.id.no) {
            noClicked();
        } else {
            Log.d(TAG, "Unimplemented call");
        }
    }

    private void yesClicked() {
        spManager.play(Constants.BUTTON);

        listener.onModalResult(true);

        dismiss();
        onDestroy();
    }

    private void noClicked() {
        spManager.play(Constants.BUTTON);

        listener.onModalResult(false);

        dismiss();
        onDestroy();
    }

    public interface AlertListener {
        void onModalResult(boolean res);
    }
}
