@echo off
REM === Nastavení ===
set ANTLR_JAR=antlr-4.13.2-complete.jar
set GRAMMAR_FILE=PLC_Lab7_expr.g4

REM === Kontrola existence ANTLR ===
if not exist %ANTLR_JAR% (
    echo Chyba: Soubor %ANTLR_JAR% nebyl nalezen.
    exit /b 1
)

echo [1/3] Generuji parser z gramatiky %GRAMMAR_FILE%...
java -jar %ANTLR_JAR% -visitor %GRAMMAR_FILE%

echo [2/3] Kompiluji Java soubory...
javac -cp .;%ANTLR_JAR% *.java

if %errorlevel% neq 0 (
    echo Kompilace selhala.
    exit /b 1
)

echo [3/3] Spoustim program...
java -cp .;%ANTLR_JAR% Main

echo Hotovo.
