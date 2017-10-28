package com.albertoruvel.credit.app.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.albertoruvel.credit.app.R;
import com.albertoruvel.credit.app.data.resp.UserConfiguration;
import com.google.gson.Gson;

/**
 * Created by jose.rubalcaba on 10/26/2017.
 */

public class PrefsUtils {

    public static void saveUserToken(Context cxt, String token) {
        SharedPreferences preferences = getPreferences(cxt);
        preferences.edit().putString(cxt.getString(R.string.sharedPreferencesTokenName), token).commit();
    }

    public static String getUserToken(Context cxt) {
        SharedPreferences preferences = getPreferences(cxt);
        return preferences.getString(cxt.getString(R.string.sharedPreferencesTokenName), "");
    }

    public static boolean isFirstTimeUser(Context cxt) {
        SharedPreferences preferences = getPreferences(cxt);
        return preferences.getBoolean(cxt.getString(R.string.sharedPreferencesFirstTimeUser), Boolean.TRUE);
    }

    public static void saveCurrentUserConfiguration(Context cxt, UserConfiguration configurationResult) {
        getPreferences(cxt).edit()
                .putString(cxt.getString(R.string.sharedPreferencesCurrentConfig), new Gson().toJson(configurationResult));
    }

    public static UserConfiguration getCurrentUserConfiguration(Context cxt) {
        SharedPreferences preferences = getPreferences(cxt);
        return new Gson().fromJson(preferences.getString(cxt.getString(R.string.sharedPreferencesCurrentConfig), ""), UserConfiguration.class);
    }

    private static SharedPreferences getPreferences(Context cxt) {
        return cxt.getSharedPreferences(cxt.getString(R.string.sharedPreferencesName), Context.MODE_PRIVATE);
    }
}
