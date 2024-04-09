generatortest:
	javac SimulationGenerator.java
	java SimulationGenerator 5 3 100 .15

build:
	javac *.java

run: build
	java Search adapticritters.params

clean:
	rm -f *.class
