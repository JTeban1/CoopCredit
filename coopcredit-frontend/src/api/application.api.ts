import { apiClient } from './client';
import { CreditApplicationRequest, CreditApplicationResponse } from '../types/application.types';

export const applicationApi = {
    create: async (data: CreditApplicationRequest): Promise<CreditApplicationResponse> => {
        const response = await apiClient.post<CreditApplicationResponse>('/api/applications', data);
        return response.data;
    },

    getAll: async (): Promise<CreditApplicationResponse[]> => {
        const response = await apiClient.get<CreditApplicationResponse[]>('/api/applications');
        return response.data;
    },

    getById: async (id: number): Promise<CreditApplicationResponse> => {
        const response = await apiClient.get<CreditApplicationResponse>(`/api/applications/${id}`);
        return response.data;
    },

    getByAffiliate: async (affiliateId: number): Promise<CreditApplicationResponse[]> => {
        const response = await apiClient.get<CreditApplicationResponse[]>(`/api/applications/affiliate/${affiliateId}`);
        return response.data;
    },

    evaluate: async (id: number): Promise<CreditApplicationResponse> => {
        const response = await apiClient.post<CreditApplicationResponse>(`/api/applications/${id}/evaluate`);
        return response.data;
    },

    getByStatus: async (status: string): Promise<CreditApplicationResponse[]> => {
        const response = await apiClient.get<CreditApplicationResponse[]>(`/api/applications/status/${status}`);
        return response.data;
    },
};
