package br.com.hkp.searchengine.gui.basic;
 
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/*******************************************************************************
 * Tratador de eventos de mouse compartilhavel para os objetos do pacote gui                                                                            
 *
 * @author "Pedro Reis"
 * @since 15 de junho de 2020  v1.0
 * @version 1.0
 ******************************************************************************/
public final class MouseHandler implements MouseListener
{
    /**
     * Tratador de eventos de mouse compartilhavel pelos objetos GUI do pacote 
     * gui
     */
    public static final MouseHandler MOUSE_HANDLER = new MouseHandler();
    
    /*[00]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Executa o metodo mouseEnter() de um objeto MouseInterface quando o cursor
     * esta sobre o objeto
     * 
     * @param e O objeto de evento do mouse
     */
    @Override
    public void mouseEntered(MouseEvent e)
    {
        ((MouseInterface)(e.getSource())).mouseEnter();
    }//mouseEntered()
    
    /*[01]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
     /**
     * Executa o metodo mouseExit() de um objeto MouseInterface quando o cursor
     * esta sobre o objeto
     * 
     * @param e O objeto de evento do mouse
     */
    @Override
    public void mouseExited(MouseEvent e)
    {
        ((MouseInterface)(e.getSource())).mouseExit();
    }//mouseExited()
    
    /*[02]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
     /**
     * Executa o metodo mouseClick() de um objeto MouseInterface quando o cursor
     * esta sobre o objeto e o mouse eh clicado
     * 
     * @param e O objeto de evento do mouse
     */
    @Override
    public void mouseClicked(MouseEvent e)
    {
        ((MouseInterface)(e.getSource())).mouseClick();
    }//mouseClicked()
    
    /*[03]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    @Override
    public void mousePressed(MouseEvent e){}
    @Override
    public void mouseReleased(MouseEvent e){}
   

}//MouseHandler
