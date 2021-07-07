import api from '@/api/api-service';
import { AxiosResponse } from 'axios';

export default {
  async getFactorys(page = 1, pageSize = 20, factoryId = ''): Promise<AxiosResponse> {
    const qsParams: Record<string, number | string> = {};
    if (page) {
      qsParams.page = page;
    }
    if (pageSize) {
      qsParams['page-size'] = pageSize;
    }
    if (factoryId) {
      qsParams.id = factoryId;
    }

    return api.get('/factorys', { params: qsParams });
  },

  async deleteFactory(factoryId: string): Promise<AxiosResponse> {
    debugger;
    return api.delete(`/factorys/${factoryId}`);
  },

  async addFactory(factoryObj: Record<string, string | number>): Promise<AxiosResponse> {
    debugger
    return api.post('/factorys', factoryObj);
  },

  async updateFactory(factoryObj: Record<string, string | number>): Promise<AxiosResponse> {
    return api.put('/factorys', factoryObj);
  },

  async getById(factoryId: string): Promise<AxiosResponse> {
    debugger;
    return api.get(`/factorys/${factoryId}`);
  },

  async getAll(): Promise<AxiosResponse> {
    debugger;
    return api.get(`/factorys/all`);
  },
};
