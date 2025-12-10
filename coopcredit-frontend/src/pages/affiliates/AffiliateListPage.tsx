import { useState, useEffect } from 'react';
import { Users, Search } from 'lucide-react';
import { Card } from '../../components/common/Card';
import { Input } from '../../components/common/Input';
import { affiliateApi } from '../../api/affiliate.api';
import { handleApiError } from '../../api/client';
import { AffiliateResponse } from '../../types/auth.types';

export const AffiliateListPage = () => {
    const [affiliates, setAffiliates] = useState<AffiliateResponse[]>([]);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState<string>('');
    const [searchTerm, setSearchTerm] = useState('');

    useEffect(() => {
        loadAffiliates();
    }, []);

    const loadAffiliates = async () => {
        try {
            setIsLoading(true);
            setError('');
            const data = await affiliateApi.getAll();
            setAffiliates(data);
        } catch (err) {
            setError(handleApiError(err));
        } finally {
            setIsLoading(false);
        }
    };

    const filteredAffiliates = affiliates.filter(affiliate =>
        affiliate.firstName.toLowerCase().includes(searchTerm.toLowerCase()) ||
        affiliate.lastName.toLowerCase().includes(searchTerm.toLowerCase()) ||
        affiliate.document.includes(searchTerm) ||
        affiliate.email.toLowerCase().includes(searchTerm.toLowerCase())
    );

    return (
        <div className="space-y-6">
            <div className="flex justify-between items-center">
                <div>
                    <h1 className="text-3xl font-bold text-gray-900">Affiliates</h1>
                    <p className="text-gray-600 mt-1">Manage cooperative members</p>
                </div>
            </div>

            {error && (
                <div className="p-3 bg-danger-50 border border-danger-200 rounded-lg">
                    <p className="text-sm text-danger-800">{error}</p>
                </div>
            )}

            <Card>
                <div className="mb-4">
                    <Input
                        type="text"
                        placeholder="Search by name, document, or email..."
                        icon={<Search className="w-5 h-5" />}
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                    />
                </div>

                {isLoading ? (
                    <div className="text-center py-8">
                        <p className="text-gray-500">Loading affiliates...</p>
                    </div>
                ) : filteredAffiliates.length === 0 ? (
                    <div className="text-center py-8">
                        <Users className="w-12 h-12 text-gray-400 mx-auto mb-2" />
                        <p className="text-gray-500">No affiliates found</p>
                    </div>
                ) : (
                    <div className="overflow-x-auto">
                        <table className="min-w-full divide-y divide-gray-200">
                            <thead className="bg-gray-50">
                                <tr>
                                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                        Document
                                    </th>
                                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                        Name
                                    </th>
                                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                        Email
                                    </th>
                                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                        Phone
                                    </th>
                                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                        Salary
                                    </th>
                                </tr>
                            </thead>
                            <tbody className="bg-white divide-y divide-gray-200">
                                {filteredAffiliates.map((affiliate) => (
                                    <tr key={affiliate.id} className="hover:bg-gray-50">
                                        <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                                            {affiliate.document}
                                        </td>
                                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                                            {affiliate.firstName} {affiliate.lastName}
                                        </td>
                                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                            {affiliate.email}
                                        </td>
                                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                            {affiliate.phone}
                                        </td>
                                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                            ${affiliate.salary.toLocaleString()}
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>
                )}
            </Card>
        </div>
    );
};
