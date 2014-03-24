package es.fraggel.cpucontrol.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import es.fraggel.cpucontrol.R;
import es.fraggel.cpucontrol.extra.Theme;
import es.fraggel.cpucontrol.service.WakeLockService;

public class MainActivity extends Activity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    Button cpucontrol=null;
    Button keySoft=null;
    CheckBox wakelockChk=null;
    boolean mostrandoSoftK=false;
    @Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.main);


        cpucontrol=(Button) findViewById(R.id.cpuControl);
        cpucontrol.setOnClickListener(this);
        /*keySoft=(Button) findViewById(R.id.keySoft);
        keySoft.setOnClickListener(this);
        comprobarBotonesVirtuales();*/
        wakelockChk=(CheckBox)findViewById(R.id.wakelockChk);
        wakelockChk.setChecked(isMyServiceRunning());
        wakelockChk.setOnCheckedChangeListener(this);
	}

    @Override
    public void onClick(View view) {
        Intent it =null;
        if(view.getId()==R.id.cpuControl){
            it=new Intent(this, CPUActivity.class);
            startActivity(it);
        }
        /*if(view.getId()==R.id.keySoft){

            try {
               Runtime rt = Runtime.getRuntime();
                Process pp = rt.exec("su");
                BufferedOutputStream bos = new BufferedOutputStream(
                        pp.getOutputStream());
                bos.write("mount -o,remount rw /system\n".getBytes());
                Process p = Runtime.getRuntime().exec(new String[]{"su", "-c", "/system/bin/sh"});

                DataOutputStream stdin = new DataOutputStream(p.getOutputStream());
                stdin.writeBytes("busybox sed -n '/qemu.hw.mainkeys/p' /system/build.prop\n");
                DataInputStream stdout = new DataInputStream(p.getInputStream());
                byte[] buffer = new byte[4096];
                int read = 0;
                String out = new String();
                while(true){
                    read = stdout.read(buffer);
                    out += new String(buffer, 0, read);
                    if(read<4096){
                        break;
                    }
                }
                stdin.flush();
                stdin.close();
                stdout.close();

                if(out.indexOf("qemu.hw.mainkeys=0")!=-1){*/
                    //bos.write("busybox sed -i 's/qemu.hw.mainkeys=0.*/qemu.hw.mainkeys=1/g' /system/build.prop\n".getBytes());
                   // cambiarFileGeneric(false);
                   // keySoft.setText(R.string.enable_virtual_keys);
                //}else{

                    //bos.write("busybox sed -i 's/qemu.hw.mainkeys=1.*/qemu.hw.mainkeys=0/g' /system/build.prop\n".getBytes());
             /*       cambiarFileGeneric(true);
                    keySoft.setText(R.string.disable_virtual_keys);
                }
                bos.write(("reboot\n").getBytes());
                bos.flush();
                bos.close();
            }catch(Exception e){

            }
        }*/
    }

    private void cambiarFileGeneric(boolean b) {
        try {
            if(b){
                Runtime rt2 = Runtime.getRuntime();
                Process pp2 = rt2.exec("su");
                BufferedOutputStream bos2 = new BufferedOutputStream(
                        pp2.getOutputStream());
                bos2.write("mount -o,remount rw /system\n".getBytes());
                bos2.write("busybox sed -i 's/key 102 .*/#key 102 MOVE_HOME/g' /system/usr/keylayout/Generic.kl\n".getBytes());
                //bos2.write("busybox sed -i 's/key 139 .*/#key 139 MENU WAKE_DROPPED/g' /system/usr/keylayout/Generic.kl\n".getBytes());
                bos2.write("busybox sed -i 's/key 158 .*/#key 158 BACK WAKE_DROPPED/g' /system/usr/keylayout/Generic.kl\n".getBytes());
                bos2.write("chmod 777 /system/usr/keylayout/Generic.kl\n".getBytes());
                bos2.flush();
                bos2.close();

            }else{
                Runtime rt3 = Runtime.getRuntime();
                Process pp3 = rt3.exec("su");
                BufferedOutputStream bos3 = new BufferedOutputStream(
                        pp3.getOutputStream());
                bos3.write("mount -o,remount rw /system\n".getBytes());
                bos3.write("busybox sed -i 's/#key 102 .*/key 102 MOVE_HOME/g' /system/usr/keylayout/Generic.kl\n".getBytes());
                //bos3.write("busybox sed -i 's/#key 139 .*/key 139 MENU WAKE_DROPPED/g' /system/usr/keylayout/Generic.kl\n".getBytes());
                bos3.write("busybox sed -i 's/#key 158 .*/key 158 BACK WAKE_DROPPED/g' /system/usr/keylayout/Generic.kl\n".getBytes());
                bos3.write("chmod 777 /system/usr/keylayout/Generic.kl\n".getBytes());
                bos3.flush();
                bos3.close();
            }

        }catch(Exception e){

        }
    }

    private void comprobarBotonesVirtuales(){
        try{
            Runtime rt = Runtime.getRuntime();
            Process pp = rt.exec("su");
            BufferedOutputStream bos = new BufferedOutputStream(
                    pp.getOutputStream());

            Process p = Runtime.getRuntime().exec(new String[]{"su", "-c", "/system/bin/sh"});
            DataOutputStream stdin = new DataOutputStream(p.getOutputStream());
            stdin.writeBytes("busybox sed -n '/qemu.hw.mainkeys/p' /system/build.prop\n");
            DataInputStream stdout = new DataInputStream(p.getInputStream());
            byte[] buffer = new byte[4096];
            int read = 0;
            String out = new String();
            while(true){
                read = stdout.read(buffer);
                out += new String(buffer, 0, read);
                if(read<4096){
                    break;
                }
            }
            stdin.flush();
            stdin.close();
            stdout.close();
            if(out.indexOf("qemu.hw.mainkeys=0")!=-1){
                keySoft.setText(R.string.disable_virtual_keys);
            }else{
                keySoft.setText(R.string.enable_virtual_keys);
            }
            bos.flush();
            bos.close();
        }catch(Exception e){

        }

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        Intent intent = new Intent(getBaseContext(), WakeLockService.class);

        if(b) {
            startService(intent);
            SharedPreferences sp =  getBaseContext().getSharedPreferences("configWakeLock", Context.MODE_PRIVATE);
            sp.edit().putBoolean("onboot",true).commit();
        } else {
            stopService(intent);
            SharedPreferences sp =  getBaseContext().getSharedPreferences("configWakeLock", Context.MODE_PRIVATE);
            sp.edit().putBoolean("onboot",false).commit();
        }
    }
    private boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("es.fraggel.cpucontrol.service.WakeLockService".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
