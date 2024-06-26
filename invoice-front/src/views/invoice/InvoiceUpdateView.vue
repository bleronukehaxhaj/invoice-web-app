<script setup>
import {ref, onMounted} from 'vue';
import {useRoute, useRouter} from 'vue-router';
import invoiceService from '@/services/invoiceService.js';
import InvoiceForm from '@/components/InvoiceForm.vue';

const route = useRoute();
const router = useRouter();
const invoiceData = ref(null);
const loading = ref(true);
const error = ref(null);

const mapResponseToFormData = (response) => {
  return {
    id: response.id,
    invoiceNumber: response.invoiceNumber,
    issueDate: response.issueDate.substring(0, 10),
    dueDate: response.dueDate.substring(0, 10),
    invoiceStatus: response.invoiceStatus,
    subtotal: response.subtotal,
    discount: response.discount,
    tax: response.tax,
    total: response.total,
    business: {
      name: response.business.name,
      email: response.business.email,
      address: response.business.address,
      phone: response.business.phone
    },
    client: {
      id: response.client.id,
      name: response.client.name,
      email: response.client.email,
      address: response.client.address,
      phone: response.client.phone
    },
    invoiceItems: response.invoiceItems.map(item => ({
      description: item.item.description,
      rate: item.itemRate,
      quantity: item.quantity,
      itemId: item.item.id
    }))
  };
};

onMounted(async () => {
  const invoiceId = route.params.id;
  try {
    const response = await invoiceService.getInvoiceById(invoiceId);
    invoiceData.value = mapResponseToFormData(response);
  } catch (err) {
    error.value = 'Error fetching invoice data.';
  } finally {
    loading.value = false;
  }
});

const handleFormSubmit = async (formData) => {
  const invoiceId = route.params.id;
  try {
    await invoiceService.updateInvoice(invoiceId, formData);
    await router.push({name: 'invoices'});
  } catch (err) {
    error.value = 'Error updating invoice.';
  }
};
</script>

<template>
  <div class="container mt-5">
    <h2>Edit Invoice</h2>
    <div v-if="loading" class="text-center mt-3">
      <span>Loading...</span>
    </div>
    <div v-else>
      <div class="card m-5 shadow-sm">
        <InvoiceForm
            :initialData="invoiceData"
            :onSubmit="handleFormSubmit"
            :isUpdate="true"
            submitButtonText="Update Invoice"
        />
      </div>
      <div v-if="error" class="alert alert-danger mt-3">
        {{ error }}
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>