/*
arquivo JMaxResultsField.java criado a partir de 16 de marco de 2019.
*/
package br.com.hkp.searchengine.gui.basic;

import static br.com.hkp.searchengine.gui.basic.MessageLabel.MESSAGE_LABEL;
import static br.com.hkp.searchengine.gui.basic.MouseHandler.MOUSE_HANDLER;
import java.util.Locale;

/*******************************************************************************
 * Campo formatado para entrada de valores numericos de tipo int.
 * 
 * @author "Pedro Reis"
 * @version 1.0
 * @since 1.0
 ******************************************************************************/
public final class JMaxResultsField 
    extends JGenericField<Integer> implements MouseInterface
{
    private final char separator;
    private final Locale locale;
    private final int maxDigits;
       
    /**
     * Cria um objeto de campo de texto para entrada de valores numericos  
     * inteiros. O valor sera exibido no campo de acordo com o Locale
     * passado pelo argumento l.
     * 
     * @param l O Locale para configuracao do campo. O valor numerico sera 
     * exibido de acordo com as convencoes deste Locale.
     * 
     * @param digits Quantos digitos pode ter o numero no campo. No maximo 10 
     * e no minimo 1. 
     * 
     * @param negativeValues Se true permite valores negativos no campo. 
     * Se false, nao.
     * 
     * @throws IllegalArgumentException Se digits maior que 10 ou menor que 1.
     */
    /*[00]----------------------------------------------------------------------
    *                       Construtor da classe
    --------------------------------------------------------------------------*/
    public JMaxResultsField(Locale l, int digits, boolean negativeValues)
        throws IllegalArgumentException
    {
        super();
        
        maxDigits = digits;
        
        if ((maxDigits < 1) || (maxDigits >10))
            throw new IllegalArgumentException("Invalid number of digits.");
                     
        locale = l;
               
        separator = '.';
        
        regexp[FOCUS_LOST] = ((negativeValues) ? "[-]?" : "") +
                             "(\\d{0,3}){1}(([" + 
                             separator + 
                             "]\\d{3})*)";
        
        regexp[FOCUS_GAINED] = ((negativeValues) ? "[-]?" : "") +
                               "0*\\d{0," + 
                               maxDigits +
                               "}";
        
        setToolTipText("N\u00famero de links em uma p\u00e1gina de resultados");
        
        addMouseListener(MOUSE_HANDLER);
    }//fim do construtor JMaxResultsField()
       
    /*
    A implementacao deste metodo recebe uma string no formato "plain" e tenta
    converte-la para um int. Se a conversao for bem sucedida este int
    eh convertido para uma string no padrao "lost". Ou seja, com caracteres 
    separadores tambem definidos pelo locale.
   
    Se a conversao para int nao for bem sucedida eh retornada uma string
    vazia.
    */
    /*[01]----------------------------------------------------------------------
    *              Do formato "plain" para o formato "lost"
    --------------------------------------------------------------------------*/
    @Override
    protected String plainToLost(String text)
    {
        int i;
        try
        {
           i = Integer.parseInt(text);
           if (i == -0.0) i = i * i;// evita que seja inserido "-0" no campo.
        }
        catch(NumberFormatException e)
        {
            return "";
        }
            
        return String.format(locale, "%,d", i);
  
    }//fim de plainToLost()
  
    /*[07]----------------------------------------------------------------------
    *              Filtra um caractere de uma string s. 
    --------------------------------------------------------------------------*/
    private static String filterString(String s, char c)
    {
                
        char[] includedChars = new char[s.length()];
       
        int index = 0;
        for (int i = 0; i < includedChars.length; i++)
        {
            char current = s.charAt(i);
            if (current != c) includedChars[index++] = current;
        } 

        return ( new String(includedChars).substring(0,index) );
       
    }//fim de filterString()                       
    
   /*[02]----------------------------------------------------------------------
    *                 Do formato "lost" para o "plain"
    --------------------------------------------------------------------------*/
    @Override
    protected String lostToPlain(String text)
    {
        return filterString(text, separator);
    }//fim de lostToPlain()
        
    /*
    Converte uma string no padrao "plain" representando um numero int em
    um tipo Integer. Se a conversao nao for possivel retorna 0.
    */
    /*[03]----------------------------------------------------------------------
    *    Recebe uma string no formato "plain" e a converte para Integer
    --------------------------------------------------------------------------*/
    @Override
    protected Integer plainToValue(String text)
    {
        try
        {
            return Integer.parseInt(text);
        }
        catch(NumberFormatException e)
        {
            return 0;
        }//fim do try/catch
    }//fim de plainToValue()
    
    /*
    Converte um Integer para sua representacao "plain", que eh sem separadores.
    */
    /*[04]----------------------------------------------------------------------
    *                      De int para "plain"
    --------------------------------------------------------------------------*/
    @Override
    protected String valueToPlain(Integer value)
    {
        long round = Math.round(value);
        if (value == round)//Evita ponto decimal em numeros inteiros
            return String.valueOf(round);
        else 
            return String.valueOf(value);
    }//fim de valueToPlain()
        
    /*[05]----------------------------------------------------------------------
    *        Retira caracteres espaco em branco das exremidades de str
    *        antes de que seja tentada uma insercao de str no campo.
    *        Impede que setValue() atualize o campo com um valor que 
    *        tenha mais digitos que o permitido por maxDigits.
    --------------------------------------------------------------------------*/
    @Override
    protected String insertString(String str)
    {
        str = str.trim();
        int countDigits = 0;
        for (int i = 0; i < str.length(); i++)
        {
            if (Character.isDigit(str.charAt(i))) countDigits++;
            if (countDigits > maxDigits)
            {
                str = "X";
                break;
            }
        }
        return str;
    }//fim de insertString()
    
    /*[06]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Exibe um texto na barra de status quando o cursor estah sobre o campo
     */
    @Override
    public void mouseEnter()
    {
        MESSAGE_LABEL.setText
        (
            "N\u00famero m\u00e1ximo de resultados exibidos na p\u00e1gina" 
        );
    }//MouseEnter()
    
}//fim da classe JMaxResultsField
