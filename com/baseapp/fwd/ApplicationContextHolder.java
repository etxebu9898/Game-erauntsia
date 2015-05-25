// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.baseapp.fwd;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

public class ApplicationContextHolder extends Application
{

    public ApplicationContextHolder()
    {
        instance = this;
    }

    public static ApplicationContextHolder getAppInstance()
    {
        return instance;
    }

    public String getisPageLoaded()
    {
        savePageHolder = getSharedPreferences("isLoaded", 0);
        if(savePageHolder != null)
        {
            return savePageHolder.getString("isloaded", "");
        } else
        {
            Log.i("AppClass", "getTotalCOST null");
            return "";
        }
    }

    public void saveIsPageLoaded(String s)
    {
        savePageHolder = getSharedPreferences("isLoaded", 0);
        android.content.SharedPreferences.Editor editor = savePageHolder.edit();
        editor.putString("isloaded", s);
        editor.commit();
    }

    public static ApplicationContextHolder instance;
    public SharedPreferences savePageHolder;
}
