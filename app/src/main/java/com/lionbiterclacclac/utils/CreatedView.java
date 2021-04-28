package com.lionbiterclacclac.utils;

import android.widget.ImageView;

public class CreatedView {
    private String name;
    private ImageView view;

    public CreatedView(String name, ImageView view) {
        this.name = name;
        this.view = view;
    }

    public String getName() {
        return name;
    }

    public ImageView getView() {
        return view;
    }
}
