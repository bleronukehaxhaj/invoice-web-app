<script setup>
import {computed} from 'vue';
import {useAuthStore} from "@/stores/auth.js";
import {useRouter} from "vue-router";

const authStore = useAuthStore();
const router = useRouter();
const isLoggedIn = computed(() => authStore.isLoggedIn)

const logout = () => {
  authStore.logOut()
  router.push({name: 'home'});
}

</script>

<template>
  <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container">
      <a class="navbar-brand" href="#">Invoice Simple</a>
      <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav"
              aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarNav">
        <ul class="navbar-nav me-auto mb-2 mb-lg-0">
          <li class="nav-item">
            <router-link :to="{name:'home'}" active-class="active" class="nav-link">Home</router-link>
          </li>
          <li class="nav-item" v-if="isLoggedIn">
            <router-link :to="{name:'dashboard'}" active-class="active" class="nav-link">Dashboard</router-link>
          </li>
          <li class="nav-item" v-if="isLoggedIn">
            <router-link :to="{name:'clients'}" active-class="active" class="nav-link">Clients</router-link>
          </li>
          <li class="nav-item" v-if="isLoggedIn">
            <router-link :to="{name:'items'}" active-class="active" class="nav-link">Items</router-link>
          </li>
          <li class="nav-item" v-if="isLoggedIn">
            <router-link :to="{name:'invoices'}" active-class="active" class="nav-link">Invoices</router-link>
          </li>
        </ul>
        <ul class="navbar-nav">
          <li class="nav-item dropdown" v-if="isLoggedIn">
            <a class="nav-link dropdown-toggle" href="#" id="profileDropdown" role="button" data-bs-toggle="dropdown"
               aria-expanded="false">
              <i class="bi bi-person-circle fs-4"></i>
            </a>
            <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="profileDropdown">
              <li>
                <router-link :to="{name:'profile'}" active-class="active" class="dropdown-item">Profile</router-link>
              </li>
              <li><a class="dropdown-item" href="#" @click="logout">Log Out</a></li>
            </ul>
          </li>
          <li class="nav-item" v-if="!isLoggedIn">
            <router-link :to="{name:'login'}" active-class="active" class="nav-link">Login</router-link>
          </li>
          <li class="nav-item" v-if="!isLoggedIn">
            <router-link :to="{name:'register'}" active-class="active" class="nav-link">Sign Up</router-link>
          </li>
        </ul>
      </div>
    </div>
  </nav>

</template>

<style scoped>

</style>
