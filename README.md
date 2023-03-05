1. Compile:

- Windows:

	javac -cp jsfml.jar;. Main.java

- Unix:

	javac -cp jsfml.jar:. Main.java

2. Then run:

- Windows:

	java -cp jsfml.jar;. Main

- Unix:

	java -cp jsfml.jar:. Main

OR Windows (Compile + Run + Delete Class) in one command:

    del *.class && javac -cp jsfml.jar;. Main.java && java -cp jsfml.jar;. Main && del *.class

Unix (Compile + Run + Delete Class)       in one command:

		rm *.class && javac -cp jsfml.jar:. Main.java && java -cp jsfml.jar:. Main && rm *.class

Good Practice:

 - git commit then push
 - delete .class files !!!
 - Documentation https://andrew-scott.uk/jsfml/javadoc/