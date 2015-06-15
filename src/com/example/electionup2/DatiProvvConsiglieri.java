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

public class DatiProvvConsiglieri extends Activity {

	private String access_token;
	private String refresh_token;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_datiprovvconsiglieri);

		TextView consiglieriTv = (TextView) findViewById(R.id.consiglieriTv);
		consiglieriTv.setBackgroundColor(Color.TRANSPARENT);
		consiglieriTv.setTextColor(Color.WHITE);

		DBConsiglieri db2 = new DBConsiglieri(getApplicationContext());
		db2.open(); // apriamo il db
		access_token = getIntent().getExtras().getString("access_token");
		refresh_token = getIntent().getExtras().getString("refresh_token");

		Cursor c = db2.fetchConsiglieri(); // query

		// prelevare dati e usarli (in una textview)

		int cognCol = c
				.getColumnIndex(DBConsiglieri.ConsiglieriMetaData.COGNOME_CONSIGLIERE);
		int nameCol = c.getColumnIndex(DBConsiglieri.ConsiglieriMetaData.NOME_CONSIGLIERE);
		int votiCol = c.getColumnIndex(DBConsiglieri.ConsiglieriMetaData.VOTI_CONSIGLIERE);

		if (c.moveToFirst()) { // se va alla prima entry, il cursore non è vuoto
			do {
				consiglieriTv.append("" + c.getString(nameCol)
						+ " " + c.getString(cognCol)
						+ ", Voti PROVVISORI: " + c.getInt(votiCol) + "\n");

			} while (c.moveToNext()); // iteriamo al prox elemento

			db2.close();

		}
	}

	public void onClickIndietro(View v) {

		Intent intent = new Intent(DatiProvvConsiglieri.this, preVotazione.class);
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