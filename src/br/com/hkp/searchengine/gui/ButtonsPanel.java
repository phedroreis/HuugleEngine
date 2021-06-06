package br.com.hkp.searchengine.gui;

import static br.com.hkp.searchengine.gui.basic.MessageLabel.MESSAGE_LABEL;
import static br.com.hkp.searchengine.gui.basic.MouseHandler.MOUSE_HANDLER;
import br.com.hkp.searchengine.gui.basic.MouseInterface;
import br.com.hkp.searchengine.main.Huugle;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

/*******************************************************************************
 * Um painel com os botoes para comandos de pesquisar e encerrar o programa.
 * 
 * @author "Pedro Reis"
 * @since 15 de junho de 2020 v1.0
 * @version 1.0
 ******************************************************************************/
public final class ButtonsPanel extends JPanel
{
    private final JButtonExtended searchButton;
    private final JButtonExtended exitButton;
    
    /*[00]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/ 
    /**
     * Construtor da classe cria um JPanel com dois botoes: Pesquisar e Sair
     */
    public ButtonsPanel() 
    {
        super();
              
        setLayout(new GridLayout(1,2));
        ButtonsHandler buttonsHandler = new ButtonsHandler();
        Icon searchIcon = null;
        Icon exitIcon = null;
        try
        {
            searchIcon =
                new ImageIcon(getClass().getResource("search.png"));
            exitIcon = 
                new ImageIcon(getClass().getResource("exit.png"));
        }
        catch (Exception e)
        {
            System.err.println(e);
        }
        searchButton = new JButtonExtended
                       (
                           "Pesquisar",
                           searchIcon,
                           "Clique para iniciar a pesquisa. [ALT + P]"
                       );
        exitButton = new JButtonExtended
                     (
                         "Sair",
                         exitIcon,
                         "Clique para encerrar o programa. [ALT + S]"
                     );
        exitButton.addActionListener(buttonsHandler);
        exitButton.setMnemonic(KeyEvent.VK_S);
        searchButton.addActionListener(buttonsHandler);
        searchButton.setMnemonic(KeyEvent.VK_P);
     
        add(searchButton);
        add(exitButton);
    }//construtor
    
    /***************************************************************************
    *
    ***************************************************************************/
    private final class JButtonExtended 
        extends JButton implements MouseInterface
    {
        private final String enterMouse;

        /*[00]------------------------------------------------------------------
        *
        ----------------------------------------------------------------------*/
        /**
         * Construtor da classe
         * 
         * @param label Um texto para o botao
         * @param icon  Um icone para o botao
         * @param enterMouse Um texto para exibir na barra de status quando o 
         * cursor estiver sobre o botao
         */
        private JButtonExtended
        (
            final String label,
            final Icon icon,
            final String enterMouse
        )
        {
            super(label, icon);
            this.enterMouse = enterMouse;
            addMouseListener(MOUSE_HANDLER);
        }//construtor

        /*[01]------------------------------------------------------------------
        *
        ----------------------------------------------------------------------*/
        /**
         * Exibe mensagem na barra de status quando o mouse esta sobre o botao
         */
        @Override
        public void mouseEnter()
        {
            MESSAGE_LABEL.setText(enterMouse);
        }//mouseEnter()

    }//ActionButton
    
    /**************************************************************************
    *
    ***************************************************************************/
    /*[00]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /*
    Handler de eventos dos botoes
    */
    private class ButtonsHandler implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (e.getSource() == searchButton)
            {
                /*
                Evita que os argumentos enviados ao metodo showResults() sejam
                modificados enquanto ainda estao sendo enviados ao metodo.
                startSearch() seta esta variavel para false e soh eh retornada
                para true depois que a pesquisa eh realizada.
                */
                if (!Concurrent.canRefreshSearchParameters.get()) return;
                /*
                Copia os parametros da pesquisa de objetos GUI para campos da
                classe MainFrame. Campos estes que serao enviados como
                argumentos ao metodo showResults() de ResultsFrame.
                */
                MainFrame.updateSearchParameters();
                Concurrent.startSearch();
            }
            else
                Huugle.finalizer(0);//Encerra execucao do programa
        }
    }//classe interna ButtonsHandler
    
}//classe ButtonsPanel
