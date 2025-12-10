// Credit Application types
export interface CreditApplicationRequest {
    affiliateId: number;
    requestedAmount: number;
    termMonths: number;
    purpose?: string;
}

export interface CreditApplicationResponse {
    id: number;
    affiliateId: number;
    requestedAmount: number;
    termMonths: number;
    purpose: string;
    status: ApplicationStatus;
    riskScore?: number;
    approvedAmount?: number;
    applicationDate: string;
    evaluatedAt?: string;
    monthlyPayment?: number;
    riskEvaluation?: RiskEvaluationResponse | null;
}

export type ApplicationStatus = 'PENDING' | 'APPROVED' | 'REJECTED' | 'IN_REVIEW';

export interface RiskEvaluationResponse {
    score: number;
    recommendation: string;
    factors: string[];
}
