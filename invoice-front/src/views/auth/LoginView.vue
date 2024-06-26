<script setup>
import {reactive, ref} from "vue";
import {useRouter} from "vue-router";
import {useAuthStore} from "@/stores/auth.js";

const router = useRouter();
const authStore = useAuthStore();
const user = reactive({
  email: '',
  password: ''
});
const emailError = ref('');
const passwordError = ref('');
const generalError = ref('');

const validateEmail = (email) => {
  const re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@(([^<>()\[\]\\.,;:\s@"]+\.)+[^<>()\[\]\\.,;:\s@"]{2,})$/i;
  return re.test(email);
};

const login = async () => {
  let valid = true;

  if (!validateEmail(user.email)) {
    emailError.value = 'Please enter a valid email address.';
    valid = false;
  } else {
    emailError.value = '';
  }

  if (user.password.length < 6) {
    passwordError.value = 'Password must be at least 6 characters long.';
    valid = false;
  } else {
    passwordError.value = '';
  }

  if (valid) {
    try {
      await authStore.logIn(user);
      await router.push({name: 'dashboard'});
    } catch (error) {
      console.error('Login error:', error);
      if (error.response && error.response.status === 401) {
        generalError.value = 'Invalid email or password. Please try again.';
      } else {
        generalError.value = 'An error occurred during login. Please try again later.';
      }
    }
  }
};

const cancel = () => {
  router.push({name: 'home'});
};
</script>

<template>
  <div class="d-flex justify-content-center align-items-center vh-100">
    <div class="card p-4" style="max-width: 400px; width: 100%;">
      <h4 class="mb-3 text-center">Login</h4>
      <form @submit.prevent="login" novalidate>
        <div class="mb-3">
          <label for="email" class="form-label">Email</label>
          <input type="email" class="form-control" id="email" v-model.trim="user.email"
                 :class="{'is-invalid': emailError}" placeholder="Email">
          <div class="invalid-feedback">{{ emailError }}</div>
        </div>
        <div class="mb-3">
          <label for="password" class="form-label">Password</label>
          <input type="password" class="form-control" id="password" v-model.trim="user.password"
                 :class="{'is-invalid': passwordError}" placeholder="Password">
          <div class="invalid-feedback">{{ passwordError }}</div>
        </div>
        <div v-if="generalError" class="alert alert-danger">{{ generalError }}</div>
        <div class="d-grid gap-2">
          <button type="submit" class="btn btn-dark">Login</button>
          <button type="button" class="btn btn-light" @click="cancel">Cancel</button>
        </div>
      </form>
      <div class="text-center mt-3">
        <span>Don't have an account?</span>
        <router-link :to="{ name: 'register' }" class="btn btn-sm btn-outline-dark">Sign Up</router-link>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>