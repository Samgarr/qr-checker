package cz.lhoracek.qrchecker;


import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.graphics.PointF;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;


public class ViewModel implements QRCodeReaderView.OnQRCodeReadListener, View.OnTouchListener {
    ObservableField<PointF[]> points = new ObservableField<>();
    ObservableInt rotation = new ObservableInt(0);
    ObservableField<Boolean> valid = new ObservableField<>();
    ObservableBoolean torch = new ObservableBoolean(false);

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

    String lastText = null;
    Runnable clearPoints = () -> points.set(null);
    Runnable clearValid = () -> valid.set(null);
    Handler handler = new Handler();

    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        this.points.set(points);
        if (!text.equals(lastText)) {
            // TODO update
            valid.set(true);
            handler.postDelayed(clearValid, 1000);
        }
        this.lastText = text;
        handler.removeCallbacks(clearPoints);
        handler.postDelayed(clearPoints, 500);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                torch.set(true);
                break;
            case MotionEvent.ACTION_UP:
                torch.set(false);
                break;
        }
        return true;
    }
}
