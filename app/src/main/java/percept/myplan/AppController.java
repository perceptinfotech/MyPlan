package percept.myplan;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.splunk.mint.Mint;

import io.tpa.tpalib.TPA;
import io.tpa.tpalib.TpaConfiguration;
import io.tpa.tpalib.ext.CrashHandling;
import io.tpa.tpalib.ext.TpaLog;
import percept.myplan.Global.LruBitmapCache;


/**
 * Created by percept on 25/7/16.
 */

public class AppController extends Application {

    public static final String TAG = AppController.class
            .getSimpleName();
    private static AppController mInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        Mint.initAndStartSession(this, "390c5614");

        TpaConfiguration config =
                new TpaConfiguration.Builder("d3baf5af-0002-4e72-82bd-9ed0c66af31c", "https://weiswise.tpa.io/")
                        .setLogType(TpaLog.Type.BOTH)           // Default
                        .setCrashHandling(CrashHandling.SILENT) // Default
                        .enableAnalytics(true)                 // Default
                        .useShakeFeedback(false, null)          // Default
                        .updateInterval(60)                     // Default
                        .useApi14(true)                         // Default
                        .build();

        TPA.initialize(this, config);
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    @Override
    public void onTerminate() {
        Mint.closeSession(this);
        super.onTerminate();

    }
}