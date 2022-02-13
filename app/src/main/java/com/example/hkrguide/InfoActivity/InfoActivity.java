package com.example.hkrguide.InfoActivity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.hkrguide.BaseActivity;
import com.example.hkrguide.InfoActivity.fragments.EntryFragment;
import com.example.hkrguide.InfoActivity.fragments.LoadingInfoFragment;
import com.example.hkrguide.InfoActivity.util.ContentXmlManager;
import com.example.hkrguide.InfoActivity.util.LocalFileManager;
import com.example.hkrguide.InfoActivity.util.MenuEntry;
import com.example.hkrguide.R;
import com.example.hkrguide.SettingsActivity.util.DataStore;
import com.example.hkrguide.Utilities;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.io.IOException;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class InfoActivity extends BaseActivity {

    @Override
    public void onResume() {
        // Initialize Super Class
        super.onResume();
        // Set Checked Item in Nav Drawer
        ((NavigationView) findViewById(R.id.nav_view)).setCheckedItem(R.id.nav_item_info);
        super.currentNavItem = R.id.nav_item_info;
        // Set Toolbar Text
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.toobar_title_info);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Initialize Super Class
        super.onCreate(savedInstanceState);
        // Inflate Layout into Parent Frame Layout
        FrameLayout frameLayout = findViewById(R.id.activity_frame);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View activityView = layoutInflater.inflate(R.layout.activity_info, findViewById(R.id.root_info), false);
        frameLayout.addView(activityView);

        /* Activity Code */

        // Initialize Share Button
        ImageButton shareButton = new ImageButton(this);
        shareButton.setImageResource(R.drawable.ic_baseline_share_24);
        shareButton.setOnClickListener(view -> {
            MenuEntry entry;

            // Get Current Fragment
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.infoFragment);
            if(currentFragment instanceof EntryFragment) {
                // If on Entry Fragment
                // Get Menu Entry, Continue
                entry = ((EntryFragment) currentFragment).entry;
            } else {
                // Other Fragments are not supported
                Toast.makeText(view.getContext(), "Page Not Sharable", Toast.LENGTH_SHORT).show();
                return;
            }

            // Initialize Facebook SDK if it is not initialized already
            if(!FacebookSdk.isInitialized()){
                FacebookSdk.setApplicationId(getString(R.string.facebook_api_key));
                FacebookSdk.sdkInitialize(this);
            }

            // Build Share Quote
            StringBuilder builder = new StringBuilder();
            // Title
            if(!entry.TITLE.equals("")){
                builder.append(entry.TITLE);
                builder.append("\n");
            }
            // Body
            if(!entry.BODY.equals("")){
                builder.append(entry.BODY);
                builder.append("\n");
            }
            // Sub Entries
            for(MenuEntry subentry : entry.NEXT_ENTRIES){
                builder.append(subentry.ENTRY_NAME);
                builder.append("\n");
            }
            // Phone Numbers
            for(MenuEntry.PhoneNumber phoneNumber : entry.PHONE_NUMBERS){
                builder.append(phoneNumber.DESCRIPTION);
                builder.append(phoneNumber.NUMBER);
                builder.append("\n");
            }
            // Links are not included here because it violates Facebook Policies
            // Instead a link to HKR Homepage is used.

            // Create Share Link Content
            ShareLinkContent content = new ShareLinkContent.Builder()
                    .setQuote(builder.toString())
                    .setContentUrl(Uri.parse(getString(R.string.url_hkr_homepage)))
                    .build();

            // Show Share Dialog
            new ShareDialog(this).show(content);
        });

        // Add Share Button to Toolbar
        TypedValue tempVal = new TypedValue();
        getTheme().resolveAttribute(android.R.attr.selectableItemBackground, tempVal, true);
        shareButton.setBackgroundResource(tempVal.resourceId);

        Toolbar toolbar = findViewById(R.id.toolbar);
        Toolbar.LayoutParams toolbarLayout = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        toolbarLayout.gravity = Gravity.END;
        toolbarLayout.rightMargin = 48;
        shareButton.setLayoutParams(toolbarLayout);

        toolbar.addView(shareButton);

        // Initialize Update Button
        FloatingActionButton updateButton = findViewById(R.id.button_update);

        // Routine to update the data XML when the button is pressed
        updateButton.setOnClickListener(view -> {
            Utilities.updateXml(this);
        });

        // Update the XML if not manual
        DataStore dataStore = new DataStore(this);
        int updatePeriod = Integer.parseInt(dataStore.getString("update_period", "12"));

        if(updatePeriod > 0){
            // Get Local XML
            ContentXmlManager localXml = LocalFileManager.getLocalXml(this);
            if(localXml != null){
                if(localXml.getLastModifiedTime().plusHours((long) updatePeriod).compareTo(ZonedDateTime.now()) < 0){
                    Utilities.updateXml(this);
                }
            } else {
                Utilities.updateXml(this);
            }
        }
    }
}