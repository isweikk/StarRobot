package com.weikk.ble;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.weikk.utils.Logger;

import java.util.List;
import java.util.UUID;

public class BleService  extends Service {
    public static final String ACTION_DATA_AVAILABLE = "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    public static final String ACTION_GATT_CONNECTED = "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public static final String ACTION_GATT_DISCONNECTED = "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public static final String ACTION_GATT_SERVICES_DISCOVERED = "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public static final String EXTRA_DATA = "com.example.bluetooth.le.EXTRA_DATA";
    private static final int STATE_CONNECTED = 2;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_DISCONNECTED = 0;
    private static final String TAG = BleService.class.getSimpleName();
    public static final UUID UUID_HEART_RATE_MEASUREMENT = UUID.fromString(SampleGattAttributes.HEART_RATE_MEASUREMENT);
    private final IBinder mBinder = new LocalBinder();
    private BluetoothAdapter mBluetoothAdapter;
    private String mBluetoothDeviceAddress;
    private BluetoothGatt mBluetoothGatt;
    private BluetoothManager mBluetoothManager;
    private int mConnectionState = STATE_DISCONNECTED;
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback()
    {
        public void onCharacteristicChanged(BluetoothGatt paramAnonymousBluetoothGatt, BluetoothGattCharacteristic paramAnonymousBluetoothGattCharacteristic)
        {
            BleService.this.broadcastUpdate("com.example.bluetooth.le.ACTION_DATA_AVAILABLE", paramAnonymousBluetoothGattCharacteristic);
        }

        public void onCharacteristicRead(BluetoothGatt paramAnonymousBluetoothGatt, BluetoothGattCharacteristic paramAnonymousBluetoothGattCharacteristic, int paramAnonymousInt)
        {
            if (paramAnonymousInt == 0)
                BleService.this.broadcastUpdate("com.example.bluetooth.le.ACTION_DATA_AVAILABLE", paramAnonymousBluetoothGattCharacteristic);
        }

        public void onConnectionStateChange(BluetoothGatt paramAnonymousBluetoothGatt, int paramAnonymousInt1, int paramAnonymousInt2)
        {
            if (paramAnonymousInt2 == 2)
            {
                mConnectionState = STATE_CONNECTED; //TODO //BleService.access$002(BleService.this, 2);
                BleService.this.broadcastUpdate("com.example.bluetooth.le.ACTION_GATT_CONNECTED");
                Logger.i(BleService.TAG, "Connected to GATT server.");
                Logger.i(BleService.TAG, "Attempting to start service discovery:" + BleService.this.mBluetoothGatt.discoverServices());
            }
            while (paramAnonymousInt2 != 0)
                return;
            mConnectionState = STATE_DISCONNECTED; //TODO //BleService.access$002(BleService.this, 0);
            Logger.i(BleService.TAG, "Disconnected from GATT server.");
            BleService.this.broadcastUpdate("com.example.bluetooth.le.ACTION_GATT_DISCONNECTED");
        }

        public void onServicesDiscovered(BluetoothGatt paramAnonymousBluetoothGatt, int paramAnonymousInt)
        {
            if (paramAnonymousInt == 0)
            {
                BleService.this.broadcastUpdate("com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED");
                return;
            }
            Logger.w(BleService.TAG, "onServicesDiscovered received: " + paramAnonymousInt);
            System.out.println("onServicesDiscovered received: " + paramAnonymousInt);
        }
    };
    public final String readStr = null;

    private void broadcastUpdate(String paramString)
    {
        sendBroadcast(new Intent(paramString));
    }

    private void broadcastUpdate(String paramString, BluetoothGattCharacteristic paramBluetoothGattCharacteristic)
    {
        Intent localIntent = new Intent(paramString);
        int j;
        if (UUID_HEART_RATE_MEASUREMENT.equals(paramBluetoothGattCharacteristic.getUuid()))
            if ((0x1 & paramBluetoothGattCharacteristic.getProperties()) != 0)
            {
                j = 18;
                Logger.d(TAG, "Heart rate format UINT16.");
                int k = paramBluetoothGattCharacteristic.getIntValue(j, 1).intValue();
                String str = TAG;
                Object[] arrayOfObject = new Object[1];
                arrayOfObject[0] = Integer.valueOf(k);
                Logger.d(str, String.format("Received heart rate: %d", arrayOfObject));
                localIntent.putExtra("com.example.bluetooth.le.EXTRA_DATA", String.valueOf(k));
            }
        while (true) {
            sendBroadcast(localIntent);
            return;
            j = 17;
            Logger.d(TAG, "Heart rate format UINT8.");
            break;
            byte[] arrayOfByte = paramBluetoothGattCharacteristic.getValue();
            for (int i = 0; i < arrayOfByte.length; i++) {
                if ((arrayOfByte != null) && (arrayOfByte.length > 0))
                    localIntent.putExtra("com.example.bluetooth.le.EXTRA_DATA", new String(arrayOfByte));
            }
        }
    }

