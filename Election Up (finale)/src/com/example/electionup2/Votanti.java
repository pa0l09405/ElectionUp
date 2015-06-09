package com.example.electionup2;

import htmlrequest.HTMLRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import rigenarazioneToken.RigeneraToken;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.app.official.ElectionUp.R;

import controlloStatoInternet.ControlloConnessioneInternet;

public class Votanti extends Activity 
implements OnClickListener{
	private String access_token;
	private String refresh_token;
	private final static int numero_campi=6;
	private final String  nonAutorizzato="nonAutorizzato";     //per gestire la risposta 401 
	private final String URL_BASE="http://api-electionup.mybluemix.net/";
	
	//da togliere dopo aver implementato l'invio dei dati
	public static Integer[] votanti= new Integer[numero_campi];
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_votanti);
		//estraggo parametri dalla Home
		access_token = getIntent().getExtras().getString("access_token"); 
		refresh_token= getIntent().getExtras().getString("refresh_token"); 
		
            
			recuperaDatiSezione();//recupero dati 
		
}

	private void recuperaDatiSezione() {
		
						/*Documentazione Recupero votanti:  
						 * Uri:   /votanti  
				 			(POST): access_token=xxxxxxxx  
				 			Template risposta: [ { "num_aventi_diritto":null,
				 			 					   "num_votanti_maschi":null,
					 			  					   "num_votanti_femmine":null,
					 			  					   "username":"snsezione14",
					 			  					    "plesso":"SN03", 
					 			  					    "num_aventi_diritto_femmine":null, 
					 			  					    "num_aventi_diritto_maschi":null, 
					 			  					    "num_votanti_totali":null } ]*/ 
		String URL_EXTRA="votanti";
		String PARAMS="access_token="+access_token;
		HTMLRequest htlmRequest= new HTMLRequest(URL_BASE+URL_EXTRA, PARAMS);
		String htmlRisposta=htlmRequest.getHTML();
		//Toast.makeText(getApplicationContext(),htmlRisposta, Toast.LENGTH_LONG).show();
		Log.i("metodo recupera dati sezione", ", risposta server :  "+htmlRisposta);
		//controllo risposta
		if (htmlRisposta!=null && ! htmlRisposta.equals(nonAutorizzato)){  //se tutto è andato bene

			//conversione JSON
			JSONArray jsonA = null;

			try {
				jsonA=new JSONArray(htmlRisposta);
					//cancellato for (int i=0;i<numero_campi;i++)
					JSONObject jsonObj = jsonA.getJSONObject(0);//(i)
					String num_aventi_diritto=jsonObj.getString("num_aventi_diritto");
					String num_aventi_diritto_femmine=jsonObj.getString("num_aventi_diritto_femmine");
					String num_aventi_diritto_maschi=jsonObj.getString("num_aventi_diritto_maschi");
					String num_votanti_totali=jsonObj.getString("num_votanti_totali");
					String num_votanti_femmine=jsonObj.getString("num_votanti_femmine");
					String num_votanti_maschi=jsonObj.getString("num_votanti_maschi");
					
					EditText text1 = (EditText)findViewById(R.id.editText1);
	            	EditText text2 = (EditText)findViewById(R.id.editText2);
	            	EditText text3 = (EditText)findViewById(R.id.editText3);
	            	EditText text4 = (EditText)findViewById(R.id.editText4);
	            	EditText text5 = (EditText)findViewById(R.id.editText5);
	            	EditText text6 = (EditText)findViewById(R.id.editText6);
					
	            	if(!num_aventi_diritto.equals("null"))
	            		text1.setText(num_aventi_diritto);
	            	if(!num_aventi_diritto_femmine.equals("null"))
	            		text2.setText(num_aventi_diritto_femmine);
	            	if(!num_aventi_diritto_maschi.equals("null"))
	            		text3.setText(num_aventi_diritto_maschi);
	            	if(!num_votanti_totali.equals("null"))
	            		text4.setText(num_votanti_totali);
	            	if(!num_votanti_femmine.equals("null"))
	            		text5.setText(num_votanti_femmine);
	            	if(!num_votanti_maschi.equals("null"))
	            		text6.setText(num_votanti_maschi);
	            	
					
					

			} catch (JSONException e1) {
				e1.printStackTrace();
				Log.i("recupero dati sezione : \n", "Errore JSON !!!! ");
				Toast.makeText(getApplicationContext(), "Errore, riprovare", Toast.LENGTH_LONG)
				.show();
				//passaggio parametri e torna alla home
				tornahome();
				finish();
			}
		}
		//controllo se la risposta è stata nulla
		if(htmlRisposta==null)
			//errore
			Toast.makeText(getApplicationContext(), "Errore richiesta al server, riprova o torna al login", Toast.LENGTH_LONG).show();
		//risposta non autorizzato
		if (htmlRisposta.equals(nonAutorizzato)) //allora rigenero il token
			try {

				access_token=	RigeneraToken.rigeneraToken(refresh_token, getApplicationContext());

				//Se non autorizzato più, faccio ripetere il login
				Toast.makeText(getApplicationContext(), "Sessione corrente scaduta\nRiautenticarsi", Toast.LENGTH_LONG)
				.show();
             } catch (JSONException e) {
			     e.printStackTrace();
               }		
		}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.votanti, menu);
		return true;
	}
	
	@Override
	public void onClick(View v) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setMessage("Sei sicuro di voler inviare i dati?\n"
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
	           		if(! ControlloConnessioneInternet.isNetworkAvailable(getApplicationContext())){
	           			//errore
	           			Toast toast = Toast.makeText(getApplicationContext(), "Connessione internet assente...", Toast.LENGTH_SHORT);
	           			toast.setGravity(Gravity.CENTER, 0, 0);
	           			toast.show();
	           		}
	            	   
	           		else {   
	            	   
	            	   /* Documentazione Invio Votanti 
	            	    * Uri:   /votanti/set  
	            	    * Parametri (POST): access_token=xxxxxxxx
	            	    * votanti={
	            	    * "num_aventi_diritto":xxx, 
	            	    * "num_votanti_maschi":xxx, 
	            	    * "num_votanti_femmine":xxx,
	            	    * "num_aventi_diritto_femmine":xxx,
	            	    * "num_aventi_diritto_maschi":xxx,
	            	    * "num_votanti_totali":xxx
	            	    *   } 
	            	    *   NOTA: i campi sono opzionali, quelli che non vengono mandati non vengono toccati.  
	            			Template risposta: { "success":true, "message":"Dati sezione aggiunti con successo" } */ 
	            	   
	            	   EditText text1 = (EditText)findViewById(R.id.editText1);
	            	   EditText text2 = (EditText)findViewById(R.id.editText2);
	            	   EditText text3 = (EditText)findViewById(R.id.editText3);
	            	   EditText text4 = (EditText)findViewById(R.id.editText4);
	            	   EditText text5 = (EditText)findViewById(R.id.editText5);
	            	   EditText text6 = (EditText)findViewById(R.id.editText6);
	            	   
	            	   
	            	   String [] datisezione=new String[numero_campi];
	            	   datisezione[0]=text1.getText().toString();
	            	   datisezione[1]=text2.getText().toString();
	            	   datisezione[2]=text3.getText().toString();
	            	   datisezione[3]=text4.getText().toString();
	            	   datisezione[4]=text5.getText().toString();
	            	   datisezione[5]=text6.getText().toString();
	            	   
	            	   JSONObject Jo = new JSONObject();    
	            	   
	            	   for(int i=0;i<numero_campi;i++){
	            		  
	            	   if (datisezione[i].matches("") ) {
	            		   datisezione[i]="0";}
	            	 
	            	   }      
	            	   
	            	   
	            	   
	            	   try{//costruisco JSONObject da inviare
	            		   
	            		   			if ( Integer.parseInt(datisezione[0])!=0 ) 
	            		   				Jo.put("num_aventi_diritto", datisezione[0]);
	            			   
	            			   		if ( Integer.parseInt(datisezione[1])!=0 ) 
	            			   			Jo.put("num_aventi_diritto_femmine", datisezione[1]);
	            			   
	            		   			if ( Integer.parseInt(datisezione[2])!=0 ) 
	            			   			Jo.put("num_aventi_diritto_maschi", datisezione[2]);
	            			   
	            			   		if ( Integer.parseInt(datisezione[3])!=0 ) 
	            			   			Jo.put("num_votanti_totali",datisezione[3]);
		            			   
	            			   		if ( Integer.parseInt(datisezione[4])!=0 ) 
			            			    Jo.put("num_votanti_femmine", datisezione[4]);
		            			   
		            			    if ( Integer.parseInt(datisezione[5])!=0 ) 
			            			   Jo.put("num_votanti_maschi", datisezione[5]);	   	   
						} 
	            			   catch (JSONException e) {
							Log.i("costruzione json per invio report : ",
									"Errore JSON");
							e.printStackTrace();
						  } 
	            	 
	            	   
	            	   String jsonString = Jo.toString();  //conversione in JSON string
	            	   
	            	   
						//ho costruito la stringa da passare
	            	   //faccio l'invio  
						String URL_BASE1 = "http://api-electionup.mybluemix.net/";
						String URL_EXTRA2 = "votanti/set";

						String PARAMS3 = "access_token="
								+ access_token + "&votanti="
								+ jsonString;
						
						Log.i("Invio dati votanti, parametri passati al server : ", PARAMS3);
					

						HTMLRequest htmlRequest2 = new HTMLRequest(URL_BASE1 + URL_EXTRA2, PARAMS3);
						String htmlResponse3 = htmlRequest2.getHTML();
						
						
						

						if (htmlResponse3 != null
									&& !htmlResponse3.equals(nonAutorizzato))
						{ //se è andato tutto bene 

							Log.i("Activity votanti, risposta all'invio dati :",
									htmlResponse3);

							//conversione JSON di risposta
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
											jsonObj1.getString("message"),
											Toast.LENGTH_LONG)
											.show();
								else
									//messaggio di errore
									Toast.makeText(
											getApplicationContext(),
											"Errore inserimento report dati sezione",
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
							Log.w("Invio dati sezione:",
									"errore invio (qualche eccezione lanciata) !!");
							//messaggio errore
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
										"Sessione corrente scaduta\nRiautenticarsi",
										Toast.LENGTH_LONG).show();

							} catch (JSONException e) {
								Toast.makeText(
										getApplicationContext(),
										"errore invio dati, riprova ",
										Toast.LENGTH_LONG).show();

							    }

	                    }
	            	   
	            	//passaggio parametri e ritorno alla Home
	            	tornahome();  
	            	finish();
	            	
	           		
	           		    }
	           		}
	               
	               
	           })
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
	               public void onClick(DialogInterface dialog, int id) {
		                
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
	

private void tornahome(){
		//passaggio parametri e ritorno alla home
		Intent intent= new Intent (this,Home.class);
		intent.putExtra("access_token", access_token);
        intent.putExtra("refresh_token", refresh_token);
		startActivity(intent);
		
	}
	
	@Override
	public void onBackPressed() {
		;           //disabilito tasto indietro
	}
	
}
