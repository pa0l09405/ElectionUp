package com.example.electionup2;

import com.app.official.ElectionUp.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;


public class Prova extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_prova);
	
		int rows = 3;
		int columns = 2;
		TableLayout layout = new TableLayout(this);
		for (int i = 0; i < rows; i++) {
		 TableRow tableRow = new TableRow(this);
		 tableRow.setGravity(Gravity.CENTER); 
		 for (int j = 0; j < columns; j++) {
			 Button button = new Button(this);
			 button.setText("Bottone " + ((columns * i) + j + 1));
			 tableRow.addView(button);
			 }
			 layout.addView(tableRow);
			}
			setContentView(layout);
	
	}
	
}
