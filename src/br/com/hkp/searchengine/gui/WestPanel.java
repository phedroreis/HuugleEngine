package br.com.hkp.searchengine.gui;

import br.com.hkp.searchengine.gui.basic.RadioButton;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import javax.swing.BorderFactory;
import javax.swing.JSeparator;


/*******************************************************************************
 *
 * @author "Pedro Reis"
 ******************************************************************************/
public final class WestPanel extends JPanel
{
    private final RadioButton selector1;
    private final RadioButton selector2;
     
    /*[00]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    public WestPanel()
    {
        super();
    
        setLayout(new GridLayout(4, 1));
               
        selector1 = new RadioButton
                    (
                        "Web",
                        KeyEvent.VK_W, 
                        "Retorna resultados como links para o acervo" +
                        " online [ALT + W]",
                        "Local",
                        KeyEvent.VK_L,
                        "Retorna resultados como links para " +
                        "arquivos da c\u00f3pia local [ALT + L]",
                        true
                    );
     
        add(selector1);
         
        JSeparator js = new JSeparator(); js.setVisible(false); add(js);
        add(new JSeparator());
        
        selector2 = new RadioButton
                    (
                        "Posts",
                        KeyEvent.VK_O,
                        "Realiza a pesquisa em textos de postagens [ALT + O]",
                        "T\u00f3picos",
                        KeyEvent.VK_T, 
                        "Realiza a pesquisa em t\u00edtulos de t\u00f3picos" +
                        " [ALT + T]",
                        false
                    );
    
        add(selector2);
       
        setBorder(BorderFactory.createTitledBorder("Seletores"));
    }//construtor
    
    /*[01]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    public boolean web()
    {
        return selector1.firstSelected();
    }//web()
    
    /*[02]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    public boolean post()
    {
        return selector2.firstSelected();
    }//post()
  
}//classe WestPanel
