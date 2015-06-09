package htmlrequest;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutionException;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;



/**

 * INPUT: URL website + parameters (in post or in get)
 * OUTPUT: getHTML() method retrieve url html e usa il thread parallelo
 * In case of POST data, use the second parameter in the constructor, 
 *                                       otherwise use URL to use GET
 * 
 * @author gruppo ElectionUp
 * @version 1
 * @see android.os.AsyncTask
 */

public class HTMLRequest extends AsyncTask<Void, Void, String> {

	private String site = "";
	private String cookie = "";
	private String parameters = "";



	/**
	 * constructor for requests POST
	 * @param u Url
	 * @param par Parametri da passare in POST
	 */
	public HTMLRequest(String u, String par) {
		site = u;
		parameters = par;
	}

	/**
	 * Constructor for request GET
	 * @param u Url
	 */
	public HTMLRequest(String u){
		site=u;
	}


	/**
	 * Setter dei Cookie
	 * @param str Cookie
	 */
	//non sarà comunque utilizzato
	public void setCookie(String str) {
		cookie = str;
	}



	/**
	 * request HTTP synchronous with Thread
	 * 
	 * @return result of the Api calling
	 */
	//method that use thread   
	public String getHTML(){

		String rit= null;

		try {
			rit= this.execute().get();
		} catch (InterruptedException e) {
			Log.e("HTMLRequest","Execution Interrupted");
			e.printStackTrace();
		} catch (ExecutionException e) {
			Log.e("HTMLRequest","Execution Exception");
			e.printStackTrace();
		}

		return rit;
	}



	/**
	 * Method doInBackground (it executes the parallel thread)
	 * @param params Parameters null (no input)
	 * @return Result of the request
	 */

	@Override
	@SuppressLint({ "NewApi", "UseValueOf" })
	protected String doInBackground(Void... params) {
        
		String nonAutorizzato = "nonAutorizzato"; 
		URL url;
		try {
			url = new URL(site);
		} catch (MalformedURLException e) {
			Log.e("HTML request", "Malfromed URL Exception in doInBackgruond method");
			e.printStackTrace();
			return null;
		}

		String urlParameters  = this.parameters;
		byte[] postData       = urlParameters.getBytes( Charset.forName( "UTF-8" ));
		int    postDataLength = postData.length;
		HttpURLConnection cox = null;
		try {
			cox = (HttpURLConnection) url
					.openConnection();
		} catch (IOException e) {
			Log.e("HTMLRequest","Error in openConnection ");
			e.printStackTrace();
		}
		assert cox != null;
		cox.addRequestProperty("Cookie", cookie);
		cox.setDoOutput( true );
		cox.setDoInput ( true );
		cox.setInstanceFollowRedirects( false );
     
		
		try {
			cox.setRequestMethod( "POST" );
		} catch (ProtocolException e1) {
			Log.e("HTMLRequest","Error on setRequestMethod");
			e1.printStackTrace();
		}
		cox.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
		cox.setRequestProperty( "charset", "utf-8");
		cox.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
		cox.setUseCaches( false );
		try( DataOutputStream wr = new DataOutputStream( cox.getOutputStream())) {

			wr.write( postData );


			Integer responseCode = new Integer( cox.getResponseCode() );

			Log.i("Sending request to URL", site);
			Log.i("\nwith the parameters : ", parameters);
			Log.i("\nresponse code : ", responseCode.toString());

			if ( responseCode == 401)
				return nonAutorizzato;

	    		
		
		} catch (IOException e1) {
			Log.e("HTMLRequest","Error on writeData");
			e1.printStackTrace();
		}
		
		
		String jsonStr = "";
		BufferedReader br;
		try {
			br = new BufferedReader(new InputStreamReader(cox.getInputStream()));
		} catch (IOException e) {
			Log.e("HTMLRequest","Error on BufferedReader");
			e.printStackTrace();
			return null;
		}
		
		String temp;
		try {
			while ((temp = br.readLine()) != null) {
				jsonStr+= temp;
			}
		} catch (IOException e) {
			Log.e("HTMLRequest","Error on readLine");
			e.printStackTrace();
		}

		return jsonStr;

	}





}
