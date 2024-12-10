package com.externalscreen;

import android.app.Presentation;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import com.facebook.react.ReactRootView;
import com.facebook.react.ReactInstanceManager;

public class CustomerDisplayPresentation extends Presentation {
    private ReactRootView reactRootView;
    private ReactInstanceManager reactInstanceManager;

    public CustomerDisplayPresentation(Context outerContext, Display display, ReactInstanceManager reactInstanceManager) {
        super(outerContext, display);
        this.reactInstanceManager = reactInstanceManager;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize ReactRootView and start the React Native component
        reactRootView = new ReactRootView(getContext());

        // Start the React application on the external display with the component name
        reactRootView.startReactApplication(
                reactInstanceManager,
                "CFS", // Use the actual registered component name
                null
        );

        // Set the ReactRootView as the content view for this presentation
        setContentView(reactRootView);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (reactRootView != null) {
            reactRootView.unmountReactApplication();
            reactRootView = null;
        }
    }
}
