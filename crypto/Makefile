SRC_DIR    = crypto
BIN_DIR    = bin
CP         = $(BIN_DIR):$(SRC_DIR)/driver_sql/mysql-connector-j-8.0.33.jar:.
MAIN       = crypto.Main
JAVAC      = javac
JAVA       = java

all: compile

compile:
	@mkdir -p $(BIN_DIR)
	@find $(SRC_DIR) -name "*.java" > /tmp/sources.txt
	@echo "Compilation de $$(wc -l < /tmp/sources.txt) fichiers..."
	$(JAVAC) -encoding UTF-8 -d $(BIN_DIR) -sourcepath $(SRC_DIR) -cp $(CP) @/tmp/sources.txt
	@echo "Compilation reussie : $$(find $(BIN_DIR) -name '*.class' | wc -l) classes generees."

run: compile
	$(JAVA) -cp $(CP) $(MAIN)

clean:
	rm -rf $(BIN_DIR) /tmp/sources.txt

.PHONY: all compile run clean
