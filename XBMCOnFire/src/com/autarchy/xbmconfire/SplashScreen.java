package com.autarchy.xbmconfire;

import com.autarchy.xbmconfire.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

public class SplashScreen extends Activity 
{
	
	/*
	 * This class exists solely because the Amazon App Store requires a minimum of 3 screenshots, but
	 * the app would otherwise have only two screens.  This splash sceen is intended to display only
	 * on first run.
	 */

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);

//        setContentView(R.layout.activity_splash_screen);        
//Toast.makeText(getApplicationContext(), getPref("showSplash"), Toast.LENGTH_LONG).show();

    	if (getPref("showSplash") == null)
    	{	
    		setContentView(R.layout.activity_splash_screen);
    		new AlertDialog.Builder(this)
    	    .setTitle(R.string.splash_dialog_title)
    	    .setMessage(R.string.splash_dialog_text)
    	    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
    	    {
    	        public void onClick(DialogInterface dialog, int which)
    	        { 
    	        	setPref("showSplash","true");
    	        	startMain();
    	        }
    	     })
    	    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() 
    	    {
    	        public void onClick(DialogInterface dialog, int which) 
    	        { 
    	        	setPref("showSplash","false");
    	        	startMain();
    	        }
    	     })
    	    .setIcon(android.R.drawable.ic_dialog_alert)
    	     .show();
    		    		
    	}       
    	else if (getPref("showSplash") == "true")
    	{
    		setContentView(R.layout.activity_splash_screen);
    		
	        new Handler().postDelayed(new Runnable() 
	        {
	 
	            @Override
	            public void run() 
	            {
	                // This method will be executed once the timer is over
	                startMain();
	            }
	        }, SPLASH_TIME_OUT);
    	} 
    	else
        {
    		startMain();	 
    	}
  	

    }

    // Function for getting preferences
    private String getPref(String p)
    {
    	SharedPreferences prefs = SplashScreen.this.getPreferences(MODE_PRIVATE);
    	return prefs.getString(p, null);
    }
    
    // Function for setting preferences
    private void setPref(String p, String v)
    {
    	SharedPreferences prefs = SplashScreen.this.getPreferences(MODE_PRIVATE);
    	prefs.edit().putString(p, v).commit();
    }
    
    private void startMain()
    {
    	// Start your app main activity
        Intent i = new Intent(SplashScreen.this, MainActivity.class);
        startActivity(i);
        // close this activity
        finish();
    }
}
