package com.gz.family.application;

import android.app.Activity;

import java.util.ArrayList;



public class ActivityManager {

	private ArrayList<Activity> activityList = new ArrayList<Activity>();
	
	private static ActivityManager instance;
	
	private ActivityManager(){
		
	}
	
	public synchronized static ActivityManager getInstance(){
		if(null == instance){
            instance = new ActivityManager();
		}
		return instance;
	}
	
	public Activity getTopActivity(){
		return activityList.get(activityList.size()-1);
	}
	
	public void addActivity(Activity ac){
		if(!activityList.contains(ac)) {
			activityList.add(ac);
		}
	}

	public void removeActivity(Activity activity) {
        if (activityList.contains(activity))
            activityList.remove(activity);
    }
	
	public void removeAllActivity(){
		for(Activity ac:activityList){
			if(null != ac){
				if(!ac.isFinishing()){
					ac.finish();
				}
				ac = null;
			}
		}
		activityList.clear();
	}

	public void exit(){
		int id = android.os.Process.myPid();
		if (id != 0) {
			android.os.Process.killProcess(id);
		}
	}
}
