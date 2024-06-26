<script setup>
import {reactive, computed, onMounted} from 'vue';
import businessService from '@/services/businessService.js';
import clientService from '@/services/clientService.js';
import itemService from '@/services/itemService.js';

const props = defineProps({
  initialData: {
    type: Object,
    default: () => ({
      id: null,
      invoiceNumber: '',
      issueDate: '',
      dueDate: '',
      invoiceStatus: 'PENDING',
      discount: 0,
      tax: 0,
      business: {
        name: '',
        email: '',
        address: '',
        phone: ''
      },
      client: {
        name: '',
        email: '',
        address: '',
        phone: ''
      },
      invoiceItems: [
        {description: '', rate: 0.00, quantity: 1, itemId: null}
      ]
    })
  },
  onSubmit: {
    type: Function,
    required: true
  },
  isUpdate: {
    type: Boolean,
    default: false
  }
});

const formData = reactive({...props.initialData});
const clients = reactive([]);
const items = reactive([]);
const errors = reactive({invoiceItems: []});

onMounted(async () => {
  try {
    const businessResponse = await businessService.getMyBusiness();
    formData.business = {...formData.business, ...businessResponse};

    if (!props.isUpdate) {
      const currentDate = new Date();
      formData.issueDate = currentDate.toISOString().substring(0, 10);
      formData.dueDate = currentDate.toISOString().substring(0, 10);
    }

    const clientsResponse = await clientService.getAllListClients();
    clients.push(...clientsResponse);

    const itemsResponse = await itemService.getAllListItems();
    items.push(...itemsResponse);

    if (props.isUpdate && formData.client.id) {
      const client = await clientService.getClientById(formData.client.id);
      formData.client = {...formData.client, ...client};
    }
  } catch (error) {
    console.error('Error fetching initial data:', error);
  }
});

const fetchClientInfo = async () => {
  const selectedClient = clients.find(client => client.name === formData.client.name);
  if (selectedClient) {
    formData.client = {...formData.client, ...selectedClient};
    errors.name = ''
  }
};

const fetchItemInfo = async (index) => {
  const selectedItem = items.find(item => item.description === formData.invoiceItems[index].description);
  if (selectedItem) {
    formData.invoiceItems[index].itemId = selectedItem.id;
    formData.invoiceItems[index].rate = selectedItem.rate;
  }
};

const addItem = () => {
  formData.invoiceItems.push({description: '', rate: 0.00, quantity: 1, itemId: null});
  errors.invoiceItems.push({});
};

const removeItem = (index) => {
  formData.invoiceItems.splice(index, 1);
};

const calculateAmount = (rate, quantity) => {
  return (rate * quantity);
};

const subtotal = computed(() => {
  return parseFloat(
      formData.invoiceItems.reduce((total, item) => {
        return total + item.rate * item.quantity;
      }, 0).toFixed(2)
  );
});

const discountAmount = computed(() => {
  return parseFloat(((subtotal.value * formData.discount) / 100).toFixed(2));
});

const taxAmount = computed(() => {
  return parseFloat((((subtotal.value - discountAmount.value) * formData.tax) / 100).toFixed(2));
});

const total = computed(() => {
  return (subtotal.value - discountAmount.value + taxAmount.value).toFixed(2);
});

const validateForm = () => {
  let valid = true;

  errors.invoiceNumber = !formData.invoiceNumber ? 'Invoice number is required.' : '';
  if (errors.invoiceNumber) valid = false;

  errors.issueDate = !formData.issueDate ? 'Issue date is required.' : '';
  if (errors.issueDate) valid = false;

  errors.dueDate = !formData.dueDate ? 'Due date is required.' : '';
  if (errors.dueDate) valid = false;

  errors.invoiceStatus = !formData.invoiceStatus ? 'Invoice status is required.' : '';
  if (errors.invoiceStatus) valid = false;

  errors.discount = formData.discount < 0 ? 'Discount cannot be negative.' : '';
  if (errors.discount) valid = false;

  errors.tax = formData.tax < 0 ? 'Tax cannot be negative.' : '';
  if (errors.tax) valid = false;

  errors.clientName = !formData.client.name || !clients.some(client => client.name === formData.client.name) ? 'Client must be selected.' : '';
  if (errors.clientName) valid = false;

  formData.invoiceItems.forEach((item, index) => {
    errors.invoiceItems = errors.invoiceItems || [];
    errors.invoiceItems[index] = {};
    errors.invoiceItems[index].description = !item.description || !items.some(i => i.description === item.description) ? 'Description is required.' : '';
    errors.invoiceItems[index].quantity = item.quantity <= 0 ? 'Quantity must be greater than 0.' : '';

    if (errors.invoiceItems[index].description || errors.invoiceItems[index].quantity) {
      valid = false;
    }
  });

  return valid;
};

