#DroidStalker

DroidStalker (codename) aims to be a complete Android application analytics tool when its under development. It tracks CPU consuption, Memory Usage, GC runs, Battery consumption, Network usage, Display rendering performance in realtime.


## Architecture

```
+-----------------+              +------------------+             +-------------------+
|                 |              |                  |             |                   |
|  node-webkit    +--------------+   Java kernel    +-------------+   Android app     |
|                 |              |                  |             |                   |
+-----------------+              +------------------+             +-------------------+
```


Architecture of this project is divided into 3 different componets:

1. Front end -> Based on node-webkit
2. Kernel -> Java based engine
3. Android App -> That resides onto the device under testing

These three components communicate with each other using Apache Thrift. So advantage of doing this is the entire system becomes decoupled. So in future we can open this tool for cloud testing service providers.

###Front End

Front end is developed using node-webkit [https://github.com/rogerwang/node-webkit]. Design should be kept in a way that we could switch it into a web app in the future. This contains all the display logic.

###Java Kernel

Java kernel is core of entire logic related with debugging. The entire interface is available in droid_stalker_thrift project.

###Android Application

This acts as a helper project for Java Kernel


## Feature list

### Backend

-[x] Get thread info
-[x] Get CPU info
-[] Get Heap dump
-[] Get Network stats
-[] Get Battery info
-[x] Get installed apps
-[] Get display info

### Frontend

@rtdp and @kaunteya update this :P

## Dependencies

1. Node-Webkit : https://github.com/rogerwang/node-webkit
2. Apache-Thrift : https://github.com/apache/thrift
