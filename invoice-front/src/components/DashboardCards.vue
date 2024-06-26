<script setup>
import {reactive, computed, onMounted} from 'vue';
import dashboardService from "@/services/dashboardService.js";

const metrics = reactive({
  totalClients: 0,
  totalAmount: 0,
  totalDue: 0,
  totalPaid: 0,
  totalItems: 0,
  totalInvoices: 0,
  totalPaidInvoices: 0,
  totalUnpaidInvoices: 0,
});

const cards = computed(() => [
  {title: 'Total Clients', value: metrics.totalClients, icon: 'bi bi-people'},
  {title: 'Total Amount', value: metrics.totalAmount, icon: 'bi bi-cash', isCurrency: true},
  {title: 'Total Due', value: metrics.totalDue, icon: 'bi bi-clock', isCurrency: true},
  {title: 'Total Paid', value: metrics.totalPaid, icon: 'bi bi-check-circle', isCurrency: true},
  {title: 'Total Items', value: metrics.totalItems, icon: 'bi bi-box'},
  {title: 'Total Invoices', value: metrics.totalInvoices, icon: 'bi bi-file-text'},
  {title: 'Paid Invoices', value: metrics.totalPaidInvoices, icon: 'bi bi-file-earmark-check'},
  {title: 'Unpaid Invoices', value: metrics.totalUnpaidInvoices, icon: 'bi bi-file-earmark-excel'},
]);

const fetchMetrics = async () => {
  try {
    const response = await dashboardService.getDashboardStatistics();
    Object.assign(metrics, response);
  } catch (error) {
    console.error('Error fetching metrics:', error);
  }
};

const formatValue = (value, isCurrency) => {
  if (isCurrency) {
    return `€${value.toFixed(2)}`;
  }
  return value.toLocaleString();
};

onMounted(fetchMetrics);
</script>

<template>
  <div class="container mt-5">
    <div class="row">
      <div class="col-md-3" v-for="card in cards" :key="card.title">
        <div class="card mb-4 shadow-lg text-center">
          <div class="card-body">
            <i :class="card.icon" style="font-size: 2rem;"></i>
            <h3 class="card-title mt-2">{{ formatValue(card.value, card.isCurrency) }}</h3>
            <p class="card-text">{{ card.title }}</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>