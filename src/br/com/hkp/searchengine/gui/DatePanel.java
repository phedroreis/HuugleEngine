package br.com.hkp.searchengine.gui;

import static br.com.hkp.searchengine.gui.basic.MessageLabel.MESSAGE_LABEL;
import static br.com.hkp.searchengine.gui.basic.MouseHandler.MOUSE_HANDLER;
import br.com.hkp.searchengine.gui.basic.MouseInterface;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/*******************************************************************************
 * Um painel para selecao de datas para ser incorporado a janela de interface 
 * do programa.
 * 
 * @author "Pedro Reis"
 * @since 15 de junho de 2020 v1.0
 * @version 1.0
 ******************************************************************************/
public final class DatePanel extends JPanel
{
    private static final String[] DAYS = 
    { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10",
      "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
      "21", "22", "23", "24", "25", "26", "27", "28", "29", "30",
      "31"
    };
    private static final String[] MONTHS = 
    { "Janeiro", "Fevereiro", "Mar\u00e7o", "Abril", "Maio", "Junho",
      "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro" 
    }; 
    private static final String[] YEARS =
    { "2005", "2006", "2007", "2008", "2009", "2010", "2011", "2012",
      "2013", "2014", "2015", "2016", "2017", "2018", "2019", "2020" 
    }; 
    
    private final JComboBoxExtended daysList;
    private final JComboBoxExtended monthsList;
    private final JComboBoxExtended yearsList;
    public static boolean validDate = true;
    
    /*[00]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Cria um seletor de datas com o rotulo e data inicial especificada
     * 
     * @param label O rotulo do seletor
     * @param d O dia inicial do seletor de datas
     * @param m O mes inicial do seletor de datas
     * @param y O ano inicial do seletor de datas
     */
    public DatePanel(String label, int d, int m, int y)
    {
        super();
        setLayout(new BorderLayout());
        
        JLabel jLabel = new JLabel(label);
        add(jLabel, BorderLayout.NORTH);
        
        daysList = new JComboBoxExtended(DAYS);
        setDay(d);
        add(daysList, BorderLayout.WEST);
        
        
        monthsList = new JComboBoxExtended(MONTHS);
        setMonth(m);
        add(monthsList, BorderLayout.CENTER);
        
       
        yearsList = new JComboBoxExtended(YEARS);
        setYear(y);
        add(yearsList, BorderLayout.EAST);
   
    }//construtor
    
    /*[01]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    private void setDay(int day)
    {
        daysList.setSelectedIndex(day - 1);
    }//setDay()
    
    /*[02]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    private void setMonth(int month)
    {
        monthsList.setSelectedIndex(month - 1);
    }//setMonth()
    
    /*[03]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    private void setYear(int year)
    {
        yearsList.setSelectedIndex(year - 2005);
    }//setYear()
    
    /*[04]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    private int getDay()
    {
        return (daysList.getSelectedIndex() + 1);
    }//getDay()
    
    /*[05]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    private int getMonth()
    {
        return (monthsList.getSelectedIndex() + 1);
    }//getMonth()
    
    /*[06]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    private int getYear()
    {
        return (yearsList.getSelectedIndex() + 5);
    }//getYear()
    
    /*[07]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Retorna a data corrente no seletor no formato AAMMDD
     * 
     * @return A data corrente no seletor no formato AAMMDD
     */
    public String getDate()
    {
        return String.format("%02d%02d%02d", getYear(), getMonth(), getDay());
    }//getDate()
          
    /***************************************************************************
     * 
     **************************************************************************/
    private final class JComboBoxExtended 
        extends JComboBox<String> implements MouseInterface
    {
        /*[00]------------------------------------------------------------------
        *
        ----------------------------------------------------------------------*/
        private JComboBoxExtended(String[] itens)
        {
            super(itens);
            addMouseListener(MOUSE_HANDLER);
        }//construtor
        
        /*[01]------------------------------------------------------------------
        *
        ----------------------------------------------------------------------*/
        @Override
        public void mouseEnter()
        {
            if (validDate)
                MESSAGE_LABEL.setText
                (
                    "O CC iniciou em 18 de janeiro de 2005 e "
                    + "encerrou em 1\u00ba de fevereiro de 2020"
                ) ;
            else
            {
                MESSAGE_LABEL.setForeground(Color.RED);
                MESSAGE_LABEL.setText
                (
                    "Selecionada data inicial posterior \u00e0 data final"
                );
            }
        }//mouseEnter()
        
        /*[02]------------------------------------------------------------------
        *
        ----------------------------------------------------------------------*/
        @Override
        public void mouseExit()
        {
            MESSAGE_LABEL.setForeground(Color.BLACK);
            MESSAGE_LABEL.setText(" ") ;
        }//mouseExit()
        
    }//classe interna JComboBoxExtended
    
}//classe DatePanel

