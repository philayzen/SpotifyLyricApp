package com.example.spotifysearch;

import android.content.Context;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseImport {

    private static final String DATABASE_NAME = "spotify.db";

    public static void copyDatabaseIfNeeded(Context context) {
        // Path to the database in the internal storage
            File dbFile = context.getDatabasePath(DATABASE_NAME);

        if (dbFile.exists()) {
            return; // Database already copied
        }

        // Make sure the directory exists
        dbFile.getParentFile().mkdirs();

        try (InputStream input = context.getAssets().open(DATABASE_NAME);
             OutputStream output = new FileOutputStream(dbFile)) {

            // Copy the database file
            byte[] buffer = new byte[1024];
            int length;
            while ((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }

            output.flush();

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error copying database: " + e.getMessage());
        }
    }
}
