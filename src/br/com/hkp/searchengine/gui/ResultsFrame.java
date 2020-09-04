package br.com.hkp.searchengine.gui;


import br.com.hkp.searchengine.main.SearchEngine;
import br.com.hkp.searchengine.main.SearchEngine.TreeSetNode;
import static br.com.hkp.searchengine.util.Global.BODY_2;
import static br.com.hkp.searchengine.util.Global.BODY_3;
import static br.com.hkp.searchengine.util.Global.BODY_4;
import static br.com.hkp.searchengine.util.Global.BODY_5;
import static br.com.hkp.searchengine.util.Global.BODY_END;
import static br.com.hkp.searchengine.util.Global.BODY_START;
import static br.com.hkp.searchengine.util.Global.CCDirName;
import static br.com.hkp.searchengine.util.Global.CONST_LENGTH;
import static br.com.hkp.searchengine.util.Global.HEAD_2;
import static br.com.hkp.searchengine.util.Global.HEAD_END;
import static br.com.hkp.searchengine.util.Global.HEAD_START;
import static br.com.hkp.searchengine.util.Global.VAR_LENGTH;
import static br.com.hkp.searchengine.util.Global.datDirName;
import static br.com.hkp.searchengine.util.Global.nicksUserArray;
import static br.com.hkp.searchengine.util.Global.RESULTS_FILENAME;
import static br.com.hkp.searchengine.util.Global.TABLE_END;
import static br.com.hkp.searchengine.util.Global.TABLE_START;
import static br.com.hkp.searchengine.util.Global.TD1_END;
import static br.com.hkp.searchengine.util.Global.TD1_START;
import static br.com.hkp.searchengine.util.Global.TD2_END;
import static br.com.hkp.searchengine.util.Global.TD2_START;
import static br.com.hkp.searchengine.util.Global.TD3_END;
import static br.com.hkp.searchengine.util.Global.TD3_START;
import static br.com.hkp.searchengine.util.Global.topicExtendedArray;
import static br.com.hkp.searchengine.util.Global.urlBase;
import br.com.hkp.searchengine.util.Util;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.TreeSet;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Essa classe implementa uma janela para exibir o progresso do processo de
 * pesquisa.
 * 
 * @author "Pedro Reis"
 * @since 17 de junho de 2020 v1.0
 * @version 1.0
 */
public final class ResultsFrame extends JFrame
{
    private final SearchEngine searchEngin;
    private final JTextArea textPanel;
    private final JScrollPane scrollPane;
    private final JProgressBar jProgressBar;
    private final URL resultsFileURL;
        
    /*[00]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    public ResultsFrame(Component c) throws IOException
    {
        super("Resultados");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setSize(570, 325);
             
        textPanel = new JTextArea();
        textPanel.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        textPanel.setEditable(false);
        scrollPane = new JScrollPane(textPanel);
        add(scrollPane, BorderLayout.CENTER);
        
        jProgressBar = new JProgressBar();
        add(jProgressBar, BorderLayout.SOUTH);
        
        searchEngin = new SearchEngine(this);
    
        setLocationRelativeTo(c);
        
        resultsFileURL = 
            new URL("file://" + (new File(RESULTS_FILENAME)).getAbsolutePath());
        
        //Insere o icone do forum CC na janela de resultados
        try
        {
            URL url = getClass().getResource("huugle-logo.png");
            Image icon = Toolkit.getDefaultToolkit().getImage(url); 
            setIconImage(icon);
        }
        catch (Exception e)
        {
            System.err.println(e);
        }
    }//construtor
    
    /*[01]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    public void write(final String s)
    {
        textPanel.append(s);
        textPanel.setCaretPosition(textPanel.getText().length());
    }//write()
    
    /*[02]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    public void writeln(final String s)
    {
        textPanel.append(s + "\n");
        textPanel.setCaretPosition(textPanel.getText().length());
    }//writeln()
    
    /*[03]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    public void updateProgressBar(final int value)
    {
        jProgressBar.setValue(value);
    }//updateProgressBar()
    
    /*[04]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    public void resetProgressBar()
    {
        jProgressBar.setValue(0);
    }//resetProgressBar()
    
    /*[05]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/   
    private static String getDate(final String date)
    { 
        String year = "20" + date.substring(0, 2);
        String month = date.substring(2, 4);
        String day = date.substring(4, 6);
        return (day + "/" + month + "/" + year);
    }//getDate()
    
    /*[06]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Abre um arquivo para escrita
     * 
     * @param buffer O tamanho do buffer em bytes
     * @return Um objeto PrintWriter aberto
     * @throws IOException Erro de IO ao abrir arquivo
     */
    public static PrintWriter openOutputFile(final int buffer)
        throws IOException
    {
        PrintWriter out = new PrintWriter
                          (
                              new BufferedWriter
                              (
                                  new FileWriter
                                  (
                                      RESULTS_FILENAME,
                                      StandardCharsets.UTF_8
                                  ),
                                  buffer
                              )
                          );
        return out;
               
    }//openOutputFile()
    
