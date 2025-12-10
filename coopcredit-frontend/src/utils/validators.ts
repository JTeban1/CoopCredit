import { z } from 'zod';

// Login validation schema
export const loginSchema = z.object({
    username: z.string().min(3, 'Username must be at least 3 characters'),
    password: z.string().min(6, 'Password must be at least 6 characters'),
});

// Register validation schema
export const registerSchema = z.object({
    username: z.string().min(3, 'Username must be at least 3 characters'),
    password: z.string().min(6, 'Password must be at least 6 characters'),
    email: z.string().email('Invalid email address'),
    affiliate: z.object({
        document: z.string().min(6, 'Document must be at least 6 characters'),
        firstName: z.string().min(2, 'First name must be at least 2 characters'),
        lastName: z.string().min(2, 'Last name must be at least 2 characters'),
        email: z.string().email('Invalid email address'),
        phone: z.string().min(10, 'Phone must be at least 10 digits'),
        salary: z.number().min(0, 'Salary must be a positive number'),
    }).optional(),
});

// Affiliate validation schema
export const affiliateSchema = z.object({
    document: z.string().min(6, 'Document must be at least 6 characters'),
    firstName: z.string().min(2, 'First name must be at least 2 characters'),
    lastName: z.string().min(2, 'Last name must be at least 2 characters'),
    email: z.string().email('Invalid email address'),
    phone: z.string().regex(/^\d{10}$/, 'Phone must be 10 digits'),
    salary: z.number().min(0, 'Salary must be a positive number'),
});

// Credit application validation schema
export const creditApplicationSchema = z.object({
    affiliateId: z.number().min(1, 'Please select an affiliate'),
    requestedAmount: z.number()
        .min(100000, 'Minimum amount is $100,000 COP')
        .max(50000000, 'Maximum amount is $50,000,000 COP'),
    termMonths: z.number()
        .min(6, 'Minimum term is 6 months')
        .max(60, 'Maximum term is 60 months'),
    purpose: z.string().optional(),
});
