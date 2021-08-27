import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router';
import LoginPage from '@/views/material-management/home/LoginPage.vue';
import AppShell from '@/components/AppShell.vue';
import PageContainer from '@/components/PageContainer.vue';
import Users from '@/views/material-management/user/Users.vue';
import Customers from '@/views/Customers.vue';
import Orders from '@/views/Orders.vue';
import Products from '@/views/Products.vue';
import Employees from '@/views/material-management/employee/Employees.vue';
import Register from '@/views/material-management/home/Register.vue';
import Dashboard from '@/views/material-management/home/Dashboard.vue';
import store from '@/store';
// import Home from '../views/Home.vue';

//material management
import DeliveryBill from '@/views/material-management/delivery-bill/DeliveryBill.vue';
import Factory from '@/views/material-management/factory/Factory.vue';
import Position from '@/views/material-management/position/Position.vue';
import Quality from '@/views/material-management/quality/Quality.vue';
import Receipt from '@/views/material-management/receipt/Receipt.vue';
import Unit from '@/views/material-management/unit/Unit.vue';
import Species from '@/views/material-management/species/Species.vue';
import Supplier from '@/views/material-management/supplier/Supplier.vue';
import Supplies from '@/views/material-management/supplies/Supplies.vue';
import Warehouse from '@/views/material-management/warehouse/Warehouse.vue';
import WarehouseCard from '@/views/material-management/warehouse-card/WarehouseCard.vue';
import Department from '@/views/material-management/department/Department.vue';

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    redirect: () => {
      console.log('check Is Authenticated');
      // if authenticated redirect to appshell else to login
      return '/login';
    },
  },
  { path: '/login', component: LoginPage, meta: { permitAll: true } },
  { path: '/register', component: Register, meta: { permitAll: true } },
  {
    path: '/home',
    redirect: '/home/manage/dashboard',
    component: AppShell,
    children: [
      {
        path: 'manage',
        redirect: '/home/manage/dashboard',
        component: PageContainer,
        children: [
          { path: 'dashboard', component: Dashboard },
          { path: 'users', component: Users },
          { path: 'customers', component: Customers },
          { path: 'orders', component: Orders },
          { path: 'products', component: Products },
          { path: 'employees', component: Employees },
          //material-management
          { path: 'delivery-bill', component: DeliveryBill },
          { path: 'factory', component: Factory },
          { path: 'position', component: Position },
          { path: 'quality', component: Quality },
          { path: 'receipt', component: Receipt },
          { path: 'unit', component: Unit },
          { path: 'species', component: Species },
          { path: 'supplier', component: Supplier },
          { path: 'supplies', component: Supplies },
          { path: 'warehouse', component: Warehouse },
          { path: 'Warehouse-card', component: WarehouseCard },
          { path: 'department', component: Department },
        ],
      },
    ],
  },
  // the default route, when none of the above matches:
  {
    path: '/:pathMatch(.*)*',
    redirect: () => {
      if (store.getters.jwt) { // check if authenticated
        return '/home';
      }
      return '/login';
    },
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});
export default router;
