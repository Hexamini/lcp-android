package it.hexamini.lcp.lcputility.solve.core;

/**
 *
 * @author amantova
 */
public class ReferenceTable
{
    //Numero di righe da agguingere ad ogni chiamata di addRow
    private final int incrementRow;
    //Numero di righe della tabella
    private int size;
    //Tabella dei riferimenti tra singolo carattere e gruppo di proposizioni
    private ReferenceLine[] tableRef;
    private class ReferenceLine
    {
        public boolean bracketAdd;
        public String reference;
        public ReferenceLine( String a, boolean b )
        {
            reference = new String( a );
            bracketAdd = b;
        }
    }
    public ReferenceTable( int numRow )
    {
        tableRef = new ReferenceLine[ numRow ];
        incrementRow = 10;
        size = 0;
    }
    public String getReference( String index, boolean returnBracket )
    {
//Rimuoco i cancelletti che delimitano il numero
        index = index.substring( 1, index.length() - 1 );
//Estrapolo l'intero contenuto
        int row = Integer.parseInt( index );
//Restituisco la stringa referenziata
        if( returnBracket && tableRef[ row ].bracketAdd ) return '(' + tableRef[ row ].reference + ')';
        else return tableRef[ row ].reference;
    }
    public String insertReference( String pr, boolean opt )
    {
        if( size == tableRef.length ) addRows();
//Nel caso si richiedesse di inserire un predicato gia' inserito
//e' inutile crearli una riga apposita, restituiamo l'indice
//corrispondente.
        int ind = alreadyInsert( pr );
        if( ind != -1 ) return '#'+Integer.toString( ind )+'#';
//Altrimenti aggiungo alla tabella il riferimento
        tableRef[ size ] = new ReferenceLine( pr, opt );
        String index = '#'+Integer.toString( size )+'#';
//Incremento il contatore di righe
        size++;
//Restituisco l'indice che e' stato asssiocato a pr nella
//ReferenceTable sotto forma di stringa contrassegnata dai caratteri
//cancelletto
        return index;
    }
    public void printAllReference()
    {
        for( int i = 0; i < size; i++ ) System.out.println( i + " " + tableRef[i].reference + " " + tableRef[i].bracketAdd );
    }
    private int alreadyInsert( String pr )
    {
        boolean found = false;
        int i = 0;
        while( i < size && !found )
        {
//Trovato e mantengo la posizione in cui l'ho trovato
            if( tableRef[i].reference.equals( pr ) ) found = true;
//Continuo a cercarlo
            else i++;
        }
        if( found ) return i;
        else return -1;
    }
    private void addRows()
    {
        ReferenceLine[] tempTable = new ReferenceLine[ size + incrementRow ];
        System.arraycopy( tableRef, 0, tempTable, 0, size );
        tableRef = tempTable;
    }
}