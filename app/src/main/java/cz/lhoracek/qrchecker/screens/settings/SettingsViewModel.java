package cz.lhoracek.qrchecker.screens.settings;


import android.app.Activity;
import android.content.Context;

import javax.inject.Inject;

import cz.lhoracek.qrchecker.di.ActivityContext;
import cz.lhoracek.qrchecker.util.Preferences;

public class SettingsViewModel {
    private final Context activityContext;
    private final Preferences preferences;

    @Inject
    public SettingsViewModel(@ActivityContext Context activityContext,
                             Preferences preferences) {
        this.activityContext = activityContext;
        this.preferences = preferences;
    }

    public void onFileSelected(String path) {
        // TODO
    }

    public void onSave() {
        // TODO
        ((Activity) activityContext).finish();
    }
}
