package com.probeez.profiles.plugin;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;

public class PluginIntent extends Intent {
	public static final String ACTION_EDIT_CONDITION = "com.probeez.profiles.plugin.EDIT_CONDITION";
	public static final String ACTION_EDIT_ACTION = "com.probeez.profiles.plugin.EDIT_ACTION";
	public static final String ACTION_TRIGGER_CONDITION = "com.probeez.profiles.plugin.TRIGGER_CONDITION";
	public static final String ACTION_CHECK_CONDITION = "com.probeez.profiles.plugin.CHECK_CONDITION";
	public static final String ACTION_PERFORM_ACTION = "com.probeez.profiles.plugin.PERFORM_ACTION";
	public static final String ACTION_START_PLUGIN = "com.probeez.profiles.plugin.START_PLUGIN";
	public static final String ACTION_STOP_PLUGIN = "com.probeez.profiles.plugin.STOP_PLUGIN";
	public static final String ACTION_QUERY_PLUGIN_STATUS = "com.probeez.profiles.plugin.QUERY_PLUGIN_STATUS";
	
	public static final String EXTRA_SUMMARY = "summary";
	public static final String EXTRA_STATE = "state";
	public static final String EXTRA_STATES = "states";
	public static final String EXTRA_RULE_TRIGGERED = "rule_triggered";
	public static final String EXTRA_PLUGIN_STATUS = "plugin_status";
	public static final String EXTRA_PLUGIN_PACKAGE = "plugin_package";
	
	protected transient Context mContext;
		

	public PluginIntent(Context context) {
		mContext = context;
		setPackage("com.probeez.profiles");
		ApplicationInfo appInfo = context.getApplicationInfo();
		putExtra(EXTRA_PLUGIN_PACKAGE, appInfo.packageName);
	}
	
	public PluginIntent(Context context, String action) {
		this(context);
		setAction(action);
	}

	public Bundle getState() {
		Bundle state = getBundleExtra(EXTRA_STATE);
		if (state==null) {
			state = new Bundle();
			putExtra(EXTRA_STATE, state);
		}
		return state;
	}

	public void send() {
		mContext.sendBroadcast(this);
	}
	
}
