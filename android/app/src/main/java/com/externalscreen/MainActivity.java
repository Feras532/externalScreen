package com.externalscreen;

import com.facebook.react.ReactActivity;
import com.facebook.react.ReactActivityDelegate;
import com.facebook.react.ReactRootView;
import android.app.Presentation;
import android.content.Context;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import com.facebook.react.bridge.ReactContext;


public class MainActivity extends ReactActivity {

    private static final String TAG = "DisplayCheck";
    private Presentation customerPresentation;
    private ReactContext reactContext = null;


    @Override
    protected String getMainComponentName() {
        return "externalScreen"; // Update with your main component name
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate called.");
        super.onCreate(savedInstanceState);

        // Register display listener for handling external display changes
        DisplayManager displayManager = (DisplayManager) getSystemService(Context.DISPLAY_SERVICE);
        displayManager.registerDisplayListener(new DisplayManager.DisplayListener() {
            @Override
            public void onDisplayAdded(int displayId) {
                Log.d(TAG, "onDisplayAdded: Display ID " + displayId);
                showCustomerFacingScreen();
            }

            @Override
            public void onDisplayChanged(int displayId) {
                Log.d(TAG, "onDisplayChanged: Display ID " + displayId);
            }

            @Override
            public void onDisplayRemoved(int displayId) {
                if (customerPresentation != null) {
                    Log.d(TAG, "onDisplayRemoved: Dismissing customer presentation.");
                    customerPresentation.dismiss();
                    customerPresentation = null;
                }
            }
        }, null);



        // // Register a listener to wait until the ReactInstanceManager is fully initialized
        // getReactNativeHost().getReactInstanceManager().addReactInstanceEventListener(context -> {
        //     Log.d(TAG, "ReactInstanceManager is initialized. Ready to show customer facing screen.");
        //     reactContext = context;
        //     showCustomerFacingScreen(); // Attempt to initialize the external display when ready
        // });

        // // Fallback to attempt display setup
        // showCustomerFacingScreen();
    }

    private void showCustomerFacingScreen() {
        if (!getReactNativeHost().hasInstance()) {
            Log.d(TAG, "ReactInstanceManager is not yet initialized. Waiting...");
            return;
        }

        DisplayManager displayManager = (DisplayManager) getSystemService(Context.DISPLAY_SERVICE);
        Display[] displays = displayManager.getDisplays();

        Log.d(TAG, "Number of displays detected: " + displays.length);

        if (displays.length > 1) {
            Display externalDisplay = displays[1];
            customerPresentation = new CustomerDisplayPresentation(this, externalDisplay, getReactNativeHost().getReactInstanceManager());
            customerPresentation.show();
        } else {
            Log.d(TAG, "No external display detected.");
        }
    }
}
