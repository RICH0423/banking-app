import { createApp, ref } from 'vue'

const LoginForm = {
  setup() {
    const email = ref('')
    const password = ref('')
    const error = ref('')

    const submit = async () => {
      error.value = ''
      try {
        const res = await fetch('http://localhost:8080/api/login', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ email: email.value, password: password.value })
        })
        if (!res.ok) {
          throw new Error('Invalid credentials')
        }
        const account = await res.json()
        alert(`Welcome ${account.name}`)
      } catch (e) {
        error.value = e.message
      }
    }

    return { email, password, error, submit }
  },
  template: `
    <div class="login">
      <h2>Login</h2>
      <div v-if="error" class="error">{{ error }}</div>
      <form @submit.prevent="submit">
        <div>
          <label>Email</label>
          <input v-model="email" type="email" required />
        </div>
        <div>
          <label>Password</label>
          <input v-model="password" type="password" required />
        </div>
        <button type="submit">Login</button>
      </form>
    </div>`
}

createApp(LoginForm).mount('#app')
