# FSE Backend - Financial Services Engine

A comprehensive Spring Boot-based backend application for financial services, providing investment management, fund scheme administration, algorithmic trading strategies, and backtesting capabilities.

## 🚀 Features

### Core Functionality
- **User Authentication & Authorization** - JWT-based authentication with role-based access control
- **Investment Management** - Complete investment lifecycle management for investors
- **Fund Scheme Administration** - AMC and Fund Manager operations
- **Algorithmic Trading** - Strategy creation, execution, and backtesting
- **Portfolio Management** - Real-time portfolio tracking and NAV calculations
- **KYC Management** - Investor verification and compliance
- **Market Data Integration** - Yahoo Finance data import and processing
- **Company Explorer** - Comprehensive company and index management
- **Statistics & Analytics** - Strategy and backtest result analytics

### User Roles
- **INVESTOR** - Individual investors managing their portfolios
- **MANAGER** - Fund managers overseeing investment strategies
- **AMC** - Asset Management Company administrators

### Fund Scheme Types
- **EQUITY** - Equity-focused investment schemes
- **DEBT** - Fixed income and debt instruments
- **HYBRID** - Balanced equity-debt portfolios
- **ELSS** - Tax-saving equity-linked savings schemes
- **INDEX** - Index-tracking passive funds

### Risk Levels
- **LOW** - Conservative investment approach
- **MODERATE** - Balanced risk-return profile
- **HIGH** - Aggressive growth strategies

### Fund Scheme Status
- **ACTIVE** - Currently accepting investments
- **CLOSED** - Not accepting new investments
- **MERGED** - Merged with another scheme

## 🚀 Features

### Core Functionality
- **User Authentication & Authorization** - JWT-based authentication with role-based access control
- **Investment Management** - Complete investment lifecycle management for investors
- **Fund Scheme Administration** - AMC and Fund Manager operations
- **Algorithmic Trading** - Strategy creation, execution, and backtesting
- **Portfolio Management** - Real-time portfolio tracking and NAV calculations
- **KYC Management** - Investor verification and compliance
- **Market Data Integration** - Yahoo Finance data import and processing
- **Company Explorer** - Comprehensive company and index management
- **Statistics & Analytics** - Strategy and backtest result analytics

### User Roles
- **INVESTOR** - Individual investors managing their portfolios
- **MANAGER** - Fund managers overseeing investment strategies
- **AMC** - Asset Management Company administrators

### Fund Scheme Types
- **EQUITY** - Equity-focused investment schemes
- **DEBT** - Fixed income and debt instruments
- **HYBRID** - Balanced equity-debt portfolios
- **ELSS** - Tax-saving equity-linked savings schemes
- **INDEX** - Index-tracking passive funds

### Risk Levels
- **LOW** - Conservative investment approach
- **MODERATE** - Balanced risk-return profile
- **HIGH** - Aggressive growth strategies

### Fund Scheme Status
- **ACTIVE** - Currently accepting investments
- **CLOSED** - Not accepting new investments
- **MERGED** - Merged with another scheme

## 🛠️ Technology Stack

- **Framework**: Spring Boot 3.5.3
- **Language**: Java 21
- **Database**: PostgreSQL
- **Security**: Spring Security with JWT
- **Build Tool**: Gradle
- **Testing**: JUnit 5 with Spring Test
- **Code Coverage**: JaCoCo
- **Web Scraping**: JSoup
- **JSON Processing**: Jackson
- **Validation**: Jakarta Validation
- **ORM**: Hibernate/JPA
- **CORS**: Cross-Origin Resource Sharing enabled
- **Validation**: Jakarta Validation
- **ORM**: Hibernate/JPA
- **CORS**: Cross-Origin Resource Sharing enabled

## 📋 Prerequisites

- Java 21 or higher
- PostgreSQL 12 or higher
- Gradle 8.0 or higher

## 🚀 Quick Start

### 1. Database Setup

Create a PostgreSQL database named `fseDB`:

```sql
CREATE DATABASE fseDB;
```

### 2. Configuration

Update `src/main/resources/application.properties` with your database credentials:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/fseDB
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 3. Build and Run

```bash
# Clone the repository
git clone <repository-url>
cd FSE_Backend

# Build the project
./gradlew build

# Run the application
./gradlew bootRun
```

The application will start on `http://localhost:8080`

## 📚 API Documentation

### Authentication Endpoints

#### Register User
```http
POST /api/auth/register
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "password123",
  "userRole": "INVESTOR"
}
```

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "john@example.com",
  "password": "password123"
}
```

#### Forgot Password
```http
POST /api/auth/forgot-password
Content-Type: application/json

{
  "email": "john@example.com",
  "newPassword": "newpassword123"
}
```

### AMC (Asset Management Company) Endpoints

#### Create AMC
```http
POST /api/amcs/create/{userId}
Authorization: Bearer <jwt-token>
Content-Type: application/json

{
  "name": "ABC Asset Management",
  "registrationNumber": "AMC123456",
  "address": "123 Financial District, Mumbai",
  "contactNumber": "+91-9876543210",
  "email": "contact@abcamc.com"
}
```

#### Get AMC by ID
```http
GET /api/amcs/{id}
Authorization: Bearer <jwt-token>
```

#### Get All AMCs
```http
GET /api/amcs
Authorization: Bearer <jwt-token>
```

#### Update AMC
```http
PUT /api/amcs/{id}
Authorization: Bearer <jwt-token>
Content-Type: application/json

