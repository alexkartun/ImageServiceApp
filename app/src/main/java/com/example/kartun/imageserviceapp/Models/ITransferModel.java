package com.example.kartun.imageserviceapp.Models;

public interface ITransferModel {

    /**
     * Start transfer all photos from "Dcim" via tcp networking to server.
     */
    void startTransfer();
}
