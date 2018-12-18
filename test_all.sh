#!/bin/bash
javac Compile.java lexer/*  parser/*.java generator/*
mv parser/*.class ./
mv lexer/*.class ./
mv generator/*.class ./
java Compile Test.java
./assemble_link_run Test.s
rm *.class
