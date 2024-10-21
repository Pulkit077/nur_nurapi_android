package com.nordicid.nurapi;

import android.content.Context;

import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class DeviceScannerManager implements NurDeviceScanner.NurDeviceScannerListener {
    private static final String TAG = "DeviceScannerManager";
    private final NurDeviceScanner mDeviceScanner;
    private final List<NurDeviceSpec> mDevicesList;
    private final ScanListener mScanListener;

    public interface ScanListener {
        void onDeviceFound(NurDeviceSpec device);
        void onScanFinished(List<NurDeviceSpec> deviceList);
    }

    public DeviceScannerManager(Context context, int requestedDevices, NurApi nurApi, ScanListener listener) {
        mDevicesList = new ArrayList<>();
        mDeviceScanner = new NurDeviceScanner(context, requestedDevices, this, nurApi);
        this.mScanListener = listener;
    }

    public void startScan() {
        Log.d(TAG, "Starting device scan");
        mDevicesList.clear();  // Clear previous results
        mDeviceScanner.scanDevices();
    }

    public void stopScan() {
        Log.d(TAG, "Stopping device scan");
        mDeviceScanner.stopScan();
    }

    public List<NurDeviceSpec> getDevicesList() {
        return mDevicesList;
    }

    @Override
    public void onScanStarted() {
        Log.d(TAG, "Starting device scan");
        mDevicesList.clear();  // Clear previous results
        mDeviceScanner.scanDevices();
    }

    @Override
    public void onDeviceFound(NurDeviceSpec device) {
        Log.d(TAG, "Device found: " + device.getName());
        mDevicesList.add(device);

        // Notify the listener when a device is found
        if (mScanListener != null) {
            mScanListener.onDeviceFound(device);
        }
    }

    @Override
    public void onScanFinished() {
        Log.d(TAG, "Scan finished. Total devices: " + mDevicesList.size());
        // Notify the listener when the scan is finished
        if (mScanListener != null) {
            mScanListener.onScanFinished(mDevicesList);
        }
    }
}
