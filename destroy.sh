#!/bin/bash

postgresql_version=$(cat POSTGRESQL_VERSION)
MY_UID=$(id -u) MY_GID=$(id -g) POSTGRESQL_VERSION=${postgresql_version} docker-compose down -v
rm -rf ./docker/s3/data/mybackups/my-bucket-path/data/*.gz
rm -rf ./docker/s3/data/.minio.sys
find ./docker/s3/minio/ -mindepth 1 | grep -v dummy-file | xargs rm -rf 
