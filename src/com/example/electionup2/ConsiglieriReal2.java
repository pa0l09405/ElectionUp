package com.example.electionup2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.app.official.ElectionUp.R;
import com.example.electionup2.DBConsiglieri;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.Toast;

public class ConsiglieriReal2 extends Activity implements OnClickListener {

	private DBSindaci db;
	private DBListe db2;
	private DBConsiglieri db3;
	private String access_token;
	private String refresh_token;
	private static int contatore_invio;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_consiglieri_real2);

		db = new DBSindaci(getApplicationContext());
		db.open();
		db2 = new DBListe(getApplicationContext());
		db2.open();
		db3 = new DBConsiglieri(getApplicationContext());
		db3.open();

		access_token = getIntent().getExtras().getString("access_token");
		refresh_token = getIntent().getExtras().getString("refresh_token");

		TableLayout layout = (TableLayout) findViewById(R.id.table2);
		Button bottone1 = new Button(this);
		bottone1.setHintTextColor(Color.RED);
		bottone1.setHint("Nessuna preferenza");
		bottone1.setHeight(350);
		bottone1.setTextSize(35);
		layout.addView(bottone1);
		bottone1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Intent intent = new Intent(ConsiglieriReal2.this,
						SindaciReal.class);
				Bundle bundle = new Bundle();
				bundle.putString("access_token", access_token);
				bundle.putString("refresh_token", refresh_token);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		visualizzaConsiglieri();
		contatore_invio = getIntent().getIntExtra("contatore", -1);
		contatore_invio++;
		if (contatore_invio == 50) {
			contatore_invio = 0;
			inviaDati();
		}
	}

	public void onBackPressed() {
		;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		;
	}

	public void inviaListe() {
		if (db2.fetchListe().getCount() != 0) {
		String jsonString;
		JSONArray jsonob = new JSONArray();
		Cursor c = db2.fetchListe(); // query
		int idCol = c.getColumnIndex(DBListe.ListeMetaData.ID_LISTA);
		int votiCol = c.getColumnIndex(DBListe.ListeMetaData.VOTI_LISTA);
		try {

			if (c.moveToFirst()) { // se va alla prima entry, il cursore non è
									// vuoto
				do {
					jsonob.put(new JSONObject()
							.put("lista", c.getString(idCol)).put(
									"numero_voti", c.getString(votiCol)));

				} while (c.moveToNext()); // iteriamo al prox elemento
			}
		} catch (JSONException e) {
			Log.i("costruzione json per invio report : ", "Errore JSON");
			e.printStackTrace();
		}

		jsonString = jsonob.toString();
		String URL_BASE = "http://api-electionup.mybluemix.net/";
		String URL_EXTRA = "report/store";
		String PARAMS = "access_token=" + access_token + "&report="
				+ jsonString;

		HTMLRequest htmlRequest = new HTMLRequest(URL_BASE + URL_EXTRA, PARAMS);
		String htmlResponse = htmlRequest.getHTML();

		if (htmlResponse != null && !htmlResponse.equals("nonAutorizzato")) { // se
																				// è
																				// andato
																				// tutto
																				// bene

			Log.i("Activity votanti, risposta all'invito dati :", htmlResponse);

			// conversione JSON
			JSONObject jsonObj1 = null;
			try {
				jsonObj1 = new JSONObject(htmlResponse);

			} catch (JSONException e) {
				Log.e("invio report : ", "Errore creazione JSONObject");
				e.printStackTrace();
			}

			try {
				if (jsonObj1.getBoolean("success"))
					Toast.makeText(getApplicationContext(),
							jsonObj1.getString("message"), Toast.LENGTH_SHORT)
							.show();
				else
					Toast.makeText(getApplicationContext(),
							"errore inserimento report sindaci",
							Toast.LENGTH_SHORT).show();

			} catch (JSONException e) {
				Log.e("invio report : ", "Errore creazione JSONObject");
				e.printStackTrace();
			}

		}
		azzeraListe();
		}
	}

	public void inviaDati() {
		inviaSindaci();
		inviaListe();
		inviaConsiglieri();
	}

	public void inviaSindaci() {
		String jsonString;
		JSONArray jsonob = new JSONArray();
		Cursor c = db.fetchSindaci(); // query
		int idCol = c.getColumnIndex(DBSindaci.SindacoMetadata.ID_SINDACO);
		int votiCol = c.getColumnIndex(DBSindaci.SindacoMetadata.VOTI_SINDACO);
		try {

			if (c.moveToFirst()) { // se va alla prima entry, il cursore non è
									// vuoto
				do {
					jsonob.put(new JSONObject().put("sindaco",
							c.getString(idCol)).put("numero_voti",
							c.getString(votiCol)));

				} while (c.moveToNext()); // iteriamo al prox elemento
			}
		} catch (JSONException e) {
			Log.i("costruzione json per invio report : ", "Errore JSON");
			e.printStackTrace();
		}

		jsonString = jsonob.toString();
		String URL_BASE = "http://api-electionup.mybluemix.net/";
		String URL_EXTRA = "report/store";
		String PARAMS = "access_token=" + access_token + "&report="
				+ jsonString;

		HTMLRequest htmlRequest = new HTMLRequest(URL_BASE + URL_EXTRA, PARAMS);
		String htmlResponse = htmlRequest.getHTML();

		if (htmlResponse != null && !htmlResponse.equals("nonAutorizzato")) { // se
																				// è
																				// andato
																				// tutto
																				// bene

			Log.i("Activity votanti, risposta all'invito dati :", htmlResponse);

			// conversione JSON
			JSONObject jsonObj1 = null;
			try {
				jsonObj1 = new JSONObject(htmlResponse);

			} catch (JSONException e) {
				Log.e("invio report : ", "Errore creazione JSONObject");
				e.printStackTrace();
			}

			try {
				if (jsonObj1.getBoolean("success"))
					Toast.makeText(getApplicationContext(),
							jsonObj1.getString("message"), Toast.LENGTH_SHORT)
							.show();
				else
					Toast.makeText(getApplicationContext(),
							"errore inserimento report sindaci",
							Toast.LENGTH_SHORT).show();

			} catch (JSONException e) {
				Log.e("invio report : ", "Errore creazione JSONObject");
				e.printStackTrace();
			}

		}
		azzeraSindaci();
	}

	public void inviaConsiglieri() {
		if (db3.fetchConsiglieri().getCount() != 0) {
		String jsonString;
		JSONArray jsonob = new JSONArray();
		Cursor c = db3.fetchConsiglieri(); // query
		int idCol = c
				.getColumnIndex(DBConsiglieri.ConsiglieriMetaData.ID_CONSIGLIERE);
		int votiCol = c
				.getColumnIndex(DBConsiglieri.ConsiglieriMetaData.VOTI_CONSIGLIERE);
		try {

			if (c.moveToFirst()) { // se va alla prima entry, il cursore non è
									// vuoto
				do {
					jsonob.put(new JSONObject().put("consigliere",
							c.getString(idCol)).put("numero_voti",
							c.getString(votiCol)));

				} while (c.moveToNext()); // iteriamo al prox elemento
			}
		} catch (JSONException e) {
			Log.i("costruzione json per invio report : ", "Errore JSON");
			e.printStackTrace();
		}

		jsonString = jsonob.toString();
		String URL_BASE = "http://api-electionup.mybluemix.net/";
		String URL_EXTRA = "report/store";
		String PARAMS = "access_token=" + access_token + "&report="
				+ jsonString;

		HTMLRequest htmlRequest = new HTMLRequest(URL_BASE + URL_EXTRA, PARAMS);
		String htmlResponse = htmlRequest.getHTML();

		if (htmlResponse != null && !htmlResponse.equals("nonAutorizzato")) { // se
																				// è
																				// andato
																				// tutto
																				// bene

			Log.i("Activity votanti, risposta all'invito dati :", htmlResponse);

			// conversione JSON
			JSONObject jsonObj1 = null;
			try {
				jsonObj1 = new JSONObject(htmlResponse);

			} catch (JSONException e) {
				Log.e("invio report : ", "Errore creazione JSONObject");
				e.printStackTrace();
			}

			try {
				if (jsonObj1.getBoolean("success"))
					Toast.makeText(getApplicationContext(),
							jsonObj1.getString("message"), Toast.LENGTH_SHORT)
							.show();
				else
					Toast.makeText(getApplicationContext(),
							"errore inserimento report sindaci",
							Toast.LENGTH_SHORT).show();

			} catch (JSONException e) {
				Log.e("invio report : ", "Errore creazione JSONObject");
				e.printStackTrace();
			}

		}
		azzeraConsiglieri();
		}
	}

	public void azzeraSindaci() {
		Cursor c = db.fetchSindaci();
		int idCol = c.getColumnIndex(DBSindaci.SindacoMetadata.ID_SINDACO);
		if (c.moveToFirst()) { // se va alla prima entry, il cursore non è vuoto
			do {
				db.update(c.getString(idCol), 0);
			} while (c.moveToNext()); // iteriamo al prox elemento
		}
	}

	public void azzeraListe() {
		Cursor c = db2.fetchListe();
		int idCol = c.getColumnIndex(DBListe.ListeMetaData.ID_LISTA);
		if (c.moveToFirst()) { // se va alla prima entry, il cursore non è vuoto
			do {
				db2.update(c.getString(idCol), 0);
			} while (c.moveToNext()); // iteriamo al prox elemento
		}
	}

	public void azzeraConsiglieri() {
		Cursor c = db3.fetchConsiglieri();
		int idCol = c
				.getColumnIndex(DBConsiglieri.ConsiglieriMetaData.ID_CONSIGLIERE);
		if (c.moveToFirst()) { // se va alla prima entry, il cursore non è vuoto
			do {
				db3.update(c.getString(idCol), 0);
			} while (c.moveToNext()); // iteriamo al prox elemento
		}
	}

	public void visualizzaConsiglieri() {
		TableLayout layout = (TableLayout) findViewById(R.id.table2);
		Cursor c = db3.fetchConsiglieri();
		int nameCol = c
				.getColumnIndex(DBConsiglieri.ConsiglieriMetaData.NOME_CONSIGLIERE);
		int cognCol = c
				.getColumnIndex(DBConsiglieri.ConsiglieriMetaData.COGNOME_CONSIGLIERE);
		int idCol = c
				.getColumnIndex(DBConsiglieri.ConsiglieriMetaData.ID_CONSIGLIERE);

		if (c.moveToFirst()) { // se va alla prima entry, il cursore non è vuoto
			do {
				Button bottone = new Button(this);
				bottone.setTag(c.getString(idCol));
				bottone.setTextColor(0x000000);
				bottone.setHintTextColor(Color.BLACK);
				bottone.setHint(c.getString(nameCol) + "\n"
						+ c.getString(cognCol));
				bottone.setTextSize(35);
				layout.addView(bottone);
				bottone.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						String sesso = getIntent().getExtras().getString(
								"sesso");
						String id = getIntent().getExtras().getString("id");
						Log.i("Sesso consiglieri=>", sesso);
						if ((db3.selectSessoConsiglieri(v.getTag().toString())
								.equals(sesso))
								|| (v.getTag().toString().equals(id))) {
							Intent intent = new Intent(ConsiglieriReal2.this,
									SindaciReal.class);
							Bundle bundle = new Bundle();
							bundle.putString("access_token", access_token);
							bundle.putString("refresh_token", refresh_token);
							intent.putExtras(bundle);
							startActivity(intent);
							finish();
						} else {

							db3.update(v.getTag().toString(), db3
									.selectVotiConsiglieri(v.getTag()
											.toString()) + 1);

							Intent intent = new Intent(ConsiglieriReal2.this,
									SindaciReal.class);
							Bundle bundle = new Bundle();
							bundle.putString("access_token", access_token);
							bundle.putString("refresh_token", refresh_token);
							intent.putExtras(bundle);
							startActivity(intent);
							finish();
						}
					}
				});
			} while (c.moveToNext());
		}
	}

}