package br.com.hkp.searchengine.registers;

import static br.com.hkp.searchengine.util.Global.FINALIZE_LOCK;
import java.io.IOException;
import java.io.RandomAccessFile;

/******************************************************************************
 * Os objetos a serem lidos e gravados em arquivos de acesso direto 
 * implementados devem pertencer a classes que estendam Register.
 * <p>
 * A finalidade de Register eh ser a superclasse de todos os tipos de 
 * objetos que serao gravados como registros em arquivos de acesso direto.
 * 
 * @author "Pedro Reis"
 * @since 28 de maio de 2020 v1.0
 * @version 1.0
 * 
 ******************************************************************************/
public abstract class Register
{
    /*
    O tamanho de um registro em bytes
    */
    private final short recordLength;
    
    /*
    O tamanho do campo que eh a chave primaria do arquivo. Se for zero o arquivo
    nao tem chave primaria
    */
    private final short keyLength;
    
    /*
    O nome do arquivo de acesso aleatorio com o caminho
    */
    private final String fileName;
    
    /*
    O arquivo para acesso aleatorio
    */
    private RandomAccessFile randomAccessFile;
    
    /*
    O indice do ultimo registro do arquivo. 
    */  
    private int ceil;
    
    /*
    A posicao do registro para a proxima operacao de leitura e gravacao 
    no modo sequencial
    */
    private int nextPosition;
      
    /*[00]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Construtor da classe
     * 
     * @param recordLength O tamanho em bytes do registro dividido por 2. No 
     * caso de campos que sejam Strings, deve-se contar o num. de caracteres da
     * String, jah que em java cada char ocupa dois bytes. 
     * <p>
     * O valor a ser passado a este argumento eh o numero de bytes de cada 
     * registro do arquivo dividido por 2. Ou seja, a soma dos tamanhos de todos
     * os campos da classe a serem gravados no arquivo, dividida por 2.
     * 
     * @param keyLength O tamanho em caracteres do campo que eh chave primaria 
     * do arquivo
     * 
     * @param fileName O nome do arquivo com o caminho relativo ou absoluto
     * 
     * @throws IOException Em caso de erro de IO
     */
    public Register
    (
        final int recordLength,
        final int keyLength,
        final String fileName
    )
        throws IOException
    {
        this.recordLength = (short)(2 * recordLength);
        this.keyLength = (short)keyLength;
        this.fileName = fileName;
    }//construtor
    
    /*[01]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Ajusta a leitura sequencial do arquivo para iniciar no registro 0, e nao
     * apos a ultima posicao acessada
     */
    public void resetSequencialReadWrite()
    {
        nextPosition = 0;
    }//resetFile()
    
    /*[02]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Abre o arquivo para leitura e reseta o ponteiro de leitura sequencial
     * 
     * @throws IOException Se arquivo nao encontrado
     */
    public void openToRead() throws IOException
    {
        if (randomAccessFile != null) 
            throw new IOException("Tentativa de abrir arquivo j\u00e1 aberto!");
        randomAccessFile = new RandomAccessFile(fileName, "r");
        ceil = getFileLength() - 1;
        resetSequencialReadWrite();
    }//openToRead()
    
    /*[03]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Abre o arquivo para escrita e reseta o ponteiro de leitura sequencial
     * 
     * @throws IOException Se arquivo nao encontrado
     */
    public void openToWrite() throws IOException
    {
        if (randomAccessFile != null) 
            throw new IOException("Tentativa de abrir j\u00e1 arquivo aberto!");
        randomAccessFile = new RandomAccessFile(fileName, "rw");
        resetSequencialReadWrite();
    }//openToWrite()
    
    /*[04]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Fecha o arquivo
     * 
     * @throws IOException Em caso de erro de IO
     */
    public void close() throws IOException
    {
        if (randomAccessFile != null)
        {
            randomAccessFile.close();
            randomAccessFile = null;
        }
    }//close()
       
    /*[05]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Retorna o tamanho do arquivo nao em bytes, mas em registros.
     * 
     * @return Quantos registros ha no arquivo. 
     * 
     * @throws IOException Em caso de erro de IO
     */ 
    public final int getFileLength() throws IOException
    {
        return (int)(randomAccessFile.length() / recordLength);
    }//getFileLength()
   
    /*[06]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
    * Retorna o tamanho do registro em bytes.
    * 
    * @return O tamanho em bytes do registro.
    */
    public final int getRecordLength()
    {
        return recordLength;
    }//getRecordLength()
    