    /*[07]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    private void buildSearchFile
    (
        final TreeSet<TreeSetNode> list,//lista com os resultados
        final StringBuilder s,//String com dados adicionais 
        final boolean webLinks,//Se true,gera links p/ arqs online
        final int maxResults//Num. max. de resultados a ser exibido
    )
        throws IOException
    {
        String cc;
        
        if (webLinks)
            cc = urlBase;
        else
            cc = CCDirName;
        
        /*
        Preve quanta memoria sera necessaria para armazenar o texto do arquivo
        */
        int buffer = 
            CONST_LENGTH + datDirName.length() * 3  + cc.length() * 2 +
            list.size() * VAR_LENGTH;
        
        /*
        Cria um StringBuilder onde sera montado o texto do arquivo HTML
        */
        StringBuilder sb = new StringBuilder(buffer);
       
        /*
        Aqui partes jah prontas do arquivo, que serao sempre as 
        mesmas, sao mescladas com os dados gerados pelo programa em uma pesquisa
        .Como o header, etc... A string s eh inserida no local apropriado
        */
        sb.append(HEAD_START).append(datDirName).append(HEAD_2)
        .append(datDirName).append(HEAD_END).append(BODY_START)
        .append(cc).append(BODY_2)
        .append(datDirName).append(BODY_3).append(cc).append(BODY_4).
        append(s).append(BODY_5).append(TABLE_START);
        
        /*
        Conta quantos links jah inseriu no arquivo
        */
        int count = 0;
        
        /*
        Percorre toda a lista com as strings com os nomes dos arquivos que serao
        convertidos em links para os proprios
        */
        for (TreeSetNode l: list)
        {
            /*
            Se webLinks = true, retorna a parte da string que farah os links
            apontarem para arquivos no acervo online. Se false, a string 
            retornada pelo metodo(que eh o prefixo da url que sera montada)
            faz o link apontar para o arquivo no acervo local.
            */
            String url = l.getURL(webLinks);
            /*
            Obtem da lista l o nome do arquivo que serah apontado na pagina
            pelo link que esse codigo estah montando
            */
            String file = l.getFile();
            /*
            Monta a string com o link. O objeto topicsTitleFinder obtem,
            a partir da ID do topico, o titulo do topico.
            */
            String topicID = file.substring(6, file.indexOf('.'));
            topicExtendedArray.read(topicID);
            String title = topicExtendedArray.getTitle();
            String link = url + "\" target = \" _blank \">" + title;
                      
            /*
            Acrescenta em StringBuilder sb uma linha da tabela de resultados
            */
            sb.append(TD1_START).append(link).append(TD1_END).
            append(TD2_START).append(String.valueOf(l.getRank())).
            append(TD2_END).append(TD3_START).append(l.getDate()).
            append(TD3_END);
            
            /*
            O usuario escolhe quantos resultados por pagina mostrar. Se foi 
            atingido esse limite, encerra o loop e parah de inserir links no 
            arquivo HTML de resultados
            */
            if (++count == maxResults) break;
        }//fim do for - Todos os links montados e inseridos em sb
        
