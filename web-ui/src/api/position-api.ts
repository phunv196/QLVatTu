import api from '@/api/api-service';
import { AxiosResponse } from 'axios';

export default {
  async getPositions(page = 1, pageSize = 20, positionId = '', code = '', name = ''): Promise<AxiosResponse> {
    const qsParams: Record<string, number | string> = {};
    if (page) { qsParams.page = page; }
    if (pageSize) { qsParams['page-size'] = pageSize; }
    if (positionId) { qsParams.id = positionId; }
    if (code) { qsParams.code = code; }
    if (name) { qsParams.name = name; }
    return api.get('/positions', { params: qsParams });
  },

  async deletePosition(positionId: string): Promise<AxiosResponse> {
    return api.delete(`/positions/${positionId}`);
  },

  async addPosition(positionObj: Record<string, string | number>): Promise<AxiosResponse> {
    return api.post('/positions', positionObj);
  },

  async updatePosition(positionObj: Record<string, string | number>): Promise<AxiosResponse> {
    return api.put('/positions', positionObj);
  },

  async getAll(): Promise<AxiosResponse> {
    return api.get(`/positions/all`);
  },

  async getPositionByCode(positionObj: Record<string, string | number>): Promise<AxiosResponse> {
    return api.post('/positions/byCode', positionObj);
  },
};
