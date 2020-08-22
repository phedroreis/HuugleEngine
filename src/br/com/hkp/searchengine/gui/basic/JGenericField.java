/*
 arquivo JGenericField.java criado a partir de 16 de marco de 2019.
 */
package br.com.hkp.searchengine.gui.basic;

import java.awt.Color;
import java.awt.Font;
import javax.swing.*;
import javax.swing.text.PlainDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.AttributeSet;
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;

/*******************************************************************************
 * As classes que implementam esta classe abstrata tem como funcao configurar e 
 * controlar a forma como um objeto GUI de entrada de texto em uma unica linha
 * exibe e edita o texto em seu campo. 
 * <p>
 * Podem ser criadas classes que implementem esta classe abstrata para
 * especializar um <i>JGenericField</i> em receber entradas de <b>Double</b>, 
 * de <b>Integer</b>, entradas de valores monetarios, qualquer tipo de dado
 * que nao seja tipo primitivo pode ter um <i>JGenericField</i> customizado
 * para sua entrada via teclado, desde que os metodos desta classe sejam 
 * adequadamente implementados.
 * <p>
 * Qualquer subclasse desta deve definir 3 padroes para formatacao de strings: 
 * "plain", "gained" e "lost".
 * <p>   
 * "gained" dever ser o formato de uma string para ser exibida no campo quando
 * este estiver com o foco ( ou seja, editavel pelo usuario ).
 * <p>      
 * "lost" o padrao para exibir uma string quando o campo estiver sem foco.
 * <p>      
 * E "plain" algum outro padrao qualquer, preferencialmente mais simples de 
 * formatar e possivelmente o padrao para o qual o objeto eh convertido para 
 * String pelo metodo <i>String.ValueOf()</i>.
 * <p>
 * Implementacoes dos metodos
 * <p>
 * {@link #gainedToPlain(java.lang.String) },
 * <p>
 * {@link #plainToGained(java.lang.String) }
 * <p>
 * {@link #plainToLost(java.lang.String) } e
 * <p>
 * {@link #lostToPlain(java.lang.String) } 
 * <p>
 * devem se encarregar de fazer a conversao entre estes padroes.
 * <p>
 * Os metodos {@link #focusGained() } e {@link #focusLost() } tambem podem ser
 * sobrescritos em uma subclasse para personalizar a aparencia do campo quando 
 * este ganha ou perde o foco. Contudo os metodos das subclasses que 
 * sobrescreverem {@link #focusGained() } e {@link #focusLost() } devem
 * fazer inicialmente uma chamada aos metodos sobrescritos desta superclasse,
 * para garantir que tudo funcione corretamente. Apenas estes dois metodos 
 * exigem que implementacoes nas subclasses facam chamadas as versoes 
 * sobrescritas da superclasse (sempre como a 1 instrucao a ser executada ). 
 * Outros metodos desta classe que forem sobrescritos nao precisam nem devem 
 * receber chamadas dos metodos sobrescritores. Pois estes, os metodos nesta 
 * superclasse, sao apenas implementacoes default que podem ser substituidas 
 * quando necessario.
 * <p>
 * Com excecao de 
 * <p>
 * {@link #setValue(java.lang.Object) }
 * <p>
 * {@link #getValue() }
 * <p>
 * {@link #setColors(java.awt.Color, java.awt.Color) }
 * <p>
 * {@link #setFonts(java.awt.Font, java.awt.Font) }
 * <p>
 * os outros metodos declarados nesta classe nao devem ser executados por alguma
 * subclasse, devendo apenas ser sobrescritos quando necessario.
 * <p>
 * <b>ENTENDENDO A FUNCAO DE CADA METODO NESTA CLASSE:</b>
 * <p>
 * Um objeto <i>JGenericField</i> serah um componente GUI 
 * (que herda da classe JTextField) que pode ter aspectos de sua aparencia e 
 * comportamento customizados pela implementacao dos metodos desta classe 
 * abstrata por uma subclasse.
 * <p>
 * Este objeto GUI terah a capacidade de:
 * <p>
 * 1 - Controlar a edicao do campo, nao permitindo que certos caracteres ou 
 * strings sejam digitados ou colados no campo, o que pode ser utilizado para 
 * impedir que dados invalidos sejam inseridos no campo.
 * <p>
 * 2 - Customizar a aparencia da exibicao do campo, que terah todas as 
 * funcionalidades* de um objeto da classe JTextField (de quem ele herda) e
 * tambem podera alterar automaticamente esta aparencia em condicionamento ao
 * campo estar ou nao com o foco. Por exemplo: ao perder o foco o campo poderah
 * apresentar o texto com outra fonte, outra cor, mudar a cor de fundo, etc...
 * <p>
 * Obs: * Exceto os metodos setText(), getText(), setFont() e setForeground().
 * Estes nao estarao disponiveis pois a execucao de algum destes metodos poderia
 * deixar o objeto em um estado inconsistente. Em substituicao a estes devem 
 * ser utilizados:
 * <p>
 * {@link #setValue(java.lang.Object) }
 * <p>
 * {@link #getValue() }
 * <p>
 * {@link #setColors(java.awt.Color, java.awt.Color) }
 * <p>
 * {@link #setFonts(java.awt.Font, java.awt.Font) }
 * <p>
 * 3 - Formatar automaticamente o texto exibido no campo. Podendo apresentar o
 * texto com uma formatacao quando o campo tiver o foco, e outra diferente 
 * quando o campo perder o foco. Uma funcionalidade que pode ser usada para,
 * quando o campo estiver com o foco, exibir o texto de uma forma mais facil de
 * editar ou processar esta edicao, e quando perder o foco exibir o texto de uma
 * forma mais legivel ou agradavel ao usuario.
 * <p>
 * 4 - Retornar qualquer tipo de objeto (de uma classe escolhida pelo
 * programador) que seja representado pela string corrente no campo. Como uma
 * string no campo deverah representar um objeto qualquer e de qualquer tipo 
 * pode ser definida pelo programador apenas com a implementacao dos metodos 
 * desta classe. Ou seja, substituindo as implementacoes default de alguns 
 * metodos desta classe por metodos que os sobrescrevem nas subclasses.
 * <p>
 * 5 - Converter qualquer objeto de qualquer classe para uma representacao 
 * textual (definida pelo programador atraves de implementacao adequada de 
 * metodos desta classe) e inseri-la no campo <i>JGenericField</i>.
 * <p>
 * Para criar uma classe que implemente <i>JGenericField</i> a fim de 
 * produzir estas funcionalidades eh preciso entender a funcao e especificacao
 * de cada metodo desta classe.
 * <p>
 * <b>CONTROLANDO O QUE PODE SER INSERIDO NO CAMPO:</b>
 * <p>
 * Esta funcao eh possibilitada quando o construtor da subclasse 
 * atribui ao campo protected regexp[FOCUS_LOST] uma string com uma expressao
 * regular que deve ser satisfeita por qualquer resultado de tentativa de 
 * edicao do campo quando este estiver sem o foco. Isto eh, sempre que o campo
 * estiver sem o foco, qualquer tentativa de inserir caracteres ou strings para 
 * modifica-lo, sera testada por essa expressao regular. A edicao soh sera feita
 * se o resultado satisfizer a expressao regular. Dessa forma a expressao 
 * regular que for atribuida a regexp[FOCUS_LOST] determina como o campo podera
 * ser atualizado (editado) quando estiver sem foco.
 * <p>
 * Em outras palavras, o padrao "lost" mencionado acima, deve ser exatamente o
 * padrao definido por esta expressao regular. Strings serao consideradas no 
 * padrao "lost" se e somente se satisfizerem a expressao regular atribuida ao
 * campo protected regexp[FOCUS_LOST] em um construtor de subclasse de 
 * <i>JGenericField</i>.
 * <p>
 * Da mesma forma este construtor da subclasse deve atribuir uma expressao
 * regular ao campo protected regexp[FOCUS_GAINED] (declarado nesta classe) que
 * definira o padrao "gained". Padrao este que devera ser satisfeito para 
 * aceitacao de qualquer tentativa de edicao quando o campo estiver COM o foco.
 * <p>
 * Em resumo: a subclasse deve atribuir (em seu construtor) strings 
 * representando expressoes regulares a regexp[FOCUS_LOST] e 
 * regexp[FOCUS_GAINED], que controlarao automaticamente que tipos de edicoes 
 * poderao ser feitas quando o campo estiver sem e com foco, respectivamente.
 * <p>
 * Dessa forma, se regexp[FOCUS_GAINED] receber "0*[0-9]{0,6}", soh sera
 * possivel ter strings no campo QUANDO COM FOCO que representem numeros 
 * inteiros de ateh 6 digitos ( no maximo ), porem podendo apresentar um numero
 * ilimitado de zeros irrelevantes a esquerda. Quando o campo estiver com foco
 * qualquer tentativa de edicao que va resultar na violacao deste padrao serah
 * automaticamente rejeitada e o campo permanecera inalterado.
 * <p>
 * Jah regexp[FOCUS_LOST] poderia receber  
 * "([0-9]{1,3}){1}(([.][0-9]{3}){0," + g +  "})" , o que permitiria que, ao
 * perder o foco, a string do campo fosse reformatada para exibir este mesmo
 * numero com separador de agrupamento (usando o ponto como separador).
 * Assim o numero  poderia aparecer sem estes pontos separadores quando 
 * estivesse sendo editado e assim que o campo perdesse o foco o numero 
 * imediatamente mudaria para um formato mais legivel, apresentado com separador
 * de milhares.
 * <p>
 * Evidentemente, para isso, nao bastaria as atribuicoes a regexp[]. Seria 
 * necessario tambem metodos que fizessem a conversao de uma string que 
 * satisfaz a expressao regular em regexp[FOCUS_GAINED]
 * para outra que satisfaca a expressao regular armazenada em
 * regexp[FOCUS_LOST]. Pois estas expressoes em si nao formatam o 
 * texto do campo, apenas impedem que textos que nao obedecem a estes formatos
 * aparecam no campo. Mesmo que o usuario os digite ou cole.
 * <p>
 * Esta classe apresenta os metodos que devem ser implementados para fazer
 * as conversoes necessarias, e sao os que se seguem:
 * <p>
 * {@link #lostToPlain(java.lang.String) } deve ter uma implementacao que 
 * converta um texto com a formatacao adequada para exibi-lo no campo sem foco,
 * para uma formatacao que serah chamada de "plain", que eh qualquer tipo de 
 * formatacao definida pelo programador (prefencialmente a mais simples).
 * <p>
 * {@link #plainToGained(java.lang.String) } deve ter uma implementacao que 
 * converta do formato "plain" descrito acima para o formato definido para o
 * texto ser apresentado quando o campo <b>TIVER</b> o foco. Que eh o formato 
 * que deve satisfazer a expressao regular atribuida a regexp[FOCUS_GAINED].
 * <p>
 * Dessa forma, tendo acesso a estes metodos atraves de um objeto de classe que
 * estenda a essa, um <i>JGenericField</i> saberah converter um texto
 * no campo que esteja sendo exibido no formato "gained" (quando o campo estah 
 * com o foco) para o formato "lost" assim que o campo perder o foco.
 * <p>
 * Somando-se a estes metodos a implementacao para 
 * <p>
 * {@link #gainedToPlain(java.lang.String)} e 
 * <p>
 * {@link #plainToLost(java.lang.String) }, 
 * <p>
 * o <i>JGenericField</i> poderah tambem fazer a conversao no sentido inverso:
 * quando o campo receber o foco.
 * <p>
 * Mais dois metodos para conversao devem ser implementados:
 * <p>
 * {@link #valueToPlain(java.lang.Object) } e
 * <p>
 * {@link #plainToValue(java.lang.String) }
 * <p>
 * Estes servem para representar o objeto na forma de uma string no padrao
 * "plain" e para, inversamente, retornar o mesmo objeto para esta representacao
 * "plain". Tais implementacoes permitem que os metodos parametrizados 
 * setValue(T value) e getValue() funcionem.
 * setValue() e getValue() podem entao ser utilizados em substituicao a 
 * setText(String s) e getText() que um <i>JGenericField</i> herda de 
 * JTextField. E na verdade <i>JGenericField</i> sobrescreve setText() com um
 * metodo de corpo vazio para impedir que seja  chamado e se possa colocar o
 * campo de entrada em algum estado inconsistente.
 * <p>
 * Ja os metodos {@link #focusGained() } e {@link #focusLost() } serao 
 * executados  toda vez que ganhar e perder o foco, e tambem se setValue(),
 * setColors() ou setFonts() forem chamados para atualizar o conteudo do campo
 * ou seus atributos. Portanto implementacoes para estes metodos podem alterar a
 * aparencia de um campo de entrada, fazendo, por exemplo, que o texto quando
 * editavel (campo com foco) apareca em uma determinada cor e trocando a cor de 
 * texto quando o campo perder o foco. Ou mudando a fonte, ou qualquer outra 
 * coisa... Porem se estes metodos precisarem ser sobrescritos deve-se incluir
 * chamadas para suas versoes nesta superclasse (sempre como a primeira 
 * instrucao no metodo da subclasse).
 * <p>
 * Alem destes se pode ainda implementar {@link #insertString(java.lang.String)}
 * se necessario. Este metodo sera sempre executado antes que a String que estah
 * para ser inserida no campo seja checada para se verificar se a edicao irah 
 * satisfaz a expressao regular que permitirah ou nao que esta 
 * edicao seja feita. Essa sera a String que serah enviada a insertString() como 
 * argumento e portanto uma implementacao de insertString() tem a oportunidade
 * de fazer qualquer pre-processamento dessa String antes que ela seja 
 * confrontada com a expressao regular que foi definida no construtor da 
 * subclasse que implementa a esta.
 * <p>
 * No entanto para customizar um  <i>JGenericField</i> da maneira desejada pode
 * nao ser necessario implementar todos os metodos aqui declarados. Por exemplo:
 * o pre-processamento da String possibilitada por uma implementacao de 
 * insertString(String str) pode nao ser necessario. Para estes casos 
 * todos os metodos possuem uma implementacao default nesta classe. Entao
 * basta nao implementar o metodo e a implementacao default serah a utilizada.
 * No caso do exemplo supra a implementacao default de insertString() apenas 
 * devolve a String recebida sem alteracoes.
 * <p>
 * Outro exemplo seria o caso do programador nao desejar ou precisar que o texto
 * no campo apresente formatacoes diferentes para com e sem foco. Nesse caso as
 * implementacoes default desta classe para os metdos listados abaixo seriam
 * suficientes.
 * <p>
 * {@link #gainedToPlain(java.lang.String) },
 * {@link #plainToGained(java.lang.String) },
 * {@link #plainToLost(java.lang.String) } e
 * {@link #lostToPlain(java.lang.String) }
 * <p>
 * Tambem no caso do objeto a ser inserido e recuperado no campo ser a propria 
 * String editada no campo, entao as implementacoes default de 
 * getValue() e setValue(), alem das de valueToPlain() e plainToValue() podem
 * ser utilizadas.
 * <p>
 * Lembre-se que esta classe eh parametrizada (generica) e o tipo do
 * parametro deve ser especificado em toda subclasse que a estenda, sendo 
 * qualquer tipo que herde de Object ou ate a propria classe Object.
 * 
 * @param <T> O tipo de objeto que serah retornado por 
 * {@link #plainToValue(java.lang.String)}, que deve processar a String do 
 * argumento para gerar um objeto correspondente a esta String que deve ser do
 * tipo T. {@link #valueToPlain(java.lang.Object) } deve ser implementado para
 * realizar a operacao inversa.
 * 
 * @author "Pedro Reis"
 * @since 1.0
 * @version 1.0
 ******************************************************************************/
