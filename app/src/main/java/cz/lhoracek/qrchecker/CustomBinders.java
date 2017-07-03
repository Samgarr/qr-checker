package cz.lhoracek.qrchecker;


import android.databinding.BindingAdapter;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;

public class CustomBinders {

    @BindingAdapter("listener")
    public static void setQrReadListener(QRCodeReaderView view, QRCodeReaderView.OnQRCodeReadListener listener) {
        view.setOnQRCodeReadListener(listener);
    }
}
