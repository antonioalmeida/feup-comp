#!/usr/bin/env bash
rm -rf bin
mkdir -p bin
cd src
jjtree parser/yal.jjt
javacc parser/yal.jj
cd ..
javac -d bin -sourcepath . src/*/*.java 
