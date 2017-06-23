package com.loonggg.lib.alarmmanager.clock;

import android.app.Activity;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

//import top.wuhaojie.bthelper.BtHelperClient;


public class ClockAlarmActivity extends Activity {
    private MediaPlayer mediaPlayer;
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock_alarm);
        String message = this.getIntent().getStringExtra("msg");
        int flag = this.getIntent().getIntExtra("flag", 0);
        showDialogInBroadcastReceiver(message, flag);
    }

    private void showDialogInBroadcastReceiver(String message, final int flag) {
        if (flag == 1 || flag == 2) {
            mediaPlayer = MediaPlayer.create(this, R.raw.in_call_alarm);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }
        //数组参数意义：第一个参数为等待指定时间后开始震动，震动时间为第二个参数。后边的参数依次为等待震动和震动的时间
        //第二个参数为重复次数，-1为不重复，0为一直震动
        if (flag == 0 || flag == 2) {
            vibrator = (Vibrator) this.getSystemService(Service.VIBRATOR_SERVICE);
            vibrator.vibrate(new long[]{100, 10, 100, 600}, 0);
        }

        final SimpleDialog dialog = new SimpleDialog(this, R.style.Theme_dialog);
        dialog.show();
        dialog.setTitle("闹钟提醒");
        dialog.setMessage(message);
        dialog.setClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog.bt_confirm == v || dialog.bt_cancel == v) {
                    if (flag == 1 || flag == 2) {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                    }
                    if (flag == 0 || flag == 2) {
                        vibrator.cancel();
                    }
                    //SendMessage('h');
                    dialog.dismiss();
                    finish();
                }
            }
        });

        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mac = pref.getString("deviceaddress", "");
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothdevice = mBluetoothAdapter.getRemoteDevice(mac);

        SendMessage('p');

    }

    SharedPreferences pref;
    String mac;
    BluetoothAdapter mBluetoothAdapter;
    final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";
    final UUID uuid = UUID.fromString(SPP_UUID);
    BluetoothDevice bluetoothdevice;

    void SendMessage(final char c) {
        new Thread() {

            BluetoothSocket socket;

            OutputStream outputStream = null;

            @Override
            public void run() {
                Log.e("TAG", "555555555555555555555555555555");
                try {
                    socket = bluetoothdevice.createInsecureRfcommSocketToServiceRecord(uuid);
                    Log.e("TAG", "7777777777777777");
                    socket.connect();
                    Log.e("TAG", "888888888888999");
                    outputStream = socket.getOutputStream();
                    Log.e("TAG", "999999999999999999999");
                    outputStream.write(c);
                    Log.e("TAG", "100000000000000000");
                    //Toast.makeText(getApplicationContext(), "已发送", Toast.LENGTH_SHORT).show();
                    outputStream.flush();
                    outputStream.close();

                    socket.close();
                } catch (IOException e) {
                    Log.e("TAG", "666+" + e.getMessage());
                }
            }


        }.run();
    }
}
