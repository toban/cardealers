#!/bin/bash

sudo docker build --tag cardealers-mongo:1.0 -f DockerfileMongo .
sudo docker build --tag cardealers-app:1.0 -f DockerfileApp .

sudo docker run -d --network host --name cardealers-monogo-instance cardealers-mongo:1.0
sudo docker run -d --network host --name cardealers-app-instance cardealers-app:1.0
