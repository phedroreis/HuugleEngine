package br.com.hkp.searchengine.builddatabase;


import br.com.hkp.searchengine.json.JsonReader;
import br.com.hkp.searchengine.json.TopicJson;
import static br.com.hkp.searchengine.json.TopicJson.TOPIC_DATE_INDEX;
import static br.com.hkp.searchengine.json.TopicJson.TOPIC_ID_INDEX;
import static br.com.hkp.searchengine.json.TopicJson.TOPIC_TITLE_INDEX;
import static br.com.hkp.searchengine.json.TopicJson.TOPIC_VIEWS_INDEX;
import br.com.hkp.searchengine.registers.TopicExtendedReg;
import br.com.hkp.searchengine.util.Global;
import static br.com.hkp.searchengine.util.Global.datDirName;
import static br.com.hkp.searchengine.util.Global.TOPIC_EXTENDED_FILENAME;
import br.com.hkp.searchengine.util.Util;
import java.io.IOException;
import java.util.Comparator;
import java.util.TreeSet;

/*******************************************************************************
 * Constroi um arquivo de indice contendo registros de todos os topicos 
 * catalogados do CC. 
 * 
 * @author "Pedro Reis"
 * @since 3 de junho de 2020 v1.0
 * @version 1.0
 ******************************************************************************/
public final class BuildTopicExtendedFile
{
    private static BuildLog buildLog;//constroi arquivo de log
       
   /*[01]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    private static void make(final TreeSet<TopicExtendedReg> index) 
        throws IOException
    {
        TopicExtendedReg topicExtendedReg = 
            new TopicExtendedReg(datDirName + TOPIC_EXTENDED_FILENAME);
            
        topicExtendedReg.openToWrite();
        
        buildLog.writeln("Gravando arquivo dat...\n");
        
        int count = 0;
        for (TopicExtendedReg topic: index)
        {
            count++;
            topicExtendedReg.setID(topic.getID());
            topicExtendedReg.setTitle(topic.getTitle());
            topicExtendedReg.setRank(topic.getRank());
            topicExtendedReg.write();
            buildLog.writeln(topicExtendedReg.toString());
        }//for
        
        topicExtendedReg.close();
        
        buildLog.writeln("\n" + count + " registros gravados");
      
    }//make() 
    
    /*[02]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    @SuppressWarnings("CallToPrintStackTrace")
    public static void main(String[] args)
    {
        try
        {
            buildLog = new BuildLog("/topics_extended.log", 1024000);
            
            Global.initializeDirNames();
            
            TreeSet<TopicExtendedReg> index = 
                new TreeSet<>(new TopicExtendedRegComparator());

            TopicJson topicJson = new TopicJson();

            JsonReader<TopicJson> jsonReader = 
                new JsonReader<>(topicJson, "/topics.json", 512000);
            
            buildLog.writeln("Lendo arquivo topics.json...\n");
               
        
            jsonReader.open(); 
            while ((topicJson = jsonReader.read()) != null)
            {
                TopicExtendedReg topicExtendedReg = new TopicExtendedReg(null);
                topicExtendedReg.setID(topicJson.getValue(TOPIC_ID_INDEX));
                topicExtendedReg.setTitle
                (
                    topicJson.getValue(TOPIC_TITLE_INDEX)
                );
                topicExtendedReg.setRank
                (
                    topicJson.getValue(TOPIC_VIEWS_INDEX), 
                    Util.date2Number(topicJson.getValue(TOPIC_DATE_INDEX))
                );
               
                
                index.add(topicExtendedReg);
                                      
                if ((jsonReader.getCountRead() % 100) == 0)
                    System.out.println(jsonReader.getCountRead());
                
                buildLog.writeln(topicExtendedReg.toString());
                   
            }//while
            jsonReader.close();
                       
            System.out.println("\n" + index.size() + " registros lidos\n");
            
            buildLog.writeln("\n" + index.size() + " registros lidos\n");
            
            make(index);
            
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
    private static class TopicExtendedRegComparator 
        implements Comparator<TopicExtendedReg>
    {
        @Override
        public int compare(TopicExtendedReg r1, TopicExtendedReg r2)
        {
            return (r1.getKey()).compareTo(r2.getKey());
        }        
    }//classe TopicExtendedRegComparator
   
}//classe BuildTopicExtendedFile
