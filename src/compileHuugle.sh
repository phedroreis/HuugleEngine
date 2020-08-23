# Esse script compila o programa HuugleEngine e cria um JAR executavel.
# 
# Autor: "Pedro Reis"
#

javac br/com/hkp/searchengine/arrays/*.java
javac br/com/hkp/searchengine/gui/*.java
javac br/com/hkp/searchengine/gui/basic/*.java
javac br/com/hkp/searchengine/main/*.java
javac br/com/hkp/searchengine/registers/*.java
javac br/com/hkp/searchengine/util/*.java

jar cfm HuugleEngine.jar manifest.txt br/com/hkp/searchengine/arrays/*.class br/com/hkp/searchengine/gui/*.class br/com/hkp/searchengine/gui/basic/*.class br/com/hkp/searchengine/main/*.class br/com/hkp/searchengine/registers/*.class br/com/hkp/searchengine/util/*.class br/com/hkp/searchengine/gui/*.png

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
echo "* Para gravar na pasta /opt e necessario privilegio de root, entao digite   *"
echo "* $ sudo -i no terminal e depois o comando $ nautilus. Vai abrir o          *"
echo "* gerenciador de arquivos no modo superusuario e sera possivel fazer        *"
echo "* as alteracoes na pasta /opt.                                              *"
echo "*                                                                           *"
echo "* OBS: a pasta /database nao faz parte desse repositorio mas pode ser       *"
echo "* baixada no link:                                                          *"
echo "*                                                                           *"
echo "* https://mega.nz/file/AvwDQLIR#yW0Fl3wOqVRidoVsrxkz0MZRSgCuPZTYa0DqAVtQiL8 *"
echo "*                                                                           *"
echo "* Descompacte o arquivo baixado e obtenha a pasta /database.                *"
echo "*                                                                           *"
echo "* Se houver incompatibilidade com o java instalado em seu sistema Linux,    *"
echo "* substitua pelo OracleJDK14.                                               *"
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
echo "*****************************************************************************"