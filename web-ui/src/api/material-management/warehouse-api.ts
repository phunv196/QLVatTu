import api from '@/api/api-service';
import { AxiosResponse } from 'axios';

export default {
  async getWarehouses(page = 1, pageSize = 20, warehouseId = '', code = '', name = '', email = '', phone = ''): Promise<AxiosResponse> {
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

    if (code) {
      qsParams.code = code;
    }

    if (name) {
      qsParams.name = name;
    }
    if (email) {
      qsParams.email = email;
    }
    if (phone) {
      qsParams.phone = phone;
    }
    return api.get('/warehouses', { params: qsParams });
  },

  async export(
    code = '', name = '', email = '', phone = ''): Promise<AxiosResponse> {
    const qsParams: Record<string, number | string> = {};
    if (code) {
      qsParams.code = code;
    }
    if (name) {
      qsParams.name = name;
    }
    if (email) {
      qsParams.email = email;
    }
    if (phone) {
      qsParams.phone = phone;
    }
    return api.post('/warehouses/export', qsParams);
  },

  async deleteWarehouse(warehouseId: string): Promise<AxiosResponse> {
    return api.delete(`/warehouses/${warehouseId}`);
  },

  async addWarehouse(warehouseObj: Record<string, string | number>): Promise<AxiosResponse> {
    return api.post('/warehouses', warehouseObj);
  },

  async updateWarehouse(warehouseObj: Record<string, string | number>): Promise<AxiosResponse> {
    return api.put('/warehouses', warehouseObj);
  },

  async getAll(): Promise<AxiosResponse> {
    return api.get(`/warehouses/all`);
  },

  async getAllBySuppliesId(suppliesId: number): Promise<AxiosResponse> {
    return api.get(`/warehouses/all/${suppliesId}`);
  },

  async getWarehouseByCode(warehouseObj: Record<string, string | number>): Promise<AxiosResponse> {
    return api.post('/warehouses/byCode', warehouseObj);
  },

  async downloadTemplate(): Promise<AxiosResponse> {
    return api.get('/warehouses/downloadTemplate',);
  },

  async uploadFile(fileImport: any): Promise<AxiosResponse> {
    return api.post('/warehouses/uploadFile', fileImport);
  },
};
