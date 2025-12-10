import { apiClient } from './client';
import { AffiliateRequest, AffiliateResponse } from '../types/auth.types';

export const affiliateApi = {
    getAll: async (): Promise<AffiliateResponse[]> => {
        const response = await apiClient.get<AffiliateResponse[]>('/api/affiliates');
        return response.data;
    },

    create: async (data: AffiliateRequest): Promise<AffiliateResponse> => {
        const response = await apiClient.post<AffiliateResponse>('/api/affiliates', data);
        return response.data;
    },

    getById: async (id: number): Promise<AffiliateResponse> => {
        const response = await apiClient.get<AffiliateResponse>(`/api/affiliates/${id}`);
        return response.data;
    },

    getByDocument: async (document: string): Promise<AffiliateResponse> => {
        const response = await apiClient.get<AffiliateResponse>(`/api/affiliates/document/${document}`);
        return response.data;
    },
};
