#!/bin/bash
javac Compile.java Lexer.java Token.java TokenType.java
java Compile Test.java
./assemble_link_run Test.s
