package br.com.hkp.searchengine.registers;

import static br.com.hkp.searchengine.registers.RegistersConstants.*;
import br.com.hkp.searchengine.util.Global;
import static br.com.hkp.searchengine.util.Global.datDirName;
import static br.com.hkp.searchengine.util.Global.postExtendedFilename;
import java.io.IOException;

/*******************************************************************************
 * Esta subclasse de Register fornece os meios para manipular arquivos cujos 
 * registros sejam dados sobre posts publicados no forum.
 * <p>
 * Um objeto PostExtendedReg pode abrir, fechar, ler, gravar e pesquisar em um
 * arquivo cujos registros sejam a ID do post (chave primaria), a ID do autor,
 * a data de publicacao no formato AAMMDD e o nome do arquivo onde foi publicado
 * o post.
 * 
 * @author "Pedro Reis"
 * @since 30 de maio de 2020 v1.0
 * @version 1.0
 ******************************************************************************/
public class PostExtendedReg extends Register
{
    private String id; //chave primaria
    private String authorID;
    private String date;
    private String file;
     
    /*[00]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * O construtor da classe informa ao construtor da superclasse Register o
     * tamanho em bytes de um registro de um arquivo de PostExtendedReg dividido
     * por 2, o tamanho em bytes do campo {@link #id}, que eh a chave primaria
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
    public PostExtendedReg(final String fileName) throws IOException
    {
        super
        (
            POST_ID_STRLENGTH + USER_ID_STRLENGTH + 
            DATE_STRLENGTH + FILE_STRLENGTH,
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
            authorID = readString(USER_ID_STRLENGTH);
            date = readString(DATE_STRLENGTH);
            file = readString(FILE_STRLENGTH);
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
        writeString(authorID, USER_ID_STRLENGTH);
        writeString(date, DATE_STRLENGTH);
        writeString(file, FILE_STRLENGTH);
    }//write()
    
    /*[03]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Setter da id de um post. Tambem chave primaria para um PostExtendedReg
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
     * Setter da ID do user que publicou o post. 
     * 
     * @param authorID Deve ser passado no formato u=XXXX ou v=nickname
     */
    public void setAuthorID(final String authorID)
    {
        this.authorID = authorID.substring(2, authorID.length());
    }//setAuthorID()
    
    /*[05]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Setter da data de publicacao do post no formato AAMMDD
     * 
     * @param date Data no formato AAMMDD
     */
    public void setDate(final String date)
    {
        this.date = date;
    }//setDate()
    
    /*[06]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Setter do nome do arquivo onde foi publicado o post. Deve ser passado
     * no formato topic=XXXXX.YYYY.html ou topic=XXXXX.YYYY.html#msgZZZZZZZ
     * 
     * @param file O nome do arquivo onde esta publicado o post.
     */
    public void setFile(final String file)
    {
        this.file = file.substring(6, file.lastIndexOf('.'));
    }//setFile()
    
    /*[07]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Getter da ID do post retornada no formato msgXXXXXX
     * 
     * @return A ID do post
     */
    public String getID()
    {
        return "msg" + id;
    }//getID()
    
    /*[08]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Getter da ID do user que publicou o post no fomato u=XXXX de usuarios 
     * inscritos ou v=nickname que eh o formato de ID de usuarios desinscritos e 
     * visitantes
     * 
     * @return A userID do autor do post
     */
    public String getAuthorID()
    {
        if (authorID.matches("\\d+"))
            return "u=" + authorID;
        else
            return "v=" + authorID;
    }//getAuthorID()
    
    /*[09]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Getter da data de publicacao do post como uma String no formato AAMMDD
     * 
     * @return Uma String no formato AAMMDD
     */
    public String getDate()
    {
        return date;
    }//getDate()
    
    /*[10]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Retorna o nome do arquivo onde foi publicado o post acrescido do sufixo
     * com #msgXXXXX com a ID do post. O sufixo faz o navegador abrir o arquivo 
     * jah posicionado no proprio post.
     * 
     * @return O nome do arquivo onde foi publicado o post
     */
    public String getFile()
    {
        return "topic=" + file + ".html#" + getID();
    }//getFile()
     
    /*[11]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Retorna a ID do topico onde esta publicado o post no formato topic=XXXXX
     * 
     * @return A ID do topico onde se encontra o post.
     */
    public String getTopicID()
    {
        return  "topic=" + file.substring(0, file.indexOf('.'));
    }//getTopicID()
    
    /*[12]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Retorna a chave primaria do objeto sem o prefixo msg
     * 
     * @return A chave primaria do objeto sem o prefixo msg
     */
    @Override
    public String getKey()
    {
       return id; 
    }//getKey()
    
    /*[13]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    private static final String P1 = "%-" + (POST_ID_STRLENGTH + 3) + "s : ";
    private static final String P2 = "%-" + (USER_ID_STRLENGTH + 2) + "s | ";
    private static final String P3 = "%-" + (TOPIC_ID_STRLENGTH + 6) + "s | ";
    private static final String P4 = "%-" + DATE_STRLENGTH + "s | ";
    private static final String P5 = "%-" + (FILE_STRLENGTH + 22) + "s";
    
    private static final String FORMAT = P1 + P2 + P3 + P4 + P5;
    
    /**
     * Retorna uma representacao textual do objeto
     * 
     * @return Uma representacao textual do objeto
     */
    @Override
    public String toString()
    {
        return String.format
        (
            FORMAT, 
            getID(), 
            getAuthorID(), 
            getTopicID(), 
            getDate(),
            getFile()
        );
    }//toString()
    
    /*[14]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Exemplo de uso
     * 
     * @param args n/a
     */
    public static void main(String[] args)
    {
        try
        {
            Global.initializer();
            PostExtendedReg p = 
                new PostExtendedReg(datDirName + postExtendedFilename);
            p.openToRead();
            if (p.read("34")) System.out.println(p);
            if (p.read("10000")) System.out.println(p);
            if (p.read("4")) System.out.println(p);
            p.close();
        }
        catch (IOException ex)
        {
            System.out.println(ex);
        }
    }//main()
    
   
}//classe PostExtendedReg
