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
     * @return Stampa a video la regola affiancata dal sequente
     */
    public String getInformation()
    {
        return predicate + " " + rule;
    }
    /**
     * @param ruleUse : setta come regola utilizzata per derivare il sequente il contenuto di <i>ruleUse</i>
     */
    public void setRule( String ruleUse )
    {
        rule = ruleUse;
    }
}