public abstract class JGenericField<T> extends JTextField
{
    protected final static int FOCUS_LOST = 0; 
    protected final static int FOCUS_GAINED = 1; 
    
    /*
    As posicoes deste vetor devem ser atribuidas (quando da execucao de um
    construtor de subclasse desta classe) duas expressoes regulares.
    regexp[FOCUS_LOST] deve receber uma expressao regular que precisa ser 
    satisfeita para qualquer string estar no padrao "lost".
    regexp[FOCUS_GAINED] deve receber uma expressao regular que precisa ser 
    satisfeita para qualquer string estar no padrao "gained".
    */
    protected final String[] regexp;
    
    /*
    Indica o estado do campo. Se com foco ou sem foco.
    */
    private int state;
    
    /*
    O campo eh criado com as cores s fontes default de um JTextField, e mantem
    esse padrao ateh que SetColors() ou setFonts() seja executado.
    */
    private Color focusGainedColor = null;
    private Color focusLostColor = null;
    private Font focusGainedFont = null;
    private Font focusLostFont = null;
    
    /*[00]----------------------------------------------------------------------
    *                     Construtor da classe
    --------------------------------------------------------------------------*/
    protected JGenericField()
    {
        super();
        
        state = FOCUS_LOST;
        regexp = new String[2];
                    
        super.setDocument(new CustomDocument());
        
        super.addFocusListener(new Handler()); 
        
    }//fim do constrotor JGenericField()
    
