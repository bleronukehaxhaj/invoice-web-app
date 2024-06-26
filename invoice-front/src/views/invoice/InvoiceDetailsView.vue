<script setup>
import {onMounted, ref} from 'vue';
import {useRoute, useRouter} from 'vue-router';
import html2pdf from 'html2pdf.js';
import invoiceService from '@/services/invoiceService';

const router = useRouter();
const route = useRoute();
const invoice = ref({});
const invoiceContent = ref(null);

const fetchInvoice = async (id) => {
  try {
    invoice.value = await invoiceService.getInvoiceById(id)
  } catch (error) {
    console.error('Failed to fetch invoice:', error);
  }
};


const generatePDF = () => {
  const element = invoiceContent.value;
  const options = {
    margin: [0, 0, 0, 0],
    filename: `invoice_${invoice.value.invoiceNumber}.pdf`,
    image: {type: 'jpeg', quality: 0.98},
    html2canvas: {scale: 2, useCORS: true},
    jsPDF: {unit: 'mm', format: 'a4', orientation: 'portrait'}
  };

  html2pdf().set(options).from(element).save();
};

const printInvoice = () => {
  window.print();
};

const goBack = () => {
  router.push({name: 'invoices'});
};

onMounted(() => {
  const invoiceId = route.params.id;
  fetchInvoice(invoiceId);
});

</script>

<template>
  <div class="container">
    <div class="d-flex justify-content-end align-items-center mb-4 mt-5">
      <button @click="generatePDF" class="btn btn-dark me-3"><i class="bi bi-file-earmark-arrow-down"></i> PDF</button>
      <button @click="printInvoice" class="btn btn-dark me-3"><i class="bi bi-printer"></i> Print</button>
      <button @click="goBack" class="btn btn-secondary">Back</button>
    </div>

    <div ref="invoiceContent" v-if="invoice && invoice.business"
         class="invoice p-5 m-5 rounded invoice-content shadow-lg">
      <div class="d-flex justify-content-between align-items-start mb-4">
        <div>
          <h1 class="fw-bold text-secondary mb-5">INVOICE</h1>
          <div class="mt-2">
            <h3 class="text-primary">{{ invoice.business.name }}</h3>
            <p class="text-muted">{{ invoice.business.address }}</p>
            <p class="text-muted">{{ invoice.business.email }}</p>
            <p class="text-muted">{{ invoice.business.phone }}</p>
          </div>
        </div>
      </div>
      <div class="row mb-4">
        <div class="col">
          <h2 class="text-muted">Bill To</h2>
          <h3 class="text-primary">{{ invoice.client.name }}</h3>
          <p class="text-muted">{{ invoice.client.address }}</p>
          <p class="text-muted">{{ invoice.client.email }}</p>
          <p class="text-muted">{{ invoice.client.phone }}</p>
        </div>
        <div class="col text-end">
          <div>
            <h4 class="text-muted">Invoice #</h4>
            <h4 class="text-primary">{{ invoice.invoiceNumber }}</h4>
          </div>
          <div>
            <h4 class="text-muted">Invoice Date</h4>
            <h5 class="text-primary">{{ new Date(invoice.issueDate).toLocaleDateString() }}</h5>
          </div>
          <div>
            <h4 class="text-muted">Invoice Due Date</h4>
            <h5 class="text-primary">{{ new Date(invoice.dueDate).toLocaleDateString() }}</h5>
          </div>
        </div>
      </div>
      <div class="table-responsive mb-4">
        <table class="table table-bordered">
          <thead class="table-primary">
          <tr>
            <th>Description</th>
            <th>Quantity</th>
            <th>Price</th>
            <th>Amount</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="item in invoice.invoiceItems" :key="item.id">
            <td>{{ item.item.description }}</td>
            <td>{{ item.quantity }} / {{ item.item.unit }}</td>
            <td>€{{ item.itemRate.toFixed(2) }}</td>
            <td>€{{ item.totalPrice.toFixed(2) }}</td>
          </tr>
          </tbody>
        </table>
      </div>
      <div class="d-flex justify-content-end">
        <div class="text-end">
          <div class="d-flex justify-content-between">
            <h4 class="text-muted">Subtotal</h4>
            <h4 class="text-primary">€{{ invoice.subtotal.toFixed(2) }}</h4>
          </div>
          <div class="d-flex justify-content-between">
            <h4 class="text-muted me-5">Discount ({{ invoice.discount }}%)</h4>
            <h4 class="text-primary">€{{ ((invoice.discount / 100) * invoice.subtotal).toFixed(2) }}</h4>
          </div>
          <div class="d-flex justify-content-between">
            <h4 class="text-muted">Tax ({{ invoice.tax }}%)</h4>
            <h4 class="text-primary">€{{
                ((invoice.subtotal - (invoice.discount / 100) * invoice.subtotal) * (invoice.tax / 100)).toFixed(2)
              }}</h4>
          </div>
          <div class="d-flex justify-content-between">
            <h4 class="text-muted">Total</h4>
            <h4 class="text-primary">€{{ invoice.total.toFixed(2) }}</h4>
          </div>
        </div>
      </div>
    </div>
    <div v-else>
      <p>Loading...</p>
    </div>
  </div>
</template>


<style scoped>
@media print {
  body * {
    visibility: hidden;
  }

  .invoice-content, .invoice-content * {
    visibility: visible;
  }

  .invoice-content {
    position: absolute;
    left: 0;
    top: 0;
    width: 100%;
  }
}
</style>
