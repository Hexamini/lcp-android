package it.hexamini.lcp;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import it.hexamini.lcp.lcputility.solve.Tree;
import it.hexamini.lcp.lcputility.solve.check.CheckGraph;
import it.hexamini.lcp.lcputility.solve.Solve;


public class MainPActivity extends ActionBarActivity implements Runnable{

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
                    Thread solveSeq= Thread.currentThread(); //creo un thread per il main principale
                    Thread madeActivity= new Thread(this, "Activity Risultato");
                    //avvio il thread che creerà la nuova Activity
                    madeActivity.start();

                    madeSolution(inputSeqSx, inputSeqDx); //questo creerà solve e risolverà il sequente
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

    private void madeSolution(String inputSeqSx, String inputSeqDx){
        //Dichiaro l'oggetto solve
        Solve solution=new Solve(inputSeqSx, inputSeqDx);
        solution.treeLeaf(); //ottengo il risultato.
    }

    /** Gestione dei thread. In questo caso il thread è unico in quanto facciamo solo uno sdoppiamento
     * per velocizzare la creazione della nuova activity e la computazione del sequente, che a volte può
     * richiedere del tempo, così rendiamo più efficente la cosa.
     */
    @Override
    public void run (){
        changeButtonText(R.id.btn_calculate_button, R.string.btn_calculate_working); //cambio il testo del bottone
        /**Implemento il codice per una nuova activity. Una volta creata una nuova activity
         * questa si dovrà mettere in attesa finchè la computazione del sequente non è terminata.
         */
    }
}


/**
 * Nota Bene: secondo me i thread andrebbero invertiti, il thread principale dovrebbe creare
 * la nuova activity, quello secondario dovrebbe semplicemente risolvere il sequente. Logicamente
 * parlando, a mio avviso, sarebbe meglio così, perchè alla fine del thread secondario rimarrebbe
 * quello principale che ha creato appunto l'activity con stampato il risultato. Il problema è che
 * non saprei come passare le stringhe dell'input destro e sinistro.
 */
