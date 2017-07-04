package cz.lhoracek.qrchecker.screens;

import android.support.v7.app.AppCompatActivity;

import cz.lhoracek.qrchecker.App;
import cz.lhoracek.qrchecker.di.activity.ActivityComponent;
import cz.lhoracek.qrchecker.di.activity.ActivityModule;
import cz.lhoracek.qrchecker.di.activity.DaggerActivityComponent;


public abstract class BaseActivity extends AppCompatActivity {
    private ActivityComponent activityComponent;

    public ActivityComponent getActivityComponent() {
        if (activityComponent == null) {
            activityComponent = DaggerActivityComponent.builder()
                    .activityModule(new ActivityModule(this))
                    .appComponent(((App) getApplication()).getAppComponent())
                    .build();
        }
        return activityComponent;
    }
}