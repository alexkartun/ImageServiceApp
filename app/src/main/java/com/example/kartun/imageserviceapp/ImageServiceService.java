package com.example.kartun.imageserviceapp;

import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class ImageServiceService extends Service {

    private BroadcastReceiver wifiReciever;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flag, int startId) {
        Toast.makeText(this,"Service starting...", Toast.LENGTH_SHORT).show();

        final IntentFilter theFilter = new IntentFilter();
        theFilter.addAction("android.new.wifi.supplicant.CONNECTION_CHANGE");
        theFilter.addAction("android.net.wifi.STATE_CHANGE");

        this.wifiReciever = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                WifiManager wifiManager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
                NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                if (networkInfo != null) {
                    if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                        if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                            startTransfer();
                        }
                    }
                }
            }
        };

        this.registerReceiver(this.wifiReciever, theFilter);
        return START_STICKY;
    }

    public void startTransfer() {

        new Thread(new Runnable() {

            @Override
            public void run() {
                File[] pics = GetAllPics();
                if (pics != null) {
                    try {
                        InetAddress serverAddr = InetAddress.getByName("10.0.2.2");
                        Socket socket = new Socket(serverAddr, 8000);
                        OutputStream output = socket.getOutputStream();

                        for (File pic : pics) {
                            try {
                                FileInputStream fis = new FileInputStream(pic);
                                Bitmap bm = BitmapFactory.decodeStream(fis);
                                byte[] imgBytes = getBytesFromBitmap(bm);

                                output.write(imgBytes.length);
                                output.write(imgBytes);

                                output.flush();

                            } catch (Exception e) {
                                Log.e("TCP", "C: Error", e);
                            }
                        }
                    } catch (Exception e) {
                        Log.e("TCP", "C: Error", e);
                    }
                }
            }
        }).start();


    }


    public byte[] getBytesFromBitmap(Bitmap bm) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 70, stream);
        return stream.toByteArray();

    }

    public File[] GetAllPics() {

        File dcim = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Camera");
        if (dcim == null) return null;
        return dcim.listFiles();
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this,"Service ending...", Toast.LENGTH_SHORT).show();
        this.unregisterReceiver(wifiReciever);
    }
}
