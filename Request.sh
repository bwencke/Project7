#!/bin/bash
echo "Compiling..."
javac -classpath .:./Channel.jar Request.java
echo "Running..."
java -classpath .:./Channel.jar Request data.cs.purdue.edu 8188
