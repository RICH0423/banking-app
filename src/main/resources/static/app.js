const { createApp } = Vue;

createApp({
    data() {
        return {
            loginForm: { email: '', password: '' },
            transactionForm: { transactionType: '', amount: null, description: '' },
            account: null,
            transactions: [],
            loggedIn: false,
            error: ''
        };
    },
    methods: {
        async login() {
            this.error = '';
            const response = await fetch('api/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(this.loginForm)
            });
            if (response.ok) {
                this.account = await response.json();
                this.loggedIn = true;
                this.loadTransactions();
            } else {
                this.error = 'Invalid credentials';
            }
        },
        async loadTransactions() {
            const res = await fetch(`api/accounts/${this.account.accountId}/transactions`);
            if (res.ok) {
                this.transactions = await res.json();
            }
        },
        formatDate(value) {
            return new Date(value).toLocaleString();
        },
        async submitTransaction() {
            const res = await fetch(`api/accounts/${this.account.accountId}/transactions`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(this.transactionForm)
            });
            if (res.ok) {
                this.account = await res.json();
                this.transactionForm = { transactionType: '', amount: null, description: '' };
                this.loadTransactions();
            }
        },
        logout() {
            this.loggedIn = false;
            this.account = null;
            this.transactions = [];
            this.loginForm = { email: '', password: '' };
        }
    }
}).mount('#app');
