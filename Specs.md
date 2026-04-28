# EXPENSE TRACKER



## PROBLEM
Many individuals struggle with managing their finances, this is due to lack of understanding on how they spend their money, when an individual understand their spending, they will have a bigger picture of their finance in terms of measuring their budget or income to their spending.

## GOAL
Expense tracker is an application that will help the user to tracker their expenses, monitor their budget, categorise their spending, view summary of their expenses based category and timeline. The user should be able to create expenses, view their expenses, edit an expense, delete an expense, add a monthly budget limit to track when they are close or over their limit, view summary and insights of their spending habits.

## USER
Any individual who want to track their expenses or spending habits.

## REQUIREMENTS

* User should be able to register and create an account.
* User should be able to log in into their account.
* User should be able to log out of their account.
* User should be able to create, view, update, and delete an expense in their account.
* User should be able to create, view, update and delete a category in their account.
* User should be able to filter expenses by category, timeline, and amount range.
* User should be able to summarise expenses by category and timeline.
* An expense should have a category.
* User should be able to set daily, weekly, monthly, and yearly budget.

## 🧩 User Stories (WHAT the user wants)

### 🔐 Authentication

- As a user, I want to register an account so that I can securely manage my expenses.
- As a user, I want to log in to my account so that I can access my expense data.
- As a user, I want to log out so that my account remains secure.

### 💸 Expense Management

- As a user, I want to create an expense so that I can track my spending.
- As a user, I want to view my expenses so that I can see where my money is going.
- As a user, I want to update an expense so that I can correct mistakes.
- As a user, I want to delete an expense so that I can remove incorrect entries.

### 🏷️ Category Management

- As a user, I want to create categories so that I can organise my expenses.
- As a user, I want to update categories so that I can maintain accurate classifications.
- As a user, I want to delete categories so that I can remove unused ones.

### 🔍 Filtering \& Insights

- As a user, I want to filter expenses by category, date, and amount so that I can analyse my spending.
- As a user, I want to view summaries of my expenses so that I can understand my spending habits.

💰 Budget Management

- As a user, I want to set a budget so that I can control my spending.
- As a user, I want to be notified when I approach or exceed my budget so that I can adjust my spending.

## 🧠 Use Cases (Detailed System Behaviour)

### 🔵 Use Case 1: Register User

**Actor: User**

**Main Flow**

- User enters email and password
- System validates input
- System checks if email already exists
- System encrypts password
- System saves user
- System returns success response

***Edge Cases***

- Email already exists
- Invalid email format
- Weak password

### 🔵 Use Case 2: Login User

**Actor: User**

**Main Flow**

- User enters email and password
- System validates input
- System verifies credentials
- System generates JWT token
- System returns token

***Edge Cases***

- Invalid credentials
- User not found

### 🔵 Use Case 3: Create Expense (IMPORTANT CORE FLOW)

**Actor: Authenticated User**

**Main Flow**

- User submits:
  - amount
  - category
  - description
  - date
- System validates input
- System checks category exists
- System creates expense
- System associates expense with user
- System saves expense
- System returns created expense

Edge Cases:

- Amount ≤ 0
- Category does not exist
- Missing required fields
- Unauthorized request

### 🔵 Use Case 4: Update Expense
**Actor: Authenticated User**

**Main Flow**

- User selects expense
- User updates fields
- System validates input
- System checks ownership
- System updates expense
- System saves changes
- System returns updated expense

***Edge Cases***

- Expense not found
- Unauthorized access
- Invalid data

### 🔵 Use Case 5: Delete Expense

**Actor: Authenticated User**

**Main Flow**

- User selects expense
- System verifies ownership
- System deletes expense
- System returns success

Edge Cases:

- Expense not found
- Unauthorized deletion

### 🔵 Use Case 6: Create Category

**Actor: Authenticated User**

**Main Flow**

- User enters category name
- System validates input
- System checks uniqueness per user
- System saves category
- System returns created category

***Edge Cases***

- Duplicate category name
- Empty name

### 🔵 Use Case 7: Filter Expenses

**Actor: Authenticated User**

**Main Flow**

- User selects filters:
  - category
  - date range
  - amount range
- System retrieves matching expenses
- System returns filtered results

***Edge Cases***

- No matching results
- Invalid filter values

### 🔵 Use Case 8: View Expense Summary

**Actor: Authenticated User**

**Main Flow**

- User requests summary
- System retrieves user expenses
- System calculates:
  - total spending
  - spending per category
  - spending per time period
- System returns summary

***Edge Cases:***

- No expenses available

### 🔵 Use Case 9: Set Budget

**Actor: Authenticated User**

**Main Flow**

- User selects budget type:
  - daily / weekly / monthly / yearly
- User enters amount
- System validates input
- System saves budget
- System returns confirmation

***Edge Cases***

- Invalid amount
- Negative value

### 🔵 Use Case 10: Budget Monitoring

**Actor: System**

**Main Flow**

- System calculates total spending
- System compares with budget
- If threshold reached:
- trigger notification (log/event)
- System continues monitoring



