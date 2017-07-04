package cz.lhoracek.qrchecker.di.application;


import android.content.Context;
import android.media.AudioManager;
import android.os.Vibrator;

import javax.inject.Singleton;

import cz.lhoracek.qrchecker.di.ApplicationContext;
import cz.lhoracek.qrchecker.util.Preferences;
import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {


    @ApplicationContext Context provideContext();

    AudioManager audioManager();

    Vibrator provideVibrator();

    Preferences providePreferences();
}