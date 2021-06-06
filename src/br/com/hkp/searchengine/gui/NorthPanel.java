package br.com.hkp.searchengine.gui;

import br.com.hkp.searchengine.gui.basic.JMaxResultsField;
import static br.com.hkp.searchengine.gui.basic.MessageLabel.MESSAGE_LABEL;
import static br.com.hkp.searchengine.gui.basic.MouseHandler.MOUSE_HANDLER;
import br.com.hkp.searchengine.gui.basic.MouseInterface;
import static br.com.hkp.searchengine.util.Global.helpFile;
import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.io.IOException;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/*******************************************************************************
 *
 * @author "Pedro Reis"
 ******************************************************************************/
public final class NorthPanel extends JPanel
{
    private Icon normalIcon;
    private Icon focusIcon;
    private final ButtonsPanel buttonsPanel;
    private final MaxResultsPanel maxResultsPanel;
    private final JLabel helpLabel;
    private final JPanel helpPanel;
    private final Desktop desktop;
    
    private long before; 
   
    /*[00]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    public NorthPanel()
    {
        super();
        
        setLayout(new BorderLayout(3,0));
                 
        desktop = Desktop.getDesktop(); 
        
        before = 0;
       
        try
        {
            normalIcon =
                new ImageIcon(getClass().getResource("help.png"));
            focusIcon = 
                new ImageIcon(getClass().getResource("helpblue.png"));
        }
        catch (Exception e)
        {
            System.err.println(e);
        }
      
        helpLabel = new JLabelExtended(normalIcon);
        
        helpPanel = new JPanel();
        helpPanel.setSize(32,22);
        helpPanel.add(helpLabel);
        helpPanel.setBorder(BorderFactory.createTitledBorder(""));
        
        buttonsPanel = new ButtonsPanel();
        
        maxResultsPanel = new MaxResultsPanel();
        
        add(buttonsPanel,BorderLayout.WEST);
        add(maxResultsPanel, BorderLayout.CENTER);
        add(helpPanel,BorderLayout.EAST);
    }//construtor
    
    /*[00]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    public int getMaxResultsValue()
    {
        return maxResultsPanel.getMaxResultsValue();
    }//getMaxResultsValue()
    
    /***************************************************************************
     * 
     **************************************************************************/
    private final class MaxResultsPanel
        extends JPanel implements MouseInterface
    {
        private final JMaxResultsField jMaxResultsField;
        
        /*[00]------------------------------------------------------------------
        *
        ----------------------------------------------------------------------*/
        private MaxResultsPanel()
        {
            super();
      
            jMaxResultsField = 
                new JMaxResultsField(new Locale("pt","BR"), 5, false);
            jMaxResultsField.setColumns(6);
            jMaxResultsField.setValue(150);

            JLabel fieldLabel = new JLabel
            (
                "M\u00e1ximo de resultados por p\u00e1gina:"
            );
            
            add(fieldLabel);
            add(jMaxResultsField);
            addMouseListener(MOUSE_HANDLER);

            setBorder(BorderFactory.createTitledBorder(""));
            
        }//construtor
        
        /*[01]------------------------------------------------------------------
        *
        ----------------------------------------------------------------------*/
        @Override
        public void mouseEnter()
        {
            MESSAGE_LABEL.setText
            (
                "P\u00e1gina no browser mostra os " +
                getMaxResultsValue() +
                " resultados mais relevantes encontrados"
            );
            
        }//mouseEnter()
        
        /*[02]------------------------------------------------------------------
        *
        ----------------------------------------------------------------------*/
        private int getMaxResultsValue()
        {
            return jMaxResultsField.getValue();
        }//getMaxResultsValue()
        
    }//classe interna MaxResultsPanel
    
    /***************************************************************************
     * 
     **************************************************************************/
    private final class JLabelExtended extends JLabel implements MouseInterface
    {
        /*[00]------------------------------------------------------------------
        *
        ----------------------------------------------------------------------*/
        private JLabelExtended(Icon icon)
        {
            super(icon);
            addMouseListener(MOUSE_HANDLER);
        }//construtor
        
        /*[01]------------------------------------------------------------------
        *
        ----------------------------------------------------------------------*/
        @Override
        public void mouseEnter()
        {
           helpLabel.setIcon(focusIcon);
           MESSAGE_LABEL.setText
           (
               "Abre manual de uso no leitor de PDF padr\u00e3o do sistema"
           );
        }//mouseEnter()
        
        /*[02]------------------------------------------------------------------
        *
        ----------------------------------------------------------------------*/
        @Override
        public void mouseExit()
        {
            helpLabel.setIcon(normalIcon);
            MESSAGE_LABEL.setText(" ");
        }//mouseExit()
        
        /*[03]------------------------------------------------------------------
        *
        ----------------------------------------------------------------------*/
        @Override
        public void mouseClick()
        {
            /*
            Espera pelo menos 45 seg. antes de tentar abrir novamente o arquivo
            com o manual.
            */
            long now = System.currentTimeMillis();
            if ((now - before) < 45000) return;
            before = now;
                
            EventQueue.invokeLater
            (
               new Runnable()
               {
                    @Override
                    public void run()
                    {
                        try
                        {
                            desktop.open(helpFile); 
                        }
                        catch (IOException ex)
                        {
                            System.err.println(ex);
                        }
                    }
                }
            ); 
        }//mouseClick()
        
    }//classe interna JLabelExtended
   
}//classe NorthPanel

