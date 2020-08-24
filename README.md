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

## Run DynamoDB locally

Install the Serverless Dynamodb offline plugin first:

```
npm install serverless-dynamodb-local
```

You can run dynamodb locally using:
```
sls dynamodb start --migration
```

## Invoke lambda function locally

Run Dynamodb locally first and then invoke the function:

```
sls invoke local -f tasks-manager --data '{"action":"CREATE_TASK","taskName":"name"}' --stage dev
```

