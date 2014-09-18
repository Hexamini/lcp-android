package it.hexamini.lcp.lcputility;

import java.util.ArrayList;
public class Tree
{
    //Nodo utilizzati in questo istante dai thread
    private ArrayList<Nodo> workingNode;
    private Nodo radice;
    private class Nodo
    {
        private Derivate info;
        private Nodo treeDX;
        private Nodo treeSX;
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
     * @param idWorker : id del lavoratore che puntera alla radice
     * @return Rimuove tutti i puntatori ai nodi e setta il primo sul nodo radice
     */
    public void reset()
    {
        workingNode.clear();
        workingNode.add( 0, radice );
    }
    /**
     * @return Visualizza il contenuto dei nodi dell'albero
     */
    public void stampa()
    {
        Nodo rSx = radice.treeSX;
        Nodo rDx = radice.treeDX;
//Stampa derivazione sequente
        stampaRic( rSx );
//Stampa derivazione sequente negato
        stampaRic( rDx );
    }
    /**
     *
     * @param r : radice dell'albero
     * @return Scorre l'albero ricorsivamente stampanto il contenuto dei nodi
     */
    private void stampaRic( Nodo r )
    {
        if( r != null )
        {
            r.info.print();
            stampaRic( r.treeSX );
            stampaRic( r.treeDX );
        }
    }
}