{
  "name": "Updated ABC Asset Management",
  "registrationNumber": "AMC123456",
  "address": "456 Financial District, Mumbai",
  "contactNumber": "+91-9876543210",
  "email": "contact@abcamc.com"
}
```

#### Delete AMC
```http
DELETE /api/amcs/{id}
Authorization: Bearer <jwt-token>
```

#### Check AMC Exists
```http
GET /api/amcs/exists/{id}
Authorization: Bearer <jwt-token>
```

### Investor Endpoints

#### Create Investor Profile
```http
POST /api/investors/create
Authorization: Bearer <jwt-token>
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com",
  "panNumber": "ABCDE1234F",
  "phoneNumber": "+91-9876543210"
}
```

#### Invest in Fund Scheme
```http
POST /api/investors/invest
Authorization: Bearer <jwt-token>
Content-Type: application/json

{
  "investorId": "uuid",
  "schemeId": "uuid",
  "amount": 10000.00
}
```

#### Redeem from Fund Scheme
```http
POST /api/investors/redeem
Authorization: Bearer <jwt-token>
Content-Type: application/json

{
  "investorId": "uuid",
  "schemeId": "uuid",
  "amount": 5000.00
}
```

#### Get Portfolio
```http
GET /api/investors/portfolio/{investorId}
Authorization: Bearer <jwt-token>
```

#### Get Transactions
```http
GET /api/investors/transactions/{investorId}
Authorization: Bearer <jwt-token>
```

#### Get Transactions by Scheme
```http
GET /api/investors/transactions/{investorId}/scheme/{schemeId}
Authorization: Bearer <jwt-token>
```

#### Get NAV History
```http
GET /api/investors/nav-history/{schemeId}
Authorization: Bearer <jwt-token>
```

#### Get Latest NAV
```http
GET /api/investors/nav-latest/{schemeId}
Authorization: Bearer <jwt-token>
```

#### Get Investor Profile
```http
GET /api/investors/profile/{investorId}
Authorization: Bearer <jwt-token>
```

#### Get KYC Status
```http
GET /api/investors/kyc-status/{investorId}
Authorization: Bearer <jwt-token>
```

#### Verify KYC
```http
POST /api/investors/kyc/verify
Authorization: Bearer <jwt-token>
Content-Type: application/json

{
  "investorId": "uuid",
  "kycStatus": true,
  "kycDocUrl": "https://example.com/kyc-doc.pdf"
}
```

#### Get Investment Summary
```http
GET /api/investors/summary/{investorId}
Authorization: Bearer <jwt-token>
```

#### Get Fund Summary
```http
GET /api/investors/fund-summary/{investorId}
Authorization: Bearer <jwt-token>
```

#### Get Available Schemes
```http
GET /api/investors/available-schemes
Authorization: Bearer <jwt-token>
```

#### Get Wallet Value
```http
GET /api/investors/wallet-value/{investorId}
Authorization: Bearer <jwt-token>
```

#### Check Investor Exists
```http
GET /api/investors/exists/{investorId}
Authorization: Bearer <jwt-token>
```

#### Get Investor Profile
```http
GET /api/investors/profile/{investorId}
Authorization: Bearer <jwt-token>
```

#### Get KYC Status
```http
GET /api/investors/kyc-status/{investorId}
Authorization: Bearer <jwt-token>
```

#### Verify KYC
```http
POST /api/investors/kyc/verify
Authorization: Bearer <jwt-token>
Content-Type: application/json

{
  "investorId": "uuid",
  "kycStatus": true,
  "kycDocUrl": "https://example.com/kyc-doc.pdf"
}
```

#### Get Investment Summary
```http
GET /api/investors/summary/{investorId}
Authorization: Bearer <jwt-token>
```

#### Get Fund Summary
```http
GET /api/investors/fund-summary/{investorId}
Authorization: Bearer <jwt-token>
```

#### Get Available Schemes
```http
GET /api/investors/available-schemes
Authorization: Bearer <jwt-token>
```

#### Get Wallet Value
```http
GET /api/investors/wallet-value/{investorId}
Authorization: Bearer <jwt-token>
```

#### Check Investor Exists
```http
GET /api/investors/exists/{investorId}
Authorization: Bearer <jwt-token>
```

### AMC (Asset Management Company) Endpoints

#### Create AMC
```http
POST /api/amcs/create/{userId}
Authorization: Bearer <jwt-token>
Content-Type: application/json

{
  "name": "ABC Asset Management",
  "registrationNumber": "AMC123456",
  "address": "123 Financial District, Mumbai",
  "contactNumber": "+91-9876543210",
  "email": "contact@abcamc.com"
}
```

#### Get AMC by ID
```http
GET /api/amcs/{id}
Authorization: Bearer <jwt-token>
```

#### Get All AMCs
```http
GET /api/amcs
Authorization: Bearer <jwt-token>
```

#### Update AMC
```http
PUT /api/amcs/{id}
Authorization: Bearer <jwt-token>
Content-Type: application/json

