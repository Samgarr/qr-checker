package cz.lhoracek.qrchecker.util;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Inject;
import javax.inject.Singleton;

import cz.lhoracek.qrchecker.di.ActivityContext;

@Singleton
public class Preferences {
    private static final String FILENAME = "filename";

    private final SharedPreferences mSharedPref;

    @Inject
    public Preferences(@ActivityContext Context context) {
        mSharedPref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public String getFilename() {
        return mSharedPref.getString(FILENAME, null);
    }

    public void setFilename(String filename) {
        mSharedPref.edit().putString(FILENAME, filename).apply();
    }
}