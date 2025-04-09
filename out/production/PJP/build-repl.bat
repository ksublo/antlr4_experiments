@echo off
setlocal

REM === Nastavení ===
set ANTLR_JAR=antlr-4.13.2-complete.jar
set GRAMMAR=PLC_Lab7_expr.g4

REM === Kontrola existence ANTLR ===
if not exist %ANTLR_JAR% (
    echo [Chyba] Soubor %ANTLR_JAR% nebyl nalezen.
    exit /b 1
)

REM === Krok 1: Generování parseru ===
echo [1/4] Generuji parser z %GRAMMAR%...
java -jar %ANTLR_JAR% -visitor %GRAMMAR%
if %errorlevel% neq 0 (
    echo [Chyba] Nepodarilo se vygenerovat parser.
    exit /b 1
)

REM === Krok 2: Kompilace ===
echo [2/4] Kompiluji Java soubory...
javac -cp .;%ANTLR_JAR% *.java
if %errorlevel% neq 0 (
    echo [Chyba] Kompilace selhala.
    exit /b 1
)

REM === Krok 3: Spusteni Main.java (prvni ukol) ===
echo [3/4] Spoustim Main (test všech výrazů)...
java -cp .;%ANTLR_JAR% Main
if %errorlevel% neq 0 (
    echo [Chyba] Spusteni Main selhalo.
    exit /b 1
)

REM === Krok 4: Spusteni REPL ===
echo [4/4] Spoustim REPL...
java -cp .;%ANTLR_JAR% REPL
if %errorlevel% neq 0 (
    echo [Chyba] Spusteni REPL selhalo.
    exit /b 1
)

echo [✔] Hotovo.

endlocal