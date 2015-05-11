package com.nitp.meet;

import static com.nitp.meet.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.nitp.meet.CommonUtilities.EXTRA_MESSAGE;
import static com.nitp.meet.CommonUtilities.SENDER_ID;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
 
public class MainActivity extends Activity implements OnClickListener {
    // label to display gcm messages
    EditText new_mb;
    Button exitbutton,findbutton,changebutton;
    static String regId1;
    private GoogleMap googleMap;  
    double latitude=0;
    double longitude=0;
    // Asyntask
    
    AsyncTask<Void, Void, Void> mRegisterTask;
    AsyncTask<Void, Void, Void> mRequestTask;
    AsyncTask<Void, Void, Void> mChangeTask;
 
    // Alert dialog manager
    AlertDialogManager alert = new AlertDialogManager();
     
    // Connection detector
    ConnectionDetector cd;
     
    public static String mobile;
    
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        exitbutton=(Button)findViewById(R.id.exitbutton);
        changebutton=(Button)findViewById(R.id.changebutton);
        findbutton=(Button)findViewById(R.id.findbutton);
        exitbutton.setOnClickListener(this);
        changebutton.setOnClickListener(this);
        findbutton.setOnClickListener(this);
        cd = new ConnectionDetector(getApplicationContext());
        try {
            // Loading map
            initilizeMap();
 
        } catch (Exception e) {
            e.printStackTrace();
        }
        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(false);
        googleMap.getUiSettings().setCompassEnabled(true);
        if(getIntent().hasExtra("latitude")&&getIntent().hasExtra("latitude"))
        {latitude=getIntent().getExtras().getDouble("latitude");
        longitude=getIntent().getExtras().getDouble("longitude");
        if(latitude!=0 && longitude!=0)
        {	String mobile2=getIntent().getExtras().getString("mobile2");
        	MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title(mobile2);
        	 
        	// Changing marker icon
        	marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        	 
        	// adding marker
        	googleMap.addMarker(marker);
        }}
        
        // Check if Internet present
        if (!cd.isConnectingToInternet()) {
            // Internet Connection is not present
            alert.showAlertDialog(MainActivity.this,
                    "Internet Connection Error",
                    "Please connect to working Internet connection", false);
            // stop executing code by return
            return;
        }
         
        // Getting name, email from intent
       // 
         
        
              
         
        // Make sure the device has the proper dependencies.
        GCMRegistrar.checkDevice(this);
 
        // Make sure the manifest was properly set - comment out this line
        // while developing the app, then uncomment it when it's ready.
        GCMRegistrar.checkManifest(this);
 
        new_mb = (EditText) findViewById(R.id.mobile);
         
        registerReceiver(mHandleMessageReceiver, new IntentFilter(
                DISPLAY_MESSAGE_ACTION));
         
        // Get GCM registration id
        final String regId = GCMRegistrar.getRegistrationId(this);

        regId1=regId;
        // Check if regid already presents
        if (regId.equals("")) {
            // Registration is not present, register now with GCM  
        	Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
        	startActivity(i);
        	
        	GCMRegistrar.register(this, SENDER_ID);
        } else {
            // Device is already registered on GCM
            if (GCMRegistrar.isRegisteredOnServer(this)) {
                // Skips registration.              
                Toast.makeText(getApplicationContext(), "Already registered with GCM", Toast.LENGTH_LONG).show();
            } else {
                // Try to register again, but not in the UI thread.
                // It's also necessary to cancel the thread onDestroy(),
                // hence the use of AsyncTask instead of a raw thread.
                final Context context = this;
                mRegisterTask = new AsyncTask<Void, Void, Void>() {
 
                    @Override
                    protected Void doInBackground(Void... params) {
                        // Register on our server
                        // On server creates a new user
                    	mobile =getIntent().getStringExtra("mobile");
                        ServerUtilities.register(context, mobile, regId);
                        return null;
                    }
 
                    @Override
                    protected void onPostExecute(Void result) {
                        mRegisterTask = null;
                    }
 
                };
                mRegisterTask.execute(null, null, null);
            }
        }
    }       
 
    /**
     * Receiving push messages
     * */
    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        	 Log.i("GCM","message received");
            String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
            Log.i("GCM Meet",newMessage);
            // Waking up mobile if it is sleeping
            WakeLocker.acquire(getApplicationContext());
             
            /**
             * Take appropriate action on this message
             * depending upon your app requirement
             * For now i am just displaying it on the screen
             * */
             
            // Showing received message
                      
            Toast.makeText(getApplicationContext(), "New Message: " + newMessage, Toast.LENGTH_LONG).show();
             
            // Releasing wake lock
            WakeLocker.release();
        }
    };
    private void initilizeMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();
 
            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
 
    @Override
    protected void onResume() {
        super.onResume();
        initilizeMap();
    }
    @Override
    protected void onDestroy() {
        if (mRegisterTask != null) {
            mRegisterTask.cancel(true);
        }
        try {
            unregisterReceiver(mHandleMessageReceiver);
            GCMRegistrar.onDestroy(this);
        } catch (Exception e) {
            Log.e("UnRegister Receiver Error", "> " + e.getMessage());
        }
        super.onDestroy();
    }

	@Override
	public void onClick(View arg0) {
		if(changebutton==(Button)arg0)
		{
            mChangeTask = new AsyncTask<Void, Void, Void>() {
            	 
                @Override
                protected Void doInBackground(Void... params) {
                    
                	ServerUtilities.change(getApplicationContext(), new_mb.getText().toString(), regId1);
                    return null;
                }

                @Override
                protected void onPostExecute(Void result) {
                    mChangeTask = null;
                }

            };
            mChangeTask.execute(null, null, null);
			
		}
		else if(findbutton==(Button)arg0)
		{
			 mRequestTask = new AsyncTask<Void, Void, Void>() {
            	 
	                @Override
	                protected Void doInBackground(Void... params) {
	                    
	                	ServerUtilities.request(getApplicationContext(), new_mb.getText().toString(), regId1);
	                    return null;
	                }

	                @Override
	                protected void onPostExecute(Void result) {
	                    mRequestTask = null;
	                }

	            };
	            mRequestTask.execute(null, null, null);
			
		}
		else if(exitbutton==(Button)arg0)
		{
			
			GCMRegistrar.unregister(getApplicationContext());
		}
		}

	
	
 
}




  


