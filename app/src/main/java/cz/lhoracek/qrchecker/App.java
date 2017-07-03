package cz.lhoracek.qrchecker;


import android.app.Application;

import cz.lhoracek.qrchecker.di.AppComponent;
import cz.lhoracek.qrchecker.di.AppModule;
import cz.lhoracek.qrchecker.di.DaggerAppComponent;
import timber.log.Timber;

public class App extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder()
                // list of modules that are part of this component need to be created here too
                .appModule(new AppModule(this)) // This also corresponds to the name of your module: %component_name%Module
                .build();


        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
