SRC_DIR    = crypto
BIN_DIR    = bin
CLASSPATH  = $(BIN_DIR):$(SRC_DIR)/driver_sql/mysql-connector-j-8.0.33.jar:.
MAIN_CLASS = crypto.Main

JAVAC      = javac
JAVA       = java

SOURCES    = $(shell find $(SRC_DIR) -name "*.java" ! -path "./$(BIN_DIR)/*")
JAVA_FLAGS =

all: compile run

compile:
	@mkdir -p $(BIN_DIR)
	@echo "Compilation de $(shell echo $(SOURCES) | wc -w) fichiers sources..."
	$(JAVAC) -encoding UTF-8 -d $(BIN_DIR) -sourcepath $(SRC_DIR) -cp $(CLASSPATH) $(SOURCES)
	@echo "Compilation terminee."

run: compile
	@echo "Lancement de CryptoApp..."
	$(JAVA) $(JAVA_FLAGS) -cp $(CLASSPATH) $(MAIN_CLASS)

run-gui: compile
	$(JAVA) -cp $(CLASSPATH) $(MAIN_CLASS)

clean:
	@echo "Nettoyage..."
	rm -rf $(BIN_DIR)

.PHONY: all compile run run-gui clean
