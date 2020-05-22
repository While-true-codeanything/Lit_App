package com.example.litapp.MainActivity.Utilities;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import java.util.Calendar;

public class DataUtility {
    public static final String File = "Dta";
    public static final String Login = "Login";
    public static final String Password = "Password";
    public static final String Class = "Class";
    public static final String Year = "Year";
    public static int curDay;

    public static String DayName() {
        switch (curDay) {
            case 1: {
                return "Понедельник";
            }
            case 2: {
                return "Вторник";
            }
            case 3: {
                return "Среда";
            }
            case 4: {
                return "Четверг";
            }
            case 5: {
                return "Пятница";
            }
            case 6: {
                return "Суббота";
            }
        }
        return "";
    }

    public static void SetCurDay(Context c) {
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.DAY_OF_WEEK) - 1 != 0 && calendar.get(Calendar.DAY_OF_WEEK) - 1 <= new DataUtility(c).MaxDays())
            curDay = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        else curDay = 1;
    }

    public static void SetDay(int d) {
        curDay = d;
    }

    public static int GetDay() {
        return curDay;
    }

    SharedPreferences dtaccessor;

    public DataUtility(Context ct) {
        try {
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
            dtaccessor = ct.getSharedPreferences(File, Context.MODE_PRIVATE);
            dtaccessor = EncryptedSharedPreferences.create(
                    File,
                    masterKeyAlias,
                    ct,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setData(String s, String dataname) {
        SharedPreferences.Editor editor = dtaccessor.edit();
        editor.putString(dataname, s);
        editor.apply();
    }

    public void DeleteData() {
        SharedPreferences.Editor editor = dtaccessor.edit();
        editor.clear().commit();
    }

    public String getData(String dataname) {
        if (dtaccessor.contains(dataname)) {
            return dtaccessor.getString(dataname, "");
        } else return "error";
    }

    public boolean CheckIfFirstStart() {
        if (dtaccessor.contains(DataUtility.Login) && dtaccessor.contains(DataUtility.Password))
            return false;
        else return true;
    }

    public int MaxDays() {
        String s = gr();
        if (s.equals("11") || s.equals("5") || s.equals("6")) return 5;
        else return 6;
    }

    public String gr() {
        String[] s = getData(DataUtility.Class).split("_");
        return s[0];
    }
}
