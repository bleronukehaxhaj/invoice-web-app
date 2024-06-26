<script setup>
import {ref, onMounted, computed} from 'vue';
import ClientForm from '@/components/ClientForm.vue';
import clientService from '@/services/clientService';
import {useRoute, useRouter} from "vue-router";
import AppTable from "@/components/AppTable.vue";
import invoiceService from "@/services/invoiceService.js";
import Pagination from "@/components/Pagination.vue";

const route = useRoute();
const router = useRouter();

const clientData = ref(null);
const invoiceCount = ref(0);
const totalBilled = ref(0);
const invoices = ref([]);
const loading = ref(false);
const page = ref(0);
const sizePerPage = ref(5);
const totalPages = ref(1);
const clientId = route.params.id;


const fetchClientData = async () => {
  try {
    clientData.value = await clientService.getClientById(clientId);
    const stats = await clientService.getClientStatistics(clientId);
    invoiceCount.value = stats.clientTotalInvoices;
    totalBilled.value = stats.clientTotalBilled;
  } catch (error) {
    console.error('Error fetching initial data:', error);
  }
};

const handleFormSubmit = async (formData) => {
  try {
    clientData.value = await clientService.updateClient(clientId, formData);
  } catch (error) {
    console.error('Error updating client:', error);
  }
};

const formatTotalBilled = computed(() => {
  return `€${parseFloat(totalBilled.value).toFixed(2)}`;
});

const fetchInvoices = async () => {
  loading.value = true;
  try {
    const response = await invoiceService.getAllClientInvoices(clientId, page.value, sizePerPage.value);
    invoices.value = response.content;
    totalPages.value = response.totalPages;
  } catch (error) {
    console.error('Error fetching invoices:', error);
  } finally {
    loading.value = false;
  }
};

const viewInvoice = (invoice) => {
  router.push({name: 'invoice-details', params: {id: invoice.id}});
};

const editInvoice = (invoice) => {
  router.push({name: 'invoice-edit', params: {id: invoice.id}});
};

const deleteInvoice = async (invoice) => {
  const confirmed = window.confirm(`Are you sure you want to delete ${invoice.invoiceNumber}?`);
  if (confirmed) {
    try {
      await invoiceService.deleteInvoice(invoice.id);
      console.log('Invoice deleted successfully');
      await fetchInvoices();
    } catch (err) {
      console.error('Error deleting invoice:', err);
    }
  }
};

const actions = [
  {
    label: 'View',
    handler: viewInvoice,
    class: 'btn-dark',
    icon: 'bi bi-eye'
  },
  {
    label: 'Edit',
    handler: editInvoice,
    class: 'btn-secondary',
    icon: 'bi bi-pencil'
  },
  {
    label: 'Delete',
    handler: deleteInvoice,
    class: 'btn-danger',
    icon: 'bi bi-trash'
  }
];

const prevPage = () => {
  if (page.value > 0) {
    page.value -= 1;
    fetchInvoices();
  }
};

const nextPage = () => {
  if (page.value < totalPages.value - 1) {
    page.value += 1;
    fetchInvoices();
  }
};

const goToPage = (pageNumber) => {
  if (page.value !== pageNumber) {
    page.value = pageNumber;
    fetchInvoices();
  }
};

onMounted(async () => {
  await fetchClientData();
  await fetchInvoices();
});

</script>

<template>
  <div class="container mt-5">
    <div class="row">
      <div class="col-md-4">
        <div class="card mb-4 shadow-sm" v-if="clientData">
          <div class="card-body text-center">
            <i class="bi bi-person-circle" style="font-size: 4rem;"></i>
            <h4>{{ clientData.name }}</h4>
            <p>{{ clientData.email }}</p>
            <span class="badge bg-info">{{ clientData.clientType }}</span>
            <div class="mt-3 text-start">
              <p><strong>Address:</strong> {{ clientData.address }}</p>
              <p><strong>Phone:</strong> {{ clientData.phone }}</p>
            </div>
          </div>
        </div>
        <div class="card mb-4 shadow-sm">
          <div class="card-body text-center">
            <h5>Total Invoices</h5>
            <p>Client Total Invoices</p>
            <h3>Total Invoices: {{ invoiceCount }}</h3>
          </div>
        </div>
        <div class="card mb-4 shadow-sm">
          <div class="card-body text-center">
            <h5>Total Billed</h5>
            <p>Client Total Billed</p>
            <h3>Total Billed: {{ formatTotalBilled }}</h3>
          </div>
        </div>
      </div>
      <div class="col-md-8">
        <div class="card mb-4 shadow-sm">
          <div class="card-body">
            <h5 class="card-title">Profile Details</h5>
            <ClientForm v-if="clientData"
                        :initialData="clientData"
                        :onSubmit="handleFormSubmit"
                        submitButtonText="Update"/>
          </div>
        </div>
      </div>
    </div>


    <AppTable
        :columns="[
        { label: 'Number', field: 'invoiceNumber' },
        { label: 'Client', field: 'client.name'},
        { label: 'Issue Date', field: 'issueDate', format: 'date'  },
        { label: 'Due Date', field: 'dueDate', format: 'date'  },
        { label: 'Status', field: 'invoiceStatus'},
        { label: 'Total', field: 'total', format: 'currency'},
      ]"
        :data="invoices"
        :actions="actions"
        class="mb-5"
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
.bi-person-circle {
  font-size: 5rem;
}
</style>
