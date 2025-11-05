javac -cp "lib/postgresql-42.7.8.jar" -d . *.java
java -cp ".:lib/postgresql-42.7.8.jar" utils.TestConnexion
