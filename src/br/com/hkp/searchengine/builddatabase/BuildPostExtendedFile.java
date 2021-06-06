package br.com.hkp.searchengine.builddatabase;


import br.com.hkp.searchengine.registers.PostExtendedReg;
import br.com.hkp.searchengine.json.JsonReader;
import br.com.hkp.searchengine.json.PostJson;
import static br.com.hkp.searchengine.json.PostJson.POST_AUTHORID_INDEX;
import static br.com.hkp.searchengine.json.PostJson.POST_DATE_INDEX;
import static br.com.hkp.searchengine.json.PostJson.POST_ID_INDEX;
import static br.com.hkp.searchengine.json.PostJson.POST_INDEX_INDEX;
import static br.com.hkp.searchengine.json.PostJson.POST_TOPICID_INDEX;
import br.com.hkp.searchengine.util.Global;
import static br.com.hkp.searchengine.util.Global.datDirName;
import static br.com.hkp.searchengine.util.Global.postExtendedFilename;
import br.com.hkp.searchengine.util.Util;
import java.io.IOException;
import java.util.Comparator;
import java.util.TreeSet;

/******************************************************************************
 * Constroi um arquivo de indice contendo registros de todos os posts 
 * catalogados do CC. 
 * <p>
 * Cada registro indica a ID do post, ID do autor do post, data de publicacao e
 * nome do arquivo HTML onde esta o post
 * 
 * @author "Pedro Reis"
 * @since 3 de junho de 2020 v1.0
 * @version 1.0
 ******************************************************************************/
public final class BuildPostExtendedFile
{
    private static BuildLog buildLog;//constroi arquivo de log
    
    /*[00]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /*
    Retorna o nome do arquivo que contem o post numero "index" do topico 
    "topicID"
    */
    private static String getFile(final String topicID, final String index)
    {
        return 
        (
            topicID + '.' + 
            String.valueOf((Integer.valueOf(index) / 25) * 25) +
            ".html"
        );
               
    }//getFile()
    
    /*[01]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    private static void make(final TreeSet<PostExtendedReg> index) 
        throws IOException
    {
        PostExtendedReg postExtendedReg = 
            new PostExtendedReg(datDirName + postExtendedFilename);
            
        postExtendedReg.openToWrite();
        
        buildLog.writeln("Gravando arquivo dat...\n");
        
        /*
        Grava o index ordenado por chave primaria no arquivo
        */
        int count = 0;
        for (PostExtendedReg post: index)
        {
            count++;
            postExtendedReg.setID(post.getID());
            postExtendedReg.setAuthorID(post.getAuthorID());
            postExtendedReg.setDate(post.getDate());
            postExtendedReg.setFile(post.getFile());
            postExtendedReg.write();
            buildLog.writeln(postExtendedReg.toString());
        }//for
        
        postExtendedReg.close();
        
        buildLog.writeln("\n" + count + " registros gravados");
      
    }//make() 
    
    /*[02]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Grava o arquivo de indice com registros de todos os posts
     * 
     * @param args n/a
     */
    @SuppressWarnings("CallToPrintStackTrace")
    public static void main(String[] args)
    {
        try
        {
            buildLog = new BuildLog("/posts_extended.log", 1024000);
            
            /*
            Inicializa variaveis globais como nomes de arquivos e diretorios
            */
            Global.initializeFilenames();
            
            /*
            Todos os registros construidos a partir de dados obtidos de um
            arquivo json serao inseridos nesta estrutura que os ordena por 
            chave primaria
            */
            TreeSet<PostExtendedReg> index = 
                new TreeSet<>(new PostExtendedRegComparator());

            /*
            Objeto que armazena um registro lido de um arquivo json
            */
            PostJson postJson = new PostJson();

            /*
            Arquivo json que serah a fonte de dados para construir o indice em
            um arquivo .dat
            */
            JsonReader<PostJson> jsonReader = 
                new JsonReader<>(postJson, "/posts.json", 1024000);
            
            buildLog.writeln("Lendo arquivo posts.json...\n");
               
        
            jsonReader.open(); 
            while ((postJson = jsonReader.read()) != null)
            {
                PostExtendedReg postExtendedReg = new PostExtendedReg(null);
                postExtendedReg.setID(postJson.getValue(POST_ID_INDEX));
                postExtendedReg.setAuthorID
                (
                    postJson.getValue(POST_AUTHORID_INDEX)
                );
                postExtendedReg.setDate
                (
                    Util.date2Number(postJson.getValue(POST_DATE_INDEX))
                );
                postExtendedReg.setFile
                (
                    getFile
                    (
                        postJson.getValue(POST_TOPICID_INDEX),
                        postJson.getValue(POST_INDEX_INDEX)
                    )
                );
                
                index.add(postExtendedReg);
                                      
                if ((jsonReader.getCountRead() % 100) == 0)
                    System.out.println(jsonReader.getCountRead());
                
                buildLog.writeln(postExtendedReg.toString());
                   
            }//while
            jsonReader.close();
                       
            System.out.println("\n" + index.size() + " registros lidos\n");
            
            buildLog.writeln("\n" + index.size() + " registros lidos\n");
            
            /*
            Cria o arquivo .dat de indice
            */
            make(index);
            
            /*
            Fecha arquivo de log
            */
            buildLog.close();  
              
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
    }//main()
    
    /***************************************************************************
    *
    ***************************************************************************/
    /*
    Comparator para ordenar o TreeSet do indice pela chave primaria que eh a 
    ID do post
    */
    private static class PostExtendedRegComparator 
        implements Comparator<PostExtendedReg>
    {
        @Override
        public int compare(PostExtendedReg r1, PostExtendedReg r2)
        {
            return (r1.getKey()).compareTo(r2.getKey());
        }        
    }//classe PostExtendedRegComparator
   
}//classe BuildPostExtendedFile