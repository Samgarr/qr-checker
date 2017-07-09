package cz.lhoracek.qrchecker.di.activity;


import android.app.Activity;
import android.content.Context;

import cz.lhoracek.qrchecker.di.ActivityContext;
import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    private final Activity activityContext;

    public ActivityModule(Activity activityContext) {
        this.activityContext = activityContext;
    }

    @Provides
    @ActivityContext
    public Context provideContext() {
        return activityContext;
    }

    @Provides
    public Activity provideActivity() {
        return activityContext;
    }
}
