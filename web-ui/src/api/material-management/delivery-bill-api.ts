import api from '@/api/api-service';
import { AxiosResponse } from 'axios';

export default {
  async getDeliveryBills(page = 1, pageSize = 20, deliveryBillId = '',
    searchCode = "",
    searchName = "",
    searchEmployee = "",
    searchWarehouse = "",
    searchFormDate = "",
    searchToDate = "",
    searchFactory = ""
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
    if (searchCode) {
      qsParams.searchCode = searchCode;
    }
    if (searchName) {
      qsParams.searchName = searchName;
    }
    if (searchEmployee) {
      qsParams.searchEmployee = searchEmployee;
    }
    if (searchWarehouse) {
      qsParams.searchWarehouse = searchWarehouse;
    }
    if (searchFormDate) {
      qsParams.searchFormDate = searchFormDate;
    }
    if (searchToDate) {
      qsParams.searchToDate = searchToDate;
    }
    if (searchFactory) {
      qsParams.searchFactory = searchFactory;
    }
    return api.get('/delivery_bills', { params: qsParams });
  },

  async deleteDeliveryBill(deliveryBillId: string): Promise<AxiosResponse> {
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
  //   return api.get(`/delivery_bills/all`);
  // },

  async getAllBySuppliesId(suppliesId: number): Promise<AxiosResponse> {
    return api.get(`/delivery_bills/all/${suppliesId}`);
  },

  async checkId(deliveryBillId: string): Promise<AxiosResponse> {
    return api.get(`/delivery_bills/equal/${deliveryBillId}`);
  },

  async getListSuppliersId(suppliersId: number): Promise<AxiosResponse> {
    return api.get(`/delivery_bills/${suppliersId}`);
  },

  async deleteByDeliveryBillId(deliveryBillId: string): Promise<AxiosResponse> {
    return api.delete(`/delivery_bills/delete_by_id/${deliveryBillId}`);
  },
  
  async getDeliveryBillByCode(deliveryBillObj: Record<string, string | number>): Promise<AxiosResponse> {
    return api.post('/delivery_bills/byCode', deliveryBillObj);
  },
};
