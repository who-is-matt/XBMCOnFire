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
 
        String xbmcName = getString(R.string.xbmc_name);
        String xbmcPackage = getString(R.string.xbmc_package);
        String spmcName = getString(R.string.spmc_name);
        String spmcPackage = getString(R.string.spmc_package);
        String ouyaName = getString(R.string.ouya_name);
        String ouyaPackage = getString(R.string.ouya_package);
        
        List<String> installedVersionName = new ArrayList<String>();
        List<String> installedVersionPackage = new ArrayList<String>();
       
        String defaultVersion = getDefault();
   
        if (defaultVersion != null) 
        {
        	Intent launchIntent = getPackageManager().getLaunchIntentForPackage(defaultVersion);
        	startActivity(launchIntent);
	    	finish();
        }
        
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
        
        if (installedVersionName.isEmpty())
        {
        	setContentView(R.layout.no_xbmc);
        	Button exitXbmc = (Button) findViewById(R.id.buttonExit);
	        exitXbmc.setOnClickListener(new OnClickListener()
	        {
	        	public void onClick(View w)
	        	{
	        		finish();
	        	}
	        });
        	
        }
        else if (installedVersionName.size() == 1)
        {
        	Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage(installedVersionPackage.get(0));
        	startActivity(LaunchIntent);
        	finish();
        }
        else
        {
        	setContentView(R.layout.version_select);      	

	        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroupVersions);
	        RadioGroup.LayoutParams rprms;

	        for(int i=0;i<installedVersionName.size();i++)
	        {
	              RadioButton radioButton = new RadioButton(this);
	              radioButton.setText(installedVersionName.get(i));
	              radioButton.setId(i);
	              rprms = new RadioGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	              radioGroup.addView(radioButton, rprms);
	        }
	        
	        radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() 
	        {
	            public void onCheckedChanged(RadioGroup group, int checkedId) 
	            {
	                
	                
	            }
	        });
	        
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
	        		
	        		if (idx == 0) 
	        		{
	        			launchIntent = getPackageManager().getLaunchIntentForPackage(getString(R.string.xbmc_package));
	        	    	if (rememberVersion.isChecked()) 
	        	    		setDefault(getString(R.string.xbmc_package));
	        		}
	        		else if (idx == 1)
	        		{
	        			launchIntent = getPackageManager().getLaunchIntentForPackage(getString(R.string.spmc_package));
	        	    	if (rememberVersion.isChecked()) 
	        	    		setDefault(getString(R.string.spmc_package));
	        		}
	        		else if (idx == 2)
	        		{
	        			launchIntent = getPackageManager().getLaunchIntentForPackage(getString(R.string.ouya_package));
	        	    	if (rememberVersion.isChecked()) 
	        	    		setDefault(getString(R.string.ouya_package));
	        		}
	        		
	        	    if (launchIntent != null)
	        	    {
	        	    	startActivity(launchIntent);
	        	    	finish();
	        	    }
	        	    else Toast.makeText(getApplicationContext(), R.string.version_notselected, Toast.LENGTH_LONG).show();
	        	    
	        	}
	        });
	        

        }
 
    }
    
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
    
    private String getDefault()
    {
    	SharedPreferences prefs = MainActivity.this.getPreferences(MODE_PRIVATE);
    	return prefs.getString("defaultPackage", null);
    }
    
    private void setDefault(String defaultPackage)
    {
    	SharedPreferences prefs = MainActivity.this.getPreferences(MODE_PRIVATE);
    	prefs.edit().putString("defaultPackage", defaultPackage).commit();
    }

}