    public void close()
    {
        if (this.mBluetoothGatt == null)
            return;
        this.mBluetoothGatt.close();
        this.mBluetoothGatt = null;
    }

    public boolean connect(String paramString)
    {
        if ((this.mBluetoothAdapter == null) || (paramString == null))
        {
            Logger.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
            return false;
        }
        if ((this.mBluetoothDeviceAddress != null) && (paramString.equals(this.mBluetoothDeviceAddress)) && (this.mBluetoothGatt != null))
        {
            Logger.d(TAG, "Trying to use an existing mBluetoothGatt for connection.");
            if (this.mBluetoothGatt.connect())
            {
                this.mConnectionState = 1;
                return true;
            }
            return false;
        }
        BluetoothDevice localBluetoothDevice = this.mBluetoothAdapter.getRemoteDevice(paramString);
        if (localBluetoothDevice == null)
        {
            Logger.w(TAG, "Device not found.  Unable to connect.");
            return false;
        }
        this.mBluetoothGatt = localBluetoothDevice.connectGatt(this, false, this.mGattCallback);
        Logger.d(TAG, "Trying to create a new connection.");
        this.mBluetoothDeviceAddress = paramString;
        this.mConnectionState = 1;
        System.out.println("device.getBondState==" + localBluetoothDevice.getBondState());
        return true;
    }

    public void disconnect()
    {
        if ((this.mBluetoothAdapter == null) || (this.mBluetoothGatt == null))
        {
            Logger.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        this.mBluetoothGatt.disconnect();
    }

    public BluetoothGattService getSupportedGattServices(UUID paramUUID)
    {
        if (this.mBluetoothGatt == null)
            return null;
        return this.mBluetoothGatt.getService(paramUUID);
    }

    public List<BluetoothGattService> getSupportedGattServices()
    {
        if (this.mBluetoothGatt == null)
            return null;
        return this.mBluetoothGatt.getServices();
    }

    public boolean initialize()
    {
        if (this.mBluetoothManager == null)
        {
            this.mBluetoothManager = ((BluetoothManager)getSystemService("bluetooth"));
            if (this.mBluetoothManager == null)
            {
                Logger.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }
        this.mBluetoothAdapter = this.mBluetoothManager.getAdapter();
        if (this.mBluetoothAdapter == null)
        {
            Logger.e(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        }
        return true;
    }

    public IBinder onBind(Intent paramIntent)
    {
        return this.mBinder;
    }

    public boolean onUnbind(Intent paramIntent)
    {
        close();
        return super.onUnbind(paramIntent);
    }

    public void readCharacteristic(BluetoothGattCharacteristic paramBluetoothGattCharacteristic)
    {
        if ((this.mBluetoothAdapter == null) || (this.mBluetoothGatt == null))
        {
            Logger.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        this.mBluetoothGatt.readCharacteristic(paramBluetoothGattCharacteristic);
    }

    public void setCharacteristicNotification(BluetoothGattCharacteristic paramBluetoothGattCharacteristic, boolean paramBoolean)
    {
        if ((this.mBluetoothAdapter == null) || (this.mBluetoothGatt == null))
            Logger.w(TAG, "BluetoothAdapter not initialized");
        do
        {
            return;
            this.mBluetoothGatt.setCharacteristicNotification(paramBluetoothGattCharacteristic, paramBoolean);
        }
        while (!UUID_HEART_RATE_MEASUREMENT.equals(paramBluetoothGattCharacteristic.getUuid()));
        BluetoothGattDescriptor localBluetoothGattDescriptor = paramBluetoothGattCharacteristic.getDescriptor(UUID.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
        localBluetoothGattDescriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        this.mBluetoothGatt.writeDescriptor(localBluetoothGattDescriptor);
    }

    public void writeCharacteristic(BluetoothGattCharacteristic paramBluetoothGattCharacteristic)
    {
        if ((this.mBluetoothAdapter == null) || (this.mBluetoothGatt == null))
        {
            Logger.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        this.mBluetoothGatt.writeCharacteristic(paramBluetoothGattCharacteristic);
    }

    public class LocalBinder extends Binder
    {
        public LocalBinder()
        {
        }

        BleService getService()
        {
            return BleService.this;
        }
    }
}
