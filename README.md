# Simple Task Manager Example

This project uses Quarkus, AWS Lambda, AWS API Gateway and the Serverless framework.

## Creating a dev native image

You can create a native image that uses _dev_ profile using:
```
./gradlew clean build -Dquarkus-profile=dev -Dquarkus.package.type=native -Dquarkus.native.container-build=true
```

## Creating a prod native image

You can create a production ready native image using:
```
./gradlew clean build -Dquarkus.package.type=native -Dquarkus.native.container-build=true
```
