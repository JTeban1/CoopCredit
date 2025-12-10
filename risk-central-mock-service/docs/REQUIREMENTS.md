# Requirements Documentation

## Functional Requirements
- **Risk Evaluation**: The service must accept a document number and return a credit score (0-850), risk level (LOW, MEDIUM, HIGH), and details.

## Non-Functional Requirements
- **Latency**: Must respond within 500ms to simulate a fast external API.
- **Availability**: Should be stateless and easy to scale (though it's a mock).

## Use Cases

### UC1: Calculate Risk Score
- **Actor**: System (Credit Application Service)
- **Input**: Document Number, Application ID
- **Process**:
    1. Receive request.
    2. Generate random score or use deterministic logic based on document number.
    3. Return response.
- **Output**: JSON with score and risk level.
