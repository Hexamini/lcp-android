package it.hexamini.lcp;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainPActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_p);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void clickCalculateButton(View v) {
        Button calculateButton = (Button) findViewById(R.id.calculate_button); //creo l'oggetto
        /**
         * Codice per verificare l'inserimento dei sequenti. Il colore dell'edittext sarà rosso se
         * è presente un errore nel codice.
         */
        changeEditTextColor(R.id.seqSx, "#ff0000");
        changeEditTextColor(R.id.seqDx, "#ff0000");
        calculateButton.setText(R.string.btn_calculate_working);
    }

    private void changeEditTextColor(int id, String color) {
        EditText et = (EditText) findViewById(id);
        // setto il colore usando l'rgb
        et.setTextColor(Color.parseColor(color));
    }

}