{
  "name": "Updated ABC Asset Management",
  "registrationNumber": "AMC123456",
  "address": "456 Financial District, Mumbai",
  "contactNumber": "+91-9876543210",
  "email": "contact@abcamc.com"
}
```

#### Delete AMC
```http
DELETE /api/amcs/{id}
Authorization: Bearer <jwt-token>
```

#### Check AMC Exists
```http
GET /api/amcs/exists/{id}
Authorization: Bearer <jwt-token>
```

### Fund Scheme Endpoints

#### Create Fund Scheme
```http
POST /api/fund-schemes/create/{amcId}
Authorization: Bearer <jwt-token>
Content-Type: application/json

{
  "name": "Growth Fund",
  "type": "EQUITY",
  "objective": "Long-term capital appreciation",
  "aum": 100000000.00,
  "currentNav": 15.50,
  "riskLevel": "HIGH",
  "expenseRatio": 1.5,
  "exitLoad": 1.0,
  "lockInPeriod": 365,
  "minInvestment": 5000.00,
  "minSipAmount": 1000.00,
  "benchmarkIndex": "NIFTY 50",
  "launchDate": "2020-01-01",
  "category": "Large Cap",
  "status": "ACTIVE"
}
```

#### Get Fund Scheme by ID
```http
GET /api/fund-schemes/{id}
Authorization: Bearer <jwt-token>
```

#### Get All Fund Schemes
```http
GET /api/fund-schemes
Authorization: Bearer <jwt-token>
```

#### Get Schemes by AMC
```http
GET /api/fund-schemes/amc/{amcId}
Authorization: Bearer <jwt-token>
```

#### Update Fund Scheme
```http
PUT /api/fund-schemes/{id}
Authorization: Bearer <jwt-token>
Content-Type: application/json

{
  "name": "Updated Growth Fund",
  "type": "EQUITY",
  "objective": "Updated objective",
  "aum": 150000000.00,
  "currentNav": 16.50,
  "riskLevel": "HIGH",
  "expenseRatio": 1.5,
  "exitLoad": 1.0,
  "lockInPeriod": 365,
  "minInvestment": 5000.00,
  "minSipAmount": 1000.00,
  "benchmarkIndex": "NIFTY 50",
  "launchDate": "2020-01-01",
  "category": "Large Cap",
  "status": "ACTIVE"
}
```

#### Delete Fund Scheme
```http
DELETE /api/fund-schemes/{id}
Authorization: Bearer <jwt-token>
```

#### Assign Fund Manager
```http
POST /api/fund-schemes/{schemeId}/assign-manager
Authorization: Bearer <jwt-token>
Content-Type: application/json

{
  "managerId": "uuid"
}
```

### Fund Manager Endpoints

#### Create Fund Manager
```http
POST /api/fundManagers/create
Authorization: Bearer <jwt-token>
Content-Type: application/json

{
  "employeeCode": "FM001",
  "qualification": "CFA",
  "experienceYears": 8,
  "bio": "Experienced fund manager with expertise in equity markets",
  "userId": "uuid"
}
```

#### Get Fund Manager by ID
```http
GET /api/fundManagers/{id}
Authorization: Bearer <jwt-token>
```

#### Get All Fund Managers
```http
GET /api/fundManagers
Authorization: Bearer <jwt-token>
```

#### Update Fund Manager
```http
PUT /api/fundManagers/{id}
Authorization: Bearer <jwt-token>
Content-Type: application/json

{
  "employeeCode": "FM001",
  "qualification": "CFA, MBA",
  "experienceYears": 10,
  "bio": "Updated bio with additional experience",
  "userId": "uuid"
}
```

#### Delete Fund Manager
```http
DELETE /api/fundManagers/{id}
Authorization: Bearer <jwt-token>
```

#### Get Schemes by Fund Manager
```http
GET /api/fundManagers/schemes/{id}
Authorization: Bearer <jwt-token>
```

#### Update Scheme
```http
PUT /api/fundManagers/scheme/{id}
Authorization: Bearer <jwt-token>
Content-Type: application/json

{
  "name": "Updated Fund Name",
  "type": "EQUITY",
  "objective": "Updated objective",
  "aum": 150000000.00,
  "currentNav": 16.50,
  "riskLevel": "HIGH",
  "expenseRatio": 1.5,
  "exitLoad": 1.0,
  "lockInPeriod": 365,
  "minInvestment": 5000.00,
  "minSipAmount": 1000.00,
  "benchmarkIndex": "NIFTY 50",
  "launchDate": "2020-01-01",
  "category": "Large Cap",
  "status": "ACTIVE"
}
```

#### Buy Stocks
```http
POST /api/fundManagers/buy/{fundManagerId}
Authorization: Bearer <jwt-token>
Content-Type: application/json

{
  "companyId": 1,
  "companyName": "Reliance Industries",
  "investedAmount": 50000.00,
  "numberOfStocks": 100,
  "investmentDate": "2024-01-15",
  "fundSchemeId": "uuid"
}
```

#### Sell Stocks
```http
POST /api/fundManagers/sell
Authorization: Bearer <jwt-token>
Content-Type: application/json

