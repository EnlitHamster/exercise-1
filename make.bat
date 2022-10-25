ECHO OFF
CLS
ECHO.

SET SRC_DIR="./src"
SET OUT_DIR="./out"

:NO_ARG
ECHO "Valid arguments: clean, setup, build, build_clean"
EXIT /B 0

:: Ripulisce il progetto di tutti i file generati per la creazione dell'eseguibile
:CLEAN
rm -rf %SRC_DIR%
rm -f ManagerSegnalibri.jar
EXIT /B 0

:: Crea le cartelle e i file necessari per la creazione dell'eseguibile
:SETUP
MKDIR %OUT_DIR%/
COPY %SRC_DIR%/**/* %OUT_DIR%/
EXIT /B 0

:: Genera l'eseguibile
:: I flag della generazione dell'eseguibile sono:
:: - create: informa il compilatore di creare l'eseguibile
:: - file: inforam il compilatore quale file deve generare
:: - main-class: definisce quale sia la classe da eseguire
:: - C: la cartella che contiene i file generati
:: L'argomento "agenda" definisce quale sia il pacchetto base dell'eseguibile
:BUILD
CALL :SETUP
javac %SRC_DIR%/**/*.java -d %OUT_DIR%/
jar --create --file ManagerSegnalibri.jar --main-class agenda.Main -C %OUT_DIR%/ agenda
EXIT /B 0

:BUILD_CLEAN
CALL :CLEAN
CALL :BUILD
EXIT /B 0


IF %1=="clean" CALL :CLEAN
ELSE IF %1=="setup" CALL :SETUP
ELSE IF %1=="build" CALL :BUILD
ELSE IF %1=="build_clean" CALL :BUILD_CLEAN
ELSE CALL :NO_ARG
