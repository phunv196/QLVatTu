import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router';
import LoginPage from '@/views/home/LoginPage.vue';
import AppShell from '@/components/AppShell.vue';
import PageContainer from '@/components/PageContainer.vue';
import Users from '@/views/user/Users.vue';
import Employees from '@/views/employee/Employees.vue';
import Dashboard from '@/views/home/Dashboard.vue';
import store from '@/store';
// import Home from '../views/Home.vue';

//material management
import DeliveryBill from '@/views/delivery-bill/DeliveryBill.vue';
import Factory from '@/views/factory/Factory.vue';
import Position from '@/views/position/Position.vue';
import Quality from '@/views/quality/Quality.vue';
import Receipt from '@/views/receipt/Receipt.vue';
import Unit from '@/views/unit/Unit.vue';
import Species from '@/views/species/Species.vue';
import Supplier from '@/views/supplier/Supplier.vue';
import Supplies from '@/views/supplies/Supplies.vue';
import Warehouse from '@/views/warehouse/Warehouse.vue';
import WarehouseCard from '@/views/warehouse-card/WarehouseCard.vue';
import Department from '@/views/department/Department.vue';

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
  {
    path: '/home',
    redirect: '/home/dashboard',
    component: AppShell,
    children: [
      { path: 'dashboard', component: Dashboard },
      { path: 'users', component: Users },
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
