## **Phase 1: Loan Management System Detailed Overview**

### **Objectives**

#### **1. Develop Core Loan Management Features**

- **User Registration and Authentication:**
  - **Objective:** Implement user registration, login, and account management processes.
  - **Details:** Users can create accounts, log in, and manage their profiles.
  - **Technologies:** Spring Boot, PostgreSQL, JWT

- **Loan Application Management:**
  - **Objective:** Enable users to apply for loans, view application statuses, and manage loan details.
  - **Details:** Users can submit loan applications, track status, and manage their loans.
  - **Technologies:** Spring Boot, PostgreSQL

- **Loan Product Management:**
  - **Objective:** Allow admins to create, update, and manage loan products.
  - **Details:** Admins can define loan types, interest rates, and terms.
  - **Technologies:** Spring Boot, PostgreSQL

- **Document Management:**
  - **Objective:** Implement functionality for document upload, verification, and tracking.
  - **Details:** Users upload documents; admins verify and manage them.
  - **Technologies:** Spring Boot, AWS S3, PostgreSQL

- **User Profile Management:**
  - **Objective:** Provide users with the ability to update personal details and credentials.
  - **Details:** Users can update contact information, addresses, and passwords.
  - **Technologies:** Spring Boot, PostgreSQL

#### **2. Create a User-Friendly Interface**

- **User Interface Design:**
  - **Objective:** Develop a modern, intuitive web interface for user interactions.
  - **Details:** User-friendly forms and navigation for loan applications and profile management.
  - **Technologies:** React, HTML/CSS

- **Admin Dashboard:**
  - **Objective:** Build an admin interface for managing loan products and applications.
  - **Details:** Dashboard for admins to manage products, view applications, and generate reports.
  - **Technologies:** React, HTML/CSS

#### **3. Implement Core System Features**

- **Real-Time Loan Application Status Tracking:**
  - **Objective:** Provide real-time updates on loan application statuses.
  - **Details:** Users receive updates on their loan applications.
  - **Technologies:** WebSocket, Spring Boot

- **Basic Reporting:**
  - **Objective:** Generate reports for loan applications and user statistics.
  - **Details:** Basic reports for loan metrics and user information.
  - **Technologies:** Spring Boot, PDF libraries

- **Modular Architecture:**
  - **Objective:** Establish a scalable and modular system architecture.
  - **Details:** Modular design for future feature expansion and improvements.
  - **Technologies:** Microservices, Spring Boot



### **1. API Endpoints**

#### **Customer Endpoints**

| **Category**                | **Endpoint**                                  | **Method** | **Path**                                  | **Description**                                          |
|-----------------------------|----------------------------------------------|------------|-------------------------------------------|----------------------------------------------------------|
| **User Registration**      | Register                                    | POST       | `/api/users/register`                     | Register a new user account.                            |
| **User Authentication**    | Login                                        | POST       | `/api/users/login`                        | Authenticate a user and provide a JWT token.           |
| **User Profile Management**| Update User Details                         | PUT        | `/api/users/{userId}/update`              | Update personal details like name, phone, etc.         |
|                             | Update Credentials                           | PUT        | `/api/users/{userId}/update-credentials` | Update email or username credentials.                 |
|                             | Reset Password                               | POST       | `/api/users/{userId}/reset-password`     | Request a password reset.                              |
| **Loan Application**       | Apply for a Loan                              | POST       | `/api/loans/apply`                         | Submit a new loan application.                         |
|                             | View Loan Application                        | GET        | `/api/loans/{loanId}`                      | View the details of a specific loan application.       |
|                             | View All Loans                               | GET        | `/api/loans`                              | List all loan applications submitted by the user.      |
| **Document Management**    | Upload Document                              | POST       | `/api/documents/upload`                    | Upload required documents for loan applications.       |
|                             | Get Document                                 | GET        | `/api/documents/{documentId}`             | Retrieve a specific document.                          |
|                             | List Documents                               | GET        | `/api/documents`                           | List all documents related to a specific loan.         |
| **Loan Status Tracking**    | Get Document Status                          | GET        | `/api/documents/{documentId}/status`     | Check the status of a specific document.               |
| **Customer Profiling**      | Get Customer Profile                         | GET        | `/api/users/{userId}/profile`             | Retrieve the profile details and risk classification of a customer. |
|                             | Get Customer Loan History                     | GET        | `/api/users/{userId}/loan-history`       | Retrieve the loan history of a customer.               |

