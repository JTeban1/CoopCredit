import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { DollarSign } from 'lucide-react';
import { useAuth } from '../../contexts/AuthContext';
import { creditApplicationSchema } from '../../utils/validators';
import { CreditApplicationRequest } from '../../types/application.types';
import { applicationApi } from '../../api/application.api';
import { affiliateApi } from '../../api/affiliate.api';
import { Button } from '../../components/common/Button';
import { Input } from '../../components/common/Input';
import { Card } from '../../components/common/Card';
import { handleApiError } from '../../api/client';
import { formatCurrency } from '../../utils/formatters';
import { AffiliateResponse } from '../../types/auth.types';

export const CreateApplicationPage = () => {
    const navigate = useNavigate();
    const { user } = useAuth();
    const [error, setError] = useState<string>('');
    const [isLoading, setIsLoading] = useState(false);
    const [affiliates, setAffiliates] = useState<AffiliateResponse[]>([]);
    const [loadingAffiliates, setLoadingAffiliates] = useState(false);

    const isAffiliate = user?.roles?.includes('ROLE_AFILIADO');

    const {
        register,
        handleSubmit,
        formState: { errors },
        watch,
        setValue,
    } = useForm<CreditApplicationRequest>({
        resolver: zodResolver(creditApplicationSchema),
        defaultValues: {
            affiliateId: user?.affiliateId || 0,
        },
    });

    useEffect(() => {
        // If user is admin/analyst, load affiliates list
        if (!isAffiliate) {
            loadAffiliates();
        }
    }, [isAffiliate]);

    const loadAffiliates = async () => {
        try {
            setLoadingAffiliates(true);
            const data = await affiliateApi.getAll();
            setAffiliates(data);
            // Auto-select first affiliate if available
            if (data.length > 0 && !user?.affiliateId) {
                setValue('affiliateId', data[0].id);
            }
        } catch (err) {
            console.error('Failed to load affiliates:', err);
        } finally {
            setLoadingAffiliates(false);
        }
    };

    const requestedAmount = watch('requestedAmount');

    const onSubmit = async (data: CreditApplicationRequest) => {
        try {
            setError('');
            setIsLoading(true);
            const response = await applicationApi.create(data);
            navigate(`/applications/${response.id}`);
        } catch (err) {
            setError(handleApiError(err));
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className="max-w-2xl mx-auto space-y-6">
            <div>
                <h1 className="text-3xl font-bold text-gray-900">New Credit Application</h1>
                <p className="text-gray-600 mt-1">
                    Complete the form to request a credit
                </p>
            </div>

            <Card variant="elevated">
                <form onSubmit={handleSubmit(onSubmit)} className="space-y-6">
                    {error && (
                        <div className="p-3 bg-danger-50 border border-danger-200 rounded-lg">
                            <p className="text-sm text-danger-800">{error}</p>
                        </div>
                    )}

                    <Input
                        label="Requested Amount (COP)"
                        type="number"
                        placeholder="5000000"
                        icon={<DollarSign className="w-5 h-5" />}
                        error={errors.requestedAmount?.message}
                        helperText={
                            requestedAmount
                                ? `Amount: ${formatCurrency(requestedAmount)}`
                                : 'Minimum $100,000 - Maximum $50,000,000'
                        }
                        {...register('requestedAmount', { valueAsNumber: true })}
                    />

                    {/* Affiliate selector for admins/analysts */}
                    {!isAffiliate && (
                        <div>
                            <label className="block text-sm font-medium text-gray-700 mb-1">
                                Select Affiliate
                            </label>
                            <select
                                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-transparent"
                                disabled={loadingAffiliates}
                                {...register('affiliateId', { valueAsNumber: true })}
                            >
                                {loadingAffiliates ? (
                                    <option value="">Loading affiliates...</option>
                                ) : affiliates.length === 0 ? (
                                    <option value="">No affiliates found</option>
                                ) : (
                                    <>
                                        <option value="">Select an affiliate</option>
                                        {affiliates.map((affiliate) => (
                                            <option key={affiliate.id} value={affiliate.id}>
                                                {affiliate.firstName} {affiliate.lastName} - {affiliate.document}
                                            </option>
                                        ))}
                                    </>
                                )}
                            </select>
                            {errors.affiliateId && (
                                <p className="mt-1 text-sm text-danger-600">{errors.affiliateId.message}</p>
                            )}
                        </div>
                    )}

                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-1">
                            Term (months)
                        </label>
                        <select
                            className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-transparent"
                            {...register('termMonths', { valueAsNumber: true })}
                        >
                            <option value="">Select a term</option>
                            <option value="6">6 months</option>
                            <option value="12">12 months</option>
                            <option value="18">18 months</option>
                            <option value="24">24 months</option>
                            <option value="36">36 months</option>
                            <option value="48">48 months</option>
                            <option value="60">60 months</option>
                        </select>
                        {errors.termMonths && (
                            <p className="mt-1 text-sm text-danger-600">{errors.termMonths.message}</p>
                        )}
                    </div>

                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-1">
                            Credit Purpose
                        </label>
                        <textarea
                            className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-transparent resize-none"
                            rows={4}
                            placeholder="Describe the purpose of the credit (minimum 10 characters)"
                            {...register('purpose')}
                        />
                        {errors.purpose && (
                            <p className="mt-1 text-sm text-danger-600">{errors.purpose.message}</p>
                        )}
                    </div>

                    <div className="flex space-x-3">
                        <Button
                            type="button"
                            variant="secondary"
                            className="flex-1"
                            onClick={() => navigate('/applications')}
                        >
                            Cancel
                        </Button>
                        <Button type="submit" variant="primary" className="flex-1" isLoading={isLoading}>
                            Submit Application
                        </Button>
                    </div>
                </form>
            </Card>
        </div>
    );
};
