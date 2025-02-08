#!/bin/bash

export IMAGES_DIR="$(pwd)/uploaded-images/"

APP_PORT=8080

PID=$(lsof -t -i:$APP_PORT)
if [ -n "$PID" ]; then
    echo "Найден процесс, использующий порт $APP_PORT (PID: $PID). Завершаем его..."
    kill -9 $PID
    echo "Процесс $PID завершен. Порт $APP_PORT освобожден."
    sleep 2
else
    echo "Порт $APP_PORT свободен."
fi

gradle bootJarWithTest

chmod 777 ./build/libs/web-blog-DEV-SNAPSHOT.jar
./build/libs/web-blog-DEV-SNAPSHOT.jar

