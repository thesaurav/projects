package com.nitp.meet;

import android.content.Context;
import android.content.Intent;
 
public final class CommonUtilities {
     
    // give your server registration url here
	static final String REGISTER_URL = "http://10.10.7.236/register.php"; 
	static final String REQUEST_URL = "http://10.10.7.236/request.php"; 
	static final String RESPONSE_URL = "http://10.10.7.236/response.php"; 
	static final String EXIT_URL = "http://10.10.7.236/exit.php"; 
	static final String CHANGE_URL = "http://10.10.7.236/update.php"; 
	 
    // Google project id
    static final String SENDER_ID = "811168961138"; 
 
    /**
     * Tag used on log messages.
     */
    static final String TAG = "Meet GCM";
 
    static final String DISPLAY_MESSAGE_ACTION =
            "com.nitp.meet.DISPLAY_MESSAGE";
 
    static final String EXTRA_MESSAGE = "message";
 
    /**
     * Notifies UI to display a message.
     * <p>
     * This method is defined in the common helper because it's used both by
     * the UI and the background service.
     *
     * @param context application's context.
     * @param message message to be displayed.
     */
    static void displayMessage(Context context, String message) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }
}
