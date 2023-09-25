package com.example.androidtask.core;

import android.content.Context;

import java.lang.ref.WeakReference;

public class ContextHolder {
    private static WeakReference<Context> sContextRef;

    public static Context getContext() {
        return sContextRef != null ? sContextRef.get() : null;
    }

    public static void setContext(Context context) {
        sContextRef = new WeakReference<>(context.getApplicationContext());
    }
}
