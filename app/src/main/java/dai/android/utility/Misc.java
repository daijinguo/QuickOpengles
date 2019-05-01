package dai.android.utility;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.os.Build;

public final class Misc {

    public static boolean supportGLESv2(Context context) {
        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1 && (Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown") || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator") || Build.MODEL.contains("Android SDK built for x86")))) {
            return true;
        }

        if (null == context) {
            return false;
        }

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo info = am.getDeviceConfigurationInfo();

        return info.reqGlEsVersion >= 0x20000;
    }
}
