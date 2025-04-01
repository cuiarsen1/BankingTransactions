# Banking Transactions API

## How to Build and Run

**This application runs on Java 17 and Spring Boot 3.4.4.**

**NOTE:** Ensure you have Maven installed on your system. 

* If on Windows, add the Maven bin directory to your PATH environment variable, and add a new environment variable called MAVEN_HOME to your system variables with the value being the path to your root maven directory.

---

1. Clone the repository:
   ```
   git clone https://github.com/cuiarsen1/BankingTransactions.git
   ```

   Navigate to the project directory base directory.

2. Build the project using Maven:
   ```
   mvn clean package
   ```

   This will create an executable JAR file in the `target/` directory `BankingTransactions-0.0.1-SNAPSHOT.jar`.

3. Run the application:
   ```
   java -jar target/BankingTransactions-0.0.1-SNAPSHOT.jar
   ```
4. The application will start on port 8080. Logs will be printed in the same console tab.

5. If using curl commands, please run the example commands below in a **Bash** tab.

## Extra Notes

* Logs are printed in the console where the application is run. Open a new console tab to run curl commands.
* Spring Data JPA or Hibernate can be integrated if there is a database connected, but if data is stored just in memory without a database, it does not serve a purpose.
* Errors are handled with the GlobalExceptionHandler defined for custom exceptions. All error flows will return detailed error responses to the user.

## API Endpoints

The base URL for the API is `http://localhost:8080`. They can be called with curl requests in a Bash tab or
through an API tool such as Postman.

**NOTE: Remember to replace the fields in the curl examples with your own relavant fields (UUID, etc.)**

The list of commands is as follows:

### 1. Create Account

* **Method:** `POST`
* **Path:** `/api/accounts`
* **Description:** Creates a new user account with a specified initial balance.
* **Example Request Body:**
  ```
  {
    "name": Alex
    "initialBalance": 1000.00
  }
  ```
    * `name` (String, required): The account holder's name
    * `initialBalance` (BigDecimal, required): The starting balance (must be non-negative, with at most 2 decimal
      places).
* **Example Success Response (201 Created):**
  ```
  {
    "id":"fb356bec-4df9-4c1f-b046-b4aeaf4e65b7",
    "message":"Account created successfully",
    "name":"Alex",
    "balance":"$1000.00",
    "createdAt":"2025-03-31T14:46:12.3898882"
  }
  ```
* **`curl` Example:**
  ```
  curl -X POST http://localhost:8080/api/accounts -H "Content-Type: application/json" -d '{ "name": "Alex", "initialBalance": 1000.00 }' 
  ```

### 2. Get Account Details

* **Method:** `GET`
* **Path:** `/api/accounts/{accountId}`
* **Description:** Retrieves the details (ID, name, balance, creation date) for a specific account.
* **Path Parameter:**
    * `accountId` (UUID, required): The unique identifier of the account.
* **Example Success Response (200 OK):**
  ```
  {
    "id":"fb356bec-4df9-4c1f-b046-b4aeaf4e65b7",
    "message":"Account found",
    "name":"Alex",
    "balance":"$1000.00",
    "createdAt":"2025-03-31T14:46:12.3898882"
  }
  ```
* **`curl` Example:**
  ```
  curl http://localhost:8080/api/accounts/fb356bec-4df9-4c1f-b046-b4aeaf4e65b7
  ```

### 3. Transfer Funds

* **Method:** `POST`
* **Path:** `/api/accounts/transfer`
* **Description:** Transfers a specified amount from one account to another.
* **Example Request Body:**
  ```
  {
    "fromAccountId": "fb356bec-4df9-4c1f-b046-b4aeaf4e65b7",
    "toAccountId": "24d5f080-e395-4f62-8da0-4c96a7010eab",
    "amount": 300,
    "description": "Rent payment"
  }
  ```
    * `fromAccountId` (UUID, required): The ID of the account to withdraw from.
    * `toAccountId` (UUID, required): The ID of the account to deposit to.
    * `amount` (BigDecimal, required): The amount to transfer (must be positive, with at most 2 decimal places).
    * `description` (String, not required): A description for the transaction.

