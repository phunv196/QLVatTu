import api from '@/api/api-service';
import { AxiosResponse } from 'axios';

export default {
  async getSpecies(page = 1, pageSize = 20, speciesId = '', code = '', name = ''): Promise<AxiosResponse> {
    const qsParams: Record<string, number | string> = {};
    if (page) {
      qsParams.page = page;
    }
    if (pageSize) {
      qsParams['page-size'] = pageSize;
    }
    if (speciesId) {
      qsParams.id = speciesId;
    }

    if (code) {
      qsParams.code = code;
    }
    if (name) {
      qsParams.name = name;
    }

    return api.get('/species', { params: qsParams });
  },

  async deleteSpecies(speciesId: string): Promise<AxiosResponse> {
    return api.delete(`/species/${speciesId}`);
  },

  async addSpecies(speciesObj: Record<string, string | number>): Promise<AxiosResponse> {
    return api.post('/species', speciesObj);
  },

  async updateSpecies(speciesObj: Record<string, string | number>): Promise<AxiosResponse> {
    return api.put('/species', speciesObj);
  },

  async getAll(): Promise<AxiosResponse> {
    return api.get(`/species/all`);
  },

  async getSpeciesByCode(speciesObj: Record<string, string | number>): Promise<AxiosResponse> {
    return api.post('/species/byCode', speciesObj);
  },
};
