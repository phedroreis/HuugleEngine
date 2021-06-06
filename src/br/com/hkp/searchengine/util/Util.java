package br.com.hkp.searchengine.util;

import static br.com.hkp.searchengine.util.Global.LOW_CHARS;
import static 
    br.com.hkp.searchengine.registers.RegistersConstants.WORD_STRLENGTH;
import static br.com.hkp.searchengine.util.Global.EXCLUDEDS;
import java.awt.Desktop;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*******************************************************************************
 *
 * @author "Pedro Reis"
 * @since 29 de abril de 2020 v1.0
 * @version 1.0
 ******************************************************************************/
public class Util
{
    /*[01]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    private static String indexMonth(String month)
    {
        switch (month)
        {
            case "Janeiro": return "01"; case "Fevereiro": return "02";  
            case "Mar\u00e7o": return "03"; case "Abril": return "04";  
            case "Maio": return "05"; case "Junho": return "06";  
            case "Julho": return "07"; case "Agosto": return "08"; 
            case "Setembro": return "09"; case "Outubro": return "10";  
            case "Novembro": return "11"; default: return "12";       
        }
        
    }//indexMonth()
    
    /*[02]------------------------------------------------------------------
    *
    ----------------------------------------------------------------------*/
    private static String monthIndex(String index)
    {
        switch (index)
        {
            case "01": return "Janeiro"; case "02": return "Fevereiro";  
            case "03": return "Mar\u00e7o"; case "04": return "Abril";  
            case "05": return "Maio"; case "06": return "Junho";  
            case "07": return "Julho"; case "08": return "Agosto"; 
            case "09": return "Setembro"; case "10": return "Outubro";  
            case "11": return "Novembro"; default: return "Dezembro";       
        }

    }//monthIndex()
    
    /*[03]------------------------------------------------------------------
    *
    ----------------------------------------------------------------------*/
    public static String number2Date(final String number)
    {
        return 
        (
            number.substring(4, 6) + " de " + 
            monthIndex(number.substring(2, 4)) +
            " de 20" + number.substring(0, 2)
        );
    }//number2Date()  

    /*[04]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    private static final Pattern PATT_DAY = 
        Pattern.compile("^\\d{2}\\b");
   
    private static final Pattern PATT_MONTH = 
        Pattern.compile("[JFMASOND][a-z\u00e7]+");
   
    private static final Pattern PATT_YEAR = 
        Pattern.compile("\\d{4}");
    /**
     * @param date A String da data por extenso no formato usado no forum
     * 
     * @return A data no formato AAMMDD
     */    
    public static String date2Number(final String date)
    {
        String day; String month; String year;
        
        Matcher matcher = PATT_DAY.matcher(date);
        
        if (matcher.find()) 
            day = matcher.group();
        else 
            throw new IllegalArgumentException
            (
                "Formato de data inv\u00e1lido!"
            );
        
        matcher = PATT_MONTH.matcher(date);
        
        if (matcher.find()) 
            month = indexMonth(matcher.group());
        else 
            throw new IllegalArgumentException
            (
                "Formato de data inv\u00e1lido!"
            );
        
        matcher = PATT_YEAR.matcher(date);
        
        if (matcher.find()) 
            year = matcher.group().substring(2,4);
        else 
            throw new IllegalArgumentException
            (
                "Formato de data inv\u00e1lido!"
            );
        
        return (year + month + day);
    }//date2Number()
    
    /*[05]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    private static final String QUOTE =
    "<div class=\"quoteheader\"><div class=\"topslice_quote\">.+?</div></div>";
    
    private static final String LINK = "<a href=.+?</a>";
    
    private static final String TAG = "<.*?>";
    
    private static String deleteTags(final String source)
    {
        return source.replaceAll(QUOTE, " ").
                      replaceAll(LINK, " ").
                      replaceAll(TAG, " ").
                      replace("&nbsp;", " ").
                      replace("&quot;", " ");
    }//deleteTags()
    
    /*[06]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    public static String stripAccents(final String s)
    {
        String n = s.toLowerCase();
        n = n.replaceAll("[\u00e0\u00e1\u00e2\u00e3\u00e4]", "a");
        n = n.replaceAll("[\u00e8\u00e9\u00ea\u00eb]", "e");
        n = n.replaceAll("[\u00ec\u00ed]", "i");
        n = n.replaceAll("[\u00f2\u00f3\u00f4\u00f5\u00f6øØ]", "o");
        n = n.replaceAll("[\u00f9\u00fa\u00fb\u00fc]", "u");
        n = n.replaceAll("[\u00f1]", "n");
        return n.replaceAll("[\u00e7]", "c");
    }//stripAccents()
      
    /*[07]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    private static final String CHARS = LOW_CHARS + LOW_CHARS.toUpperCase();
    
    private final static String REGEXP = 
        "\\b[" + CHARS + "]{4," + WORD_STRLENGTH + "}\\b";
    
    private static final Pattern P = Pattern.compile(REGEXP);
    /**
     * 
     * @param htmlPost O HTML bruto do post
     * 
     * @param title O titulo do topico onde o post foi publicado
     * 
     * @return Um HashMap associando cada palavra do post a um rank de 
     * relevancia, que vem a ser um inteiro que indica a relevancia de se 
     * retornar este post no caso de uma pesquisa por essa dada palavra
     */
    public static HashMap<String, Integer> searchPost
    (
        final String htmlPost,
        final String title
    )
    {
        HashMap<String, Integer> map = new HashMap<>();
        String lowTitle = title.toLowerCase();
        String post = deleteTags(htmlPost);
        int bonus;
       
        Matcher m = P.matcher(post);
        while (m.find())
        {
            String word = m.group().toLowerCase();
            
            if (EXCLUDEDS.contains(word)) continue;
            
            if (map.containsKey(word)) 
                map.put(word, map.get(word) + 1);
            else
            {
                if (lowTitle.contains(word))
                    bonus = 100000;
                else 
                    bonus = 0;
                map.put(word, 1 + bonus);
            }
        }//while
        
        return map;
    }//searchPost()
    
    /*[08]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    public static HashMap<String, Integer> searchTitle(final String htmlTitle)
    {
        HashMap<String, Integer> map = new HashMap<>();
        
        String title = htmlTitle.replace("&quot;", " ");
                     
        Matcher m = P.matcher(title.toLowerCase());
        while (m.find())
        {
            String word = m.group();
            if (map.containsKey(word)) 
                map.put(word, map.get(word) + 1);
            else
                map.put(word, 1);
        }//while
        
        return map;
    }//searchTitle()
       
    /*[09]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    public static void openWebpage(final URL url) 
    {
        Desktop desktop = 
            Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE))
        {
            try 
            {
                URI uri = url.toURI();
                desktop.browse(uri);
            }
            catch (Exception e)
            {
                System.err.println(e);
            }
        }
    }//openWebpage()
    
    
}//classe Util
