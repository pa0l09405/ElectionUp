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
import com.example.electionup2.DBSindaci;

public class DatiProvvSindaci extends Activity {

	private String access_token;
	private String refresh_token;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_datiprovvsindaci);

		TextView sindaciTv = (TextView) findViewById(R.id.sindaciTv);
		sindaciTv.setBackgroundColor(Color.TRANSPARENT);
		sindaciTv.setTextColor(Color.WHITE);
		
		DBSindaci db = new DBSindaci(getApplicationContext());
		db.open(); // apriamo il db
		access_token = getIntent().getExtras().getString("access_token");
		refresh_token = getIntent().getExtras().getString("refresh_token");

		Cursor c = db.fetchSindaci(); // query

		// prelevare dati e usarli (in una textview)

		int cognCol = c
				.getColumnIndex(DBSindaci.SindacoMetadata.COGNOME_SINDACO);
		int nameCol = c.getColumnIndex(DBSindaci.SindacoMetadata.NOME_SINDACO);
		int votiCol = c.getColumnIndex(DBSindaci.SindacoMetadata.VOTI_SINDACO);

		if (c.moveToFirst()) { // se va alla prima entry, il cursore non è vuoto
			do {
				sindaciTv.append("" + c.getString(nameCol)
						+ " " + c.getString(cognCol)
						+ ", Voti PROVVISORI: " + c.getInt(votiCol) + "\n");

			} while (c.moveToNext()); // iteriamo al prox elemento

			db.close();

		}
	}

	public void onClickIndietro(View v) {
		Intent intent = new Intent(DatiProvvSindaci.this, preVotazione.class);
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