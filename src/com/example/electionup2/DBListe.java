package com.example.electionup2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class DBListe {

	SQLiteDatabase mdbliste;
	DbHelper mDbHelperListe;
	Context mContextListe;
	private static final String DB_Name = "DbListe";// nome del database
	private static final int DB_VERSION = 1;// numero di versione del database

	public DBListe(Context ctx) {
		mContextListe = ctx;
		mDbHelperListe = new DbHelper(ctx, DB_Name, null, DB_VERSION);
	}

	public void open() {
		mdbliste = mDbHelperListe.getWritableDatabase();
	}

	public void close() {
		mdbliste.close();
	}
	public void deleteListe() {
		mdbliste.delete(ListeMetaData.LISTE_TABLE, null, null);
	}

	public void insert(String ID, String NOME_LISTA, int VOTI) {
		ContentValues cv = new ContentValues();
		cv.put(ListeMetaData.ID_LISTA, ID);
		cv.put(ListeMetaData.NOME_LISTA, NOME_LISTA);
		cv.put(ListeMetaData.VOTI_LISTA, VOTI);
		mdbliste.insert(ListeMetaData.LISTE_TABLE, null, cv);
	}

	public void update(String indice, int voti) {
		ContentValues cv = new ContentValues();
		cv.put(ListeMetaData.VOTI_LISTA, voti);
		mdbliste.update(ListeMetaData.LISTE_TABLE, cv, ListeMetaData.ID_LISTA
				+ " = ?", new String[] { String.valueOf(indice) });
	}

	public Cursor fetchListe() {
		return mdbliste.query(ListeMetaData.LISTE_TABLE, null, null, null,
				null, null, null);
	}

	public int selectVotiListe(String id) {
		Cursor c = mdbliste.query(ListeMetaData.LISTE_TABLE,
				new String[] { ListeMetaData.VOTI_LISTA },
				ListeMetaData.ID_LISTA + "= ?",
				new String[] { String.valueOf(id) }, null, null, null);
		if (c != null)
			c.moveToFirst();
		int vot = Integer.parseInt(c.getString(0));
		return vot;
	}
	
	public String selectNomiListe(String id) {
		Cursor c = mdbliste.query(ListeMetaData.LISTE_TABLE,
				new String[] { ListeMetaData.NOME_LISTA },
				ListeMetaData.ID_LISTA + "= ?",
				new String[] { String.valueOf(id) }, null, null, null);
		if (c != null)
			c.moveToFirst();
		String nom = c.getString(0);
		return nom;
	}

	static class ListeMetaData {
		static final String LISTE_TABLE = "LISTE";
		static final String ID_LISTA = "_id";
		static final String NOME_LISTA = "NOME_LISTA";
		static final String VOTI_LISTA = "VOTI";
	}

	private static final String PRODUCTS_TABLE_CREATE = " CREATE TABLE IF NOT EXISTS "
			+ ListeMetaData.LISTE_TABLE
			+ " ( "
			+ ListeMetaData.ID_LISTA
			+ " TEXT PRIMARY KEY, "
			+ ListeMetaData.NOME_LISTA
			+ " TEXT NOT NULL , "
			+ ListeMetaData.VOTI_LISTA
			+ " INTEGER NOT NULL );";

	private class DbHelper extends SQLiteOpenHelper {

		public DbHelper(Context context, String name, CursorFactory factory,
				int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase _db) {
			_db.execSQL(PRODUCTS_TABLE_CREATE);

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub

		}
	}
}
