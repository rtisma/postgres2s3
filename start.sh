#!/bin/bash

MY_UID=$(id -u) MY_GID=$(id -g) docker-compose build
MY_UID=$(id -u) MY_GID=$(id -g) docker-compose up -d
