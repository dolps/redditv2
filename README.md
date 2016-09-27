#redditV2
Run mvn clean install -> installs dependencies takes some time the first time to
get the wildfly server

tests:
mvn verify from root

running web
cd frontend
mvn wildfly:run