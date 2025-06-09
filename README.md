##  Banking Application

A Monolithic Banking Application developed using Java MVC(JSP, Servlet, JDBC) and MySQL Database.

### Architecture Diagram
A monolithic application is built as a single, unified unit. All the application's functionalities, such as user interface(View Layer), business logic(Controller/Service Layer), and data access(DAO Layer), are tightly coupled and deployed as one piece.

```mermaid
graph TB
    %% Client Layer
    Browser[Web Browser]
    
    %% Monolithic MVC Application
    subgraph "Monolithic MVC Banking Application"
        %% View Layer (JSP)
        subgraph "View Layer - JSP"
            INDEX[index.jsp]
            LOGIN[login.jsp]
            DASH[dashboard.jsp]
            TRANS[transaction.jsp]
            ERROR[error.jsp]
            CSS[style.css]
        end
        
        %% Controller Layer (Servlets)
        subgraph "Controller Layer - Servlets"
            LS[LoginServlet]
            DS[DashboardServlet]
            TS[TransactionServlet]
            LOS[LogoutServlet]
        end
        
        %% Model Layer
        subgraph "Model Layer"
            subgraph "DAOs"
                ADAO[AccountDAO]
                TDAO[TransactionDAO]
            end
            
            subgraph "Models"
                ACC[Account.java]
                TRN[Transaction.java]
            end
            
            subgraph "Utilities"
                DBC[DBConnection.java]
            end
        end
    end
    
    %% Database Layer
    subgraph "Database Layer"
        MySQL[(MySQL Database)]
    end
    
    %% Request Flow
    Browser --> INDEX
    Browser --> LOGIN
    Browser --> DASH
    Browser --> TRANS
    Browser --> ERROR
    
    %% Static Resources
    LOGIN --> CSS
    DASH --> CSS
    TRANS --> CSS
    
    %% JSP to Servlets
    LOGIN --> LS
    DASH --> DS
    TRANS --> TS
    LOGIN --> LOS
    
    %% Servlet Response to JSP
    LS --> DASH
    LS --> ERROR
    DS --> DASH
    TS --> TRANS
    TS --> ERROR
    LOS --> LOGIN
    
    %% Servlets to DAOs
    LS --> ADAO
    DS --> ADAO
    DS --> TDAO
    TS --> ADAO
    TS --> TDAO
    
    %% DAOs use Models
    ADAO --> ACC
    TDAO --> TRN
    
    %% DAOs use DB Connection
    ADAO --> DBC
    TDAO --> DBC
    
    %% DB Connection to MySQL
    DBC --> MySQL
    
    %% Styling
    classDef client fill:#e3f2fd
    classDef view fill:#f3e5f5
    classDef controller fill:#e8f5e8
    classDef model fill:#fff3e0
    classDef dao fill:#fce4ec
    classDef database fill:#e0f2f1
    
    class Browser client
    class INDEX,LOGIN,DASH,TRANS,ERROR,CSS view
    class LS,DS,TS,LOS controller
    class ACC,TRN,DBC model
    class ADAO,TDAO dao
    class MySQL database
```

### Project Structure
```
banking-app/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── banking/
│       │           ├── model/
│       │           │   ├── Account.java
│       │           │   └── Transaction.java
│       │           ├── dao/
│       │           │   ├── AccountDAO.java
│       │           │   └── TransactionDAO.java
│       │           ├── controller/
│       │           │   ├── LoginServlet.java
│       │           │   ├── DashboardServlet.java
│       │           │   ├── TransactionServlet.java
│       │           │   └── LogoutServlet.java
│       │           └── util/
│       │               └── DBConnection.java
│       └── webapp/
│           ├── WEB-INF/
│           │   ├── web.xml
│           │   └── lib/
│           ├── jsp/
│           │   ├── login.jsp
│           │   ├── dashboard.jsp
│           │   ├── transaction.jsp
│           │   └── error.jsp
│           ├── css/
│           │   └── style.css
│           └── index.jsp
└── pom.xml
```

### Database Schema

```sql
-- Create database
CREATE DATABASE banking_db;
USE banking_db;

-- Accounts table
CREATE TABLE accounts (
    account_id INT PRIMARY KEY AUTO_INCREMENT,
    account_number VARCHAR(20) UNIQUE NOT NULL,
    customer_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    balance DECIMAL(10, 2) DEFAULT 0.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Transactions table
CREATE TABLE transactions (
    transaction_id INT PRIMARY KEY AUTO_INCREMENT,
    account_id INT,
    transaction_type ENUM('DEPOSIT', 'WITHDRAWAL', 'TRANSFER') NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    description VARCHAR(255),
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (account_id) REFERENCES accounts(account_id)
);

-- Sample data
INSERT INTO accounts (account_number, customer_name, email, password, balance) VALUES
('ACC001', 'Otto', 'otto@gmail.com', 'password123', 5000.00),
('ACC002', 'Rich', 'rich@gmail.com', '123456', 1000.00);
```

### Deployment Instructions
1. Set up MySQL Database
- Install MySQL Server with docker
```
docker run --name mysql -e MYSQL_ROOT_PASSWORD=password -p 3306:3306 -d mysql:8.0
```

- Create the database and tables using the provided SQL scripts
```
docker exec -it mysql mysql -uroot -p
```

- Update database credentials in DBConnection.java

2. Build the Application
```
mvn clean package
```

3. Run with Tomcat
- Option 1: Deploy to External Tomcat
	- Copy the generated banking-app.war from target/ directory
	```
	cp target/banking-app.war /path/to/tomcat/webapps/
	```

	- Start Tomcat server
	```
	# Start Tomcat
    /path/to/tomcat/bin/startup.sh  # Linux/Mac
    /path/to/tomcat/bin/startup.bat  # Windows
    ```

- Option 2: Using Tomcat Maven Plugin
```
# Download and run embedded Tomcat
mvn tomcat7:run
```

4. Access the Application:
```
Open browser and navigate to: http://localhost:8081/banking-app
Login with sample credentials:
Email: rich@gmail.com, Password: 123456
```


