echo.
echo ***************************************************************************
echo * Para compilar este projeto eh aconselhavel que voce tenha instalado no  *
echo * seu sistema o OracleJDK14 ou posterior.                                 *
echo *                                                                         *
echo * Se voce tem o OracleJDK tecle ENTER ou entao CTRL+C para abortar.       *
echo *                                                                         *
echo ***************************************************************************

pause

echo.
echo COMPILANDO PROJETO...
echo.

javac br\com\hkp\searchengine\arrays\*.java
javac br\com\hkp\searchengine\gui\*.java
javac br\com\hkp\searchengine\gui\basic\*.java
javac br\com\hkp\searchengine\main\*.java
javac br\com\hkp\searchengine\registers\*.java
javac br\com\hkp\searchengine\util\*.java

echo.
echo GERANDO ARQUIVO JAR...
echo.

jar cmf manifest.txt Huugle.jar br\com\hkp\searchengine\arrays\*.class br\com\hkp\searchengine\gui\*.class br\com\hkp\searchengine\gui\basic\*.class br\com\hkp\searchengine\main\*.class br\com\hkp\searchengine\registers\*.class br\com\hkp\searchengine\util\*.class br\com\hkp\searchengine\gui\*.png

echo.
echo ------- ARQUIVO JAR GERADO --------
echo.

echo ***************************************************************************
echo * Para executar esse programa voce precisa da pasta /database que nao faz *
echo * faz parte desse repositorio mas pode ser baixada no link:               *
echo *                                                                         *
echo *https://mega.nz/file/Eygi2AJY#53QAHmNPyp4nvvQ2S5nfAgQHdTV4OGJ5HqA4yhUwoSE*
echo *                                                                         *
echo * O arquivo jar gerado e a pasta database devem ser copiados para uma     *
echo * mesma pasta nomeada /Huugle.                                            *
echo *                                                                         *
echo * Voce tambem pode baixar uma copia navegavel do Clube Cetico no link:    *
echo *                                                                         *
echo *https://mega.nz/file/NnwyXIYT#Kvo6b3snx3XPtj7pQK4m1C4Z5dH3Dzo0RINww0OoVcA*
echo *                                                                         *
echo * Ira baixar o arquivo compactado com a pasta /clubecetico.org. Para      *
echo * navegar pela copia acesse o arquivo index.html nesta pasta.             *
echo *                                                                         *
echo * Para que o programa crie links para esta copia local, a pasta /Huugle   *
echo * deve ser copiada para dentro da pasta /clubecetico.org obtida no link   *
echo * acima.                                                                  *
echo ***************************************************************************