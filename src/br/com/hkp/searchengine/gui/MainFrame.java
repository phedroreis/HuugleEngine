package br.com.hkp.searchengine.gui;

import br.com.hkp.searchengine.main.Huugle;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

/*******************************************************************************
 * Janela principal da gui do programa.
 * 
 * @author "Pedro Reis"
 * @since 16 de junho de 2020 v1.0
 * @version v1.0
 ******************************************************************************/
public final class MainFrame extends JFrame implements WindowListener
{
    private final SouthPanel southPanel;
    private final CenterPanel centerPanel;
    private final EastPanel eastPanel;
    private final NorthPanel northPanel;
    private final WestPanel westPanel;
    private final ResultsFrame resultsFrame; 
    
    /*[00]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Cria a janela principal do programa.
     * 
     * @throws IOException
     */
    public MainFrame() throws IOException
    {
        super("Huugle");
     
        northPanel = new NorthPanel();
        southPanel = new SouthPanel();
        centerPanel = new CenterPanel();
        eastPanel = new EastPanel();
        westPanel = new WestPanel();
        
        JPanel mainPanel = new JPanel();
        
        mainPanel.setLayout(new BorderLayout(15,5));
        
        mainPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        
        mainPanel.add(northPanel, BorderLayout.NORTH);
            
        mainPanel.add(southPanel, BorderLayout.SOUTH);
          
        mainPanel.add(centerPanel, BorderLayout.CENTER);
           
        mainPanel.add(westPanel, BorderLayout.WEST);
        
        mainPanel.add(eastPanel, BorderLayout.EAST);
                            
        add(mainPanel);
        
        resultsFrame = new ResultsFrame(mainPanel);
        
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(650, 335);
        setMinimumSize(new Dimension(650,335));
     
        /*
        Impede que a janela principal assuma dimensoes menores que as definidas
        em setMinimunSize() em qualquer plataforma. Porque o metodo 
        setMinimumSize() eh plataforma dependente e pode nao funcionar em
        algumas.
        */
        addComponentListener
        (
            new ComponentAdapter()
            {
                @Override
                public void componentResized(ComponentEvent e)
                {
                    Dimension d = getSize();
                    Dimension minD = getMinimumSize();
                    if(d.width < minD.width) d.width = minD.width;
                    if(d.height < minD.height) d.height = minD.height;
                    setSize(d);
                }
            }
        );
                     
        //Insere o icone do forum na janela
        try
        {
            URL url = getClass().getResource("huugle-logo.png");
            setIconImage(Toolkit.getDefaultToolkit().getImage(url));
        }
        catch (Exception e)
        {
            System.err.println(e);
        }
    }//construtor
    
     
    /*[01]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    private static MainFrame mainFrame;
    private static String[] words;
    private static String beginDate;
    private static String endDate;
    private static String user;
    private static boolean enableWebLinks;
    private static boolean enableSearchPosts;
    private static int maxResults;
    private static int w1;
    private static int w2;
    private static int w3;
    private static int w4;
    private static int[] values;
  
    /**
     * 
     */
    public static void updateSearchParameters()
    {
        words = mainFrame.centerPanel.getWords();
        beginDate = mainFrame.eastPanel.getBeginDate();
        endDate = mainFrame.eastPanel.getEndDate();
        user = mainFrame.eastPanel.getUser();
        enableWebLinks = mainFrame.westPanel.web();
        enableSearchPosts = mainFrame.westPanel.post();
        maxResults = mainFrame.northPanel.getMaxResultsValue();
        values = mainFrame.southPanel.getValues();
        w1 = values[0] * 2; 
        w2 = values[1] * 10; 
        w3 = values[2] * 10; 
        w4 = values[3] * 100;
    }//updateSearchParameters()
    
    /*[02]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Laco principal que executa o programa.
     * 
     * @throws java.io.IOException
     */
    public static void execute() throws IOException
    {
        
        mainFrame = new MainFrame();
        
        mainFrame.addWindowListener(mainFrame);
        mainFrame.setVisible(true);

        while (true)
        {
            Concurrent.awaitUntilSearchButtonClicked();
          
            mainFrame.resultsFrame.showResults
            (
                words,
                user,
                beginDate, 
                endDate,
                enableWebLinks,
                enableSearchPosts,
                maxResults,
                w1,
                w2,
                w3,
                w4
             );
        }//while
      
    }//execute()
    
    /***************************************************************************
     * 
     * ************************************************************************/
    /*[00]----------------------------------------------------------------------
    *             Encerra o programa quando a janela eh fechada
    --------------------------------------------------------------------------*/
   @Override
   public void windowClosing(WindowEvent e) 
   {
       Huugle.finalizer(0);
   }//fim do handler de evento do fechamento da janela 
   
   /*[01]----------------------------------------------------------------------
   *             Metodos vazios da interface WindowListener
   --------------------------------------------------------------------------*/
   @Override
   public void windowActivated(WindowEvent e){}
   @Override
   public void windowClosed(WindowEvent e){}
   @Override
   public void windowDeactivated(WindowEvent e){}
   @Override
   public void windowDeiconified(WindowEvent e){}
   @Override
   public void windowIconified(WindowEvent e){}
   @Override
   public void windowOpened(WindowEvent e){}
   
   
}//classe MainFrame