{
  "fundSchemeId": "uuid",
  "companyId": 1,
  "stocksToSell": 50
}
```

#### Get Strategy Count
```http
GET /api/fundManagers/strategies
Authorization: Bearer <jwt-token>
```

### Company and Market Data Endpoints

#### Get All Companies
```http
GET /api/companies
Authorization: Bearer <jwt-token>
```

#### Get Company by ID
```http
GET /api/companies/{id}
Authorization: Bearer <jwt-token>
```

#### Get Companies by Index
```http
GET /api/companies/index/{indexName}
Authorization: Bearer <jwt-token>
```

#### Get Companies by Fund Scheme
```http
GET /api/companies/scheme/{schemeId}
Authorization: Bearer <jwt-token>
```

#### Update Company Risk Data
```http
GET /api/companies/update-risk-data
Authorization: Bearer <jwt-token>
```

### Import and Data Management Endpoints

#### Import Index Data
```http
POST /api/import
Authorization: Bearer <jwt-token>
Content-Type: application/json

{
  "indexName": "NIFTY 50"
}
```

#### Get Companies for Index
```http
GET /api/import/companies?index=NIFTY 50
Authorization: Bearer <jwt-token>
```

#### Import Candle Data
```http
POST /api/candles/import
Authorization: Bearer <jwt-token>
Content-Type: application/json

[
  {
    "date": "2024-01-15",
    "symbol": "RELIANCE.NS",
    "open": 2500.00,
    "high": 2550.00,
    "low": 2480.00,
    "close": 2520.00,
    "volume": 1000000
  }
]
```

### Save Strategy Endpoints

#### Save Strategy
```http
POST /api/save-strategies
Authorization: Bearer <jwt-token>
Content-Type: application/json

{
  "name": "Custom Strategy",
  "type": "Moving Average Crossover",
  "capitalAllocation": 100000.00,
  "status": "not started",
  "parametersJson": "{\"shortPeriod\": 10, \"longPeriod\": 20}",
  "resultJson": null
}
```

#### Get All Saved Strategies
```http
GET /api/save-strategies
Authorization: Bearer <jwt-token>
```

#### Get Saved Strategy by ID
```http
GET /api/save-strategies/{id}
Authorization: Bearer <jwt-token>
```

#### Delete Saved Strategy
```http
DELETE /api/save-strategies/{id}
Authorization: Bearer <jwt-token>
```

#### Start Strategy Simulation
```http
POST /api/save-strategies/{id}/start
Authorization: Bearer <jwt-token>
```

#### Stop Strategy Simulation
```http
POST /api/save-strategies/{id}/stop
Authorization: Bearer <jwt-token>
```

#### Complete Strategy Simulation
```http
POST /api/save-strategies/{id}/complete
Authorization: Bearer <jwt-token>
Content-Type: application/json

"{\"initialEquity\": 100000, \"finalEquity\": 105000, \"totalTrades\": 25}"
```

#### Get Strategy Result
```http
GET /api/save-strategies/result/{strategyId}
Authorization: Bearer <jwt-token>
```

### Statistics Endpoints

#### Get Strategy Count
```http
GET /api/statistics/strategies/count
Authorization: Bearer <jwt-token>
```

#### Get Backtest Result Count
```http
GET /api/statistics/backtest-results/count
Authorization: Bearer <jwt-token>
```

### Strategy and Backtesting Endpoints

#### Create Trading Strategy
```http
POST /api/strategies
Authorization: Bearer <jwt-token>
Content-Type: application/json

{
  "name": "Moving Average Crossover",
  "symbol": "RELIANCE.NS",
  "script": "strategy script content",
  "paramsJson": "{\"shortPeriod\": 10, \"longPeriod\": 20}",
  "initialCapital": 100000.00,
  "startDate": "2024-01-01",
  "endDate": "2024-12-31",
  "status": "not started"
}
```

#### Get All Strategies
```http
GET /api/strategies
Authorization: Bearer <jwt-token>
```

#### Get Strategy by ID
```http
GET /api/strategies/{id}
Authorization: Bearer <jwt-token>
```

#### Run Backtest
```http
POST /api/backtest
Authorization: Bearer <jwt-token>
Content-Type: application/json

{
  "strategyId": 1,
  "symbolList": ["RELIANCE.NS", "TCS.NS"],
  "startDate": "2024-01-01",
  "endDate": "2024-12-31",
  "initialCapital": 100000
}
```

#### Get Backtest Results
```http
GET /api/backtest/result/{strategyId}
Authorization: Bearer <jwt-token>
```

#### Get Candle Data for Backtest
```http
POST /api/backtest/candles
Authorization: Bearer <jwt-token>
Content-Type: application/json

{
  "strategyId": 1,
  "symbolList": ["RELIANCE.NS", "TCS.NS"],
  "startDate": "2024-01-01",
  "endDate": "2024-12-31"
}
```

### Save Strategy Endpoints

#### Save Strategy
```http
POST /api/save-strategies
Authorization: Bearer <jwt-token>
Content-Type: application/json

{
  "name": "Custom Strategy",
  "type": "Moving Average Crossover",
  "capitalAllocation": 100000.00,
  "status": "not started",
  "parametersJson": "{\"shortPeriod\": 10, \"longPeriod\": 20}",
  "resultJson": null
}
```

#### Get All Saved Strategies
```http
GET /api/save-strategies
Authorization: Bearer <jwt-token>
```

#### Get Saved Strategy by ID
```http
GET /api/save-strategies/{id}
Authorization: Bearer <jwt-token>
```

#### Delete Saved Strategy
```http
DELETE /api/save-strategies/{id}
Authorization: Bearer <jwt-token>
```

#### Start Strategy Simulation
```http
POST /api/save-strategies/{id}/start
Authorization: Bearer <jwt-token>
```

#### Stop Strategy Simulation
```http
POST /api/save-strategies/{id}/stop
Authorization: Bearer <jwt-token>
```

#### Complete Strategy Simulation
```http
POST /api/save-strategies/{id}/complete
Authorization: Bearer <jwt-token>
Content-Type: application/json

