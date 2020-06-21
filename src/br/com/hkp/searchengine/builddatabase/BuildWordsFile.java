package br.com.hkp.searchengine.builddatabase;

import br.com.hkp.searchengine.json.JsonReader;
import br.com.hkp.searchengine.json.PostJson;
import static br.com.hkp.searchengine.json.PostJson.POST_ID_INDEX;
import static br.com.hkp.searchengine.json.PostJson.POST_POST_INDEX;
import static br.com.hkp.searchengine.json.PostJson.POST_TOPICID_INDEX;
import br.com.hkp.searchengine.registers.PostReg;
import br.com.hkp.searchengine.registers.TopicExtendedReg;
import br.com.hkp.searchengine.registers.WordReg;
import br.com.hkp.searchengine.util.Global;
import static br.com.hkp.searchengine.util.Global.datDirName;
import static br.com.hkp.searchengine.util.Global.topicExtendedFilename;
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
public class BuildWordsFile
{
    private static BuildLog buildLog;//constroi arquivo de log
    
    /*private final static int[] HEAP_LIMIT = 
        {3000, 3000, 3000, 3000, 1800, 3000, 4000};*/
    
    /*[01]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    private static void make
    (
        final TreeMap<String, TreeSet<PostReg>> index,
        final int step
    ) 
        throws IOException
    {
        WordReg wordReg = 
            new WordReg(datDirName + "/words" + step + ".dat");
        
        PostReg postReg =
            new PostReg(datDirName + "/post_lists" + step + ".dat");
           
        wordReg.openToWrite();
        
        postReg.openToWrite();
        
        TreeSet<PostReg> postRegList;
        
        buildLog.writeln("Passo " + step + ": gravando arquivo dat...\n");
        
        int countWords = 0; int postRegIndex = 0;
        for (String word: index.keySet())
        {
            countWords++;
            wordReg.setWord(word);
            wordReg.setFirstPost(postRegIndex);
            postRegList = index.get(word);
            for (PostReg reg: postRegList)
            {
                postReg.setID(reg.getID());
                /*
                Quando o post esta catalogado na sublista de uma palavra que 
                tambem ocorre no titulo do topico, o setter do rank deve receber
                o valor com sinal trocado. Ou seja, negativo. O valor negativo
                informa que a palavra tb existe no titulo do topico e que o 
                metodo presentOnTitle() deve retornar 1 e nao 0
                */
                if (reg.presentOnTitle() == 1)
                    postReg.setRank(-reg.getRank());
                else
                    postReg.setRank(reg.getRank());
                postReg.write();
                postRegIndex++;
            }
            wordReg.setLastPost(postRegIndex - 1);
            wordReg.write();
            
            buildLog.writeln(wordReg.toString());
        }//for
        
        wordReg.close();
        postReg.close();
        
        buildLog.writeln
        (
            "\n" + countWords + " palavras catalogadas no passo " + step
        );
      
    }//make() 
    
    /*[02]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    private static void buildIndexes(final int count) throws IOException
    {
        /*
        Essa estrutura indexa todas as palavras catalogadas e suas listas de 
        posts onde ocorrem
        */
        TreeMap<String, TreeSet<PostReg>> index = new TreeMap<>();
        
        /*
        Recebe os registros de todos os posts
        */
        PostJson postJson = new PostJson();
        
        /*
        Leitor sequencial do arquivo posts.json
        */
        JsonReader<PostJson> jsonReader = 
            new JsonReader<>(postJson, "/posts.json", 512000);
        
        /*
        Para cada texto de post lido, serah gerado um HashMap associando 
        todas as palavras catalogadas nesse titulo a um ranking que indica a 
        relevancia desse post ser apresentado como resultado de uma busca por 
        essa dada palavra
        */
        HashMap<String, Integer> postsWords;
        
        /*
        Cada palavra catalogada apontarah para uma lista de posts onde a palavra
        ocorre
        */
        TreeSet<PostReg> postRegList;
        
               
        TopicExtendedReg topicExtendedReg = 
            new TopicExtendedReg(datDirName + topicExtendedFilename);
        
        topicExtendedReg.openToRead();
        
        /*
        Abre o arquivo posts.json de onde serao lidos os registros de posts
        */
        jsonReader.open();
        
        /*
        O arquivo de registros de posts serah lido em 7 etapas, cada uma 
        realizada por uma chamada a este metodo buildIndexes()
        */
        int first2Read = count * 100000;
        int last2Read = first2Read + 99999;
        int toRead = -1;
        
        while ((postJson = jsonReader.read()) != null)
        {
            /*
            Determina quando comecar a processar os registros lidos e quando
            parar.
            */
            toRead++;
            if (toRead < first2Read) continue;
            if (toRead > last2Read) break;
            
            
            String postID = postJson.getValue(POST_ID_INDEX); 
            String topicID = postJson.getValue(POST_TOPICID_INDEX);
            String thePost = postJson.getValue(POST_POST_INDEX);
            
            topicExtendedReg.read(topicID.substring(6, topicID.length()));
            String topicTitle = topicExtendedReg.getTitle();
                   
            /*
            postsWords eh um HashMap associando cada palavra catalogada no 
            post a um rank de relevancia desse post em uma busca por essa
            palavra
            */
            postsWords = Util.searchPost(thePost, topicTitle);

            /*
            Todas as palavras em posts words devem ser inseridas no TreeMap
            index, assim como a lista postRegList referente a cada palavra
            em index.
            */
            for (String word: postsWords.keySet())
            {
                /*
                Cria um no (registro) que sera inserido na postRegList
                associada a palavra word que sera inserida em index
                rank com valor superior a 100000 indica que a palavra tb ocorre
                no titulo do topico. E o valor correto do rank é rank - 100000
                pois os 100000 acrescidos sao apenas para passar esta informacao
                
                Nesse caso o rank eh atribuido com sinal trocado e negativo,
                para passar esta informacao ao objeto
                */
                int rank = postsWords.get(word);
                if (rank > 100000) rank = 100000 - rank;

                PostReg postReg = new PostReg(null);
                postReg.setID(postID);
                postReg.setRank(rank);
              

                /*
                Se o index jah contem a palavra, entao o no postReg eh
                inserido na postRegList ja associada a esta palavra no index
                */
                if (index.containsKey(word))
                {
                    
                    postRegList = index.get(word);
                    if (postRegList.size() > 3000)//ou HEAP_LIMIT[count]
                    {
                        PostReg last = postRegList.last();
                        if (postReg.getRank() > last.getRank())
                        {
                            postRegList.remove(last);
                            postRegList.add(postReg);
                        }
                    }
                    else
                        postRegList.add(postReg);
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
                   
                }

            }//for word

            if ((jsonReader.getCountRead() % 1000) == 0) 
                System.out.println(jsonReader.getCountRead());

        }//while
        
        topicExtendedReg.close();
        
        jsonReader.close();

        System.out.println("\n" + index.size() + " palavras catalogadas\n");
        
        buildLog.writeln("\n" + index.size() + " palavras catalogadas\n");

        /*
        Cria o indice com todas as palavras catalogadas nesta chamada de 
        metodo.
        */
        make(index, count);
        
    }//buildIndexes()
    
     /*[03]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    @SuppressWarnings("CallToPrintStackTrace")
    public static void main(String[] args)
    {
        try
        {
            Global.initializeFilenames();
            
            buildLog = new BuildLog("/words.log", 1024000);
            
            for (int i = 0; i <= 9; i++) buildIndexes(i);
                    
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
    
}//classe BuildWordsFile
