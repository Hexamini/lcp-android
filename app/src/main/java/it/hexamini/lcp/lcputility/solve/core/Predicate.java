package it.hexamini.lcp.lcputility.solve.core;

import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author amantova
 */
public class Predicate
{
    public ArrayList<String> prSx;
    public ArrayList<String> prDx;
    public Predicate( ArrayList<String> sx, ArrayList<String> dx )
    {
        prSx = new ArrayList<String>();
        prDx = new ArrayList<String>();
        prSx.addAll( sx );
        prDx.addAll( dx );
    }
    public Predicate( List<String> sx, List<String> dx )
    {
        prSx = new ArrayList<String>();
        prDx = new ArrayList<String>();
        prSx.addAll( sx );
        prDx.addAll( dx );
    }
    public void printPredicate()
    {
        System.out.println( prSx.toString() + "|-" + prDx.toString() );
    }
}