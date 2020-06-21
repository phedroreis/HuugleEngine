package br.com.hkp.searchengine.arrays;

import static 
    br.com.hkp.searchengine.registers.RegistersConstants.DATE_STRLENGTH;
import static 
    br.com.hkp.searchengine.registers.RegistersConstants.FILE_STRLENGTH;
import static 
    br.com.hkp.searchengine.registers.RegistersConstants.POST_ID_STRLENGTH;
import static 
    br.com.hkp.searchengine.registers.RegistersConstants.USER_ID_STRLENGTH;
import br.com.hkp.searchengine.util.Global;
import static br.com.hkp.searchengine.util.Global.postExtendedFilename;
import java.io.IOException;

/*******************************************************************************
 *
 * @author "Pedro Reis"
 ******************************************************************************/
public final class PostExtendedArray extends Array
{
    private static final int OFFSET_AUTHOR_ID = POST_ID_STRLENGTH;
    private static final int OFFSET_DATE = OFFSET_AUTHOR_ID + USER_ID_STRLENGTH;
    private static final int OFFSET_FILE = OFFSET_DATE + DATE_STRLENGTH;
    private static final int RECORD_LENGTH = OFFSET_FILE + FILE_STRLENGTH;
   
    /*[00]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    public PostExtendedArray() throws IOException
    {
        super(postExtendedFilename, RECORD_LENGTH);
    }//construtor
    
    /*[01]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    public String getID()
    {
       return "msg" + getString(0, POST_ID_STRLENGTH); 
    }//getID()
    
    /*[02]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    public String getAuthorID()
    {
        String s = getString(OFFSET_AUTHOR_ID, USER_ID_STRLENGTH);
        if (s.matches("\\d+"))
            return "u=" + s;
        else 
            return "v=" + s;
    }//getAuthorID()
    
    /*[03]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    public String getDate()
    {
        return getString(OFFSET_DATE, DATE_STRLENGTH);
    }//getDate()
    
    /*[04]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    public String getFile()
    {
        return "topic=" + getString(OFFSET_FILE, FILE_STRLENGTH) +
               ".html#" + getID();
    }//getFile()
    
    /*[05]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    @Override
    protected String getKey()
    {
        return getString(0, POST_ID_STRLENGTH); 
    }//getKey()
    
    /*[06]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    private static final String P1 = "%-" + (POST_ID_STRLENGTH + 3) + "s : ";
    private static final String P2 = "%-" + (USER_ID_STRLENGTH + 2) + "s | ";
    private static final String P3 = "%-" + DATE_STRLENGTH + "s | ";
    private static final String P4 = "%-" + (FILE_STRLENGTH + 22) + "s";
    
    private static final String FORMAT = P1 + P2 + P3 + P4;
    
    /**
     * Retorna uma representacao textual do objeto
     * 
     * @return Uma representacao textual do objeto
     */
    @Override
    public String toString()
    {
        return String.format
        (
            FORMAT, 
            getID(), 
            getAuthorID(), 
            getDate(),
            getFile()
        );
    }//toString()
    
    /*[07]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    @SuppressWarnings("CallToPrintStackTrace")
    public static void main(String[] args)
    {
        try
        {
            Global.initializer();
            PostExtendedArray p = new PostExtendedArray();
            for (int i = 0; i < 500; i++)
            {
                p.baseAdress = RECORD_LENGTH * 2 * i;
                System.out.println(p);
            }
            
            if (p.read("999999"))
            {
                System.out.println("");
                System.out.println(p);
            }
            else System.out.println("Não existe");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    
    }//main()
    
}//PostExtendedArray
