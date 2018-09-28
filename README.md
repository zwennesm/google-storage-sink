# Google Pub/Sub Loader

Loads data from Google Pub/Sub to Google Storage.
```
google-storage-sink v0.1
Usage: gss [options]

  -s, --sub <value>      Google Pub/Sub subscription name
  -b, --bucket <value>   Google Storage bucket name
  -r, --rows <value>     Max records per generated file
  -m, --minutes <value>  Max minutes before sink empties
  -f, --secret <value>   Location of Google Service account key
  --help                 Prints this usage text
```

### Set up
 
1. Create a topic and subscription (configured to pull) in Google Cloud. In future versions this feature will be included.
But for now the resources mentioned have to be created manually.

### Run the application

1. Start by building the JAR file:
   
    ```
    sbt clean assembly
    ```
2. Then run the application with some extra parameters:

    ```
    java -jar target/scala-2.12/gss-0-1.jar \
        --sub <GOOGLE_SUBSCRIPTION_ID> \
        --bucket <GOOGLE_STORAGE_BUCKET_NAME> \
        --rows <MAX_RECORDS_BEFORE_BUFFER_EMPTIES> \
        --mins <MAX_MINUTES_BEFORE_BUFFER_EMPTIES> \
        --secret <LOCATION_TO_SECRET_AUTH_FILE>
    ```
   The `auth` should reference to a JSON file with read/write access to Pub/Sub and read/write to the given 
   `storage.bucket`. It's recommended to use a `Service account` for this. [Read more about creating 
   keys with Service accounts.](https://cloud.google.com/iam/docs/creating-managing-service-account-keys)

### Using Docker

The project includes a `Dockerfile` which makes it possible to deploy the application as a Docker container.
Use the following steps to build and run a Docker image locally.

1. Build the Docker image:
    
    Small note; The Docker build requires a Google Service account key file. This file is currently copied
    from the root of this project to the Docker image. So in order to make this work you need to place the
    secret key file in to the root of this repository and name it: `secret.json`. We hope to fix this in 
    future releases.
    
    ```
    docker build -t google-storage-sink:0.2 .
    ```
2. Run the application with the required parameters (supplied as `ENV VARIABLES`):
    
    ```
    docker run \
        -e GOOGLE_SUBSCRIPTION_ID=subscription-name \
        -e GOOGLE_BUCKET_NAME=data-storage-bucket \
        -e EMPTY_BUFFER_ROWS=1000 \
        -e EMPTY_BUFFER_MINUTES=10 \
        -e SECRET_AUTH_LOCATION=/etc/secret.json \
        google-storage-sink:0.2
    ```