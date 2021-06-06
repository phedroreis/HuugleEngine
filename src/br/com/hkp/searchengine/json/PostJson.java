package br.com.hkp.searchengine.json;

/*******************************************************************************
 * Um objeto PostJson pode ser utilizado por um objeto da classe JsonReader para
 * processar o arquivo posts.json contendo os registros de aproximadamente 
 * um milhao de posts catalogados do acervo HTML do forum Clube Cetico
 * 
 * @author "Pedro Reis"
 * @since 31 de maio de 2020 v1.0
 * @version 1.0
 ******************************************************************************/
public class PostJson extends Json 
{
    /*
    Estas chaves correspondem as chaves do arquivo posts.json e sao declaradas
    neste array na mesma ordem em que estao dispostas nos registros do arquivo
    */
    private static final String[] KEYS = 
    {
        "id", "topicID", "authorID", "date", "modified", "index", "post"
    };
    
    public static final int POST_ID_INDEX = 0;
    public static final int POST_TOPICID_INDEX = 1;
    public static final int POST_AUTHORID_INDEX = 2;
    public static final int POST_DATE_INDEX = 3;
    public static final int POST_MODIFIED_INDEX = 4;
    public static final int POST_INDEX_INDEX = 5;
    public static final int POST_POST_INDEX = 6;
  
    
    /*[00]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Construtor
     */
    public PostJson()
    {
        super(KEYS);
    }//construtor PostJson()
       
}//classe PostJson
