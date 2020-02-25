#!/bin/bash

MY_UID=$(id -u) MY_GID=$(id -g) docker-compose down -v
rm -rf ./docker/s3/data/mybackups/*.gz
rm -rf ./docker/s3/data/.minio.sys
find ./docker/s3/minio/ -mindepth 1 | grep -v dummy-file | xargs rm -rf 
