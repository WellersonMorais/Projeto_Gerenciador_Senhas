@echo off
chcp 65001
cd /d "%~dp0"

echo ================================
echo Compilando PasswordManagerProject
echo ================================

REM Verifica se o javac está no PATH
where javac >nul 2>&1
if %errorlevel% neq 0 (
    echo ERRO: javac não encontrado. Instale o JDK e adicione ao PATH.
    pause
    exit /b
)

REM Criar diretório "out" se não existir
if not exist out (
    mkdir out
)

REM Encontrar todos os .java e colocar na lista
dir /s /b src\*.java > sources.txt

REM Verifica se encontrou arquivos
for /f %%i in ('type sources.txt ^| find /c /v ""') do set count=%%i
if %count%==0 (
    echo ERRO: Nenhum arquivo .java encontrado na pasta src\
    pause
    exit /b
)

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

set /p runApp=Deseja executar o programa agora? (s/n): 
if /i "%runApp%"=="s" (
    echo Executando...
    java -cp "out;lib/*" main.Main
)

pause
