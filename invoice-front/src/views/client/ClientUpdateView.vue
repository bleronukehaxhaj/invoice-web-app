<script setup>
import {useRoute, useRouter} from "vue-router";
import {onMounted, ref} from "vue";
import clientService from "@/services/clientService.js";
import ClientForm from "@/components/ClientForm.vue";

const router = useRouter();
const route = useRoute();
const clientData = ref(null);
const loading = ref(true);
const clientId = route.params.id;

onMounted(async () => {


  try {
    clientData.value = await clientService.getClientById(clientId);
  } catch (error) {
    console.error('Error fetching client data:', error);
  } finally {
    loading.value = false;
  }
});

const handleFormSubmit = async (formData) => {
  loading.value = true;

  try {
    await clientService.updateClient(clientId, formData);
    console.log('Client updated successfully');
    await router.push({name: 'clients'});
  } catch (error) {
    console.error('Error updating client:', error);
  } finally {
    loading.value = false;
  }
};
</script>

<template>
  <div class="container mt-5">
    <h2>Edit Client</h2>
    <div v-if="loading" class="text-center mt-3">
      <span>Loading...</span>
    </div>
    <div v-else>
      <div class="card mb-5 shadow-sm">
        <ClientForm
            v-if="clientData"
            :initialData="clientData"
            :onSubmit="handleFormSubmit"
            submitButtonText="Update Client"/>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>