package br.com.hkp.searchengine.builddatabase;

import br.com.hkp.searchengine.registers.PostReg;
import br.com.hkp.searchengine.registers.WordReg;
import br.com.hkp.searchengine.util.Global;
import static br.com.hkp.searchengine.util.Global.datDirName;
import static br.com.hkp.searchengine.util.Global.POST_LISTS_FILENAME;
import static br.com.hkp.searchengine.util.Global.WORDS_FILENAME;
import java.io.IOException;

/*******************************************************************************
 * Aplicacao para mesclar os arquivos de indices de palavras em um unico arquivo
 * 
 * @author "Pedro Reis"
 * @since 8 de junho de 2020 v1.0
 * @version 1.0
 ******************************************************************************/
public class Merge
{
    private final String infinite = "\uffff\uffff\uffff\uffff";
    
    private final int numberOfFiles = 10;
    
    private final WordReg[] wordsFilesArray;
    
    private final PostReg[] postRegsFilesArray;
    
    private final WordReg mergedWordsFile;
    
    private final PostReg mergedPostsFile;
    
    private static BuildLog buildLog;//constroi arquivo de log
        
    /*[00]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    private Merge() throws IOException
    {
        wordsFilesArray = new WordReg[numberOfFiles];
        postRegsFilesArray = new PostReg[numberOfFiles];
               
        for (int i = 0; i < numberOfFiles; i++)
        {
            wordsFilesArray[i] = 
                new WordReg(datDirName + "/words" + i + ".dat");
            
            postRegsFilesArray[i] = 
                new PostReg(datDirName + "/post_lists" + i + ".dat");
           
        }//for
        
        mergedWordsFile = new WordReg(datDirName + WORDS_FILENAME); 
        
        mergedPostsFile = new PostReg(datDirName + POST_LISTS_FILENAME); 
               
    }//construtor 
    
    /*[01]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    private void openFiles() throws IOException
    {
        for (int i = 0; i < numberOfFiles; i++)
        {
            wordsFilesArray[i].openToRead();
            postRegsFilesArray[i].openToRead();
            
            wordsFilesArray[i].read(-1);
        }//for i
        
        mergedWordsFile.openToWrite();
        mergedPostsFile.openToWrite();
    }//openFiles()
    
    /*[02]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    private void closeFiles() throws IOException
    {
        for (int i = 0; i < numberOfFiles; i++)
        {
            wordsFilesArray[i].close();
            postRegsFilesArray[i].close();
        }//for
        
        mergedWordsFile.close();
        mergedPostsFile.close();
    }//closeFiles()
    
    /*[03]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    private int smallerWordIndex()
    {
        String smaller = wordsFilesArray[0].getWord();
        int index = 0;
        for (int i = 1; i < numberOfFiles; i++)
        {
            String word = wordsFilesArray[i].getWord();
            if (word.compareTo(smaller) < 0) 
            {
                smaller = word;
                index = i;
            }
        }
        return index;
    }//smallerWordIndex()
    
    /*[04]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    private static final String SEPARATOR_TOP = 
        "-------------------------------+----------+--------¬";
    private static final String SEPARATOR_BOTTOM = 
        "-------------------------------+----------+---------";
    
    private void merge() throws IOException
    {
        int i = smallerWordIndex();
        int firstPostIndex = 0;
        int listSize = 0;
        int countMergedWords = 0;
        
        String word = wordsFilesArray[i].getWord();
        
        while (!word.equals(infinite))
        {
            int first = wordsFilesArray[i].getFirstPost();
            int last = wordsFilesArray[i].getLastPost();
            listSize += wordsFilesArray[i].getListSize();
            
            for (int n = first; n <= last; n++)
            {
                postRegsFilesArray[i].read(n);
                mergedPostsFile.setID(postRegsFilesArray[i].getID());
                if (postRegsFilesArray[i].presentOnTitle() == 1)
                    mergedPostsFile.setRank(-postRegsFilesArray[i].getRank());
                else
                    mergedPostsFile.setRank(postRegsFilesArray[i].getRank()); 
                mergedPostsFile.write();
            }//for n
            
            buildLog.writeln(wordsFilesArray[i].toString());
            System.out.println(wordsFilesArray[i].toString());
               
            if (!wordsFilesArray[i].read(-1))
            {    
                wordsFilesArray[i].setWord(infinite); 
            }
              
            i = smallerWordIndex();
            
            if (!(wordsFilesArray[i].getWord()).equals(word))
            {
                mergedWordsFile.setWord(word);
                mergedWordsFile.setFirstPost(firstPostIndex);
                mergedWordsFile.setLastPost(firstPostIndex + listSize - 1);
                mergedWordsFile.write();
                
                buildLog.writeln(SEPARATOR_TOP);
   
                buildLog.writeln
                (
                    mergedWordsFile.toString() + 
                    " | <|-- [" + 
                    ++countMergedWords + 
                    "]"
                );
                
                buildLog.writeln(SEPARATOR_BOTTOM);
              
                System.out.println(SEPARATOR_TOP);
                
                System.out.println(mergedWordsFile.toString() + " | <|--");
                
                System.out.println(SEPARATOR_BOTTOM);
              
                firstPostIndex = mergedWordsFile.getLastPost() + 1;
                listSize = 0;
            }//if
            
            word = wordsFilesArray[i].getWord();
            
        }//while
        
    }//merge()
    
    /*[05]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Mescla arquivos de indices de palavras e seus respectivos arquivos de 
     * sublistas de posts em um unico arquivo de indice de palavras associado
     * a um unico arquivo de sublistas de posts
     * 
     * @param args n/a
     */
    @SuppressWarnings("CallToPrintStackTrace")
    public static void main(String[] args)
    {
        try
        {
            Global.initializeDirNames();
            
            buildLog = new BuildLog("/merge.log", 2048000);
            
            Merge m = new Merge();
            m.openFiles();
            m.merge();
            m.closeFiles();
            
            buildLog.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
       
    }//main()
    
}//Merge
