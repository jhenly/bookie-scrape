#!/usr/bin/env bash

echo "Building executable jar with: 'mvn clean package assembly:single'"
cd ..
mvn clean package assembly:single
