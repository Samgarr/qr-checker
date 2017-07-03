package cz.lhoracek.qrchecker.di.application;


import android.content.Context;
import android.os.Vibrator;

import java.util.Properties;

import javax.inject.Singleton;

import cz.lhoracek.qrchecker.di.ApplicationContext;
import cz.lhoracek.qrchecker.util.Preferences;
import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private Context appContext;

    public AppModule(Context appContext) {
        this.appContext = appContext;
    }

    @Provides
    @ApplicationContext
    public Context provideContext() {
        return appContext;
    }

    @Provides
    public Vibrator provideVibrator() {
        return (Vibrator) appContext.getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Provides
    @Singleton
    public Preferences providePreferences() {
        return new Preferences();
    }
}
