package com.thecelltech.ghumao;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.maps.model.LatLng;

import static com.thecelltech.ghumao.App.CHANNEL_ID;

public class GPS_Service extends Service {

    private LocationListener listener;
    private LocationManager locationManager;
    LatLng latLngA = new LatLng(12.3456789, 98.7654321);
    LatLng latLngB = new LatLng(98.7654321, 12.3456789);
    Location locationA = new Location("point A");
    Object o = "initial";
    LatLng destinationPoint;

    MediaPlayer mp;

    Location Previous_location = null;


    private void Ringtone() {
        try {
            mp.start();

        } catch (Exception e) {
            System.out.println("==========Sound Execption =======");
            System.out.println(e.toString());
        }
    }

    private Notification BuildNotificationFun(String input) {

        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);


        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("GPS Service")
                .setContentText(input)
                .setSmallIcon(R.drawable.gps)
                .setContentIntent(pendingIntent)
                .build();

        return notification;

    }


    private void WeakUpBabe() {
        System.out.println("ghum theke uthun");
        Ringtone();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        System.out.println("==============================onStartCommand ");

        String lat, lng;
        try {

            lat = intent.getStringExtra("lat");
            lng = intent.getStringExtra("lng");
            System.out.println(lat);
            System.out.println(lng);
            destinationPoint = new LatLng(Double.valueOf(lat), Double.valueOf(lng));

            locationA.setLatitude(destinationPoint.latitude);
            locationA.setLongitude(destinationPoint.longitude);
            float acc = (float) 12.0;
            locationA.setAccuracy(acc);
            System.out.println("location A ===================");
            System.out.println(locationA);

        } catch (Exception e) {

            System.out.println(e.toString());
            System.out.println(destinationPoint.toString());
            lat = " lat";
            lng = "lng";

        }


        Log.i("tag", "=============>>>>backgrund<<<<<");
        Log.i("tag", destinationPoint.toString());


        Notification notification = BuildNotificationFun(lat + " " + lng);
        startForeground(12, notification);


        location_Service();
        // notification.notifyAll();
        //do heavy work on a background thread
        //stopSelf();

        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate() {
        try {
            mp = MediaPlayer.create(this, 2131296256);

        } catch (Exception e) {
            System.out.println("Sound execption =========");
            System.out.println(e.toString());
        }

        // location_Service();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locationManager != null) {
            locationManager.removeUpdates(listener);
        }
    }

    @SuppressLint("MissingPermission")
    private void location_Service() {

        System.out.println("location service started");
        System.out.println("===========================================================");
        // Ringtone();

        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                System.out.println("==============location=======================");
                Log.i("location ", location.toString());
                System.out.println(location.toString());


                System.out.println("==============Distance=======================");
                //todo calculate distance between two point
                Double d = Double.valueOf(location.distanceTo(locationA));

                Notification notification = BuildNotificationFun(d.toString());
                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(12, notification);


                System.out.println(d.toString());
                System.out.println(d);

                Double dis = 1000.0;// meter

                o = d.toString();
                if (d < dis) {
                    WeakUpBabe();
                }


                Intent i = new Intent("location_update");
                //i.putExtra("coordinates", location.getLongitude() + " " + location.getLatitude());
                i.putExtra("coordinates", d.toString());
                sendBroadcast(i);
                // stopSelf();

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        };

        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0, listener);
    }
}