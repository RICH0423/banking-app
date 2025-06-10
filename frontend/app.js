const { createApp } = Vue;
createApp({
    data() {
        return {
            email: '',
            password: '',
            loggedIn: false,
            account: {},
            transactions: [],
            newTransaction: {
                transactionType: 'DEPOSIT',
                amount: 0,
                description: ''
            }
        };
    },
    methods: {
        async login() {
            const res = await fetch('/api/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ email: this.email, password: this.password })
            });
            if (res.ok) {
                this.account = await res.json();
                this.loggedIn = true;
                this.fetchTransactions();
            } else {
                alert('Login failed');
            }
        },
        async fetchTransactions() {
            const res = await fetch(`/api/accounts/${this.account.accountId}/transactions`);
            if (res.ok) {
                this.transactions = await res.json();
            }
        },
        async submitTransaction() {
            const res = await fetch(`/api/accounts/${this.account.accountId}/transactions`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(this.newTransaction)
            });
            if (res.ok) {
                this.fetchTransactions();
                const balanceChange = this.newTransaction.transactionType === 'DEPOSIT'
                    ? this.newTransaction.amount
                    : -this.newTransaction.amount;
                this.account.balance += balanceChange;
                this.newTransaction.amount = 0;
                this.newTransaction.description = '';
            } else {
                alert('Transaction failed');
            }
        }
    }
}).mount('#app');
