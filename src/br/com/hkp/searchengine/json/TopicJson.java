package br.com.hkp.searchengine.json;

/******************************************************************************
 * Um objeto TopicJson pode ser utilizado por um objeto da classe JsonReader pra
 * processar o arquivo topics.json contendo os registros de 25010 topicos 
 * catalogados do acervo HTML do forum Clube Cetico
 * 
 * @author "Pedro Reis"
 * @since 31 de maio de 2020 v1.0
 * @version 1.0
 ******************************************************************************/
public class TopicJson extends Json 
{
     /*
    Estas chaves correspondem as chaves do arquivo topics.json e sao declaradas
    neste array na mesma ordem em que estao dispostas nos registros do arquivo
    */
    private static final String[] KEYS = 
    {
        "id", "title", "authorID", "sectionID", "subSectionID", "postID",
        "date", "views", "pages", "pollID"
    };
    
    public static final int TOPIC_ID_INDEX = 0;
    public static final int TOPIC_TITLE_INDEX = 1;
    public static final int TOPIC_AUTHORID_INDEX = 2;
    public static final int TOPIC_SECTIONID_INDEX = 3;
    public static final int TOPIC_SUBSECTIONID_INDEX = 4;
    public static final int TOPIC_POSTID_INDEX = 5;
    public static final int TOPIC_DATE_INDEX = 6;
    public static final int TOPIC_VIEWS_INDEX = 7;
    public static final int TOPIC_PAGES_INDEX = 8;
    public static final int TOPIC_POLLID_INDEX = 9;
    
    /*[00]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Construtor
     */
    public TopicJson()
    {
        super(KEYS);
    }//construtor TopicJson()
   
}//classe TopicJson
