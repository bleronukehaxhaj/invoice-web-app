<script setup>
import {useRouter} from "vue-router";
import BusinessForm from "@/components/BusinessForm.vue";
import businessService from "@/services/businessService.js";
import {useAuthStore} from "@/stores/auth.js";

const router = useRouter();
const authStore = useAuthStore();

const handleFormSubmit = async (formData) => {
  try {
    const response = await businessService.createBusiness(formData);
    await authStore.checkUserBusiness();
    await router.push({name: 'dashboard'});
  } catch (error) {
    console.error('Error creating business:', error);
  }
};

</script>

<template>
  <div class="container mt-5">
    <h2>Add Business</h2>
    <div class="card mb-5">
      <BusinessForm :onSubmit="handleFormSubmit" submitButtonText="Add Business"/>
    </div>
  </div>
</template>

<style scoped>

</style>