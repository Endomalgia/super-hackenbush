#
# Java Makefile
# https://stackoverflow.com/questions/29184999/making-a-java-makefile (helpful source)
# https://dwm.suckless/org (makefile is based on the dwm makefile since im bad at making them)
#

# TO USE
# make to build the jar file
# make run to run the jar file
# make clean to remove all class and jar files

# NOTES
# := {simple assignment} vs = {recursive} vs ?= {assign if not already set ie command line}

EXE = jout
MANIFEST_FILE = manifest.txt # Java manifest (just sets entry point atm)

# Includes and Libs
INCS := 
LIBS := 

# Flags
JFLAGS := 

# Compiler & Platform/Arch
JC := javac
JR := jar
JL := java
PLATFORM := ${shell uname -s}
ARCHITECTURE := ${shell uname -m}

# Directories {With escaped forward slashes}
BUILD_DIR := .\/
SRC_DIR := .\/src

# Files
SRC := ${shell find ${SRC_DIR} -name '*.java'}
CLS := ${shell echo ${SRC} | sed 's/${SRC_DIR}/${BUILD_DIR}/g' | sed 's/.java/.class/g'}

all: build

build:
	@echo "SRC = ${SRC}"
	@echo "CLS = ${CLS}"
	
	@echo "/////// BUILD BEGINS HERE ///////"
	
	${JC} -d ${BUILD_DIR} ${SRC} ${JFLAGS}

	@echo "/////// LINK  BEGINS HERE ///////"

	${JR} cfm ${EXE}.jar ${MANIFEST_FILE} ${BUILD_DIR}/*.class

clean:
	# rm -r ${BUILD_DIR} Temporarily(?) making this the base dir to avoid annoying problems
	rm ${BUILD_DIR}/*.class
	rm ${EXE}.jar

run:
	${JL} -jar ${EXE}.jar

.PHONY: all clean run
