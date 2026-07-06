package sh.margot.pixelcamerahelper;

import android.content.Context;
import android.content.SharedPreferences;

final class Prefs {
    private static final String KEY = "target_package";

    private Prefs() {}

    static SharedPreferences get(Context c) {
        return c.getSharedPreferences("pch", Context.MODE_PRIVATE);
    }

    static String targetPackage(Context c) {
        return get(c).getString(KEY, null);
    }

    static void setTargetPackage(Context c, String pkg) {
        get(c).edit().putString(KEY, pkg).apply();
    }
}
