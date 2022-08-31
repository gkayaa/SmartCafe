package com.example.smartcafe;

import android.content.ComponentCallbacks2;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public interface MyFirebaseInstanceIDService extends ComponentCallbacks2 {
    @Nullable
    IBinder onBind(Intent intent);

    void onTokenRefresh();
}
