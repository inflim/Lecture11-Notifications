package ru.mail.park.lecture11;

import android.app.Application;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

public class Statistics {


    private static final String EVENT_BUTTON_CLICK = "button_click";
    private static final String PARAM_BUTTON_NAME = "button_name";

    private static FirebaseAnalytics analytics;


    public static void init(Application app) {
        analytics = FirebaseAnalytics.getInstance(app);
    }


    public static void logButtonClick(String buttonName) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_BUTTON_NAME, buttonName);
        analytics.logEvent(EVENT_BUTTON_CLICK, bundle);
    }

}
