package com.example.electionup2;

import htmlrequest.HTMLRequest;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import rigenarazioneToken.RigeneraToken;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.app.official.ElectionUp.R;

import controlloStatoInternet.ControlloConnessioneInternet;

@SuppressLint("RtlHardcoded")
public class Liste extends Activity {
	
	private  List<EditText> allEds = new ArrayList<EditText>();   //mem gli oggetti edittext
	private  List<TextView> lis = new ArrayList<TextView>();     //mem gli oggetti lista
	private ArrayList<String> ids = new ArrayList<String>();    //mem gli id 
	final String  nonAutorizzato="nonAutorizzato";     //per gestire la risposta 401 
	private  int rows;
	private String access_token;
	private String refresh_token;
	private  String numeroVoti;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_liste);
		//estraggo parametri da AssegnaVoto
		access_token = getIntent().getExtras().getString("access_token"); 
		refresh_token= getIntent().getExtras().getString("refresh_token"); 
		//controllo connessione
		if (ControlloConnessioneInternet.isNetworkAvailable(getApplicationContext()))
		         recuperaListe(); //recupero dati liste della sezione
		else {
			//messaggio errore
			Toast miotoast = Toast.makeText(getApplicationContext(), "Connessione internet assente...", Toast.LENGTH_SHORT);
		    miotoast.setGravity(Gravity.CENTER, 0, 0);
		    miotoast.show();
		  }
	}
	
	
	
	public void recuperaListe(){
		   /*documentazione Recupero liste:  
			Uri:   /candidati/liste  
			Parametri (POST): access_token=xxxxxxxx 
			Template risposta: [ { "id":"SNMOV5S", "nome":"MoVimento 5 Stelle", "sindaco":null, "consiglieri":null }, … ] */
		
		final String URL_BASE="http://api-electionup.mybluemix.net/";
		final String URL_EXTRA="candidati/liste";
		final String PARAMS="access_token="+access_token;
		
		HTMLRequest htlmRequest= new HTMLRequest(URL_BASE+URL_EXTRA, PARAMS);
		String htmlRisposta=htlmRequest.getHTML();
		
		//controllo risposta
		if (htmlRisposta!=null && ! htmlRisposta.equals(nonAutorizzato)){ //se tutto va bene

			//conversione JSON
			JSONArray	jsonA=null;
			
			try {
				//assegno la dimensione dinamicamente in base alla risposta
				jsonA=new JSONArray(htmlRisposta);
				rows=jsonA.length();
				
			for (int i=0;i<rows;i++){
				JSONObject object = jsonA.getJSONObject(i);
				String lista1=object.getString("nome");
				//memorizzo gli id
				ids.add(object.getString("id"));
				
				//creo righe nel tableLayout
				TableLayout layout=(TableLayout) findViewById(R.id.table1);
				TableRow tableRow = new TableRow(this);
						
						 //edittext per inserire i voti
					     EditText edit= new EditText(this);
						 edit.setId(i);
						 edit.setTextColor(0xffffffff);
						 edit.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
						 edit.setHintTextColor(0xffffffff);
						 edit.setHint("# voti");
						 tableRow.addView(edit);
						 layout.addView(tableRow);
						 //memorizzo l'edittext
						 allEds.add(edit);

						 //button per visualizzare i nomi delle liste
						 Button lista = new Button(this);
						 lista.setBackgroundColor(0x00ffffff);
						 lista.setTextColor(0xffffffff);
						 lista.setText(lista1);
						 lista.setGravity(Gravity.LEFT);
						 lista.setId(i);
						 tableRow.addView(lista);
						 //memorizzo il bottone lista
						 lis.add(lista);
						 
						}
			
			} catch (JSONException e) {
				e.printStackTrace();
				//messaggio errore
				Toast.makeText(getApplicationContext(), "Dati liste non trovati", Toast.LENGTH_LONG)
				.show();
			}
		
		}
		
		//controllo se la risposta è stata nulla
		if(htmlRisposta==null)
			Toast.makeText(getApplicationContext(), "Errore richiesta al server, riprova o torna al login", Toast.LENGTH_LONG).show();
 
		//risposta non autorizzata
		if (htmlRisposta.equals(nonAutorizzato)) //allora rigenero il token
			try {
				access_token=	RigeneraToken.rigeneraToken(refresh_token, getApplicationContext());
				//Se non autorizzato più, faccio ripetere il login e avviso l'utente
				Toast.makeText(getApplicationContext(), "Sessione corrente scaduta\nRiautenticarsi o tornare alla home", Toast.LENGTH_LONG)
				.show();
            } catch (JSONException e) {
				e.printStackTrace();
              }
	}
	
	
	@Override
	public void onBackPressed() {
	   ;//disabilito il tasto indietro
	   }
	

	public void onClick(View v) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setMessage("Sei sicuro di voler inviare i voti?\n"
	    		+ "(operazione irreversibile)")
	           .setCancelable(false)
	           .setNegativeButton("No", new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	                    dialog.cancel();
	               }
	           })
	           .setPositiveButton("Si", new DialogInterface.OnClickListener() {
	        	   //si attiva al si
	               public void onClick(DialogInterface dialog, int id) {
	            	   
	            	 //controllo connessione
						if (ControlloConnessioneInternet.isNetworkAvailable(getApplicationContext())) {
							
							//per costrure il JSON da inviare
							String jsonString;
							JSONArray jsonob = new JSONArray();
							
							
							
							/*				Salva report:
							Uri: /report/store
							Parametri (POST): access_token=xxxxxxxx
							report=[{"sindaco":"SNSIN02","numero_voti":"40"},
							{"consigliere":"SNCON10","numero_voti":"35"},
							{"lista":"SNLIS07","numero_voti":"35"},
							…]
							NOTA: Nell’array json da mandare si possono inserire quanti report si vuole, anche solo report dei sindaci o mischiati tra loro, l’importante che siano sempre in un array e che i singoli oggetti mantengano questa formattazione.
							Template risposta:
							- Successo {
							“success”: true,
							“message”: “Report inseriti con success”
							}
							- Fallito {
							“success”: false,
							“message”: messaggio dell’errore verificatosi
							}
		                   */
							
							for (int i = 0; i < allEds.size(); i++) { //per ciascun edittext creato

								numeroVoti = allEds.get(i).getText().toString();

								if (numeroVoti.matches("")) {
									numeroVoti = "0";
								} else if (numeroVoti.matches("-")) {
									numeroVoti = "0";
								} 
								
								

								if ( Integer.parseInt(numeroVoti)!=0 ) {
									try {//costruisco il jsonObject se il numero dei voti non è zero
										

											jsonob.put(new JSONObject().put(
													"lista", ids.get(i)).put(
													"numero_voti", numeroVoti));

										
									} catch (JSONException e) {
										Log.i("costruzione json per invio report : ",
												"Errore JSON");
										e.printStackTrace();
									}
								}//fine if
								
							}//fine for
							
							
							if(jsonob.length()!=0){
									jsonString = jsonob.toString();
									//ho costruito la stringa da passare

									Log.w("invio report sindaci : \n",
											"access_token="
													+ access_token
													+ ". stringa json costruita : "
													+ jsonString);

									//faccio l'invio  
									String URL_BASE1 = "http://api-electionup.mybluemix.net/";
									String URL_EXTRA2 = "report/store";

									String PARAMS3 = "access_token="
											+ access_token + "&report="
											+ jsonString;

									HTMLRequest htmlRequest2 = new HTMLRequest(
											URL_BASE1 + URL_EXTRA2, PARAMS3);
									String htmlResponse3 = htmlRequest2
											.getHTML();

									if (htmlResponse3 != null
											&& !htmlResponse3
													.equals(nonAutorizzato)) { //se è andato tutto bene 

										Log.i("Activity liste, risposta all'invio dati :",
												htmlResponse3);

										//conversione JSON
										JSONObject jsonObj1 = null;
										try {
											jsonObj1 = new JSONObject(
													htmlResponse3);

										} catch (JSONException e) {
											Log.e("invio report : ",
													"Errore creazione JSONObject");
											e.printStackTrace();
										}

										try {//se la risposta è success
											if (jsonObj1.getBoolean("success"))
												//messaggio di conferma
												Toast.makeText(
														getApplicationContext(),
														jsonObj1.getString("message")
																+ " (per liste)",
														Toast.LENGTH_LONG)
														.show();
											else //messaggio di errore
												Toast.makeText(
														getApplicationContext(),
														"errore inserimento report liste",
														Toast.LENGTH_LONG)
														.show();

										} catch (JSONException e) {
											Log.e("invio report : ",
													"Errore creazione JSONObject");
											e.printStackTrace();
										}

									}

									// se c'è un errore nella richiesta (es eccezione di lettura, scrittura nello stream)
									if (htmlResponse3 == null) {
										Log.w("Invio numero votanti :",
												"errore invio (qualche eccezione lanciata) !!");
										Toast.makeText(
												getApplicationContext(),
												"Errore invio dati, riprova o ritorna al login ",
												Toast.LENGTH_LONG).show();
									}

									// se il token è scaduto
									if (htmlResponse3.equals(nonAutorizzato)) {
										try {

											// lo rigenero
											access_token = RigeneraToken
													.rigeneraToken(
															refresh_token,
															getApplicationContext());

											//Se non autorizzato più, faccio ripetere il login
											Toast.makeText(
													getApplicationContext(),
													"Sessione corrente scaduta\nRiautenticarsi o riprovare",
													Toast.LENGTH_LONG).show();

										} catch (JSONException e) {
											//messaggio di errre
											Toast.makeText(
													getApplicationContext(),
													"Errore invio dati, riprova ",
													Toast.LENGTH_LONG).show();

										   }

									
								
									}
									
							}//if che vede se l'array è vuoto

							
							//svuoto arraylist
							ids.clear();
							allEds.clear();
							lis.clear();
							tornahome();
							finish();
							
						}//if della connessione (che verifica se c'è internet
						
						else {//messaggio di errore
							  Toast.makeText(getApplicationContext(), "Connessione internet assente...", Toast.LENGTH_SHORT).show();
							  }
						
					
				}})
				;
		AlertDialog alert = builder.create();
		alert.show();
	}
	   
public void onClickIndietro(View v) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setMessage("Sei sicuro di voler tornare alla home?\n"
	    		+ "I dati non salvati andranno persi.")
	           .setCancelable(false)
	           .setPositiveButton("Si", new DialogInterface.OnClickListener() {
	        	   //si attiva al si
	               public void onClick(DialogInterface dialog, int id) {
	            	   //svuoto arraylist
	            	   ids.clear();
	            	   allEds.clear();  
	            	   lis.clear();
	            	   //passaggio parametri e ritorno alla home
	            	   tornahome();  
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
	
public void tornahome(){
		//passaggio parametri e ritorno alla home
		Intent intent= new Intent (this,Home.class);
		intent.putExtra("access_token", access_token);
        intent.putExtra("refresh_token", refresh_token);
		startActivity(intent);
		
	}
}
