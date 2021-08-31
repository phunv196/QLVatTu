import api from '@/api/api-service';
import { AxiosResponse } from 'axios';

export default {
  async getSuppliesStats(): Promise<AxiosResponse> {
    return api.get('/stats/supplies-stats');
  },

  async getMonthStats(): Promise<AxiosResponse> {
    return api.get('/stats/month-stats');
  },

  async getStats(groupBy: 'by-receipt' | 'by-delivery-bill'): Promise<AxiosResponse> {
    if (groupBy === 'by-receipt') {
      return api.get('/stats/by-receipt');
    }
    return api.get('/stats/by-delivery-bill');
  },
};
