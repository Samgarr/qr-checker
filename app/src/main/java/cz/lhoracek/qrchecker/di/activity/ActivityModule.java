package cz.lhoracek.qrchecker.di.activity;


import android.content.Context;

import cz.lhoracek.qrchecker.di.ActivityContext;
import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    private final Context activityContext;

    public ActivityModule(Context activityContext) {
        this.activityContext = activityContext;
    }

    @Provides
    @ActivityContext
    public Context provideContext() {
        return activityContext;
    }
}
