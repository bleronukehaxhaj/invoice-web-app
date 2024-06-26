<script setup>
import {reactive, ref} from "vue";
import {useRouter} from "vue-router";
import {useAuthStore} from "@/stores/auth.js";

const router = useRouter();
const authStore = useAuthStore();

const user = reactive({
  firstName: '',
  lastName: '',
  email: '',
  password: ''
});

const firstNameError = ref('');
const lastNameError = ref('');
const emailError = ref('');
const passwordError = ref('');
const generalError = ref('');

const validateEmail = (email) => {
  const re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@(([^<>()\[\]\\.,;:\s@"]+\.)+[^<>()\[\]\\.,;:\s@"]{2,})$/i;
  return re.test(email);
};

const signUp = async () => {
  let valid = true;

  if (!user.firstName) {
    firstNameError.value = 'First name is required.';
    valid = false;
  } else {
    firstNameError.value = '';
  }

  if (!user.lastName) {
    lastNameError.value = 'Last name is required.';
    valid = false;
  } else {
    lastNameError.value = '';
  }

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
      await authStore.register(user);
      await router.push({name: 'business-create'});
    } catch (error) {
      console.error('Signup error:', error);
      if (error.message === 'Email already exists') {
        emailError.value = 'An account with this email already exists.';
      } else {
        generalError.value = 'An error occurred during registration. Please try again later.';
      }
    }
  }
};

const cancel = () => {
  router.push({name: 'home'});
};
</script>

<template>
  <div class="d-flex justify-content-center align-items-center">
    <div class="card p-4 m-5" style="max-width: 400px; width: 100%;">
      <h4 class="mb-3 text-center">Sign Up</h4>
      <form @submit.prevent="signUp" novalidate>
        <div class="mb-3">
          <label for="firstName" class="form-label">First Name</label>
          <input type="text" class="form-control" id="firstName" v-model.trim="user.firstName"
                 :class="{'is-invalid': firstNameError}" placeholder="First Name">
          <div class="invalid-feedback">{{ firstNameError }}</div>
        </div>
        <div class="mb-3">
          <label for="lastName" class="form-label">Last Name</label>
          <input type="text" class="form-control" id="lastName" v-model.trim="user.lastName"
                 :class="{'is-invalid': lastNameError}" placeholder="Last Name">
          <div class="invalid-feedback">{{ lastNameError }}</div>
        </div>
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
          <button type="submit" class="btn btn-dark">Sign Up</button>
          <button type="button" class="btn btn-light" @click="cancel">Cancel</button>
        </div>
      </form>
      <div class="text-center mt-3">
        <span>Already have an account?</span>
        <router-link :to="{ name: 'login' }" class="btn btn-sm btn-outline-dark">Login</router-link>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>