    /*[01]----------------------------------------------------------------------
    *          Insere um texto no campo, substituindo o corrente
    --------------------------------------------------------------------------*/
    private void setNewText(String text)
    {
        if (state == FOCUS_GAINED)
            super.setText(plainToGained(text));
        else
            super.setText(plainToLost(text));
    }//fim de setNewText()
    
    /*[02]----------------------------------------------------------------------
    *            Atualiza os atributos de aparencia do campo
    --------------------------------------------------------------------------*/
    private void updateFieldAttr()
    {
        if (state == FOCUS_GAINED)
            focusGained();
        else
            focusLost();
    }//fim de updateFieldAttr()
    
    /**
     * Atualiza o campo com uma string que representa um objeto qualqeur de 
     * tipo T.
     * 
     * @param value O objeto do tipo parametrizado T.
     */
    /*[03]----------------------------------------------------------------------
    *     Recebe um objeto de tipo parametrizado T e o envia para ser 
    *     convertido para uma representacao String no padrao "plain".
    *     A conversao eh realizada pelo metodo valueToPlain() do objeto 
    *     control. 
    --------------------------------------------------------------------------*/
    public void setValue(T value)
    {
        setNewText(valueToPlain(value));
        updateFieldAttr();
    }//fim de setValue()
    
    /**
     * Retorna um objeto de tipo T que corresponde ao texto correntemente no 
     * campo.
     * 
     * @return Um objeto de tipo T correspondente ao conteudo corrente do campo.
     */
    /*[04]----------------------------------------------------------------------
    *                   Funcao inversa a setValue().
    --------------------------------------------------------------------------*/
    public T getValue()
    {
       if (state == FOCUS_GAINED)
           return (T)plainToValue(gainedToPlain(super.getText()));
       else
           return (T)plainToValue(lostToPlain(super.getText()));
    }//fim de getValue()
    
