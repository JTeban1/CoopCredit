import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Plus, FileText, Eye, Loader2, PlayCircle } from 'lucide-react';
import { Button } from '../../components/common/Button';
import { Card } from '../../components/common/Card';
import { Badge } from '../../components/common/Badge';
import { Modal } from '../../components/common/Modal';
import { useAuth } from '../../contexts/AuthContext';
import { applicationApi } from '../../api/application.api';
import { CreditApplicationResponse } from '../../types/application.types';
import { formatCurrency, formatDate } from '../../utils/formatters';
import { handleApiError } from '../../api/client';

export const ApplicationListPage = () => {
    const { user } = useAuth();
    const [applications, setApplications] = useState<CreditApplicationResponse[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string>('');
    const [selectedApp, setSelectedApp] = useState<CreditApplicationResponse | null>(null);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [evaluating, setEvaluating] = useState<number | null>(null);

    const isAffiliate = user?.roles?.includes('ROLE_AFILIADO');
    const isAdminOrAnalyst = user?.roles?.some(r => r === 'ROLE_ADMIN' || r === 'ROLE_ANALISTA');

    useEffect(() => {
        loadApplications();
    }, [user]);

    const loadApplications = async () => {
        try {
            setLoading(true);
            setError('');
            let data: CreditApplicationResponse[];
            
            if (isAffiliate && user?.affiliateId) {
                // Affiliate users only see their own applications
                data = await applicationApi.getByAffiliate(user.affiliateId);
            } else if (isAdminOrAnalyst) {
                // Admin/Analyst users see all applications
                data = await applicationApi.getAll();
            } else {
                data = [];
            }
            
            setApplications(data);
        } catch (err) {
            console.error('Failed to load applications:', err);
            setError('Failed to load applications');
        } finally {
            setLoading(false);
        }
    };

    const handleViewApplication = async (app: CreditApplicationResponse) => {
        try {
            // Fetch fresh data for the application
            const freshData = await applicationApi.getById(app.id);
            setSelectedApp(freshData);
            setIsModalOpen(true);
        } catch (err) {
            console.error('Failed to load application details:', err);
            // Fallback to existing data
            setSelectedApp(app);
            setIsModalOpen(true);
        }
    };

    const handleEvaluate = async (id: number) => {
        try {
            setEvaluating(id);
            const evaluated = await applicationApi.evaluate(id);
            // Update the application in the list
            setApplications(prev => prev.map(app => 
                app.id === id ? evaluated : app
            ));
            // Update modal if open
            if (selectedApp?.id === id) {
                setSelectedApp(evaluated);
            }
        } catch (err) {
            console.error('Failed to evaluate application:', err);
            alert(handleApiError(err));
        } finally {
            setEvaluating(null);
        }
    };

    const closeModal = () => {
        setIsModalOpen(false);
        setSelectedApp(null);
    };

    return (
        <div className="space-y-6">
            <div className="flex items-center justify-between">
                <div>
                    <h1 className="text-3xl font-bold text-gray-900">Credit Applications</h1>
                    <p className="text-gray-600 mt-1">Manage all applications</p>
                </div>
                <Link to="/applications/new">
                    <Button variant="primary">
                        <Plus className="w-4 h-4 mr-2" />
                        New Application
                    </Button>
                </Link>
            </div>

            <Card>
                {loading ? (
                    <div className="text-center py-12">
                        <Loader2 className="w-8 h-8 mx-auto animate-spin text-primary-600" />
                        <p className="mt-2 text-gray-600">Loading applications...</p>
                    </div>
                ) : error ? (
                    <div className="text-center py-12 text-danger-600">
                        <p>{error}</p>
                        <Button variant="secondary" className="mt-4" onClick={loadApplications}>
                            Retry
                        </Button>
                    </div>
                ) : applications.length === 0 ? (
                    <div className="text-center py-12 text-gray-500">
                        <FileText className="w-16 h-16 mx-auto mb-4 opacity-50" />
                        <p className="text-lg font-medium">No applications</p>
                        <p className="text-sm mt-1">
                            Create your first credit application by clicking the "New Application" button
                        </p>
                    </div>
                ) : (
                    <div className="overflow-x-auto">
                        <table className="min-w-full divide-y divide-gray-200">
                            <thead className="bg-gray-50">
                                <tr>
                                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">ID</th>
                                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Affiliate ID</th>
                                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Amount</th>
                                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Term</th>
                                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Status</th>
                                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Created</th>
                                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Actions</th>
                                </tr>
                            </thead>
                            <tbody className="bg-white divide-y divide-gray-200">
                                {applications.map((app) => (
                                    <tr key={app.id} className="hover:bg-gray-50">
                                        <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">#{app.id}</td>
                                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{app.affiliateId}</td>
                                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{formatCurrency(app.requestedAmount)}</td>
                                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{app.termMonths} months</td>
                                        <td className="px-6 py-4 whitespace-nowrap">
                                            <Badge status={app.status} />
                                        </td>
                                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{formatDate(app.applicationDate)}</td>
                                        <td className="px-6 py-4 whitespace-nowrap text-sm space-x-2">
                                            <button
                                                onClick={() => handleViewApplication(app)}
                                                className="text-primary-600 hover:text-primary-800 inline-flex items-center"
                                            >
                                                <Eye className="w-4 h-4 mr-1" /> View
                                            </button>
                                            {isAdminOrAnalyst && app.status === 'PENDING' && (
                                                <button
                                                    onClick={() => handleEvaluate(app.id)}
                                                    disabled={evaluating === app.id}
                                                    className="text-success-600 hover:text-success-800 inline-flex items-center ml-2 disabled:opacity-50"
                                                >
                                                    {evaluating === app.id ? (
                                                        <Loader2 className="w-4 h-4 mr-1 animate-spin" />
                                                    ) : (
                                                        <PlayCircle className="w-4 h-4 mr-1" />
                                                    )}
                                                    Evaluate
                                                </button>
                                            )}
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>
                )}
            </Card>

            {/* Application Detail Modal */}
            <Modal isOpen={isModalOpen} onClose={closeModal} title="Application Details" size="lg">
                {selectedApp && (
                    <div className="space-y-6">
                        {/* Header with Status */}
                        <div className="flex items-center justify-between">
                            <h3 className="text-lg font-semibold text-gray-900">
                                Application #{selectedApp.id}
                            </h3>
                            <Badge status={selectedApp.status} />
                        </div>

                        {/* Application Info Grid */}
                        <div className="grid grid-cols-2 gap-4">
                            <div className="bg-gray-50 p-4 rounded-lg">
                                <p className="text-sm text-gray-500">Affiliate ID</p>
                                <p className="text-lg font-semibold">{selectedApp.affiliateId}</p>
                            </div>
                            <div className="bg-gray-50 p-4 rounded-lg">
                                <p className="text-sm text-gray-500">Requested Amount</p>
                                <p className="text-lg font-semibold">{formatCurrency(selectedApp.requestedAmount)}</p>
                            </div>
                            <div className="bg-gray-50 p-4 rounded-lg">
                                <p className="text-sm text-gray-500">Term</p>
                                <p className="text-lg font-semibold">{selectedApp.termMonths} months</p>
                            </div>
                            <div className="bg-gray-50 p-4 rounded-lg">
                                <p className="text-sm text-gray-500">Monthly Payment</p>
                                <p className="text-lg font-semibold">
                                    {selectedApp.monthlyPayment ? formatCurrency(selectedApp.monthlyPayment) : 'N/A'}
                                </p>
                            </div>
                            <div className="bg-gray-50 p-4 rounded-lg col-span-2">
                                <p className="text-sm text-gray-500">Application Date</p>
                                <p className="text-lg font-semibold">{formatDate(selectedApp.applicationDate)}</p>
                            </div>
                        </div>

                        {/* Purpose */}
                        {selectedApp.purpose && (
                            <div className="bg-gray-50 p-4 rounded-lg">
                                <p className="text-sm text-gray-500 mb-1">Purpose</p>
                                <p className="text-gray-900">{selectedApp.purpose}</p>
                            </div>
                        )}

                        {/* Risk Evaluation (if available) */}
                        {selectedApp.riskEvaluation && (
                            <div className="border-t pt-4">
                                <h4 className="font-semibold text-gray-900 mb-3">Risk Evaluation</h4>
                                <div className="grid grid-cols-2 gap-4">
                                    <div className="bg-blue-50 p-4 rounded-lg">
                                        <p className="text-sm text-blue-600">Risk Score</p>
                                        <p className="text-2xl font-bold text-blue-800">
                                            {selectedApp.riskEvaluation.score}
                                        </p>
                                    </div>
                                    <div className="bg-blue-50 p-4 rounded-lg">
                                        <p className="text-sm text-blue-600">Recommendation</p>
                                        <p className="text-lg font-semibold text-blue-800">
                                            {selectedApp.riskEvaluation.recommendation}
                                        </p>
                                    </div>
                                </div>
                                {selectedApp.riskEvaluation.factors && selectedApp.riskEvaluation.factors.length > 0 && (
                                    <div className="mt-3">
                                        <p className="text-sm text-gray-500 mb-2">Risk Factors</p>
                                        <ul className="list-disc list-inside text-gray-700">
                                            {selectedApp.riskEvaluation.factors.map((factor, idx) => (
                                                <li key={idx}>{factor}</li>
                                            ))}
                                        </ul>
                                    </div>
                                )}
                            </div>
                        )}

                        {/* Actions */}
                        <div className="flex justify-end space-x-3 border-t pt-4">
                            {isAdminOrAnalyst && selectedApp.status === 'PENDING' && (
                                <Button
                                    variant="primary"
                                    onClick={() => {
                                        handleEvaluate(selectedApp.id);
                                    }}
                                    isLoading={evaluating === selectedApp.id}
                                >
                                    <PlayCircle className="w-4 h-4 mr-2" />
                                    Evaluate Application
                                </Button>
                            )}
                            <Button variant="secondary" onClick={closeModal}>
                                Close
                            </Button>
                        </div>
                    </div>
                )}
            </Modal>
        </div>
    );
};
