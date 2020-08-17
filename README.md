# uberAppServer
Application allows passengers to arrange rides with drivers in their city. Created by Alexandra Duron and Graeme Somerville.

A version of Uber we created as a practice project. This is the server side program.

* The program can be ran on any machine which can run Java applications however we chose an AWS EC2 instance to practice useing AWS.
* Client-Server communication is handled through java sockets
* We are using threading to handle multiple clients, new clients are accepted and then sent into a new thread where all further communication occurs

## To Run
### In AWS
* Open an EC2 instance in AWS
* Compile and run the program in the terminal
