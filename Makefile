SRC_DIR = crypto
BIN_DIR = bin

CLASSPATH = $(BIN_DIR):$(SRC_DIR)/driver_sql/mysql-connector-j-8.0.33.jar:.
MAIN_CLASS = crypto.Main

JAVAC = javac
JAVA  = java

SOURCES = $(shell find $(SRC_DIR) -name "*.java" ! -path "./$(BIN_DIR)/*")

JAVA_FLAGS = -Djava.awt.headless=true

UNAME := $(shell uname)

ifeq ($(UNAME), Linux)
    DISPLAY_SET := $(shell echo $$DISPLAY)
    ifneq ($(DISPLAY_SET),)
        JAVA_FLAGS =
    endif
endif

all: compile run

compile:
	@mkdir -p $(BIN_DIR)
	@echo "Compilation des sources depuis $(SRC_DIR)..."
	@echo "Nombre de fichiers sources trouves: $$(echo $(SOURCES) | wc -w)"
	$(JAVAC) -d $(BIN_DIR) -sourcepath $(SRC_DIR) -cp $(CLASSPATH) $(SOURCES)
	@echo "Compilation terminee"
	@echo "Contenu de $(BIN_DIR):"
	@find $(BIN_DIR) -type f -name "*.class" | head -10

run: compile
	@clear
	@echo "Lancement de l'application..."
	@echo "Classpath: $(CLASSPATH)"
	@echo "Classe principale: $(MAIN_CLASS)"
	@echo "Options Java: $(JAVA_FLAGS)"
	$(JAVA) $(JAVA_FLAGS) -cp $(CLASSPATH) $(MAIN_CLASS)

run-gui: compile
	@clear
	@echo "Lancement avec interface graphique..."
	$(JAVA) -cp $(CLASSPATH) $(MAIN_CLASS)

clean:
	@echo "Nettoyage des fichiers compilés..."
	rm -rf $(BIN_DIR)

.PHONY: all compile run run-gui clean 