    /**
     * Retorna o conteudo do campo formatado no padrao definido para "lost".
     * (Como o texto eh exibido quando o campo perde o foco.)
     * 
     * @return O conteudo do campo formatado no padrao definido para "lost".
     */
    /*[05]----------------------------------------------------------------------
    *        Retorna o conteudo do campo formatado no padrao "lost".
    --------------------------------------------------------------------------*/
    @Override
    public String toString()
    {
       return plainToLost(valueToPlain(getValue())); 
    }//fim de toString()
    
     /**
     * Ajusta as cores de texto para quando o campo estiver com e sem o foco.
     * Se o metodo nao for chamado nenhuma vez sera usada, para ambos os casos,
     * as cores defaults de um JTextField;
     * 
     * @param l A cor do texto quando o campo estiver sem o foco.
     * @param g A cor do texto quando o campo estiver com o foco.
     */
    /*[06]----------------------------------------------------------------------
    *                   Ajusta as cores de texto do campo.
    --------------------------------------------------------------------------*/
    public final void setColors(Color l, Color g)
    {
        focusLostColor = l;
        focusGainedColor = g;
        updateFieldAttr();
    }//fim de setColors()
    
    /**
     * Ajusta as fontes de texto para quando o campo estiver com e sem o foco.
     * Se o metodo nao for chamado nenhuma vez sera usada, para ambos os casos,
     * as fontes defaults de um JTextField;
     * 
     * @param l A fonte do texto quando o campo estiver sem o foco.
     * @param g A fonte do texto quando o campo estiver com o foco.
     */
    /*[07]----------------------------------------------------------------------
    *               Ajusta as fontes que serao utilizadas no campo.
    --------------------------------------------------------------------------*/
    public final void setFonts(Font l, Font g)
    {
        focusLostFont = l;
        focusGainedFont = g;
        updateFieldAttr();
    }//fim de setFonts()
        
