package cz.lhoracek.qrchecker.screens.list;


import android.app.Activity;
import android.content.Context;

import com.nbsp.materialfilepicker.MaterialFilePicker;

import java.util.regex.Pattern;

import javax.inject.Inject;

import cz.lhoracek.qrchecker.di.ActivityContext;
import cz.lhoracek.qrchecker.screens.BaseViewModel;
import cz.lhoracek.qrchecker.util.Preferences;
import cz.lhoracek.qrchecker.util.adapter.handler.ItemBinder;

public class ListViewModel extends BaseViewModel {
    private final Context activityContext;
    private final Preferences preferences;

    @Inject
    public ListViewModel(@ActivityContext Context activityContext,
                         Preferences preferences) {
        this.activityContext = activityContext;
        this.preferences = preferences;
    }

    @Override
    public void onCreate(){
        if(preferences.getFilename() == null){
            startFilePicker();
        }
    }

    public void onOptionsItemSelected(){
        startFilePicker();
    }

    public void onFileSelected(String path) {
        // TODO
    }

    private void startFilePicker() {
        new MaterialFilePicker()
                .withActivity((Activity) activityContext)
                .withRequestCode(ListActivity.REQUEST_CODE)
                .withFilter(Pattern.compile(".*\\.csv$")) // Filtering files and directories by file name using regexp
                //.withFilterDirectories(true) // Set directories filterable (false by default)
                //.withHiddenFiles(true) // Show hidden files and folders
                .start();
    }

    public ItemBinder<ItemViewModel> getItemBinder() {
        return null; // TODO
    }

    // TODO list
    // TODO click handler?
}
