#!/bin/bash
echo "Compiling..."
javac -classpath .:./Channel.jar *.java
echo "Running a Server, a Request, and a Response"
java -classpath .:./Channel.jar Server 8188&
while [ x != 10 ]; do
	java -classpath .:./Channel.jar Request localhost 8188&
	x++
done
java -classpath .:./Channel.jar Response localhost 8188&
echo "Type Request to run an addition Request, Response to run an additional Response, or exit to quit"
while [ 1 != 2 ]; do
	read input
	if [ "$input" = "Request" ]; then
		java -classpath .:./Channel.jar Request localhost 8188&
	elif [ "$input" = "Response" ]; then
		java -classpath .:./Channel.jar Response localhost 8188&
	elif [ "$input" = "Server" ]; then
		echo "You only need to run one server, dumby."
	elif [ "$input" = "exit" ]; then
		break
	fi
done
echo "goodbye."
