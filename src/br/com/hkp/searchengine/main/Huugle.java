package br.com.hkp.searchengine.main;

import br.com.hkp.searchengine.gui.MainFrame;
import br.com.hkp.searchengine.util.Global;
import static br.com.hkp.searchengine.util.Global.*;
import java.awt.Toolkit;
import java.io.IOException;
import javax.swing.JOptionPane;


/*******************************************************************************
 * Aplicacao Huugle - Um motor de busca para pesquisa no acervo do CC
 * 
 * @author "Pedro Reis"
 * @since 18 de junho de 2020 v1.0
 * @version 1.0
 ******************************************************************************/
public class Huugle
{
    /*[00]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Encerra a aplicacao fechando os arquivos abertos
     * 
     * @param code 0 em caso de terminacao normal e 1 para termino com excecao
     */
    public static void finalizer(int code)
    {
        try
        {
            /*
            Evita que uma operacao de leitura de arquivo seja realizada depois
            do inicio da rotina de encerramento do programa. Uma vez que esta
            rotina irah fechar todos os arquivos abertos.
            */
            Global.FINALIZE_LOCK.lock();
            
            topicsWordFinder.close();
            
            postsWordFinder.close();
                      
            topicsPostRegArray.close();
            
            postsPostRegArray.close();
            
            System.exit(code);
            
        } //finalizer()
        catch (IOException e)
        {
            System.exit(1);
        }
        finally
        {
            /*
            Jamais serah executado pois o programa encerrarah antes
            */
            Global.FINALIZE_LOCK.unlock();
        }
    }//finalizer()

    /*[01]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Programa principal
     * 
     * @param args n/a
     */
    @SuppressWarnings("CallToPrintStackTrace")
    public static void main(String[] args) 
    {
        try
        {
            initializer();
            MainFrame.execute();
        } //main()
        catch (IOException e)
        {
            e.printStackTrace();
            Toolkit.getDefaultToolkit().beep();
                    
            //Abre janelinha de mensagem informando o erro
            JOptionPane.showMessageDialog
            (
                null,
                "Erro fatal: " + e.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE
            );
            
            finalizer(1);
        }
    }//main()
    
}//classe Huugle
