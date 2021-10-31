#!/bin/bash

for service in "$@"
do
  echo "--- Recompiling service ($service) ---"
  mvn -f ./$service/ clean install -DskipTests

  echo "--- Rebuilding and restarting Docker image ($service) ---"
  docker-compose up -d --force-recreate --no-deps --build $service

done

