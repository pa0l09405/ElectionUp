package com.example.electionup2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.os.Parcel;
import android.os.Parcelable;

public class DBConsiglieri implements Parcelable {
	SQLiteDatabase dbconsiglieri;
	DbHelper mDbHelperConsiglieri;
	Context mContextConsiglieri;
	private static final String DB_Name = "DbConsiglieri";// nome del database
	private static final int DB_VERSION = 1;// numero di versione del database

	public DBConsiglieri(Context ctx) {
		mContextConsiglieri = ctx;
		mDbHelperConsiglieri = new DbHelper(ctx, DB_Name, null, DB_VERSION);
	}

	public void open() {
		dbconsiglieri = mDbHelperConsiglieri.getWritableDatabase();
	}

	public void close() {
		dbconsiglieri.close();
	}
	public void deleteConsiglieri() {
		dbconsiglieri.delete(ConsiglieriMetaData.CONSIGLIERI_TABLE, null, null);
	}

	public void insert(String ID, String NOME_CONSIGLIERE,
			String COGNOME_CONSIGLIERE, String SESSO, int VOTI) {
		ContentValues cv = new ContentValues();
		cv.put(ConsiglieriMetaData.ID_CONSIGLIERE, ID);
		cv.put(ConsiglieriMetaData.NOME_CONSIGLIERE, NOME_CONSIGLIERE);
		cv.put(ConsiglieriMetaData.COGNOME_CONSIGLIERE, COGNOME_CONSIGLIERE);
		cv.put(ConsiglieriMetaData.SESSO_CONSIGLIERE, SESSO);
		cv.put(ConsiglieriMetaData.VOTI_CONSIGLIERE, VOTI);
		dbconsiglieri.insert(ConsiglieriMetaData.CONSIGLIERI_TABLE, null, cv);
	}

	public void update(String indice, int voti) {
		ContentValues cv = new ContentValues();
		cv.put(ConsiglieriMetaData.VOTI_CONSIGLIERE, voti);
		dbconsiglieri.update(ConsiglieriMetaData.CONSIGLIERI_TABLE, cv,
				ConsiglieriMetaData.ID_CONSIGLIERE + " = ?",
				new String[] { String.valueOf(indice) });
	}

	public Cursor fetchConsiglieri() {
		return dbconsiglieri.query(ConsiglieriMetaData.CONSIGLIERI_TABLE, null,
				null, null, null, null, null);
	}

	public int selectVotiConsiglieri(String id) {
		Cursor c = dbconsiglieri.query(ConsiglieriMetaData.CONSIGLIERI_TABLE,
				new String[] { ConsiglieriMetaData.VOTI_CONSIGLIERE },
				ConsiglieriMetaData.ID_CONSIGLIERE + "= ?",
				new String[] { String.valueOf(id) }, null, null, null);
		if (c != null)
			c.moveToFirst();
		int vot = Integer.parseInt(c.getString(0));
		return vot;
	}
	
	public String selectSessoConsiglieri(String id) {
		Cursor c = dbconsiglieri.query(ConsiglieriMetaData.CONSIGLIERI_TABLE,
				new String[] { ConsiglieriMetaData.SESSO_CONSIGLIERE },
				ConsiglieriMetaData.ID_CONSIGLIERE + "= ?",
				new String[] { String.valueOf(id) }, null, null, null);
		if (c != null)
			c.moveToFirst();
		String sess = c.getString(0);
		return sess;
	}
	
	static class ConsiglieriMetaData {
		static final String CONSIGLIERI_TABLE = "LISTE";
		static final String ID_CONSIGLIERE = "_id";
		static final String NOME_CONSIGLIERE = "NOME_CONSIGLIERE";
		static final String COGNOME_CONSIGLIERE = "COGNOME_CONSIGLIERE";
		static final String SESSO_CONSIGLIERE = "SESSO_CONSIGLIERE";
		static final String VOTI_CONSIGLIERE = "VOTI";
	}

	private static final String CONSIGLIERI_TABLE_CREATE = " CREATE TABLE IF NOT EXISTS "
			+ ConsiglieriMetaData.CONSIGLIERI_TABLE
			+ " ( "
			+ ConsiglieriMetaData.ID_CONSIGLIERE
			+ " TEXT NOT NULL , "
			+ ConsiglieriMetaData.NOME_CONSIGLIERE
			+ " TEXT NOT NULL , "
			+ ConsiglieriMetaData.COGNOME_CONSIGLIERE
			+ " TEXT NOT NULL , "
			+ ConsiglieriMetaData.SESSO_CONSIGLIERE
			+ " TEXT NOT NULL , "
			+ ConsiglieriMetaData.VOTI_CONSIGLIERE + " INTEGER NOT NULL );";

	private class DbHelper extends SQLiteOpenHelper {

		public DbHelper(Context context, String name, CursorFactory factory,
				int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL(CONSIGLIERI_TABLE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub

		}

	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub

	}
}