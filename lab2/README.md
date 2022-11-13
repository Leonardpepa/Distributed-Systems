# Εργαστήριο 2

## Υλοποίηση Κατανεμημένου Συστήματος Τραπεζικών Λειτουργιών

## Αρχιτεκτονική Τριων Επιπέδων (3-Tier)

## Structure
* Client_ATM contains the code for the Presentation layer
* Server_ATM contains the code for the Business Logic and the Data Layer

## Prerequisites
* Server
    - docker
* Client
    - java 8 or higher

## How to run

### STEP 1 Run the Server
* ***Be sure you are in the lab2/ folder***
open a terminal and run: <br>
```terminal
    docker compose up
```
* ***when the server is ready you will see the message: server is listening in port 8080*** <br>
***then you can proceed to step 2*** <br>
* ***the first time the initialization will take some time, please wait until the server and database are started***

### STEP 2 Run the Client
to run the client you have 2 options
* run via the Intellij IDE
    - open the Client_ATM as project in intellij and run the Main.java
* run via terminal
    - navigate to Client_ATM/src with
    ```terminal
        cd Client_ATM/src
    ```
    - run:  
    ```terminal
       javac *.java && java Main
    ```
