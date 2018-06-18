package com.example.kartun.imageserviceapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;
import com.example.kartun.imageserviceapp.Models.INotificationManagerModel;
import com.example.kartun.imageserviceapp.Models.ITransferModel;
import com.example.kartun.imageserviceapp.Models.NotificationManagerModel;
import com.example.kartun.imageserviceapp.Models.TransferModel;
import com.example.kartun.imageserviceapp.Recievers.WifiReciever;

/**
 * Image service class.
 */
public class ImageService extends Service {

    private WifiReciever wifiReciever;

    public ImageService() {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        /*
        Creating channel and models to work with.
         */
        createNotificationChannel();
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, getString(R.string.channel_id));
        INotificationManagerModel managerModel = new NotificationManagerModel(notificationManagerCompat, builder);
        ITransferModel iTransferModel = new TransferModel(managerModel);
        this.wifiReciever = new WifiReciever(iTransferModel);
    }


    /**
     * Create notification channel.
     */
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String CHANNEL_ID = getString(R.string.channel_id);
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flag, int startId) {
        Toast.makeText(this,"Service starting...", Toast.LENGTH_SHORT).show();
        this.registerReceiver(this.wifiReciever.getWifiReciever(), this.wifiReciever.getIntentFilter());
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this,"Service ending...", Toast.LENGTH_SHORT).show();
        this.unregisterReceiver(this.wifiReciever.getWifiReciever());
    }
}
