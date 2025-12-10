# Setup Guide

## Prerequisites
- **Node.js** (v18+)
- **npm** (v9+) or **yarn** or **pnpm**

## Installation

1. **Clone the repository**:
   ```bash
   git clone https://github.com/riwi/coopcredit.git
   cd coopcredit/coopcredit-frontend
   ```

2. **Install Dependencies**:
   ```bash
   npm install
   ```

## Configuration
Create a `.env` file in the root if you need to override defaults (often not needed for local dev if backend is on 8080).

## Running Locally

1. **Start Backend**:
   Ensure `credit-application-service` is running on port 8080.

2. **Start Dev Server**:
   ```bash
   npm run dev
   ```
   The app will run at `http://localhost:5173`.

## Building for Production

1. **Build**:
   ```bash
   npm run build
   ```
   Output will be in `dist/`.

2. **Preview**:
   ```bash
   npm run preview
   ```
