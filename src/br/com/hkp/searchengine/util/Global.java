package br.com.hkp.searchengine.util;

import br.com.hkp.searchengine.arrays.NicksUserArray;
import br.com.hkp.searchengine.arrays.PostExtendedArray;
import br.com.hkp.searchengine.arrays.PostRegArray;
import br.com.hkp.searchengine.arrays.TopicExtendedArray;
import br.com.hkp.searchengine.registers.WordReg;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*******************************************************************************
 * A classe define constantes e campos de acesso global a todas as classes.
 * 
 * @author "Pedro Reis"
 * @since 29 de abril de 2020 v1.0
 * @version 1.0
 ******************************************************************************/
public final class Global
{
    /*
     * Nomes de arquivos e diretorios que serao referenciados por todo o
     * programa
     */
    public static String jsonsDirName; 
    public static String installDir;
    public static String datDirName;
    public static String urlBase;
    public static File helpFile;
    
    /**
     * Um TreeSet com todas as palavras de + de 3 caracteres que nao constam
     * do indice de palavras catalogadas em posts e titulos de topicos por serem
     * palavras de ocorrencia muito comum
     */
    public static final HashSet<String> EXCLUDEDS = new HashSet<>(1024);
    
    /**
     * Obtem uma ID de usuario a partir do nick de usuario
     */
    public static NicksUserArray nicksUserArray;
    /**
     * Obtem os dados de post a partir da ID do post
     */
    public static PostExtendedArray postExtendedArray;
    /**
     * Obtem titulo e rank de um topico a partir da ID do topico
     */
    public static TopicExtendedArray topicExtendedArray;
    
    /**
     * Objeto que faz a leitura do arquivo do indice de palavras de titulos de
     * topicos
     */
    public static WordReg topicsWordFinder;
     /**
     * Objeto que faz a leitura do arquivo do indice de palavras de textos de
     * postagens
     */
    public static WordReg postsWordFinder;
    /**
     * Objeto que faz a leitura do arquivo de listas de posts relativos a  
     * aberturas de topicos
     */
    public static PostRegArray topicsPostRegArray;
     /**
     * Objeto que faz a leitura do arquivo de listas de posts 
     */
    public static PostRegArray postsPostRegArray;
     
    /**
     * Cor de primeiro plano para termos de busca e nicks de usuario validos
     * digitados nos campos de texto
     */
    public static final Color DARK_GREEN = new Color(0, 128, 0);
    /**
     * Cor de primeiro plano para termos de busca com prefixo - 
     */
    public static final Color PALE_GREEN = new Color(144, 195, 144);
    
   /**
    * Todas as letras minusculas (com e sem acento) do alfabeto portugues. E 
    * mais algumas letras minusculas com acento de outros alfabetos
    */
    public static final String LOW_CHARS = 
        "\u00e0\u00e1\u00e2\u00e3\u00e4" + //a
        "\u00e8\u00e9\u00ea\u00eb" + //e
        "\u00ec\u00ed" +  //i
        "\u00f2\u00f3\u00f4\u00f5\u00f6øØ" + //o
        "\u00f9\u00fa\u00fb\u00fc" + //u
        "\u00f1" + //n
        "\u00e7" + //c
        "a-z"; 
         
