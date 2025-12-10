import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext';
import { Card } from '../../components/common/Card';
import { Badge } from '../../components/common/Badge';
import { Button } from '../../components/common/Button';
import { Modal } from '../../components/common/Modal';
import { FileText, CheckCircle, XCircle, Clock, Loader2, Eye, PlayCircle } from 'lucide-react';
import { applicationApi } from '../../api/application.api';
import { CreditApplicationResponse } from '../../types/application.types';
import { formatCurrency, formatDate } from '../../utils/formatters';
import { handleApiError } from '../../api/client';

export const DashboardPage = () => {
    const { user } = useAuth();
    const [applications, setApplications] = useState<CreditApplicationResponse[]>([]);
    const [loading, setLoading] = useState(true);
    const [selectedApp, setSelectedApp] = useState<CreditApplicationResponse | null>(null);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [evaluating, setEvaluating] = useState<number | null>(null);

    const isAffiliate = user?.roles?.includes('ROLE_AFILIADO');
    const isAnalyst = user?.roles?.some((r) => r === 'ROLE_ADMIN' || r === 'ROLE_ANALISTA');

    useEffect(() => {
        if (user) {
            loadApplications();
        }
    }, [user]);

    const loadApplications = async () => {
        try {
            setLoading(true);
            let data: CreditApplicationResponse[];
            
            if (isAffiliate && user?.affiliateId) {
                data = await applicationApi.getByAffiliate(user.affiliateId);
            } else if (isAnalyst) {
                data = await applicationApi.getAll();
            } else {
                data = [];
            }
            
            setApplications(data);
        } catch (err) {
            console.error('Failed to load applications:', err);
        } finally {
            setLoading(false);
        }
    };

    const handleViewApplication = async (app: CreditApplicationResponse) => {
        try {
            const freshData = await applicationApi.getById(app.id);
            setSelectedApp(freshData);
            setIsModalOpen(true);
        } catch (err) {
            console.error('Failed to load application details:', err);
            setSelectedApp(app);
            setIsModalOpen(true);
        }
    };

    const handleEvaluate = async (id: number) => {
        try {
            setEvaluating(id);
            const evaluated = await applicationApi.evaluate(id);
            setApplications(prev => prev.map(app => app.id === id ? evaluated : app));
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

    // Calculate stats
    const stats = {
        total: applications.length,
        pending: applications.filter(a => a.status === 'PENDING').length,
        approved: applications.filter(a => a.status === 'APPROVED').length,
        rejected: applications.filter(a => a.status === 'REJECTED').length,
    };

    // Get recent applications (last 5)
    const recentApplications = [...applications]
        .sort((a, b) => new Date(b.applicationDate).getTime() - new Date(a.applicationDate).getTime())
        .slice(0, 5);

    return (
        <div className="space-y-6">
            <div>
                <h1 className="text-3xl font-bold text-gray-900">Dashboard</h1>
                <p className="text-gray-600 mt-1">
                    Welcome, {user?.username}
                </p>
            </div>

            {/* Stats Grid */}
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
                <Card className="hover:shadow-lg transition-shadow">
                    <div className="flex items-center justify-between">
                        <div>
                            <p className="text-sm text-gray-600">Total Applications</p>
                            <p className="text-2xl font-bold text-gray-900 mt-1">
                                {loading ? <Loader2 className="w-6 h-6 animate-spin" /> : stats.total}
                            </p>
                        </div>
                        <div className="w-12 h-12 bg-primary-100 rounded-lg flex items-center justify-center">
                            <FileText className="w-6 h-6 text-primary-600" />
                        </div>
                    </div>
                </Card>

                <Card className="hover:shadow-lg transition-shadow">
                    <div className="flex items-center justify-between">
                        <div>
                            <p className="text-sm text-gray-600">Pending</p>
                            <p className="text-2xl font-bold text-warning-600 mt-1">
                                {loading ? <Loader2 className="w-6 h-6 animate-spin" /> : stats.pending}
                            </p>
                        </div>
                        <div className="w-12 h-12 bg-warning-100 rounded-lg flex items-center justify-center">
                            <Clock className="w-6 h-6 text-warning-600" />
                        </div>
                    </div>
                </Card>

                <Card className="hover:shadow-lg transition-shadow">
                    <div className="flex items-center justify-between">
                        <div>
                            <p className="text-sm text-gray-600">Approved</p>
                            <p className="text-2xl font-bold text-success-600 mt-1">
                                {loading ? <Loader2 className="w-6 h-6 animate-spin" /> : stats.approved}
                            </p>
                        </div>
                        <div className="w-12 h-12 bg-success-100 rounded-lg flex items-center justify-center">
                            <CheckCircle className="w-6 h-6 text-success-600" />
                        </div>
                    </div>
                </Card>

                <Card className="hover:shadow-lg transition-shadow">
                    <div className="flex items-center justify-between">
                        <div>
                            <p className="text-sm text-gray-600">Rejected</p>
                            <p className="text-2xl font-bold text-danger-600 mt-1">
                                {loading ? <Loader2 className="w-6 h-6 animate-spin" /> : stats.rejected}
                            </p>
                        </div>
                        <div className="w-12 h-12 bg-danger-100 rounded-lg flex items-center justify-center">
                            <XCircle className="w-6 h-6 text-danger-600" />
                        </div>
                    </div>
                </Card>
            </div>

            {/* Quick Actions */}
            <Card>
                <h2 className="text-xl font-semibold text-gray-900 mb-4">Quick Actions</h2>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                    {isAffiliate && (
                        <Link
                            to="/applications/new"
                            className="p-4 border-2 border-dashed border-gray-300 rounded-lg hover:border-primary-500 hover:bg-primary-50 transition-colors text-center"
                        >
                            <FileText className="w-8 h-8 text-primary-600 mx-auto mb-2" />
                            <p className="font-medium text-gray-900">New Application</p>
                            <p className="text-sm text-gray-600">Request a new credit</p>
                        </Link>
                    )}
                    <Link
                        to="/applications"
                        className="p-4 border-2 border-dashed border-gray-300 rounded-lg hover:border-primary-500 hover:bg-primary-50 transition-colors text-center"
                    >
                        <FileText className="w-8 h-8 text-primary-600 mx-auto mb-2" />
                        <p className="font-medium text-gray-900">View Applications</p>
                        <p className="text-sm text-gray-600">Review all applications</p>
                    </Link>
                    {isAnalyst && (
                        <Link
                            to="/affiliates"
                            className="p-4 border-2 border-dashed border-gray-300 rounded-lg hover:border-primary-500 hover:bg-primary-50 transition-colors text-center"
                        >
                            <FileText className="w-8 h-8 text-primary-600 mx-auto mb-2" />
                            <p className="font-medium text-gray-900">Manage Affiliates</p>
                            <p className="text-sm text-gray-600">View and manage affiliates</p>
                        </Link>
                    )}
                </div>
            </Card>

            {/* Recent Applications */}
            <Card>
                <h2 className="text-xl font-semibold text-gray-900 mb-4">Recent Applications</h2>
                {loading ? (
                    <div className="text-center py-8">
                        <Loader2 className="w-8 h-8 mx-auto animate-spin text-primary-600" />
                        <p className="mt-2 text-gray-600">Loading...</p>
                    </div>
                ) : recentApplications.length === 0 ? (
                    <div className="text-center py-8 text-gray-500">
                        <FileText className="w-12 h-12 mx-auto mb-2 opacity-50" />
                        <p>No recent applications</p>
                        <p className="text-sm">Applications will appear here</p>
                    </div>
                ) : (
                    <div className="overflow-x-auto">
                        <table className="min-w-full divide-y divide-gray-200">
                            <thead className="bg-gray-50">
                                <tr>
                                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">ID</th>
                                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Amount</th>
                                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Status</th>
                                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Date</th>
                                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Actions</th>
                                </tr>
                            </thead>
                            <tbody className="bg-white divide-y divide-gray-200">
                                {recentApplications.map((app) => (
                                    <tr key={app.id} className="hover:bg-gray-50">
                                        <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">#{app.id}</td>
                                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{formatCurrency(app.requestedAmount)}</td>
                                        <td className="px-6 py-4 whitespace-nowrap">
                                            <Badge status={app.status} />
                                        </td>
                                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{formatDate(app.applicationDate)}</td>
                                        <td className="px-6 py-4 whitespace-nowrap text-sm">
                                            <button
                                                onClick={() => handleViewApplication(app)}
                                                className="text-primary-600 hover:text-primary-800 inline-flex items-center"
                                            >
                                                <Eye className="w-4 h-4 mr-1" /> View
                                            </button>
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
                            {isAnalyst && selectedApp.status === 'PENDING' && (
                                <Button
                                    variant="primary"
                                    onClick={() => handleEvaluate(selectedApp.id)}
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
