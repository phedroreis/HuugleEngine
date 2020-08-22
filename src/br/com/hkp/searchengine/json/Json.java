package br.com.hkp.searchengine.json;

/*******************************************************************************
 * A funcao desta classe eh ser superclasse para subclasses correspondentes aos
 * tipos de arquivos json que serao processados pelo programa para gerar os 
 * arquivos de sua base de dados.
 * <p>
 * Os arquivos HTML editados que se constituem no clone do forum CC foram 
 * processados previamente e disto foram gerados diversos arquivos no formato
 * json contendo registros sobre todos os dados coletados sobre usuarios do 
 * forum, posts, topicos, etc... O programa irah utilizar os dados constantes 
 * nos arquivos listando registros sobre topicos, postagens e o dos registros
 * de usuarios
 * <p>
 * Para cada um destes 3 arquivos ha uma subclasse de Json correspondente, que 
 * serah utilizada por um objeto JsonReader quando da leitura e processamento
 * destes arquivos
 * 
 * @author "Pedro Reis"
 * @since 31 de maio de 2020 v1.0
 * @version 1.0
 ******************************************************************************/
public class Json
{
    private final String[] keys;
    private final String[] values;
    private String ident;// os espacos de identacao que serao impressos por 
                         //toString()
    private final int lastKeyIndex;
    
    /*[00]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * O construtor eh inicializado por um objeto de uma subclasse, com um array
     * contendo os nomes das chaves de todos os campos de um determinado arquivo
     * json. A ordem destas chaves no array deve corresponder a ordem em que 
     * estas chaves sao declaradas nos registros do arquivo json.
     * 
     * @param k Um array com os nomes das chaves de um registro de arquivo json
     */
    @SuppressWarnings("OverridableMethodCallInConstructor")
    protected Json(final String[] k)
    {
        keys = k;
        lastKeyIndex = (keys.length - 1);
        values = new String[keys.length];
        setIdent(0);
    }//construtor
    
    /*[01]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Retorna o num. de chaves de um objeto Json
     * 
     * @return O num. de chaves ou campos
     */
    protected final int getNumberOfKeys()
    {
        return keys.length;
    }//getNumberOfKeys()
    
    /*[02]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Atribui um valor a uma chave de indice (posicao) index
     * 
     * @param index A posicao ordinal da chave no arquivo json que corresponde 
     * a classe Json criada para o processamento deste tipo de arquivo
     * 
     * @param value O valor a ser atribuido a chave
     */
    public final void setValue(final int index, final String value)
    {
        values[index] = value;
    }//setValue()
       
    /*[03]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Obtem o valor da chave de indice index
     * 
     * @param index A posicao ordinal do valor a ser obtido
     * 
     * @return O valor referente a chave na posicao ordinal index
     */
    public final String getValue(final int index)
    {
        return values[index];
    }//getValue()
   
    /*[04]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
     /**
     * Ajusta o numero de espacos de identacao para o metodo toString()
     * 
     * @param n Quantas tabulacoes para cada identacao. Cada tabulacao equivale
     * a 4 espacos. toString() tabula com espacos.
     */
    public void setIdent(final int n)
    {
        ident = "";
        for (int i = 1; i <= n; i++) ident += ident + "    ";
    }//setIdent()
    
    /*[05]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * A representacao textual do objeto no padrao de um registro de arquivo 
     * tipo json
     * 
     * @return Uma representacao textual do objeto
     */
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder(512);
        sb.append(ident).append("{\n");
        String plusIdent = ident + "    ";
        for (int i = 0; i <= lastKeyIndex; i++)
        {
            sb.append(plusIdent).append('"').append(keys[i]).append("\": \"").
               append(values[i]).append("\"");
            
            if (i == lastKeyIndex)
                sb.append('\n').append(ident).append('}');
            else
                sb.append(",\n");
        }
        return sb.toString();
    }//toString()
    
}//classe Json