#### **Admin Endpoints**

| **Category**                   | **Endpoint**                                | **Method** | **Path**                                  | **Description**                                            |
|-------------------------------|--------------------------------------------|------------|-------------------------------------------|------------------------------------------------------------|
| **Loan Product Management**  | Create Loan Product                        | POST       | `/api/loans/products`                     | Create a new loan product.                               |
|                               | Update Loan Product                        | PUT        | `/api/loans/products/{productId}`        | Update details of an existing loan product.             |
|                               | Delete Loan Product                        | DELETE     | `/api/loans/products/{productId}`        | Delete a specific loan product.                          |
|                               | Get All Loan Products                       | GET        | `/api/loans/products`                     | List all loan products available in the system.         |
| **Loan Application Management** | View All Loan Applications                | GET        | `/api/loans/applications`                | View all loan applications submitted by users.          |
| **Document Management**      | Verify Document                             | POST       | `/api/documents/verify`                   | Admin verifies the uploaded documents.                 |
|                               | List Documents                              | GET        | `/api/documents`                           | List all documents for a specific loan or user.          |
| **Document Requirements Management** | Get Document Requirements          | GET        | `/api/loans/{loanId}/requirements`       | Retrieve document requirements for a specific loan.     |
|                               | Update Document Requirements               | PUT        | `/api/loans/{loanId}/requirements`       | Update the document requirements for a specific loan.   |
| **Support Management**       | View Support Requests                        | GET        | `/api/support/requests`                   | View all support requests submitted by users.           |
|                               | Respond to Support Request                   | POST       | `/api/support/requests/{requestId}/respond` | Admin responds to support requests.                     |
| **Report Generation**        | Generate Profit/Loss Report                   | GET        | `/api/reports/profit-loss`                | Generate a report showing profits and losses.            |
|                               | Generate Model Performance Report           | GET        | `/api/reports/model-performance`         | Generate a report on the performance of different models.|
| **Analytics and Monitoring**  | Track Model Performance                      | GET        | `/api/analytics/model-performance`       | Track and review the performance of various models.      |
|                               | Get Customer Portfolio Overview             | GET        | `/api/admin/customers/portfolio-overview`| View overall customer portfolio distribution.           |
|                               | Get Individual Customer Portfolio          | GET        | `/api/admin/customers/{userId}/portfolio`| View detailed customer portfolio for a specific user.   |

### **2. Models and Attributes**

