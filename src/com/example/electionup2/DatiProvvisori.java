package com.example.electionup2;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.app.official.ElectionUp.R;

public class DatiProvvisori extends TabActivity implements OnClickListener {
	String access_token;
	String refresh_token;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dati_provvisori);
		access_token = getIntent().getExtras().getString("access_token");
		refresh_token = getIntent().getExtras().getString("refresh_token");

		// Toast.makeText(getApplicationContext(), access_token+refresh_token,
		// Toast.LENGTH_LONG)
		// .show();

		TabHost tabHost = getTabHost();

		TabSpec spec1 = tabHost.newTabSpec("Scheda 1");
		spec1.setIndicator("Sindaci");
		Intent intent1 = new Intent(this, DatiProvvSindaci.class);
		intent1.putExtra("access_token", access_token);
		intent1.putExtra("refresh_token", refresh_token);
		spec1.setContent(intent1);
		tabHost.addTab(spec1);

		TabSpec spec2 = tabHost.newTabSpec("Scheda 2");
		spec2.setIndicator("Liste");
		Intent intent2 = new Intent(this, DatiProvvListe.class);
		intent2.putExtra("access_token", access_token);
		intent2.putExtra("refresh_token", refresh_token);
		spec2.setContent(intent2);
		tabHost.addTab(spec2);

		TabSpec spec3 = tabHost.newTabSpec("Scheda 3");
		spec3.setIndicator("Consiglieri");
		Intent intent3 = new Intent(this, DatiProvvConsiglieri.class);

		intent3.putExtra("access_token", access_token);
		intent3.putExtra("refresh_token", refresh_token);
		spec3.setContent(intent3);
		tabHost.addTab(spec3);
	}

	public void onClickIndietro(View v) {
		Intent intent = new Intent(DatiProvvisori.this, preVotazione.class);
		Bundle bundle = new Bundle();
		bundle.putString("access_token", access_token);
		bundle.putString("refresh_token", refresh_token);
		intent.putExtras(bundle);
		startActivity(intent);
		finish();
	}

	@Override
	public void onBackPressed() {
		;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		;
	}
}