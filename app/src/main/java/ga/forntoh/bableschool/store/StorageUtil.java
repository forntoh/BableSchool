package ga.forntoh.bableschool.store;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;

public class StorageUtil {

    private SharedPreferences preferences;

    private StorageUtil(Context context) {
        this.preferences = context.getSharedPreferences(context.getPackageName() + ".DB", Context.MODE_PRIVATE);
    }

    public static StorageUtil with(Context context) {
        return new StorageUtil(context);
    }

    private static String encode(String thing) {
        return Base64.encodeToString(thing.getBytes(), Base64.DEFAULT);
    }

    public void saveMatriculation(String mat) {
        save("mat", mat);
    }

    public String loadMatriculation() {
        return load("mat");
    }

    public void savePassword(String password) {
        save("pass", password);
    }

    public String loadPassword() {
        return load("pass");
    }

    private void save(String key, String value) {
        if (TextUtils.isEmpty(value)) return;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, encode(value));
        editor.apply();
    }

    private String load(String key) {
        String val = preferences.getString(key, "");
        if (TextUtils.isEmpty(val)) return null;
        return decode(val);
    }

    private void clear(String key) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(key);
        editor.apply();
    }

    private String decode(String thing) {
        return new String(Base64.decode(thing, Base64.DEFAULT));
    }
}
