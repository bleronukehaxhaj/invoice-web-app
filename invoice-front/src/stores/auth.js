import {defineStore} from "pinia";
import {computed, ref} from "vue";
import * as authService from "@/services/authService.js";
import businessService from "@/services/businessService.js";

export const useAuthStore = defineStore('auth', () => {

    const token = ref(localStorage.getItem('token') || null)
    const business = ref(null);

    async function logIn(user) {
        const response = await authService.login(user)
        console.log(response)
        if (response) {
            token.value = response.token;
            await checkUserBusiness();
        }
    }

    async function register(user) {
        const response = await authService.register(user);
        console.log(response)
        if (response) {
            token.value = response.token;
        }
    }

    function logOut() {
        if (isLoggedIn.value) {
            localStorage.removeItem('token');
            token.value = null;
            business.value = null;
        }
    }


    const isLoggedIn = computed(() => {
        return !!token.value;
    });

    async function checkUserBusiness() {
        try {
            business.value = await businessService.getMyBusiness();
            return !!business.value;
        } catch (error) {
            business.value = null;
            return false;
        }
    }

    return {logIn, register, logOut, isLoggedIn, checkUserBusiness, business};
})