| **Model**              | **Attributes**                                          | **Description**                                   |
|-----------------------|---------------------------------------------------------|---------------------------------------------------|
| **User**              | `userId`, `name`, `email`, `password`, `phone`, `address`, `role`, `createdAt`, `creditScore`, `portfolioCategory` | Represents a user in the system.                 |
| **LoanProduct**       | `productId`, `name`, `description`, `interestRate`, `loanTerm`, `maxAmount`, `minAmount`, `eligibilityCriteria`, `createdAt` | Represents a loan product offered by the bank.   |
| **LoanApplication**   | `loanId`, `userId`, `productId`, `amount`, `status`, `applicationDate`, `approvalDate`, `disbursementDate`, `repaymentStatus`, `defaultDate` | Represents a loan application submitted by a user. |
| **Document**          | `documentId`, `userId`, `loanId`, `documentType`, `fileUrl`, `status`, `uploadedAt` | Represents a document uploaded for loan applications. |
| **DocumentRequirement** | `loanId`, `documentType`, `required`                    | Represents the required documents for a specific loan product. |
| **SupportRequest**    | `requestId`, `userId`, `subject`, `message`, `status`, `submittedAt` | Represents a support request from a user.       |
| **Notification**      | `notificationId`, `userId`, `message`, `timestamp`     | Represents notifications sent to users.         |
| **CustomerProfile**   | `userId`, `creditScore`, `portfolioCategory`, `riskLevel`, `totalLoans`, `totalDefaults`, `totalRepayments`, `averageRepaymentTime`, `defaultRate` | Represents the customer’s profile for risk assessment. |
| **CustomerLoanHistory**| `loanId`, `userId`, `loanProductId`, `amount`, `startDate`, `endDate`, `status`, `defaultDate` | Represents the history of loans taken by a customer. |
| **CustomerPortfolio** | `userId`, `portfolioCategory`, `totalLoans`, `defaultCount`, `successfulRepayments`, `totalAmountBorrowed`, `totalAmountRepaid` | Summarizes the overall loan performance and classification of the customer. |

### **3. Data Collection and Customer Profiling**

**Customer Profiling:**

- **Risk Assessment**:
    - **Credit Score**: Calculated based on the user’s credit history and current financial situation.
    - **Portfolio Category**: Determines if the customer is classified as Poor, Good, or Excellent based on their repayment behavior and loan history.

- **Data Collected**:
    - **Loan Application Data**: Amount applied for, repayment terms, and approval/rejection dates.
    - **Loan Repayment Data**: Timeliness of payments, missed payments, and default dates.
    - **Historical Data**: Previous loans, repayment success, defaults, and average repayment time.

**Customer Portfolio Classification**:
- **Poor**: High default rates, frequent late payments.
- **Good**: Generally on-time with occasional late payments.
- **Excellent**: Consistent timely repayments with no defaults.

**Database Tables for Customer Profiling**:

| **Table Name**       | **Attributes**                                                                                                 |
|---------------------|----------------------------------------------------------------------------------------------------------------|
| **CustomerProfile** | `userId` (Primary Key), `creditScore`, `portfolioCategory`, `riskLevel`, `totalLoans`, `totalDefaults`, `totalRepayments`, `averageRepaymentTime`, `defaultRate` |
| **CustomerLoanHistory** | `loanId`, `userId`, `loanProductId`, `amount`, `startDate`, `endDate`, `status`, `defaultDate`                           |
| **CustomerPortfolio** | `userId`, `portfolioCategory`, `totalLoans`, `defaultCount`, `successfulRepayments`, `totalAmountBorrowed`, `totalAmountRepaid` |

### **4. Data Flow Diagram**

#### **User Flow**

1. **Register** → **Create User** → **JWT Token**
2. **Login** → **Authenticate User** → **JWT Token**
3. **Apply for Loan** → **Submit

Application** → **Review by Admin**
4. **Upload Document** → **Submit Document** → **Verify Document**
5. **Track Loan Status** → **Check Status** → **View Updates**

#### **Admin Flow**

1. **Manage Loan Products** → **Create/Update/Delete Products** → **View Loan Products**
2. **Review Applications** → **Approve/Reject Applications** → **Update Application Status**
3. **Verify Documents** → **Review/Approve Documents** → **Update Document Status**
4. **Generate Reports** → **Create Reports** → **View Profit/Loss & Performance Reports**

### **5. Innovative Features and Future Enhancements**

