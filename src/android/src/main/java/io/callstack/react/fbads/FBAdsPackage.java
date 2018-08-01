/**
 * FBAdsPackage.java
 * io.callstack.react.fbads;
 * <p>
 * Created by Mike Grabowski on 25/09/16.
 * Copyright © 2016 Callstack.io. All rights reserved.
 */
package io.callstack.react.fbads;

import android.app.Activity;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Main package exporting native modules and views
 */
public class FBAdsPackage implements ReactPackage {
    private Activity mActivity;

    public FBAdsPackage(Activity activity) {
        mActivity = activity;
    }

    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
        return Arrays.<NativeModule>asList(
                new NativeAdManager(reactContext),
                new AdSettingsManager(reactContext),
                new InterstitialAdManager(reactContext),
                new InStreamVideoViewManager(reactContext, mActivity)
        );
    }

    // Deprecated RN 0.47
    public List<Class<? extends JavaScriptModule>> createJSModules() {
        return Collections.emptyList();
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        return Arrays.<ViewManager>asList(
                new NativeAdViewManager(reactContext),
                new BannerViewManager(reactContext)
        );
    }
}
