<script setup>
import DashboardCards from "@/components/DashboardCards.vue";
import AppTable from "@/components/AppTable.vue";
import Pagination from "@/components/Pagination.vue";
import {useRouter} from "vue-router";
import {onMounted, ref} from "vue";
import invoiceService from "@/services/invoiceService.js";

const router = useRouter();
const invoices = ref([]);
const page = ref(0);
const sizePerPage = ref(5);
const totalPages = ref(1);

const fetchInvoices = async () => {
  try {
    const response = await invoiceService.getAllInvoices(page.value, sizePerPage.value);
    invoices.value = response.content;
    totalPages.value = response.totalPages;
  } catch (error) {
    console.error('Error fetching invoices:', error);
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

onMounted(() => {
  fetchInvoices();
});

</script>

<template>
  <div class="container mt-5">
    <div class="d-flex justify-content-between align-items-center mb-3">
      <h2>Dashboard</h2>
      <div class="d-flex">
        <router-link :to="{ name: 'invoice-create' }" class="btn btn-dark me-2">Create Invoice</router-link>
        <router-link :to="{ name: 'client-create' }" class="btn btn-dark me-2">Add Client</router-link>
        <router-link :to="{ name: 'item-create' }" class="btn btn-dark">Add Item</router-link>
      </div>
    </div>
    <DashboardCards/>
    <h2>Latest invoices</h2>
    <AppTable
        :columns="[
        { label: 'Number', field: 'invoiceNumber' },
        { label: 'Client', field: 'client.name'},
        { label: 'Issue Date', field: 'issueDate', format: 'date'  },
        { label: 'Due Date', field: 'dueDate', format: 'date'  },
        { label: 'Status', field: 'invoiceStatus' },
        { label: 'Total', field: 'total',format: 'currency'},
      ]"
        :data="invoices"
        :actions="actions"
        class="mb-5"
    />

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