    /**
     * Esse lock paraliza a leitura dos arquivos para que os arquivos sejam 
     * fechados ao encerramento do programa. Ou faz a rotina de encerramento
     * esperar ateh que uma operacao de leitura esteja terminada, e entao a 
     * rotina de encerramento fecha todos os arquivos abertos e termina o 
     * programa.
     */
    public static Lock FINALIZE_LOCK = new ReentrantLock();
    /*
     * Diretorio onde devem ser lidos e/ou gravados arquivos json
     * As classes desse projeto que acessam este diretorio devem ser executadas
     * dentro do NetBeans para que encontrem o diretorio no caminho relativo ao
     * projeto Gambiarra
     */
    private static final String JSONS_DIR_NAME = "../Gambiarra/Projeto/jsons";
    /*
    Diretorio de instalacao do programa no Ubuntu
    */
    private static final String INSTALL_DIR = "/opt";
    /*
     * Diretorio onde devem ser lidos e/ou gravados os arquivos dat
     * 
     * Diretorio da versao para instalar no Ubuntu
     */
    private static final String DAT_DIR_NAME = 
        "/clubecetico.org/Huugle/database";
    /*
     * URL base da versao online estatica do forum
     */
    private static final String URL_BASE = "https://clubecetico.org/acervo";
    /**
     * URL do arquivo para exibir no navegador o resultado das pesquisas
     * 
     * Arquivo da versao para instalar no Ubuntu
     */
    public static final String RESULTS_FILENAME = "/tmp/huugleResults.html";
    /**
     * Nome do arquivo com os indices que recuperam uma userID pela chave 
     * primaria nick do usuario
     */
    public static final String NICKS_USER_FILENAME = "/nicks_user.dat";
    /**
     * Nome do arquivo com os indices de todas as palavras catalogadas em textos
     * de postagens.
     */
    public static final String WORDS_FILENAME = "/words.dat";
    /**
     * Nome do arquivo que eh uma lista de sublistas, onde cada sublista eh uma
     * lista de posts onde ocorre uma dada palavra catalogada no arquivo 
     * WORDS_FILENAME
     */
    public static final String POST_LISTS_FILENAME = "/post_lists.dat";
    /**
     * Nome do arquivo que tem uma entrada para cada post publicado no forum.
     * Estes registros sao recuperados pela chave primaria ID do post
     */
    public static final String POST_EXTENDED_FILENAME = "/post_extended.dat";
    /**
     * Nome do arquivo que tem uma entrada para cada topico publicado no forum
     */
    public static final String TOPIC_EXTENDED_FILENAME = "/topic_extended.dat";
    /**
     * Nome do arquivo contendo os indices de todas as palavras catalogadas em
     * titulos de topicos.
     */
    public static final String TOPICS_WORDS_FILENAME = "/topics_words.dat";
    /**
     * Nome do arquivo que eh uma lista de sublistas, onde cada sublista eh uma
     * lista de posts onde ocorre uma dada palavra catalogada no arquivo 
     * TOPICS_WORDS_FILENAME
     */
    public static final String POST_LISTS_FOR_TOPICS_FILENAME = 
        "/post_lists_for_topics.dat";
    /*
    Nome do arquivo PDF com o manual do programa
    */
    private static final String HELP_FILENAME = "/manual.pdf";
      
    /**************************************************************************
     * Strings para compor o arquivo HTML listando resultados de uma pesquisa
     *************************************************************************/ 
    public static final String PREFIX = 
       "<tr><td class=\"subject windowbg2\"><div ><span id=\"msg_\"><a href=\"";
    public static final String INFIX_1 = 
       "</a></span></div></td><td class=\"stats windowbg\">";
    public static final String INFIX_2 = 
       "</td><td class=\"lastpost windowbg2\">";
    public static final String SUFIX = 
       "<br /></td></tr>\n";
    
    public static final String PART_1A = 
"<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" " +
"\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">" +
"<html xmlns=\"http://www.w3.org/1999/xhtml\">" +
"<head><link rel=\"stylesheet\" type=\"text/css\" " +
"href=\"";
    
    public static final String PART_1B =
"/index_green.css?fin20\" />" +
"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />" +
"<meta name=\"description\" content=\"Pesquisa\" />" +
"<title>Pesquisa</title><link rel=\"canonical\" " +
"href=\"../forum/search.html\" /><link rel=\"index\" " +
"href=\"../forum/search.html\" /></head><body>" +
"<div id=\"wrapper\" style=\"width: 90%\">" +
"<div id=\"header\"><div class=\"frame\">" +
"<div id=\"top_section\"><h1 class=\"forumtitle\">" +
"<a href=\"../index-2.html\">" +
"<img src=\"";
    
    public static final String PART_1C = 
"/logo.png\" " +
"alt=\"Forum Clube Cetico\" /></a></h1></div>" +
"<div id=\"upper_section\" class=\"middletext\"><div class=\"user\">" +
"</div><div><a href=\"../index-2.html\"></a></div>" +
"<div class=\"news normaltext\"></div></div>" +
"<br class=\"clear\" /></div></div>" +
"<div id=\"content_section\"><div class=\"frame\">" +
"<div id=\"main_content_section\"><div class=\"navigate_section\"><ul><li>" +
"<a href=\"../index-2.html\"><span>Forum Clube Cetico</span></a> &#187;" +
"</li><li class=\"last\"><span>Pesquisa : ";
 
