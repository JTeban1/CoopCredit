import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { UserPlus, Mail, Lock, User, Phone, DollarSign, FileText } from 'lucide-react';
import { useAuth } from '../../contexts/AuthContext';
import { registerSchema } from '../../utils/validators';
import { RegisterRequest } from '../../types/auth.types';
import { Button } from '../../components/common/Button';
import { Input } from '../../components/common/Input';
import { Card } from '../../components/common/Card';
import { handleApiError } from '../../api/client';

export const RegisterPage = () => {
    const navigate = useNavigate();
    const { register: registerUser } = useAuth();
    const [error, setError] = useState<string>('');
    const [isLoading, setIsLoading] = useState(false);
    const [step, setStep] = useState(1);

    const {
        register,
        handleSubmit,
        formState: { errors },

    } = useForm<RegisterRequest>({
        resolver: zodResolver(registerSchema),
        defaultValues: {
            affiliate: {
                document: '',
                firstName: '',
                lastName: '',
                email: '',
                phone: '',
                salary: 0,
            },
        },
    });

    const onSubmit = async (data: RegisterRequest) => {
        try {
            setError('');
            setIsLoading(true);
            await registerUser(data);
            navigate('/dashboard');
        } catch (err) {
            setError(handleApiError(err));
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-success-500 via-success-600 to-success-700 p-4">
            <div className="w-full max-w-2xl">
                <div className="text-center mb-8">
                    <div className="inline-flex items-center justify-center w-16 h-16 bg-white rounded-full mb-4 shadow-lg">
                        <UserPlus className="w-8 h-8 text-success-600" />
                    </div>
                    <h1 className="text-3xl font-bold text-white mb-2">Create Account</h1>
                    <p className="text-success-100">Register to apply for credits</p>
                </div>

                <Card variant="elevated" className="backdrop-blur-sm bg-white/95">
                    <form onSubmit={handleSubmit(onSubmit)} className="space-y-6">
                        {error && (
                            <div className="p-3 bg-danger-50 border border-danger-200 rounded-lg">
                                <p className="text-sm text-danger-800">{error}</p>
                            </div>
                        )}

                        {/* Progress indicator */}
                        <div className="flex items-center justify-center space-x-2 mb-6">
                            <div
                                className={`w-3 h-3 rounded-full ${step === 1 ? 'bg-success-600' : 'bg-gray-300'
                                    }`}
                            />
                            <div className="w-12 h-1 bg-gray-300" />
                            <div
                                className={`w-3 h-3 rounded-full ${step === 2 ? 'bg-success-600' : 'bg-gray-300'
                                    }`}
                            />
                        </div>

                        {step === 1 ? (
                            <>
                                <h2 className="text-xl font-semibold text-gray-900 mb-4">
                                    User Information
                                </h2>

                                <Input
                                    label="Username"
                                    type="text"
                                    placeholder="Choose a username"
                                    icon={<User className="w-5 h-5" />}
                                    error={errors.username?.message}
                                    {...register('username')}
                                />

                                <Input
                                    label="Email"
                                    type="email"
                                    placeholder="your@email.com"
                                    icon={<Mail className="w-5 h-5" />}
                                    error={errors.email?.message}
                                    {...register('email')}
                                />

                                <Input
                                    label="Password"
                                    type="password"
                                    placeholder="Minimum 6 characters"
                                    icon={<Lock className="w-5 h-5" />}
                                    error={errors.password?.message}
                                    {...register('password')}
                                />

                                <Button
                                    type="button"
                                    variant="primary"
                                    className="w-full"
                                    onClick={() => setStep(2)}
                                >
                                    Next
                                </Button>
                            </>
                        ) : (
                            <>
                                <h2 className="text-xl font-semibold text-gray-900 mb-4">
                                    Affiliate Information
                                </h2>

                                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                                    <Input
                                        label="Document"
                                        type="text"
                                        placeholder="Document number"
                                        icon={<FileText className="w-5 h-5" />}
                                        error={errors.affiliate?.document?.message}
                                        {...register('affiliate.document')}
                                    />

                                    <Input
                                        label="Phone"
                                        type="tel"
                                        placeholder="3001234567"
                                        icon={<Phone className="w-5 h-5" />}
                                        error={errors.affiliate?.phone?.message}
                                        {...register('affiliate.phone')}
                                    />

                                    <Input
                                        label="First Name"
                                        type="text"
                                        placeholder="Your first name"
                                        icon={<User className="w-5 h-5" />}
                                        error={errors.affiliate?.firstName?.message}
                                        {...register('affiliate.firstName')}
                                    />

                                    <Input
                                        label="Last Name"
                                        type="text"
                                        placeholder="Your last name"
                                        icon={<User className="w-5 h-5" />}
                                        error={errors.affiliate?.lastName?.message}
                                        {...register('affiliate.lastName')}
                                    />

                                    <Input
                                        label="Affiliate Email"
                                        type="email"
                                        placeholder="affiliate@email.com"
                                        icon={<Mail className="w-5 h-5" />}
                                        error={errors.affiliate?.email?.message}
                                        {...register('affiliate.email')}
                                    />

                                    <Input
                                        label="Salary"
                                        type="number"
                                        placeholder="0"
                                        icon={<DollarSign className="w-5 h-5" />}
                                        error={errors.affiliate?.salary?.message}
                                        {...register('affiliate.salary', { valueAsNumber: true })}
                                    />
                                </div>

                                <div className="flex space-x-3">
                                    <Button
                                        type="button"
                                        variant="secondary"
                                        className="flex-1"
                                        onClick={() => setStep(1)}
                                    >
                                        Back
                                    </Button>
                                    <Button
                                        type="submit"
                                        variant="primary"
                                        className="flex-1"
                                        isLoading={isLoading}
                                    >
                                        Register
                                    </Button>
                                </div>
                            </>
                        )}

                        <div className="text-center text-sm text-gray-600">
                            Already have an account?{' '}
                            <Link
                                to="/login"
                                className="text-success-600 hover:text-success-700 font-medium"
                            >
                                Sign in here
                            </Link>
                        </div>
                    </form>
                </Card>
            </div>
        </div>
    );
};
