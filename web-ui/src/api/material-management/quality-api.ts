import api from '@/api/api-service';
import { AxiosResponse } from 'axios';

export default {
  async getQualitys(page = 1, pageSize = 20, qualityId = '', code='', name=''): Promise<AxiosResponse> {
    const qsParams: Record<string, number | string> = {};
    if (page) {
      qsParams.page = page;
    }
    if (pageSize) {
      qsParams['page-size'] = pageSize;
    }
    if (qualityId) {
      qsParams.id = qualityId;
    }
    if (code) {
      qsParams.code = code;
    }
    if (name) {
      qsParams.name = name;
    }

    return api.get('/qualitys', { params: qsParams });
  },

  async deleteQuality(qualityId: string): Promise<AxiosResponse> {
    return api.delete(`/qualitys/${qualityId}`);
  },

  async addQuality(qualityObj: Record<string, string | number>): Promise<AxiosResponse> {
    return api.post('/qualitys', qualityObj);
  },

  async updateQuality(qualityObj: Record<string, string | number>): Promise<AxiosResponse> {
    return api.put('/qualitys', qualityObj);
  },

  async getAll(): Promise<AxiosResponse> {
    return api.get(`/qualitys/all`);
  },

  async getQualityByCode(qualityObj: Record<string, string | number>): Promise<AxiosResponse> {
    return api.post('/qualitys/byCode', qualityObj);
  },
};
