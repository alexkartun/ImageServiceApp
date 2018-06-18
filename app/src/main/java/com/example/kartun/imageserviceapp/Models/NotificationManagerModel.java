package com.example.kartun.imageserviceapp.Models;

import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import com.example.kartun.imageserviceapp.R;

/**
 * Notification manager model class. Responsible for notifying the NotificationManager.
 */
public class NotificationManagerModel implements INotificationManagerModel {

    private NotificationManagerCompat notificationManagerCompat;
    private NotificationCompat.Builder builder;
    private static String startStringContent = "Download in progress";
    private static String midStringContent = "Half way done";
    private static String endStringContent = "Download complete";


    public NotificationManagerModel(NotificationManagerCompat notificationManagerCompat, NotificationCompat.Builder builder) {

        this.notificationManagerCompat = notificationManagerCompat;
        this.builder = builder;

        builder.setSmallIcon(R.drawable.ic_launcher_background)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setContentTitle("Picture Download");

    }

    @Override
    public void notifyManager(int max, int current) {
        setContent(max, current);

        builder.setProgress(max, current, false);
        notificationManagerCompat.notify(1, builder.build());

    }

    /**
     * Set content of builder depends on max and current values.
     * @param max max value of transfer
     * @param current current value of transfer.
     */
    private void setContent(int max, int current) {
        if (current < max / 2)
            builder.setContentText(startStringContent);
        else if (current >= max/2 && current < max)
            builder.setContentText(midStringContent);
        else builder.setContentText(endStringContent);
    }

}
