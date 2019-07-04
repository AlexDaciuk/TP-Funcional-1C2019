# TP-Funcional-1C2019

# Requisitos

- Java 8 como default del usuario que ejecuta `start`
- Tener el servicio de Docker iniciado y tener permisos con el usuario que ejecuta `start`


# Carga de DB

La db se carga con las filas del archivo `assets/csv/train.csv`

# ETL y XGBoost

A completar por Lucas

# API Rest

La api rest esta armada con la libreria `http4s` y el `route` donde toma los JSON es `/predict`, quedando la URL completa como `http://127.0.0.1/predict`, devolviendo **algo**

# API Test

Para probar el funcionamiento de la API y el modelo, se armo un pequeño codigo que usa `http4s` (en este caso como cliente HTTP) para hacer requests a la API usando el archivo `assets/csv/test.csv` para formar los JSON para el request

Todo el manejo de JSONs se hizo con `circe` y el manejo de CSVs se hizo con `kantan.csv`
