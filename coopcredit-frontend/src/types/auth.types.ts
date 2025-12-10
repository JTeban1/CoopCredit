// Authentication types
export interface LoginRequest {
    username: string;
    password: string;
}

export interface RegisterRequest {
    username: string;
    password: string;
    email: string;
    affiliate?: AffiliateRequest;
}

export interface AuthResponse {
    token: string;
    username: string;
    email: string;
    roles: string[];
    affiliateId?: number;
}

export type UserRole = 'ROLE_AFILIADO' | 'ROLE_ANALISTA' | 'ROLE_ADMIN';

export interface User {
    username: string;
    email: string;
    roles: UserRole[];
    affiliateId?: number;
}

// Affiliate types
export interface AffiliateRequest {
    document: string;
    firstName: string;
    lastName: string;
    email: string;
    phone: string;
    salary: number;
}

export interface AffiliateResponse extends AffiliateRequest {
    id: number;
    createdAt: string;
}
