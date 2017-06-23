package com.dujiajun.bclockmanager.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.dujiajun.bclockmanager.R;
import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.Constants;

import com.inuker.bluetooth.library.connect.listener.BluetoothStateListener;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;

import java.util.ArrayList;
import java.util.List;

import java.util.UUID;

import static com.inuker.bluetooth.library.Constants.REQUEST_SUCCESS;

/**
 * Created by cqduj on 2017/06/06.
 */

public class SearchingActivity extends AppCompatActivity {
    //BtHelperClient btHelperClient;
    //private BluetoothAdapter adapter;
    List<String> deviceNames = new ArrayList<>();
    ArrayAdapter<String> deviceAdapter;
    List<BluetoothDevice> devices = new ArrayList<>();
    BluetoothClient mClient;
    String connect_mac;
    UUID SerialPortServiceClass_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
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
                connect_mac = devices.get(i).getAddress();
                Toast.makeText(SearchingActivity.this, devices.get(i).getName() + " " + devices.get(i).getAddress(), Toast.LENGTH_SHORT).show();
                mClient.connect(connect_mac, new BleConnectResponse() {
                    @Override
                    public void onResponse(int code, BleGattProfile profile) {
                        if (code == REQUEST_SUCCESS) {
                            //TODO
                            mClient.write(connect_mac, SerialPortServiceClass_UUID, UUID.randomUUID(), "p".getBytes(), new BleWriteResponse() {
                                @Override
                                public void onResponse(int code) {
                                    if (code == Constants.CODE_WRITE) {
                                        Toast.makeText(SearchingActivity.this, "write", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });

        mClient = new BluetoothClient(getApplicationContext());
        mClient.registerBluetoothStateListener(mBluetoothStateListener);

        if (!mClient.isBluetoothOpened()) {
            mClient.openBluetooth();
        } else {
            search();
        }

    }

    private void search() {
        SearchRequest request = new SearchRequest.Builder()
                .searchBluetoothLeDevice(3000, 3)   // 先扫BLE设备3次，每次3s
                .searchBluetoothClassicDevice(5000) // 再扫经典蓝牙5s
                .searchBluetoothLeDevice(2000)      // 再扫BLE设备2s
                .build();

        mClient.search(request, new SearchResponse() {
            @Override
            public void onSearchStarted() {
                Toast.makeText(SearchingActivity.this, "onSearchStarted", Toast.LENGTH_SHORT).show();
                devices.clear();
                deviceNames.clear();
                deviceAdapter.notifyDataSetChanged();
            }

            @Override
            public void onDeviceFounded(SearchResult device) {

                if (!"NULL".equals(device.getName())) {
                    deviceNames.add(device.getName());
                    devices.add(device.device);
                    deviceAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onSearchStopped() {
                Toast.makeText(SearchingActivity.this, "onSearchStopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSearchCanceled() {

            }
        });
    }

    //OnSearchDeviceListener onSearchDeviceListener = ;
    private final BluetoothStateListener mBluetoothStateListener = new BluetoothStateListener() {
        @Override
        public void onBluetoothStateChanged(boolean openOrClosed) {
            if (openOrClosed) {
                search();
            }

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

                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onPause() {

        mClient.unregisterBluetoothStateListener(mBluetoothStateListener);
        if (mClient.getConnectStatus(connect_mac) == Constants.STATUS_DEVICE_CONNECTING) {
            mClient.disconnect(connect_mac);
        }
        super.onPause();
    }

}
