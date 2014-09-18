package it.hexamini.lcp.lcputility;

/**
 *
 * @author andrea
 */
public class legOperand
{
    private String tmpPredicate;
    public legOperand(){}
    /**
     *
     * @param predicate
     * @return
     */
    public String addPriority( String predicate )
    {
        tmpPredicate = predicate;
//Il parametro predicate potrebbe essere la stringa vuota
        if( tmpPredicate.length() > 0 )
        {
//La ricerca degli operatori e in ordine di forza del legame, l'ordine di ricerca definisce la priorita
            tmpPredicate = priorityOperand( '-', false );
            tmpPredicate = priorityOperand( '&', true );
            tmpPredicate = priorityOperand( 'v', true );
            tmpPredicate = priorityOperand( '>', true );
        }
        return tmpPredicate;
    }
    /**
     *
     * @param posOperand
     * @return
     */
    private int sqrAfterOperand( int posOperand )
    {
//Posizione della parentesi tonda chiusa da inserire
        int indexEnd = posOperand + 1;
        int cntSquare = 0;
        do
        {
            if( tmpPredicate.charAt( indexEnd ) == ')' ) cntSquare--;
            else if( tmpPredicate.charAt( indexEnd ) == '(' ) cntSquare++;
            indexEnd++;
        } while( cntSquare > 0 );
        return indexEnd;
    }
    /**
     *
     * @param posOperand
     * @return
     */
    private int sqrBeforeOperand( int posOperand )
    {
//Posizione della parentesi tonda aperta da inserire
        int indexStart = posOperand - 1;
        int cntSquare = 0;
//L'operatore e' all'inizio del sequente
        if( indexStart < 0 ) return 0;
        do
        {
            if( tmpPredicate.charAt( indexStart ) == '(' ) cntSquare--;
            else if( tmpPredicate.charAt( indexStart ) == ')' ) cntSquare++;
            indexStart--;
        } while( cntSquare > 0 );
        return indexStart + 1;
    }
    /**
     * @param operand : operatore da cercare nella string tmpPredicate
     * @param isBin : <i><u>true</u></i> se l'operatore è binario, <i><u>false</u></i> se unario
     * @return Restituisce tmpPredicate con l'aggiunta della parentesi tonde attorno all'operatore e ai suoi argomenti
     */
    private String priorityOperand( char operand, boolean isBin )
    {
        int posPr = 0;
        while( posPr < tmpPredicate.length() )
        {
/*
Cerchiamo l'operatore e aggiungiamo le parentesi attorno
il suo operando
*/
            if( tmpPredicate.charAt( posPr ) == operand )
            {
                sqrOperand( posPr, isBin );
                posPr += 2;
            }
            else posPr++;
        }
        return tmpPredicate;
    }
    /**
     *
     * @param indStart
     * @param indEnd
     * @return
     */
    private void insertSquare( int indStart, int indEnd )
    {
//Parte del sequente prima dell'operatore negato
        String preArg = tmpPredicate.substring( 0, indStart );
//Parte del sequente contenente l'operatore e l'operando
        String arg = tmpPredicate.substring( indStart, indEnd );
//Il restante del sequente dopo l'operando
        String postArg = tmpPredicate.substring( indEnd );
        preArg += '(';
        postArg = ')' + postArg;
        tmpPredicate = preArg + arg + postArg;
    }
    /**
     *
     * @param posOperand
     */
    private void sqrOperand( int posOperand, boolean isBin )
    {
        if( isBin ) insertSquare( sqrBeforeOperand( posOperand ), sqrAfterOperand( posOperand ) );
        else insertSquare( posOperand, sqrAfterOperand( posOperand ) );
    }
}
