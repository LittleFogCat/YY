package top.littlefogcat.yy;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by jjy on 2018/1/17.
 */

public class SpUtils {
    private static SharedPreferences sp;
    private static boolean init = false;

    public static void init(Context context) {
        sp = PreferenceManager.getDefaultSharedPreferences(context);
        init = true;
    }

    public static String getString(String key, String defVal) {
        if (!init) {
            return "";
        }
        return sp.getString(key, defVal);
    }

    public static long getLong(String key, long defVal) {
        if (!init) {
            return 0;
        }
        return sp.getLong(key, defVal);
    }

    public static int getInt(String key, int defVal) {
        if (!init) {
            return 0;
        }
        return sp.getInt(key, defVal);
    }

    public static boolean getBoolean(String key, boolean defVal) {
        if (!init) {
            return false;
        }
        return sp.getBoolean(key, defVal);
    }

    public static void putLong(String key, long val) {
        if (!init) {
            return;
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(key, val);
        try {
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
            editor.commit();
        }
    }
    public static void putBoolean(String key, boolean val) {
        if (!init) {
            return;
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, val);
        try {
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
            editor.commit();
        }
    }

    public static void putString(String key, String val) {
        if (!init) {
            return;
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, val);
        try {
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
            editor.commit();
        }
    }
}
