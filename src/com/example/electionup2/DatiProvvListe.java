package com.example.electionup2;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;

import com.app.official.ElectionUp.R;

public class DatiProvvListe extends Activity {

	private String access_token;
	private String refresh_token;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_datiprovvliste);

		TextView listeTv = (TextView) findViewById(R.id.listeTv);
		listeTv.setBackgroundColor(Color.TRANSPARENT);
		listeTv.setTextColor(Color.WHITE);

		DBListe db2 = new DBListe(getApplicationContext());
		db2.open(); // apriamo il db
		access_token = getIntent().getExtras().getString("access_token");
		refresh_token = getIntent().getExtras().getString("refresh_token");

		Cursor c = db2.fetchListe(); // query

		// prelevare dati e usarli (in una textview)

		int nameCol = c
				.getColumnIndex(DBListe.ListeMetaData.NOME_LISTA);
		
		int votiCol = c.getColumnIndex(DBListe.ListeMetaData.VOTI_LISTA);

		if (c.moveToFirst()) { // se va alla prima entry, il cursore non è vuoto
			do {
				listeTv.append("" + c.getString(nameCol)

						+ ", Voti PROVVISORI: " + c.getInt(votiCol) + "\n");

			} while (c.moveToNext()); // iteriamo al prox elemento

			db2.close();

		}
	}

	public void onClickIndietro(View v) {

		Intent intent = new Intent(DatiProvvListe.this, preVotazione.class);
		Bundle bundle = new Bundle();
		bundle.putString("access_token", access_token);
		bundle.putString("refresh_token", refresh_token);
		intent.putExtras(bundle);
		startActivity(intent);
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	public void onClick(View v) {

		;
	}

	public void onBackPressed() {
		;
	}

}