package br.com.hkp.searchengine.registers;

import static 
    br.com.hkp.searchengine.registers.RegistersConstants.POST_ID_STRLENGTH;
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
public class PostReg extends Register
{
    /*
    PostReg nao tem chave primaria
    */
    private String id;
    private short rank;
      
    /*[00]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * O construtor da classe informa ao construtor da superclasse Register o
     * tamanho em bytes de um registro de um arquivo de PostReg, o 
     * tamanho em bytes de campo {@link #id}, que eh a chave primaria do arquivo
     * e tambem o nome do arquivo em disco. 
     * <p>
     * Todos estes, dados necessarios para que os metodos desta classe e da 
     * superclasse possam abrir, fechar, gravar, ler e pesquisar em arquivos 
     * de Registers. Ou seja, arquivos cujos campos sejam os campos de classes
     * que estendem a classe Register
     * 
     * @param fileName O nome do arquivo de objetos PostReg
     * 
     * @throws IOException Em caso de erro de IO
     */
    public PostReg(final String fileName) throws IOException
    {
        super
        (
            POST_ID_STRLENGTH + 1,
            POST_ID_STRLENGTH, 
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
            id = readString(POST_ID_STRLENGTH);
            rank = readShort();
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
        writeString(id, POST_ID_STRLENGTH);
        writeShort(rank);
    }//write()
    
    /*[03]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Setter da id de um post. 
     * 
     * @param id A id de um post que deve ser passada no formato msgXXXXX
     */
    public void setID(final String id)
    {
        this.id = id.substring(3, id.length());
    }//setID()
    
    /*[04]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Setter do rank de um post. 
     * 
     * @param rank O rank do post
     */
    public void setRank(final int rank)
    {
        this.rank = (short)rank;
    }//setRank()
    
    /*[05]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Retorna a ID do post formatada como msgXXXXX
     * 
     * @return A ID do post formatada
     */
    public String getID()
    {
        return  "msg" + id;
    }//getID()
    
    /*[06]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Um inteiro identificando o rank do post
     * 
     * @return O rank do post.
     */
    public int getRank()
    {
        if (rank < 0) return  (-rank);
        return rank;
    }//getRank()
    
    /*[07]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Retorna 1 se a palavra que aponta para a sublista deste post estiver 
     * presente tambem no titulo do topico onde este post foi publicado.
     * E 0 se nao.
     * 
     * @return 1 se a palavra que aponta para a sublista deste post estiver 
     * presente tambem no titulo do topico onde este post foi publicado.
     * E 0 se nao.
     */
    public int presentOnTitle()
    {
        if (rank < 0) return 1;
        return 0;
    }//presentOnTitle()
    
    /*[08]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Um arquivo de PostReg nao tem chave primaria. Este metodo retorna null
     * 
     * @return null
     */
    @Override
    public String getKey()
    {
       return null; 
    }//getKey()
    
    /*[09]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    private static final String P1 = "%-" + (POST_ID_STRLENGTH + 3) + "s : ";
        
    private static final String FORMAT = P1 + "%5d | %1d";
    /**
     * Retorna uma representacao textual do objeto
     * 
     * @return Uma representacao textual do objeto
     */
    @Override
    public String toString()
    {
        return  String.format(FORMAT, getID(), getRank(), presentOnTitle());
    }//toString()
    
}//classe PostReg
