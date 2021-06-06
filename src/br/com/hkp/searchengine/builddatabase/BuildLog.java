package br.com.hkp.searchengine.builddatabase;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/*******************************************************************************
 * Classe para gravar arquivos de log
 * 
 * @author "Pedro Reis"
 * @since 4 de junho de 2020 v1.0
 * @version 1.0
 ******************************************************************************/
class BuildLog
{
    private final PrintWriter out;
    
    /*[00]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Cria e abre um arquivo texto para saida de mensagens de log. Se o arquivo
     * jah existir seu conteudo sera eliminado. O arquivo eh criado em um 
     * diretorio nomeado "log" dentro do diretorio corrente. Se este diretorio
     * nao existir, serah criado.
     * 
     * @param fileName O nome do arquivo que deve comecar com uma /
     * 
     * @param buffer Buffer de gravacao
     * 
     * @throws IOException Em caso de erro de IO
     */
    protected BuildLog
    (
        final String fileName,
        final int buffer
    ) 
        throws IOException
    {
        File logDir = new File("log");
        if (!logDir.exists()) logDir.mkdirs();
        out = new PrintWriter
              (
                  new BufferedWriter
                  (
                      new FileWriter("log" + fileName, StandardCharsets.UTF_8), 
                      buffer
                  )
              );
    }//construtor
    
    /*[01]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Escreve uma string no arquivo de log com quebra de linha
     * 
     * @param s A String
     */
    protected void writeln(String s)
    {
        out.println(s);
    }//writeln()
    
    /*[02]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    /**
     * Fecha o arquivo de log
     */
    protected void close()
    {
        out.close();
    }//close()
    
}//BuildLog
