SRC_DIR = crypto
BIN_DIR = bin

MAIN_CLASS = crypto.Main

JAVAC = javac
JAVA = java

SOURCES = $(shell find $(SRC_DIR) -name "*.java")

all: compile run

compile:
	@mkdir -p $(BIN_DIR)
	@echo "Compilation des sources..."
	$(JAVAC) -d $(BIN_DIR) -sourcepath $(SRC_DIR) $(SOURCES)

run: compile
	@echo "Lancement de l'application..."
	$(JAVA) -cp $(BIN_DIR) $(MAIN_CLASS)

clean:
	@echo "Nettoyage..."
	rm -rf $(BIN_DIR)

.PHONY: all compile run clean
