#!/bin/bash
echo "Compiling..."
javac -classpath .:./Channel.jar Response.java
echo "Running..."
java -classpath .:./Channel.jar Response data.cs.purdue.edu 8188
