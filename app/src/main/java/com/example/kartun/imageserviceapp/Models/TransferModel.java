package com.example.kartun.imageserviceapp.Models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import com.example.kartun.imageserviceapp.Communication.ITcpClient;
import com.example.kartun.imageserviceapp.Communication.TcpClient;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;

/**
 * Transfer model class that responsible to send all pictures to the server.
 */
public class TransferModel implements ITransferModel {

    private INotificationManagerModel notificationManagerModel;
    private ITcpClient tcpClient;

    public TransferModel (INotificationManagerModel notificationManagerModel) {

        this.notificationManagerModel = notificationManagerModel;
        this.tcpClient = new TcpClient();
    }

    @Override
    public void startTransfer() {
        // Run the task in thread.
        new Thread(new Runnable() {

            @Override
            public void run() {
                // Check connection
                if (tcpClient.connect()) {
                    File[] allAlbums = getAllFiles();
                    if (allAlbums != null) {
                        // Get max number of all pictures to be transferred.
                        int progressMax = getNumberOfImagesToTransfer(allAlbums);
                        int progressCurrent = 0;
                        notificationManagerModel.notifyManager(progressMax, progressCurrent);
                        // Iterate over all albums.
                        for (File album : allAlbums) {
                            File[] pics = getAllPics(album);
                            if (pics != null) {
                                // Iterate over all pictures.
                                for (File pic : pics) {
                                    try {
                                        FileInputStream fis = new FileInputStream(pic);
                                        Bitmap bm = BitmapFactory.decodeStream(fis);
                                        byte[] imgBytes = getBytesFromBitmap(bm);
                                        byte[] picNameBytes = pic.getName().getBytes();

                                        // send length of picture name bytes and name bytes.
                                        tcpClient.write(picNameBytes.length, picNameBytes);
                                        // send length of image bytes and image bytes.
                                        tcpClient.write(imgBytes.length, imgBytes);

                                        notificationManagerModel.notifyManager(progressMax, ++progressCurrent);
                                    } catch (Exception e) {
                                        Log.e("TCP", "C: Error", e);
                                    }
                                }
                            }
                        }
                        notificationManagerModel.notifyManager(0, 0);
                    }
                    tcpClient.disconnect();
                }
            }
        }).start();
    }

    /**
     * Get the number of images to be transferred.
     * @param allAlbums albums to check.
     * @return number of pictures.
     */
    private static int getNumberOfImagesToTransfer(File[] allAlbums) {
        int numberImages = 0;
        for (File album: allAlbums) {
            File[] pics = getAllPics(album);
            if (pics != null) {
                numberImages += pics.length;
            }
        }
        return numberImages;
    }

    /**
     * Get array of all pictures in album by filtering all files except images.
     * @param album album to scan.
     * @return array of pics.
     */
    private static File[] getAllPics(File album) {
        FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return (name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".png")||
                        name.toLowerCase().endsWith(".bmp") || name.toLowerCase().endsWith(".gif"));
            }
        };
        return album.listFiles(filter);
    }

    /**
     * Get bytes array from image.
     * @param bm image to parse to bytes.
     * @return array of bytes.
     */
    private static byte[] getBytesFromBitmap(Bitmap bm) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 70, stream);
        return stream.toByteArray();

    }

    /**
     * Get array of all directories in "Dcim" directory.
     * @return array of all directories.
     */
    private static File[] getAllFiles() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).listFiles();
    }

}
