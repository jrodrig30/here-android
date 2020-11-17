package br.unisc.pdm.cliente.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesHelper {

    private SharedPreferences mPreferences;

    static public final String PREFS_NAME  = "client_preferences";
    static public final String PREF_REG_NUMBER = "pref_reg_number";

    public PreferencesHelper(Context ctx) {
        mPreferences = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public String getRegistrationNumber() {
        return mPreferences.getString(PREF_REG_NUMBER, null);
    }

    public boolean setRegistrationNumber(String number) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(PREF_REG_NUMBER, number);
        return editor.commit();
    }

}
