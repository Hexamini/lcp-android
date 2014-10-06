package it.hexamini.lcp.lcputility.canvas;

import android.graphics.Paint;

import it.hexamini.lcp.lcputility.solve.core.Tree;

/**
 * Created by Andrea on 02/10/2014.
 */
public class TreeMargin
{
    public class Nodo
    {
        public final float LENGHT_DX;
        public final float LENGHT_SX;
        public float margin;
        public float relativeCenter;

        public Nodo treeDX;
        public Nodo treeSX;

        /**
         *
         * @param c : Poszione asse centrale
         * @param lSx : Lunghezza del sequente sinistro
         * @param lDx : Lunghezza del sequente destro
         * @param m : Margine tra i due sequenti
         */
        public Nodo( float c, float lSx, float lDx, float m  )
        {
            relativeCenter = c;
            LENGHT_SX = lSx;
            LENGHT_DX = lDx;
            margin = m;

            treeDX = null;
            treeSX = null;
        }

        public float[] getMeasures()
        {
            return new float[]{ margin, LENGHT_SX, LENGHT_DX, relativeCenter };
        }
    }

    private final int INCREMENT_MARGIN;
    //Larghezza della linea del centro
    private final int DELTA;
    private final int MARGIN_BETWEEN;

    private Nodo radice;
    private Tree.Nodo treeDerivateRadice;

    private Paint paint;

    /**
     * @param radiceTree : Nodo radice dell'albero di derivazione creato
     * @param setDistance : Distranza di default tra i due sequenti generati
     *                      dalla stessa derivazione
     * @param paintContext : Pennello impiegato per disegnare l'albero
     */
    public TreeMargin( Tree.Nodo radiceTree, int setDistance, Paint paintContext )
    {
        INCREMENT_MARGIN = 10;
        DELTA = 0;
        MARGIN_BETWEEN = setDistance;

        paint = paintContext;
        treeDerivateRadice = radiceTree;

        /*
        Il centro può essere un qualunque valore dato che il margine non
        risente di questo fattore, per comodita' lo setto a 0. Il margine
        applicato e' il valore di MARGIN_BETWEEN
        */
        radice = setDefaultMarginTree( treeDerivateRadice, 0 );

        /*
        Con i margini impostati a default c'e' il rischio di sovrapposizione
        dei rami e quindi e' necessario incrementare la loro distanza
        */
        adjustMargin( radice );
    }

    public Nodo getRadice()
    {
        return radice;
    }

    /**
     * @param center : Definisce il valore dell'asse principale dell'albero
     */
    public void setCenter( float center )
    {
        incrementChildrensCenter( radice, center - radice.relativeCenter );
    }

    /**
     * @param pattern : Nodo da confrontare se il sequente che rappresenta
     *                  supera il centro di altri nodi
     * @param matcher : Nodo da controllare se il pattern abbia superato il suo
     *                  centro
     * @return : Restituisce true se e' stato trovato un centro superato e
     *           aggiustato, altrimenti false
     */
    private boolean foundEraseAcross( Nodo pattern, Nodo matcher )
    {
        if( matcher != null && matcher != pattern )
        {
            //Punti di estremo sinistro e destro
            float ptExtremeSx = pattern.relativeCenter -
                                ( pattern.margin / 2 + pattern.LENGHT_SX );
            float ptExtremeDx = pattern.relativeCenter +
                                ( pattern.margin / 2 + pattern.LENGHT_DX );

            //Trovato centro superato
            if( ptExtremeSx < matcher.relativeCenter + DELTA ||
                ptExtremeDx > matcher.relativeCenter - DELTA )
            {
                matcher.margin += INCREMENT_MARGIN;

                /*
                Diminuisci il valore del centro dei figli del ramo sinitro ed
                incrementa quello dei figli del ramo destro
                */
                incrementChildrensCenter( matcher.treeSX, -INCREMENT_MARGIN / 2 );
                incrementChildrensCenter( matcher.treeDX, INCREMENT_MARGIN / 2 );

                return true;
            }
            else
            {

                //Scandaglia l'albero fino a trovare un centro superato o a visitare tutto l'albero
                return( foundEraseAcross( pattern, matcher.treeSX ) ||
                        foundEraseAcross( pattern, matcher.treeDX )
                );
            }
        }
        else return false;
    }

    /**
     * @param base : Nodo di partenza dell'albero di derivazione
     * @param center : Coordinata relativa all'asse centrale dell'albero
     * @return : Restituisce il nodo radice dell'albero TreeMargin con dentro
     *           la lunghezza dei livelli di derivazione con due sequenti, in
     *           cui si assume che la distanza tra di loro sia quella di
     *           default
     */
    private Nodo setDefaultMarginTree( Tree.Nodo base, float center )
    {
        if( base.treeSX != null )
        {
            if( base.treeDX == null ) return setDefaultMarginTree( base.treeSX, center );
            else
            {
                float lenghtSeqSx = paint.measureText( base.treeSX.getPredicate() );
                float lenghtSeqDx = paint.measureText( base.treeDX.getPredicate() );

                Nodo nodoAddCenter = new Nodo( center,
                                               lenghtSeqSx,
                                               lenghtSeqDx,
                                               MARGIN_BETWEEN
                                             );

                float newCenterSx = center - ( MARGIN_BETWEEN / 2 + lenghtSeqSx / 2 );
                float newCenterDx = center + ( MARGIN_BETWEEN / 2 + lenghtSeqDx / 2 );

                nodoAddCenter.treeSX = setDefaultMarginTree( base.treeSX, newCenterSx );

                nodoAddCenter.treeDX = setDefaultMarginTree( base.treeDX, newCenterDx );

                return nodoAddCenter;
            }
        }
        else return null;
    }

    /**
     * @param pattern : Nodo, a partire dal quale si controlla se i sequenti
     *                  che rappresenta non superino i centri di altri nodi
     */
    private void adjustMargin( Nodo pattern )
    {
        if( pattern != null )
        {
            //Trova ed elimina la sovrapposizione del nodo pattern con uno dei
            //centri
            foundEraseAcross( pattern, radice );

            adjustMargin( pattern.treeSX );
            adjustMargin( pattern.treeDX );
        }
    }

    /**
     * @param children : Nodo figlio da traslare il centro relativo
     * @param increment : Valore da sommare al margine tra i sequenti
     */
    private void incrementChildrensCenter( Nodo children, float increment )
    {
        if( children != null )
        {
            children.relativeCenter += increment;

            incrementChildrensCenter( children.treeSX, increment );
            incrementChildrensCenter( children.treeDX, increment );
        }
    }
}
