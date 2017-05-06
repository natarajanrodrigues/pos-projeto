#!/bin/bash
mvn clean install
docker-compose up -d --build
