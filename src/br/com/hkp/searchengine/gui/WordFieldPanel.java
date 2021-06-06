package br.com.hkp.searchengine.gui;

import br.com.hkp.searchengine.gui.basic.JWordField;
import static br.com.hkp.searchengine.util.Global.EXCLUDEDS;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JPanel;

/*******************************************************************************
 * Um campo de entrada para digitar palavra a ser pesquisada em topicos ou posts
 * 
 * @author "Pedro Reis"
 * @since 15 de junho de 2020 v1.0
 * @version 1.0
 ******************************************************************************/
public final class WordFieldPanel extends JPanel 
{
    private final JWordField jTextField;
    
    /*[00]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Configura um campo de entrada para palavras a serem pesquisadas
     * 
     * @param label O indice do campo de entrada
     */
    public WordFieldPanel(String label)
    {
        super();
        setLayout(new BorderLayout());
        JLabel jLabel = new JLabel(label + " ");
        jTextField = new JWordField();
        jTextField.setToolTipText
        (
            "M\u00ednimo de 4 e m\u00e1ximo de 16 caracteres"
        );
        add(jLabel, BorderLayout.WEST);
        add(jTextField, BorderLayout.CENTER);
    }//construtor
    
    /*[01]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Retorna o texto corrente no campo de entrada
     * 
     * @return O texto corrente no campo de entrada
     */
    public String getText()
    {
        String value = jTextField.getValue();
        
        if (value.isEmpty()) return "";
                
        switch (value.charAt(0))
        {
            case '+':
            case '-':
                if (value.length() < 5) return "";
                break;
            default:
                if (value.length() < 4) return "";
        }
               
        if (EXCLUDEDS.contains(value)) return "";
        
        return value;
    }//getText()
         
    /*[02]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * ActionListener para o campo de texto
     * 
     * @param a Objeto ActionListener
     */
    public void addActionListener(ActionListener a)
    {
        jTextField.addActionListener(a);
    }//addActionListener()
    
    /*[03]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Retorna o campo de texto que gerou o ActionEvent
     * 
     * @return o campo de texto que gerou o ActionEvent
     */
    public JWordField getSource()
    {
        return jTextField;
    }//getSource()
      
}//classe WordFieldPanel
