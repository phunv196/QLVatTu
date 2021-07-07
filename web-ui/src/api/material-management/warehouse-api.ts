import api from '@/api/api-service';
import { AxiosResponse } from 'axios';

export default {
  async getWarehouses(page = 1, pageSize = 20, warehouseId = ''): Promise<AxiosResponse> {
    const qsParams: Record<string, number | string> = {};
    if (page) {
      qsParams.page = page;
    }
    if (pageSize) {
      qsParams['page-size'] = pageSize;
    }
    if (warehouseId) {
      qsParams.id = warehouseId;
    }

    return api.get('/warehouses', { params: qsParams });
  },

  async deleteWarehouse(warehouseId: string): Promise<AxiosResponse> {
    debugger;
    return api.delete(`/warehouses/${warehouseId}`);
  },

  async addWarehouse(warehouseObj: Record<string, string | number>): Promise<AxiosResponse> {
    return api.post('/warehouses', warehouseObj);
  },

  async updateWarehouse(warehouseObj: Record<string, string | number>): Promise<AxiosResponse> {
    return api.put('/warehouses', warehouseObj);
  },

  async getAll(): Promise<AxiosResponse> {
    debugger;
    return api.get(`/warehouses/all`);
  },

  async getAllBySuppliesId(suppliesId:number): Promise<AxiosResponse> {
    debugger;
    return api.get(`/warehouses/all/${suppliesId}`);
  },
};
