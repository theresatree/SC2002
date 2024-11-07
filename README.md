
# SC2002: Object Oriented Programming

## Table of Contents
1. [About Us](#1-about-us)
2. [Getting Started](#2-getting-started)</br>
    2.1. [Installing Maven](#21-installing-maven)</br>
    2.2. [Setup Instructions](#22-setup-instructions)</br>
3. [JavaDoc](#3-javadoc)

Link to GitHub Repository: https://github.com/theresatree/SC2002

<hr>

### 1. About Us

<b>Members of SCSG Group 4:</b>
- Ng Hoe Ping
- Edmund Yeo Zi Long
- Angelina Delia Sutiarto
- Spyridon Giakoumatos
- Wu Yiqing


<hr>

## 2. Getting Started
Maven is required to run Apache POI, which is a Java library for reading and writing in XLSX files. It helps manage dependencies in Java Project and ensured they are configured correctly. 

Clone the repository to your local machine by running
```bash
git clone https://github.com/theresatree/SC2002
```
</br>
</br>

### 2.1 Installing Maven
> Refer to this [guide](https://www.baeldung.com/install-maven-on-windows-linux-mac) for instructions to install Maven

After installing maven, please ensure it is properly configured by running
```bash
mvn --version
```
You should see the following output
>Apache Maven 3.9.9 (8e8579a9e76f7d015ee5ec7bfcdc97d260186937)
Maven home: /opt/homebrew/Cellar/maven/3.9.9/libexec
Java version: 23, vendor: Homebrew, runtime: /opt/homebrew/Cellar/openjdk/23/libexec/openjdk.jdk/Contents/Home
Default locale: en_SG, platform encoding: UTF-8
OS name: "mac os x", version: "13.4", arch: "aarch64", family: "mac"

</br>
</br>

### 2.2 Setup Instructions
*Note: Please ensure maven is properly installed before continuing to this step!*

1. **Navigate to the correct working directory**
>.../GitHub/sc2002/sc2002


2. **To compile, simply execute the commmand in the working directory**
```bash
mvn clean compile
```

3. **To run main.java, simply execute the command in the working directory**
```bash
mvn exec:exec
```
</br>
</br>
<hr>

## 3. JavaDoc
JavaDoc can be accessed by ``index.html`` under
>.../GitHub/sc2002/sc2002/target/reports/apidocs

</br>

If the following directory is unavailable, try running the following command to regenerate the javadoc
```bash
mvn javadoc:javadoc
```
</br>


*Note: Open ``index.html`` directly in a browser to view the documentation locally. 
If viewing on GitHub, download the files first as GitHub cannot render HTML files directly.*