    /*[08]----------------------------------------------------------------------
    *    Este metodo serah executado toda vez que o campo receber o foco.
    --------------------------------------------------------------------------*/
    protected void focusGained()
    {
        if (focusGainedColor != null)setForeground(focusGainedColor);
        if (focusGainedFont != null) setFont(focusGainedFont);
    }//fim de focusGained()
    
    /*[09]----------------------------------------------------------------------
    *     Este metodo serah executado toda vez que o campo perder o foco.
    --------------------------------------------------------------------------*/
    protected void focusLost()
    {
        if (focusLostColor != null) setForeground(focusLostColor);
        if (focusLostFont != null) setFont(focusLostFont);
    }//fim de focusLost()
    
     /**
     * Este eh o metodo responsavel por converter uma string no padrao "plain"
     * para o padrao "gained". Ele deve ser capaz de transformar a String 
     * text para um formato que seja adequado para exibicao no campo quando este
     * estiver com o foco. Tanto o formato "plain" como "gained" (assim como o
     * "lost") podem ser definidos livremente, da forma que se julgar mais 
     * adequada. Mas eh preciso lembrar que qualquer string no formato 
     * "gained" deve ser reconhecida pela expressao regular atribuida a
     * regexp[{FOCUS_GAINED]. Caso contario ela nao poderah ser inserida no 
     * campo.
     * 
     * @param text A String a ser convertida
     * 
     * @return text no padrao definido para "gained".
     */
    /*[10]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    protected String plainToGained(String text)
    {
        return text;
    }//fim de plainToGained()
    
     /**
     * Este eh o metodo responsavel por converter uma string no padrao "plain"
     * para o padrao "lost". Ele deve ser capaz de transformar a String 
     * text para um formato que seja adequado para exibicao no campo quando este
     * estiver SEM o foco. Tanto o formato "plain" como "lost" (assim como o
     * "gained") podem ser definidos livremente, da forma que se julgar mais 
     * adequada.
     * 
     * @param text A String a ser convertida
     * 
     * @return text no padrao definido para "lost".
     */
    /*[11]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    protected String plainToLost(String text)
    {
        return text;
    }//fim de plainToLost()
    
    /**
     * Este metodo deve esperar uma String passada no padrao "lost" para
     * converte-la ao padrao "plain". Eh preciso estar ciente que um objeto
     * JCustomField controlado por algum objeto de classe que implemente esta
     * interface, deverah executar este metodo sempre que necessitar converter
     * uma string do padrao "lost" para "plain".
     * 
     * @param text A String a ser convertida.
     * 
     * @return text convertida para o padrao "plain".
     */
    /*[12]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    protected String lostToPlain(String text)
    {
        return text;
    }//fim de lostToPlain()
    
    /**
     * Este metodo deve esperar uma String passada no padrao "gained" para
     * converte-la ao padrao "plain".
     * 
     * @param text A String a ser convertida.
     * 
     * @return text convertida para o padrao "plain".
     */
    /*[13]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    protected String gainedToPlain(String text)
    {
        return text;
    }//fim de gainedToPlain()
    
    /**
     * Recebe uma string no padrao "plain" e retorna um objeto T correspondente.
     * 
     * @param text A String utilizada para determinar e criar o objeto do tipo
     * T
     * 
     * @return Um objeto do tipo parametrizado T
     */
    /*[14]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    protected T plainToValue(String text)
    {
        return (T)text;
    }//fim de plainToValue()
    
    /**
     * Recebe um objeto value do tipo parametrizado T e gera uma string no 
     * padrao "plain" que represente este objeto.
     * 
     * @param value O objeto do tipo T.
     * 
     * @return Um string associada ao objeto value no padrao "plain"
     */
    /*[15]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    protected String valueToPlain(T value)
    {
        return (String)value;
    }//fim de valueToPlain()
    
     /**
     * O metodo insertString() da classe interna privada CustomDocument eh 
     * responsavel por controlar as insercoes de texto de um objeto
     * JGenericField. Ele sobrescreve insertString() da superclasse 
     * PlainDocument com o codigo exibido abaixo:
     * <pre>
     * {@code 
     *   public void insertString(int offs, String str, AttributeSet a)
            throws BadLocationException
            {
                if (str == null)  return;
         
                str = control.insertString(str);

                int textLength = getLength();
                String text = getText(0, textLength);


                String result = 
                text.substring(0, offs)+ str + text.substring(offs, textLength);

                if (result.matches(regexp[state]))
                   super.insertString(offs, str, a);

            }//fim de insertString()

     * }
     * </pre>
     * 
     * Nota-se pelo codigo acima que antes de checar se str, uma string com a 
     * qual se estah tentando atualizar um JGenericField inserindo-a na posicao
     * deteminada pelo argumento offs, str eh enviada a este metodo para dar a
     * liberdade de um objeto JGenericField (de uma classe que herde desta) 
     * examinar a string str e altera-la de acordo com sua conveniencia. 
     * 
     * @param str A String enviada pelo metodo 
     * public void insertString(int offs, String str, AttributeSet a) para que
     * possa ser pre-processada neste metodo, se necessario.
     * 
     * @return str pre-processada.
     * 
     */
    /*[16]----------------------------------------------------------------------
    *
    --------------------------------------------------------------------------*/
    protected String insertString(String str)
    {
        return str;
    }//fim de insertString()
    