"{\"initialEquity\": 100000, \"finalEquity\": 105000, \"totalTrades\": 25}"
```

#### Get Strategy Result
```http
GET /api/save-strategies/result/{strategyId}
Authorization: Bearer <jwt-token>
```

### Company and Market Data Endpoints

#### Get All Companies
```http
GET /api/companies
Authorization: Bearer <jwt-token>
```

#### Get Company by ID
```http
GET /api/companies/{id}
Authorization: Bearer <jwt-token>
```

#### Get Companies by Index
```http
GET /api/companies/index/{indexName}
Authorization: Bearer <jwt-token>
```

#### Get Companies by Fund Scheme
```http
GET /api/companies/scheme/{schemeId}
Authorization: Bearer <jwt-token>
```

#### Update Company Risk Data
```http
GET /api/companies/update-risk-data
Authorization: Bearer <jwt-token>
```

### Import and Data Management Endpoints

#### Import Index Data
```http
POST /api/import
Authorization: Bearer <jwt-token>
Content-Type: application/json

{
  "indexName": "NIFTY 50"
}
```

#### Get Companies for Index
```http
GET /api/import/companies?index=NIFTY 50
Authorization: Bearer <jwt-token>
```

#### Import Candle Data
```http
POST /api/candles/import
Authorization: Bearer <jwt-token>
Content-Type: application/json

[
  {
    "date": "2024-01-15",
    "symbol": "RELIANCE.NS",
    "open": 2500.00,
    "high": 2550.00,
    "low": 2480.00,
    "close": 2520.00,
    "volume": 1000000
  }
]
```

### Statistics Endpoints

#### Get Strategy Count
```http
GET /api/statistics/strategies/count
Authorization: Bearer <jwt-token>
```

#### Get Backtest Result Count
```http
GET /api/statistics/backtest-results/count
Authorization: Bearer <jwt-token>
```

## 🏗️ Project Structure

```
src/main/java/com/fse/FSE_Backend_Proj/
├── config/                 # Security and configuration
│   └── SecurityConfig.java # JWT and CORS configuration
├── controller/             # REST API controllers
│   ├── AuthController.java           # Authentication endpoints
│   ├── InvestorController.java       # Investor management
│   ├── AMCController.java           # Asset Management Company
│   ├── FundManagerController.java    # Fund Manager operations
│   ├── FundSchemeController.java     # Fund Scheme management
│   ├── StrategyController.java       # Trading strategies
│   ├── BacktestController.java       # Strategy backtesting
│   ├── SaveStrategyController.java   # Saved strategies
│   ├── CompanyController.java        # Company management
│   ├── CompanyExplorerController.java # Company exploration
│   ├── ImportController.java         # Data import
│   ├── CandleImportController.java   # Candle data import
│   └── StatisticsController.java     # Analytics and statistics
├── dto/                   # Data Transfer Objects
│   ├── authDto/           # Authentication DTOs
│   ├── investorDto/       # Investor-related DTOs
│   ├── fundManagerDto/    # Fund Manager DTOs
│   ├── fundSchemeDto/     # Fund Scheme DTOs
│   ├── amcDto/            # AMC DTOs
│   └── strategyDto/       # Strategy DTOs
├── engine/                # Trading strategy engine
│   ├── builtin/          # Built-in strategies
│   │   ├── BuyAndHoldStrategy.java
│   │   ├── DayOfWeekStrategy.java
│   │   ├── MovingAverageCrossover.java
│   │   ├── RSIStrategy.java
│   │   ├── ThresholdBasedStrategy.java
│   │   └── VolumeSurgeStrategy.java
│   ├── dsl/              # Domain-specific language
│   │   └── DSLStrategyExecutor.java
│   ├── StrategyEngine.java
│   └── StrategyExecutor.java
├── exception/             # Custom exceptions
│   ├── DuplicatePanException.java
│   └── ResourceNotFoundException.java
├── indicator/             # Technical indicators
│   └── IndicatorService.java
├── model/                 # Entity models
│   ├── enums/            # Enumerations
│   │   ├── BacktestStatus.java
│   │   ├── FundSchemeStatus.java
│   │   ├── FundSchemeType.java
│   │   ├── RiskLevel.java
│   │   ├── TradeAction.java
│   │   ├── TransactionType.java
│   │   ├── UserRole.java
│   │   └── UserStatus.java
│   ├── User.java
│   ├── Investor.java
│   ├── AMC.java
│   ├── FundManager.java
│   ├── FundScheme.java
│   ├── Company.java
│   ├── CompanyInvestment.java
│   ├── Strategy.java
│   ├── SaveStrategy.java
│   ├── BacktestResult.java
│   ├── Candle.java
│   ├── Trade.java
│   ├── Transaction.java
│   ├── NAVHistory.java
│   └── UnitLedger.java
├── parser/                # Data parsing utilities
│   └── ScriptParserService.java
├── repository/            # Data access layer
│   ├── UserRepository.java
│   ├── InvestorRepository.java
│   ├── AMCRepository.java
│   ├── FundManagerRepository.java
│   ├── FundSchemeRepository.java
│   ├── CompanyRepository.java
│   ├── StrategyRepository.java
│   ├── SaveStrategyRepository.java
│   ├── BacktestResultRepository.java
│   ├── CandleRepository.java
│   ├── TransactionRepository.java
│   ├── NAVHistoryRepository.java
│   └── UnitLedgerRepository.java
├── service/               # Business logic layer
│   ├── impl/             # Service implementations
│   │   ├── AMCServiceImpl.java
│   │   ├── FundManagerServiceImpl.java
│   │   └── FundSchemeServiceImpl.java
│   ├── UserService.java
│   ├── InvestorService.java
│   ├── AMCService.java
│   ├── FundManagerService.java
│   ├── FundSchemeService.java
│   ├── CompanyService.java
│   ├── CompanyExplorerService.java
│   ├── StrategyService.java
│   ├── SaveStrategyService.java
│   ├── ImportService.java
│   ├── YahooFinanceService.java
│   └── CandleGraphService.java
└── util/                  # Utility classes
    ├── JWTAuthFilter.java
    ├── JWTUtil.java
    ├── MathUtil.java
    └── YahooFinanceParser.java
