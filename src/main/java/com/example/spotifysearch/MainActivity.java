package com.example.spotifysearch;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.chaquo.python.Python;
import com.chaquo.python.PyObject;

import com.chaquo.python.android.AndroidPlatform;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import java.net.URI;
public class MainActivity extends AppCompatActivity {
    private ExecutorService executor;
    private WebView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        if (! Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }

        DatabaseImport.copyDatabaseIfNeeded(getApplicationContext());

        // Create an executor for background tasks
        executor = Executors.newSingleThreadExecutor();
        startFlaskServer();
        // Run Flask server in background
        //Python py = Python.getInstance();
        //PyObject flaskModule = py.getModule("app");
        //flaskModule.callAttr("main");
        view = findViewById(R.id.spotify);
        view.getSettings().setJavaScriptEnabled(true);
        view.getSettings().setDomStorageEnabled(true);
        view.setWebViewClient(new WebViewClient());
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        view.loadUrl("http://localhost:5000/");
        //view.loadUrl("https://www.amazon.de");
    }

    private void startFlaskServer() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Python py = Python.getInstance();
                PyObject flaskModule = py.getModule("app");
                flaskModule.callAttr("main");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Shutdown the executor when the activity is destroyed
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
        }
    }
}