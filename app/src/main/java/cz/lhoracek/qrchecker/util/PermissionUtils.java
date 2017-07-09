package cz.lhoracek.qrchecker.util;


import android.Manifest;
import android.app.Activity;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.listener.multi.DialogOnAnyDeniedMultiplePermissionsListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import javax.inject.Inject;

import cz.lhoracek.qrchecker.R;

public class PermissionUtils {

    private final Activity activity;

    @Inject
    public PermissionUtils(Activity activity) {
        this.activity = activity;
    }

    public void checkPermissions() {
        MultiplePermissionsListener dialogMultiplePermissionsListener =
                DialogOnAnyDeniedMultiplePermissionsListener.Builder
                        .withContext(activity)
                        .withTitle(R.string.permission_title)
                        .withMessage(R.string.permission_text)
                        .withButtonText(android.R.string.ok)
                        .withIcon(R.mipmap.ic_launcher)
                        .build();

        Dexter.withActivity(activity)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(dialogMultiplePermissionsListener).check();
    }
}
