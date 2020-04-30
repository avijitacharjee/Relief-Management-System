package com.avijit.rms;

import android.app.Activity;
import android.app.Application;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Toast;

public class App extends Application {
    App app;
    @Override
    public void onCreate() {
            super.onCreate();
            app = this;
    }

}

