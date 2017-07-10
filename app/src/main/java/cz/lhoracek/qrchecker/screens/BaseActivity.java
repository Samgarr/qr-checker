package cz.lhoracek.qrchecker.screens;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import javax.inject.Inject;

import cz.lhoracek.qrchecker.App;
import cz.lhoracek.qrchecker.R;
import cz.lhoracek.qrchecker.di.activity.ActivityComponent;
import cz.lhoracek.qrchecker.di.activity.ActivityModule;
import cz.lhoracek.qrchecker.di.activity.DaggerActivityComponent;
import pub.devrel.easypermissions.EasyPermissions;


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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}