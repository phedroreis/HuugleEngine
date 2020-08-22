package br.com.hkp.searchengine.gui.basic;

import static br.com.hkp.searchengine.gui.basic.MessageLabel.MESSAGE_LABEL;
import static br.com.hkp.searchengine.gui.basic.MouseHandler.MOUSE_HANDLER;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/*******************************************************************************
 * Um painel com dois botoes de radio
 * 
 * @author "Pedro Reis"
 * @since 15 de junho de 2020 v1.0
 * @version 1.0
 ******************************************************************************/
public final class RadioButton extends JPanel 
{
    private final JRadioButtonExtended button1;
    private final JRadioButtonExtended button2;
    
    /*[00]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    public RadioButton
    (
        final String label1, 
        final int menemon1,
        final String mouseText1,
        final String label2, 
        final int menemon2,
        final String mouseText2,
        final boolean firstSelected
    )
    {
        super();
        button1 = new JRadioButtonExtended(label1, firstSelected, mouseText1);
        button1.setMnemonic(menemon1);
        button2 = new JRadioButtonExtended(label2, !firstSelected, mouseText2);
        button2.setMnemonic(menemon2);
        add(button1);
        add(button2);
        
        ButtonGroup group = new ButtonGroup();
        group.add(button1);
        group.add(button2);
    }//construtor
    
    /*[01]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    public boolean firstSelected()
    {
        return button1.isSelected();
    }//firstSelected()
    
    /***************************************************************************
     * 
     **************************************************************************/
    private final class JRadioButtonExtended 
        extends JRadioButton implements MouseInterface
    {
        private final String mouseEnter;
        
        /*[00]------------------------------------------------------------------
        *
        ----------------------------------------------------------------------*/
        private JRadioButtonExtended
        (
            final String label,
            final boolean selected,
            final String mouseEnter
        )
        {
            super(label, selected);
            this.mouseEnter = mouseEnter;
            addMouseListener(MOUSE_HANDLER);
        }//construtor
        
        /*[01]------------------------------------------------------------------
        *
        ----------------------------------------------------------------------*/
        @Override
        public void mouseEnter()
        {
            MESSAGE_LABEL.setText(mouseEnter);
        }//mouseEnter()
        
    }//classe interna JRadioButtonExtended
    
    
}//classe RadioButton
