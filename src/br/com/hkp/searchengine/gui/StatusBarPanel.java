package br.com.hkp.searchengine.gui;

import static br.com.hkp.searchengine.gui.basic.MessageLabel.MESSAGE_LABEL;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/*******************************************************************************
 * Uma barra de status na janela principal do programa.
 * 
 * @author "Pedro Reis"
 * @since 15 de junho de 2020 v1.0
 * @version 1.0
 ******************************************************************************/
public final class StatusBarPanel extends JPanel
{
        
    /*[00]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    public StatusBarPanel()
    {
        super();
        setLayout(new BorderLayout());
       
        add(MESSAGE_LABEL, BorderLayout.WEST);
        setBorder(BorderFactory.createLoweredBevelBorder());
        setOpaque(true);
        setBackground(Color.LIGHT_GRAY); 
    }//construtor
    
  
}//StatusBarPanel
