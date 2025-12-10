import { createContext, useContext, useState, useEffect, ReactNode } from 'react';
import { User, AuthResponse, LoginRequest, RegisterRequest } from '../types/auth.types';
import { authApi } from '../api/auth.api';

interface AuthContextType {
    user: User | null;
    token: string | null;
    login: (credentials: LoginRequest) => Promise<void>;
    register: (data: RegisterRequest) => Promise<void>;
    logout: () => void;
    isAuthenticated: boolean;
    hasRole: (role: string) => boolean;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider = ({ children }: { children: ReactNode }) => {
    const [user, setUser] = useState<User | null>(null);
    const [token, setToken] = useState<string | null>(null);

    useEffect(() => {
        // Load user and token from localStorage on mount
        const storedToken = localStorage.getItem('token');
        const storedUser = localStorage.getItem('user');

        if (storedToken && storedUser) {
            setToken(storedToken);
            setUser(JSON.parse(storedUser));
        }
    }, []);

    const login = async (credentials: LoginRequest) => {
        const response: AuthResponse = await authApi.login(credentials);

        const userData: User = {
            username: response.username,
            email: response.email,
            roles: response.roles as any[],
            affiliateId: response.affiliateId,
        };

        setToken(response.token);
        setUser(userData);
        localStorage.setItem('token', response.token);
        localStorage.setItem('user', JSON.stringify(userData));
    };

    const register = async (data: RegisterRequest) => {
        const response: AuthResponse = await authApi.register(data);

        const userData: User = {
            username: response.username,
            email: response.email,
            roles: response.roles as any[],
            affiliateId: response.affiliateId,
        };

        setToken(response.token);
        setUser(userData);
        localStorage.setItem('token', response.token);
        localStorage.setItem('user', JSON.stringify(userData));
    };

    const logout = () => {
        setUser(null);
        setToken(null);
        localStorage.removeItem('token');
        localStorage.removeItem('user');
    };

    const hasRole = (role: string): boolean => {
        return user?.roles.includes(role as any) || false;
    };

    const value = {
        user,
        token,
        login,
        register,
        logout,
        isAuthenticated: !!token && !!user,
        hasRole,
    };

    return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

export const useAuth = () => {
    const context = useContext(AuthContext);
    if (context === undefined) {
        throw new Error('useAuth must be used within an AuthProvider');
    }
    return context;
};
