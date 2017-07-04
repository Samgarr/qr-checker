package cz.lhoracek.qrchecker.screens.home;


import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.graphics.PointF;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;

import java.io.File;

import javax.inject.Inject;

import cz.lhoracek.qrchecker.R;
import cz.lhoracek.qrchecker.di.ActivityContext;
import cz.lhoracek.qrchecker.screens.list.ListActivity;
import cz.lhoracek.qrchecker.util.Preferences;
import cz.lhoracek.qrchecker.util.SoundPoolPlayer;


public class MainViewModel {
    ObservableField<PointF[]> points = new ObservableField<>();
    ObservableInt rotation = new ObservableInt(0);
    ObservableField<Boolean> valid = new ObservableField<>(true); // TODO remove
    ObservableBoolean torch = new ObservableBoolean(false);

    private final Vibrator vibrator;
    private final SoundPoolPlayer soundPoolPlayer;
    private final Context activityContext;
    private final AudioManager audioManager;
    private final Preferences preferences;

    @Inject
    public MainViewModel(Vibrator vibrator,
                         SoundPoolPlayer soundPoolPlayer,
                         @ActivityContext Context activityContext,
                         AudioManager audioManager,
                         Preferences preferences) {
        this.vibrator = vibrator;
        this.soundPoolPlayer = soundPoolPlayer;
        this.activityContext = activityContext;
        this.audioManager = audioManager;
        this.preferences = preferences;
    }

    public ObservableField<PointF[]> getPoints() {
        return points;
    }

    public ObservableInt getRotation() {
        return rotation;
    }

    public ObservableField<Boolean> getValid() {
        return valid;
    }

    public ObservableBoolean getTorch() {
        return torch;
    }

    public View.OnTouchListener getScreenTouchListener() {
        return (v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    soundPoolPlayer.playShortResource(R.raw.ok);
                    torch.set(true);
                    break;
                case MotionEvent.ACTION_UP:
                    torch.set(false);
                    break;
            }
            return true;
        };
    }

    public QRCodeReaderView.OnQRCodeReadListener getQrCodeListener() {
        return (text, points) -> {
            this.points.set(points);
            if (!text.equals(lastText)) {


                // TODO update
                valid.set(true);
                vibrator.vibrate(250);
                soundPoolPlayer.playShortResource(R.raw.ok);
                handler.postDelayed(clearValid, 1000);
            }
            this.lastText = text;
            handler.removeCallbacks(clearPoints);
            handler.postDelayed(clearPoints, 500);
        };
    }

    public void onCreate() {
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
        if (preferences.getFilename() == null) {
            startFilePicker();
        } else if (!new File(preferences.getFilename()).exists()) {
            preferences.setFilename(null);
            startFilePicker();
        }
    }

    private void startFilePicker() {
        activityContext.startActivity(new Intent(activityContext, ListActivity.class));
    }

    public View.OnClickListener getFabListener() {
        return v -> startFilePicker();
    }

    String lastText = null;
    Runnable clearPoints = () -> points.set(null);
    Runnable clearValid = () -> valid.set(null);
    Handler handler = new Handler();

    public void onResume() {
        soundPoolPlayer.loadSound(R.raw.ok);
        soundPoolPlayer.loadSound(R.raw.fail);
    }

    public void onPause() {
        soundPoolPlayer.unloadSound(R.raw.ok);
        soundPoolPlayer.unloadSound(R.raw.fail);
    }

}
