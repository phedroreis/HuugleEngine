package br.com.hkp.searchengine.builddatabase;

import br.com.hkp.searchengine.registers.PostReg;
import br.com.hkp.searchengine.registers.TopicExtendedReg;
import br.com.hkp.searchengine.util.Global;
import static br.com.hkp.searchengine.util.Global.datDirName;
import static br.com.hkp.searchengine.util.Global.postListsFilename;
import static br.com.hkp.searchengine.util.Global.topicExtendedFilename;
import java.io.IOException;

/*******************************************************************************
 * Lista os registros de um arquivo do tipo Register 
 * 
 * @author "Pedro Reis"
 * @since 5 de junho de 2020 v1.0
 * @version 1.0
 ******************************************************************************/
public class ListFile
{
   @SuppressWarnings("CallToPrintStackTrace")
    public static void main(String[] args)
    {
        try 
        {
            Global.initializeFilenames();
            
            PostReg postReg =
                new PostReg(datDirName + postListsFilename);
            
            postReg.openToRead();
            
            System.out.println(postReg.getFileLength());
            System.out.println(postReg.getRecordLength());
            
            int count = 0;
            while(postReg.read(-1))
            {
                System.out.println(postReg); 
                if (postReg.presentOnTitle() == 1) break; count++;
                //if (count++ >5000) break; 
            }
            System.out.println(count);
            postReg.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }//main()
    
}//ListFile
