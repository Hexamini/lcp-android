package it.hexamini.lcp;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import it.hexamini.lcp.lcputility.check.CheckGraph;
import com.github.amlcurran.showcaseview.*;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

/**Importo tutto il necessario per la creazione del
 * help al primo avvio dell'app
 */


public class MainPActivity extends ActionBarActivity implements View.OnClickListener,OnShowcaseEventListener, AdapterView.OnItemClickListener{

    public final static String PARAM_SEQUENTS_SX = "it.hexamini.lcp.PARAM_SEQUENTS_SX";
    public final static String PARAM_SEQUENTS_DX = "it.hexamini.lcp.PARAM_SEQUENTS_DX";
    private ShowcaseView firstRun; //la devo dichiarare qui perchè viene usata da onClick.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_p);
        firstRunIntroduction();
    }

    @Override
    public void onClick(View view) {
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
    }

    @Override
    public void onShowcaseViewShow(ShowcaseView showcaseView) {
        //setto il bottone calcola e reset non cliccabili
        setClickable(false, R.id.btn_calculate_button);
        setClickable(false, R.id.reset_button);
    }

    @Override
    public void onShowcaseViewHide(ShowcaseView showcaseView) {
    }

    @Override
    public void onShowcaseViewDidHide (ShowcaseView showcaseView) {
        setClickable(true, R.id.reset_button);
        setClickable(true, R.id.btn_calculate_button);
        //faccio partire la slide successiva
        secondSlide();

        /**
         * NOTA: Non riesco a capire il perchè, ma sbloccando prima i bottoni reset eppoi
         * chiamando la seconda slide sequenza viene invertita. Probabilmente sarà un bug
         * della libreria.
         */
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
        /**
         * Codice per verificare l'inserimento dei sequenti. Il colore dell'edittext sarà rosso se
         * è presente un errore nel codice.
         */
        CheckGraph controlInput;
        controlInput = new CheckGraph();
        EditText seqSx= (EditText) findViewById(R.id.seqSx);
        EditText seqDx= (EditText) findViewById(R.id.seqDx);
        //importo i dati su stringhe
        String inputSeqSx= seqSx.getText().toString();
        String inputSeqDx= seqDx.getText().toString();
        if (!inputSeqSx.isEmpty() || !inputSeqDx.isEmpty()) //almeno uno dei due non deve essere vuoto
        {
            if (controlInput.isCorrect(inputSeqSx)) {
                if (controlInput.isCorrect(inputSeqDx)) {
                    //gli input sono corretti e si passa alla vera computazione del sequente
                    changeButtonText(R.id.btn_calculate_button, R.string.btn_calculate_working); //cambio il testo del bottone

                    Intent intent = new Intent( this, CanvasSolvePActivity.class );
                    intent.putExtra( PARAM_SEQUENTS_SX, inputSeqSx );
                    intent.putExtra( PARAM_SEQUENTS_DX, inputSeqDx );

                    startActivity( intent );
                } else
                    changeEditTextColor(R.id.seqDx, "#ff0000"); //coloro l'edittext di rosso
            } else
                changeEditTextColor(R.id.seqSx, "#ff0000"); //coloro l'edittext di rosso
        }
    }

    public void clickResetButton(View v){
        /**
         * Alla pressione di questo bottone gli edittext ritorneranno a essere vuoti
         */
        EditText seqSx= (EditText) findViewById(R.id.seqSx);
        EditText seqDx= (EditText) findViewById(R.id.seqDx);
        seqSx.setText(""); //svuoto entrambi gli edittext, li resetto
        seqDx.setText("");
        changeEditTextColor(R.id.seqSx, "#808080"); //corrisponde a #ff787878 di android
        changeEditTextColor(R.id.seqDx, "#808080");
        changeButtonText(R.id.btn_calculate_button, R.string.calculate_result); //ripristino il testo originale del bottone
    }

    private void changeEditTextColor(int id, String color) {
        EditText et = (EditText) findViewById(id);
        // setto il colore usando l'rgb
        et.setTextColor(Color.parseColor(color));
    }

    private void changeButtonText (int id, int idText) {
        Button changeName= (Button) findViewById(id);
        //cambio il testo del bottone con quello passato nella variabile idText
        changeName.setText(idText);
    }

    private void firstRunIntroduction(){
        //mi fa l'animazione del primo avvio
        firstSlide();
        //la seconda slide viene avviata da onShowcaseViewDidHide
    }

    private void firstSlide(){
        ViewTarget calculateButtonTarget= new ViewTarget(R.id.btn_calculate_button, this);
        firstRun= new ShowcaseView.Builder(this, true)
                .setTarget(calculateButtonTarget)
                .setContentTitle(R.string.calculate_result) //titolo del testo
                .setContentText(R.string.btn_calculate_intro) //testo introduttivo
                .doNotBlockTouches() //impedisce il touch se non sull'ok e sul bottone
                .setStyle(R.style.AppTheme) //tema dell'intro
                .setShowcaseEventListener(this)
                .singleShot(R.id.btn_calculate_button) //visualizzato solamente al primo avvio
                .build();
    }

    private void secondSlide(){
        ViewTarget resetButtonTarget= new ViewTarget(R.id.reset_button, this);
        firstRun=new ShowcaseView.Builder(this, true)
                .setTarget(resetButtonTarget)
                .setContentTitle(R.string.reset) //titolo del testo
                .setContentText(R.string.btn_reset_intro) //testo introduttivo
                .doNotBlockTouches() //impedisce il touch se non sull'ok e sul bottone
                .setStyle(R.style.AppTheme) //tema dell'intro
                .setShowcaseEventListener(this)
                .singleShot(R.id.reset_button) //visualizzato solamente al primo avvio
                .build();
    }

    private void setClickable(boolean mode,int id){
        //cambia lo stato di un bottone da cliccabile o no
        Button setClickable= (Button) findViewById(id);
        setClickable.setEnabled(mode);
    }

}
