package it.hexamini.lcp.lcputility;

public class Derivate
{
    private String predicate;
    private String rule;
    /**
     * @param a : parte sinistra del sequente
     * @param b : parte destra del sequente
     */
    public Derivate( String a )
    {
        predicate = new String( a );
        rule = new String();
    }
    /**
     * @return Stampa a video la regola affiancata dal sequente
     */
    public void print()
    {
        System.out.println( predicate + " {" + rule + '}' );
    }
    /**
     * @param ruleUse : setta come regola utilizzata per derivare il sequente il contenuto di <i>ruleUse</i>
     */
    public void setRule( String ruleUse )
    {
        rule = ruleUse;
    }
}