# Multiway merge sort Benchmarks

This project is done as a coding assignment for [Database Systems Architecture](http://cs.ulb.ac.be/public/teaching/infoh417) course at ULB.
This project contains the code for Benchmarking the code in other project.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. 

### Prerequisites

* JDK 1.8

### With maven
#### Building

Run the following in root dir of the repository:

````
mvn clean install
````

#### Downloading libs
Make sure to download all the libs before running without maven.
Use this script or check pom for libs. 

````
mvn install -P qa
````

### Without maven
#### Building
You can build the project either through scripts or manually running javac.

In the main directory of the project run the following:

##### Unix Based Systems

````
rm -rf target/
mkdir -p target/classes
javac -source 1.8 -target 1.8 -d target/classes -cp 'lib/*' `find src/main/java -name "*.java"`

````

Or you can run the script:

````
build-scripts/build.sh
````

##### Windows
[TODO] - check lib support

````
rmdir target/classes /s /Q
mkdir target/classes
dir /s /B *.java > target/classes/sources.txt
javac -source 1.8 -target 1.8 -d target/classes -cp "lib/*" "@target/classes/sources.txt"

````

#### Running
You can run the project either through scripts or manually running javac.

In the main directory of the project run the following:

##### Unix Based Systems

````
java -cp 'src/main/conf/:lib/*:target/classes' com.bdma.dsa.cde.Main
````

Or you can run the script:

````
build-scripts/run.sh
````

##### Windows

In the main directory of the project.

````
java -cp "src/main/conf/;lib/*;target/classes" com.bdma.dsa.cde.Main <file-path>
````

### Configuring
A sample config file is present in `conf` directory.

[TODO]

## Authors

* **Danish Amjad** - *Initial work* - [Danish Amjad](https://github.com/damjad)