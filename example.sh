cd bin
java parser.Yal examples/$1.yal $2 $3 $4
java -jar ../jasmin.jar $1.j
java $1
cd ..
