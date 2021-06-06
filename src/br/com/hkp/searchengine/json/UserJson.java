package br.com.hkp.searchengine.json;

/*******************************************************************************
 * Um objeto UserJson pode ser utilizado por um objeto da classe JsonReader pra
 * processar o arquivo users.json contendo os registros de 1970 usuarios 
 * catalogados do acervo HTML do forum Clube Cetico
 * 
 * @author "Pedro Reis"
 * @since 31 de maio de 2020 v1.0
 * @version 1.0
 ******************************************************************************/
public class UserJson extends Json 
{
    /*
    Estas chaves correspondem as chaves do arquivo users.json e sao declaradas
    neste array na mesma ordem em que estao dispostas nos registros do arquivo
    */
    private static final String[] KEYS = 
    {
        "id", "nick", "level", "membergroup", "postcount", "gender", "avatar",
        "blurb", "signature", "signatureText", "sites", "descriptions", 
        "profile"
    };
    
    public static final int USER_ID_INDEX = 0;
    public static final int USER_NICK_INDEX = 1;
    public static final int USER_LEVEL_INDEX = 2;
    public static final int USER_MEMBERGROUP_INDEX = 3;
    public static final int USER_POSTCOUNT_INDEX = 4;
    public static final int USER_GENDER_INDEX = 5;
    public static final int USER_AVATAR_INDEX = 6;
    public static final int USER_BLURB_INDEX = 7;
    public static final int USER_SIGNATURE_INDEX = 8;
    public static final int USER_SIGNATURETEXT_INDEX = 9;
    public static final int USER_SITES_INDEX = 10;
    public static final int USER_DESCRIPTIONS_INDEX = 11;
    public static final int USER_PROFILE_INDEX = 12;
    
    /*[00]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Construtor
     */
    public UserJson()
    {
        super(KEYS);
    }//construtor UserJson()
   
}//classe UserJson
