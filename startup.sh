#!/bin/bash

export IMAGES_DIR="$(pwd)/uploaded-images/"

mvn clean install

CATALINA_HOME="указать путь до Tomcat"
#CATALINA_HOME="/Users/ildan/Downloads/apache-tomcat-10.1.34"

WAR_FILE="$(pwd)/target/web-blog.war"
WEBAPPS_DIR="$CATALINA_HOME/webapps"

cp "$WAR_FILE" "$WEBAPPS_DIR"

echo "Остановка Tomcat..."
$CATALINA_HOME/bin/shutdown.sh

echo "Запуск Tomcat..."
$CATALINA_HOME/bin/startup.sh

echo "web-blog.war развернут и Tomcat перезапущен."
