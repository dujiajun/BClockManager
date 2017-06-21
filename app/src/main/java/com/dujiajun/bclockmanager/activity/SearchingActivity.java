package com.dujiajun.bclockmanager.activity;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.dujiajun.bclockmanager.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import top.wuhaojie.bthelper.BtHelperClient;
import top.wuhaojie.bthelper.OnSearchDeviceListener;

/**
 * Created by cqduj on 2017/06/06.
 */

public class SearchingActivity extends AppCompatActivity {
    BtHelperClient btHelperClient;
    //private BluetoothAdapter adapter;
    List<String> deviceNames = new ArrayList<>();
    ArrayAdapter<String> deviceAdapter;
    List<BluetoothDevice> devices = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searching);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //search();
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter == null) {
            Toast.makeText(this, "获取蓝牙设备失败", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!adapter.isEnabled()) {
            adapter.enable();
            Intent enable = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            startActivity(enable);
        }

        deviceAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, deviceNames);
        final ListView listView = (ListView) findViewById(R.id.lv_deivces);
        listView.setAdapter(deviceAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("devicename", devices.get(i).getName());
                editor.putString("deviceaddress", devices.get(i).getAddress());
                editor.apply();
                Toast.makeText(SearchingActivity.this, devices.get(i).getName() + " " + devices.get(i).getAddress(), Toast.LENGTH_SHORT).show();
            }
        });
        if (btHelperClient == null) {
            btHelperClient = BtHelperClient.from(this);

        }
        btHelperClient.searchDevices(onSearchDeviceListener);
    }

    OnSearchDeviceListener onSearchDeviceListener = new OnSearchDeviceListener() {
        @Override
        public void onStartDiscovery() {
            devices.clear();
            deviceNames.clear();
            deviceAdapter.notifyDataSetChanged();
            Toast.makeText(SearchingActivity.this, "onStartDiscovery", Toast.LENGTH_SHORT).show();//.d("TAG", "onStartDiscovery()");
            //listView.setClickable(false);
        }

        @Override
        public void onNewDeviceFounded(BluetoothDevice device) {
            //Toast.makeText(SearchingActivity.this, "onNewDeviceFounded"+device.getName(), Toast.LENGTH_SHORT).show();
            deviceNames.add(device.getName());
            devices.add(device);
            deviceAdapter.notifyDataSetChanged();
            //Log.d("TAG", "new device: " + device.getName() + " " + device.getAddress());
        }

        @Override
        public void onSearchCompleted(List<BluetoothDevice> bondedDevices, List<BluetoothDevice> newDevices) {
            Toast.makeText(SearchingActivity.this, "onSearchCompleted", Toast.LENGTH_SHORT).show();
            //devices = newDevices;
            //listView.setClickable(true);
        }

        @Override
        public void onError(Exception e) {
            e.printStackTrace();
            Toast.makeText(SearchingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_searching, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                if (btHelperClient != null) {
                    btHelperClient.searchDevices(onSearchDeviceListener);
                }
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onPause() {
        //unregisterReceiver(mReceiver);
        if (btHelperClient != null) {
            btHelperClient.close();
        }
        super.onPause();
    }
    /*private List<String> bondedDevices = new ArrayList<>();
    private List<String> newDevices = new ArrayList<>();
    private BluetoothAdapter adapter;
    private void search() {
        adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter==null){
            Toast.makeText(this, "获取蓝牙设备失败", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!adapter.isEnabled()) {
            adapter.enable();
            Intent enable = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            startActivity(enable);
        }

        Set<BluetoothDevice> bondedDevices = adapter.getBondedDevices();

        if (bondedDevices.size()>0){
            for (BluetoothDevice device : bondedDevices)
            this.bondedDevices.add(device.getName()+"\n"+device.getAddress());
        }

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);

        //当搜索结束后调用onReceive
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);
        //enable.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 3600); //3600为蓝牙设备可见时间

        Intent searchIntent = new Intent(this, ComminuteActivity.class);
        startActivity(searchIntent);
    }*/
/*
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // 已经配对的则跳过
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    newDevices.add(device.getName() + "\n" + device.getAddress());  //保存设备地址与名字
                }
            }else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {  //搜索结束
                if (newDevices.size() == 0) {
                    newDevices.add("没有搜索到设备");
                }
            }

        }
    };


*/
    //UUID可以看做一个端口号
    /*private static final UUID MY_UUID =
            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    //像一个服务器一样时刻监听是否有连接建立
    private class AcceptThread extends Thread{
        private BluetoothServerSocket serverSocket;

        public AcceptThread(boolean secure){
            BluetoothServerSocket temp = null;
            try {
                temp = adapter.listenUsingRfcommWithServiceRecord(
                        NAME_INSECURE, MY_UUID);
            } catch (IOException e) {
                Log.e("app", "listen() failed", e);
            }
            serverSocket = temp;
        }

        public void run(){
            BluetoothSocket socket=null;
            while(true){
                try {
                    socket = serverSocket.accept();
                } catch (IOException e) {
                    Log.e("app", "accept() failed", e);
                    break;
                }
            }
            if(socket!=null){
                //此时可以新建一个数据交换线程，把此socket传进去
            }
        }

        //取消监听
        public void cancel(){
            try {
                serverSocket.close();
            } catch (IOException e) {
                Log.e("app", "Socket Type close() of server failed", e);
            }
        }

    }*/
}
