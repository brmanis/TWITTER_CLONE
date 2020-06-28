package com.example.twitter_clone;

import android.app.Application;

import com.parse.Parse;

public class App extends Application {

    @Override
    public void onCreate() {
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.app_ID))
                // if defined
                .clientKey(getString(R.string.app_key))
                .server(getString(R.string.server_id))
                .build()
        );
        super.onCreate();
    }
}
