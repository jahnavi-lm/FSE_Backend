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

### Fund Management Endpoints

#### Create Fund Scheme
```http
POST /api/fund-schemes/create
Authorization: Bearer <jwt-token>
Content-Type: application/json

{
  "name": "Growth Fund",
  "type": "EQUITY",
  "description": "High-growth equity fund",
  "riskLevel": "HIGH"
}
```

#### Assign Fund Manager
```http
POST /api/fund-schemes/assign-manager
Authorization: Bearer <jwt-token>
Content-Type: application/json

{
  "schemeId": "uuid",
  "managerId": "uuid"
}
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
  "startDate": "2024-01-01",
  "endDate": "2024-12-31"
}
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

### Company and Market Data

#### Import Candle Data
```http
POST /api/import/candles
Authorization: Bearer <jwt-token>
Content-Type: application/json

{
  "symbol": "RELIANCE.NS",
  "startDate": "2024-01-01",
  "endDate": "2024-12-31"
}
```

#### Get Company Details
```http
GET /api/companies/{symbol}
Authorization: Bearer <jwt-token>
```

## 🏗️ Project Structure

```
src/main/java/com/fse/FSE_Backend_Proj/
├── config/                 # Security and configuration
├── controller/             # REST API controllers
├── dto/                   # Data Transfer Objects
├── engine/                # Trading strategy engine
│   ├── builtin/          # Built-in strategies
│   └── dsl/              # Domain-specific language
├── exception/             # Custom exceptions
├── indicator/             # Technical indicators
├── model/                 # Entity models
│   └── enums/            # Enumerations
├── parser/                # Data parsing utilities
├── repository/            # Data access layer
├── service/               # Business logic layer
│   └── impl/             # Service implementations
└── util/                  # Utility classes
```

## 🔧 Built-in Trading Strategies

The application includes several pre-built trading strategies:

- **Buy and Hold** - Long-term investment strategy
- **Moving Average Crossover** - Technical analysis based on moving averages
- **RSI Strategy** - Relative Strength Index based trading
- **Day of Week Strategy** - Calendar-based trading patterns
- **Volume Surge Strategy** - Volume-based trading signals
- **Threshold Based Strategy** - Custom threshold-based rules

## 🧪 Testing

Run tests with coverage:

```bash
# Run all tests
./gradlew test

# Generate coverage report
./gradlew jacocoTestReport

# View coverage report
open build/reports/jacoco/test/html/index.html
```

## 🔒 Security

- JWT-based authentication
- Role-based access control
- Password encryption
- Secure API endpoints

## 📊 Database Schema

Key entities include:
- **User** - Authentication and user management
- **Investor** - Individual investor profiles
- **FundScheme** - Investment fund schemes
- **FundManager** - Fund management professionals
- **Transaction** - Investment transactions
- **Strategy** - Trading strategies
- **BacktestResult** - Strategy backtesting results
- **Candle** - Market price data
- **Company** - Company information

## 🚀 Deployment

### Docker Deployment

```dockerfile
FROM openjdk:21-jdk-slim
COPY build/libs/FSE_Backend-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### Environment Variables

```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/fseDB
export SPRING_DATASOURCE_USERNAME=your_username
export SPRING_DATASOURCE_PASSWORD=your_password
export JWT_SECRET=your_jwt_secret
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🆘 Support

For support and questions:
- Create an issue in the repository
- Contact the development team
- Check the API documentation

## 🔄 Version History

- **v0.0.1-SNAPSHOT** - Initial release with core functionality
  - User authentication and authorization
  - Investment management
  - Fund scheme administration
  - Strategy backtesting
  - Portfolio management

---

**Note**: This is a development version. For production deployment, ensure proper security configurations and database optimizations.
