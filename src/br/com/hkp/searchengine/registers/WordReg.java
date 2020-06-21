package br.com.hkp.searchengine.registers;

import static 
    br.com.hkp.searchengine.registers.RegistersConstants.WORD_STRLENGTH;
import java.io.IOException;


/*******************************************************************************
 * Cada palavra catalagoda das postagens nos arquivos de topicos do CC serah 
 * relacionada a uma lista de posts onde ha ocorrencia desta palavra.
 * <p>
 * Esta lista nao abrangera, necessariamente, todos os posts onde a palavra 
 * ocorre, mas possivelmente um subconjuntos destes selecionados de acordo com
 * algum criterio de rankeamento.
 *
 * 
 * @author "Pedro Reis"
 * @since 6 de junho de 2020 v1.0
 * @version 1.0
 ******************************************************************************/
public class WordReg extends Register
{
    
    private String word;//chave primaria
    private int firstPost;
    private int lastPost;
      
    /*[00]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * 
     * @param fileName O nome do arquivo de objetos WordReg
     * 
     * @throws IOException Em caso de erro de IO
     */
    public WordReg(final String fileName) throws IOException
    {
        super
        (
            WORD_STRLENGTH + 4,
            WORD_STRLENGTH, 
            fileName
        );
    }//construtor
   
    /*[01]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Le um registro na posicao "position" para os campos de um objeto 
     * PostExtendedReg.
     * <p>
     * A leitura eh feita no modo sequencial quando "position" eh passado com 
     * valor -1. E retornarah true se a leitura foi feita ou false se "position"
     * for um indice maior que o do ultimo registro do arquivo.
     * 
     * @param position A posicao do registro a ser lido ou -1 para ler 
     * o registro na posicao que suceda a posicao do ultimo registro lido. Ou
     * seja, sequencialmente.
     * 
     * @return true se o registro foi lido ou false se se "position"
     * for um indice maior que o do ultimo registro do arquivo.
     * 
     * @throws IOException Em caso de erro de IO
     */
    @Override
    public boolean read(final int position) throws IOException
    {
        /*
        A chamada para a versao desse metodo na superclasse verifica se a 
        leitura em "position" eh valida e, nesse caso, ajusta ponteiro
        de leitura para a posicao "position". Entao o metodo copia os dados no
        registro do arquivo para o campos desse objeto.
        
        Se "position" nao for uma posicao valida o metodo retorna false.
        */
        if (super.read(position))
        {
            word = readString(WORD_STRLENGTH);
            firstPost = readInt();
            lastPost = readInt();
            return true;
        }
        return false;
    }//read()
     
    /*[02]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Copia os campos deste objeto para um registro de um arquivo. A operacao
     * de gravacao para este tipo de arquivo deve ser sequencial.
     * <p>
     * As classes que estendem Register nao implementam operacao de gravacao 
     * aleatoria em arquivos. Apenas leitura aleatoria eh possivel.
     * 
     * @throws IOException Em caso de erro de IO
     */
    @Override
    public void write() throws IOException
    {
        super.write();//Chamada para metodo da superclasse ajusta ponteiro para 
                      //gravacao sequencial apos posicao do ultimo registro
                      //gravado
        writeString(word, WORD_STRLENGTH);
        writeInt(firstPost);
        writeInt(lastPost);
    }//write()
    
    /*[03]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Setter da word. 
     * 
     * @param word A word
     */
    public void setWord(final String word)
    {
        this.word = word;
    }//setID()
    
    /*[04]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Setter do ponteiro que aponta para o primeiro registro de post no arquivo
     * com as listas de posts relativos a cada palavra catalogada
     * 
     * @param firstPost Indice do primeiro post na lista de posts onde ocorre
     * a palavra
     */
    public void setFirstPost(final int firstPost)
    {
        this.firstPost = firstPost;
    }//setRank()
    
    /*[04]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
     /**
     * Setter do ponteiro que aponta para o ultimo registro de post no arquivo
     * com as listas de posts relativos a cada palavra catalogada
     * 
     * @param lastPost Indice do ultimo post na lista de posts onde ocorre
     * a palavra
     */
    public void setLastPost(final int lastPost)
    {
        this.lastPost = lastPost;
    }//setRank()
    
    /*[05]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * A palavra catalogada em textos de posts ou titulos de topicos
     * 
     * @return A palavra
     */
    public String getWord()
    {
        return word;
    }//getWord()
    
    /*[06]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
     /**
     * Getter do ponteiro que aponta para o primeiro registro de post no arquivo
     * com as listas de posts relativos a cada palavra catalogada
     * 
     * @return Indice do primeiro post na lista de posts onde ocorre
     * a palavra
     */
    public int getFirstPost()
    {
        return firstPost;
    }//getFirstPost()
    
    /*[07]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
     /**
     * Getter do ponteiro que aponta para o ultimo registro de post no arquivo
     * com as listas de posts relativos a cada palavra catalogada
     * 
     * @return Indice do ultimo post na lista de posts onde ocorre
     * a palavra
     */
    public int getLastPost()
    {
        return lastPost;
    }//getLastPost()
    
    /*[08]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
     /**
     * Getter do tamanho da lista de ponteiros
     * 
     * @return Quantos posts ha na sublista de posts nos quais a palavra ocorre
     */
    public int getListSize()
    {
        return (lastPost - firstPost + 1);
    }//getListSize()
    
    /*[09]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Retorna a palavra do indice
     * 
     * @return A palavra
     */
    @Override
    public String getKey()
    {
       return word; 
    }//getKey()
    
    /*[10]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    private static final String P1 = "%-" + (WORD_STRLENGTH + 3) + "s : ";
        
    private static final String FORMAT = P1 + "%8d | %8d | %6d";
    /**
     * Retorna uma representacao textual do objeto
     * 
     * @return Uma representacao textual do objeto
     */
    @Override
    public String toString()
    {
        return  String.format
                (
                    FORMAT, 
                    getWord(), 
                    getFirstPost(), 
                    getLastPost(), 
                    getListSize()
                );
    }//toString()
    
    public static void main(String[] args)
    {
        
    }
    
}//WordReg

