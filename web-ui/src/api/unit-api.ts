import api from '@/api/api-service';
import { AxiosResponse } from 'axios';

export default {
  async getUnits(page = 1, pageSize = 20, unitId = '', code='', name=''): Promise<AxiosResponse> {
    const qsParams: Record<string, number | string> = {};
    if (page) {
      qsParams.page = page;
    }
    if (pageSize) {
      qsParams['page-size'] = pageSize;
    }
    if (unitId) {
      qsParams.id = unitId;
    }
    if (code) {
      qsParams.code = code;
    }
    if (name) {
      qsParams.name = name;
    }

    return api.get('/units', { params: qsParams });
  },

  async deleteUnit(unitId: string): Promise<AxiosResponse> {
    return api.delete(`/units/${unitId}`);
  },

  async addUnit(unitObj: Record<string, string | number>): Promise<AxiosResponse> {
    return api.post('/units', unitObj);
  },

  async updateUnit(unitObj: Record<string, string | number>): Promise<AxiosResponse> {
    return api.put('/units', unitObj);
  },

  async getAll(): Promise<AxiosResponse> {
    return api.get(`/units/all`);
  },

  async getUnitByCode(unitObj: Record<string, string | number>): Promise<AxiosResponse> {
    return api.post('/units/byCode', unitObj);
  },
};
