package io.callstack.react.fbads;

import android.app.Activity;
import android.util.Log;
import android.view.ViewGroup;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSize;
import com.facebook.ads.InstreamVideoAdListener;
import com.facebook.ads.InstreamVideoAdView;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

public class InStreamVideoViewManager extends ReactContextBaseJavaModule implements LifecycleEventListener {
    private String TAG = "InStreamVideoViewManager";
    private Promise mLoadedPromise = null;
    private Promise mShowPromise = null;

    private ViewGroup adContainer;
    private InstreamVideoAdView adView;
    ReactApplicationContext mReactContext;
    private Activity mActivity;
    private Position position;

    public InStreamVideoViewManager(ReactApplicationContext reactContext, Activity activity) {
        super(reactContext);
        mReactContext = reactContext;
        mReactContext.addLifecycleEventListener(this);
        mActivity = activity;
        position = new Position();
    }

    @ReactMethod
    public void setPosition(int px, int py, int dx, int dy, Promise p) {
        if (mShowPromise != null) return;
        position.setDx(dx);
        position.setDy(dy);
        position.setPx(px);
        position.setPy(py);
        mShowPromise = p;
    }

    @ReactMethod
    public void showAd(Promise p) {
        if (mShowPromise != null) {
            p.reject("E_FAILED_TO_SHOW", "Only one `showAd` can be called at once");
            return;
        }
        if (adView != null && adView.isAdLoaded()) {
            adContainer = (ViewGroup) mActivity.findViewById(android.R.id.content);
            adContainer.post(new Runnable() {
                @Override
                public void run() {
                    adContainer.removeView(adView);
                    adContainer.addView(adView);
                    adView.show();
                }
            });
            mShowPromise = p;
        }

    }

    private int pxToDP(int px) {
        return (int) (px / mReactContext.getResources().getDisplayMetrics().density);
    }

    @ReactMethod
    public void loadAd(String placementId, int px, int py, Promise p) {
        if (mLoadedPromise != null) {
            p.reject("E_FAILED_TO_SHOW", "Only one `loadAd` can be called at once");
            return;
        }

        ReactApplicationContext reactContext = this.getReactApplicationContext();

        if (adView != null)
            adView.destroy();
        adView = new InstreamVideoAdView(
                reactContext,
                placementId,
                new AdSize(pxToDP(px), pxToDP(py))
        );

        mLoadedPromise = p;
        adView.setAdListener(new InstreamVideoAdListener() {
            @Override
            public void onAdVideoComplete(Ad ad) {
                if (adContainer != null) {
                    adContainer.post(new Runnable() {
                        @Override
                        public void run() {
                            adContainer.removeView(adView);
                        }
                    });
                }
            }

            @Override
            public void onError(Ad ad, AdError error) {
//                ad.getPlacementId();
                Log.e(TAG, "InstreamVideoAdView video ad failed to load: " + error.getErrorMessage());
                mLoadedPromise.reject("E_FAILED_TO_SHOW",
                        "InstreamVideoAdView ad failed to load: " + error.getErrorMessage());
                cleanUp();
            }

            @Override
            public void onAdLoaded(Ad ad) {
                Log.d(TAG, "InstreamVideoAdView ad is loaded and ready to be displayed!");
                mLoadedPromise.resolve(true);
            }

            @Override
            public void onAdClicked(Ad ad) {
                Log.d(TAG, "InstreamVideoAdView ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                Log.d(TAG, "InstreamVideoAdView ad impression logged!");
            }
        });
        adView.loadAd();
    }

    private void cleanUp() {
        mLoadedPromise = null;
        mShowPromise = null;
    }

    @Override
    public String getName() {
        return "CKTInStreamViewManager";
    }

    @Override
    public void onHostResume() {

    }

    @Override
    public void onHostPause() {

    }

    @Override
    public void onHostDestroy() {

    }
}