```

## 🔧 Built-in Trading Strategies

The application includes several pre-built trading strategies:

### 1. Buy and Hold Strategy
- **Description**: Long-term investment strategy
- **Logic**: Buy stocks and hold them for the entire period
- **Use Case**: Conservative, long-term investment approach

### 2. Moving Average Crossover Strategy
- **Description**: Technical analysis based on moving averages
- **Logic**: Buy when short-term MA crosses above long-term MA, sell when it crosses below
- **Parameters**: Short period (10), Long period (20)
- **Use Case**: Trend-following strategy

### 3. RSI Strategy
- **Description**: Relative Strength Index based trading
- **Logic**: Buy when RSI is oversold (< 30), sell when overbought (> 70)
- **Parameters**: RSI period (14), oversold threshold (30), overbought threshold (70)
- **Use Case**: Mean reversion strategy

### 4. Day of Week Strategy
- **Description**: Calendar-based trading patterns
- **Logic**: Trade based on specific days of the week
- **Parameters**: Target days, buy/sell signals
- **Use Case**: Calendar effect exploitation

### 5. Volume Surge Strategy
- **Description**: Volume-based trading signals
- **Logic**: Buy when volume is significantly above average
- **Parameters**: Volume threshold multiplier
- **Use Case**: Momentum trading

### 6. Threshold Based Strategy
- **Description**: Custom threshold-based rules
- **Logic**: Buy/sell based on price thresholds
- **Parameters**: Upper and lower price thresholds
- **Use Case**: Range-bound trading

## 📊 Database Schema

### Core Entities

#### User
- **id** (UUID): Primary key
- **name** (String): User's full name
- **email** (String): Unique email address
- **passwordHash** (String): Encrypted password
- **role** (UserRole): INVESTOR, MANAGER, AMC
- **phone** (String): Contact number
- **status** (UserStatus): ACTIVE, INACTIVE
- **createdAt** (LocalDateTime): Account creation timestamp
- **updatedAt** (LocalDateTime): Last update timestamp

#### Investor
- **id** (String): Foreign key to User
- **kycStatus** (boolean): KYC verification status
- **kycDocUrl** (String): KYC document URL
- **dob** (LocalDate): Date of birth
- **panNumber** (String): PAN card number
- **address** (String): Residential address
- **guardianName** (String): Guardian's name
- **occupation** (String): Professional occupation
- **annualIncome** (BigDecimal): Annual income
- **nomineeName** (String): Nominee's name
- **bankAccountNo** (String): Bank account number
- **ifscCode** (String): IFSC code
- **walletBalance** (double): Available balance
- **fundScheme** (FundScheme): Associated fund scheme

#### AMC (Asset Management Company)
- **id** (UUID): Primary key
- **name** (String): Company name
- **registrationNumber** (String): SEBI registration number
- **address** (String): Registered address
- **contactNumber** (String): Contact number
- **email** (String): Contact email
- **user** (User): Associated user account

#### FundManager
- **id** (String): Primary key
- **employeeCode** (String): Employee identification
- **qualification** (String): Professional qualifications
- **experienceYears** (int): Years of experience
- **bio** (String): Professional biography
- **user** (User): Associated user account

#### FundScheme
- **id** (UUID): Primary key
- **name** (String): Scheme name
- **type** (FundSchemeType): EQUITY, DEBT, HYBRID, ELSS, INDEX
- **objective** (String): Investment objective
- **aum** (BigDecimal): Assets Under Management
- **currentNav** (BigDecimal): Current Net Asset Value
- **navUpdatedAt** (LocalDateTime): NAV update timestamp
- **riskLevel** (RiskLevel): LOW, MODERATE, HIGH
- **expenseRatio** (BigDecimal): Expense ratio percentage
- **exitLoad** (BigDecimal): Exit load percentage
- **lockInPeriod** (Integer): Lock-in period in days
- **minInvestment** (BigDecimal): Minimum investment amount
- **minSipAmount** (BigDecimal): Minimum SIP amount
- **benchmarkIndex** (String): Benchmark index name
- **launchDate** (LocalDate): Scheme launch date
- **category** (String): Fund category
- **status** (FundSchemeStatus): ACTIVE, CLOSED, MERGED
- **amc** (AMC): Associated AMC
- **manager** (FundManager): Fund manager

#### Company
- **id** (Long): Primary key
- **name** (String): Company name
- **symbol** (String): Stock symbol
- **sector** (String): Business sector
- **marketCap** (BigDecimal): Market capitalization
- **totalCapital** (BigDecimal): Total capital
- **riskScore** (Double): Risk assessment score

#### Strategy
- **id** (Long): Primary key
- **name** (String): Strategy name
- **symbolList** (List<String>): Target symbols
- **script** (String): DSL script content
- **paramsJson** (String): Strategy parameters
- **symbol** (String): Primary symbol
- **initialCapital** (Double): Starting capital
- **startDate** (LocalDate): Backtest start date
- **endDate** (LocalDate): Backtest end date
- **status** (String): Strategy status
- **resultJson** (String): Backtest results

#### SaveStrategy
- **id** (Long): Primary key
- **name** (String): Strategy name
- **type** (String): Strategy type
- **capitalAllocation** (Double): Allocated capital
- **status** (String): Strategy status
- **parametersJson** (String): Strategy parameters
- **resultJson** (String): Simulation results

#### BacktestResult
- **id** (Long): Primary key
- **initialEquity** (double): Starting equity
- **finalEquity** (double): Ending equity
- **totalTrades** (int): Number of trades
- **trades** (List<Trade>): Trade details
- **strategy** (Strategy): Associated strategy

#### Candle
- **id** (Long): Primary key
- **date** (LocalDate): Trading date
- **symbol** (String): Stock symbol
- **open** (double): Opening price
- **high** (double): Highest price
- **low** (double): Lowest price
- **close** (double): Closing price
- **volume** (long): Trading volume

#### Transaction
- **id** (UUID): Primary key
- **investor** (Investor): Associated investor
- **fundScheme** (FundScheme): Associated scheme
- **type** (TransactionType): BUY, REDEEM
- **amount** (BigDecimal): Transaction amount
- **units** (BigDecimal): Number of units
- **nav** (BigDecimal): NAV at transaction
- **timestamp** (LocalDateTime): Transaction timestamp

## 🔒 Security Configuration

### JWT Authentication
- **Secret Key**: Configurable via `jwt.secret`
- **Access Token Expiration**: 15 minutes (900000ms)
- **Refresh Token Expiration**: 7 days (604800000ms)
- **Algorithm**: HMAC-SHA256

### CORS Configuration
- **Allowed Origins**: `http://localhost:5173` (Frontend)
- **Allowed Methods**: GET, POST, PUT, DELETE, OPTIONS
- **Allowed Headers**: All headers
- **Allow Credentials**: true

