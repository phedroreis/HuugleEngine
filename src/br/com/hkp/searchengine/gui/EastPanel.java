package br.com.hkp.searchengine.gui;

import static br.com.hkp.searchengine.gui.basic.MouseHandler.MOUSE_HANDLER;
import br.com.hkp.searchengine.gui.basic.MouseInterface;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/*******************************************************************************
 * Um painel para selecionar os filtros de uma pesquisa. Que podem ser o autor e
 * um intervalo entre datas.
 * 
 * @author "Pedro Reis"
 * @since 16 de junho de 2020 v1.0
 * @version 1.0
 ******************************************************************************/
public final class EastPanel extends JPanel implements MouseInterface
{
    private final UserPanel userPanel;
    private final DatePanel beginDatePanel;
    private final DatePanel endDatePanel;
      
    /*[00]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Configura a interface para pesquisa em posts
     * 
     */
    public EastPanel()
    {
        super();
             
        setLayout(new GridLayout(3, 1));
               
        userPanel = new UserPanel();
        add(userPanel);
        
        beginDatePanel = new DatePanel("A partir desta data", 18, 1, 2005);
        add(beginDatePanel);
        
        endDatePanel = new DatePanel("At\u00e9 esta data", 1, 2, 2020);
        add(endDatePanel);
        
        setBorder(BorderFactory.createTitledBorder("Filtros"));
        
        addMouseListener(MOUSE_HANDLER);
    }//construtor
    
    /*[01]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Retorna o nick do forista. As pesquisas se restringirao aos posts desse 
     * forista. Porem se o nick nao existir ou estiver em branco, a pesquisa 
     * se darah em posts de quaisquer foristas.
     * 
     * @return O nick do forista restringindo o escopo da busca.
     */
    public String getUser()
    {
        return userPanel.getUser();
    }//getUser()
    
    /*[02]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * A pesquisa retornarah apenas posts a partir desta data. Se for setada 
     * uma data posterior a data final, o resultado da busca sera vazio.
     * 
     * @return A data de inicio da pesquisa no formato AAMMDD
     */
    public String getBeginDate()
    {
        return beginDatePanel.getDate();
    }//getBeginDate()
    
    /*[03]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
     /**
     * A pesquisa retornarah apenas posts ateh esta data. Se for setada 
     * uma data anterior a data inicial, o resultado da busca sera vazio.
     * 
     * @return A data limite da pesquisa no formato AAMMDD
     */
    public String getEndDate()
    {
        return endDatePanel.getDate();
    }//getEndDate()
    
    /*[04]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    @Override
    public void mouseExit()
    {
       DatePanel.validDate = 
           (endDatePanel.getDate().compareTo(beginDatePanel.getDate()) >= 0); 
    }//mouseExit()
    
    /*[05]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    @Override
    public void mouseEnter() {}
   
    
   
}//classe EastPanel
