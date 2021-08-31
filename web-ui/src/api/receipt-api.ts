import api from '@/api/api-service';
import { AxiosResponse } from 'axios';

export default {
  async getReceipts(page = 1, pageSize = 20, receiptId = '', searchCode = "",
  searchName = "",
  searchEmployee = "",
  searchWarehouse = "",
  searchFormDate = "",
  searchToDate = ""): Promise<AxiosResponse> {
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

    return api.get('/receipts', { params: qsParams });
  },

  async export(
  searchCode = "",
  searchName = "",
  searchEmployee = "",
  searchWarehouse = "",
  searchFormDate = "",
  searchToDate = ""): Promise<AxiosResponse> {
    const qsParams: Record<string, number | string> = {};
    if (searchCode) {
      qsParams.code = searchCode;
    }
    if (searchName) {
      qsParams.name = searchName;
    }
    if (searchEmployee) {
      qsParams.employeeId = searchEmployee;
    }
    if (searchWarehouse) {
      qsParams.warehouseId = searchWarehouse;
    }
    if (searchFormDate) {
      qsParams.formDate = searchFormDate;
    }
    if (searchToDate) {
      qsParams.toDate = searchToDate;
    }
    return api.post('/receipts/export', qsParams);
  },

  async deleteReceipt(receiptId: string): Promise<AxiosResponse> {
    return api.delete(`/receipts/${receiptId}`);
  },

  async addReceipt(receiptObj: Record<string, string | number>): Promise<AxiosResponse> {
    return api.post('/receipts', receiptObj);
  },

  async updateReceipt(receiptObj: Record<string, string | number>): Promise<AxiosResponse> {
    return api.put('/receipts', receiptObj);
  },

  async getAll(): Promise<AxiosResponse> {
    return api.get(`/receipts/all`);
  },

  async getAllBySuppliesId(suppliesId:number): Promise<AxiosResponse> {
    return api.get(`/receipts/all/${suppliesId}`);
  },

  async getSequence(): Promise<AxiosResponse> {
    return api.get(`/receipts/sequence`);
  },

  async checkId(receiptId: string): Promise<AxiosResponse> {
    return api.get(`/receipts/equal/${receiptId}`);
  },

  async deleteByReceiptsId(receiptId: string): Promise<AxiosResponse> {
    return api.delete(`/receipts/delete_by_id/${receiptId}`);
  },

  async getReceiptByCode(receiptObj: Record<string, string | number>): Promise<AxiosResponse> {
    return api.post('/receipts/byCode', receiptObj);
  },

  async downloadFileDocx(deliveryBillId: any): Promise<AxiosResponse> {
    return api.get(`/receipts/downloadFileDocx/${deliveryBillId}`);
  },
};
