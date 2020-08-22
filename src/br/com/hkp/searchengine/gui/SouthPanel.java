package br.com.hkp.searchengine.gui;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JSeparator;

/*******************************************************************************
 *
 * @author "Pedro Reis"
 ******************************************************************************/
public final class SouthPanel extends JPanel
{
    private final StatusBarPanel statusBarPanel;
    private final JSlidersPanel jSlidersPanel;
    
    /*[00]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    public SouthPanel()
    {
        super();
        
        setLayout(new BorderLayout());
        
        add(new JSeparator(), BorderLayout.NORTH);
       
        jSlidersPanel = new JSlidersPanel();
        
        add(jSlidersPanel, BorderLayout.CENTER);
        
        statusBarPanel = new StatusBarPanel();
         
        add(statusBarPanel, BorderLayout.SOUTH);
        
    }//construtor
    
    /*[01]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    public int[] getValues()
    {
        return jSlidersPanel.getValues();
    }//getValues()
    
       
}//classe SouthPanel

