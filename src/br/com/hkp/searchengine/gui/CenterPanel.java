package br.com.hkp.searchengine.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

/*******************************************************************************
 * Um painel para entrada de palavras a serem pesquisadas em posts e titulos de 
 * topicos
 * 
 * @author "Pedro Reis"
 * @since 15 de junho de 2020 v1.0
 * @version 1.0
 ******************************************************************************/
public final class CenterPanel extends JPanel
{
    private static final int NUMBER_OF_TEXTFIELDS = 5;
     
    WordFieldPanel[] wordFieldPanelArray;
    
    /*[00]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Configura um painel para entrada de palavras a serem pesquisadas em 
     * posts e titulos de topicos
     * 
     */
    public CenterPanel()
    {
        super();
     
        setLayout(new GridLayout(NUMBER_OF_TEXTFIELDS + 1, 1));
        wordFieldPanelArray = new WordFieldPanel[NUMBER_OF_TEXTFIELDS];
        
        JLabel jLabel = new JLabel
                        (
                            "   Pesquisar pelas palavras:"
                        );
                     
        add(jLabel);
        
        EnterHandler enterHandler = new EnterHandler();
        
        for (int i = 0; i < NUMBER_OF_TEXTFIELDS; i++)
        {
            wordFieldPanelArray[i] = new WordFieldPanel("" + (i + 1));
            wordFieldPanelArray[i].addActionListener(enterHandler);
            add(wordFieldPanelArray[i]);
        }//for i
        
        setBorder(BorderFactory.createRaisedSoftBevelBorder());
               
    }//construtor
    
    /*[01]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Retorna um array com as palavras correntes nos campos de pesquisa
     * 
     * @return Um array com as palavras correntes nos campos de pesquisa.
     * O array retornado tem apenas palavras validas.
     */
    public String[] getWords()
    {
        ArrayList<String> list = new ArrayList<>(NUMBER_OF_TEXTFIELDS);
        
        boolean noPlusSignal = true;
        String word;
        /*
        Testa se ha algum sinal de mais precedendo alguma das palavras digitadas
        nos campos. Se houver, so serao incluidas no array retornado por este 
        metodo, palavras que iniciem com sinal de mais ou de menos.
        
        Porque um sinal de mais torna irrelevante a busca por palavras que nao 
        sejam precedidas pelo operador de mais ou de menos.
        */
        for (WordFieldPanel wordFieldPanel : wordFieldPanelArray)
        {
            word = wordFieldPanel.getText();
            if ((!word.isEmpty()) && (word.charAt(0) == '+'))
            {
                noPlusSignal = false;
                break;
            }
        }    
        
        for (WordFieldPanel wordFieldPanel : wordFieldPanelArray)
        {
            boolean addWord = false;
            
            word = wordFieldPanel.getText();
            
            if (word.isEmpty()) continue;
            
            char firstChar = word.charAt(0);
            /*
            Palavras que iniciam sem operador soh serao incluidas no array
            retornado se nao houver nenhuma outra sendo precedida por + e se 
            tiverem mais que 3 caracteres
            */
            if (Character.isLetter(firstChar))
                addWord = ((word.length() > 3) && (noPlusSignal));
            else
            /*
            Palavras precedidas de + ou de - soh serao incluidas no array 
            retornado por este metodo se tiverem mais de 3 letras. Ou seja, 
            o operador de mais ou de menos e 4 ou mais letras.
            */    
                addWord = (word.length() > 4);
            
            if ((addWord) && (!list.contains(word))) list.add(word);
        }//for
                   
        return list.toArray(new String[list.size()]);
        
    }//getWords()
    
   
    
    /***************************************************************************
    *           Handler de eventos dos campos de entrada de palavras
    ***************************************************************************/
    private class EnterHandler implements ActionListener
    {
       /*[00]-------------------------------------------------------------------
        *
        ----------------------------------------------------------------------*/
        @Override
        public void actionPerformed(ActionEvent e)
        {
            for (int i = 0; i < NUMBER_OF_TEXTFIELDS; i++)
                if (e.getSource() == wordFieldPanelArray[i].getSource())
                {
                    
                    wordFieldPanelArray[(i + 1) % NUMBER_OF_TEXTFIELDS].
                        getSource().requestFocus();
                    return;
                }
        }
    }//classe interna EnterHandler
  
}//classe CenterPanel
