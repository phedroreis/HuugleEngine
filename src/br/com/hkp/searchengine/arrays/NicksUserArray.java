package br.com.hkp.searchengine.arrays;

import static 
    br.com.hkp.searchengine.registers.RegistersConstants.NICK_STRLENGTH;
import static 
    br.com.hkp.searchengine.registers.RegistersConstants.USER_ID_STRLENGTH;
import br.com.hkp.searchengine.util.Global;
import static br.com.hkp.searchengine.util.Global.nicksUserFilename;
import java.io.IOException;

/*******************************************************************************
 *
 * @author "Pedro Reis"
 ******************************************************************************/
public final class NicksUserArray extends Array
{
    private static final int OFFSET_USER_ID = NICK_STRLENGTH;
    private static final int RECORD_LENGTH = OFFSET_USER_ID + USER_ID_STRLENGTH;
    
    /*[00]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    public NicksUserArray() throws IOException
    {
        super(nicksUserFilename, RECORD_LENGTH);
    }//construtor
    
    /*[01]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    public String getNick()
    {
        return getString(0, NICK_STRLENGTH);
    }//getNick()
    
    /*[02]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    public String getUserID()
    {
        String s = getString(OFFSET_USER_ID, USER_ID_STRLENGTH);
        if (s.matches("\\d+"))
            return "u=" + s;
        else 
            return "v=" + s;
    }//getUserID()
    
    /*[03]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    @Override
    protected String getKey()
    {
        return getNick();
    }//getKey()
    
    /*[04]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    private static final String P1 = "%-" + NICK_STRLENGTH + "s : ";
    private static final String P2 = "%-" + (USER_ID_STRLENGTH + 2) + "s";
    
    private static final String FORMAT = P1 + P2; 
    
    /**
     * Retorna uma representacao textual do objeto
     * 
     * @return Uma representacao textual do objeto
     */
    @Override
    public String toString()
    {
        return String.format(FORMAT, getNick(), getUserID());
    }//toString()
    
    /*[05]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    @SuppressWarnings("CallToPrintStackTrace")
    public static void main(String[] args)
    {
        try
        {
            Global.initializer();
            NicksUserArray p = new NicksUserArray();
            for (int i = 0; i < 50; i++)
            {
                p.baseAdress = RECORD_LENGTH * 2 * i;
                System.out.println(p.getNick());
                System.out.println(p.getUserID());
                System.out.println("");
                
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
    }//main()
    
}//NicksUserArray
