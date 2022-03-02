JAVA=java
JAVAC=javac
JFLEX=jflex
CLASSPATH=-cp /usr/share/java/cup.jar:.
CUP=cup

# all: Main.class

# Main.class: absyn/*.java parser.java sym.java Lexer.java ShowTreeVisitor.java Scanner.java Main.java
# Main.class: Scanner.java

%.class: %.java
	$(JAVAC) $(CLASSPATH) $^

Lexer.java: tiny.flex
	$(JFLEX) tiny.flex

parser.java: tiny.cup
	$(CUP) -expect 3 cminus.cup

clean:
	rm -f parser.java Lexer.java sym.java *.class absyn/*.class *~
