package controlloStatoInternet;

import android.content.Context;
import android.net.*;
import android.net.NetworkInfo;

public class ControlloConnessioneInternet {

	/**
	 * @param context context in cui il metodo è evocato
	 * @return true if the device's connected
	 */
	static public boolean isNetworkAvailable(Context context) {  
	    ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);  
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();  
	    return activeNetworkInfo != null;  
	}  
	
	
	//costruttore vuoto
	public ControlloConnessioneInternet() {
	}

}
