# Risk Central Mock Service

## Overview
The `risk-central-mock-service` simulates an external Credit Risk Bureau. It provides a REST API to evaluate the creditworthiness of a person based on their ID document and Application ID.

## Purpose
This service is used for testing and development of the `credit-application-service` without requiring a connection to a real banking system.

## Tech Stack
- **Language**: Java 17
- **Framework**: Spring Boot 3.x
- **Build Tool**: Maven

## Architecture
This is a simple service with a single Controller (`RiskEvaluationController`) and a Service Layer (`RiskEvaluationService`) that implements the mock logic (randomized or deterministic based on inputs).

## Getting Started
See [SETUP.md](./SETUP.md) for running instructions.
See [API_REFERENCE.md](./API_REFERENCE.md) for endpoint details.
