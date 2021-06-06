package br.com.hkp.searchengine.gui;

import static br.com.hkp.searchengine.gui.basic.MessageLabel.MESSAGE_LABEL;
import static br.com.hkp.searchengine.gui.basic.MouseHandler.MOUSE_HANDLER;
import br.com.hkp.searchengine.gui.basic.MouseInterface;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;

/*******************************************************************************
 * Painel com controles deslizantes para controlar o peso de cada um dos 4 
 * criterios utilizados para classificacao de relevancia dos posts retornados
 * pela pesquisa
 * 
 * @author "Pedro Reis"
 * @since 15 de junho de 2020 v1.0
 * @version 1.0
 ******************************************************************************/
public final class JSlidersPanel extends JPanel
{
    private static final String[] LABELS =
    {
        "Repeti\u00e7\u00e3o de palavras",
        "Quantidade de palavras no t\u00edtulo do t\u00f3pico",
        "M\u00e9dia de visualiza\u00e7\u00f5es di\u00e1rias",
        "Quantidade de palavras localizadas"
    };
    
    private static final String[] MESSAGES =
    {
        "Ajusta o peso da repeti\u00e7\u00e3o de palavras para o" +
        " c\u00e1lculo da relev\u00e2ncia do resultado",
        "O peso do n\u00famero de palavras presentes no t\u00edtulo do" +
        " t\u00f3pico",
        "O peso da m\u00e9dia de visualiza\u00e7\u00f5es di\u00e1rias" +
        " do t\u00f3pico",
        "O peso do n\u00famero de palavras localizadas"
    };
    
    /*
    Valores iniciais dos controles
    */
    private static final int[] DEFAULT = {2, 5, 4, 7};
    
    private final JSliderExtended[] jSliderArray = new JSliderExtended[4];
    private final int[] values = new int[4];
    
    
    /*[00]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    public JSlidersPanel()
    {
        super();
        setLayout(new GridLayout(2,2));
        for (int i = 0; i <= 3; i++)
        {
            jSliderArray[i] = new JSliderExtended(DEFAULT[i], MESSAGES[i]);
            jSliderArray[i].setMajorTickSpacing(1);
            jSliderArray[i].setPaintTicks(true);
            jSliderArray[i].setBorder
            (
                BorderFactory.createTitledBorder(LABELS[i])
            );
            add(jSliderArray[i]);
        }
  
    }//construtor
    
    /*[01]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    public int[] getValues()
    {
        for (int i = 0; i <=3; i++) values[i] = jSliderArray[i].getValue();
        return values;
    }//getValues()
    
    /***************************************************************************
     * 
     **************************************************************************/
    private final class JSliderExtended
        extends JSlider implements MouseInterface
    {
        private final String mouseMsg;
        
        /*[00]------------------------------------------------------------------
        *
        ----------------------------------------------------------------------*/
        private JSliderExtended(final int defaultValue, final String msg)
        {
            super(SwingConstants.HORIZONTAL, 0, 20, defaultValue);
            mouseMsg = msg;
            addMouseListener(MOUSE_HANDLER);
        }//construtor
        
        /*[01]------------------------------------------------------------------
        *
        ----------------------------------------------------------------------*/
        @Override
        public void mouseEnter()
        {
            MESSAGE_LABEL.setText(mouseMsg);
        }//mouseEnter()
        
    }//classe interna JSliderExtended
 
}//classe JSlidersPanel
