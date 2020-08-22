package br.com.hkp.searchengine.main;

import br.com.hkp.searchengine.arrays.PostRegArray;
import br.com.hkp.searchengine.gui.ResultsFrame;
import static br.com.hkp.searchengine.util.Global.URL_BASE;
import static br.com.hkp.searchengine.util.Global.postExtendedArray;
import static br.com.hkp.searchengine.util.Global.postsPostRegArray;
import static br.com.hkp.searchengine.util.Global.postsWordFinder;
import static br.com.hkp.searchengine.util.Global.topicExtendedArray;
import static br.com.hkp.searchengine.util.Global.topicsPostRegArray;
import static br.com.hkp.searchengine.util.Global.topicsWordFinder;
import br.com.hkp.searchengine.util.Util;
import static br.com.hkp.searchengine.util.Util.number2Date;
import java.io.IOException;
import java.util.Comparator;
import java.util.TreeMap;
import java.util.TreeSet;

/*******************************************************************************
 * A classe que efetivamente realiza a pesquisa
 * 
 * @author "Pedro Reis"
 * @since 12 de junho de 2020 v1.0
 * @version 1.0
 ******************************************************************************/
public final class SearchEngine
{
    private final ResultsFrame resultsFrame;
              
    private TreeMap<String, TreeMapNode> treeMap;
    
    private String upWord;
    
    private String noAccentWord;
          
    /*[00]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    public SearchEngine(ResultsFrame resultsFrame) throws IOException
    {
        this.resultsFrame = resultsFrame;
    }//construtor
    
    /*[01]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    private void addOnTreeMap
    (
        final String word,//Sera verificado se a lista de post satisfaz filtros
        final String userID,//ID de usar eh filtro. Se null busca qualquer user 
        final String begin,
        final String end,//posts entre begin e end serao inclusos em treeMap
        final boolean searchPosts,//se true pesquisa por posts, false topicos
        final int w1,
        final int w2,
        final int w3,
        final int w4,//pesos para cada um dos 4 criterios de pesquisa
        final int wordBit
    ) 
        throws IOException
    {
        int firstPost;
        int listSize;
        
        byte[] block;
        
        if (searchPosts)
        {
            if (!postsWordFinder.read(word))
            {
                resultsFrame.writeln(upWord + " - n\u00e3o localizada");
                return;
            }
            firstPost = postsWordFinder.getFirstPost();
            listSize = postsWordFinder.getListSize();
            resultsFrame.writeln
            (
                upWord + " - palavra localizada em " + listSize + " posts"
            );
            
            block = postsPostRegArray.read(firstPost, listSize);
        }
        else 
        {
            if (!topicsWordFinder.read(word))
            {
                resultsFrame.writeln(upWord + " - n\u00e3o localizada");
                return;
            }
            firstPost = topicsWordFinder.getFirstPost();
            listSize = topicsWordFinder.getListSize();
            resultsFrame.writeln
            (
                upWord + " - palavra localizada em " + 
                listSize + " t\u00edtulos de t\u00f3picos"
            );
            
            block = topicsPostRegArray.read(firstPost, listSize);

        }
            
        resultsFrame.writeln("\nFiltrando posts com " + upWord + "...");
        
        int count = 0;//Conta os posts da sublista que casaram com os filtros
                        
        //O loop le o registro de cada post onde ha ocorrencia de word
        for (int i = 0; i < listSize; i++)
        {
            /*
            Recupera o i-esimo post da sublista de posts onde "word" ocorre. Se
            a pesquisa eh para textos de posts, procura nos indices que
            catalogaram palavras em textos de postagens. Se a pesquisa eh para 
            titulos de topicos, procura a palavra nos indices que catalogaram 
            palavras em titulos de topicos
            */
            String postID;
            int postRank;
            int presentOnTitle;
        
            postID = PostRegArray.getID(block, i);

            postRank = PostRegArray.getRank(block, i);
            if (postRank < 0)
            {
                postRank = - postRank;
                presentOnTitle = 1;
            }
            else
                presentOnTitle = 0;

            
            postExtendedArray.read(postID);
            
