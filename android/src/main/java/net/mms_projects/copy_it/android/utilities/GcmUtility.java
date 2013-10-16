package net.mms_projects.copy_it.android.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import net.mms_projects.copy_it.R;

import java.io.IOException;

public class GcmUtility {

    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";

    private static final String TAG = "gcm-utility";

    private static GoogleCloudMessaging gcm;

    /**
     * Gets the current registration ID for application on GCM service.
     * <p>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     *         registration ID.
     */
    public static String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.length() == 0) {
            Log.i(TAG, "Registration not found.");
            return null;
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = AppUtility.getVersionCode(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return null;
        }
        return registrationId;
    }

    /**
     * Stores the registration ID and app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param regId registration ID
     */
    public static void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = AppUtility.getVersionCode(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

    public static boolean needsRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);

        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.length() == 0) {
            return false;
        }

        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = AppUtility.getVersionCode(context);
        if (registeredVersion != currentVersion) {
            return false;
        }

        return true;
    }

    public static void clear(Context context) {
        SharedPreferences preferences = getGCMPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
    private static SharedPreferences getGCMPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        return context.getSharedPreferences("gcm",
                Context.MODE_PRIVATE);
    }

    /**
     * Registers the application with GCM servers asynchronously.
     * <p>
     * Stores the registration ID and app versionCode in the application's
     * shared preferences.
     */
    public static String register(Context context) throws GmcRegistrationException {
        String registrationId = null;

        try {
            if (gcm == null) {
                gcm = GoogleCloudMessaging.getInstance(context);
            }
            
            registrationId = gcm.register(context.getResources().getString(R.string.gcm_sender_id));
        } catch (IOException ioException) {
            throw new GmcRegistrationException(ioException);

            // If there is an error, don't just keep trying to register.
            // Require the user to click a button again, or perform
            // exponential back-off.
        }

        return registrationId;
    }

    public static class GmcRegistrationException extends Exception {

        public GmcRegistrationException(Throwable throwable) {
            super(throwable);
        }

    }

}
