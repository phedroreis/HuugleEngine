package br.com.hkp.searchengine.gui;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/*******************************************************************************
 * Essa classe controla os comandos para a thread principal acordar e realizar
 * uma pesquisa e dormir e esperar que o botao Pesquisar seja clicado.
 * 
 * @author "Pedro Reis"
 * @since 5 de maio de 2020 v1.0
 * @version 1.0
 ******************************************************************************/
public final class Concurrent
{
    /*
    Condition SEARCH suspende o loop que faz a chamada ao metodo que realiza a
    pesquisa ateh que o botao Pesquisar seja clicado
    */
    private final static Lock LOCK = new ReentrantLock();
    private final static Condition SEARCH = LOCK.newCondition();
    /*
    O botao Pesquisar soh eh habilitado para ler os campos da GUI com os 
    parametros de uma nova pesquisa quando a pesquisa corrente terminar
    */
    protected static AtomicBoolean canRefreshSearchParameters = 
        new AtomicBoolean(true);
    
    /*[00]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * A thread principal dorme ao executar este metodo e acorda ao 
     * startSearch() se acionado por um clique no botal Pesquisar
     */
    @SuppressWarnings("CallToPrintStackTrace")
    public static void awaitUntilSearchButtonClicked()
    {
        try
        {
            LOCK.lock();
            canRefreshSearchParameters.set(true);
            SEARCH.await();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
            System.exit(1);
        }
        finally
        {
            LOCK.unlock();    
        }
        
    }//awaitUntilSearchButtonClicked()
    
    /*[01]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Click no botao Pesquisar executa este metodo acordando a thread no 
     * programa principal para que realize uma pesquisa.
     */
    public static void startSearch()
    {
        try
        {
            LOCK.lock();
            canRefreshSearchParameters.set(false);
            SEARCH.signal();
        }
        finally
        {
            LOCK.unlock();     
        }
    }//startSearch()
       
}//classe Concurrent