            String authorID = postExtendedArray.getAuthorID();
            String date = postExtendedArray.getDate();
           
            /*
            Se o post corresponde aos filtros especificados (autor e data),
            eh criado um objeto TreeMapNode que armazena o arquivo e o rank do
            post para aquela palavra (word). Esse objeto TreeMapNode eh 
            inserido em um TreeMap cuja chave eh o nome do arquivo HTML onde
            foi publicado o referido post. Nome de arquivo este, acrescido da
            ID do post. Exemplo: topic=1252.0.html#msg32512
            */
            if 
            (
                ( (userID == null) || (authorID.equals(userID)) ) 
                && (date.compareTo(begin) >= 0) && (end.compareTo(date) >= 0)
            )
            {
                count++;//Mais um post para word correspondeu aos filtros

                //O arquivo onde foi publicado o post
                String file = postExtendedArray.getFile();
                String topicID = file.substring(6, file.indexOf('.'));
                             
                topicExtendedArray.read(topicID);
                
                //Calculo do rank do topico baseado no num. de visualizacoes
                int topicRank = topicExtendedArray.getRank();
                
                //Calculo ainda parcial do rank desse post
                int rank = w1 * postRank + w2 * presentOnTitle + w3 * topicRank;

                /*
                A palavra eh inserida diacritizada em treeMap. Porque 
                posteriormente uma nova chamada a este metodo serah feita
                tentando inserir em treeMap posts catalogados dessa word
                na forma diacritizada. Assim nao serah criado um novo node 
                em treeMap para esse post, mas sim serah atualizado o post
                agora sendo inserido.
                */
                TreeMapNode newTreeMapNode = new TreeMapNode();
                newTreeMapNode.setWord(noAccentWord);
                newTreeMapNode.setDate(date); 
                newTreeMapNode.updateBitmap(wordBit);

                /*
                O nome do arquivo file inclui a ID do post. Portanto em 
                treeMap nao ha dois nomes de arquivo identicos, uma vez que
                a tentativa de insercao de um post com ID que ja exista em 
                treeMap indica que este post foi catalogado para uma outra
                word. Nesta chamada de metodo, sao incluidoso todos os posts
                onde ocorre ESTA word, e na sublista de posts de word nao ha
                duplicatas. Logo, um novo post com esta mesma ID (file) soh
                pode ser inserido em treeMap em uma nova chamada a este 
                metodo, e pertencerah a sublista de posts onde ocorre outra
                word. Outra palavra das que foram digitadas nos campos de 
                busca.
                */
                if (treeMap.containsKey(file))
                {
                    TreeMapNode nodeYetOnTreeMap = treeMap.get(file);

                    int currentRank = nodeYetOnTreeMap.getRank();
                    newTreeMapNode.updateBitmap(nodeYetOnTreeMap.getBitmap());

                    /*
                    Se este post que esta sendo inserido ja existe em 
                    treeMap e foi catalogado para a mesma palavra
                    associada, porem escrita de forma ortograficamente 
                    distinta. Por exemplo: se novo post que vai ser inserido
                    esta associado a palvra util, mas ja existe este post 
                    como chave em treeMap, soh que foi catalogado para 
                    "util com acento". Se isso ocorre, apenas o num. de 
                    vezes que a palavra ocorre no post eh usado para 
                    atualizar o rank desse post
                    */
                    if (nodeYetOnTreeMap.getWord().equals(noAccentWord)) 
                        newTreeMapNode.setRank(currentRank + w1 * postRank);
                    /*
                    Mas se este post que esta sendo inserido ja existe em 
                    treeMap porem foi catalogado para uma palavra 
                    diferente. Entao eh mais uma palavra da lista de 
                    pesquisa associada a um mesmo post. Nesse caso, alem do
                    rank da nova palavra, eh somado ao rank do post que jah
                    existe em treeMap, o peso w4. Reforcando o rank por ser
                    mais uma das palavras digitadas nos campos de busca, a
                    ser localizada no mesmo post.
                    */
                    else
                        newTreeMapNode.setRank(currentRank + rank + w4);
                }//if - Um post com esta ID jah existia em treeMap

                /*
                Se um post para essa palavra (word) nao existe ainda 
                em treeMap, um novo node serah inserido para este post.
                */
                else
                    newTreeMapNode.setRank(rank);

                /*
                Inclui novo node ou atualiza rank de node jah existente.
                */
                treeMap.put(file, newTreeMapNode);
               
            }//if - post correspondeu aos filtros 
            
