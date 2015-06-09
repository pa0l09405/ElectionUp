package com.example.electionup2;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.app.official.ElectionUp.R;

@SuppressWarnings("deprecation")
public class AssegnaVoto extends TabActivity implements OnClickListener
{
	private  String access_token;
	private  String refresh_token;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_assegna_voto);
		//estraggo parametri dalla Home
		access_token = getIntent().getExtras().getString("access_token"); 
		refresh_token= getIntent().getExtras().getString("refresh_token"); 
		
		//creo tab
		TabHost tabHost = getTabHost();
		//al cambio di tab
		/*
		tabHost.setOnTabChangedListener(new OnTabChangeListener() {
					@Override
					public void onTabChanged(String tabId) {
						//messaggio al cambio di tab
						Toast.makeText(getApplicationContext(), "Ricorda di confermare i voti per ogni schermata!", Toast.LENGTH_LONG)
		    	        .show();
					}
		});*/
		
		
		 //SCHEDA 1

		TabSpec spec1 = tabHost.newTabSpec("Scheda 1");
		spec1.setIndicator("Sindaci/"
				+ "Presidenti");//titolo tab 1
		//1. Ho definito il mio oggetto TabSpec
		
		//passo parametri a Sindaci
		Intent intent1 = new Intent(this, Sindaci.class);
		intent1.putExtra("access_token", access_token);
        intent1.putExtra("refresh_token", refresh_token);

		spec1.setContent(intent1);
		tabHost.addTab(spec1);
 
		//SCHEDA 2
		
		TabSpec spec2 = tabHost.newTabSpec("Scheda 2");
		spec2.setIndicator("Liste");//titolo tab2
		//2. Ho definito il mio oggetto TabSpec
		//passo parametri a Liste
		Intent intent2 = new Intent(this, Liste.class);
		intent2.putExtra("access_token", access_token);
        intent2.putExtra("refresh_token", refresh_token);
		spec2.setContent(intent2);
		tabHost.addTab(spec2);
 
		//SCHEDA 3
		
		TabSpec spec3 = tabHost.newTabSpec("Scheda 3");
		spec3.setIndicator("Consiglieri");//titolo tab3 
		//2. Ho definito il mio oggetto TabSpec
		//passo parametri a Consiglieri
		Intent intent3 = new Intent(this, Consiglieri.class);
		intent3.putExtra("access_token", access_token);
        intent3.putExtra("refresh_token", refresh_token);
		spec3.setContent(intent3);
		tabHost.addTab(spec3);
		
		//fisso dimensione testo a 10
		for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
	        TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
	        tv.setTextSize(10);}
	    }


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.assegna_voto, menu);
		return true;
	}


	@Override
	public void onClick(View v) {
		;//disabilito tasto indietro
	}
}