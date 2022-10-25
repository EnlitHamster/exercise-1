SRC_DIR := ./src
OUT_DIR := ./out

# Ripulisce il progetto di tutti i file generati per la creazione dell'eseguibile
.PHONY: clean
clean:
	rm -rf $(OUT_DIR)/
	rm -f ManagerSegnalibri.jar

# Crea le cartelle e i file necessari per la creazione dell'eseguibile
.PHONY: setup
setup:
	mkdir $(OUT_DIR)
	cp $(SRC_DIR)/**/* $(OUT_DIR)/

# Genera l'eseguibile
# I flag della generazione dell'eseguibile sono:
# - create: informa il compilatore di creare l'eseguibile
# - file: inforam il compilatore quale file deve generare
# - main-class: definisce quale sia la classe da eseguire
# - C: la cartella che contiene i file generati
# L'argomento "agenda" definisce quale sia il pacchetto base dell'eseguibile
.PHONY: build
build: setup
	javac $(SRC_DIR)/**/*.java -d $(OUT_DIR)/
	jar --create --file ManagerSegnalibri.jar --main-class agenda.Main -C $(OUT_DIR)/ agenda

.PHONY:
build_clean: clean build
