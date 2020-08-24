# Esse script compila o programa HuugleEngine e cria um JAR executavel.
# 
# Autor: "Pedro Reis" 23 de agosto de 2020
#
#!bin/bash  

echo "*****************************************************************************"
echo "* Para compilar esse projeto eh aconselhavel que voce tenha instalado  em   *"
echo "* seu sistema o OracleJDK14 ou posterior.                                   *"
echo "*                                                                           *"
echo "* Para isso desinstale o OpenJDK com o comando:                             *"
echo "*                                                                           *"
echo "* $ sudo apt-get remove --purge openjdk-*                                   *"
echo "*                                                                           *"
echo "* E instale o OracleJDK14 com:                                              *"
echo "*                                                                           *"
echo "* $ sudo add-apt-repository ppa:linuxuprising/java                          *"
echo "* $ sudo apt update                                                         *"
echo "* $ sudo aptinstall oracle-java14-installer                                 *"
echo "*                                                                           *"
echo "* Vai surgir uma tela com o contrato de licenca da Oracle no terminal.      *"
echo "* Role ate o fim e tecle ESC. Entao selecione <sim> para aceitar e a tecla  *"
echo "* ENTER. Pronto, o OracleJDK14 esta instalado em seu sistema.               *"
echo "*                                                                           *"
echo "* Se voce jah tem o OracleJDK tecle <ENTER> para continuar, caso contrario  *"
echo "* tecle <CTRL+C> para abortar.                                              *"
echo "*                                                                           *"
echo "*****************************************************************************" 

read n  

echo " COMPILANDO PROJETO..."
echo ""

javac br/com/hkp/searchengine/arrays/*.java
javac br/com/hkp/searchengine/gui/*.java
javac br/com/hkp/searchengine/gui/basic/*.java
javac br/com/hkp/searchengine/main/*.java
javac br/com/hkp/searchengine/registers/*.java
javac br/com/hkp/searchengine/util/*.java

echo ""
echo " CONSTRUINDO ARQUIVO EXECUTAVEL JAR..."
echo ""

jar cfm HuugleEngine.jar manifest.txt br/com/hkp/searchengine/arrays/*.class br/com/hkp/searchengine/gui/*.class br/com/hkp/searchengine/gui/basic/*.class br/com/hkp/searchengine/main/*.class br/com/hkp/searchengine/registers/*.class br/com/hkp/searchengine/util/*.class br/com/hkp/searchengine/gui/*.png

echo ""
echo "------ ARQUIVO HuugleEngine.jar  CRIADO --------"
echo ""
echo ""
echo "*****************************************************************************"
echo "*                   Para tornar o jar executavel                            *"
echo "*                                                                           *"
echo "* Clique sobre o arquivo e abra Propriedades. Selecione 'tornar             *"
echo "* executavel'.                                                              *"
echo "*                                                                           *"
echo "* Crie uma pasta de nome /clubecetico.org/Huugle dentro da pasta do sistema *"
echo "* /opt e copie o arquivo HuugleEngine.jar e a pasta /database para dentro   *"
echo "* da pasta /Huugle. De exatamente esses nomes, respeitando maiusculas e     *"
echo "* minusculas.                                                               *"
echo "*                                                                           *"
echo "* Para gravar na pasta /opt e necessario privilegio de root. Digite         *"
echo "* $ sudo -i no terminal e depois o comando $ nautilus. Vai abrir o          *"
echo "* gerenciador de arquivos no modo superusuario e sera possivel fazer        *"
echo "* as alteracoes na pasta /opt.                                              *"
echo "*                                                                           *"
echo "* OBS: a pasta /database nao faz parte desse repositorio mas pode ser       *"
echo "* baixada no link:                                                          *"
echo "*                                                                           *"
echo "* https://mega.nz/file/Eygi2AJY#53QAHmNPyp4nvvQ2S5nfAgQHdTV4OGJ5HqA4yhUwoSE *"
echo "*                                                                           *"
echo "* Voce tambem pode instalar uma copia do acervo do Clube Cetico no seu      *"
echo "* sistema e fazer os links gerados por esse motor de busca apontar para     *"
echo "* essa copia local, em vez da copia online.                                 *"
echo "*                                                                           *"
echo "* Para ter essa copia baixe o arquivo no link:                              *"
echo "*                                                                           *"
echo "* https://mega.nz/file/NnwyXIYT#Kvo6b3snx3XPtj7pQK4m1C4Z5dH3Dzo0RINww0OoVcA *"
echo "*                                                                           *"
echo "* Descompacte e copie a pasta /clubecetico.org obtida para a pasta /opt,    *"
echo "* mesclando assim seus arquivos com o conteudo da pasta /clubecetico.org    *"
echo "* que voce criou no passo anterior. Para navegar pelo acervo acesse         *"
echo "* index.html ou index-2.html na pasta /clubecetico.org                      *"
echo "*                                                                           *"
echo "*****************************************************************************"
