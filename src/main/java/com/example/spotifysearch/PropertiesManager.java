package com.example.spotifysearch;

import android.content.Context;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class PropertiesManager {
    private static final String PROPERTIES_FILE_NAME = "app.properties";
    private final Context context;
    private Properties properties;
    private File propertiesFile;

    public PropertiesManager(Context context) {
        this.context = context;
        this.properties = new Properties();
        initializePropertiesFile();
    }

    private void initializePropertiesFile() {
        // Use app's internal storage directory which is accessible to both Java and Python
        File internalDir = context.getFilesDir();
        propertiesFile = new File(internalDir, PROPERTIES_FILE_NAME);

        if (!propertiesFile.exists()) {
            try {
                // Create initial properties
                properties.setProperty("example_key", "example_value");
                saveProperties();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        loadProperties();
    }

    public void saveProperties() {
        try (FileOutputStream out = new FileOutputStream(propertiesFile)) {
            properties.store(out, "App Properties");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadProperties() {
        try (FileInputStream in = new FileInputStream(propertiesFile)) {
            properties.load(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
        saveProperties();
    }

    public String getPropertiesFilePath() {
        return propertiesFile.getAbsolutePath();
    }
}