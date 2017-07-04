package cz.lhoracek.qrchecker.screens.list;


import android.databinding.BaseObservable;
import android.databinding.Bindable;

import cz.lhoracek.qrchecker.BR;

public class ItemViewModel extends BaseObservable {

    private String name;
    private String qrCode;
    private boolean checked;

    public ItemViewModel(String name, String qrCode, boolean checked) {
        this.name = name;
        this.qrCode = qrCode;
        this.checked = checked;
    }

    public String getName() {
        return name;
    }

    public String getQrCode() {
        return qrCode;
    }

    @Bindable
    public boolean isChecked() {
        return checked;
    }

    private void setChecked(boolean checked){
        this.checked = checked;
        notifyPropertyChanged(BR.checked);
    }
}
