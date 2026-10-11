# Dockerfile de la base de datos MySQL a la que se conecta el backend de la veterinaria.
# La imagen parte de MySQL 8.0, versión compatible con el dialecto MySQL8Dialect
# configurado en application.properties.
FROM mysql:8.0

# Las variables de entorno crean, al primer arranque, la base de datos y el usuario
# que el backend utiliza (ver spring.datasource.* en application.properties).
ENV MYSQL_ROOT_PASSWORD=root
ENV MYSQL_DATABASE=mydatabase
ENV MYSQL_USER=myuser
ENV MYSQL_PASSWORD=password

# El script se copia a la carpeta de inicialización: MySQL lo ejecuta automáticamente
# sobre "mydatabase" la primera vez que se crea el contenedor.
COPY init.sql /docker-entrypoint-initdb.d/

# El contenedor expone el puerto estándar de MySQL; al ejecutarlo se publica en el 3307
# del equipo (docker run -p 3307:3306), que es el puerto que usa el backend.
EXPOSE 3306
