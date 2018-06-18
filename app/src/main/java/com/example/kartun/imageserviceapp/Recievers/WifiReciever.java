package com.example.kartun.imageserviceapp.Recievers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import com.example.kartun.imageserviceapp.Models.ITransferModel;

/**
 * Wifi reciever class. Responsible for wifi broadcasting.
 */
public class WifiReciever {

    private BroadcastReceiver wifiReciever;
    private IntentFilter intentFilter;
    private ITransferModel transferModel;

    public WifiReciever(ITransferModel transferModel) {

        this.transferModel = transferModel;
        createIntentFilter();
        createWifiReciever();
    }

    /**
     * Return wifi reciever.
     * @return wifiReciever
     */
    public BroadcastReceiver getWifiReciever() {
        return this.wifiReciever;
    }

    /**
     * Return intent filter.
     * @return intentFilter
     */
    public IntentFilter getIntentFilter() {
        return this.intentFilter;
    }

    /**
     * Create intent filter.
     */
    private void createIntentFilter() {

        intentFilter = new IntentFilter();
        intentFilter.addAction("android.new.wifi.supplicant.CONNECTION_CHANGE");
        intentFilter.addAction("android.net.wifi.STATE_CHANGE");

    }

    /**
     * Create wifi reciever.
     */
    private void createWifiReciever() {

        this.wifiReciever = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                if (networkInfo != null) {
                    if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                        if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                            transferModel.startTransfer();
                        }
                    }
                }
            }
        };
    }
}
