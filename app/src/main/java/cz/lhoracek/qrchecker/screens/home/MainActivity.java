package cz.lhoracek.qrchecker.screens.home;

import android.databinding.DataBindingUtil;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.OrientationEventListener;

import javax.inject.Inject;

import cz.lhoracek.qrchecker.R;
import cz.lhoracek.qrchecker.databinding.ActivityMainBinding;
import cz.lhoracek.qrchecker.screens.BaseActivity;
import timber.log.Timber;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MainActivity extends BaseActivity {

    private ActivityMainBinding binding;

    @Inject MainViewModel viewModel;
    private OrientationEventListener orientationEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getActivityComponent().inject(this);
        super.onCreate(savedInstanceState);
        viewModel.onCreate();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setViewModel(viewModel);
        initOrientationListener();
    }

    private void initOrientationListener() {
        orientationEventListener = new OrientationEventListener(this, SensorManager.SENSOR_DELAY_NORMAL) {
            @Override
            public void onOrientationChanged(int orientation) {
                Timber.d("Orientation changed %d", orientation);
                int viewOrientation = viewModel.getRotation().get();

                if (Math.abs(orientation - viewOrientation) < 75) {
                    return; // adding some treshhold
                }

                int rotation = (((orientation + 45) / 90) * 90) % 360;
                if (viewOrientation != rotation) {
                    Timber.d("Updating orientation %d", rotation);
                    viewModel.getRotation().set(rotation);
                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.onResume();
        orientationEventListener.enable();
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewModel.onPause();
        orientationEventListener.disable();
    }
}
