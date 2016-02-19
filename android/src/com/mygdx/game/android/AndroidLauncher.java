package com.mygdx.game.android;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

        LinearLayout layout = (LinearLayout) findViewById(R.id.layout);
        layout.addView(initializeForView(new MyGdxGame(), configuration));

        //Dont show the application logo in the status bar
        actionBar.setDisplayShowHomeEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0,0,0,"Settings").setIcon(R.drawable.ic_launcher).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
    }
}

