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

import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;

import javax.inject.Inject;

import cz.lhoracek.qrchecker.R;
import cz.lhoracek.qrchecker.di.ActivityContext;
import cz.lhoracek.qrchecker.screens.list.ItemViewModel;
import cz.lhoracek.qrchecker.screens.list.ListActivity;
import cz.lhoracek.qrchecker.util.DataManager;
import cz.lhoracek.qrchecker.util.Preferences;
import cz.lhoracek.qrchecker.util.SoundPoolPlayer;
import timber.log.Timber;


public class MainViewModel {
    private static final long[] PATTERN_FAIL = {250l, 100l, 250l, 100l, 250l};
    private static final long[] PATTERN_ALREADY_VALIDATED = {500l, 100l, 200l, 100l, 550l};

    public enum CheckState {
        VALID,
        INVALID,
        ALREADY_VALIDATED,
    }

    ObservableField<PointF[]> points = new ObservableField<>();
    ObservableInt rotation = new ObservableInt(0);
    ObservableField<CheckState> valid = new ObservableField<>(null);
    ObservableBoolean torch = new ObservableBoolean(false);

    private final Vibrator vibrator;
    private final SoundPoolPlayer soundPoolPlayer;
    private final Context activityContext;
    private final AudioManager audioManager;
    private final DataManager dataManager;
    private final Preferences preferences;

    @Inject
    public MainViewModel(Vibrator vibrator,
                         SoundPoolPlayer soundPoolPlayer,
                         @ActivityContext Context activityContext,
                         AudioManager audioManager,
                         DataManager dataManager,
                         Preferences preferences) {
        this.vibrator = vibrator;
        this.soundPoolPlayer = soundPoolPlayer;
        this.activityContext = activityContext;
        this.audioManager = audioManager;
        this.dataManager = dataManager;
        this.preferences = preferences;
    }

    public ObservableField<PointF[]> getPoints() {
        return points;
    }

    public ObservableInt getRotation() {
        return rotation;
    }

    public ObservableField<CheckState> getValid() {
        return valid;
    }

    public ObservableBoolean getTorch() {
        return torch;
    }

    public View.OnTouchListener getScreenTouchListener() {
        return (v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
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
                CheckState state = CheckState.INVALID;

                Collection<ItemViewModel> items = dataManager.getItems();
                for (ItemViewModel item : items) {
                    if (text.equals(item.getQrCode())) {
                        Timber.d("Found qrCode %s", text);
                        state = item.isChecked() ? CheckState.ALREADY_VALIDATED : CheckState.VALID;
                        item.setChecked(true);
                        item.setCheckedTime(new Date());
                    }
                }

                if (state == CheckState.VALID) {
                    dataManager.writeItems(items);
                }

                switch (state) {
                    case VALID:
                        vibrator.vibrate(250);
                        soundPoolPlayer.playShortResource(R.raw.ok);
                        break;
                    case INVALID:
                        vibrator.vibrate(PATTERN_FAIL, -1);
                        soundPoolPlayer.playShortResource(R.raw.fail);
                        break;
                    case ALREADY_VALIDATED:
                        vibrator.vibrate(PATTERN_ALREADY_VALIDATED, -1);
                        soundPoolPlayer.playShortResource(R.raw.fail);

                }
                valid.set(state);
                handler.postDelayed(clearValid, 1000);
            }
            this.lastText = text;
            handler.removeCallbacks(clearPoints);
            handler.postDelayed(clearPoints, 500);
        };
    }

    public void onCreate() {
        //audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
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
