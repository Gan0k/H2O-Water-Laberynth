package com.mygdx.sorryFib.android;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
 
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.mygdx.sorryFib.Game;

public class AndroidLauncher extends AndroidApplication {

	private NfcAdapter mNfcAdapter;

	public static final String MIME_TEXT_PLAIN = "text/plain";
	public static final String TAG = "NfcDemo";

	private Game game;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

		if (mNfcAdapter == null) {
			Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
			finish();
			return;
		}
	 
		if (!mNfcAdapter.isEnabled()) {
			Toast.makeText(this, "NFC Disabled, please enable.", Toast.LENGTH_LONG).show();
		} else {
			Log.i(TAG,"NFC systems enabled!");
		}

		handleIntent(getIntent());

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		game = new Game();
		initialize(game, config);
	}

	private void handleIntent(Intent intent) {
		Log.i(TAG,Integer.toString(times));
		String action = intent.getAction();
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
			String type = intent.getType();
			if (MIME_TEXT_PLAIN.equals(type)) {
				Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
				new NdefReaderTask().execute(tag);
			} 
			else Log.d(TAG, "Wrong mime type: " + type);
		} 
		else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
			Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			String[] techList = tag.getTechList();
			String searchedTech = Ndef.class.getName();
			 
			for (String tech : techList) {
				if (searchedTech.equals(tech)) {
					new NdefReaderTask().execute(tag);
					break;
				}
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		setupForegroundDispatch(this, mNfcAdapter);
	}

	@Override
	protected void onPause() {
		stopForegroundDispatch(this, mNfcAdapter);  
		super.onPause();
	}

	@Override
	protected void onNewIntent(Intent intent) { 
		handleIntent(intent);
	}

	public static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
		final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
 
		final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);
 
		IntentFilter[] filters = new IntentFilter[1];
		String[][] techList = new String[][]{};
 
		filters[0] = new IntentFilter();
		filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
		filters[0].addCategory(Intent.CATEGORY_DEFAULT);
		try {
			filters[0].addDataType(MIME_TEXT_PLAIN);
		} catch (MalformedMimeTypeException e) {
			throw new RuntimeException("Check your mime type.");
		}
		 
		adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
	}

	public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
		adapter.disableForegroundDispatch(activity);
	}

	private class NdefReaderTask extends AsyncTask<Tag, Void, String> {
	 
	    @Override
	    protected String doInBackground(Tag... params) {
	        Tag tag = params[0];
	         
	        Ndef ndef = Ndef.get(tag);
	        if (ndef == null) {
	            // NDEF is not supported by this Tag. 
	            return null;
	        }
	 
	        NdefMessage ndefMessage = ndef.getCachedNdefMessage();
	 
	        NdefRecord[] records = ndefMessage.getRecords();
	        for (NdefRecord ndefRecord : records) {
	            if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
	                try {
	                    return readText(ndefRecord);
	                } catch (UnsupportedEncodingException e) {
	                    Log.e(TAG, "Unsupported Encoding", e);
	                }
	            }
	        }
	 
	        return null;
	    }
	     
	    private String readText(NdefRecord record) throws UnsupportedEncodingException { 
	        byte[] payload = record.getPayload();
	 
	        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
	        int languageCodeLength = payload[0] & 0063;

	        return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
	    }
	     
	    @Override
	    protected void onPostExecute(String result) {
	        if (result != null) {
	            Log.e(TAG, result);
	        }
	    }
	}
}
