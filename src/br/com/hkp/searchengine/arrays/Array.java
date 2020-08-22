package br.com.hkp.searchengine.arrays;

import static br.com.hkp.searchengine.util.Global.datDirName;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/*******************************************************************************
 *
 * @author "Pedro Reis"
 ******************************************************************************/
public abstract class Array
{
    private final byte[] theArray;
    
    private final int recordLength;
          
    protected int baseAdress;
    
    private final int fileLength;//O tamanho do array em registros
    
       
    /*[00]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    protected Array
    (
        final String fileName,
        final int recordLength
    ) 
        throws IOException
    {
        this.recordLength = recordLength * 2;
        baseAdress = 0;
        
        File inputFile = new File(datDirName + fileName);
                    
        theArray = new byte[(int)inputFile.length()];
        
        fileLength = theArray.length / this.recordLength;
        
        FileInputStream f = new FileInputStream(inputFile);
        f.read(theArray);
        f.close();
       
    }//construtor
    
    /*[01]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    protected final String getString(final int offset, final int fieldLength)
    {
        StringBuilder sb = new StringBuilder(fieldLength);
        
        int i = baseAdress + (offset * 2);
        int nextField = i + (fieldLength * 2);
       
        do
        {
            int msb = theArray[i];
            int lsb = theArray[i + 1];
            
            if ((msb + lsb) == 0) break;//caractere nulo
                               
            sb.append( (char)(((msb & 255) << 8) + (lsb & 255)) );
            
            i += 2;
            
        }while(i < nextField);
        
        return sb.toString();
    }//getString()
    
    /*[02]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    protected final short getShort(final int offset)
    {
        int i = baseAdress + (offset * 2);
               
        return (short)( ((theArray[i] & 255) << 8) + (theArray[i + 1] & 255) );
    }//getShort()
    
    /*[03]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Deve retornar a chave primaria do registro sem prefixos ou sufixos.
     * 
     * @return A chave primaria excluidos os prefixos ou sufixos se houver
     */
    protected abstract String getKey();
    
    /*[04]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    public final boolean read(final String key)
    {
        int ceil = fileLength - 1;//limite superior (ultimo registro)
        int floor = 0;//limite inferior (primeiro registro)
        
        while(ceil >= floor)
        {
            int position = ((floor + ceil) / 2);//divide em 2 o escopo da busca 
            baseAdress = position * recordLength;
            int result = key.compareTo(getKey());
            if (result == 0) return true;//existe registro com chave=key
            if (result > 0) 
                floor = position + 1;//reduz o escopo a metade de cima 
            else
                ceil = position - 1;//o escopo passa a ser metade inferior 
        }//while
        
        baseAdress = 0;
        return false;//nao existe registro com chave primaria igual a key
        
    }//read()
 
}//Array
