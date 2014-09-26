package it.hexamini.lcp.lcputility.solve.core;

/**
 *
 * @author amantova
 */
public class CompactInfo
{
    public String indexTable;
    public int indexEndGroup;
    public CompactInfo( String a, int b )
    {
        indexTable = a;
        indexEndGroup = b;
    }
    public void printIndexTable()
    {
        System.out.println( indexTable );
    }
}