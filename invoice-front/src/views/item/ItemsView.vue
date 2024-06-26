<script setup>
import {onMounted, ref} from 'vue';
import AppTable from "@/components/AppTable.vue";
import {useRouter} from "vue-router";
import itemService from "@/services/itemService.js";
import Pagination from "@/components/Pagination.vue";


const router = useRouter();
const searchQuery = ref('');
const items = ref([]);
const loading = ref(false);
const page = ref(0);
const sizePerPage = ref(5);
const totalPages = ref(1);

const fetchItems = async () => {
  loading.value = true;
  try {
    const response = await itemService.getAllItems(page.value, sizePerPage.value);
    items.value = response.content;
    totalPages.value = response.totalPages;
  } catch (error) {
    console.error('Error fetching items:', error);
  } finally {
    loading.value = false;
  }
};


const editItem = (item) => {
  router.push({name: 'item-edit', params: {id: item.id}});
};

const deleteItem = async (item) => {
  const confirmed = window.confirm(`Are you sure you want to delete ${item.id}?`);
  if (confirmed) {
    try {
      await itemService.deleteItem(item.id);
      await fetchItems();
    } catch (err) {
      console.error('Error deleting item:', err);
    }
  }
};

const addItem = () => {
  router.push({name: 'item-create'});
};

const searchItem = async () => {
  if (!searchQuery.value) {
    await fetchItems();
    return;
  }
  loading.value = true;
  try {
    const response = await itemService.searchItemsByDescription(searchQuery.value);
    items.value = response.content;
    totalPages.value = response.totalPages;
  } catch (error) {
    console.error('Error searching items:', error);
  } finally {
    loading.value = false;
  }
};

const actions = [
  {
    label: 'Edit',
    handler: editItem,
    class: 'btn-secondary',
    icon: 'bi bi-pencil'
  },
  {
    label: 'Delete',
    handler: deleteItem,
    class: 'btn-danger',
    icon: 'bi bi-trash'
  }
];

const prevPage = () => {
  if (page.value > 0) {
    page.value -= 1;
    fetchItems();
  }
};

const nextPage = () => {
  if (page.value < totalPages.value - 1) {
    page.value += 1;
    fetchItems();
  }
};

const goToPage = (pageNumber) => {
  if (page.value !== pageNumber) {
    page.value = pageNumber;
    fetchItems();
  }
};

onMounted(() => {
  fetchItems();
});
</script>

<template>
  <div class="container mt-5">
    <div class="d-flex justify-content-between align-items-center mb-3">
      <h2>Items</h2>
      <div class="d-flex">
        <input type="text" class="form-control me-2"
               placeholder="Search by Item description"
               v-model.trim="searchQuery"
               style="width: 200px;">
        <button class="btn btn-dark me-2" @click="searchItem">Search</button>
        <button class="btn btn-dark" @click="addItem">New Item</button>
      </div>
    </div>
    <AppTable
        :columns="[
      { label: 'Description', field: 'description' },
      { label: 'Unit', field: 'unit' },
      { label: 'Rate', field: 'rate' },
    ]"
        :data="items"
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