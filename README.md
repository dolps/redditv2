#redditV2
External plugins:
Lombok is used to generate annotations,
to run in ide download intellij plugin for it

Run mvn clean install -> installs dependencies takes some time the first time to
get the wildfly server

tests:
mvn verify from root

running web
cd frontend
mvn wildfly:run