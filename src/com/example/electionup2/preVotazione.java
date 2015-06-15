package com.example.electionup2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

import com.app.official.ElectionUp.R;

public class preVotazione extends Activity implements OnClickListener{
	
	static String access_token;
	static String refresh_token;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_prevotazione);
		access_token = getIntent().getExtras().getString("access_token");
		refresh_token = getIntent().getExtras().getString("refresh_token");

		// Toast.makeText(getApplicationContext(), access_token,
		// Toast.LENGTH_LONG)
		// .show();
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}
	
	public void onClickAssegnaVotoReal(View v) {
		Intent intent = new Intent(this, SindaciReal.class);
		intent.putExtra("access_token", access_token);
		intent.putExtra("refresh_token", refresh_token);
		startActivity(intent);
		finish();
	}
	public void onClickIndietro(View v) {
		Intent intent = new Intent(preVotazione.this, Home.class);
		Bundle bundle = new Bundle();
		bundle.putString("access_token", access_token);
		bundle.putString("refresh_token", refresh_token);
		intent.putExtras(bundle);
		startActivity(intent);
		finish();
	}
	
	public void onClickDatiProvvisori(View v){
		Intent intent = new Intent(this, DatiProvvisori.class);
		intent.putExtra("access_token", access_token);
		intent.putExtra("refresh_token", refresh_token);
		startActivity(intent);
		finish();
	}
	public void onClick(View v) {

		;
	}
	
	public void onBackPressed() {
		;
	}

	
	
}