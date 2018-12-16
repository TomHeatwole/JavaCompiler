#!/bin/bash
javac *.java parser/*.java
mv parser/*.class ./
java Compile Test.java
./assemble_link_run Test.s
rm *.class
