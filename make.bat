ECHO OFF
CLS
ECHO.

SET SRC_DIR="./src"
SET OUT_DIR="./out"
SET SOURCES_FILE="sources.txt"

IF "%1" == "clean" (
GOTO:CLEAN
) ELSE IF "%1" == "setup" (
 GOTO:SETUP
) ELSE IF "%1" == "build" (
GOTO:BUILD
) ELSE IF "%1" == "build_clean" (
GOTO:BUILD_CLEAN
) ELSE (
GOTO:NO_ARG
)

:NO_ARG
ECHO "Valid arguments: clean, setup, build, build_clean"
EXIT /B 0

:: Ripulisce il progetto di tutti i file generati per la creazione dell'eseguibile
:CLEAN
:: /S Rimuove tutte le directory e file nella directory specificata oltre alla directory stessa.
:: /Q Non richiede alcuna conferma per la rimozione di un albero di directory
RD /S /Q %OUT_DIR%
DEL /Q ManagerSegnalibri.jar
DEL /Q %SOURCES_FILE%
@ECHO CLEAN eseguito
EXIT /B 0

:: Crea le cartelle e i file necessari per la creazione dell'eseguibile
:SETUP
MKDIR %OUT_DIR%
:: Inserire /V come parametro di robocopy per un output verboso
:: /E permette di copiare le sottodirectory incluse quelle vuote
:: /NFL : No File List - don't log file names.
:: /NDL : No Directory List - don't log directory names.
:: /NJH : No Job Header.
:: /NJS : No Job Summary.
:: /NP  : No Progress - don't display percentage copied.
:: /NS  : No Size - don't log file sizes.
:: /NC  : No Class - don't log file classes.
ROBOCOPY /E /NFL /NDL /NJH /NJS %SRC_DIR% %OUT_DIR%
@ECHO SETUP eseguito
EXIT /B 0

:: Genera l'eseguibile
:: I flag della generazione dell'eseguibile sono:
:: - create: informa il compilatore di creare l'eseguibile
:: - file: informa il compilatore quale file deve generare
:: - main-class: definisce quale sia la classe da eseguire
:: - C: la cartella che contiene i file generati
:: L'argomento "agenda" definisce quale sia il pacchetto base dell'eseguibile
:BUILD
CALL :SETUP
::javac %SRC_DIR%/agenda/*.java -d %OUT_DIR%
dir src\*.java /b /s > %SOURCES_FILE%
javac @%SOURCES_FILE% -d %OUT_DIR%
jar --create --file ManagerSegnalibri.jar --main-class agenda.Main -C %OUT_DIR%/ agenda
@ECHO BUILD eseguito
EXIT /B 0

:BUILD_CLEAN
CALL :CLEAN
CALL :BUILD
@ECHO BUILD_CLEAN eseguito
EXIT /B 0
