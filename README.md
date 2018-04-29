# Table of contents

* [Prerequisits](#prerequisits)
* [Super quick start](#super-quick-start)
* [Run API tests](#run-api-tests)
* [Manual build](#manual-build)
* [API](#api)
* [How it works?](#how-it-works?)
* [Data store choose explained](#data-store-choose-explained)
  * [MongoDB](#mongodb)
  * [Redis](#redis)
* [TO-DO improvements](#to-do-improvements)

# Prerequisits
Docker-compose & docker

# Super Quick Start

Simply run:
```
cd <project-dir>/compose
./run.sh up -d
```
This will pull 3 containers (service, mongo & redis).

Access service on http://localhost:8080/app

Access Swagger API on http://localhost:8080/app/swagger

# Run API tests

Compose should be started.

```
mvn -P apiTests package
docker run overbooking_api_tests
```


# Maunal build
To build (including docker container)
```
mvn package
```
To run 
```
docker run -p 8080:8080 e MONGO_IP=<mongo_host> -e MONGO_PORT=<mongo_port> -e REDIS_IP=<redis_host> - e REDIS_PORT=<redis_port> -e LOGGING_LEVEL_ROOT=info
```

# API
Api is available on http://localhost:8080/app/swagger
It is selfdescriptive and proivdes examples. Based on swagger.

# How it works?
This service is highly scalable and threadsafe. This is achieved by using redis as distributed lock and mongo as database.

When booking endpoint is called:
1. Service gets lock on range date (multilock containing each day of a range and locks it as one with no deadlocks) so any other thread or service replica can't modify same days.
2. Gets read lock for configuration overbooking ratio (to prevent making reservations in case if someone is changeing documentation in write lock mode)
3. Checks in DB for limits for each day, that the're not higher then overbooking ratio
4. Increments all days limits by 1
5. Releases all locks.

When configuration endpoint is called:
1. Service gets write lock on configuration (so no-one can create reservartion at the same time)
2. Saves new configuration
3. Releases locks

# Data store choose explained
#### MongoDB
MongoDB is fast, easy to use, highly scalable database and perfectly suits for such cases. It faster and easier to use then SQL database and provide more partition tolerance with high availability. Especially when there is no need in transcations, because parallel execution is controlled by application (based on dictributed locks)

#### Redis
Redis is used as distributed lock storage, to make Service be scalable and allow to synchronze multiple instances. Basicaly it allows multiple processes of service to work as if it is multiple threads of one process.

# TO-DO improvements

1. add more logging
2. expand DB schema, use room ids for choosing a room, save reservation details etc.
3. add indexes for days collections for faster search
4. add TTLs for days to clean-up automatically
5. add TTLs for locks in REDIS to clean-up automatically
