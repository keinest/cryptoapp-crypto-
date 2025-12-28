SRC_DIR = .
BIN_DIR = bin

# Ajout du répertoire courant au classpath peut aider si des ressources sont nécessaires
CLASSPATH = $(BIN_DIR):driver_sql/mysql-connector-j-8.0.33.jar:.
MAIN_CLASS = Main

JAVAC = javac
JAVA = java

SOURCES = $(shell find $(SRC_DIR) -name "*.java" ! -path "./$(BIN_DIR)/*")

all: compile run

compile:
	@mkdir -p $(BIN_DIR)
	@echo "Compilation des sources..."
	$(JAVAC) -d $(BIN_DIR) -sourcepath $(SRC_DIR) -cp $(CLASSPATH) $(SOURCES)
	@echo "Compilation terminée. Contenu de $(BIN_DIR):"
	@find $(BIN_DIR) -type f -name "*.class" | head -10

run: compile
	@clear
	@echo "Lancement de l'application..."
	@echo "Classpath: $(CLASSPATH)"
	@echo "Classe principale: $(MAIN_CLASS)"
	$(JAVA) -cp $(CLASSPATH) $(MAIN_CLASS)

clean:
	@echo "Nettoyage..."
	rm -rf $(BIN_DIR)

.PHONY: all compile run clean
