package com.autarchy.xbmcforfiretv;

import java.util.ArrayList;
import java.util.List;

import com.autarchy.xbmcforfiretv.R;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;
import android.os.Build;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

    	
    	// Assign string references to variables for convenience
        String xbmcName = getString(R.string.xbmc_name);
        String xbmcPackage = getString(R.string.xbmc_package);
        String spmcName = getString(R.string.spmc_name);
        String spmcPackage = getString(R.string.spmc_package);
        String ouyaName = getString(R.string.ouya_name);
        String ouyaPackage = getString(R.string.ouya_package);
        
        // Initalize arrays for detected installed versions of XBMC
        // TODO: Maybe combine these lists into a 2D array to decrease the chance that the names and packages will
        // 		get out of sync?
        List<String> installedVersionName = new ArrayList<String>();
        List<String> installedVersionPackage = new ArrayList<String>();
       
        // Check for user-specified version to launch
        String defaultVersion = getPref("defaultPackage");
   
        if (defaultVersion != null) 
        {
        	Intent launchIntent = getPackageManager().getLaunchIntentForPackage(defaultVersion);
        	startActivity(launchIntent);
	    	finish();
        }
        
        // If no version has been specified by the user, check for installed versions of XBMC
        if (isAppInstalled(xbmcPackage))
        {
        	installedVersionName.add(xbmcName);
        	installedVersionPackage.add(xbmcPackage);
        }
        if (isAppInstalled(spmcPackage))
        {
        	installedVersionName.add(spmcName);
        	installedVersionPackage.add(spmcPackage);
        }
        if (isAppInstalled(ouyaPackage))
        {
        	installedVersionName.add(ouyaName);
        	installedVersionPackage.add(ouyaPackage);
        }
        // If no versions of XBMC are installed, display the no_xbmc view, which contains
        // a link and QR code for the XBMC wiki page for the Amazon Fire TV
        if (installedVersionName.isEmpty())
        {
        	setContentView(R.layout.no_xbmc);
        	Button exitXbmc = (Button) findViewById(R.id.buttonExit);
	        exitXbmc.setOnClickListener(new OnClickListener()
	        {
	        	public void onClick(View w)
	        	{
	        		finish();	// ensures that this app is closed once the job is done
	        	}
	        });
        }
        // If a single version of XBMC is found, automatically launch it then exit
        else if (installedVersionName.size() == 1)
        {
        	Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage(installedVersionPackage.get(0));
        	startActivity(LaunchIntent);
        	finish();	// ensures that this app is closed once the job is done
        }
        // If multiple versions are found, populate the version_select view to allow the
        // user to select the desired version to launch
        else
        {
        	setContentView(R.layout.version_select);      	

        	// Populate the radio group in the version_select view with the installed versions of XBMC
	        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroupVersions);
	        RadioGroup.LayoutParams rprms;
	        CheckBox cb = (CheckBox) findViewById(R.id.checkBoxRemember);
	        for(int i=0;i<installedVersionName.size();i++)
	        {
	              RadioButton radioButton = new RadioButton(this);
	              radioButton.setText(installedVersionName.get(i));
	              radioButton.setId(i);
	              radioButton.setTextColor(cb.getTextColors());
	              rprms = new RadioGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	              radioGroup.addView(radioButton, rprms);
	        }


	        /* *** This section is not currently used, but might prove useful in the future	        
	        radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() 
	        {
	            public void onCheckedChanged(RadioGroup group, int checkedId) 
	            {
	            	
	            }
	        });
	        *** */
	        
	        // Set a listener for the buttonStart button:
	        //    If no radio button is selected, display an error message in a toast
	        //    If a radio button is selected, launch the specified version of XBMC
	        //    If a radio button is selected and the check box is checked, save the
	        //       user's preference, then launch the specified version.
	        Button startXbmc = (Button) findViewById(R.id.buttonStart);
	        startXbmc.setOnClickListener(new OnClickListener()
	        {
	        	public void onClick(View w)
	        	{
	        		RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroupVersions);
	        		int radioButtonID = radioGroup.getCheckedRadioButtonId();
	        		View radioButton = radioGroup.findViewById(radioButtonID);
	        		int idx = radioGroup.indexOfChild(radioButton);
	        		
	        		CheckBox rememberVersion = (CheckBox) findViewById(R.id.checkBoxRemember);
	        		
	        		Intent launchIntent = null;
	        		
	        		// TODO: This section could be simplified if the separate lists were combined into a 2D list
	        		if (idx == 0) 
	        		{
	        			launchIntent = getPackageManager().getLaunchIntentForPackage(getString(R.string.xbmc_package));
	        	    	if (rememberVersion.isChecked()) 
	        	    		setPref("defaultPackage", getString(R.string.xbmc_package));
	        		}
	        		else if (idx == 1)
	        		{
	        			launchIntent = getPackageManager().getLaunchIntentForPackage(getString(R.string.spmc_package));
	        	    	if (rememberVersion.isChecked()) 
	        	    		setPref("defaultPackage", getString(R.string.spmc_package));
	        		}
	        		else if (idx == 2)
	        		{
	        			launchIntent = getPackageManager().getLaunchIntentForPackage(getString(R.string.ouya_package));
	        	    	if (rememberVersion.isChecked()) 
	        	    		setPref("defaultPackage", getString(R.string.ouya_package));
	        		}
	        		
	        	    if (launchIntent != null)
	        	    {
	        	    	startActivity(launchIntent);
	        	    	finish();	// ensures that this app is closed once the job is done
	        	    }
	        	    else Toast.makeText(getApplicationContext(), R.string.version_notselected, Toast.LENGTH_LONG).show();
	        	    
	        	}
	        });
	        

        }
 
    }
    
    
    // Function to check if a specified package is installed
    private boolean isAppInstalled(String packageName) 
    {
        PackageManager pm = getPackageManager();
        boolean installed = false;
        try {
           pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
           installed = true;
        } catch (PackageManager.NameNotFoundException e) {
           installed = false;
        }
        return installed;
    }
    
    
    // Function for getting preferences
    // Required to prevent scope issues
    private String getPref(String p)
    {
    	SharedPreferences prefs = MainActivity.this.getPreferences(MODE_PRIVATE);
    	return prefs.getString(p, null);
    }
    
    // Function for setting preferences
    // Required to prevent scope issues
    private void setPref(String p, String v)
    {
    	SharedPreferences prefs = MainActivity.this.getPreferences(MODE_PRIVATE);
    	prefs.edit().putString(p, v).commit();
    }

}
