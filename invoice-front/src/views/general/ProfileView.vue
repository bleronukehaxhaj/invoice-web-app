<script setup>
import {ref, onMounted} from 'vue';
import businessService from '@/services/businessService.js';
import BusinessForm from "@/components/BusinessForm.vue";
import userService from "@/services/userService.js";

const businessData = ref(null);
const userData = ref(null);

const fetchUserData = async () => {
  try {
    userData.value = await userService.getUserDetails();
  } catch (error) {
    console.error('Error fetching user data:', error);
  }
};

const fetchBusinessData = async () => {
  try {
    businessData.value = await businessService.getMyBusiness();
  } catch (error) {
    console.error('Error fetching business data:', error);
  }
};

const handleBusinessFormSubmit = async (formData) => {
  try {
    businessData.value = await businessService.updateBusiness(formData.id, formData);
  } catch (error) {
    console.error('Error updating business:', error);
  }
};

onMounted(async () => {
  await fetchUserData();
  await fetchBusinessData();
});
</script>

<template>
  <div class="container mt-5">
    <div class="row">
      <div class="col-md-4">
        <div class="card mb-4 shadow-sm" v-if="userData">
          <div class="card-body text-center">
            <i class="bi bi-person-circle" style="font-size: 4rem;"></i>
            <h4>{{ userData.firstName }}</h4>
            <p>{{ userData.lastName }}</p>
            <div class="mt-3 text-start">
              <p><strong>Email:</strong> {{ userData.email }}</p>
            </div>
          </div>
        </div>
        <div class="card mb-4 shadow-sm" v-if="businessData">
          <div class="card-body text-center">
            <h4>Business Details</h4>
            <p><strong>Name:</strong> {{ businessData.name }}</p>
            <p><strong>Email:</strong> {{ businessData.email }}</p>
            <p><strong>Address:</strong> {{ businessData.address }}</p>
            <p><strong>Phone:</strong> {{ businessData.phone }}</p>
          </div>
        </div>
      </div>
      <div class="col-md-8">
        <div class="card mb-4 shadow-sm">
          <div class="card-body">
            <h5 class="card-title">Update Business</h5>
            <BusinessForm v-if="businessData"
                          :initialData="businessData"
                          :onSubmit="handleBusinessFormSubmit"
                          submitButtonText="Update"/>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.bi-person-circle {
  font-size: 5rem;
}
</style>
