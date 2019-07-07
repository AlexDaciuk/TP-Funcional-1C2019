# TP-Funcional-1C2019

## Informe

Se puede ver el informe desde [Overleaf](https://www.overleaf.com/project/5d06c9927e7b0e2e9f940cae)

## Requisitos

- Java 8 como default del usuario que ejecuta `start` y `apitest`
- Tener el servicio de Docker iniciado y tener permisos para trabajar con containers con el usuario que ejecuta `start` y `apitest`

## Carga de DB

La db se carga con las filas del archivo `assets/csv/train.csv` a la tabla `data` de la DB, convirtiendo las columnas vacias a `-1`.

Si la tabla `data` ya existia, se droppea y se recrea.

## ETL y XGBoost

A completar por Lucas

## API Rest

La api rest esta armada con la libreria `http4s` y el `route` donde toma los JSON es `/predict`, quedando la URL completa como `http://127.0.0.1/predict`, devolviendo **algo**

## API Test

Para probar el funcionamiento de la API y el modelo, se armo un pequeño codigo que usa `http4s` (en este caso como cliente HTTP) para hacer requests a la API usando el archivo `assets/csv/test.csv` para formar los JSON para el request



Todo el manejo de JSONs se hizo con `circe` y el manejo de CSVs se hizo con `kantan.csv`



## Ejecucion

Para cargar la DB, ejecutar el ETL y levantar el servicio REST basta con ejecutar el script `start.sh` que esta en el root del proyecto

Para ejecutar la prueba de la API, basta con ejecutar el script `apitest.sh` que esta en el root del proyecto
