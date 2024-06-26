<script setup>

import {reactive, defineProps, defineEmits} from 'vue';
import {useRouter} from "vue-router";

const router = useRouter();

const props = defineProps({
  initialData: {
    type: Object,
    default: () => ({
      name: '',
      email: '',
      address: '',
      phone: '',
      clientType: 'INDIVIDUAL'
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
  name: '',
  email: '',
  address: '',
  phone: '',
  clientType: ''
});

const validateEmail = (email) => {
  const re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@(([^<>()\[\]\\.,;:\s@"]+\.)+[^<>()\[\]\\.,;:\s@"]{2,})$/i;
  return re.test(email);
};

const validateForm = () => {
  let valid = true;

  if (!formData.name) {
    errors.name = 'Name is required.';
    valid = false;
  } else {
    errors.name = '';
  }

  if (!formData.email) {
    errors.email = 'Email is required.';
    valid = false;
  } else if (!validateEmail(formData.email)) {
    errors.email = 'Email is invalid.';
    valid = false;
  } else {
    errors.email = '';
  }

  if (!formData.address) {
    errors.address = 'Address is required.';
    valid = false;
  } else {
    errors.address = '';
  }

  if (!formData.phone) {
    errors.phone = 'Phone is required.';
    valid = false;
  } else {
    errors.phone = '';
  }

  if (!formData.clientType) {
    errors.clientType = 'Client type is required.';
    valid = false;
  } else {
    errors.clientType = '';
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
        <label for="name" class="form-label">Name</label>
        <input type="text" class="form-control" :class="{'is-invalid': errors.name}" id="name"
               v-model.trim="formData.name">
        <div v-if="errors.name" class="invalid-feedback">{{ errors.name }}</div>
      </div>
      <div class="mb-3">
        <label for="email" class="form-label">Email</label>
        <input type="email" class="form-control" :class="{'is-invalid': errors.email}" id="email"
               v-model.trim="formData.email">
        <div v-if="errors.email" class="invalid-feedback">{{ errors.email }}</div>
      </div>
      <div class="mb-3">
        <label for="address" class="form-label">Address</label>
        <input type="text" class="form-control" :class="{'is-invalid': errors.address}" id="address"
               v-model.trim="formData.address">
        <div v-if="errors.address" class="invalid-feedback">{{ errors.address }}</div>
      </div>
      <div class="mb-3">
        <label for="phone" class="form-label">Phone</label>
        <input type="text" class="form-control" :class="{'is-invalid': errors.phone}" id="phone"
               v-model.trim="formData.phone">
        <div v-if="errors.phone" class="invalid-feedback">{{ errors.phone }}</div>
      </div>
      <div class="mb-3">
        <label for="clientType" class="form-label">Client Type</label>
        <select class="form-control" :class="{'is-invalid': errors.clientType}" id="clientType"
                v-model.trim="formData.clientType">
          <option value="INDIVIDUAL">INDIVIDUAL</option>
          <option value="BUSINESS">BUSINESS</option>
        </select>
        <div v-if="errors.clientType" class="invalid-feedback">{{ errors.clientType }}</div>
      </div>
      <div class="d-flex justify-content-end gap-2 mt-4 mb-4">
        <button type="submit" class="btn btn-dark">{{ submitButtonText }}</button>
        <router-link :to="{ name: 'clients' }" class="btn btn-light">Cancel</router-link>
      </div>
    </form>
  </div>
</template>

<style scoped>

</style>