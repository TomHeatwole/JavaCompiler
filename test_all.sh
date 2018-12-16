#!/bin/bash
javac Compile.java lexer/*  parser/*.java
mv parser/*.class ./
mv lexer/*.class ./
java Compile Test.java
./assemble_link_run Test.s
rm *.class
