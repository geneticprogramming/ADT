# ADT
Automatically Defined Templates approach to genetic programming for chaotic series prediction

The source can be run on a server by adding the parameter
-Djava.awt.headless=true
and setting program parameter
--visualize=false


This document describes the files and directories provided in the source code distribution to accompany the article �Implementing the template method pattern in genetic programming for improved time series prediction�, submitted to the journal Genetic Programming and Evolvable Machines.

Database Requirements:

The binary distributions make use of a relational database to store test results. A MariaDB script is provided in adt.sql. This should also be compatible with MySQL. This script will create a database named adt and the required tables. The included batch files contain username and password parameters which must be changed to work with your local installation.

Coupled vs Decoupled distributions:

The referenced paper describes Automatically Defined Templates (ADT), a new modularity approach for genetic programming. Two approaches to ADT are described Coupled and Decoupled. The initial implementation of ADT assumed a coupled approach. Testing proved this approach was not a reasonable methodology. A change was made to the source code to switch to a coupled approach. Both approaches do not exist as opinions in the same code base. The decoupled approach only exists in earlier versions of the code. Therefore, two source and binary distributions are provided.
The source is distributed in Maven compatible format. The code is distributed under a GPL license.

Binary Distributions:
Two jar files are built from the source described above.
Target nova-1.1-SNAPSHOT.jar Decoupled
Target nova-1.4-SNAPSHOT.jar Coupled
These are enabled to be run from a directory named target at the same level as the source distribution root.

Batch Files:
The provided batch files include one file for each experiment described in the referenced paper. The batches are configured for Microsoft Windows, but are compatible with Linux shell scripts with minor modifications. You must provide the user id and password of your local MySqlMariaDb installation.

The batch files are configured to incorporate visualization.The batches can be run on a server by adding the parameter
	-Djava.awt.headless=true
and setting program parameter
	--visualize=false

For any questions, please contact dave@infoblazer.com

