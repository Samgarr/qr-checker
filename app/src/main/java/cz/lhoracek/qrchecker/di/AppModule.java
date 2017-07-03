package cz.lhoracek.qrchecker.di;


import android.content.Context;
import android.os.Vibrator;

import java.util.Properties;

import javax.inject.Singleton;

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
    public Vibrator provideVibrator() {
        return (Vibrator) appContext.getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Provides
    @Singleton
    public Preferences providePreferences() {
        return new Preferences();
    }
}
