@echo off
setlocal

rem Caminhos
set SPOTBUGS_DIR=C:\Users\wellerson\Desktop\PasswordManagerProject\src\tools\spotbugs-4.9.3\spotbugs-4.9.3
set PLUGIN=%SPOTBUGS_DIR%\plugin\findsecbugs-plugin.jar
set JAR=%SPOTBUGS_DIR%\lib\spotbugs.jar
set OUT_DIR=C:\Users\wellerson\Desktop\PasswordManagerProject\out

rem Verifica se a pasta out existe
if not exist "%OUT_DIR%" (
    echo A pasta de bytecode 'out' não foi encontrada. Compile seu projeto antes.
    pause
    exit /b
)

rem Executa SpotBugs com FindSecBugs
echo Rodando SpotBugs com FindSecBugs...
java -jar "%JAR%" -pluginList "%PLUGIN%" "%OUT_DIR%"

echo Análise finalizada.
pause
endlocal
