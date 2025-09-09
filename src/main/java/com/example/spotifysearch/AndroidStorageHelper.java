package com.example.spotifysearch;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import java.io.File;

public class AndroidStorageHelper {
    private Context context;
    private SharedPreferences preferences;
    private static final String PREF_NAME = "DatabasePreferences";
    private static final String KEY_STORAGE_LOCATION = "database_storage_location";

    public AndroidStorageHelper(Context context) {
        this.context = context;
        this.preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public String getDatabasePath(String dbName) {
        boolean useExternalStorage = preferences.getBoolean(KEY_STORAGE_LOCATION, false);

        if (useExternalStorage && isExternalStorageWritable()) {
            // Get external files directory (Android/data/your.package.name/files)
            File externalDir = context.getExternalFilesDir(null);
            return new File(externalDir, dbName).getAbsolutePath();
        } else {
            // Use internal storage (data/data/your.package.name/databases)
            File internalDir = new File(context.getDataDir(), "databases");
            if (!internalDir.exists()) {
                internalDir.mkdirs();
            }
            return new File(internalDir, dbName).getAbsolutePath();
        }
    }

    public void setStoragePreference(boolean useExternalStorage) {
        preferences.edit()
                .putBoolean(KEY_STORAGE_LOCATION, useExternalStorage)
                .apply();
    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }
}