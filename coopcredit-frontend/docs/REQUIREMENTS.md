# Requirements Documentation

## Functional Requirements
- **Responsive Design**: The UI must adapt to Desktop and Mobile screens.
- **Form Validation**:
    - Username/Password cannot be empty.
    - Credit amount must be positive.
    - Term must be between 1 and 60 months.
- **User Feedback**: Show loading spinners during API calls and toast notifications for success/error.

## Non-Functional Requirements
- **Usability**: Intuitive navigation with max 3 clicks to reach main features.
- **Browser Output**: Chrome, Firefox, Safari, Edge (latest versions).

## Use Cases (User Journey)
### UJ1: New Affiliate Registration
1. User lands on Login page.
2. Clicks "Register".
3. Fills form.
4. Clicks "Sign Up".
5. Redirected to Dashboard on success.

### UJ2: Submit Application
1. User logs in.
2. Navigates to Dashboard.
3. Clicks "New Credit".
4. Fills Amount/Term.
5. Clicks "Submit".
6. Sees new row in table with "PENDING" status.
