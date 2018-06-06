cd bin
java parser.Yal ../testsuite/$1/$2.yal $3
java -jar ../jasmin.jar $2.j
java $2
cd ..


