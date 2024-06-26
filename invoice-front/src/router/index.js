import {createRouter, createWebHistory} from 'vue-router'
import HomeView from "@/views/general/HomeView.vue";
import LoginView from "@/views/auth/LoginView.vue";
import RegisterView from "@/views/auth/RegisterView.vue";
import DashboardView from "@/views/general/DashboardView.vue";
import ClientsView from "@/views/client/ClientsView.vue";
import ItemsView from "@/views/item/ItemsView.vue";
import ProfileView from "@/views/general/ProfileView.vue";
import InvoicesView from "@/views/invoice/InvoicesView.vue";
import ClientAddView from "@/views/client/ClientAddView.vue";
import {useAuthStore} from "@/stores/auth.js";
import ClientUpdateView from "@/views/client/ClientUpdateView.vue";
import ItemAddView from "@/views/item/ItemAddView.vue";
import ItemUpdateView from "@/views/item/ItemUpdateView.vue";
import InvoiceAddView from "@/views/invoice/InvoiceAddView.vue";
import InvoiceUpdateView from "@/views/invoice/InvoiceUpdateView.vue";
import InvoiceDetailsView from "@/views/invoice/InvoiceDetailsView.vue";
import ClientDetailsView from "@/views/client/ClientDetailsView.vue";
import BusinessAddView from "@/views/business/BusinessAddView.vue";


const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            name: 'home',
            component: HomeView
        },
        {
            path: '/login',
            name: 'login',
            component: LoginView
        },
        {
            path: '/register',
            name: 'register',
            component: RegisterView
        },
        {
            path: '/dashboard',
            name: 'dashboard',
            component: DashboardView,
            meta: {requiresAuth: true}
        },
        {
            path: '/profile',
            name: 'profile',
            component: ProfileView,
            meta: {requiresAuth: true}
        },
        {
            path: '/invoices',
            name: 'invoices',
            component: InvoicesView,
            meta: {requiresAuth: true}
        },
        {
            path: '/invoices/create',
            name: 'invoice-create',
            component: InvoiceAddView,
            meta: {requiresAuth: true, requiresBusiness: true}
        },
        {
            path: '/invoices/:id/edit',
            name: 'invoice-edit',
            component: InvoiceUpdateView,
            meta: {requiresAuth: true}
        },
        {
            path: '/invoices/:id',
            name: 'invoice-details',
            component: InvoiceDetailsView,
            meta: {requiresAuth: true}
        },
        {
            path: '/clients',
            name: 'clients',
            component: ClientsView,
            meta: {requiresAuth: true}
        },
        {
            path: '/clients/create',
            name: 'client-create',
            component: ClientAddView,
            meta: {requiresAuth: true}
        },
        {
            path: '/clients/:id/edit',
            name: 'client-edit',
            component: ClientUpdateView,
            meta: {requiresAuth: true}
        },
        {
            path: '/clients/:id',
            name: 'client-details',
            component: ClientDetailsView,
            meta: {requiresAuth: true}
        },
        {
            path: '/items',
            name: 'items',
            component: ItemsView,
            meta: {requiresAuth: true}
        },
        {
            path: '/items/create',
            name: 'item-create',
            component: ItemAddView,
            meta: {requiresAuth: true}
        },
        {
            path: '/items/:id/edit',
            name: 'item-edit',
            component: ItemUpdateView,
            meta: {requiresAuth: true}
        },
        {
            path: '/businesses/create',
            name: 'business-create',
            component: BusinessAddView,
            meta: {requiresAuth: true}
        },
        // {
        //   path: '/about',
        //   name: 'about',
        //   // which is lazy-loaded when the route is visited.
        //   component: () => import('../views/AboutView.vue')
        // }
    ]

})

router.beforeEach(async (to, from, next) => {
    const authStore = useAuthStore();

    if (to.matched.some(record => record.meta.requiresAuth)) {
        if (!authStore.isLoggedIn) {
            return next({name: 'login'});
        }
    }

    if (to.matched.some(record => record.meta.requiresBusiness)) {
        const hasBusiness = await authStore.checkUserBusiness();
        if (!hasBusiness) {
            return next({name: 'business-create'});
        }
    }

    next();
});

export default router
