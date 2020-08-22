package br.com.hkp.searchengine.json;

import static br.com.hkp.searchengine.util.Global.jsonsDirName;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;


/*******************************************************************************
 * Esta classe fornece os meios para ler sequencialmente um arquivo tipo json,
 * atribuindo seus campos a um objeto Json correspondente. 
 * <p>
 * Classes tipo Json, declaradas neste pacote, estendem a classe Json e ha uma
 * classe que estende Json para cada tipo de arquivo json que serah processado
 * pelo programa para criar os arquivos da base de dados necessarios ao processo
 * de busca no acervo do fórum CC.
 * 
 * @author "Pedro Reis"
 * @since 31 de maio de 2020 v1.0
 * @version 1.0
 * 
 * @param <T extends Json> Qualquer subclasse de Json
 ******************************************************************************/
public final class JsonReader<T extends Json> 
{
    private BufferedReader jsonFile;
    private final int buffer;
    private final File file;
    private int countRead;
    private final int numberOfKeys;
    private final T json;
    
      
    /*[00]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Construtor
     * 
     * @param js O objeto json que retornarah os valores lidos no arquivo pelo
     * metodo {@link #read() }
     * 
     * @param fileName O nome do arquivo iniciado com separador de arquivo /
     * porem sem o caminho. O diretorio onde estao os arquivos json serah
     * inserido por este construtor
     * 
     * @param buffer O buffer de leitura
     */
    public JsonReader(final T js, final String fileName, final int buffer)
    {
        json = js;
        file = new File(jsonsDirName + fileName);
        this.buffer = buffer;
        numberOfKeys = json.getNumberOfKeys(); 
    }//construtor JsonReader()
    
    /*[01]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Retorna o num. de registros do arquivo que jah foram lidos ateh o momento
     * da chamada deste metodo
     * 
     * @return O num. de registros jah lidos.
     */
    public int getCountRead()
    {
        return countRead;
    }//getCountRead()
    
    /*[02]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Abre o arquivo json para leitura apenas
     * 
     * 
     * @throws IOException Em caso de erro de IO
     */
    public void open() throws IOException
    {
              
        jsonFile = new BufferedReader
                   (
                       new FileReader(file, StandardCharsets.UTF_8), buffer
                   );
        
        jsonFile.readLine();//consome { de abertura de arquivo json
        countRead = 0;
       
    }//open()
       
    /*[03]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Fecha o arquivo
     * 
     * @throws IOException Em caso de erro de IO
     */
    public void close() throws IOException
    {
        if (jsonFile != null)
        {
            jsonFile.close();
            jsonFile = null;
        }
    }//close()
    
    /*[04]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Le um registro do tipo Json em um arquivo json.
     * 
     * @return Um objeto do tipo Json ou null se nao ha mais registros no 
     * arquivo
     * 
     * @throws IOException Em caso de erro de IO
     */
    public T read() throws IOException
    {
        String line = jsonFile.readLine();//Le {, se leu } eh o fim do arquivo
        if (line.trim().equals("}")) return null;
        
        /*
        Le cada linha de um registro em um arquivo json, extrai o dado e o 
        atribui ao campo apropriado de um objeto tipo Json
        */
        for (int i = 0; i < numberOfKeys; i++)
        {
            line = jsonFile.readLine(); 
            json.setValue
            (
                i,
                line.substring(line.indexOf(':') + 2, line.lastIndexOf('"'))
            );
        }//for - leu um campo de um registro em um arquivo json
     
        jsonFile.readLine();//consome }
        
        countRead++;//Leu mais um registro em um arquivo json
        
        return json;
    }//read()
       
}//classe JsonReader
