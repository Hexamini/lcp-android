package it.hexamini.lcp.lcputility.solve.core;

public class Derivate
{
    public String predicate;
    public String rule;
    /**
     * @param a : parte sinistra del sequente
     */
    public Derivate( String a )
    {
        predicate = new String( a );
        rule = new String();
    }

    /**
     * @param ruleUse : setta come regola utilizzata per derivare il sequente il contenuto di <i>ruleUse</i>
     */
    public void setRule( String ruleUse )
    {
        rule = ruleUse;
    }
}