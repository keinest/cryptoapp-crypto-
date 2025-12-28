SRC_DIR = crypto
BIN_DIR = bin

CLASSPATH  = $(BIN_DIR):crypto/driver_sql/mysql-connector-j-8.0.33.jar 
MAIN_CLASS = crypto.Main

JAVAC = javac
JAVA  = java

SOURCES = $(shell find $(SRC_DIR) -name "*.java")

all: compile run

compile:
	@clear
	@mkdir -p $(BIN_DIR)
	@echo "Compilation des sources..."
	$(JAVAC) -d $(BIN_DIR) -sourcepath $(SRC_DIR) -cp $(CLASSPATH) $(SOURCES)

run: compile
	@clear
	@echo "Lancement de l'application..."
	$(JAVA) -cp $(CLASSPATH) $(MAIN_CLASS)

clean:
	@echo "Nettoyage..."
	rm -rf $(BIN_DIR)

.PHONY: all compile run clean
