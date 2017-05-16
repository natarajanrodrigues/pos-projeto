#!/bin/bash
docker-compose down
docker rmi -f natarajan/suggestions-db
docker rmi -f natarajan/suggestions-web
docker volume remove posprojeto_postgres-volume-suggestions
