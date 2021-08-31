import api from '@/api/api-service';
import { AxiosResponse } from 'axios';

export default {
  async getSupplier(page = 1, pageSize = 20, supplierId = '',
   code = '', name = '', email = '', phone = ''): Promise<AxiosResponse> {
    const qsParams: Record<string, number | string> = {};
    if (page) {
      qsParams.page = page;
    }
    if (pageSize) {
      qsParams['page-size'] = pageSize;
    }
    if (code) {
      qsParams.code = code;
    }

    if (name) {
      qsParams.name = name;
    }
    if (email) {
      qsParams.email = email;
    }
    if (phone) {
      qsParams.phone = phone;

    }
    if (supplierId) {
      qsParams.id = supplierId;
    }


    return api.get('/supplier', { params: qsParams });
  },

  async export(
    searchCode = "",
    searchName = "",
    searchEmail = "",
    searchPhone = "",
  ): Promise<AxiosResponse> {
    const qsParams: Record<string, number | string> = {};
    if (searchCode) {
      qsParams.code = searchCode;
    }
    if (searchName) {
      qsParams.name = searchName;
    }
    if (searchEmail) {
      qsParams.email = searchEmail;
    }
    if (searchPhone) {
      qsParams.phone = searchPhone;
    }
    return api.post('/supplier/export', qsParams);
  },

  async deleteSupplier(supplierId: string): Promise<AxiosResponse> {
    return api.delete(`/supplier/${supplierId}`);
  },

  async addSupplier(supplierObj: Record<string, string | number>): Promise<AxiosResponse> {
    return api.post('/supplier', supplierObj);
  },

  async updateSupplier(supplierObj: Record<string, string | number>): Promise<AxiosResponse> {
    return api.put('/supplier', supplierObj);
  },

  async getAll(): Promise<AxiosResponse> {
    return api.get(`/supplier/all`);
  },

  async getSupplierByCode(supplierObj: Record<string, string | number>): Promise<AxiosResponse> {
    return api.post('/supplier/byCode', supplierObj);
  },

  async downloadTemplate(): Promise<AxiosResponse> {
    return api.get('/supplier/downloadTemplate',);
  },

  async uploadFile(fileImport: any): Promise<AxiosResponse> {
    return api.post('/supplier/uploadFile', fileImport);
  },
};
