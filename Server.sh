#!/bin/bash
echo "Compiling..."
javac -classpath .:./Channel.jar Server.java
echo "Running..."
java -classpath .:./Channel.jar Server 8188
