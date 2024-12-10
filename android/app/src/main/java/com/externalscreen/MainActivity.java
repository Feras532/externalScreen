package com.externalscreen;

import android.app.Presentation;
import android.content.Context;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;

import com.facebook.react.ReactActivity;
import com.facebook.react.ReactInstanceManager;

public class MainActivity extends ReactActivity {

    private static final String TAG = "CFS";
    private Presentation customerPresentation;

    @Override
    protected String getMainComponentName() {
        return "externalScreen"; // Main React Native component name
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);

        // Register display listener for detecting external displays
        setupDisplayListener();
        // Attempt to initialize the external display
        showCustomerFacingScreen();
    }

    private void setupDisplayListener() {
        DisplayManager displayManager = (DisplayManager) getSystemService(Context.DISPLAY_SERVICE);
        displayManager.registerDisplayListener(new DisplayManager.DisplayListener() {
            @Override
            public void onDisplayAdded(int displayId) {
                Log.d(TAG, "External display added: ID " + displayId);
                showCustomerFacingScreen();
            }

            @Override
            public void onDisplayChanged(int displayId) {
                Log.d(TAG, "Display changed: ID " + displayId);
            }

            @Override
            public void onDisplayRemoved(int displayId) {
                Log.d(TAG, "External display removed: ID " + displayId);
                if (customerPresentation != null) {
                    customerPresentation.dismiss();
                    customerPresentation = null;
                }
            }
        }, null);
    }

    private void showCustomerFacingScreen() {
        DisplayManager displayManager = (DisplayManager) getSystemService(Context.DISPLAY_SERVICE);
        Display[] displays = displayManager.getDisplays();

        if (displays.length > 1) { // Check if an external display is available
            Display externalDisplay = displays[1]; // Use the second display (typically index 1)

            if (customerPresentation == null) {
                ReactInstanceManager reactInstanceManager = getReactNativeHost().getReactInstanceManager();
                customerPresentation = new CustomerDisplayPresentation(this, externalDisplay, reactInstanceManager);
                customerPresentation.show();
                Log.d(TAG, "Customer-facing screen is now displayed on external display.");
            }
        } else {
            Log.d(TAG, "No external display detected.");
        }
    }
}
