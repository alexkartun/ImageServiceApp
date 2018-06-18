package com.example.kartun.imageserviceapp.Models;

public interface INotificationManagerModel {

    /**
     * Notify manager with max value and current value.
     * @param max max value of transfer.
     * @param current current value of transfer.
     */
    void notifyManager(int max, int current);

}
