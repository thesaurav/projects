package com.nitp.meet;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;

import static com.nitp.meet.CommonUtilities.SENDER_ID;
import static com.nitp.meet.CommonUtilities.displayMessage;
 
public class GCMIntentService extends GCMBaseIntentService {
 
    private static final String TAG = "GCMIntentService";
    static String regId,mobile;
    static double latitude,longitude;
    
    AsyncTask<Void, Void, Void> mResponseTask;
 
    public GCMIntentService() {
        super(SENDER_ID);
    }
 
    /**
     * Method called on device registered
     **/
    @Override
    protected void onRegistered(Context context, String registrationId) {
        Log.i(TAG, "Device registered: regId = " + registrationId);
        displayMessage(context, "Your device registred with GCM");
       // Log.d("mobile", RegisterActivity.mobile);
        //ServerUtilities.register(context, RegisterActivity.mobile,  registrationId);
    }
 
    /**
     * Method called on device un registred
     * */
    @Override
    protected void onUnregistered(Context context, String registrationId) {
        Log.i(TAG, "Device unregistered");
        displayMessage(context, getString(R.string.gcm_unregistered));
        ServerUtilities.unregister(context, registrationId);
    }
 
    /**
     * Method called on Receiving a new message
     * */
    @Override
    protected void onMessage(Context context, Intent intent) {
        Log.i(TAG, "Received message from server");
      
        String message1 = intent.getExtras().getString("type");
        if(message1.equalsIgnoreCase("register") || message1.equalsIgnoreCase("update") )
        {
        	String message=intent.getExtras().getString("price");
        Toast.makeText(getApplicationContext(), message, TRIM_MEMORY_RUNNING_LOW).show();
        displayMessage(context, message);
        // notifies user
        generateNotification(context, message);
        }
        else if(message1.equalsIgnoreCase("request"))
        {
        	GPSTracker gps = new GPSTracker(context);
               
               // check if GPS enabled     
               if(gps.canGetLocation()){
                    
                   latitude = gps.getLatitude();
                   longitude = gps.getLongitude();
                   regId = GCMRegistrar.getRegistrationId(this);
                   
                   mobile=intent.getExtras().getString("mobile");
                   mResponseTask = new AsyncTask<Void, Void, Void>() {
                  	 
    	                @Override
    	                protected Void doInBackground(Void... params) {
    	                	
    	                    
    	                	ServerUtilities.response(getApplicationContext(), mobile, latitude,longitude,regId);
    	                    return null;
    	                }

    	                @Override
    	                protected void onPostExecute(Void result) {
    	                  mResponseTask = null;
    	                }

    	            };
    	            mResponseTask.execute(null, null, null);
            
            
                   //googleMap.setMyLocationEnabled(true);
                   // \n is for new line
                //   Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();    
               }else{
                   // can't get location
                   // GPS or Network is not enabled
                   // Ask user to enable GPS/network in settings
                   gps.showSettingsAlert();
               }
               String message=intent.getExtras().getString("price");
               Toast.makeText(getApplicationContext(), message, TRIM_MEMORY_RUNNING_LOW).show();
               displayMessage(context, message);
               // notifies user
               generateNotification(context, message);
        	}
        else if(message1.equalsIgnoreCase("response"))
        {
        	Double latitude=Double.parseDouble(intent.getExtras().getString("latitude"));
        	Double longitude=Double.parseDouble(intent.getExtras().getString("longitude"));
        	String mobile2=intent.getExtras().getString("mobile");
        	Intent i = new Intent(getApplicationContext(), MainActivity.class);
            
            // Registering user on our server                   
            // Sending registraiton details to MainActivity
        	i.putExtra("latitude", latitude);
        	i.putExtra("longitude", longitude);
        	i.putExtra("mobile2", mobile2);
        	i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            
            String message=intent.getExtras().getString("price");
            Toast.makeText(getApplicationContext(), message, TRIM_MEMORY_RUNNING_LOW).show();
            displayMessage(context, message);
            // notifies user
            generateNotification(context, message);
            startActivity(i);
            
        }
    }
 
    /**
     * Method called on receiving a deleted message
     * */
    @Override
    protected void onDeletedMessages(Context context, int total) {
        Log.i(TAG, "Received deleted messages notification");
        String message = getString(R.string.gcm_deleted, total);
        displayMessage(context, message);
        // notifies user
        generateNotification(context, message);
    }
 
    /**
     * Method called on Error
     * */
    @Override
    public void onError(Context context, String errorId) {
        Log.i(TAG, "Received error: " + errorId);
        displayMessage(context, getString(R.string.gcm_error, errorId));
    }
 
    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log message
        Log.i(TAG, "Received recoverable error: " + errorId);
        displayMessage(context, getString(R.string.gcm_recoverable_error,
                errorId));
        return super.onRecoverableError(context, errorId);
    }
 
    /**
     * Issues a notification to inform the user that server has sent a message.
     */
    private static void generateNotification(Context context, String message) {
        int icon = R.drawable.ic_launcher;
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(icon, message, when);
         
        String title = context.getString(R.string.app_name);
         
        Intent notificationIntent = new Intent(context, MainActivity.class);
        // set intent so it does not start a new activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent =
                PendingIntent.getActivity(context, 0, notificationIntent, 0);
        notification.setLatestEventInfo(context, title, message, intent);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
         
        // Play default notification sound
        notification.defaults |= Notification.DEFAULT_SOUND;
         
        // Vibrate if vibrate is enabled
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notificationManager.notify(0, notification);      
 
    }
 
}
