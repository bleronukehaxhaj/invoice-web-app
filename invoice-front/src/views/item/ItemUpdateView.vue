<script setup>
import {useRoute, useRouter} from "vue-router";
import {onMounted, ref} from "vue";
import itemService from "@/services/itemService.js";
import ItemForm from "@/components/ItemForm.vue";

const router = useRouter();
const route = useRoute();
const itemData = ref(null);
const loading = ref(true);


onMounted(async () => {
  const itemId = route.params.id;

  try {
    itemData.value = await itemService.getItemById(itemId);
  } catch (error) {
    console.error('Error fetching item data:', error);
  } finally {
    loading.value = false;
  }
});

const handleFormSubmit = async (formData) => {
  const itemId = route.params.id;
  loading.value = true;

  try {
    await itemService.updateItem(itemId, formData);
    console.log('Item updated successfully');
    await router.push({name: 'items'});
  } catch (error) {
    console.error('Error updating item:', error);
  } finally {
    loading.value = false;
  }
};
</script>

<template>
  <div class="container mt-5">
    <h2>Edit Item</h2>
    <div v-if="loading" class="text-center mt-3">
      <span>Loading...</span>
    </div>
    <div v-else>
      <div class="card mb-5 shadow-sm">
        <ItemForm
            v-if="itemData"
            :initialData="itemData"
            :onSubmit="handleFormSubmit"
            submitButtonText="Update Item"
        />
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>