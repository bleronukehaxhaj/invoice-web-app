<script setup>
import {defineProps, defineEmits} from 'vue';

const props = defineProps({
  totalPages: {
    type: Number,
    required: true
  },
  currentPage: {
    type: Number,
    required: true
  }
});

const emit = defineEmits(['prevPage', 'nextPage', 'goToPage']);

const prevPage = () => {
  if (props.currentPage > 0) {
    emit('prevPage');
  }
};

const nextPage = () => {
  if (props.currentPage < props.totalPages - 1) {
    emit('nextPage');
  }
};

const goToPage = (pageNumber) => {
  if (props.currentPage !== pageNumber) {
    emit('goToPage', pageNumber);
  }
};
</script>

<template>
  <nav aria-label="Page navigation example">
    <ul class="pagination justify-content-center">
      <li class="page-item" :class="{ disabled: currentPage === 0 }">
        <a class="page-link" href="#" @click.prevent="prevPage">Previous</a>
      </li>
      <li class="page-item" v-for="pageNumber in totalPages" :key="pageNumber"
          :class="{ active: currentPage === pageNumber - 1 }">
        <a class="page-link" href="#" @click.prevent="goToPage(pageNumber - 1)">{{ pageNumber }}</a>
      </li>
      <li class="page-item" :class="{ disabled: currentPage === totalPages - 1 }">
        <a class="page-link" href="#" @click.prevent="nextPage">Next</a>
      </li>
    </ul>
  </nav>
</template>

<style scoped>

</style>