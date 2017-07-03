package cz.lhoracek.qrchecker.screens.settings;


import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import com.nbsp.materialfilepicker.utils.FileUtils;

import java.io.File;
import java.util.List;
import java.util.prefs.Preferences;
import java.util.regex.Pattern;
import java.util.zip.Checksum;

import javax.inject.Inject;
import javax.inject.Named;

import cz.lhoracek.qrchecker.App;
import cz.lhoracek.qrchecker.R;
import cz.lhoracek.qrchecker.databinding.ActivitySettingsBinding;
import timber.log.Timber;

public class SettingActivity extends Activity {
    public static final int REQUEST_CODE = 987651;

    @Inject SettingsViewModel viewModel;

    ActivitySettingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((App) getApplication()).getAppComponent().inject(this);
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
    }

    public void startPicker() {
        new MaterialFilePicker()
                .withActivity(this)
                .withRequestCode(REQUEST_CODE)
                .withFilter(Pattern.compile(".*\\.csv$")) // Filtering files and directories by file name using regexp
                //.withFilterDirectories(true) // Set directories filterable (false by default)
                //.withHiddenFiles(true) // Show hidden files and folders
                .start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String path = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);

            } else {
                Timber.d("Canceled");
            }
        }
    }
}
