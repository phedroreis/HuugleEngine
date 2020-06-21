package br.com.hkp.searchengine.registers;

import static br.com.hkp.searchengine.registers.RegistersConstants.*;
import br.com.hkp.searchengine.util.Global;
import static br.com.hkp.searchengine.util.Global.datDirName;
import static br.com.hkp.searchengine.util.Global.topicExtendedFilename;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/*******************************************************************************
 *
 * @author "Pedro Reis"
 * @since 30 de maio de 2020 v1.0
 * @version 1.0
 ******************************************************************************/
public final class TopicExtendedReg extends Register
{
    private String id;//chave primaria
    private String title;
    private short rank;
      
    /*[00]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * O construtor da classe informa ao construtor da superclasse Register o
     * tamanho em bytes de um registro de um arquivo de PostExtendedReg, o 
     * tamanho em bytes de campo {@link #id}, que eh a chave primaria do arquivo
     * e tambem o nome do arquivo em disco. 
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
    public TopicExtendedReg(final String fileName) throws IOException
    {
        super
        (
            TOPIC_ID_STRLENGTH + TITLE_STRLENGTH + 1,
            TOPIC_ID_STRLENGTH, 
            fileName
        );
    }//construtor
    
    /*[01]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Le um registro na posicao "position" para os campos de um objeto 
     * TopicExtendedReg.
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
            id = readString(TOPIC_ID_STRLENGTH);
            title = readString(TITLE_STRLENGTH);
            rank = readShort();
            return true;
        }
        return false;
    }//read()
    
    /*[02]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Copia os campos deste objeto para um registro de um arquivo 
     * 
     * @throws IOException Em caso de erro de IO
     */
    @Override
    public void write() throws IOException
    {
        super.write();//Chamada para metodo da superclasse ajusta ponteiro para 
                      //gravacao sequencial apos posicao do ultimo registro
                      //gravado
        writeString(id, TOPIC_ID_STRLENGTH);
        writeString(title, TITLE_STRLENGTH);
        writeShort(rank);
    }//write()
    
    /*[03]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Setter da id de um topico. Tambem chave primaria para um TopicExtendedReg
     * 
     * @param id A id de um topico que deve ser passada no formato topic=XXXXX
     */
    public void setID(final String id)
    {
        this.id = id.substring(6, id.length());
    }//setID()
    
    /*[04]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Setter do titulo de um topico.
     * 
     * @param title O titulo do topico
     */
    public void setTitle(final String title)
    {
        this.title = title;
    }//setTitle()
    
    /*[05]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Armazena um short classificando o topico por seu rankeamento em funcao
     * da relevancia de se retornar um post deste topico como resultado de uma 
     * pesquisa.Topicos com mais visualizacoes sao mais bem ranqueados.
     * 
     * @param views O numero de visualizacoes do topico eh usado para
     * calcular seu rank
     * 
     * @param date A data de publicacao do post no formato AAMMDD
     */
    private static final transient LocalDateTime LAST_DAY = 
        LocalDateTime.of(2020, 2, 1, 0, 0, 0);
    
    private static final transient double LN_2 = Math.log(2f);
    
    public void setRank(final String views, final String date)
    {
        LocalDateTime publicationDay = 
            LocalDateTime.of
            (
                Integer.valueOf(date.substring(0, 2)) + 2000,
                Integer.valueOf(date.substring(2, 4)),
                Integer.valueOf(date.substring(4, 6)),
                0,
                0,
                0
            );
        
        int days = (int)publicationDay.until(LAST_DAY, ChronoUnit.DAYS);
        
        if (days == 0) days = 1;
            
        double floatRank = Math.log(Double.valueOf(views)/days)/LN_2;
        
        if (rank < 0)
            this.rank = (short)(floatRank - 0.5);
        else
            this.rank = (short)(floatRank + 0.5);
        
    }//setRank()
    
    /*[06]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Este metodo prove uma forma de copiar os campos de um objeto 
     * TopicExtendedReg para outro objeto TopicExtendedReg
     * <p>
     * O campo rank eh calculado como uma funcao da media de visualizacoes 
     * diarias. Esse metodo prove uma forma do rank de campo rank de um objeto
     * dessa classe ser atribuido a outro objeto dessa mesma classe. Uma vez 
     * para a copia dos campos de um objeto para outro, dados sobre a media de 
     * visualizacoes do topico nao estarao disponiveis
     * 
     * @param rank O rank a ser atribuido a um TopicExtendedReg
     */
    public void setRank(int rank)
    {
        this.rank = (short)rank;
    }//setRank()
        
    /*[06]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Getter da ID do topico no formato topic=XXXXXX
     * 
     * @return A ID do topico
     */
    public String getID()
    {
        return "topic=" + id;
    }//getID()
    
    /*[07]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Getter do titulo do topico
     * 
     * @return O titulo do topico
     */
    public String getTitle()
    {
        return title;
    }//getTitle()
    
    /*[08]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Getter do ranking do topico
     * 
     * @return O ranking do topico
     */
    public int getRank()
    {
        return rank;
    }//getRank()
   
    /*[09]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Retorna a chave primaria do objeto sem o prefixo topic=
     * 
     * @return A chave primaria do objeto sem o prefixo topic=
     */
    @Override
    public String getKey()
    {
       return id; 
    }//getKey()
    
    /*[10]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    private static final String P1 = "%-" + (TOPIC_ID_STRLENGTH + 6) + "s : ";
    private static final String P2 = "%-" + TITLE_STRLENGTH  + "s | ";
    private static final String P3 = "%3d";
       
    private static final String FORMAT = P1 + P2 + P3;
    /**
     * Retorna uma representacao textual do objeto
     * 
     * @return Uma representacao textual do objeto
     */
    @Override
    public String toString()
    {
        return  String.format(FORMAT, getID(), getTitle(), getRank());
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
            TopicExtendedReg t = 
                new TopicExtendedReg(datDirName + topicExtendedFilename);
            t.openToRead();
            if (t.read("10")) System.out.println(t);
            if (t.read("10020")) System.out.println(t);
            if (t.read("1014")) System.out.println(t);
            if (t.read("9997")) System.out.println(t);
            if (t.read("9982")) System.out.println(t);
            if (t.read("28586")) System.out.println(t);
            t.close();
        }
        catch (IOException ex)
        {
            System.out.println(ex);
        }
    }//main()
    
  
}//classe TopicExtendedReg
