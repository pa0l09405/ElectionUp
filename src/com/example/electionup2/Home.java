package com.example.electionup2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.app.official.ElectionUp.R;

import controlloStatoInternet.ControlloConnessioneInternet;

public class Home extends Activity implements OnClickListener {
	private String access_token;
	private String refresh_token;
	
	final private String messaggio =" Con questa funzionalità puoi inviare report dei voti "
			                         +"raccolti durante lo spoglio.\nAttenzione: in ogni report " 
			                         +"successivo al primo NON vanno contati i voti già inviati "
			                         +"in report precedenti.\nEs: se per un candidato sono stati inviati 6 voti " 
			                         +"in un primo report e al momento dell'invio successivo, sul report " 
			                         +"cartaceo, " 
			                         +"si contano per lui 10 voti, bisogna inviarne altri 4 (e non 10!). "
                                     +"Inoltre, è previsto anche l'invio di un numero negativo di voti " 
                                     +"(preceduti dal segno -) per correggere eventuali voti di troppo "
                                     +"inviati "
                                     +"in report precedenti.";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		// estraggo access_token e refresh_token da Main Activity
		access_token = getIntent().getExtras().getString("access_token");
		refresh_token = getIntent().getExtras().getString("refresh_token");
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	public void onClickRisultati(View v) {

		// controllo connessione
		if (ControlloConnessioneInternet
				.isNetworkAvailable(getApplicationContext())) {

			// Passaggio parametri e apertura di Risultati
			Intent intent = new Intent(this, Risultati.class);
			intent.putExtra("access_token", access_token);
			intent.putExtra("refresh_token", refresh_token);
			startActivity(intent);
			finish();
		}
		// messaggio errore
		else
			Toast.makeText(getApplicationContext(),
					"Connessione internet assente...", Toast.LENGTH_SHORT)
					.show();
	}

	@Override
	public void onClick(View v) {
		// controllo connessione
		if (ControlloConnessioneInternet
				.isNetworkAvailable(getApplicationContext())) {

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(messaggio)
			       .setCancelable(false).setPositiveButton("Ho letto", 
			    		                                    new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	               vaiAiReport();
	       		   finish();    
	               }
	           });
			      
			AlertDialog alert = builder.create();
			alert.show();
			
			
		} else {
			// errore
			Toast miotoast = Toast.makeText(getApplicationContext(),
					"Connessione internet assente...", Toast.LENGTH_SHORT);
			miotoast.setGravity(Gravity.CENTER, 0, 0);
			miotoast.show();
		}
	}

	public void onClickAssegnaVotoReal(View v) {
		Intent intent = new Intent(this, preVotazione.class);
		intent.putExtra("access_token", access_token);
		intent.putExtra("refresh_token", refresh_token);
		startActivity(intent);
		finish();
	}

	public void onClickVotanti(View v) {
		// controllo connessione
		if (ControlloConnessioneInternet
				.isNetworkAvailable(getApplicationContext())) {

			// Passaggio parametri e apertura di Votanti
			Intent intent = new Intent(this, Votanti.class);
			intent.putExtra("access_token", access_token);
			intent.putExtra("refresh_token", refresh_token);
			startActivity(intent);
			finish();
		}
		// messaggio errore
		else
			Toast.makeText(getApplicationContext(),
					"Connessione internet assente...", Toast.LENGTH_SHORT)
					.show();
	}

	@Override
	public void onBackPressed() {
		; // disabilito tasto indietro
	}

	public void onClickLogout(View v) {

		// effettuo il logout
		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		startActivity(intent);
		finish();

	}


	private void vaiAiReport(){
		//passaggio parametri e vai ad Assegna Voto
		Intent intent= new Intent (this,AssegnaVoto.class);
		intent.putExtra("access_token", access_token);
        intent.putExtra("refresh_token", refresh_token);
		startActivity(intent);
		
	}





}
