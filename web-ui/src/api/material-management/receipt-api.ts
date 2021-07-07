import api from '@/api/api-service';
import { AxiosResponse } from 'axios';

export default {
  async getReceipts(page = 1, pageSize = 20, receiptId = ''): Promise<AxiosResponse> {
    const qsParams: Record<string, number | string> = {};
    if (page) {
      qsParams.page = page;
    }
    if (pageSize) {
      qsParams['page-size'] = pageSize;
    }
    if (receiptId) {
      qsParams.id = receiptId;
    }

    return api.get('/receipts', { params: qsParams });
  },

  async deleteReceipt(receiptId: string): Promise<AxiosResponse> {
    debugger;
    return api.delete(`/receipts/${receiptId}`);
  },

  async addReceipt(receiptObj: Record<string, string | number>): Promise<AxiosResponse> {
    return api.post('/receipts', receiptObj);
  },

  async updateReceipt(receiptObj: Record<string, string | number>): Promise<AxiosResponse> {
    return api.put('/receipts', receiptObj);
  },

  async getAll(): Promise<AxiosResponse> {
    debugger;
    return api.get(`/receipts/all`);
  },

  async getAllBySuppliesId(suppliesId:number): Promise<AxiosResponse> {
    debugger;
    return api.get(`/receipts/all/${suppliesId}`);
  },

  async getSequence(): Promise<AxiosResponse> {
    return api.get(`/receipts/sequence`);
  },

  async checkId(receiptId: string): Promise<AxiosResponse> {
    debugger;
    return api.get(`/receipts/equal/${receiptId}`);
  },

  async deleteByReceiptsId(receiptId: string): Promise<AxiosResponse> {
    debugger;
    return api.delete(`/receipts/delete_by_id/${receiptId}`);
  },
};
