package es.fraggel.cpucontrol.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import es.fraggel.cpucontrol.service.WakeLockService;

public class WakeLockBroadcastReceiver extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent i) {
		
		SharedPreferences sp =  context.getSharedPreferences("configWakeLock", Context.MODE_PRIVATE);
		boolean onboot = sp.getBoolean("onboot", false);
        //Toast.makeText(context, "WakeLock broadcast iniciado "+ onboot, Toast.LENGTH_SHORT).show();
		if(onboot) {
			Intent intent = new Intent(context, WakeLockService.class);
			context.startService(intent);
		}
	}
}