    public static final String PART_2 =
"</span></li></ul></div>" +
"<a id=\"top\"></a><div class=\"pagesection\">" +
"<div class=\"pagelinks floatleft\">P\u00e1ginas: [<strong>1</strong>]" +
"  &nbsp;&nbsp;<a href=\"#bot\"><strong>Ir para o Fundo</strong></a></div>" +
"</div><div class=\"tborder topic_table\" id=\"messageindex\">" +
"<table class=\"table_grid\" cellspacing=\"0\"><thead>" +
"<tr class=\"catbg\">" +
"<th scope=\"col\" class=\"lefttext first_th\">T\u00f3pico</th>" +
"<th scope=\"col\" width=\"14%\">Rank</th>" +
"<th scope=\"col\" class=\"lefttext last_th\" width=\"22%\">Data</th>" +
"</tr></thead><tbody>\n";
    
    public static final String PART_3 = 
"</tbody></table></div><a id=\"bot\"></a><div class=\"pagesection\">" +
"<div class=\"pagelinks\">P\u00e1ginas: [<strong>1</strong>]  " +
"&nbsp;&nbsp;<a href=\"#top\"><strong>Ir para o Topo</strong></a></div>" +
"</div>	</div> </div></div><div id=\"footer_section\"><div class=\"frame\">" +
"</div></div></div></body></html>";
  
    public static final int VAR_LENGTH = 
        PREFIX.length() + INFIX_1.length() + INFIX_2.length() + SUFIX.length()
        + 500;
    
    public static final int CONST_LENGTH = 
        PART_1A.length() + PART_1B.length() + PART_1C.length() + 
        PART_2.length();
    
    
    /*[01]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Inicializa nomes de arquivos e diretorios e tambem constroi a lista com 
     * as palavras que foram excluidas do indice por serem bastante comuns
     * 
     * @throws IOException Em caso de erro de IO
     */
    public static void initializeDirNames() throws IOException
    {
        jsonsDirName = JSONS_DIR_NAME; 
        installDir = INSTALL_DIR;
        datDirName = installDir + DAT_DIR_NAME;
        urlBase = URL_BASE;
                   
        helpFile = new File(datDirName + HELP_FILENAME);
        
        TextFileReader reader = 
            new TextFileReader(datDirName + "/excludeds.dat", 2048);
        String line;
        while((line = reader.readln()) != null)
        {
            EXCLUDEDS.add(line);
            EXCLUDEDS.add('+' + line);
            EXCLUDEDS.add('-' + line);
        }
        reader.close();
    }//initializeFilenames()
    
   
    /*[02]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
     /**
     * Inicializacao de objetos globais do programa Huugle
     * 
     * @throws IOException Em caso de erro de IO
     */
    public static void initializer() throws IOException
    {
        initializeDirNames();
           
        topicsWordFinder = new WordReg(datDirName + TOPICS_WORDS_FILENAME);
        
        topicsWordFinder.openToRead();
        
        postsWordFinder = new WordReg(datDirName + WORDS_FILENAME);
        
        postsWordFinder.openToRead();
               
        topicsPostRegArray = 
            new PostRegArray(datDirName + POST_LISTS_FOR_TOPICS_FILENAME);

        postsPostRegArray = new PostRegArray(datDirName + POST_LISTS_FILENAME);        
                
        nicksUserArray = new NicksUserArray();
        postExtendedArray = new PostExtendedArray();
        topicExtendedArray = new TopicExtendedArray();
       
    }//initializer()
    
    /***************************************************************************
     * 
     **************************************************************************/
    private static class TextFileReader
    {
        private final BufferedReader in;

        /*[00]------------------------------------------------------------------
        *
        ----------------------------------------------------------------------*/
        private TextFileReader
        (
            final String fileName,
            final int buffer
        ) 
            throws IOException
        {
            in = new BufferedReader
                 (
                     new FileReader(fileName, StandardCharsets.UTF_8), 
                     buffer
                 );
        }//construtor

        /*[01]------------------------------------------------------------------
        *
        ----------------------------------------------------------------------*/
        public String readln() throws IOException
        {
            return in.readLine();
        }//writeln()

        /*[02]------------------------------------------------------------------
        *
        ----------------------------------------------------------------------*/
        public void close() throws IOException
        {
            in.close();
        }//close()

    }//classe interna TextFileReader
    
}//classe Global


