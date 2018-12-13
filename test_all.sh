#!/bin/bash
javac Compile.java
java Compile Test.java
./assemble_link_run Test.s
