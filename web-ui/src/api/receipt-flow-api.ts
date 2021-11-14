import api from '@/api/api-service';
import { AxiosResponse } from 'axios';

export default {
  async getReceiptFlows(page = 1, pageSize = 5, receiptId: number): Promise<AxiosResponse> {
    const qsParams: Record<string, number | string> = {};
    if (page) { qsParams.page = page; }
    if (pageSize) { qsParams['page-size'] = pageSize; }
    if (receiptId) { qsParams.receiptId = receiptId; }
    return api.get('/receipt_flows', { params: qsParams });
  },

  async deleteReceiptFlow(receiptFlowId: string): Promise<AxiosResponse> {
    return api.delete(`/receipt_flows/${receiptFlowId}`);
  },

  async addReceiptFlow(receiptFlowObj: Record<string, string | number>): Promise<AxiosResponse> {
    return api.post('/receipt_flows', receiptFlowObj);
  },

  async updateReceiptFlow(receiptFlowObj: Record<string, string | number>): Promise<AxiosResponse> {
    return api.put('/receipt_flows', receiptFlowObj);
  },

  async getReceiptFlow(receiptId:number, suppliesId:number): Promise<AxiosResponse> {
    const qsParams: Record<string, number | string> = {};
    if (receiptId) {
      qsParams.receiptId = receiptId;
    }
    if (suppliesId) {
      qsParams.suppliesId = suppliesId;
    }
    return api.get('/receipt_flows/receipt_flow', { params: qsParams });
  },

  async checkReceiptFlow(receiptId:number, suppliesId:number): Promise<AxiosResponse> {
    const qsParams: Record<string, number | string> = {};
    if (receiptId) {
      qsParams.receiptId = receiptId;
    }
    if (suppliesId) {
      qsParams.suppliesId = suppliesId;
    }
    return api.get('/receipt_flows/check_receipt_flow', { params: qsParams });
  },
};
