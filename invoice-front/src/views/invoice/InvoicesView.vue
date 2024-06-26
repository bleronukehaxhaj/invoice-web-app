<script setup>
import {onMounted, ref} from 'vue';
import AppTable from "@/components/AppTable.vue";
import {useRouter} from "vue-router";
import invoiceService from "@/services/invoiceService.js";
import Pagination from "@/components/Pagination.vue";


const router = useRouter();
const searchQuery = ref('');
const invoices = ref([]);
const loading = ref(false);
const page = ref(0);
const sizePerPage = ref(5);
const totalPages = ref(1);

const fetchInvoices = async () => {
  loading.value = true;
  try {
    const response = await invoiceService.getAllInvoices(page.value, sizePerPage.value);
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

const addInvoice = () => {
  router.push({name: 'invoice-create'});
};

const updateInvoiceStatus = async (invoice) => {
  const newStatus = invoice.invoiceStatus === 'PAID' ? 'PENDING' : 'PAID';
  try {
    await invoiceService.updateInvoiceStatus(invoice.id, newStatus);
    console.log(`Invoice status updated to ${newStatus}`);
    await fetchInvoices();
  } catch (error) {
    console.error('Error updating invoice status:', error);
  }
};

const getStatusButtonLabel = (invoice) => {
  if (invoice.invoiceStatus === 'PAID') {
    return 'Mark as Pending';
  } else if (invoice.invoiceStatus === 'PENDING') {
    return 'Mark as Paid';
  } else {
    return 'Mark as Paid';
  }
};

const searchInvoices = async () => {
  if (!searchQuery.value) {
    await fetchInvoices();
    return;
  }
  loading.value = true;
  try {
    const response = await invoiceService.searchInvoicesByClientName(searchQuery.value);
    invoices.value = response.content;
    totalPages.value = response.totalPages;
  } catch (error) {
    console.error('Error searching invoices:', error);
  } finally {
    loading.value = false;
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
  },
  {
    label: (invoice) => getStatusButtonLabel(invoice),
    handler: updateInvoiceStatus,
    class: 'btn-info',
    icon: 'bi bi-arrow-repeat'
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
      <h2>Invoices</h2>
      <div class="d-flex">
        <input type="text" class="form-control me-2"
               placeholder="Search by Client Name"
               v-model.trim="searchQuery"
               style="width: 200px;">
        <button class="btn btn-dark me-2" @click="searchInvoices">Search</button>
        <button class="btn btn-dark" @click="addInvoice">New Invoice</button>
      </div>
    </div>
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