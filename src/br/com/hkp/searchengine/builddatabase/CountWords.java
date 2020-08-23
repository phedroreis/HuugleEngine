package br.com.hkp.searchengine.builddatabase;

import br.com.hkp.searchengine.json.JsonReader;
import br.com.hkp.searchengine.json.PostJson;
import static br.com.hkp.searchengine.json.PostJson.POST_POST_INDEX;
import static 
    br.com.hkp.searchengine.registers.RegistersConstants.WORD_STRLENGTH;
import br.com.hkp.searchengine.util.Global;
import br.com.hkp.searchengine.util.Util;
import java.io.IOException;
import java.util.HashMap;
import java.util.TreeMap;

/*******************************************************************************
 * Cria um arquivo de log com as frequencias de cada palavra catalogavel em
 * textos de postagens
 * 
 * @author "Pedro Reis"
 * @since 7 de junho de 2020 v1.0
 * @version 1.0
 ******************************************************************************/
public class CountWords
{
    private static BuildLog buildLog;//constroi arquivo de log
    
    /*[01]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    private static void count() throws IOException
    {
               
        /*
        Recebe os registros de todos os posts
        */
        PostJson postJson = new PostJson();
        
        /*
        Leitor sequencial do arquivo posts.json
        */
        JsonReader<PostJson> jsonReader = 
            new JsonReader<>(postJson, "/posts.json", 2048000);
        
       
        HashMap<String, Integer> postsWords;
        
        /*
        Registra a frequencia de ocorrencia de cada palavra
        */
        TreeMap<String, Integer> frequencies = new TreeMap<>();
        
        jsonReader.open();
        
        while ((postJson = jsonReader.read()) != null)
        {
        
            String thePost = postJson.getValue(POST_POST_INDEX);
  
            postsWords = Util.searchPost(thePost, "title");

           
            for (String word: postsWords.keySet())
            {
               
                if (frequencies.containsKey(word))
                {
                    int frequency = frequencies.get(word);
                    frequencies.replace(word, frequency + 1);
                }
                else
                {
                   frequencies.put(word, 1);
                }

            }//for word

            if ((jsonReader.getCountRead() % 1000) == 0) 
                System.out.println(jsonReader.getCountRead());

        }//while
              
        jsonReader.close();

        System.out.println
        (
            "\n" + frequencies.size() + " palavras catalogadas\n"
        );
        
        buildLog.writeln("\n" + frequencies.size() + " palavras catalogadas\n");
       
        TreeMap<Integer, String> sorted = new TreeMap<>();
        int count = 0;
        buildLog.writeln(" "); 
        for(String dup: frequencies.keySet())
        {
            count++;
            int f = frequencies.get(dup);
            sorted.put(f, dup);
            buildLog.writeln(dup + " ocorreu " + f + " vez(es)");
        }
        
        buildLog.writeln(" "); 
        String format = "%-" + WORD_STRLENGTH + "s : %5d";
        for(Integer f: sorted.keySet())
        {
            buildLog.writeln(String.format(format, sorted.get(f), f));
        }
        
        buildLog.writeln(count + " palavras");
       
    }//count()
    
    /*[02]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Faz um inventario da frequencia de palavras catalogaveis em textos de 
     * posts
     * 
     * @param args n\a
     */
    @SuppressWarnings("CallToPrintStackTrace")
    public static void main(String[] args)
    {
        try
        {
            Global.initializeDirNames();
            
            buildLog = new BuildLog("/count_words.log", 1024000);
            
            count();
            
            buildLog.close();
        }
        catch (IOException  e)
        {
            e.printStackTrace();
        }
        
    }//main()
    
}//CountWords
