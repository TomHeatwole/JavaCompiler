#!/bin/bash
./compile_move_test Test.java
./assemble_link_run Test.s
rm main.o
rm *.class
