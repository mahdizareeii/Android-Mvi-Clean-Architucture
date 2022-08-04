package com.mvi.sample;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.theaetetuslabs.android_apkmaker.AndroidApkMaker;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ApkMakerService extends IntentService {

    private final String notificationChannel = "serviceChannelId";

    public static final String TAG = "ApkMakerService";

    public ApkMakerService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final NotificationManager mNotifyManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, notificationChannel);
        mBuilder.setContentTitle("Building Apk")
                .setContentText("Getting started")
                .setSmallIcon(R.drawable.ic_launcher_background);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    notificationChannel,
                    "Default Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationManager.createNotificationChannel(channel);
        }
        int notificationId = (int) (System.currentTimeMillis() & 0xfffffff);
        Notification notification = mBuilder.build();
        notificationManager.notify(notificationId, notification);
        startForeground(AndroidApkMaker.ONGOING_NOTIFICATION_ID, notification);

        File filesDir = getFilesDir();
        try {
            moveAsset(this, "test.zip", filesDir);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        File projectDir = new File(filesDir, "test_project");
        unpackZip(new File(filesDir, "test.zip").getAbsolutePath(), projectDir.getAbsolutePath());

        AndroidApkMaker.AfterInstallDialogAdder adder = (builder, installActivity) -> builder.setMessage(R.string.app_name);

        String projectPackageName = intent.getStringExtra("project_package_name");

        if (projectPackageName == null || projectPackageName.isEmpty())
            throw new IllegalStateException("You must pass project package name into service");

        new AndroidApkMaker(
                this,
                mNotifyManager,
                mBuilder
        ).make(
                "test apk",
                projectPackageName,
                projectDir.getAbsolutePath(),
                true,
                adder
        );
    }

    public static void moveAsset(Context context, String assetName, File destDir) throws IOException {
        File dest = new File(destDir, assetName);
        try {
            InputStream assetIn = context.getAssets().open(assetName);
            dest.createNewFile();
            int length = 0;
            byte[] buffer = new byte[4096];
            FileOutputStream rawOut = new FileOutputStream(dest);
            while ((length = assetIn.read(buffer)) > 0) {
                rawOut.write(buffer, 0, length);
            }
            rawOut.close();
            assetIn.close();
        } catch (IOException e) {
            Log.e(TAG, "Could not move " + assetName + "!");
            e.printStackTrace();
            throw new IOException(e);
        }

    }

    //modified from http://stackoverflow.com/a/10997886/3000692
    private boolean unpackZip(String pathToZip, String destPath) {
        new File(destPath).mkdirs();
        InputStream is;
        ZipInputStream zis;
        try {
            String filename;
            is = new FileInputStream(pathToZip);
            zis = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry ze;
            byte[] buffer = new byte[1024];
            int count;

            while ((ze = zis.getNextEntry()) != null) {
                // zapis do souboru
                filename = ze.getName();

                // Need to create directories if not exists, or
                // it will generate an Exception...
                if (ze.isDirectory()) {
                    File fmd = new File(destPath, filename);
                    fmd.mkdirs();
                    continue;
                }

                FileOutputStream fout = new FileOutputStream(new File(destPath, filename));

                // cteni zipu a zapis
                while ((count = zis.read(buffer)) != -1) {
                    fout.write(buffer, 0, count);
                }

                fout.close();
                zis.closeEntry();
            }

            zis.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}