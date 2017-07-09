package cz.lhoracek.qrchecker.screens.list;


import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.util.Date;

import cz.lhoracek.qrchecker.BR;

public class ItemViewModel extends BaseObservable {

    private String name;
    private String qrCode;
    private boolean checked;
    private Date checkedTime;

    public ItemViewModel(String name, String qrCode, boolean checked, Date checkedTime) {
        this.name = name;
        this.qrCode = qrCode;
        this.checked = checked;
        this.checkedTime = checkedTime;
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

    private void setChecked(boolean checked) {
        this.checked = checked;
        notifyPropertyChanged(BR.checked);
    }

    @Bindable
    public Date getCheckedTime() {
        return checkedTime;
    }

    public void setCheckedTime(Date checkedTime) {
        this.checkedTime = checkedTime;
        notifyPropertyChanged(BR.checkedTime);
    }
}
