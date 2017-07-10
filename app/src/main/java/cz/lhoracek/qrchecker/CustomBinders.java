package cz.lhoracek.qrchecker;


import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.databinding.BindingAdapter;
import android.graphics.PointF;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;

import timber.log.Timber;

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

    @BindingAdapter("layoutTransition")
    public static void bindLayoutTransition(ViewGroup view, LayoutTransition transition) {
        view.setLayoutTransition(transition);
    }

    @BindingAdapter("enableTransition")
    public static void bindEnableTransition(ViewGroup view, int type) {
        view.getLayoutTransition().enableTransitionType(type);
    }

    @BindingAdapter("animateRotationTo")
    public static void bindAnimateRotation(View view, int rotation) {
        float from = (view.getRotation() + 720) % 360;
        float to = (rotation + 720) % 360;

        if (from == to) {
            return;
        }

        if (from == 270 && to == 0) {
            to = 360;
        } else if (from == 0 && to == 270) {
            from = 360;
        }

        Timber.d("Animating rotation from %f to %f", from, to);
        ObjectAnimator imageViewObjectAnimator = ObjectAnimator.ofFloat(view, "rotation", from, to);
        imageViewObjectAnimator.setDuration(500); // miliseconds
        imageViewObjectAnimator.start();
    }
}
