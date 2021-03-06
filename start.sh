#!/usr/bin/env bash
# Creo la imagen de docker de db-loader
cd db-loader
sbt compile
sbt docker:publishLocal

# Levanto los containers, cuando se levanta db-loader, empieza la carga de la db
docker-compose up db-loader

# compilar el proyecto del etl y generar la imagen de docker
cd ../etl
sbt compile
sbt docker:publishLocal

# Volver a la raíz y levantar el container del ETL
cd ..
chmod 777 shared_files # asegurarse de que se puede generar archivos dentro del directorio con el user de docker
docker-compose -f docker-compose-etl.yml up etl

# Compilo, creo el container y corro el compose de web server
cd rest-service
sbt compile
sbt docker:publishLocal
cd ..
docker-compose -f docker-compose-rest-service.yml up rest-service
