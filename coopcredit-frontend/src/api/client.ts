                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                import axios, { AxiosError, InternalAxiosRequestConfig } from 'axios';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080';

// Create axios instance
export const apiClient = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        'Content-Type': 'application/json',
    },
});

// Request interceptor - Add JWT token to headers
apiClient.interceptors.request.use(
    (config: InternalAxiosRequestConfig) => {
        const token = localStorage.getItem('token');
        if (token && config.headers) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

// Response interceptor - Handle errors
apiClient.interceptors.response.use(
    (response) => response,
    (error: AxiosError) => {
        // Only redirect to login for 401 errors on protected routes (not auth endpoints)
        const isAuthEndpoint = error.config?.url?.includes('/auth/');
        
        if (error.response?.status === 401 && !isAuthEndpoint) {
            // Unauthorized on protected route - clear token and redirect to login
            localStorage.removeItem('token');
            localStorage.removeItem('user');
            window.location.href = '/login';
        }
        return Promise.reject(error);
    }
);

// Error handler helper
export const handleApiError = (error: unknown): string => {
    if (axios.isAxiosError(error)) {
        // Check for error response data
        const errorData = error.response?.data;
        
        // Handle different error response formats
        if (typeof errorData === 'object' && errorData !== null) {
            // Check for detail field (Problem JSON format)
            if ('detail' in errorData && typeof errorData.detail === 'string') {
                return errorData.detail;
            }
            // Check for message field
            if ('message' in errorData && typeof errorData.message === 'string') {
                return errorData.message;
            }
            // Check for error field
            if ('error' in errorData && typeof errorData.error === 'string') {
                return errorData.error;
            }
        }
        
        // Handle 401 specifically
        if (error.response?.status === 401) {
            return 'Invalid username or password';
        }
        
        // Handle 403
        if (error.response?.status === 403) {
            return 'Access denied';
        }
        
        // Handle network errors
        if (error.code === 'ERR_NETWORK') {
            return 'Network error. Please check your connection.';
        }
        
        // Fallback to error message
        return error.message || 'An error occurred';
    }
    
    if (error instanceof Error) {
        return error.message;
    }
    
    return 'An unexpected error occurred';
};
