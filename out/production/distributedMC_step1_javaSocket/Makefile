JFLAGS=-g
JC=javac
JVM=java

.SUFFIXES: .java .class

.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES=programation_distribuer.MasterSocket.java programation_distribuer.WorkerSocket.java

MAIN=programation_distribuer.MasterSocket

default:classes

classes: $(CLASSES:.java=.class)

run: $(MAIN).class
	$(JVM) $(MAIN)

clean:
	rm *.class