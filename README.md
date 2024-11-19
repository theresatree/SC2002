# SC2002: Object Oriented Programming

## Table of Contents
1. [About Us](#1-about-us)
2. [Setup Instructions](#2-setup-instructions)
3. [JavaDoc](#3-javadoc)
    3.1 [Preview Javadoc in Windows](#31-preview-javadoc-in-windows)
    3.2 [Preview Javadoc in Mac](#31-preview-javadoc-in-mac)

[Link](https://github.com/theresatree/SC2002) to GitHub Repository
[Link](https://github.com/theresatree/SC2002/blob/main/Relevant%20Files/SC2002%20UML%20Class%20Diagram.pdf) to UML Class Diagram
> UML Class Diagram PDF needs to be downloaded to be viewed.


<hr>

### 1. About Us

<b>Members of SCSG Group 4:</b>
- Ng Hoe Ping
- Edmund Yeo Zi Long
- Angelina Delia Sutiarto
- Spyridon Giakoumatos
- Wu Yiqing


<hr>

## 2. Setup Instructions

1. **Navigate to the correct working directory**
>.../GitHub/sc2002/sc2002


<b>Compile and run</b>
To compile, run the following command
```bash
javac -d bin "@sources.txt"
```

To run, run the following command
```bash
java -cp bin sc2002.main.Main
```
<br>
<hr>

## 3. JavaDoc
JavaDoc can be accessed by ``index.html`` through [here](https://theresatree.github.io/SC2002/sc2002/docs/index.html) or under the directory
>.../GitHub/sc2002/sc2002/docs

</br>

If the following directory is unavailable, try running the following command to regenerate the javadoc
```bash
javadoc -d docs -sourcepath src/main/java -subpackages sc2002
```
</br>


*Note: Open ``index.html`` directly in a browser to view the documentation locally. 
If viewing on GitHub and not GitHub Pages, download the files first as GitHub cannot render HTML files directly.*

### 3.1 Preview Javadoc in Windows
If under the working directory, you can directly preview the javadoc by running
```bash
start docs\index.html
```

### 3.1 Preview Javadoc in Mac
If under the working directory, you can directly preview the javadoc by running
```bash
open docs/index.html
```