package br.com.hkp.searchengine.registers;

/*******************************************************************************
 *
 * @author "Pedro Reis"
 ******************************************************************************/
public class RegistersConstants
{
    /**
     * Num. max. de caracteres na ID de um post, excluindo-se o prefixo msg
     */
    public static final short POST_ID_STRLENGTH = 7;
    /**
     * Num. max. de caracteres na ID de um user excluindo os prefixos u= ou v=
     * 
     * TODO AJUSTAR A SUBCLASSE PARA ESTA MODIFICACAO, NO PROJETO ORIGINAL 
     * OS PREFIXOS ERAM INCLUIDOS NA USER ID
     */
    public static final short USER_ID_STRLENGTH = 23;
    /**
     * Num. de caracteres em um registro de data com formato AAMMDD
     */
    public static final short DATE_STRLENGTH = 6;
    /**
     * Num. max. de caracteres em um nome de arquivo de topico, excluindo-se
     * o prefixo topic= e o sufixo .html#msgXXXXXX ou .html apenas.
     * Armazena apenas o numero do topico e da pagina separados por um ponto,
     * no formato XXXXX.YYYYY
     */
    public static final short FILE_STRLENGTH = 11;
    /**
     * Num. max. de caracteres na ID de um topico excluindo-se o prefixo topic=
     */
    public static final short TOPIC_ID_STRLENGTH = 5;
    /**
     * Num. max. de caracteres que pode ter o titulo de um topico
     */
    public static final short TITLE_STRLENGTH = 151;
    /**
     * Num. max. de caracteres que pode ter o nick de um user
     */
    public static final short NICK_STRLENGTH = 33;
    /**
    * Num. max. de caracteres que pode ter uma palavra catalogada no INDEX
    */
    public static final int WORD_STRLENGTH = 16;
    
}//classe RegistersConstants
