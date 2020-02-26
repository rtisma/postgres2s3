# postgres2s3

Automatic periodic backups of PostgresSQL to S3

## Environment Variables
### Required
```
# For more information on cron config, refer to:
# https://docs.oracle.com/cd/E12058_01/doc/doc.1014/e12030/cron_expressions.htm
BACKUP_CRONSCHEDULE
BACKUP_AUTOCREATEBUCKET   
BACKUP_PGDUMPEXEPATH
S3_ACCESSKEY
S3_SECRETKEY
S3_BUCKETNAME
S3_ENDPOINTURL
DB_NAME
DB_HOST
DB_PORT
DB_USER
DB_PASSWORD
```

### Optional
```
BACKUP_PREFIX
```

## Demo
For more information on the configuration, refer to the `backup` service in the `docker-compose.yml`.

### Demo Startup
Start up the `postgres` database, `s3` object storage using minio, and the `postgres2s3` service
```bash
./start.sh
```
By logging into http://localhost:8085 with accessKey `minio` and secretKey `minio123`, the backups can be observed every 10 seconds

### Stopping Demo and Cleaning Data
To stop all the services and destroy all object data from minio run the following:
```bash
./stop.sh
```
