import { Link, useNavigate } from 'react-router-dom';
import { LogOut, User, Menu, X } from 'lucide-react';
import { useState } from 'react';
import { useAuth } from '../../contexts/AuthContext';
import { Button } from '../common/Button';

export const Header = () => {
    const { user, logout } = useAuth();
    const navigate = useNavigate();
    const [isMenuOpen, setIsMenuOpen] = useState(false);

    const handleLogout = () => {
        logout();
        navigate('/login');
    };

    return (
        <header className="sticky top-0 z-40 bg-white border-b border-gray-200 backdrop-blur-sm bg-white/95">
            <div className="container mx-auto px-4">
                <div className="flex items-center justify-between h-16">
                    {/* Logo */}
                    <Link to="/dashboard" className="flex items-center space-x-2">
                        <div className="w-8 h-8 bg-primary-600 rounded-lg flex items-center justify-center">
                            <span className="text-white font-bold text-lg">C</span>
                        </div>
                        <span className="text-xl font-bold text-gray-900">
                            {import.meta.env.VITE_APP_NAME || 'CoopCredit'}
                        </span>
                    </Link>

                    {/* Desktop Navigation */}
                    <nav className="hidden md:flex items-center space-x-6">
                        <Link
                            to="/dashboard"
                            className="text-gray-700 hover:text-primary-600 font-medium transition-colors"
                        >
                            Dashboard
                        </Link>
                        <Link
                            to="/applications"
                            className="text-gray-700 hover:text-primary-600 font-medium transition-colors"
                        >
                            Applications
                        </Link>
                        {user?.roles.some((r) => r === 'ROLE_ADMIN' || r === 'ROLE_ANALISTA') && (
                            <Link
                                to="/affiliates"
                                className="text-gray-700 hover:text-primary-600 font-medium transition-colors"
                            >
                                Affiliates
                            </Link>
                        )}
                    </nav>

                    {/* User Menu */}
                    <div className="hidden md:flex items-center space-x-4">
                        <div className="flex items-center space-x-2 text-sm">
                            <User className="w-4 h-4 text-gray-500" />
                            <span className="text-gray-700">{user?.username}</span>
                        </div>
                        <Button variant="ghost" size="sm" onClick={handleLogout}>
                            <LogOut className="w-4 h-4 mr-2" />
                            Logout
                        </Button>
                    </div>

                    {/* Mobile Menu Button */}
                    <button
                        className="md:hidden p-2 text-gray-700 hover:bg-gray-100 rounded-lg"
                        onClick={() => setIsMenuOpen(!isMenuOpen)}
                    >
                        {isMenuOpen ? <X className="w-6 h-6" /> : <Menu className="w-6 h-6" />}
                    </button>
                </div>

                {/* Mobile Menu */}
                {isMenuOpen && (
                    <div className="md:hidden py-4 border-t border-gray-200">
                        <nav className="flex flex-col space-y-2">
                            <Link
                                to="/dashboard"
                                className="px-4 py-2 text-gray-700 hover:bg-gray-100 rounded-lg"
                                onClick={() => setIsMenuOpen(false)}
                            >
                                Dashboard
                            </Link>
                            <Link
                                to="/applications"
                                className="px-4 py-2 text-gray-700 hover:bg-gray-100 rounded-lg"
                                onClick={() => setIsMenuOpen(false)}
                            >
                                Applications
                            </Link>
                            {user?.roles.some((r) => r === 'ROLE_ADMIN' || r === 'ROLE_ANALISTA') && (
                                <Link
                                    to="/affiliates"
                                    className="px-4 py-2 text-gray-700 hover:bg-gray-100 rounded-lg"
                                    onClick={() => setIsMenuOpen(false)}
                                >
                                    Affiliates
                                </Link>
                            )}
                            <div className="px-4 py-2 border-t border-gray-200 mt-2">
                                <div className="flex items-center space-x-2 text-sm mb-2">
                                    <User className="w-4 h-4 text-gray-500" />
                                    <span className="text-gray-700">{user?.username}</span>
                                </div>
                                <Button variant="ghost" size="sm" onClick={handleLogout} className="w-full">
                                    <LogOut className="w-4 h-4 mr-2" />
                                    Logout
                                </Button>
                            </div>
                        </nav>
                    </div>
                )}
            </div>
        </header>
    );
};
