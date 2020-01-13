package com.thecelltech.ghumao;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Objects;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugins.GeneratedPluginRegistrant;

public class MainActivity extends FlutterActivity {
    private static final String CHANNEL = "com.shahanulshaheb.ghumao";
    public static final String STREAM = "com.yourcompany.eventchannelsample/stream";
    private BroadcastReceiver broadcastReceiver;
    int cnt = 0;
    MediaPlayer mp;
    LatLng destinationLatLng;


    @Override
    protected void onResume() {
        super.onResume();
        if (broadcastReceiver == null) {
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    cnt++;
                    Intent i = new Intent(getApplicationContext(), GPS_Service.class);
                    //startService(i);
                    String s = intent.getExtras().get("coordinates").toString();
                    i.putExtra("inputExtra", s);
                    // enable_buttons(false);
                    // ContextCompat.startForegroundService(context,i);
                    //    textView.append("count: "+cnt+"\n" +intent.getExtras().get("coordinates")+"\n");

                }
            };
        }
        registerReceiver(broadcastReceiver, new IntentFilter("location_update"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
    }

    private boolean runtime_permissions() {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);

            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                enable_buttons(true);
            } else {
                runtime_permissions();
            }
        }
    }

//==============flutter

    @Override
    public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
        GeneratedPluginRegistrant.registerWith(flutterEngine);
        // Object s = R.raw.loud_funky_tune;


        try {

            setVolumeControlStream(AudioManager.STREAM_MUSIC);

            mp = MediaPlayer.create(this, 2131296256);

            mp.prepare();

        } catch (Exception e) {
            System.out.println("Sound execption =========");
            System.out.println(e.toString());
        }


        new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), CHANNEL)
                .setMethodCallHandler(
                        (call, result) -> {
                            // Note: this method is invoked on the main thread.
                            // TODO
                            if (call.method.equals("getBatteryLevel")) {
                                int batteryLevel = getBatteryLevel();
                                String song = call.argument("song");
                                String volume = call.argument("volume");

                                System.out.println("=====================================");
                                System.out.println(volume);
                                System.out.println(R.raw.loud_funky_tune);
                                Ringtone();


/*
                                if (!runtime_permissions())
                                    enable_buttons(false);
*/

                                if (batteryLevel != -1) {
                                    result.success(batteryLevel);
                                } else {
                                    result.error("UNAVAILABLE", "Battery level not available.", null);
                                }
                            } else if (call.method.equals("startBackgroundService")) {


                                try {
                                    String lat = call.argument("lat");
                                    String lon = call.argument("lng");
                                    destinationLatLng = new LatLng(Double.valueOf(lat), Double.valueOf(lon));


                                } catch (Exception e) {
                                    System.out.println("========= Destination  Execption ==========");
                                    System.out.println(e.toString());
                                }

                                System.out.println("========= Destination ==========");
                                System.out.println(destinationLatLng);
                                System.out.println("========= Destination ==========");
                                System.out.println("========= Destination ==========");
                                System.out.println("========= Destination ==========");
                                System.out.println("========= Destination ==========");
                                System.out.println("========= Destination ==========");
                                if (!runtime_permissions()) {

                                    Intent i = new Intent(getApplicationContext(), GPS_Service.class);
                                    //startService(i);
                                    i.putExtra("lat", String.valueOf(destinationLatLng.latitude));
                                    i.putExtra("lng", String.valueOf(destinationLatLng.longitude));
                                     ContextCompat.startForegroundService(this, i);
                                }

                            } else if (call.method.equals("stopBackgroundService")) {

                                if (!runtime_permissions())
                                    enable_buttons(false);
                            } else {
                                result.notImplemented();
                            }


                        }
                );

       /* new EventChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), CHANNEL).setStreamHandler(
                new EventChannel.StreamHandler() {
                    @Override
                    public void onListen(Object args, EventChannel.EventSink events) {

                        final Objects[] s = new Objects[1];
                        if (broadcastReceiver == null) {
                            broadcastReceiver = new BroadcastReceiver() {
                                @Override
                                public void onReceive(Context context, Intent intent) {
                                    cnt++;

                                    //    textView.append("count: "+cnt+"\n" +intent.getExtras().get("coordinates")+"\n");
                                    //result.success(intent.getExtras().get("coordinates"));
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                        s[0] = (Objects) intent.getExtras().get("coordinates");
                                    }

                                }
                            };
                        }
                        registerReceiver(broadcastReceiver, new IntentFilter("location_update"));

                        events.success(s[0].toString());


                    }

                    @Override
                    public void onCancel(Object args) {
                        broadcastReceiver.abortBroadcast();

                    }
                }
        );*/


    }

    private void enable_buttons(boolean b) {
        if (b) {
            Intent i = new Intent(getApplicationContext(), GPS_Service.class);
            //startService(i);
            i.putExtra("lat", destinationLatLng.latitude);
            i.putExtra("lng", destinationLatLng.longitude);
            ContextCompat.startForegroundService(this, i);
        } else {
            Intent i = new Intent(getApplicationContext(), GPS_Service.class);
            stopService(i);

        }


    }


    private int getBatteryLevel() {
        int batteryLevel = -1;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            BatteryManager batteryManager = (BatteryManager) getSystemService(BATTERY_SERVICE);
            batteryLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        } else {
            Intent intent = new ContextWrapper(getApplicationContext()).
                    registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
            batteryLevel = (intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) * 100) /
                    intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        }

        //Ringtone();
        /*Intent i = new Intent(getApplicationContext(), GPS_Service.class);
        startService(i);*/
        return batteryLevel;

    }


    private void Ringtone() {

        try {
            mp.start();


        } catch (Exception e) {
            System.out.println("==========Sound Execption =======");
            System.out.println(e.toString());
        }


    }


}