const handleSubmit = () => {
  if (!validateForm()) {
    return;
  }
  const payload = {
    id: formData.id,
    invoiceNumber: formData.invoiceNumber,
    issueDate: new Date(formData.issueDate).toISOString(),
    dueDate: new Date(formData.dueDate).toISOString(),
    invoiceStatus: formData.invoiceStatus,
    discount: parseFloat(formData.discount),
    tax: parseFloat(formData.tax),
    clientId: formData.client.id,
    invoiceItems: formData.invoiceItems.map(item => ({
      quantity: parseFloat(item.quantity),
      itemId: item.itemId
    }))
  };
  props.onSubmit(payload);
};
</script>

<template>
  <div class="container mt-5">
    <h2>Invoice</h2>
    <form @submit.prevent="handleSubmit" novalidate>
      <div class="row">
        <div class="col-md-6">
          <h5>From</h5>
          <div class="mb-3">
            <label for="fromName" class="form-label">Name</label>
            <input type="text" class="form-control" id="fromName" v-model="formData.business.name" disabled/>
          </div>
          <div class="mb-3">
            <label for="fromEmail" class="form-label">Email</label>
            <input type="email" class="form-control" id="fromEmail" v-model="formData.business.email" disabled/>
          </div>
          <div class="mb-3">
            <label for="fromAddress" class="form-label">Address</label>
            <input type="text" class="form-control" id="fromAddress" v-model="formData.business.address" disabled/>
          </div>
          <div class="mb-3">
            <label for="fromPhone" class="form-label">Phone</label>
            <input type="text" class="form-control" id="fromPhone" v-model="formData.business.phone" disabled/>
          </div>
        </div>
        <div class="col-md-6">
          <h5>Bill To</h5>
          <div class="mb-3">
            <label for="toName" class="form-label">Name</label>
            <input type="text"
                   class="form-control"
                   id="toName"
                   v-model="formData.client.name"
                   @input="fetchClientInfo"
                   list="clientList"
                   placeholder="Client Name"
                   autocomplete="off"
                   :class="{ 'is-invalid': errors.clientName }"/>

            <datalist id="clientList">
              <option v-for="client in clients" :key="client.id" :value="client.name"></option>
            </datalist>
            <div v-if="errors.clientName" class="invalid-feedback">{{ errors.clientName }}</div>
          </div>
          <div class="mb-3">
            <label for="toEmail" class="form-label">Email</label>
            <input type="email" class="form-control" id="toEmail" v-model="formData.client.email" disabled/>
          </div>
          <div class="mb-3">
            <label for="toAddress" class="form-label">Address</label>
            <input type="text" class="form-control" id="toAddress" v-model="formData.client.address" disabled/>
          </div>
          <div class="mb-3">
            <label for="toPhone" class="form-label">Phone</label>
            <input type="text" class="form-control" id="toPhone" v-model="formData.client.phone" disabled/>
          </div>
        </div>
      </div>
      <div class="row">
        <div class="col-md-4">
          <div class="mb-3">
            <label for="invoiceNumber" class="form-label">Number</label>
            <input type="text" class="form-control" id="invoiceNumber" v-model="formData.invoiceNumber"
                   :class="{ 'is-invalid': errors.invoiceNumber }"/>
            <div v-if="errors.invoiceNumber" class="invalid-feedback">{{ errors.invoiceNumber }}</div>
          </div>
        </div>
        <div class="col-md-4">
          <div class="mb-3">
            <label for="issueDate" class="form-label">Date</label>
            <input type="date" class="form-control" id="issueDate" v-model="formData.issueDate"
                   :class="{ 'is-invalid': errors.issueDate }"/>
            <div v-if="errors.issueDate" class="invalid-feedback">{{ errors.issueDate }}</div>
          </div>
        </div>
        <div class="col-md-4">
          <div class="mb-3">
            <label for="dueDate" class="form-label">Due Date</label>
            <input type="date" class="form-control" id="dueDate" v-model="formData.dueDate"
                   :class="{ 'is-invalid': errors.dueDate }"/>
            <div v-if="errors.dueDate" class="invalid-feedback">{{ errors.dueDate }}</div>
          </div>
        </div>
        <div class="col-md-4">
          <div class="mb-3">
            <label for="invoiceStatus" class="form-label">Status</label>
            <select class="form-select" id="invoiceStatus" v-model="formData.invoiceStatus"
                    :class="{ 'is-invalid': errors.invoiceStatus }">
              <option value="PENDING">PENDING</option>
              <option value="PAID">PAID</option>
              <option value="OVERDUE">OVERDUE</option>
              <option value="CANCELED">CANCELED</option>
            </select>
            <div v-if="errors.invoiceStatus" class="invalid-feedback">{{ errors.invoiceStatus }}</div>
          </div>
        </div>
      </div>
      <div class="table-responsive">
        <table class="table table-bordered">
          <thead>
          <tr>
            <th scope="col">Description</th>
            <th scope="col">Rate</th>
            <th scope="col">Qty</th>
            <th scope="col">Amount</th>
            <th scope="col">Action</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="(item, index) in formData.invoiceItems" :key="index">
            <td>
              <input type="text" class="form-control" v-model="item.description" @input="fetchItemInfo(index)"
                     list="itemList" placeholder="Item Description" autocomplete="off"
                     :class="{ 'is-invalid': errors.invoiceItems[index]?.description }"/>
              <datalist id="itemList">
                <option v-for="item in items" :key="item.id" :value="item.description"></option>
              </datalist>
              <div v-if="errors.invoiceItems[index]?.description" class="invalid-feedback">
                {{ errors.invoiceItems[index].description }}
              </div>
            </td>
            <td><input type="number" class="form-control" v-model="item.rate" disabled/></td>
            <td><input type="number" class="form-control" v-model="item.quantity"
                       :class="{ 'is-invalid': errors.invoiceItems[index]?.quantity }"/>
              <div v-if="errors.invoiceItems[index]?.quantity" class="invalid-feedback">
                {{ errors.invoiceItems[index].quantity }}
              </div>
            </td>
            <td>{{ calculateAmount(item.rate, item.quantity) }}</td>
            <td>
              <button type="button" class="btn btn-danger" @click="removeItem(index)"><i class="bi bi-x"></i></button>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
      <button type="button" class="btn btn-primary mb-3" @click="addItem">Add Item</button>
      <div class="row">
        <div class="col-md-6 offset-md-6">
          <div class="d-flex justify-content-between mb-2">
            <span>Subtotal</span>
            <span>€{{ subtotal }}</span>
          </div>
          <div class="d-flex justify-content-between mb-2 align-items-center">
            <span>Discount (%)</span>
            <input type="number" class="form-control form-control-sm" style="width: 60px;" v-model="formData.discount"
                   :class="{ 'is-invalid': errors.discount }"/>
            <div v-if="errors.discount" class="invalid-feedback">{{ errors.discount }}</div>
            <span>€{{ discountAmount }}</span>
          </div>
          <div class="d-flex justify-content-between mb-2 align-items-center">
            <span>Tax (%)</span>
            <input type="number" class="form-control form-control-sm" style="width: 60px;" v-model="formData.tax"
                   :class="{ 'is-invalid': errors.tax }"/>
            <div v-if="errors.tax" class="invalid-feedback">{{ errors.tax }}</div>
            <span>€{{ taxAmount }}</span>
          </div>
          <h5 class="d-flex justify-content-between">
            <span>Total</span>
            <span>€{{ total }}</span>
          </h5>
        </div>
      </div>

      <div class="d-flex justify-content-end gap-2 mt-5 mb-4">
        <button type="submit" class="btn btn-dark">{{ isUpdate ? 'Update' : 'Save' }}</button>
        <router-link :to="{ name: 'invoices' }" class="btn btn-light">Cancel</router-link>
      </div>
    </form>
  </div>
</template>


<style scoped>
.container {
  max-width: 900px;
}

.table-responsive {
  margin-top: 20px;
}

.table th, .table td {
  vertical-align: middle;
}

.text-end {
  text-align: right;
}
</style>
