<script setup>

import {reactive, defineProps, defineEmits} from 'vue';
import {useRouter} from "vue-router";

const router = useRouter();

const props = defineProps({
  initialData: {
    type: Object,
    default: () => ({
      description: '',
      unit: '',
      rate: null
    })
  },
  onSubmit: {
    type: Function,
    required: true
  },
  submitButtonText: {
    type: String,
    default: 'Submit'
  }
});

const emit = defineEmits(['formSubmit']);
const formData = reactive({...props.initialData});
const errors = reactive({
  description: '',
  unit: '',
  rate: '',
});


const validateForm = () => {
  let valid = true;

  if (!formData.description) {
    errors.description = 'Description is required.';
    valid = false;
  } else {
    errors.description = '';
  }

  if (!formData.unit) {
    errors.unit = 'Unit is required.';
    valid = false;
  } else {
    errors.unit = '';
  }

  if (formData.rate === null || formData.rate === undefined || isNaN(formData.rate) || formData.rate < 0) {
    errors.rate = 'Rate must be a non-negative number.';
    valid = false;
  } else {
    errors.rate = '';
  }

  return valid;
};

const submitForm = () => {
  if (validateForm()) {
    props.onSubmit({...formData});
    emit('formSubmit');
  }
};

</script>

<template>
  <div class="container mt-5">
    <form @submit.prevent="submitForm" novalidate>
      <div class="mb-3">
        <label for="name" class="form-label">Description</label>
        <input type="text" class="form-control" :class="{'is-invalid': errors.description}" id="name"
               v-model.trim="formData.description">
        <div v-if="errors.description" class="invalid-feedback">{{ errors.description }}</div>
      </div>
      <div class="mb-3">
        <label for="email" class="form-label">Unit</label>
        <input type="email" class="form-control" :class="{'is-invalid': errors.unit}" id="email"
               v-model.trim="formData.unit">
        <div v-if="errors.unit" class="invalid-feedback">{{ errors.unit }}</div>
      </div>
      <div class="mb-3">
        <label for="address" class="form-label">Rate</label>
        <input type="text" class="form-control" :class="{'is-invalid': errors.rate}" id="address"
               v-model.trim="formData.rate">
        <div v-if="errors.rate" class="invalid-feedback">{{ errors.rate }}</div>
      </div>

      <div class="d-flex justify-content-end gap-2 mt-4 mb-4">
        <button type="submit" class="btn btn-dark">{{ submitButtonText }}</button>
        <router-link :to="{ name: 'items' }" class="btn btn-light">Cancel</router-link>
      </div>
    </form>
  </div>
</template>

<style scoped>

</style>