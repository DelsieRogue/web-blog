# Веб-Блог

Это веб-приложение для ведения блога.

## Запуск тестов

Для запуска тестов выполните следующую команду:

```bash
mvn clean test
```

Для запуска приложения необходимо указать путь до Tomcat в переменной 
CATALINA_HOME в файле start.sh

далее запустить команды:

```bash
chmod 777 startup.sh
```
```bash
./startup.sh
```

Для доступа к приложению откройте следующий URL в браузере:

[http://localhost:8080/web-blog/post](http://localhost:8080/web-blog/post)



