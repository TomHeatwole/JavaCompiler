#!/bin/bash
javac *.java parser/*.java
java Compile Test.java
./assemble_link_run Test.s
