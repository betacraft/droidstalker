# DroidStalker Dummy Kernel

This kernel acts as a dummy DroidStalker kernel, and sends dummy values for each function call. The main purpose of this kernel is to help front end development of the DroidStalker easily.

## How to build ?

### Dependencies

You will need maven installed onto the computer.

### Steps

1. cd into the droid_stalker_dummy_kernel
2. execute mvn clean assembly:assembly

The final jar will be ready in target folder: droid_stalker_dummy_kernel-<version>-jar-with-dependencies.jar. 

## How to run ?

```
java -jar droid_stalker_dummy_kernel-<version>-jar-with-dependencies.jar
```