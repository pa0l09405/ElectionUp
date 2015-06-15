//schermata di benvenuto all'app

package com.example.electionup2;

import htmlrequest.HTMLRequest;

import com.app.official.ElectionUp.R;

import controlloStatoInternet.ControlloConnessioneInternet;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.esci) {
			finish();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {

		String nonAutorizzato = "nonAutorizzato"; // per gestire la risposta 401
													// dal server

		EditText editText1 = (EditText) findViewById(R.id.editText1);
		// /*solo per debugging ---->*/ editText1.setText("snsezione14");
		EditText editText2 = (EditText) findViewById(R.id.editText2);
		// /*solo per debugging ---->*/ editText2.setText("19santino94");
		String username = editText1.getText().toString();// estraggo username
		String password = editText2.getText().toString();// estraggo password

		if (username.isEmpty() || password.isEmpty())
			Toast.makeText(getApplicationContext(), "Compila entrambi i campi",
					Toast.LENGTH_LONG).show();

		else {

			// Messaggio di caricamento
			Toast miotoast = Toast.makeText(getApplicationContext(),
					"Caricamento ...", Toast.LENGTH_SHORT);
			miotoast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
			miotoast.show();

			// controllo connessione
			if (!ControlloConnessioneInternet
					.isNetworkAvailable(getApplicationContext())) {
				Toast.makeText(getApplicationContext(),
						"Connessione internet assente...", Toast.LENGTH_SHORT)
						.show();
			} else {

				/*
				 * documentazione AUTENTICAZIONE : Uri:/oauth
				 * 
				 * Parametri (POST): grant_type=password
				 * client_id=app_rappresentante client_secret=dematdefalsindaci
				 * username=xxxx password=xxx
				 * 
				 * Template risposta: -Accettata: {"access_token":"xxxxxxx",
				 * "expires_in":###, "token_type":"Bearer", "scope":null,
				 * "refresh_token":"xxxxxxx"} -Respinta:
				 * {"type":"http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html"
				 * , "title":"Unauthorized", "status":401, "detail":xxxx}
				 */

				final String URL_BASE = "http://api-electionup.mybluemix.net/";
				final String URL_EXTRA = "oauth";
				final String PARAMS = "grant_type=password&client_id=app_rappresentante&client_secret=dematdefalsindaci&username="
						+ username + "&password=" + password;

				HTMLRequest htlmRequest = new HTMLRequest(URL_BASE + URL_EXTRA,
						PARAMS);

				String htmlRisposta = htlmRequest.getHTML();

				// verifica risposta
				if (htmlRisposta != null
						&& !htmlRisposta.equals(nonAutorizzato)) {

					// conversione JSON
					JSONObject jsonObj = null;
					try {
						jsonObj = new JSONObject(htmlRisposta);
					} catch (JSONException e) {
						Log.e("Autenticazione : ",
								"Errore creazione JSONObject");
						e.printStackTrace();
					}

					if (jsonObj.has("access_token")) {

						String access_token = "", refresh_token = "";
						try {
							access_token = jsonObj.getString("access_token");
							refresh_token = jsonObj.getString("refresh_token");
						}

						catch (JSONException e) {
							e.printStackTrace();
						}

						// passaggio parametro Activity Home
						Intent intent = new Intent(this, Home.class);
						intent.putExtra("access_token", access_token);
						intent.putExtra("refresh_token", refresh_token);
						startActivity(intent);
						finish();

						// toast visualizzato nella home
						Toast.makeText(
								getApplicationContext(),
								"Accesso effettuato\nSe non l'hai fatto precedentemente, inserisci i dati della sezione",
								Toast.LENGTH_LONG).show();

					} else {

						Toast.makeText(getApplicationContext(),
								"Accesso fallito ...", Toast.LENGTH_LONG)
								.show();// messaggio errore

					}
				} else {
					Toast.makeText(getApplicationContext(),
							"Accesso fallito...", Toast.LENGTH_LONG).show();// messaggio
																			// errore
				}
			} // fine ramo else

		}
	}

	@Override
	public void onBackPressed() {
		// disabilito tasto indietro
		;
	}
}
