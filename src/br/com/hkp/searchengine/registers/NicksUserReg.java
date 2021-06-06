package br.com.hkp.searchengine.registers;

import static 
    br.com.hkp.searchengine.registers.RegistersConstants.NICK_STRLENGTH;
import static 
    br.com.hkp.searchengine.registers.RegistersConstants.USER_ID_STRLENGTH;
import java.io.IOException;

/*******************************************************************************
 * Esta subclasse de Register fornece os meios para manipular arquivos cujos 
 * registros sejam o nick e a ID de cada usuario do forum. Onde o nick eh chave
 * primaria do arquivo. Eh utilizado para recupear uma ID de user a partir do 
 * seu nick, ou informar que tal nick nao existia entre os cadastrados no forum
 * <p>
 * Um objeto NicksUserReg pode abrir, fechar, ler, gravar e pesquisar em um
 * arquivo cujos registros sejam o nick e a ID de um usuario.
 * 
 * @author "Pedro Reis"
 * @since 6 de maio de 2020 v1.0
 * @version 1.0
 ******************************************************************************/
public class NicksUserReg extends Register
{
    private String nick;//chave primaria
    private String userID;
    
    /*[00]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * O construtor da classe informa ao construtor da superclasse Register o
     * tamanho em bytes de um registro de um arquivo de NicksUserReg dividido
     * por 2, o tamanho em bytes do campo {@link #nick}, que eh a chave primaria
     * do arquivo, e tambem o nome do arquivo em disco. 
     * <p>
     * Todos estes, dados necessarios para que os metodos desta classe e da 
     * superclasse possam abrir, fechar, gravar, ler e pesquisar em arquivos 
     * de Registers. Ou seja, arquivos cujos campos sejam os campos de classes
     * que estendem a classe Register
     * 
     * @param fileName O nome do arquivo de objetos PostExtendedReg
     * 
     * @throws IOException Em caso de erro de IO
     */
    public NicksUserReg(final String fileName) throws IOException
    {
        super
        (
            NICK_STRLENGTH + USER_ID_STRLENGTH, 
            NICK_STRLENGTH, 
            fileName
        );
    }//construtor
    
    /*[01]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Le um registro na posicao "position" para os campos de um objeto 
     * NicksUserReg.
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
            nick = readString(NICK_STRLENGTH);
            userID = readString(USER_ID_STRLENGTH);
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
        writeString(nick, NICK_STRLENGTH);
        writeString(userID, USER_ID_STRLENGTH);
    }//write()
    
    /*[03]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Setter do nick de usuario. Tambem chave primaria para um NicksUserReg
     * 
     * @param nick O nick do usuario
     */
    public void setNick(final String nick)
    {
        this.nick = nick;
    }//setNick()
    
    /*[04]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Setter da ID do usuario de nick "nick"
     * 
     * @param userID Deve ser passado no formato u=XXXX ou v=nickname
     */
    public void setUserID(final String userID)
    {
        this.userID = userID.substring(2, userID.length());
    }//setUserID()
    
    /*[05]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Getter do nick de usuario. Chave primaria de um registro.
     * 
     * @return O nick de usuario.
     */
    public String getNick()
    {
        return nick;
    }//getNick()
    
    /*[06]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Getter da ID do user no fomato u=XXXX de usuarios inscritos ou v=nickname
     * que eh o formato de ID de usuarios desinscritos e visitantes
     * 
     * @return A userID referente ao nick da chave primaria
     */
    public String getUserID()
    {
        if (userID.matches("\\d+"))
            return "u=" + userID;
        else
            return "v=" + userID;
    }//getAuthorID()
    
    /*[07]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Retorna a chave primaria do objeto 
     * 
     * @return A chave primaria do objeto 
     */
    @Override
    public String getKey()
    {
       return nick; 
    }//getKey()
    
    /*[08]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    private static final String P1 = "%-" + NICK_STRLENGTH + "s : ";
    private static final String P2 = "%-" + (USER_ID_STRLENGTH + 2) + "s";
    
    private static final String FORMAT = P1 + P2; 
    
    /**
     * Retorna uma representacao textual do objeto
     * 
     * @return Uma representacao textual do objeto
     */
    @Override
    public String toString()
    {
        return String.format(FORMAT, getNick(), getUserID());
    }//toString()

 
}//classe NicksUserReg
