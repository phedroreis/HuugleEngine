package br.com.hkp.searchengine.gui.basic;

import javax.swing.JLabel;

/*******************************************************************************
 * Um objeto do tipo JLabel para exibir mensagens na barra de status da janela *
 * principal                                                                   *
 *                                                                             *
 * @author "Pedro Reis"                                                        * 
 * @since 15 de junho de 2020 v1.0                                             *
 * @version 1.0                                                                *
 ******************************************************************************/
public final class MessageLabel extends JLabel
{
    /**
     * Um objeto compartilhavel por todos os objetos do pacote gui e que tem por
     * funcao exibir mensagens na barra de status da janela principal
     */
    public final static MessageLabel MESSAGE_LABEL = new MessageLabel();
    
    /*[00]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Constroi o objeto e imprime no label uma string em branco
     */
    public MessageLabel()
    {
        super(" ");
    }//construtor
    
    /*[01]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Escreve um texto no label
     * 
     * @param msg O texto
     */
    @Override
    public void setText(String msg)
    {
        super.setText(" " + msg);
    }//setText()
    
}//MessageLabel