   /*---------------------------------------------------------------------------
    *   Estes sao sobrescritos como final aqui para que nao possam ser 
    *   sobrescritos ou executados em alguma subclasse, pois isto poderia
    *   deixar o objeto em um estado inconsistente. 
    --------------------------------------------------------------------------*/ 
    
    @Override
    public final void setText(String text)
    {
        
    }//fim de setText()
    
    @Override
    public final String getText()
    {
        return null;
    }//fim de getText()
    
/*
================================================================================    
*/    
    
private class  Handler implements FocusListener
{
    /*[01]----------------------------------------------------------------------
    *                   Handler do evento ganhar o foco
    --------------------------------------------------------------------------*/
    @Override
    public void focusGained(FocusEvent ev)
    {
        state = FOCUS_GAINED;
        setNewText(lostToPlain(JGenericField.super.getText()));
        JGenericField.this.focusGained();
    }//fim de focusGained()

    /*[02]----------------------------------------------------------------------
    *                  Handler do evento perder o foco
    --------------------------------------------------------------------------*/
    @Override
    public void focusLost(FocusEvent ev)
    {
        state = FOCUS_LOST;
        setNewText(gainedToPlain(JGenericField.super.getText()));
        JGenericField.this.focusLost();
    }//fim de focusLost()
}//fim da classe interna Handler

/*
================================================================================    
*/ 
 
private class CustomDocument extends PlainDocument
{
    /*[01]----------------------------------------------------------------------
    *        Sobrescreve o metodo insertString() impendindo que sejam 
    *        inseridos no documento caracteres invalidos.
    --------------------------------------------------------------------------*/
    @Override
    public void insertString(int offs, String str, AttributeSet a)
        throws BadLocationException
    {
        if (str == null)  return;
        
        /*
        Este metodo, que pode ser implementado em um objeto JTextFieldControls
        pode fazer, se necessario, um pre-processamento de str. Caso nao seja
        necessario basta retornar a propria str sem modificacao.
        */
        str = JGenericField.this.insertString(str);
                        
        int textLength = getLength();
        String text = getText(0, textLength);
                
        /*
        result preve o resultado da insercao
        */
             
        StringBuilder result = new StringBuilder(textLength + str.length());
        result.append(text.substring(0, offs))
              .append(str)
              .append(text.substring(offs, textLength));
           
        if (result.toString().matches(regexp[state])) 
            super.insertString(offs, str, a);
       
    }//fim de insertString()
    
}//fim da classe CustomDocument

/*
================================================================================    
*/ 

}//fim da classe JGenericField


