package cz.lhoracek.qrchecker.screens.list;


import android.app.Activity;
import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.os.Environment;

import com.android.databinding.library.baseAdapters.BR;
import com.nbsp.materialfilepicker.MaterialFilePicker;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import javax.inject.Inject;

import cz.lhoracek.qrchecker.R;
import cz.lhoracek.qrchecker.di.ActivityContext;
import cz.lhoracek.qrchecker.util.DateUtil;
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

    private ObservableList<ItemViewModel> items = new ObservableArrayList<>();

    @Inject
    public ListViewModel(@ActivityContext Context activityContext,
                         Preferences preferences,
                         PermissionUtils permissionUtils) {
        this.activityContext = activityContext;
        this.preferences = preferences;
        this.permissionUtils = permissionUtils;
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

        try {
            File csvData = new File(preferences.getFilename());
            CSVParser parser = CSVParser.parse(csvData, Charset.forName("UTF-8"), CSVFormat.EXCEL.withFirstRecordAsHeader());
            for (CSVRecord csvRecord : parser) {
                Date date = csvRecord.get("time") == null || csvRecord.get("time").isEmpty() ? null : SimpleDateFormat.getDateTimeInstance().parse(csvRecord.get("time"));
                items.add(new ItemViewModel(csvRecord.get("name"),
                        csvRecord.get("qrcode"),
                        Boolean.valueOf(csvRecord.get("checked")),
                        date)
                );
            }
        } catch (IOException | ParseException e) {
            Timber.e(e);
        }
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
            // TODO nothing
        };
    }

    public LongClickHandler<ItemViewModel> getItemLongClickHandler() {
        return viewModel -> {
            try (FileWriter fileWriter = new FileWriter(preferences.getFilename());
                 CSVPrinter csvPrinter = CSVFormat.EXCEL.withFirstRecordAsHeader().print(fileWriter)) {
                viewModel.setChecked(!viewModel.isChecked());
                viewModel.setCheckedTime(viewModel.isChecked() ? new Date() : null);


                Object[] FILE_HEADER = {"name", "qrcode", "checked", "time"};
                csvPrinter.printRecord(FILE_HEADER);

                for (ItemViewModel item : items) {
                    csvPrinter.printRecord(item.getName(), item.getQrCode(), item.isChecked(), DateUtil.formatDate(item.getCheckedTime()));
                }

                // TODO toggle
            } catch (IOException e) {
                Timber.e(e);
            }
        };
    }
}