        /*
        O sufixo, parte final do arquivo, eh acrescido a StringBuilder sb
        */
        sb.append(TABLE_END).append(BODY_END);
        /*
        O arquivo eh gravado em disco, SOBRESCREVENDO o arquivo de resultados
        anterior.
        */
        PrintWriter out = openOutputFile(buffer);
        out.print(sb);
        out.close();
        
        /*
        Finalmente o metodo openWebpage() eh chamado para abrir o arquivo com 
        os links no navegador padrao do SO.
        */
        Util.openWebpage(resultsFileURL);
        
    }//fim do metodo buildSearchFile()
      
    /*[08]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    private static final String SEPARATOR_1 =
        "\n=======================================" +
        "=====================================\n";
    
    private static final String SEPARATOR_2 =
        "-------+-------------------------------" +
        "-------------------------------------";
    
    public void showResults
    (
        final String[] words,
        final String nick, 
        final String begin,
        final String end,
        final boolean webLinks,
        final boolean searchPosts,
        final int maxResults,
        final int w1,
        final int w2,
        final int w3,
        final int w4
    ) 
        throws IOException
    {
        setVisible(true);
        
        if (words.length == 0)
        {
            writeln("Nenhuma palavra a ser pesquisada\n" + SEPARATOR_1);
            return;
        }
        
        StringBuilder sb = new StringBuilder(512);
        
        writeln("Procurando pela(s) palavra(s) :\n");
        for (String w: words) sb.append(w.toUpperCase()).append("  ");
        
        write(sb.toString());
        
        writeln("");
        
        if (!nick.isEmpty()) 
        {
            writeln("\nEm posts do usu\u00e1rio : " + nick.toUpperCase());
            sb.append("| ").append(nick).append(" ");
        }
        
        String b = getDate(begin); String e = getDate(end);
        
        sb.append("| ").append(b).append(" &le; datas &le; ").append(e);
        
                      
        String userID = null;
        if (nicksUserArray.read(nick))
        {
            userID = nicksUserArray.getUserID();
            writeln("\nUserId : " + userID);
        }
        else
        {
                       
            if (!nick.isEmpty())
                writeln
                (
                    "\nUsu\u00e1rio n\u00e3o encontrado! "
                    + "Pesquisando para qualquer usu\u00e1rio."
                );
            else 
                writeln("\nPesquisando para qualquer usu\u00e1rio.");
        }
       
        writeln("\nBuscando posts publicados entre " + b + " e " + e);
       
        TreeSet<TreeSetNode> list = searchEngin.getFileList
                                    (
                                        words, userID, begin, end, 
                                        searchPosts, w1, w2, w3, w4
                                    );
            
        /*
        Copia os argumentos que serao enviados ao metodo buildSearchFile() em
        em thread separada para que os objetos originais possam ser modificados
        sem que estas modificacoes afetem o processamento de suas copias na
        thread
        */
        TreeSet<TreeSetNode> l = list;
        StringBuilder s = sb;
        boolean w = webLinks;
        int m = maxResults;
        
        /*
        Abre uma janela no navegador com os resultados em uma thread separada
        */
        EventQueue.invokeLater
        (
            new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        buildSearchFile(l, s, w, m);
                    }
                    catch (IOException e)
                    {
                        System.err.println(e);
                    }
                }//run()
            }
        );
        
        writeln("\n  RANK | ARQUIVO\n" + SEPARATOR_2);
         
        for (TreeSetNode p: list) writeln(p.toString());
        
        writeln("\n" + list.size() + " arquivos listados");
        
        writeln(SEPARATOR_1);
   
    }//showResults()
 
}//classe ResultsFrame

