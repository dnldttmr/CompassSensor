package com.mygdx.game.android;

import android.app.ActionBar;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.mygdx.game.MyGdxGame;

public class AndroidLauncher extends AndroidApplication{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment);

        ActionBar actionBar = getActionBar();

        AndroidApplicationConfiguration configuration = new AndroidApplicationConfiguration();
        configuration.disableAudio = true;
        configuration.useAccelerometer = true;
        configuration.useCompass = true;

        LinearLayout layout = (LinearLayout) findViewById(R.id.layout);
        layout.addView(initializeForView(new MyGdxGame(), configuration));

        //Dont show the application logo in the status bar
        actionBar.setDisplayShowHomeEnabled(false);
    }
}