* **Example Success Response (200 OK):**
  ```
  {
    "id":"6eeebd96-e867-4d83-ad30-d2dd882c4b3f",
    "message":"Transfer successful",
    "fromAccountId":"fb356bec-4df9-4c1f-b046-b4aeaf4e65b7",
    "toAccountId":"24d5f080-e395-4f62-8da0-4c96a7010eab",
    "amount":"$300.00",
    "type":"TRANSFER",
    "description":"Rent payment",
    "timestamp":"2025-03-31T15:09:29.5833538"
  }
  ```
* **`curl` Example:**
  ```
  curl -X POST http://localhost:8080/api/accounts/transfer -H "Content-Type: application/json" -d '{ "fromAccountId": "fb356bec-4df9-4c1f-b046-b4aeaf4e65b7", "toAccountId": "24d5f080-e395-4f62-8da0-4c96a7010eab", "amount": 300.00, "description": "Rent payment" }'
  ```

### 4. Get Transaction History

* **Method:** `GET`
* **Path:** `/api/accounts/{accountId}/transactions`
* **Description:** Retrieves the list of all transactions where the specified account was either the sender or the
  receiver, sorted by newest first.
* **Path Parameter:**
    * `accountId` (UUID, required): The unique identifier of the account.
* **Example Success Response (200 OK):**
  ```
  [
    {
      "id":"6eeebd96-e867-4d83-ad30-d2dd882c4b3f",
      "message":"Found 1 transactions",
      "fromAccountId":"fb356bec-4df9-4c1f-b046-b4aeaf4e65b7",
      "toAccountId":"24d5f080-e395-4f62-8da0-4c96a7010eab",
      "amount":"$300.00",
      "type":"TRANSFER",
      "description":"Rent payment",
      "timestamp":"2025-03-31T15:09:29.5833538"
    }
  ]
  ```
  **(The response will be an empty list `[]` if the account has no transactions)**
* **`curl` Example:**
  ```
  curl http://localhost:8080/api/accounts/fb356bec-4df9-4c1f-b046-b4aeaf4e65b7/transactions
  ```

### 5. Deposit Funds

* **Method:** `POST`
* **Path:** `/api/deposit`
* **Description:** Deposits a specified amount into the chosen account.
* **Example Request Body:**
  ```
  {
    "toAccountId": "fb356bec-4df9-4c1f-b046-b4aeaf4e65b7",
    "amount": 300.00
  }
  ```
    * `toAccountId` (UUID, required): The ID of the account to deposit to.
    * `amount` (BigDecimal, required): The amount to deposit (must be positive, with at most 2 decimal places).

* **Example Success Response (200 OK):**
  ```
  {
    "id":"a40e13cf-0530-4ebd-8ff3-9cbe532e5880",
    "message":"Deposit successful. New balance for account: 1000.00",
    "toAccountId":"fb356bec-4df9-4c1f-b046-b4aeaf4e65b7",
    "amount":"$300.00",
    "type":"DEPOSIT",
    "timestamp":"2025-03-31T15:21:39.285774"
  }
  ```
* **`curl` Example:**
  ```
  curl -X POST http://localhost:8080/api/accounts/deposit -H "Content-Type: application/json" -d '{ "toAccountId": "fb356bec-4df9-4c1f-b046-b4aeaf4e65b7", "amount": 300.00 }'
  ```

### 6. Withdraw Funds

* **Method:** `POST`
* **Path:** `/api/withdraw`
* **Description:** Withdraws a specified amount from the chosen account.
* **Example Request Body:**
  ```
  {
    "fromAccountId": "fb356bec-4df9-4c1f-b046-b4aeaf4e65b7",
    "amount": 300.00
  }
  ```
    * `fromAccountId` (UUID, required): The ID of the account to withdraw from.
    * `amount` (BigDecimal, required): The amount to withdraw (must be positive, with at most 2 decimal places).

* **Example Success Response (200 OK):**
  ```
  {
    "id":"a40e13cf-0530-4ebd-8ff3-9cbe532e5880",
    "message":"Withdrawal successful. New balance for account: 700.00",
    "fromAccountId":"fb356bec-4df9-4c1f-b046-b4aeaf4e65b7",
    "amount":"$300.00",
    "type":"WITHDRAWAL",
    "timestamp":"2025-03-31T15:21:39.285774"
  }
  ```
* **`curl` Example:**
  ```
  curl -X POST http://localhost:8080/api/accounts/withdraw -H "Content-Type: application/json" -d '{ "fromAccountId": "fb356bec-4df9-4c1f-b046-b4aeaf4e65b7", "amount": 300.00 }'
  ```