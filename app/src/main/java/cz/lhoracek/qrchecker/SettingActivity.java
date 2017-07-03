package cz.lhoracek.qrchecker;


import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.util.List;
import java.util.prefs.Preferences;
import java.util.regex.Pattern;
import java.util.zip.Checksum;

import javax.inject.Inject;
import javax.inject.Named;

public class SettingActivity extends Activity implements View.OnClickListener {
    public static final int REQUEST_CODE = 987651;

    @Inject Preferences preferences;

    private ObservableField<String> filePath      = new ObservableField<>(null);
    private String                  masterApkPath = null;
    private String                  slaveApkPath  = null;
    private ActivityChooseFileBinding mBinding;

    public FileChooserActivity() {
        App.component().inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_choose_file);
        mBinding.setActivity(this);
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
                try {
                    filePath.set(data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH));
                    File masterApk = FileUtils.extract(filePath.get(), Const.MASTER_APK_FILENAME);
                    File slaveApk = FileUtils.extract(filePath.get(), Const.SLAVE_APK_FILENAME);
                    masterApkPath = masterApk == null ? null : masterApk.getPath();
                    slaveApkPath = slaveApk == null ? null : slaveApk.getPath();
                    mBinding.invalidateAll();
                    mBinding.executePendingBindings();
                    Log.d(getClass().getSimpleName(), "Picked file " + filePath.get());
                } catch (Exception e) {
                    cancel();
                    Common.showToast(this, this.getString(R.string.update_failed));
                }
            } else {
                Log.d(getClass().getSimpleName(), "Canceled");
            }
        }
    }
}