### Password Security
- **Encoder**: BCryptPasswordEncoder
- **Strength**: 10 rounds (default)

### Public Endpoints
- `/api/auth/**` - Authentication endpoints
- `/api/public/**` - Public endpoints
- `/api/investors/**` - Investor endpoints
- `/api/fundManagers/**` - Fund manager endpoints
- `/api/amcs/**` - AMC endpoints
- `/api/fund-schemes/**` - Fund scheme endpoints
- `/api/import/**` - Import endpoints
- `/api/backtest/**` - Backtesting endpoints
- `/api/strategies/**` - Strategy endpoints
- `/api/save-strategies/**` - Saved strategy endpoints
- `/api/statistics/**` - Statistics endpoints

## 🧪 Testing

### Running Tests
```bash
# Run all tests
./gradlew test

# Run specific test class
./gradlew test --tests InvestorControllerIntegrationTest

# Run tests with coverage
./gradlew jacocoTestReport

# View coverage report
open build/reports/jacoco/test/html/index.html
```

### Test Structure
```
src/test/java/com/fse/FSE_Backend_Proj/
├── controller/
│   ├── FundManagerControllerIntegrationTest.java
│   ├── InvestorControllerIntegrationTest.java
│   └── StrategyControllerIntegrationTest.java
├── service/
│   ├── impl/
│   │   └── AMCServiceImplTest.java
│   ├── InvestorServiceImplTest.java
│   ├── StrategyServiceTest.java
│   └── UserServiceTest.java
└── FseBackendProjApplicationTests.java
```

## 🚀 Deployment

### Docker Deployment

#### Dockerfile
```dockerfile
FROM openjdk:21-jdk-slim

# Set working directory
WORKDIR /app

# Copy the JAR file
COPY build/libs/FSE_Backend-0.0.1-SNAPSHOT.jar app.jar

# Expose port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
```

#### Docker Compose
```yaml
version: '3.8'
services:
  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      - SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/fseDB
      - SPRING_DATASOURCE_USERNAME=postgres
      - SPRING_DATASOURCE_PASSWORD=postgres
    depends_on:
      - db
  
  db:
    image: postgres:15
    environment:
      - POSTGRES_DB=fseDB
      - POSTGRES_USER=postgres
      - POSTGRES_PASSWORD=postgres
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data

volumes:
  postgres_data:
```

