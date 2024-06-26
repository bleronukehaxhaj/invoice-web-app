<script setup>
import {defineProps} from 'vue';

const props = defineProps({
  columns: {
    type: Array,
    required: true
  },
  data: {
    type: Array,
    required: true
  },
  actions: {
    type: Array,
    default: () => []
  }
});

const getNestedValue = (obj, path) => {
  if (!path) return undefined;
  return path.split('.').reduce((value, key) => value?.[key], obj);
};

const formatValue = (value, format) => {
  if (!value) return '';
  if (format === 'date') {
    return new Intl.DateTimeFormat('en-US').format(new Date(value));
  }
  if (format === 'currency') {
    return parseFloat(value).toFixed(2);
  }
  return value;
};
</script>

<template>
  <div class="container mt-5">
    <table class="table ">
      <thead>
      <tr>
        <th v-for="(col, index) in columns" :key="index" scope="col">{{ col.label }}</th>
        <th v-if="actions.length > 0" scope="col">Actions</th>
      </tr>
      </thead>
      <tbody class="table-group-divider">
      <tr v-if="data.length === 0">
        <td :colspan="columns.length + (actions.length > 0 ? 1 : 0)" class="text-center">
          No data available
        </td>
      </tr>
      <tr v-else v-for="(item, index) in data" :key="index">
        <td v-for="(col, colIndex) in columns" :key="colIndex">
          {{ formatValue(getNestedValue(item, col.field), col.format) }}
        </td>
        <th v-if="actions.length > 0">
          <button
              v-for="(action, actionIndex) in actions"
              :key="actionIndex"
              @click="() => action.handler(item)"
              class="btn btn-sm me-2"
              :class="action.class"
          >
            <i :class="action.icon"></i>
            {{ typeof action.label === 'function' ? action.label(item) : action.label }}
          </button>
        </th>
      </tr>
      </tbody>
    </table>
  </div>
</template>


<style scoped>

</style>