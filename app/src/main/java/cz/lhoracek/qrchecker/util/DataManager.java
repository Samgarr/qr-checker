package cz.lhoracek.qrchecker.util;


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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import cz.lhoracek.qrchecker.screens.list.ItemViewModel;
import timber.log.Timber;

public class DataManager {

    private static final String ITEM_NAME = "name";
    private static final String ITEM_QRCODE = "qrcode";
    private static final String ITEM_CHECKED = "checked";
    private static final String ITEM_TIME = "time";

    private final Preferences preferences;

    @Inject
    public DataManager(Preferences preferences) {
        this.preferences = preferences;
    }


    public Collection<ItemViewModel> getItems() {
        if (preferences.getFilename() == null) {
            return null;
        } else if (!new File(preferences.getFilename()).exists()) {
            preferences.setFilename(null);
            return null;
        }

        List<ItemViewModel> items = new ArrayList<>();
        File csvData = new File(preferences.getFilename());
        CSVParser parser = null;
        try {
            parser = CSVParser.parse(csvData, Charset.forName("UTF-8"), CSVFormat.EXCEL.withFirstRecordAsHeader());
        } catch (IOException e) {
            Timber.e(e);
            return null;
        }
        for (CSVRecord csvRecord : parser) {
            Date date = null;
            try {
                date = csvRecord.get(ITEM_TIME) == null || csvRecord.get(ITEM_TIME).isEmpty() ? null : SimpleDateFormat.getDateTimeInstance().parse(csvRecord.get(ITEM_TIME));
            } catch (ParseException e) {
                Timber.e(e);
            }
            String name = csvRecord.isSet(ITEM_NAME) ? csvRecord.get(ITEM_NAME) : null;
            String qrcode = csvRecord.isSet(ITEM_QRCODE) ? csvRecord.get(ITEM_QRCODE) : null;
            Boolean checked = csvRecord.isSet(ITEM_CHECKED) ? Boolean.valueOf(csvRecord.get(ITEM_CHECKED)) : null;

            Timber.d("Parsed item %s, %s, %b, %s", name, qrcode, checked, date);
            items.add(new ItemViewModel(name, qrcode, checked, date));
        }

        return items;
    }

    public void writeItems(Collection<ItemViewModel> items) {
        try (FileWriter fileWriter = new FileWriter(preferences.getFilename());
             CSVPrinter csvPrinter = CSVFormat.EXCEL.withFirstRecordAsHeader().print(fileWriter)) {

            Object[] FILE_HEADER = {ITEM_NAME, ITEM_QRCODE, ITEM_CHECKED, ITEM_TIME};
            csvPrinter.printRecord(FILE_HEADER);

            for (ItemViewModel item : items) {
                csvPrinter.printRecord(item.getName(), item.getQrCode(), item.isChecked(), DateUtil.formatDate(item.getCheckedTime()));
            }
        } catch (IOException e) {
            Timber.e(e);
        }
    }
}
