package br.com.hkp.searchengine.gui.basic;

import static br.com.hkp.searchengine.gui.basic.MessageLabel.MESSAGE_LABEL;

/*******************************************************************************
 * Interface para os objetos GUI do pacote gui tratarem eventos de mouse
 * 
 * @author "Pedro Reis"
 * @since 15 de junho de 2020 v1.0
 * @version 1.0
 ******************************************************************************/
public interface MouseInterface
{
    /*[01]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Codigo que deve ser executado quando cursor estiver sobre o objeto
     */
    public void mouseEnter();
    /*[02]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Codigo para ser executado quando o cursor sair do objeto
     */
    public default void mouseExit()
    {
        MESSAGE_LABEL.setText(" ");
    }
    /*[03]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Codigo para ser executado quando o mouse for clicado com o cursor sobre o
     * objeto
     */
    public default void mouseClick() {}
   
}//interface MouseInterface
