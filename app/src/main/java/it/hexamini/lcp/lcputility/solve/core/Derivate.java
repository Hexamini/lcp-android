package it.hexamini.lcp.lcputility.solve.core;

public class Derivate
{
    private String predicate;
    private String rule;
    /**
     * @param a : parte sinistra del sequente
     */
    public Derivate( String a )
    {
        predicate = new String( a );
        rule = new String();
    }
    /**
     * @return Restituisce la stringa del predicato
     */
    public String getPredicate()
    {
        return predicate;
    }
    /**
     * @return Restituisce la stringa della regola
     */
    public String getRule()
    {
        return rule;
    }
    /**
     * @param ruleUse : setta come regola utilizzata per derivare il sequente il contenuto di <i>ruleUse</i>
     */
    public void setRule( String ruleUse )
    {
        rule = ruleUse;
    }
}