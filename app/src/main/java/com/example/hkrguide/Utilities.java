package com.example.hkrguide;

import android.app.Activity;
import android.app.AlarmManager;
import android.content.Context;
import android.content.res.Configuration;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentActivity;

import com.example.hkrguide.InfoActivity.fragments.LoadingInfoFragment;
import com.example.hkrguide.InfoActivity.util.LocalFileManager;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Utilities {

    // Semaphore to prevent multiple execution
    private static Semaphore updateSemaphore = new Semaphore(1);

    public synchronized static void updateXml(AppCompatActivity activity) {
        // Only Execute if the Semaphore can be Acquired
        if (updateSemaphore.tryAcquire()) {
            // Make Toast to inform user
            Toast.makeText(activity, "Updating Info Page", Toast.LENGTH_SHORT).show();

            Log.i("Update XML", "Updating XML From Server");

            // Prepare Executors
            ExecutorService primaryExecutor = Executors.newSingleThreadExecutor();
            ExecutorService auxiliaryExecutor = Executors.newSingleThreadExecutor();

            // Submit task that downloads the XML file to Primary Executor
            primaryExecutor.submit(() -> {
                try {
                    // Download XML File
                    LocalFileManager.downloadFile(new URL(activity.getString(R.string.url_hkr_info_online)), activity.getFilesDir().toPath().resolve(activity.getString(R.string.file_hkr_info_online_cache)));
                    // Goto Loading Fragment
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.infoFragment, new LoadingInfoFragment()).commit();
                } catch (IOException err) {
                    // Failed to Load
                    Log.e("Update XML", err.toString());
                    // Inform User
                    Toast.makeText(activity, "Update Failed", Toast.LENGTH_SHORT).show();
                } finally {
                    // Release Update Semaphore
                    updateSemaphore.release();
                }
            });

            // Submit a watchdog task to Auxiliary Executor
            auxiliaryExecutor.submit(() -> {
                try {
                    // Wait 30 seconds for Update Task to terminate
                    primaryExecutor.shutdown();
                    primaryExecutor.awaitTermination(30, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    Log.e("Update XML Watchdog", "Interrupted when waiting Update XML to finish");
                } finally {
                    // If the task is not terminated, force termination
                    if (!primaryExecutor.isTerminated()) {
                        Log.e("Update XML Watchdog", "Update XML took too long, terminating");
                    }
                    primaryExecutor.shutdownNow();
                }
            });

            auxiliaryExecutor.shutdown();
        }
    }

    // Set Night Mode
    public static void setNightMode(Configuration configuration, Boolean state) {
        // Get Current Mode
        switch (configuration.uiMode & Configuration.UI_MODE_NIGHT_MASK) {
            case Configuration.UI_MODE_NIGHT_NO:
                // Light Mode
                // Set to Dark if state is true
                if (state) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                // Night Mode
                // Set to Light if state is false
                if (!state) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
                break;
        }
    }
}
