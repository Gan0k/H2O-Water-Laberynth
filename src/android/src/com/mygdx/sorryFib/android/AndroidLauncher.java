package com.mygdx.sorryFib.android;

import java.util.UUID;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.mygdx.sorryFib.*;
import com.mygdx.sorryFib.android.bluetooth.*;
import com.mygdx.sorryFib.utils.*;

import android.os.Bundle;
import android.content.Intent;
import android.content.Context;
import android.view.WindowManager;
import android.bluetooth.BluetoothAdapter;
import android.telephony.TelephonyManager;
import android.util.Log;

public class AndroidLauncher extends AndroidApplication implements MainActivityInterface {

	BluetoothMultiplayer bluetoothMultiplayer;
	BluetoothActionResolverAndroid bluetoothActionResolverAndroid;
	private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	private Game game;

	private static final int REQUEST_ENABLE_BT = 3;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useAccelerometer = true;
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		this.bluetoothMultiplayer = new BluetoothMultiplayer();
        bluetoothActionResolverAndroid = new BluetoothActionResolverAndroid(bluetoothMultiplayer);
        this.game = new Game(bluetoothActionResolverAndroid,this);
        initialize(game, config);
        this.bluetoothMultiplayer.setGame(game);
	}

	private boolean isBluetoothEnabled(){
    	if(mBluetoothAdapter == null) //device doesn't support bluetooth
    		return false;
    	return mBluetoothAdapter.isEnabled();
    }

    /**
 	* Displays a dialog asking to enable bluetooth
 	*/
    public boolean enableBluetoothQuestion(){
        // If BT is not on, request that it be enabled.
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }
        return isBluetoothEnabled();
    }

    private String getDeviceId(){
	  	final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);

	    final String tmDevice, tmSerial, androidId;
	    tmDevice = "" + tm.getDeviceId();
	    tmSerial = "" + tm.getSimSerialNumber();
	    androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

	    UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
	    return deviceUuid.toString();
	}

}