| **Feature**            | **Description**                                                                                                       | **Benefits**                                              | **Challenges**                                           |
|------------------------|-----------------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------|---------------------------------------------------------|
| **Predictive Analytics** | Using historical data to predict future loan default risks.                                                             | Helps in preemptive risk management and loan approvals.  | Requires accurate data and advanced models.            |
| **Dynamic Risk Assessment** | Adjusting customer profiles and loan terms based on real-time data and analytics.                                          | Provides up-to-date risk assessments.                    | Complexity in real-time data processing and updates.    |
| **Enhanced User Interface** | Streamlined, user-friendly design for ease of navigation and feature access.                                               | Improves user experience and satisfaction.               | Ongoing need for UI/UX improvements and feedback.      |
| **Advanced Portfolio Analytics** | Detailed analysis of customer portfolios to identify trends and improve loan offerings.                                 | Enables better decision-making for loan products.        | Requires sophisticated analytics and reporting tools.   |
| **Automated Loan Processing** | Implementing automation for loan approvals and document verification.                                                         | Reduces manual work and speeds up the loan process.      | High implementation cost and maintenance.              |

### **6. Reference Table**

| **Endpoint Group**    | **Endpoints**                                                                                          |
|----------------------|--------------------------------------------------------------------------------------------------------|
| **Customer Endpoints** | User Registration, Authentication, Profile Management, Loan Applications, Document Management, Loan Status Tracking, Customer Profiling |
| **Admin Endpoints**  | Loan Product Management, Document Verification, Document Requirements, Support Management, Reporting, Analytics, Portfolio Overview |


Detailed table comparing the innovative features of Phase 1 of the Loan Management System (LMS) against current loan management systems in Zimbabwe. The table highlights the key innovations introduced in Phase 1 and compares them to traditional systems and existing digital solutions.

## **Innovative Features Comparison for Phase 1**

| **Innovation Aspect**            | **Phase 1 Feature**                                                | **Description**                                                                                     | **Comparison with Current Systems in Zimbabwe**                                                   |
|----------------------------------|-------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------|
| **Online Loan Application**     | **Fully Online Loan Application and Management System**          | Users can apply for loans, upload documents, and manage applications entirely online.             | **Traditional Systems**: Often require in-person visits for loan applications and paperwork.  |
|                                  |                                                                   |                                                                                                     | **Existing Digital Solutions**: May offer online applications but with limited management features.|
| **Loan Product Customization**   | **Dynamic and Customizable Loan Catalogs for Banks**             | Banks can create and manage various loan products with different terms, interest rates, and criteria. | **Traditional Systems**: Fixed loan products with limited customization.                        |
|                                  |                                                                   |                                                                                                     | **Existing Digital Solutions**: Fixed products with limited flexibility for updates or customization.|
| **Advanced User Profiling**      | **Detailed Customer Profiling and Risk Assessment**             | Tracks loan history, repayment behavior, and categorizes users into risk levels (Poor, Good, Excellent). | **Traditional Systems**: Basic profiling, often without detailed risk assessment.              |
|                                  |                                                                   |                                                                                                     | **Existing Digital Solutions**: Basic loan tracking but limited in detailed risk profiling.     |
| **Real-Time Loan Status Tracking**| **Real-Time Loan Application Status Tracking**                   | Users can view the real-time status of their loan applications, including approval and disbursement stages. | **Traditional Systems**: Users must call or visit the bank for status updates.                   |
|                                  |                                                                   |                                                                                                     | **Existing Digital Solutions**: May have delays or lack real-time updates.                       |
| **Comprehensive Admin Dashboard** | **Advanced Admin Dashboard for Loan Management**                  | A centralized interface for managing loan products, reviewing applications, and generating reports.  | **Traditional Systems**: Admin tasks are often spread across multiple systems or manually managed. |
|                                  |                                                                   |                                                                                                     | **Existing Digital Solutions**: Basic dashboards with limited functionality for analytics and management. |
| **User-Friendly Interface**      | **Modern, User-Friendly Design and Intuitive UI**                 | A modern and intuitive interface for users to apply for loans, manage profiles, and track applications. | **Traditional Systems**: Often outdated interfaces with complex navigation.                        |
|                                  |                                                                   |                                                                                                     | **Existing Digital Solutions**: Some may have modern designs, but many have usability issues.   |
| **Modular Architecture**         | **Scalable and Modular System Architecture for Future Enhancements** | A modular design that supports future upgrades and integration of new features.                     | **Traditional Systems**: Rigid architecture, challenging to extend or upgrade.                   |
|                                  |                                                                   |                                                                                                     | **Existing Digital Solutions**: Some may have modular designs, but many lack flexibility for future enhancements. |
| **Document Management System**    | **Comprehensive Document Management and Tracking**               | Users can upload, track, and manage documents required for loan applications.                       | **Traditional Systems**: Document submission is often manual and tracked via physical or email-based processes. |
|                                  |                                                                   |                                                                                                     | **Existing Digital Solutions**: Limited document management features; often only for uploads.  |
| **Support and Communication**    | **Efficient Support Request Handling and Management**            | Users can submit support requests and receive responses through the platform.                        | **Traditional Systems**: Support requests are often handled via phone or in-person.               |
|                                  |                                                                   |                                                                                                     | **Existing Digital Solutions**: Basic support features, often lacking efficient request management.|
| **Data Collection and Analytics** | **Advanced Data Collection and Predictive Analytics**            | Collects data on loan applications, customer behavior, and performs predictive analytics for risk assessment. | **Traditional Systems**: Limited data collection and basic reporting.                            |
|                                  |                                                                   |                                                                                                     | **Existing Digital Solutions**: Basic analytics without advanced predictive models.             |

