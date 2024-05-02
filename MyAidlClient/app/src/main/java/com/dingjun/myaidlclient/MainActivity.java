package com.dingjun.myaidlclient;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.dingjun.myaidlserver.IMyAidlColorInterface;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MyAIDLClientMainActivity";

    IMyAidlColorInterface iAidlColorService;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iAidlColorService = IMyAidlColorInterface.Stub.asInterface(service);
            Log.d(TAG, "ServiceConnection: Remote Color Service Connected!!!");

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected: ");
            iAidlColorService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = new Intent("com.dingjun.myaidlserver.MyAIDLColorService");
        intent.setPackage("com.dingjun.myaidlserver");

        bindService(intent, mConnection, BIND_AUTO_CREATE);
        Log.d(TAG, "onCreate: bindservice called...");

        Button btnColor = findViewById(R.id.btn_color);
        btnColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int color = iAidlColorService.getColor();
                    v.setBackgroundColor(color);
                } catch(RemoteException re) {
                    re.printStackTrace();
                }
            }
        });
    }
}