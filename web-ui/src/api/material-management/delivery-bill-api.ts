import api from '@/api/api-service';
import { AxiosResponse } from 'axios';

export default {
  async getDeliveryBills(page = 1, pageSize = 20, deliveryBillId = '',
    name:string ,code:string, formDate:string, toDate:string,
    ): Promise<AxiosResponse> {
    const qsParams: Record<string, number | string | Date> = {};
    if (page) {
      qsParams.page = page;
    }
    if (pageSize) {
      qsParams['page-size'] = pageSize;
    }
    if (deliveryBillId) {
      qsParams.id = deliveryBillId;
    }
    if(name) {
      qsParams.name = name;
    }
    if(code) {
      qsParams.code = code;
    }
    if(formDate) {
      qsParams.formDate = formDate;
    }
    if(toDate) {
      qsParams.toDate = toDate;
    }
debugger
    return api.get('/delivery_bills', { params: qsParams });
  },

  async deleteDeliveryBill(deliveryBillId: string): Promise<AxiosResponse> {
    debugger;
    return api.delete(`/delivery_bills/${deliveryBillId}`);
  },

  async addDeliveryBill(deliveryBillObj: Record<string, string | number>): Promise<AxiosResponse> {
    return api.post('/delivery_bills', deliveryBillObj);
  },

  async updateDeliveryBill(deliveryBillObj: Record<string, string | number>): Promise<AxiosResponse> {
    return api.put('/delivery_bills', deliveryBillObj);
  },

  async getSequence(): Promise<AxiosResponse> {
    return api.get(`/delivery_bills/sequence`);
  },

  // async getAll(): Promise<AxiosResponse> {
  //   debugger;
  //   return api.get(`/delivery_bills/all`);
  // },

  async getAllBySuppliesId(suppliesId:number): Promise<AxiosResponse> {
    debugger;
    return api.get(`/delivery_bills/all/${suppliesId}`);
  },

  async checkId(deliveryBillId: string): Promise<AxiosResponse> {
    debugger;
    return api.get(`/delivery_bills/equal/${deliveryBillId}`);
  },

  async getListSuppliersId(suppliersId: number): Promise<AxiosResponse> {
    debugger;
    return api.get(`/delivery_bills/${suppliersId}`);
  },

  async deleteByDeliveryBillId(deliveryBillId: string): Promise<AxiosResponse> {
    debugger;
    return api.delete(`/delivery_bills/delete_by_id/${deliveryBillId}`);
  },
};
