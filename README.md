# ScalaJS template

Small project that contains an Akka-Http server that serves index.html and javascript files generated
by ScalaJS. The ScalaJS application will be executed on the client and it will show a small Snake game.

## Running the server

```
sbt scalaJsProjectJVM/run
```

Will run a server that binds to localhost on port 8080

## Generating the javascript application

```
sbt scalaJSProjectJs/fastOptJS
```
