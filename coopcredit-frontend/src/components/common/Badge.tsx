import clsx from 'clsx';
import { ApplicationStatus } from '../../types/application.types';

interface BadgeProps {
    status: ApplicationStatus;
    className?: string;
}

export const Badge = ({ status, className }: BadgeProps) => {
    const statusStyles = {
        PENDING: 'bg-warning-100 text-warning-800 border-warning-200',
        APPROVED: 'bg-success-100 text-success-800 border-success-200',
        REJECTED: 'bg-danger-100 text-danger-800 border-danger-200',
        IN_REVIEW: 'bg-primary-100 text-primary-800 border-primary-200',
    };

    const statusLabels = {
        PENDING: 'Pending',
        APPROVED: 'Approved',
        REJECTED: 'Rejected',
        IN_REVIEW: 'In Review',
    };

    return (
        <span
            className={clsx(
                'inline-flex items-center px-3 py-1 rounded-full text-sm font-medium border',
                statusStyles[status],
                className
            )}
        >
            {statusLabels[status]}
        </span>
    );
};
