# Problem Statement 1
Perform a systematic literature review and document the findings with critical analysis.

## Learning Objective
Perform research on transaction recovery in distributed database systems – To achieve this task, read and understand a given published research material and document the findings and include the critical analysis.

## Requirements
- Read the [given paper](https://ieeexplore-ieee-org.ezproxy.library.dal.ca/document/7068178) and document your findings. You should include – what is the central theme, what problem is addressed, is there any literature review done, how successful the research is, are there any shortcomings, is there any room for improvement etc.
- In addition, explore any similar recent research article or proposal, and provide its citation with a 1⁄2 page summary with a 1⁄2 page critical analysis.
  
# Problem Statement 2
To create a prototype of a light-weight DBMS that uses SQL-like commands using Java programming language (no 3rd party libraries allowed).

## Learning Objective
Implementation of a custom multiuser DBMS with a single Transaction Manager application. 
This can be done by a custom-built program written in Java, and then perform some concurrent transactions.

## Requirements
- Write/Draw (using any tool), the design principles that is used or have used in the application program development/execution. 
- The application should be console based (no GUI needed) and it should accept user input in the form of SQL query, once the user has successfully logged in. Provide functionality for creating one database only.
- One of the required functionalities of the application is creating two factor authentication - “user authentication”. It should use ID, Password, and a simple captcha for authentication.
- The second required functionality is design of persistent storage. Once the input query is processed, user information, logs etc. must be kept in a file format - JSON, XML, CSV etc., are standard formats. To get novelty points, design own delimiters to store or access the data within a text file.
- Implementation of Queries (DDL & DML) – CREATE, SELECT, INSERT, UPDATE, DELETE applied to any number of tables.

### Implementation of Transaction 
Implement single transaction handling logic, where a transaction is identified by the system depending on user input, such as “Begin Transaction”, “End Transaction” etc. 
Since this is a transaction, it must follow ACID property, therefore, the processed query should not immediately write on your persistent data file. You can process the queries but keep those in intermediate data structure, like LinkedLists, ArrayLists etc. Once it gets “Commit” only then it should update/delete/insert etc. in the text file from the data structure. For “Rollback” the data structure must be emptied.
