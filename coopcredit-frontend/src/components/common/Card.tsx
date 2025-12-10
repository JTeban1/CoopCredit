import { ReactNode } from 'react';
import clsx from 'clsx';

interface CardProps {
    children: ReactNode;
    variant?: 'default' | 'elevated' | 'bordered';
    className?: string;
    header?: ReactNode;
    footer?: ReactNode;
}

export const Card = ({
    children,
    variant = 'default',
    className,
    header,
    footer,
}: CardProps) => {
    const variantStyles = {
        default: 'bg-white shadow-md',
        elevated: 'bg-white shadow-xl',
        bordered: 'bg-white border-2 border-gray-200',
    };

    return (
        <div
            className={clsx(
                'rounded-xl overflow-hidden transition-shadow duration-300',
                variantStyles[variant],
                className
            )}
        >
            {header && (
                <div className="px-6 py-4 border-b border-gray-200 bg-gray-50">
                    {header}
                </div>
            )}
            <div className="p-6">{children}</div>
            {footer && (
                <div className="px-6 py-4 border-t border-gray-200 bg-gray-50">
                    {footer}
                </div>
            )}
        </div>
    );
};
