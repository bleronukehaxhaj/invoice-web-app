<script setup>
import {onMounted, ref} from 'vue';
import AppTable from "@/components/AppTable.vue";
import {useRouter} from "vue-router";
import clientService from "@/services/clientService.js";
import Pagination from "@/components/Pagination.vue";


const router = useRouter();
const searchQuery = ref('');
const clients = ref([]);
const loading = ref(false);
const page = ref(0);
const sizePerPage = ref(5);
const totalPages = ref(1);

const fetchClients = async () => {
  loading.value = true;
  try {
    const response = await clientService.getAllClients(page.value, sizePerPage.value);
    clients.value = response.content;
    totalPages.value = response.totalPages;
  } catch (error) {
    console.error('Error fetching clients:', error);
  } finally {
    loading.value = false;
  }
};

const viewClient = (client) => {
  router.push({name: 'client-details', params: {id: client.id}});
};

const editClient = (client) => {
  router.push({name: 'client-edit', params: {id: client.id}});
};

const deleteClient = async (client) => {
  const confirmed = window.confirm(`Are you sure you want to delete ${client.name}?`);
  if (confirmed) {
    try {
      await clientService.deleteClient(client.id);
      await fetchClients();
    } catch (err) {
      console.error('Error deleting client:', err);
    }
  }
};

const addClient = () => {
  router.push({name: 'client-create'});
};

const searchClients = async () => {
  if (!searchQuery.value) {
    await fetchClients();
    return;
  }
  loading.value = true;
  try {
    const response = await clientService.searchClientsByName(searchQuery.value);
    clients.value = response.content;
    totalPages.value = response.totalPages;
  } catch (error) {
    console.error('Error searching clients:', error);
  } finally {
    loading.value = false;
  }
};

const actions = [
  {
    label: 'View',
    handler: viewClient,
    class: 'btn-dark',
    icon: 'bi bi-eye'
  },
  {
    label: 'Edit',
    handler: editClient,
    class: 'btn-secondary',
    icon: 'bi bi-pencil'
  },
  {
    label: 'Delete',
    handler: deleteClient,
    class: 'btn-danger',
    icon: 'bi bi-trash'
  }
];

const prevPage = () => {
  if (page.value > 0) {
    page.value -= 1;
    fetchClients();
  }
};

const nextPage = () => {
  if (page.value < totalPages.value - 1) {
    page.value += 1;
    fetchClients();
  }
};

const goToPage = (pageNumber) => {
  if (page.value !== pageNumber) {
    page.value = pageNumber;
    fetchClients();
  }
};

onMounted(() => {
  fetchClients();
});
</script>

<template>
  <div class="container mt-5">
    <div class="d-flex justify-content-between align-items-center mb-3">
      <h2>Clients</h2>
      <div class="d-flex">
        <input type="text" class="form-control me-2" placeholder="Search by Client Name" v-model.trim="searchQuery"
               style="width: 200px;">
        <button class="btn btn-dark me-2" @click="searchClients">Search</button>
        <button class="btn btn-dark" @click="addClient">New Client</button>
      </div>
    </div>
    <AppTable
        :columns="[
      { label: 'Name', field: 'name' },
      { label: 'Email', field: 'email' },
      { label: 'Address', field: 'address' },
      { label: 'Phone', field: 'phone' },
      { label: 'Type', field: 'clientType' },
    ]"
        :data="clients"
        :actions="actions"
    />
    <div v-if="loading" class="text-center mt-3">
      <span>Loading...</span>
    </div>
    <Pagination v-if="totalPages > 1"
                :totalPages="totalPages"
                :currentPage="page"
                @prevPage="prevPage"
                @nextPage="nextPage"
                @goToPage="goToPage"
                class="mt-3"/>
  </div>
</template>

<style scoped>

</style>