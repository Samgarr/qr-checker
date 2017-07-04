package cz.lhoracek.qrchecker.screens.settings;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import javax.inject.Inject;

import cz.lhoracek.qrchecker.R;
import cz.lhoracek.qrchecker.databinding.ActivitySettingsBinding;
import cz.lhoracek.qrchecker.screens.BaseActivity;
import timber.log.Timber;

public class ListActivity extends BaseActivity {
    public static final int REQUEST_CODE = 987651;

    @Inject ListViewModel viewModel;

    ActivitySettingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getActivityComponent().inject(this);
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_list);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_pick_file:
                viewModel.onStartPicker();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String path = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
                viewModel.onFileSelected(path);
            } else {
                Timber.d("Canceled");
            }
        }
    }
}
