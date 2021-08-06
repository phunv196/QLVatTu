import api from '@/api/api-service';
import { AxiosResponse } from 'axios';

export default {
  async getCartItems(loginName: string): Promise<AxiosResponse> {
    const qsParams: Record<string, string> = {
      'login-name': loginName || '',
    };
    return api.get('/cart', { params: qsParams });
  },
};
