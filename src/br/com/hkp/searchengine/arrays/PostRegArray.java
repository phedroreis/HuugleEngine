package br.com.hkp.searchengine.arrays;

import static br.com.hkp.searchengine.registers.RegistersConstants.POST_ID_STRLENGTH;
import br.com.hkp.searchengine.util.Global;
import static br.com.hkp.searchengine.util.Global.datDirName;
import static br.com.hkp.searchengine.util.Global.POST_LISTS_FILENAME;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author "Pedro Reis"
 */
public class PostRegArray
{
    private final RandomAccessFile randomAccessFile;
    
    private static final int REGISTER_LENGTH = POST_ID_STRLENGTH * 2 + 2;
   
    /*[00]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    public PostRegArray(String fileName) throws FileNotFoundException
    {
        randomAccessFile = new RandomAccessFile(fileName, "r");
    }//construtor
    
    /*[01]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    public byte[] read(int position, int length) throws IOException
    {
        randomAccessFile.seek(position * REGISTER_LENGTH);
        byte[] blockReaded = new byte[length * REGISTER_LENGTH];
        randomAccessFile.read(blockReaded);
        return blockReaded;
    }//read()
    
    /*[02]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    public static String getID(byte[] block, int position)
    {
        StringBuilder sb = new StringBuilder(POST_ID_STRLENGTH);
        
        int i = position * REGISTER_LENGTH;
        int nextField = i + POST_ID_STRLENGTH * 2;
       
        do
        {
            int msb = block[i];
            int lsb = block[i + 1];
            
            if ((msb + lsb) == 0) break;//caractere nulo
                               
            sb.append( (char)(((msb & 255) << 8) + (lsb & 255)) );
            
            i += 2;
            
        }while(i < nextField);
        
        return sb.toString();
    }//getID()
    
    /*[04]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    public static short getRank(byte[] block, int position)
    {
        int i = (position * REGISTER_LENGTH) + (POST_ID_STRLENGTH * 2);
               
        return (short)( ((block[i] & 255) << 8) + (block[i + 1] & 255) );
    }//getRank()
    
    /*[05]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    public void close() throws IOException
    {
        randomAccessFile.close();
    }//close()
    
    /*[06]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    public static void main(String[] args) throws IOException
    {
       Global.initializeDirNames();
       PostRegArray p = new PostRegArray(datDirName + POST_LISTS_FILENAME);
       byte[] block = p.read(0, 1215);
       for (int i = 0; i < 1215; i++)
       {
           System.out.println(PostRegArray.getID(block, i));
           System.out.println(PostRegArray.getRank(block, i));
           System.out.println("");
       }
    }//main()
    
}//PostRegArray
