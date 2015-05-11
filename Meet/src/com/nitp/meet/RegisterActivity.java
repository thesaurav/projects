package com.nitp.meet;

import static com.nitp.meet.CommonUtilities.SENDER_ID;
import static com.nitp.meet.CommonUtilities.REGISTER_URL;

import com.google.android.gcm.GCMRegistrar;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
 
public class RegisterActivity extends Activity implements OnClickListener{
    // alert dialog manager
    AlertDialogManager alert = new AlertDialogManager();
     
    // Internet detector
    ConnectionDetector cd;
     
    // UI elements
    EditText txtName;
    
   
    // Register button
    Button btnRegister;
    public static String mobile;
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
         
        cd = new ConnectionDetector(getApplicationContext());
 
        // Check if Internet present
        if (!cd.isConnectingToInternet()) {
            // Internet Connection is not present
            alert.showAlertDialog(RegisterActivity.this,
                    "Internet Connection Error",
                    "Please connect to working Internet connection", false);
            // stop executing code by return
            return;
        }
 
        // Check if GCM configuration is set
        if (REGISTER_URL == null || SENDER_ID == null || REGISTER_URL.length() == 0
                || SENDER_ID.length() == 0) {
            // GCM sernder id / server url is missing
            alert.showAlertDialog(RegisterActivity.this, "Configuration Error!",
                    "Please set your Server URL and GCM Sender ID", false);
            // stop executing code by return
             return;
        }
         
        txtName = (EditText) findViewById(R.id.txtName);
        
        btnRegister = (Button) findViewById(R.id.btnRegister);
         
        /*
         * Click event on Register button
         * */	
        
        btnRegister.setOnClickListener(this); 
    }

	

	@Override
	public void onClick(View arg0) {
		mobile = txtName.getText().toString();
        
        
        // Check if user filled the form
        if(mobile.trim().length() > 0 ){
            // Launch Main Activity
        	GCMRegistrar.register(this, SENDER_ID);
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
             
            // Registering user on our server                   
            // Sending registraiton details to MainActivity
            i.putExtra("mobile", mobile);
            
            
            startActivity(i);
            finish();
        }else{
            // user doen't filled that data
            // ask him to fill the form
            alert.showAlertDialog(RegisterActivity.this, "Registration Error!", "Please enter your details", false);
        }
		
	}
 
}