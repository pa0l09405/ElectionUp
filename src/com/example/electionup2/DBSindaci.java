package com.example.electionup2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBSindaci {

	SQLiteDatabase mDb;
	DbHelper mDbHelper;
	Context mContext;
	private static final int DB_VERSION = 1;

	private static final String DBNAME = "SindaciDb";

	public DBSindaci(Context ctx) {
		mContext = ctx;
		mDbHelper = new DbHelper(ctx, DBNAME, null, DB_VERSION); // quando
																	// instanziamo
																	// questa
																	// classe
																	// instanziamo
																	// anche
																	// l'helper
	}

	public void open() { // database leggibile/scrivibile
		mDb = mDbHelper.getWritableDatabase();
		mDb = mDbHelper.getReadableDatabase();
	}

	public void close() {// chiude il DB
		mDb.close();
	}

	public void insert(String id, String cogn, String nome, int voti) {// motodo
																		// per
																		// la
																		// insert
		ContentValues cv = new ContentValues();
		cv.put(SindacoMetadata.ID_SINDACO, id);
		cv.put(SindacoMetadata.COGNOME_SINDACO, cogn);
		cv.put(SindacoMetadata.NOME_SINDACO, nome);
		cv.put(SindacoMetadata.VOTI_SINDACO, voti);
		mDb.insert(SindacoMetadata.TABLE_NAME, null, cv);
	}
	public void deleteSindaci() {
		mDb.delete(SindacoMetadata.TABLE_NAME, null, null);
	}
	public int update(String id, int voti) {
		ContentValues cv = new ContentValues();
		cv.put(SindacoMetadata.VOTI_SINDACO, voti);
		return mDb.update(SindacoMetadata.TABLE_NAME, cv,
				SindacoMetadata.ID_SINDACO + " = ?",
				new String[] { String.valueOf(id) });
	}
	
	public Cursor fetchSindaci() {// metodo per fare la query su tutti i dati
		return mDb.query(SindacoMetadata.TABLE_NAME, null, null, null, null,
				null, null);

	}

	public int selectVotiSindaci(String id) {
		Cursor c = mDb.query(SindacoMetadata.TABLE_NAME,
				new String[] {SindacoMetadata.VOTI_SINDACO}, SindacoMetadata.ID_SINDACO
						+ "= ?", new String[] { String.valueOf(id) }, null,
				null, null);
		if(c!=null)
			c.moveToFirst();
		int vot=Integer.parseInt(c.getString(0));
		return vot;
	}

	static class SindacoMetadata {
		static final String TABLE_NAME = "Sindaci";
		static final String ID_SINDACO = "_id";
		static final String COGNOME_SINDACO = "Cognome";
		static final String NOME_SINDACO = "Nome";
		static final String VOTI_SINDACO = "Voti";

	}

	private static final String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ SindacoMetadata.TABLE_NAME + " (" + SindacoMetadata.ID_SINDACO
			+ " text primary key, " + SindacoMetadata.COGNOME_SINDACO
			+ " text not null, " + SindacoMetadata.NOME_SINDACO
			+ " text not null, " + SindacoMetadata.VOTI_SINDACO
			+ " integer not null);";

	private class DbHelper extends SQLiteOpenHelper { // classe che ci aiuta a
														// creare il DB

		public DbHelper(Context context, String name, CursorFactory factory,
				int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase _db) {
			_db.execSQL(TABLE_CREATE);

		}

		@Override
		public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
			// qui si metteranno eventuali modifiche alla versione del database

		}

	}
}
