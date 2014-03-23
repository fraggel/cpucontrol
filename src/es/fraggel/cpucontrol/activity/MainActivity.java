package es.fraggel.cpucontrol.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import es.fraggel.cpucontrol.R;
import es.fraggel.cpucontrol.extra.Theme;

public class MainActivity extends Activity implements View.OnClickListener{
    Button cpucontrol=null;
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
	}

    @Override
    public void onClick(View view) {
        Intent it =null;
        if(view.getId()==R.id.cpuControl){
            it=new Intent(this, CPUActivity.class);
            startActivity(it);
        }
    }
}