### Environment Variables

```bash
# Database Configuration
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/fseDB
export SPRING_DATASOURCE_USERNAME=your_username
export SPRING_DATASOURCE_PASSWORD=your_password

# JWT Configuration
export JWT_SECRET=your_ultra_secure_jwt_secret_key
export JWT_ACCESS_EXPIRATION=900000
export JWT_REFRESH_EXPIRATION=604800000

# Application Configuration
export SPRING_APPLICATION_NAME=FSE_Backend_Proj
export SERVER_PORT=8080

# Logging Configuration
export LOGGING_LEVEL_ORG_SPRINGFRAMEWORK_SECURITY=DEBUG
export SPRING_OUTPUT_ANSI_ENABLED=ALWAYS
```

### Production Considerations

1. **Security**
   - Change default JWT secret
   - Use strong database passwords
   - Enable HTTPS
   - Configure proper CORS origins

2. **Database**
   - Use connection pooling
   - Configure proper indexes
   - Set up database backups
   - Monitor performance

3. **Application**
   - Configure proper logging
   - Set up monitoring
   - Use environment-specific profiles
   - Configure health checks

4. **Infrastructure**
   - Use load balancers
   - Set up auto-scaling
   - Configure proper resource limits
   - Monitor system resources
  
  db:
    image: postgres:15
    environment:
      - POSTGRES_DB=fseDB
      - POSTGRES_USER=postgres
      - POSTGRES_PASSWORD=postgres
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data

volumes:
  postgres_data:
```

### Environment Variables

```bash
# Database Configuration
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/fseDB
export SPRING_DATASOURCE_USERNAME=your_username
export SPRING_DATASOURCE_PASSWORD=your_password

# JWT Configuration
export JWT_SECRET=your_ultra_secure_jwt_secret_key
export JWT_ACCESS_EXPIRATION=900000
export JWT_REFRESH_EXPIRATION=604800000

# Application Configuration
export SPRING_APPLICATION_NAME=FSE_Backend_Proj
export SERVER_PORT=8080

# Logging Configuration
export LOGGING_LEVEL_ORG_SPRINGFRAMEWORK_SECURITY=DEBUG
export SPRING_OUTPUT_ANSI_ENABLED=ALWAYS
```

### Production Considerations

1. **Security**
   - Change default JWT secret
   - Use strong database passwords
   - Enable HTTPS
   - Configure proper CORS origins

2. **Database**
   - Use connection pooling
   - Configure proper indexes
   - Set up database backups
   - Monitor performance

3. **Application**
   - Configure proper logging
   - Set up monitoring
   - Use environment-specific profiles
   - Configure health checks

4. **Infrastructure**
   - Use load balancers
   - Set up auto-scaling
   - Configure proper resource limits
   - Monitor system resources

## 🤝 Contributing

### Development Setup

1. **Fork the repository**
2. **Create a feature branch**
   ```bash
   git checkout -b feature/amazing-feature
   ```
3. **Make your changes**
4. **Run tests**
   ```bash
   ./gradlew test
   ```
5. **Commit your changes**
   ```bash
   git commit -m 'Add amazing feature'
   ```
6. **Push to the branch**
   ```bash
   git push origin feature/amazing-feature
   ```
7. **Open a Pull Request**

### Code Style Guidelines

- Follow Java naming conventions
- Use meaningful variable and method names
- Add proper documentation
- Include unit tests for new features
- Follow REST API best practices

### Testing Guidelines

- Write unit tests for all business logic
- Include integration tests for controllers
- Maintain good test coverage (>80%)
- Use meaningful test names
- Mock external dependencies

## 📝 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🆘 Support

### Getting Help

- **Documentation**: Check this README and inline code comments
- **Issues**: Create an issue in the repository
- **Discussions**: Use GitHub Discussions for questions
- **Contact**: Reach out to the development team

### Common Issues

1. **Database Connection**
   - Ensure PostgreSQL is running
   - Check database credentials
   - Verify database exists

2. **JWT Issues**
   - Check JWT secret configuration
   - Verify token expiration settings
   - Ensure proper token format

3. **CORS Issues**
   - Verify frontend origin in CORS config
   - Check browser console for errors
   - Ensure proper headers

4. **Import Issues**
   - Check Yahoo Finance API availability
   - Verify symbol format (e.g., RELIANCE.NS)
   - Check network connectivity

## 🔄 Version History

### v0.0.1-SNAPSHOT (Current)
- **Initial Release**
  - Complete user authentication system
  - Investor management with KYC
  - AMC and Fund Manager operations
  - Fund scheme administration
  - Algorithmic trading strategies
  - Backtesting engine with 6 built-in strategies
  - Market data integration
  - Portfolio management
  - Transaction tracking
  - NAV calculation and history
  - Company explorer functionality
  - Statistics and analytics
  - Comprehensive API documentation

### Planned Features
- **Real-time Market Data**: WebSocket integration
- **Advanced Analytics**: Machine learning models
- **Mobile API**: Optimized for mobile applications
- **Multi-currency Support**: International markets
- **Advanced Risk Management**: VaR calculations
- **Regulatory Compliance**: SEBI guidelines integration

---

**Note**: This is a development version. For production deployment, ensure proper security configurations, database optimizations, and infrastructure setup according to your organization's standards.
