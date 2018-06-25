package ru.mail.park.lecture11;

import android.app.Application;

public class App extends Application {


    @Override
    public void onCreate() {
        super.onCreate();


        Statistics.init(this);
    }


}
