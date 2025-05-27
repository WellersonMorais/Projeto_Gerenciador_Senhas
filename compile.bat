@echo off
chcp 65001
cd /d "%~dp0"
echo ================================
echo Compilando PasswordManagerProject
echo ================================

REM Criar diretório "out" se não existir
if not exist out (
    mkdir out
)

REM Encontrar todos os .java e colocar na lista
dir /s /b src\*.java > sources.txt

REM Compilar com as bibliotecas de lib/
javac -d out -cp "lib/*" @sources.txt

IF %ERRORLEVEL% NEQ 0 (
    echo ------------------------
    echo ERRO NA COMPILACAO
    echo ------------------------
    pause
    exit /b
)

echo ------------------------
echo Compilado com sucesso!
echo ------------------------

REM Pergunta se o usuário quer executar
set /p runApp=Deseja executar o programa agora? (s/n): 
if /i "%runApp%"=="s" (
    echo Executando...
    java -cp "out;lib/*" main.Main
)

pause
