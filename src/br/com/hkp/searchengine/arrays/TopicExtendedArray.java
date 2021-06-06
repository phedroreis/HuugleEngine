package br.com.hkp.searchengine.arrays;

import static
    br.com.hkp.searchengine.registers.RegistersConstants.TITLE_STRLENGTH;
import static
    br.com.hkp.searchengine.registers.RegistersConstants.TOPIC_ID_STRLENGTH;
import br.com.hkp.searchengine.util.Global;
import static br.com.hkp.searchengine.util.Global.topicExtendedFilename;
import java.io.IOException;

/*******************************************************************************
 *
 * @author "Pedro Reis"
 ******************************************************************************/
public final class TopicExtendedArray extends Array
{
    private static final int OFFSET_TITLE = TOPIC_ID_STRLENGTH;
    private static final int OFFSET_RANK = OFFSET_TITLE + TITLE_STRLENGTH;
    private static final int RECORD_LENGTH = OFFSET_RANK + 1;
    
    /*[00]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    public TopicExtendedArray() throws IOException
    {
        super(topicExtendedFilename, RECORD_LENGTH);
    }//construtor
    
    /*[01]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    public String getID()
    {
        return "topic=" + getString(0, TOPIC_ID_STRLENGTH);
    }//getID()
    
    /*[02]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    public String getTitle()
    {
       return getString(OFFSET_TITLE, TITLE_STRLENGTH);
    }//getTitle()
    
    /*[03]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    public int getRank()
    {
       
       return getShort(OFFSET_RANK);
    }//getRank()
    
    /*[04]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    @Override
    protected String getKey()
    {
        return getString(0, TOPIC_ID_STRLENGTH);
    }//getKey()
    
    /*[05]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    private static final String P1 = "%-" + (TOPIC_ID_STRLENGTH + 6) + "s : ";
    private static final String P2 = "%-" + TITLE_STRLENGTH  + "s | ";
    private static final String P3 = "%3d";
       
    private static final String FORMAT = P1 + P2 + P3;
    /**
     * Retorna uma representacao textual do objeto
     * 
     * @return Uma representacao textual do objeto
     */
    @Override
    public String toString()
    {
        return  String.format(FORMAT, getID(), getTitle(), getRank());
    }//toString()
    
    /*[06]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    @SuppressWarnings("CallToPrintStackTrace")
    public static void main(String[] args)
    {
        try
        {
            Global.initializer();
            TopicExtendedArray p = new TopicExtendedArray();
            for (int i = 0; i < 100; i++)
            {
                p.baseAdress = RECORD_LENGTH * 2 * i;
                System.out.println(p);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
    }//main()
    
}//TopicExtendedArray
