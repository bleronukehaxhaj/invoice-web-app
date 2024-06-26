<script setup>
import InvoiceForm from "@/components/InvoiceForm.vue";
import {useRouter} from "vue-router";
import invoiceService from "@/services/invoiceService.js";

const router = useRouter();

const handleFormSubmit = async (formData) => {
  try {
    await invoiceService.createInvoice(formData);
    await router.push({name: 'invoices'});
  } catch (error) {
    console.error('Error adding invoice:', error);
  }
};
</script>

<template>
  <div class="container mt-5 mb-5">
    <div class="card m-5 shadow-sm">
      <InvoiceForm :onSubmit="handleFormSubmit"/>
    </div>
  </div>
</template>

<style scoped>

</style>