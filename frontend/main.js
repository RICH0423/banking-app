import { createApp, ref } from 'vue'

const App = {
  setup() {
    const message = ref('Welcome to Banking App')
    return { message }
  },
  template: `<h1>{{ message }}</h1>`
}

createApp(App).mount('#app')
