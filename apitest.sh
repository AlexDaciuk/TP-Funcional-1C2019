#!/usr/bin/env bash

# Compilo, creo el container y corro la test de la api con el otro CSV
cd api-test
sbt compile
sbt docker:publishLocal
cd ..
docker-compose -f docker-compose-apitest.yml up apitest
