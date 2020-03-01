#!/bin/bash

postgresql_version=$(cat  ./POSTGRESQL_VERSION)
MY_UID=$(id -u) MY_GID=$(id -g) POSTGRESQL_VERSION=${postgresql_version} docker-compose build
MY_UID=$(id -u) MY_GID=$(id -g) POSTGRESQL_VERSION=${postgresql_version} docker-compose up -d