    /*[07]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
    * Le uma string de tamanho especificado no arquivo. Na posicao corrente do
    * ponteiro de arquivo e avanca o ponteiro para o primeiro byte do arquivo
    * apos a String lida
    * 
    * @param length Quantos caracteres tem o campo da String no registro do
    * arquivo
    * 
    * @return A String lida excluidos os caracteres nulos de preenchimento, caso 
    * existam
    * 
    * @throws IOException Em caso de erro de IO
    * 
    */
    protected final String readString(final int length) throws IOException
    {
        char c[] = new char[length]; 
        
        try
        {
            /*
            Evita que a rotina de encerramento feche o arquivo no meio de uma
            operacao de leitura de arquivo.
            */
            FINALIZE_LOCK.lock();
            for (int i = 0; i < length; i++) c[i] = randomAccessFile.readChar();
        }
        finally
        {
            FINALIZE_LOCK.unlock();
        }
            
        String s = new String(c);
        int end = s.indexOf('\0');
        if (end == -1) return s;
        return  s.substring(0, end);
    }//readString()
    
    /*[08]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
    * Escreve uma string na posicao corrente do ponteiro de gravacao e avanca o
    * ponteiro
    * 
    * @param s A String a ser gravada
    * 
    * @param length Quantos caracteres deve ter o campo da String no arquivo.
    * Se a String, por exemplo, tiver 3 caracteres e length for passado como 
    * 10, as outras 7 posicoes restantes serao preenchidas com caracteres nulos
    * 
    * @throws IOException Em caso de erro de IO
    */
    protected final void writeString(final String s, final int length) 
        throws IOException
    {
        StringBuilder sb = new StringBuilder(s);
        sb.setLength(length);
       
        randomAccessFile.writeChars(sb.toString());
       
    }//writeString()
    
    /*[09]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Le um inteiro de dois bytes com sinal na posicao do ponteiro e avanca o
     * o ponteiro de arquivo
     * 
     * @return O inteiro lido
     * 
     * @throws IOException Em caso de erro de IO
     */
    protected short readShort() throws IOException
    {
        try
        {
            /*
            Evita que a rotina de encerramento feche o arquivo no meio de uma
            operacao de leitura de arquivo.
            */
            FINALIZE_LOCK.lock();
            return randomAccessFile.readShort();
        }
        finally
        {
            FINALIZE_LOCK.unlock();
        }
    }//readShort()
    
    /*[10]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Grava um inteiro de dois bytes com sinal na posicao do ponteiro e avanca
     * o ponteiro
     * 
     * @param value O valor a ser gravado
     * 
     * @throws IOException Em caso de erro de IO
     */
    protected void writeShort(final short value) throws IOException
    {
        randomAccessFile.writeShort(value);
    }//writeShort()
    
    /*[09]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Le um inteiro de 4 bytes
     * 
     * @return O inteiro lido
     * 
     * @throws IOException Em caso de erro de IO
     */
    protected int readInt() throws IOException
    {
        try
        {
            /*
            Evita que a rotina de encerramento feche o arquivo no meio de uma
            operacao de leitura de arquivo.
            */
            FINALIZE_LOCK.lock();
            return randomAccessFile.readInt();
        }
        finally
        {
            FINALIZE_LOCK.unlock();
        }
    }//readInt()
    
    /*[10]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Grava um inteiro de 4 bytes 
     * 
     * @param value O valor a ser gravado
     * 
     * @throws IOException Em caso de erro de IO
     */
    protected void writeInt(final int value) throws IOException
    {
        randomAccessFile.writeInt(value);
    }//writeInt()
    
    /*[11]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /*
    Posiciona o ponteiro de leitura e gravacao do arquivo no registro na posicao
    position. Se position for passado com valor negativo a leitura ou escrita 
    sequencial eh realizada.
    */
    private void seek(int position) throws IOException
    {
        if (position < 0) position = nextPosition;
     
        try
        {
            /*
            Evita que a rotina de encerramento feche o arquivo no meio de uma
            operacao de leitura de arquivo.
            */
            FINALIZE_LOCK.lock();
            randomAccessFile.seek(position * recordLength);
        }
        finally
        {
            FINALIZE_LOCK.unlock();
        }
        
        nextPosition = position + 1;
    }//seek()
    
    /*[12]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /*
    Le a chave primaria do registro na posicao "position". A chave primaria deve 
    ser sempre uma String e ser o primeiro campo do registro.
    */
    private String readKey(final int position) throws IOException
    {
        seek(position);
        return readString(keyLength);
    }//readKey()
   