            resultsFrame.updateProgressBar(((i-firstPost+1) * 100) / listSize);
            
        }//for i - Os posts que satisfizeram aos filtros (autor e data) e que
         //        pertenciam a sublista de posts associados a palavra word,
         //        foram inseridos em treeMap. Ou atualizaram os ranks de nohs
         //        jah anteriormente inseridos por pertencerem a sublista de 
         //        posts de alguma outra palavra.
                      
        resultsFrame.resetProgressBar();
        
        resultsFrame.writeln
        (
             "\n" + count + " posts com " + upWord + " corresponderam"
        );
        
    }//addOnTreeMap()
    
    /*[02]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    public TreeSet<TreeSetNode> getFileList
    (
        final String[] wordsArray,
        final String userID,
        final String begin, 
        final String end,
        final boolean searchPosts,
        final int w1,
        final int w2,
        final int w3,
        final int w4
    ) 
        throws IOException
    {
        /*
        Um mapa associando todos os posts encontrados na pesquisa a um valor
        de ranking de relevancia desse post
        */
        treeMap = new TreeMap<>();
        
        int wordBit = 1; 
        int plusBitmap = 0;
        int minusBitmap = 0;
        
        for (String word: wordsArray)
        {
            switch (word.charAt(0))
            {
                case '+':
                    plusBitmap = (plusBitmap | wordBit); 
                    word = word.substring(1, word.length());
                    break;
                case '-':
                    minusBitmap = (minusBitmap | wordBit); 
                    word = word.substring(1, word.length());
            }//switch
          
            upWord = word.toUpperCase();
            noAccentWord = Util.stripAccents(word);

            resultsFrame.writeln
            (
                "\nObtendo a lista de posts para a palavra " +
                upWord + "...\n"
            );
            /*
            Procura posts ou topicos com a palavra, e insere os que 
            encontrar em um TreeMap associando o post a um valor de rank 
            alem da data de publicacao
            */
            addOnTreeMap
            (
                word, userID, begin, end, searchPosts, w1, w2, w3, w4, wordBit
            );

            /*
            Procura posts ou topicos com a mesma palavra, mas agora 
            diacritizada
            */
            if (!word.equals(noAccentWord)) 
            {
                upWord = noAccentWord.toUpperCase();
                
                resultsFrame.writeln
                (
                    "\nObtendo a lista de posts para a palavra " +
                    upWord + "...\n"
                );
                
                addOnTreeMap
                (
                    noAccentWord, userID, begin, end, searchPosts,
                    w1, w2, w3, w4, wordBit
                );
            }//if
            
            wordBit <<= 1;
          
        }//for i
        
        int bitmap = plusBitmap + minusBitmap;
        
        if (bitmap > 0)
            resultsFrame.writeln
            (
                "\nAplicando opera\u00e7\u00f5es (+) e (-) \u00e0 " +
                treeMap.size() +
                " posts localizados...\n"
            );
        
        TreeSet<TreeSetNode> treeSet = 
            new TreeSet<>(new TreeSetNodeRankComparator());
        
        /*
        Transforma o TreeMap em um TreeSet ordenado pelo rankeamento de 
        relevancia dos posts.
        */
        TreeSetNode treeSetNode;
        
        for (String file: treeMap.keySet())
        {
            TreeMapNode value = treeMap.get(file);
            if ((value.getBitmap() & bitmap) == plusBitmap)
            {
                treeSetNode = new TreeSetNode();
                treeSetNode.setFile(file);
                treeSetNode.setRank(value.getRank());
                treeSetNode.setDate(value.getDate());
                treeSet.add(treeSetNode);
            }
        }//for
                
        return treeSet;
    }//getFileList()
    
           
    /***************************************************************************
    
    ***************************************************************************/
    private final class TreeMapNode
    {
        private String word;
        private String date;
        private int rank;
        private int wordsBitmap = 0;
     
        /*[01]------------------------------------------------------------------
        *
        ----------------------------------------------------------------------*/
        public void setWord(final String word)
        {
            this.word = word;
        }//setWord()

        /*[02]------------------------------------------------------------------
        *
        ----------------------------------------------------------------------*/
        public void setDate(final String date)
        {
            this.date = date;
        }//setDate()

        /*[03]------------------------------------------------------------------
        *
        ----------------------------------------------------------------------*/
        public void setRank(final int rank)
        {
            this.rank = rank;
        }//setRank()
        
        /*[04]------------------------------------------------------------------
        *
        ----------------------------------------------------------------------*/
        public void updateBitmap(final int bitmap)
        {
            wordsBitmap = (wordsBitmap | bitmap);
        }//updateBitmap()

        /*[05]------------------------------------------------------------------
        *
        ----------------------------------------------------------------------*/
        public String getWord()
        {
            return word;
        }//getWord()

        /*[06]------------------------------------------------------------------
        *
        ----------------------------------------------------------------------*/
        public int getRank()
        {
            return rank;
        }//getRank()

        /*[07]------------------------------------------------------------------
        *
        ----------------------------------------------------------------------*/
        public String getDate()
        {
            return date;
        }//getDate()
        
        /*[08]------------------------------------------------------------------
        *
        ----------------------------------------------------------------------*/
        public int getBitmap()
        {
            return wordsBitmap;
        }//getBitmap()

        
    }//classe interna TreeMapNode
    
    /***************************************************************************
     * 
     **************************************************************************/
    public class TreeSetNode
    {
        private String file;
        private String date;
        private int rank;
        
        /*[01]------------------------------------------------------------------
        *
        ----------------------------------------------------------------------*/
        public void setFile(final String file)
        {
            this.file = file;
        }//setFile()

        /*[02]------------------------------------------------------------------
        *
        ----------------------------------------------------------------------*/
        public void setDate(final String date)
        {
            this.date = date;
        }//setDate()

        /*[03]------------------------------------------------------------------
        *
        ----------------------------------------------------------------------*/
        public void setRank(final int rank)
        {
            this.rank = rank;
        }//setRank()

        /*[04]------------------------------------------------------------------
        *
        ----------------------------------------------------------------------*/
        public String getFile()
        {
            return file;
        }//getFile()

        /*[05]------------------------------------------------------------------
        *
        ----------------------------------------------------------------------*/
        public String getURL(boolean web)
        {
            if (web)
                return  URL_BASE + "/forum/" + getFile();
            else
                return  "../forum/" + getFile();
        }//getURL()

        /*[06]------------------------------------------------------------------
        *
        ----------------------------------------------------------------------*/
        public int getRank()
        {
            return rank;
        }//getRank()

        /*[07]------------------------------------------------------------------
        *
        ----------------------------------------------------------------------*/
        public String getDate()
        {
            return number2Date(date);
        }//getDate()  
        
        /*[08]------------------------------------------------------------------
        *
        ----------------------------------------------------------------------*/
        @Override
        public String toString()
        {
            return String.format("%6d | %s", getRank(), getFile());
        }//toString()
   
    }//classe interna TreeSetNode
    
    /***************************************************************************
    *
    ***************************************************************************/
    private static class TreeSetNodeRankComparator 
        implements Comparator<TreeSetNode>
    {
        @Override
        public int compare(TreeSetNode r1, TreeSetNode r2)
        {
            int r = (r2.getRank() - r1.getRank());
            
            if (r == 0)
                return (r1.getFile()).compareTo(r2.getFile());
            else
                return r;
        }        
    }//classe interna TreeSetNodeRankComparator
 
    
}//SearchEngine
