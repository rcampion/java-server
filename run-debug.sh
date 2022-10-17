#!/bin/sh

java -agentlib:jdwp=transport=dt_socket,address=9999,server=y,suspend=n -jar target/java-server-0.0.1-SNAPSHOT.jar
