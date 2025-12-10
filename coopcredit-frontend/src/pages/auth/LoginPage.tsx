import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { LogIn, Mail, Lock } from 'lucide-react';
import { useAuth } from '../../contexts/AuthContext';
import { loginSchema } from '../../utils/validators';
import { LoginRequest } from '../../types/auth.types';
import { Button } from '../../components/common/Button';
import { Input } from '../../components/common/Input';
import { Card } from '../../components/common/Card';
import { handleApiError } from '../../api/client';

export const LoginPage = () => {
    const navigate = useNavigate();
    const { login } = useAuth();
    const [error, setError] = useState<string>('');
    const [isLoading, setIsLoading] = useState(false);

    const {
        register,
        handleSubmit,
        formState: { errors },
    } = useForm<LoginRequest>({
        resolver: zodResolver(loginSchema),
    });

    const onSubmit = async (data: LoginRequest) => {
        try {
            setError('');
            setIsLoading(true);
            await login(data);
            navigate('/dashboard');
        } catch (err) {
            setError(handleApiError(err));
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-primary-500 via-primary-600 to-primary-700 p-4">
            <div className="w-full max-w-md">
                <div className="text-center mb-8">
                    <div className="inline-flex items-center justify-center w-16 h-16 bg-white rounded-full mb-4 shadow-lg">
                        <LogIn className="w-8 h-8 text-primary-600" />
                    </div>
                    <h1 className="text-3xl font-bold text-white mb-2">
                        {import.meta.env.VITE_APP_NAME || 'CoopCredit'}
                    </h1>
                    <p className="text-primary-100">Sign in to your account</p>
                </div>

                <Card variant="elevated" className="backdrop-blur-sm bg-white/95">
                    <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
                        {error && (
                            <div className="p-3 bg-danger-50 border border-danger-200 rounded-lg">
                                <p className="text-sm text-danger-800">{error}</p>
                            </div>
                        )}

                        <Input
                            label="Username"
                            type="text"
                            placeholder="Enter your username"
                            icon={<Mail className="w-5 h-5" />}
                            error={errors.username?.message}
                            {...register('username')}
                        />

                        <Input
                            label="Password"
                            type="password"
                            placeholder="Enter your password"
                            icon={<Lock className="w-5 h-5" />}
                            error={errors.password?.message}
                            {...register('password')}
                        />

                        <Button
                            type="submit"
                            variant="primary"
                            className="w-full"
                            isLoading={isLoading}
                        >
                            Sign In
                        </Button>

                        <div className="text-center text-sm text-gray-600">
                            Don't have an account?{' '}
                            <Link
                                to="/register"
                                className="text-primary-600 hover:text-primary-700 font-medium"
                            >
                                Register here
                            </Link>
                        </div>
                    </form>
                </Card>
            </div>
        </div>
    );
};