### **Summary Table of Innovations**

| **Innovation Aspect**            | **Description**                                                                                     | **Traditional Systems**                                                                 | **Existing Digital Solutions**                                                |
|----------------------------------|-----------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------|---------------------------------------------------------------------------------|
| **Online Loan Application**     | Users can manage loan applications and documents entirely online.                                  | Requires in-person visits for applications and document submissions.                 | Some online applications but often with limited management features.           |
| **Loan Product Customization**   | Customizable loan products with varying terms and interest rates.                                  | Fixed loan products with limited customization.                                      | Limited customization options for loan products.                               |
| **Advanced User Profiling**      | Detailed profiling with risk assessments and portfolio classifications.                            | Basic user profiles and loan tracking.                                              | Basic profiling with minimal risk assessment.                                |
| **Real-Time Loan Status Tracking**| Users get real-time updates on loan application status.                                            | Updates require manual checking via phone or in-person visits.                        | Some may have updates, but real-time tracking is not always available.         |
| **Comprehensive Admin Dashboard** | Centralized dashboard for managing products, applications, and reports.                           | Admin tasks managed across multiple systems or manually.                              | Basic dashboards with limited features for analytics and management.           |
| **User-Friendly Interface**      | Modern and intuitive design for a seamless user experience.                                         | Often outdated interfaces with complex navigation.                                   | Some have modern designs, but usability issues are common.                      |
| **Modular Architecture**         | Flexible and scalable architecture for future enhancements.                                        | Rigid systems with challenges for extensions and upgrades.                             | Modular designs are available but may lack flexibility for future growth.      |
| **Document Management System**    | Advanced document management including uploads, tracking, and status updates.                    | Manual document handling with physical or email-based processes.                      | Basic document management features for uploads only.                           |
| **Support and Communication**    | Efficient handling of support requests through the platform.                                       | Support requests handled via phone or in-person.                                      | Basic support features, often lacking efficient request management.            |
| **Data Collection and Analytics** | Collection of comprehensive data for predictive analytics and risk assessment.                     | Limited data collection with basic reporting.                                        | Basic analytics and reporting without advanced predictive capabilities.        |

### **References for Comparison**

- **Traditional Systems**: Examples include the loan application processes at local Zimbabwean banks (e.g., CBZ, Stanbic, FBC).
- **Existing Digital Solutions**: Review of digital loan platforms and banking apps available in Zimbabwe (e.g., ZB Bank Mobile App, EcoCash Loans).

This table provides a clear comparison of how Phase 1’s innovative features offer improvements over existing systems and solutions in Zimbabwe.