package br.com.hkp.searchengine.gui;

import static br.com.hkp.searchengine.gui.basic.MessageLabel.MESSAGE_LABEL;
import static br.com.hkp.searchengine.gui.basic.MouseHandler.MOUSE_HANDLER;
import br.com.hkp.searchengine.gui.basic.MouseInterface;
import static br.com.hkp.searchengine.util.Global.DARK_GREEN;
import static br.com.hkp.searchengine.util.Global.nicksUserArray;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/*******************************************************************************
 * Campo para digitar o nick do usuario para o qual serah realizada a pesquisa
 * 
 * @author "Pedro Reis"
 * @since 15 de junho de 2020 v1.0
 * @version 1.0
 ******************************************************************************/
public final class UserPanel extends JPanel 
{
    private final JTextFieldExtended jTextField;
    
    /*[00]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    public UserPanel()
    {
        super();
        setLayout(new BorderLayout());
        JLabel jLabel = new JLabel("Somente postagens do usu\u00e1rio:");
        add(jLabel, BorderLayout.NORTH);
        jTextField = new JTextFieldExtended();
        jTextField.addFocusListener(new FocusHandler());
        add(jTextField, BorderLayout.CENTER);
    }//construtor
    
    /*[01]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    public String getUser()
    {
        return jTextField.getText().trim();
    }//getUser()
    
   
    /***************************************************************************
     * 
     **************************************************************************/
    private final class JTextFieldExtended 
        extends JTextField implements MouseInterface
    {
        /*[00]------------------------------------------------------------------
        *
        ----------------------------------------------------------------------*/
        private JTextFieldExtended()
        {
            super();
            addMouseListener(MOUSE_HANDLER);
        }//construtor
        
        /*[01]------------------------------------------------------------------
        *
        ----------------------------------------------------------------------*/
        @Override
        public void mouseEnter()
        {
            MESSAGE_LABEL.setText
            (
                "Restringe a pesquisa a um usu\u00e1rio espec\u00edfico"
            );
        }//mouseEnter()
        
    }//classe interna JTextFieldExtended
    
    /***************************************************************************
     * 
     **************************************************************************/
    private class FocusHandler implements FocusListener
    {
        /*[00]------------------------------------------------------------------
        *
        ----------------------------------------------------------------------*/
        @Override
        public void focusLost(FocusEvent e)
        {
           if (!nicksUserArray.read(getUser())) 
               jTextField.setForeground(Color.RED);
           else
               jTextField.setForeground(DARK_GREEN);
        }//focusLost()
        
        /*[01]------------------------------------------------------------------
        *
        ----------------------------------------------------------------------*/
        @Override
        public void focusGained(FocusEvent e)
        {
            jTextField.setForeground(Color.BLACK);
        }//focusGained()
        
    }//classe interna FocusHandler
    
}//UserPanel

