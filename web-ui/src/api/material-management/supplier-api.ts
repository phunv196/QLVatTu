import api from '@/api/api-service';
import { AxiosResponse } from 'axios';

export default {
  async getSupplier(page = 1, pageSize = 20, supplierId = '', code = '', name = '', email = '', phone = ''): Promise<AxiosResponse> {
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

  async deleteSupplier(supplierId: string): Promise<AxiosResponse> {
    debugger;
    return api.delete(`/supplier/${supplierId}`);
  },

  async addSupplier(supplierObj: Record<string, string | number>): Promise<AxiosResponse> {
    return api.post('/supplier', supplierObj);
  },

  async updateSupplier(supplierObj: Record<string, string | number>): Promise<AxiosResponse> {
    return api.put('/supplier', supplierObj);
  },

  async getAll(): Promise<AxiosResponse> {
    debugger;
    return api.get(`/supplier/all`);
  },

  async getSupplierByCode(supplierObj: Record<string, string | number>): Promise<AxiosResponse> {
    return api.post('/supplier/byCode', supplierObj);
  },
};
