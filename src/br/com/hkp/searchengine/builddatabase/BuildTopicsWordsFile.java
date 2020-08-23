package br.com.hkp.searchengine.builddatabase;

import br.com.hkp.searchengine.json.JsonReader;
import br.com.hkp.searchengine.json.TopicJson;
import static br.com.hkp.searchengine.json.TopicJson.TOPIC_POSTID_INDEX;
import static br.com.hkp.searchengine.json.TopicJson.TOPIC_TITLE_INDEX;
import br.com.hkp.searchengine.registers.PostReg;
import static
    br.com.hkp.searchengine.registers.RegistersConstants.WORD_STRLENGTH;
import br.com.hkp.searchengine.registers.WordReg;
import br.com.hkp.searchengine.util.Global;
import static br.com.hkp.searchengine.util.Global.datDirName;
import static 
    br.com.hkp.searchengine.util.Global.POST_LISTS_FOR_TOPICS_FILENAME;
import static br.com.hkp.searchengine.util.Global.TOPICS_WORDS_FILENAME;
import br.com.hkp.searchengine.util.Util;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.TreeSet;

/*******************************************************************************
 *
 * @author "Pedro Reis"
 * @since 6 de junho de 2020 v1.0
 * @version 1.0
 ******************************************************************************/
public class BuildTopicsWordsFile
{
    private static BuildLog buildLog;//constroi arquivo de log
       
    /*[01]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    private static void make(final TreeMap<String, TreeSet<PostReg>> index) 
        throws IOException
    {
        WordReg wordReg = new WordReg(datDirName + TOPICS_WORDS_FILENAME);
        
        PostReg postReg = 
            new PostReg(datDirName + POST_LISTS_FOR_TOPICS_FILENAME);
           
        wordReg.openToWrite();
        
        postReg.openToWrite();
        
        TreeSet<PostReg> postRegList;
        
        buildLog.writeln("Gravando arquivos dat...\n");
        
        int count = 0; int postIndex = 0;
        for (String word: index.keySet())
        {
            count++;
            wordReg.setWord(word);
            wordReg.setFirstPost(postIndex);
            postRegList = index.get(word);
            for (PostReg reg: postRegList)
            {
                postReg.setID(reg.getID());
                postReg.setRank(reg.getRank());
                postReg.write();
                postIndex++;
            }
            wordReg.setLastPost(postIndex - 1);
            wordReg.write();
            
            buildLog.writeln(wordReg.toString());
        }//for
        
        wordReg.close();
        postReg.close();
        
        buildLog.writeln("\n" + count + " palavras catalogadas");
      
    }//make() 
    
    /*[02]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    private static void buildIndexes() throws IOException
    {
        /*
        Uma arvore que indexa todas as palavras catalogadas em titulos de 
        topicos em ordem lexografica, e associa a cada palavra uma lista de 
        posts. Que sao os posts inauguarais dos topicos em cujos titulos a dada
        palavra ocorre. Lista esta ordenada por um campo rank pretendendo
        inferir a relevancia de um post ser apresentado como resultado em uma 
        pesquisa pela palavra que aponta para essa referidalista ordenada de
        posts.
        */
        TreeMap<String, TreeSet<PostReg>> index = new TreeMap<>();
        
        /*
        Cria um objeto topicJson para ler sequencialmente um arquivo com 
        registros de todos os topicos jah publicados no forum. topicJson recebe
        um registro lido de um arquivo json
        */
        TopicJson topicJson = new TopicJson();
        
        /*
        Objeto utilizado para ler sequencialmente o arquivo topics.json, com 
        registros de todos os topicos e dados sobre estes topicos como autor,
        data, etc...
        */
        JsonReader<TopicJson> jsonReader = 
            new JsonReader<>(topicJson, "/topics.json", 512000);
        
        /*
        Para cada titulo de topico lido, serah gerado um HashMap associando 
        todas as palavras catalogadas nesse titulo a um ranking que indica a 
        relevancia desse topico ser apresentado como resultado de uma busca por 
        essa dada palavra
        */
        HashMap<String, Integer> titlesWords;
        
        /*
        Cada palavra catalogada apontarah para uma lista de posts que sao os 
        posts inaugurais dos topicos em cujo titulo a dada palavra ocorre
        */
        TreeSet<PostReg> postRegList;
        
        TreeMap<String, Integer> frequencies = new TreeMap<>();
      
        /*
        Abre o arquivo topics.json de onde serao lidos os registros de topicos
        */
        jsonReader.open();
        
        while ((topicJson = jsonReader.read()) != null)
        {
            String topicTitle = topicJson.getValue(TOPIC_TITLE_INDEX);             
            String firstPostID = topicJson.getValue(TOPIC_POSTID_INDEX);
          
            /*
            postsWords eh um HashMap associando cada palavra catalogada no 
            post a um rank de relevancia desse post em uma busca por essa
            palavra
            */
            titlesWords = Util.searchTitle(topicTitle);

            /*
            Todas as palavras em posts words devem ser inseridas no TreeMap
            index, assim como a lista postRegList referente a cada palavra
            em index.
            */
            for (String word: titlesWords.keySet())
            {
                /*
                Cria um no (registro) que sera inserido na postRegList
                associada a palavra word que sera inserida em index
                */
                int rank = titlesWords.get(word);

                PostReg postReg = new PostReg(null);
                postReg.setID(firstPostID);
                postReg.setRank(rank);
              

                /*
                Se o index jah contem a palavra, entao o no postReg eh
                inserido na postRegList ja associada a esta palavra no index
                */
                if (index.containsKey(word))
                {
                    
                    postRegList = index.get(word);
                    postRegList.add(postReg);
                    int frequency = frequencies.get(word);
                    frequencies.replace(word, frequency + 1);
                }
                /*
                Se o index ainda nao contem a nova palavra, ela eh inserida
                no index e uma postRegList eh criada associada a esta 
                palavra. Entao o noh postReg eh inserido nesta lista vazia
                */
                else
                {
                    postRegList = new TreeSet<>(new PostRegComparator());
                    postRegList.add(postReg);
                    index.put(word, postRegList);
                    frequencies.put(word, 1);
                }

            }//for word

            if ((jsonReader.getCountRead() % 100) == 0) 
                System.out.println(jsonReader.getCountRead());

        }//while
        
        jsonReader.close();

        System.out.println("\n" + index.size() + " palavras catalogadas\n");
        
        buildLog.writeln("\n" + index.size() + " palavras catalogadas\n");

        /*
        Cria o indice com todas as palavras catalogadas nesta chamada de 
        metodo.
        */
        make(index);
        
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
   
    }//buildIndexes()
    
    /*[03]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    @SuppressWarnings("CallToPrintStackTrace")
    public static void main(String[] args)
    {
        try
        {
            Global.initializeDirNames();
            
            buildLog = new BuildLog("/topics_words.log", 1024000);
            
            buildIndexes();
            
            buildLog.close();
        }
        catch (IOException  e)
        {
            e.printStackTrace();
        }
        
    }//main()
    
    /***************************************************************************
    *
    ***************************************************************************/
    /*[00]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    private static class PostRegComparator implements Comparator<PostReg>
    {
        @Override
        public int compare(PostReg r1, PostReg r2)
        {
            int r = (r2.getRank() - r1.getRank());
            if (r == 0)
            /*
            Eh chamado o metodo getID() porque a classe PostReg nao implementa
            getKey(), pois um arquivo com esse tipo de registro nao tem chave
            primaria
            */  
                return (r1.getID()).compareTo(r2.getID());
            else
                return r;
        }        
    }//classe PostRegComparator
    
}//BuildTopicsWordsFile