    /*[13]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /*
    Encontra o indice de um registro com chave key. Se -1 for retornado nao 
    existe registro com chave primaria igual a key. A chave primaria deve ser 
    declarada de forma a ser sempre o primeiro campo de um registro.
    
    O argumento key deve ser passado sem prefixos ou sufixos invariantes, caso
    o tipo de chave tenha prefixos ou sufixos invariantes.
    
    Obs: A rigor o prefixo de ID de usuario nao eh invariante porque pode ser 
    "u=" ou "v=", mas como este prefixo pode ser inferido com base no valor da
    parte variavel da ID, eh considerado invariante e uma key de ID de usuario 
    deve ser passada a este metodo sem estes prefixos
    */
    private int searchFor(final String key) throws IOException
    {
        int c = ceil;//limite superior do escopo da pesquisa (ultimo registro)
        int f = 0;//limite inferior do escopo da pesquisa (primeiro registro)
        
        while(c >= f)//enquanto o escopo da pesquisa no arquivo nao se reduz a 0
        {
            int position = ((f + c) / 2);//divide em 2 o escopo da busca 
            int result = key.compareTo(readKey(position));
            if (result == 0) return position;//existe registro com chave=key
            if (result > 0) 
                f = position + 1;//reduz o escopo a metade de cima do esc. atual
            else
                c = position - 1;//o escopo passa a ser metade inferior do atual
        }//while
        
        return -1;//nao existe registro com chave primaria igual a key
        
    }//searchFor()
    
    /*[14]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Le o registro do arquivo na posicao "position". Se position = -1 le o 
     * registro na posicao corrente e atualiza o ponteiro para a posicao do
     * proximo registro. Este metodo deve ser sobrescrito nas subclasses que 
     * devem fazer uma chamada a este antes de tentarem fazer a leitura no 
     * arquivo. Para se certificarem de que nao foi alcancado o fim do arquivo.
     * 
     * @param position A posicao do registro a ser lido
     * 
     * @return False se tentar ler em uma posicao alem do ultimo registro do 
     * arquivo
     * 
     * @throws IOException Em caso de erro de IO
     */
    protected boolean read(final int position) throws IOException
    {
        /*
        Se eh feita uma tentativa de ler alem do fim do arquivo
        */
        if 
        ( 
            ((position == -1) && (nextPosition > ceil)) ||
            (position > ceil)
        ) 
            return false;//Informa que a leitura do arquivo nao deve ser feita
       
        seek(position);//Posiciona o ponteiro de leitura
        
        return true;//Informa para o metodo que sobrescreve este que a leitura 
                    //pode ser feita
    }//read()
    
    /*[15]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Atualiza os campos do objeto com valores lidos de um registro do arquivo
     * cuja chave primaria tenha valor igual a key
     * 
     * @param key Um valor de chave primaria do arquivo, sem prefixos ou sufixos
     * que sejam invariaveis.
     * 
     * @return True se encontrou e leu registro com chave = key. False se nao.
     * 
     * @throws IOException Em caso de erro de IO
     * 
     */
    public boolean read(final String key) throws IOException
    {
        int index = searchFor(key);
        if (index == -1) return false;
        read(index);//Vai chamar o read() da subclasse que chamarah o read() 
                    //desta classe como primeira instrucao
        return true;
    }//read()
    
    /*[16]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Escreve os campos do objeto no arquivo. Todas as operacoes de gravacao
     * devem ser sequenciais. Este metodo deve ser sobrescrito nas subclasses
     * que  devem fazer uma chamada a este antes de tentarem fazer a gravacao no 
     * arquivo. Para garantir que o ponteiro de gravacao foi posicionado 
     * corretamente para a gravacao sequencial.
     * 
     * @throws IOException Em caso de erro de IO
     */
    protected void write() throws IOException
    {
        seek(-1);//Ajusta o ponteiro para gravacao sequencial
    }//write()
    
    /*[17]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Este metodo deve ser implementado para retornar a chave primaria da 
     * classe. Pode ser utilizado para fins de ordenacao de objetos Register 
     * em estruturas de dados
     * <p>
     * Quando a classe nao tiver chave primaria este metodo deve retornar sempre
     * null
     * <p>
     * Deve sempre retornar a chave sem prefixos ou sufixos invariaveis. E soh
     * poderah ser utilizado para comparacoes com chaves cujos prefixos e 
     * sufixos invariaves tenham sido extraidos.
     * 
     * @return A chave primaria da classe sem prefixos ou sufixos invariaves ou 
     * null se a classe e seu correspondente tipo de arquivo nao tiverem chave 
     * primaria
     */
    protected abstract String getKey();
    
 
}//classe Register
