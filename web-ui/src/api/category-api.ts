import api from '@/api/api-service';
import { AxiosResponse } from 'axios';

export default {
  async getCategorys(page = 1, pageSize = 20, categoryId = '', code='', name='', parentCode=''): Promise<AxiosResponse> {
    const qsParams: Record<string, number | string> = {};
    if (page) { qsParams.page = page; }
    if (pageSize) { qsParams['page-size'] = pageSize; }
    if (categoryId) { qsParams.id = categoryId; }
    if (code) { qsParams.code = code; }
    if (name) { qsParams.name = name; }
    if (parentCode) { qsParams.parentCode = parentCode; }
    return api.get('/categorys', { params: qsParams });
  },

  async deleteCategory(categoryId: string): Promise<AxiosResponse> {
    return api.delete(`/categorys/${categoryId}`);
  },

  async addCategory(categoryObj: Record<string, string | number>): Promise<AxiosResponse> {
    return api.post('/categorys', categoryObj);
  },

  async updateCategory(categoryObj: Record<string, string | number>): Promise<AxiosResponse> {
    return api.put('/categorys', categoryObj);
  },

  async getAll(): Promise<AxiosResponse> {
    return api.get(`/categorys/all`);
  },

  async getListByParentCode(parentCode: string): Promise<AxiosResponse> {
    return api.get(`/categorys/getListByParentCode/${parentCode}`);
  },

  async getListIsNotParentCode(categoryObj?: Record<string, string | number>): Promise<AxiosResponse> {
    return api.get(`/categorys/getListIsNotParentCode`, {params: categoryObj});
  },

  async getCategoryByCode(categoryObj: Record<string, string | number>): Promise<AxiosResponse> {
    return api.post('/categorys/byCode', categoryObj);
  },
};
