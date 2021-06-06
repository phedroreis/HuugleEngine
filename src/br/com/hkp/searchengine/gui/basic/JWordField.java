package br.com.hkp.searchengine.gui.basic;

import static br.com.hkp.searchengine.gui.basic.MessageLabel.MESSAGE_LABEL;
import static br.com.hkp.searchengine.gui.basic.MouseHandler.MOUSE_HANDLER;
import static 
    br.com.hkp.searchengine.registers.RegistersConstants.WORD_STRLENGTH;
import static br.com.hkp.searchengine.util.Global.DARK_GREEN;
import static br.com.hkp.searchengine.util.Global.EXCLUDEDS;
import static br.com.hkp.searchengine.util.Global.LOW_CHARS;
import static br.com.hkp.searchengine.util.Global.PALE_GREEN;
import java.awt.Color;
import java.awt.Font;

/*******************************************************************************
 * Esta classe eh um JGenericField que implementa um campo de entrada que soh
 * permite a digitacao dos caracteres minusculos e de palavra. (sem digitos,
 * espacos, pontuacao, etc...), e que nao ultrapassem 16 caracteres
 * 
 * @author "Pedro Reis"
 * @since 5 de junho de 2020 v1.0
 * @version 1.0
 ******************************************************************************/
public final class JWordField 
    extends JGenericField<String> implements MouseInterface
{
    private final Font normalFont;
    private final Font minusFont;
    private final Font plusFont;
    
    private final static String REGEXP = 
        "([+-]?[" + LOW_CHARS + "]{0," + WORD_STRLENGTH + "})";
    
     /**
     * Construtor da classe.
     */
    /*[00]----------------------------------------------------------------------
    *                          Construtor da classe
    --------------------------------------------------------------------------*/
    public JWordField()
    {
        super();
        regexp[FOCUS_LOST] = REGEXP;
        regexp[FOCUS_GAINED] = REGEXP;
        normalFont = getFont();
        minusFont = new Font(Font.MONOSPACED, Font.ITALIC, 12);
        plusFont = new Font(Font.MONOSPACED, Font.ITALIC + Font.BOLD, 12);
        addMouseListener(MOUSE_HANDLER);
    }//fim do construtor JFilterCharFIeld()
    
    /*[01]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    @Override
    protected void focusGained()
    {
        super.focusGained();
        setForeground(Color.BLACK);
        setFont(normalFont);
    }//focusGained()
    
    /*[02]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    @Override
    protected void focusLost()
    {
        super.focusLost();
        String value = getValue();
        
        if (value.isEmpty()) return;
        
        setForeground(DARK_GREEN);
        
        if (value.charAt(0) == '-')
        {
            setFont(minusFont);
            if ((value.length() < 5) || (EXCLUDEDS.contains(value)))
                setForeground(Color.RED);
            else
            {
                setFont(minusFont);
                setForeground(PALE_GREEN);
            }
        }
        else if (value.charAt(0) == '+')
        {
            setFont(plusFont);
            if ((value.length() < 5) || (EXCLUDEDS.contains(value)))
                setForeground(Color.RED);
            else
                setFont(plusFont);
        }
        else if ((value.length() < 4) || (EXCLUDEDS.contains(value)))
            setForeground(Color.RED);
        
    }//focusLost()
     
    /*[03]----------------------------------------------------------------------
    *        Retira caracteres espaco em branco das exremidades de str
    *        antes de que seja tentada uma insercao de str no campo.
    --------------------------------------------------------------------------*/
    @Override
    protected String insertString(String str)
    {
        return str.trim();
    }//fim de insertString()
    
    /*[04]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Mensagem na barra de status da janela principal quando o cursor estah 
     * sobre o objeto
     */
    @Override
    public void mouseEnter()
    {
        MESSAGE_LABEL.setText
        (
            "Prefixo + : apenas resultados com a palavra."
            + " Prefixo - : descarta resultados com a palavra"
        );
    }//mouseEnter()
    
}//fim da classe JWordField
