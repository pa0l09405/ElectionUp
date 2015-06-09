package rigenarazioneToken;

import htmlrequest.HTMLRequest;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;


public class RigeneraToken {

	
	/**
	 * @param  refresh_token detenuto al momento della chiamata
	 * @param  context in cui chiamo questo metodo
	 * @return il nuovo access_token (se vi sono errori restituisce null) 
	 *
	 */
	static public String rigeneraToken(String refresh_token, Context context) throws JSONException{
		
		final String URL_BASE="http://api-electionup.mybluemix.net/";
		final String URL_EXTRA="oauth";
		final String PARAMS="grant_type=refresh_token&client_id=app_rappresentante&client_secret=dematdefalsindaci&refresh_token="+refresh_token;
		
		HTMLRequest request=new HTMLRequest(URL_BASE+URL_EXTRA, PARAMS);
		String risposta=request.getHTML();
		
		if(risposta!=null && ! risposta.equals("nonAutorizzato")){ //tutto è andato bene
			
			Toast.makeText(context, "Token rigenerato !", Toast.LENGTH_LONG)
	         .show();
			Log.w("rigenerazione token : ", risposta);
			
			//conversione Json
			JSONObject jsonObj=new JSONObject(risposta);
			return jsonObj.getString("access_token");
			
		}else {
			
			Log.w("rigenerazione token : ", "Operazioni non più autorizzate, TORNARE ALLA SCHERMATA DI BENVENUTO !");
			Toast.makeText(context, "Operazioni non più autorizzate, TORNARE ALLA SCHERMATA DI BENVENUTO !", Toast.LENGTH_LONG)
	         .show();
	
	        return null;
		}
		
	}
	
	
	//costruttore vuoto
	public RigeneraToken() {
		
	}

}
