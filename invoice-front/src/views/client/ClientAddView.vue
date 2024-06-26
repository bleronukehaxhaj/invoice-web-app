<script setup>
import ClientForm from "@/components/ClientForm.vue";
import {useRouter} from "vue-router";
import clientService from "@/services/clientService.js";

const router = useRouter();

const handleFormSubmit = async (formData) => {
  try {
    const response = await clientService.createClient(formData);
    await router.push({name: 'clients'});
  } catch (error) {
    console.error('Error creating client:', error);
  }
};

</script>

<template>
  <div class="container mt-5">
    <h2>Add New Client</h2>
    <div class="card mb-5 shadow-sm">
      <ClientForm :onSubmit="handleFormSubmit" submitButtonText="Add Client"/>
    </div>
  </div>
</template>

<style scoped>

</style>