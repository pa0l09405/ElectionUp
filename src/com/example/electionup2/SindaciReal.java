package com.example.electionup2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.app.official.ElectionUp.R;
import com.example.electionup2.DBSindaci;
import com.example.electionup2.DBListe;
import com.example.electionup2.DBConsiglieri;

import android.widget.Toast;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;

public class SindaciReal extends Activity implements OnClickListener {

	private DBSindaci db;
	private DBListe db2;
	private DBConsiglieri db3;
	private String access_token;
	private String refresh_token;
	private static int contatore_invio = 0;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sindaci_real);

		db = new DBSindaci(getApplicationContext());
		db.open();
		db2 = new DBListe(getApplicationContext());
		db2.open();
		db3 = new DBConsiglieri(getApplicationContext());
		db3.open();

		access_token = getIntent().getExtras().getString("access_token");
		refresh_token = getIntent().getExtras().getString("refresh_token");

		if (db.fetchSindaci().getCount() == 0) {
			inizializzaDBSindaci();
			scriviFile("save_access_token.txt", access_token);
		} else if (!leggiFile("save_access_token.txt").equals(access_token)) {
			inviaDati();
			db.deleteSindaci();
			inizializzaDBSindaci();
			scriviFile("save_access_token.txt", access_token);
		}
		visualizzaSindaci();
		contatore_invio = getIntent().getIntExtra("contatore", -1);
		contatore_invio++;
		if (contatore_invio == 50) {
			contatore_invio = 0;
			inviaDati();
		}
	}

	public void inizializzaDBSindaci() {
		String URL_BASE = "http://api-electionup.mybluemix.net/";
		String URL_EXTRA = "candidati/sindaci";
		String PARAMS = "access_token=" + access_token;

		HTMLRequest htlmRequest = new HTMLRequest(URL_BASE + URL_EXTRA, PARAMS);
		String htmlRisposta = htlmRequest.getHTML();

		if (htmlRisposta != null) {

			// conversione JSON
			JSONArray jsonA = null;

			try {
				jsonA = new JSONArray(htmlRisposta);

				for (int i = 0; i < jsonA.length(); i++) {

					JSONObject object = jsonA.getJSONObject(i);
					db.insert(object.getString("id"),
							object.getString("cognome"),
							object.getString("nome"), 0);
				}
			} catch (JSONException e) {
				e.printStackTrace();
				Toast.makeText(getApplicationContext(),
						"Dati sindaci non trovati", Toast.LENGTH_SHORT).show();
			}
		}
	}

	public void visualizzaSindaci() {
		TableLayout layout = (TableLayout) findViewById(R.id.table);
		Cursor c = db.fetchSindaci();
		int cognCol = c
				.getColumnIndex(DBSindaci.SindacoMetadata.COGNOME_SINDACO);
		int nameCol = c.getColumnIndex(DBSindaci.SindacoMetadata.NOME_SINDACO);
		int idCol = c.getColumnIndex(DBSindaci.SindacoMetadata.ID_SINDACO);
		
		if (c.moveToFirst()) { // se va alla prima entry, il cursore non è vuoto
			do {
				Button bottone = new Button(this);
				bottone.setTag(c.getString(idCol));
				bottone.setHintTextColor(Color.BLACK);
				bottone.setHint(c.getString(nameCol) + "\n"
						+ c.getString(cognCol));
				bottone.setHeight(350);
				bottone.setTextSize(35);
				layout.addView(bottone);
				bottone.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {

						db.update(v.getTag().toString(),
								db.selectVotiSindaci(v.getTag().toString()) + 1);

						Intent intent = new Intent(SindaciReal.this,
								ListeReal.class);
						Bundle bundle = new Bundle();
						bundle.putString("access_token", access_token);
						bundle.putString("refresh_token", refresh_token);
						intent.putExtra("contatore", contatore_invio);
						intent.putExtras(bundle);
						startActivity(intent);
						finish();
					}
				});
			} while (c.moveToNext()); // iteriamo al prox elemento
		}
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

	public void onBackPressed() {
		;
	}

	public void onClickSalva(View v) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Terminare l'assegnamento dei voti?")
				.setCancelable(false)
				.setPositiveButton("Si", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						salvaEdEsci();
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

	private void salvaEdEsci() {
		inviaDati();
		Intent intent = new Intent(SindaciReal.this, preVotazione.class);
		Bundle bundle = new Bundle();
		intent.putExtra("access_token", access_token);
		intent.putExtra("refresh_token", refresh_token);
		bundle.putString("access_token", access_token);
		bundle.putString("refresh_token", refresh_token);
		startActivity(intent);
		finish();
	}
	public void creaFile() {
		String name = "save_access_token.txt";
		File accFile = new File(this.getFilesDir(), name);
		if (!accFile.exists()) {
			try {
				accFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public String leggiFile(String path) {
		String s = "";
		creaFile();
		final int READ_BLOCK_SIZE = 60;
		try {
			FileInputStream fileIn = openFileInput(path);
			InputStreamReader InputRead = new InputStreamReader(fileIn);

			char[] inputBuffer = new char[READ_BLOCK_SIZE];

			int charRead;

			while ((charRead = InputRead.read(inputBuffer)) > 0) {
				// char to string conversion
				String readstring = String
						.copyValueOf(inputBuffer, 0, charRead);
				s += readstring;
			}
			InputRead.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}

	public void scriviFile(String path, String tok) {
		creaFile();
		try {
			FileOutputStream fileout = openFileOutput(path, this.MODE_PRIVATE);
			OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);
			outputWriter.write(tok);
			outputWriter.close();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	@Override
	public void onClick(View v) {

		;
	}

}
