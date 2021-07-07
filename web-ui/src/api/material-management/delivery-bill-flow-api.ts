import api from '@/api/api-service';
import { AxiosResponse } from 'axios';

export default {
  async getDeliveryBillFlows(page = 1, pageSize = 5, deliveryBillId:number): Promise<AxiosResponse> {
    debugger
    const qsParams: Record<string, number | string> = {};
    if (page) {
      qsParams.page = page;
    }
    if (pageSize) {
      qsParams['page-size'] = pageSize;
    }
    if (deliveryBillId) {
      qsParams.deliveryBillId = deliveryBillId;
    }

    return api.get('/delivery_bill_flows', { params: qsParams });
  },

  async deleteDeliveryBillFlow(deliveryBillFlowId: string): Promise<AxiosResponse> {
    debugger;
    return api.delete(`/delivery_bill_flows/${deliveryBillFlowId}`);
  },

  async addDeliveryBillFlow(deliveryBillFlowObj: Record<string, string | number>): Promise<AxiosResponse> {
    return api.post('/delivery_bill_flows', deliveryBillFlowObj);
  },

  async updateDeliveryBillFlow(deliveryBillFlowObj: Record<string, string | number>): Promise<AxiosResponse> {
    return api.put('/delivery_bill_flows', deliveryBillFlowObj);
  },

  async getDeliveryBillFlow(deliveryBillId:number, suppliesId:number): Promise<AxiosResponse> {
    const qsParams: Record<string, number | string> = {};
    if (deliveryBillId) {
      qsParams.deliveryBillId = deliveryBillId;
    }
    if (suppliesId) {
      qsParams.suppliesId = suppliesId;
    }

    return api.get('/delivery_bill_flows/delivery_bill_flow', { params: qsParams });
  },


};
