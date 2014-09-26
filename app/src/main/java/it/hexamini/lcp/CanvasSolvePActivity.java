package it.hexamini.lcp;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import it.hexamini.lcp.lcputility.solve.core.Solve;
import it.hexamini.lcp.lcputility.solve.core.Tree;

public class CanvasSolvePActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String seqSx = intent.getStringExtra( MainPActivity.PARAM_SEQUENTS_SX );
        String seqDx = intent.getStringExtra( MainPActivity.PARAM_SEQUENTS_DX );

        Solve solution = new Solve( seqSx, seqDx );
        Tree treeSolve = solution.treeLeaf();

        //disegno le stringhe sullo schermo partendo dall'angolo in basso a sinsitra
        drawSolveSolution(treeSolve);
        /**TextView t = new TextView( this );
         *
         * .::Gli oggetti devono essere presenti anche in activity_canvas_solve_p.xml
         *    Così si perde la corrispondenza con l'editor! Poi impazziamo a trovare i glich grafici
         *
         *t.setTextSize(20);
         *t.setText(seqSx + " " + seqDx);
         *
         *setContentView(t);
         */
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.canvas_solve, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void drawSolveSolution(Tree sol){
        /** Questa funzione stampa l'albero sullo schermo. Bisogna partire in basso a sinistra e
         * spostarsi in alto a ogni ricorsione. Bisogna spostarsi lateralmente per ogni ramo che si
         * va a stampare o si potrebbe sovrascrivere stringe già scritte.
         */
        Tree.Nodo n = sol.getRadice();

        //Questa andrà stampata nello schermo
        System.out.println( n.treeSX.info.getPredicate() + " " + n.treeSX.info.getRule() );

        //Manca la ricosione
    }
}
