package cz.lhoracek.qrchecker.screens.list;


import android.app.Activity;
import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.os.Environment;

import com.android.databinding.library.baseAdapters.BR;
import com.nbsp.materialfilepicker.MaterialFilePicker;

import java.io.File;
import java.util.Date;
import java.util.regex.Pattern;

import javax.inject.Inject;

import cz.lhoracek.qrchecker.R;
import cz.lhoracek.qrchecker.di.ActivityContext;
import cz.lhoracek.qrchecker.util.DataManager;
import cz.lhoracek.qrchecker.util.PermissionUtils;
import cz.lhoracek.qrchecker.util.Preferences;
import cz.lhoracek.qrchecker.util.adapter.binder.ItemBinderBase;
import cz.lhoracek.qrchecker.util.adapter.handler.ClickHandler;
import cz.lhoracek.qrchecker.util.adapter.handler.ItemBinder;
import cz.lhoracek.qrchecker.util.adapter.handler.LongClickHandler;
import timber.log.Timber;

public class ListViewModel {
    private final Context activityContext;
    private final Preferences preferences;
    private final PermissionUtils permissionUtils;
    private final DataManager dataManager;

    private ObservableList<ItemViewModel> items = new ObservableArrayList<>();

    @Inject
    public ListViewModel(@ActivityContext Context activityContext,
                         Preferences preferences,
                         PermissionUtils permissionUtils,
                         DataManager dataManager) {
        this.activityContext = activityContext;
        this.preferences = preferences;
        this.permissionUtils = permissionUtils;
        this.dataManager = dataManager;
    }

    public void onStart() {
        permissionUtils.checkPermissions();
        if (preferences.getFilename() == null) {
            //       startFilePicker();
            return;
        } else if (!new File(preferences.getFilename()).exists()) {
            preferences.setFilename(null);
            //     startFilePicker();
            return;
        }

        items.addAll(dataManager.getItems());
        Timber.d("Added %d items", items.size());
    }

    public void onOptionsItemSelected() {
        startFilePicker();
    }

    public void onFileSelected(String path) {
        // TODO validate file
        preferences.setFilename(path);
    }

    private void startFilePicker() {
        new MaterialFilePicker()
                .withActivity((Activity) activityContext)
                .withRequestCode(ListActivity.REQUEST_CODE)
                .withFilter(Pattern.compile(".*\\.csv$")) // Filtering files and directories by file name using regexp
                //.withFilterDirectories(true) // Set directories filterable (false by default)
                //.withHiddenFiles(true) // Show hidden files and folders
                .withRootPath(Environment.getExternalStorageDirectory().getPath())
                .start();
    }

    public ItemBinder<ItemViewModel> getItemBinder() {
        return new ItemBinderBase<>(BR.viewModel, R.layout.item_list);
    }

    public ObservableList<ItemViewModel> getItems() {
        return items;
    }

    public ClickHandler<ItemViewModel> getItemClickHandler() {
        return (viewModel, viewId) -> {
            // TODO nothing so far
        };
    }

    public LongClickHandler<ItemViewModel> getItemLongClickHandler() {
        return viewModel -> {
            viewModel.setChecked(!viewModel.isChecked());
            viewModel.setCheckedTime(viewModel.isChecked() ? new Date() : null);
            dataManager.writeItems(items);
        };
    }
}
