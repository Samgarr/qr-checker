package cz.lhoracek.qrchecker;


import android.databinding.BindingAdapter;
import android.graphics.PointF;
import android.view.View;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;

public class CustomBinders {

    @BindingAdapter("listener")
    public static void setQrReadListener(QRCodeReaderView view, QRCodeReaderView.OnQRCodeReadListener listener) {
        view.setOnQRCodeReadListener(listener);
    }

    @BindingAdapter("points")
    public static void sePoint(PointsOverlayView view, PointF[] points) {
        view.setPoints(points);
    }

    @BindingAdapter("torch")
    public static void setTorch(QRCodeReaderView view, boolean torch) {
        view.setTorchEnabled(torch);
    }

    @BindingAdapter("autofocusInterval")
    public static void setAutoFocus(QRCodeReaderView view, int intervalMillis) {
        view.setAutofocusInterval(intervalMillis);
    }

    @BindingAdapter("touch")
    public static void setTouchListener(View view, View.OnTouchListener listener) {
        view.setOnTouchListener(listener);
    }
}
