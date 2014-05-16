#DroidStalker

DroidStalker (codename) aims to be a complete Android application analytics tool when its under development. It tracks CPU consuption, Memory Usage, GC runs, Battery consumption, Network usage, Display rendering performance in realtime.


## Architecture

Architecture of this project is divided into 3 different componets:

1. Front end -> Based on node-webkit
2. Kernel -> Java based engine
3. Android App -> That resides onto the device under testing

These three components communicate with each other using Apache Thrift. So advantage of doing this is the entire system becomes decoupled. So in future we can open this tool for cloud testing service providers.

###Front End


###Java Kernel


###Android Application


## Dependencies
