package it.hexamini.lcp.lcputility.solve.core;

import java.io.Serializable;
import java.util.ArrayList;

public class Tree implements Serializable
{
    private static final long serialVersionUID = 1L;

    //Nodo utilizzati in questo istante dai thread
    private ArrayList<Nodo> workingNode;
    private Nodo radice;

    public class Nodo
    {
        private Derivate info;
        public Nodo treeDX;
        public Nodo treeSX;
        /**
         * @param a : contenuto del nodo, sequente piu' regola
         * @param b : sequente derivato
         * @param c : eventuale biforcazione del sequente in due rami
         */
        public Nodo( Derivate a, Nodo b, Nodo c )
        {
            info = a;
            treeSX = b;
            treeDX = c;
        }

        /**
         * @return Restituisce sotto forma di String il predicato inserito nel nodo
         */
        public String getPredicate()
        {
            return info.predicate;
        }

        /**
         * @return Restituisce sotto forma di String la regola inserita nel nodo
         */
        public String getRule()
        {
            return info.rule;
        }

        /**
         * @param rule : scrivi nel campo info la regola utilizzata per derivare il sequente
         */
        public void setDerivateRule( String rule )
        {
            info.setRule( rule );
        }
    }
    /**
     * Istanzia un albero binario vuoto
     */
    public Tree()
    {
        radice = new Nodo( null, null, null );
        workingNode = new ArrayList<Nodo>();
        workingNode.add( radice );
    }

    /**
     * @param seq : sequente da inserire nel campo info del nodo
     * @param idWorker : id del thread che lavora a quel ramo dell'albero
     * @return Aggiunge un nodo all'albero sul ramo sinistro contenente il sequente con la regola da applicare
     */
    public void addSx( Derivate seq, int idWorker )
    {
        Nodo currentPoint = workingNode.get( idWorker );
        Nodo p = new Nodo( seq, null, null );
        currentPoint.treeSX = p;
        workingNode.set( idWorker, p );
    }
    /**
     * @param seq : sequente da inserire nel campo info del nodo
     * @param idWorker : id del thread che lavora a quel ramo dell'albero
     * @return Aggiunge un nodo all'albero contenente il sequente con la regola da applicare
     */
    public void addDx( Derivate seq, int idWorker )
    {
        Nodo currentPoint = workingNode.get( idWorker );
        Nodo p = new Nodo( seq, null, null );
        currentPoint.treeDX = p;
        workingNode.set( idWorker, p );
    }
    /**
     * @param idSxWorker : id thread che lavorera sul nuovo ramo sinistro dell'albero
     * @param idDxWorker : id thread che lavorera sul nuovo ramo destro dell'albero
     * @return Crea un nuovo lavoratore sul nodo puntato da quello in corso in cui, quello nuovo procedera a destra e quello attuale a sinistra dell'albero
     */
    public void branchSplit( int idSxWorker, int idDxWorker )
    {
        workingNode.add( idDxWorker, workingNode.get( idSxWorker ) );
    }
    /**
     * @return Restituisce il nodo radice dell'albero
     */
    public Nodo getRadice()
    {
        return radice;
    }
    /**
     * @param rule : regola applicata da inserire nel campo info del nodo
     * @param idWorker : id del thread che lavora a quel nodo dell'albero
     */
    public void ruleUsed( String rule, int idWorker )
    {
        Nodo work = workingNode.get( idWorker );
        work.setDerivateRule( rule );
    }
    /**
     *
     * @return Rimuove tutti i puntatori ai nodi e setta il primo sul nodo radice
     */
    public void reset()
    {
        workingNode.clear();
        workingNode.add( 0, radice );
    }
}