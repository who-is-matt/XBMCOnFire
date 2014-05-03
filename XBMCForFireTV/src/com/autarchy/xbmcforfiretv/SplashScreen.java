package com.autarchy.xbmcforfiretv;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends Activity {
	
	/*
	 * This class exists solely because the Amazon App Store requires a minimum of 3 screenshots, but
	 * the app would otherwise have only two screens.  This splashsceen is intended to display only
	 * on first run.
	 */

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    	// If first run, show splashscreen and then set firstRun pref to false
    	if (getPref("firstRun") == null)
    	{
    		setContentView(R.layout.activity_splash_screen);

    		setPref("firstRun","false");
	 
	        new Handler().postDelayed(new Runnable() {
	 
	            @Override
	            public void run() {
	                // This method will be executed once the timer is over
	                // Start your app main activity
	                Intent i = new Intent(SplashScreen.this, MainActivity.class);
	                startActivity(i);
	 
	                // close this activity
	                finish();
	            }
	        }, SPLASH_TIME_OUT);
    	}
    	else
    	{
    		Intent i = new Intent(SplashScreen.this, MainActivity.class);
            startActivity(i);
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
    
}
