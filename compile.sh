#!/usr/bin/env bash
rm -rf bin
mkdir -p bin
cd src
jjtree parser/yal.jjt
javacc parser/yal.jj
cd ..
javac -cp .:junit-4.12.jar:hamcrest-core-1.3.jar -d bin -sourcepath . src/*/*.java 

cp -r src/testsuite bin
cp hamcrest-core-1.3.jar bin
cp junit-4.12.jar bin
cp io.class bin
