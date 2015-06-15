package com.example.electionup2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;

import com.app.official.ElectionUp.R;

public class Risultati extends Activity implements OnClickListener {

	private String access_token;
	private String refresh_token;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_risultati);
		// estraggo paramentri da Home
		access_token = getIntent().getExtras().getString("access_token");
		refresh_token = getIntent().getExtras().getString("refresh_token");
		// abilito WebView
		WebView webview = (WebView) findViewById(R.id.webview);
		webview.getSettings().setJavaScriptEnabled(true);
		webview.loadUrl("http://stats-electionup.mybluemix.net/");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.risultati, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// messaggio
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Sei sicuro di voler tornare alla home?\n")
				.setCancelable(false)
				.setPositiveButton("Si", new DialogInterface.OnClickListener() {
					// si attiva al si
					public void onClick(DialogInterface dialog, int id) {

						tornahome();// passaggio parametri e ritorno alla home
						finish();
					}
				})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();

	}

	public void tornahome() {
		// passaggio parametri e ritorno alla home
		Intent intent = new Intent(this, Home.class);
		intent.putExtra("access_token", access_token);
		intent.putExtra("refresh_token", refresh_token);
		startActivity(intent);

	}

	@Override
	public void onBackPressed() {
		;// disabilito tasto indietro
	}

}
