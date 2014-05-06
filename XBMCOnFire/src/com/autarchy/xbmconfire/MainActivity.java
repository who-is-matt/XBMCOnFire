package com.autarchy.xbmconfire;

import java.util.ArrayList;
import java.util.List;

import com.autarchy.xbmconfire.R;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.LayoutParams;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity 
{

    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
    	super.onCreate(savedInstanceState);
    	
    	// Assign string references to variables for convenience
        final String XBMC_NAME = getString(R.string.xbmc_name);
        final String XBMC_PACKAGE = getString(R.string.xbmc_package);
        final String SPMC_NAME = getString(R.string.spmc_name);
        final String SPMC_PACKAGE = getString(R.string.spmc_package);
        final String OUYA_NAME = getString(R.string.ouya_name);
        final String OUYA_PACKAGE = getString(R.string.ouya_package);
        
        // Create arrays for detected installed versions of XBMC
        final List<String> installedVersionName = new ArrayList<String>();
        final List<String> installedVersionPackage = new ArrayList<String>();	// Must be final
       
        // Check for user-specified version to launch
        String defaultVersion = getPref("defaultPackage");
   
        if (defaultVersion != null) 
        {
        	Intent launchIntent = getPackageManager().getLaunchIntentForPackage(defaultVersion);
        	startActivity(launchIntent);
	    	finish();
        }
        
        // If no version has been specified by the user, check for installed versions of XBMC
        if (isAppInstalled(XBMC_PACKAGE))
        {
        	installedVersionName.add(XBMC_NAME);
        	installedVersionPackage.add(XBMC_PACKAGE);
        }
        if (isAppInstalled(SPMC_PACKAGE))
        {
        	installedVersionName.add(SPMC_NAME);
        	installedVersionPackage.add(SPMC_PACKAGE);
        }
        if (isAppInstalled(OUYA_PACKAGE))
        {
        	installedVersionName.add(OUYA_NAME);
        	installedVersionPackage.add(OUYA_PACKAGE);
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
	        		
	        		if (idx >= 0)
	        		{
	        			launchIntent = getPackageManager().getLaunchIntentForPackage(installedVersionPackage.get(idx));
	        			if (rememberVersion.isChecked()) 
	        				setPref("defaultPackage", installedVersionPackage.get(idx));
	        			startActivity(launchIntent);
	        	    	finish();	// ensures that this app is closed once the job is done
	        	    }
	        	    else 
	        	    	Toast.makeText(getApplicationContext(), R.string.version_notselected, Toast.LENGTH_LONG).show();
	        	    
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

/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
*/
    
/*    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
*/
    /**
     * A placeholder fragment containing a simple view.
     */
/*
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }
*/
}
