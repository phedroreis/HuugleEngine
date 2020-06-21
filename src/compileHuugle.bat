javac br\com\hkp\searchengine\arrays\*.java
javac br\com\hkp\searchengine\gui\*.java
javac br\com\hkp\searchengine\gui\basic\*.java
javac br\com\hkp\searchengine\main\*.java
javac br\com\hkp\searchengine\registers\*.java
javac br\com\hkp\searchengine\util\*.java

jar -cmfv Huugle.jar manifest.txt br\com\hkp\searchengine\arrays\*.class br\com\hkp\searchengine\gui\*.class br\com\hkp\searchengine\gui\basic\*.class br\com\hkp\searchengine\main\*.class br\com\hkp\searchengine\registers\*.class br\com\hkp\searchengine\util\*.class br\com\hkp\searchengine\gui\*.png

@echo off
echo.
echo *******************************************************************
echo *                  Para tornar o jar executavel                   *
echo *                                                                 *
echo * Abrir o Huugle.jar com o 7zip e editar o arquivo MANIFEST.MF    *
echo *                                                                 *
echo * Incluir a linha Main-Class: br.com.hkp.searchengine.main.Huugle *
echo *                                                                 *
echo * Huugle.jar e /database devem estar na mesma pasta para que o    *
echo * programa seja executado.                                        *
echo *                                                                 *
echo *******************************************************************
