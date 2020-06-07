package com.reactlibrary.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import java.security.InvalidParameterException;

/**
 * PermissionUtils to help determine if a particular permission is granted.
 */

public final class PermissionUtils {

    private PermissionUtils() {
    }

    /**
     * Determine whether  the location permission has been granted (ACCESS_COARSE_LOCATION or
     * ACCESS_FINE_LOCATION). Below Android M, will always return true.
     *
     * @param context to determine with if the permission is granted
     * @return true if you have any location permission or the sdk is below Android M.
     * @throws InvalidParameterException if {@code context} is null
     */
    public static boolean isLocationPermissionGranted(@NonNull final Context context) throws InvalidParameterException {
        return (!isMarshmallowOrLater()
                || isPermissionGranted(context, Manifest.permission.ACCESS_FINE_LOCATION));
    }

    /**
     * @return true if the current sdk is above or equal to Android M
     */
    private static boolean isMarshmallowOrLater() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * Determine whether the provided context has been granted a particular permission.
     *
     * @param context    where from you determine if a permission is granted
     * @param permission you want to determinie if it is granted
     * @return true if you have the permission, or false if not
     */
    @TargetApi(Build.VERSION_CODES.M)
    private static boolean isPermissionGranted(@NonNull final Context context, @NonNull final String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }
}
