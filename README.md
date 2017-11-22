# The Simplest Vert.x-web Server

"This project is licensed under the terms of the open source license."
This is a sample project to show the minimal deployment of a `vertx-web` based routing HTTP server.

java libraries:

 io.vertx.core.*;
 io.vertx.core.http.*;
 io.vertx.core.json.JsonObject;
 io.vertx.ext.web.*;
 io.vertx.ext.web.handler.BodyHandler;
 java.util.ArrayList;
 java.util.Collection;
 java.util.Collections;
 java.util.HashMap;
 java.util.Iterator;
 java.util.List;
 java.util.Map.Entry;

Steps:
1.Via command line, enter the project directory.
2. Compile the project: Write this command:
mvn compile
3. Run the project: write this command:
mvn exec:java -Dexec.mainClass= test.projec1.Server
4. Create deployable JAR file: Write this commands:
mvn clean package
java -cp target/project1-0.0.2-SNAPSHOT.jar \
test.project1.Server

test:
1. Via command line, enter the project directory.
2. write this command:
curl -d '{"text":"word"}' -H "Content-Type: application/json" -X POST http://localhost:8080/analyze
