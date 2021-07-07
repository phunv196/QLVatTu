import api from '@/api/api-service';
import { AxiosResponse } from 'axios';

export default {
  async getSupplies(page = 1, pageSize = 20, suppliesId = ''): Promise<AxiosResponse> {
    const qsParams: Record<string, number | string> = {};
    if (page) {
      qsParams.page = page;
    }
    if (pageSize) {
      qsParams['page-size'] = pageSize;
    }
    if (suppliesId) {
      qsParams.id = suppliesId;
    }

    return api.get('/supplies', { params: qsParams });
  },

  async deleteSupplies(suppliesId: string): Promise<AxiosResponse> {
    debugger;
    return api.delete(`/supplies/${suppliesId}`);
  },

  async addSupplies(suppliesObj: Record<string, string | number>): Promise<AxiosResponse> {
    return api.post('/supplies', suppliesObj);
  },

  async updateSupplies(suppliesObj: Record<string, string | number>): Promise<AxiosResponse> {
    return api.put('/supplies', suppliesObj);
  },

  async getSuppliesById(suppliesId: string): Promise<AxiosResponse> {
    debugger;
    return api.get(`/supplies/${suppliesId}`);
  },

  async getAll(): Promise<AxiosResponse> {
    debugger;
    return api.get(`/supplies/all`);
  },
};
