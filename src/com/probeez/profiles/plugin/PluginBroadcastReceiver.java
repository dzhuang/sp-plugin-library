package com.probeez.profiles.plugin;

import static android.app.Activity.RESULT_OK;
import static com.probeez.profiles.plugin.PluginIntent.ACTION_CHECK_CONDITION;
import static com.probeez.profiles.plugin.PluginIntent.ACTION_PERFORM_ACTION;
import static com.probeez.profiles.plugin.PluginIntent.ACTION_QUERY_PLUGIN_STATUS;
import static com.probeez.profiles.plugin.PluginIntent.ACTION_START_PLUGIN;
import static com.probeez.profiles.plugin.PluginIntent.ACTION_STOP_PLUGIN;
import static com.probeez.profiles.plugin.PluginIntent.EXTRA_PLUGIN_STATUS;
import static com.probeez.profiles.plugin.PluginIntent.EXTRA_RULE_TRIGGERED;
import static com.probeez.profiles.plugin.PluginIntent.EXTRA_STATE;
import static com.probeez.profiles.plugin.PluginIntent.EXTRA_STATES;

import java.util.Set;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public abstract class PluginBroadcastReceiver extends BroadcastReceiver {
	public static final int STATUS_UNKNOWN = -1;	
	public static final int STATUS_STARTED = 0;
	public static final int STATUS_STOPPED = 1;
	static final String TAG = "SPPlugin";
	/** to enable debug run 'adb shell setprop log.tag.SPPlugin DEBUG' and reinstall/restart a plugin */
	static final boolean DEBUG = Log.isLoggable(TAG, Log.DEBUG); 

	
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (DEBUG) Log.d(TAG, "Got SP broadcast:"+action);
		if (ACTION_START_PLUGIN.equals(action)) {
			onStartPlugin(context);
		} 
		else if (ACTION_STOP_PLUGIN.equals(action)) {
			onStopPlugin(context);
		} 
		else if (ACTION_QUERY_PLUGIN_STATUS.equals(action)) {
			int status = onQueryPluginStatus(context);
			Bundle results = new Bundle();
			results.putInt(EXTRA_PLUGIN_STATUS, status);
			setResult(RESULT_OK, null, results);
		} 
		else if (ACTION_CHECK_CONDITION.equals(action)) {
			Bundle results = onCheckConditionList(context, 
					intent.getBundleExtra(EXTRA_STATE), 
					intent.getBundleExtra(EXTRA_STATES)
				);
			setResult(RESULT_OK, null, results);
		} 
		else if (ACTION_PERFORM_ACTION.equals(action)) {
			boolean triggered = intent.getBooleanExtra(EXTRA_RULE_TRIGGERED, true);
			Bundle state = intent.getBundleExtra(EXTRA_STATE);
			if (DEBUG) Log.d(TAG, "Calling onPerformAction, rule triggered:"+triggered);
			onPerformAction(context, state, triggered);
		}
	}

  protected Bundle onCheckConditionList(Context context, Bundle sourceState, Bundle targetStates) {
		Set<String> keys = targetStates.keySet();
		Bundle results = new Bundle(keys.size());
		for (String key : keys) {
			Bundle targetState = targetStates.getBundle(key);
			boolean result = onCheckCondition(context, sourceState, targetState);
			if (DEBUG) Log.d(TAG, "Check condition:"+key+", result:"+result);
			results.putBoolean(key, result);
		}
		return results;
  }

	protected boolean onCheckCondition(Context context, Bundle sourceState, Bundle targetState) {
  	return false;
  }
  
  protected void onPerformAction(Context context, Bundle state, boolean triggered) {
  }
  
  protected void onStartPlugin(Context context) {
  }

  protected void onStopPlugin(Context context) {
  }
  
  protected int onQueryPluginStatus(Context context) {
  	return STATUS_UNKNOWN;
  }
	
}
