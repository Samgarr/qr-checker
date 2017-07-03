package cz.lhoracek.qrchecker;


import android.app.Application;

import cz.lhoracek.qrchecker.di.application.AppComponent;
import cz.lhoracek.qrchecker.di.application.AppModule;
import cz.lhoracek.qrchecker.di.application.DaggerAppComponent;
import timber.log.Timber;

public class App extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
