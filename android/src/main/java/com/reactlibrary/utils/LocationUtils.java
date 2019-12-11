package com.reactlibrary.utils;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.NonNull;

public final class LocationUtils {

    private LocationUtils() {
    }

    /**
     * Determine whether Location is turned on. Below Android M, will always return true.
     *
     * @param context to determine with if location is on
     * @return true if location is turned on or the sdk is below Android M.
     */
    public static boolean isLocationOn(@NonNull final Context context) {
        return !isMarshmallowOrLater() || isLocationTurnedOn(context);
    }

    /**
     * @return true if the current sdk is above or equal to Android M
     */
    private static boolean isMarshmallowOrLater() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * Determine if location is turned on.
     *
     * @param context where from you determine if location is turned on
     * @return true if location is turned on
     */
    @TargetApi(Build.VERSION_CODES.M)
    private static boolean isLocationTurnedOn(@NonNull final Context context) {
        final ContentResolver contentResolver = context.getContentResolver();
        final int mode = Settings.Secure.getInt(contentResolver, Settings.Secure.LOCATION_MODE, Settings.Secure.LOCATION_MODE_OFF);

        return (mode != Settings.Secure.LOCATION_MODE_OFF);
    }
}
