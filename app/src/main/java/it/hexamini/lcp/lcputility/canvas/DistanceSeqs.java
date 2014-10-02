package it.hexamini.lcp.lcputility.canvas;

import java.util.ArrayList;

/**
 * Created by Andrea on 01/10/2014.
 */
public class DistanceSeqs
{
    //Lista centri immessi
    private ArrayList<Float> listCenter;

    private ArrayList<Integer> marginSeqs;

    private final int DISTANCE_DEFAULT;
    private final int INCREMENT_DISTANCE;
    //Numero di richieste fatta alla classe per ottenere il margine corretto
    private int nMarginsProvided;

    /**
     * @param distanceBase : Spazio tra il sequente sinistro e quello destro dello stesso livello
     *                       di derivazione
     */
    public DistanceSeqs( int distanceBase )
    {
        DISTANCE_DEFAULT = distanceBase;
        INCREMENT_DISTANCE = 10;
        nMarginsProvided = 0;
        listCenter = new ArrayList<Float>();
        marginSeqs = new ArrayList<Integer>();
    }

    /**
     * @param meauserTextSx : Lunghezza del sequente sinistro
     * @param meauserTextDx : Lunghezza del sequente destro
     * @param center : Centro del sequente da voler inserito
     * @return : Ritorna la distanza tra i sequenti dello stesso livello di derivazione
     * @throws ErrorDistance : In caso di superamento del centro di un livello di derivazione,
     *                         viene invocata l'eccezzione ErrorDistance
     */
    public int getDistance( float meauserTextSx, float meauserTextDx, float center ) throws ErrorDistance
    {
        //Aggiungo il centro con la linea associata se non presente
        if( !listCenter.contains( center ) ) listCenter.add( center );

        nMarginsProvided++;

        float xPosSx = 0;
        float xPosDx = 0;

        boolean applyDefault = ( marginSeqs.size() < nMarginsProvided );

        //Nessun margine calcolato, primo inserito
        if( applyDefault )
        {
            //Lato sinistro
            xPosSx = center - ( meauserTextSx + DISTANCE_DEFAULT );
            //Lato destro
            xPosDx = center + DISTANCE_DEFAULT;
        }
        else
        {
            xPosSx = center - ( meauserTextSx + marginSeqs.get( nMarginsProvided - 1 ) );
            xPosDx = center + marginSeqs.get( nMarginsProvided - 1 );
        }

        int indexOverCenterSx = overCenter( meauserTextSx, xPosSx );
        int indexOverCenterDx = overCenter( meauserTextDx, xPosDx );

        if( indexOverCenterSx != -1 || indexOverCenterDx != -1 )
        {
            int indexOverCenter = ( indexOverCenterSx != -1 ) ? indexOverCenterSx : indexOverCenterDx;

            System.out.println( indexOverCenter + " " + marginSeqs.get( indexOverCenter )  );

            marginSeqs.set( indexOverCenter, marginSeqs.get( indexOverCenter ) + INCREMENT_DISTANCE );
            //Rimuovo tutti i centri
            listCenter.clear();
            throw new ErrorDistance();
        }
        else
        {
            if( applyDefault )
            {
                //Aggiunto quello di default
                marginSeqs.add( DISTANCE_DEFAULT );
                return DISTANCE_DEFAULT;
            }

            else return marginSeqs.get( nMarginsProvided - 1 );
        }
    }

    /**
     * Setta nMarginsProvider a 0 facendo ricominciare la lettura da marginSeqs dall'inizio
     */
    public void rewindStart()
    {
        //Ricomincia a restituire dal primo immesso
        nMarginsProvided = 0;
    }

    /**
     * @param meauser : Lunghezza del sequente
     * @param xPos : Valore della cordinata X dove si desidera posizionare il testo
     * @return : Ritorna l'indice del centro che si attraversa, altrimenti -1
     */
    private int overCenter( float meauser, float xPos )
    {
        boolean foundAcross = false;

        int indListCenter = 0;
        //Escluso l'ultimo dato che ci scrivo sopra
        int lenghtListCenter = listCenter.size() - 1;

        while( indListCenter < lenghtListCenter && !foundAcross )
        {
            //Condizione per cui un sequente superi la linea del centro
            //NOTA: potrebbe essere che un sequente superi la linea del centro senza intersecarla
            if( xPos <= listCenter.get( indListCenter ) && xPos + meauser > listCenter.get( indListCenter ) ) foundAcross = true;
            else indListCenter++;
        }

        return ( foundAcross ) ? indListCenter : -1;
    }
}
