package com.dingjun.myaidlserver;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.Random;

public class MyAIDLColorService extends Service {

    private static final String TAG = "MyAIDLColorService";
    public MyAIDLColorService() {
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: startServiceForeground.");
        startServiceForeground();

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return binder;
    }
    private final IMyAidlColorInterface.Stub binder = new IMyAidlColorInterface.Stub() {
        @Override
        public int getColor() throws RemoteException {
            Random random = new Random();
            int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
            String colorHex = Integer.toHexString(color);
            Log.d(TAG, "getColor: " + colorHex.toUpperCase());
            return color;
        }
    };

    private static final String CHANNEL_ID_STRING = "com.dingjun.myaidlserver.MyAIDLColorService";
    private static final int CHANNEL_ID = 0x11;

    @SuppressLint("ForegroundServiceType")
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startServiceForeground() {
        NotificationManager notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel;
        channel = new NotificationChannel(CHANNEL_ID_STRING, getString(R.string.app_name),
                NotificationManager.IMPORTANCE_LOW);
        notificationManager.createNotificationChannel(channel);
        Notification notification = new Notification.Builder(getApplicationContext(),
                CHANNEL_ID_STRING).build();
        startForeground(CHANNEL_ID, notification);
    }
}