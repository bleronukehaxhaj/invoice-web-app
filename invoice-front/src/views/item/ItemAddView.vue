<script setup>
import ItemForm from "@/components/ItemForm.vue";
import {useRouter} from "vue-router";
import itemService from "@/services/itemService.js";

const router = useRouter();

const handleFormSubmit = async (formData) => {
  try {
    const response = await itemService.createItem(formData);
    await router.push({name: 'items'});
  } catch (error) {
    console.error('Error creating client:', error);
  }
};
</script>

<template>
  <div class="container mt-5">
    <h2>Add New Item</h2>
    <div class="card mb-5 shadow-sm">
      <ItemForm :onSubmit="handleFormSubmit" submitButtonText="Add Item"/>
    </div>
  </div>
</template>

<style scoped>

</style>