import api from '@/api/api-service';
import { AxiosResponse } from 'axios';

export default {
  async getDepartment(page = 1, pageSize = 20, departmentId = '', code='', name='', email='', phone=''): Promise<AxiosResponse> {
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

    }if (departmentId) {
      qsParams.id = departmentId;
    }


    return api.get('/department', { params: qsParams });
  },

  async deleteDepartment(departmentId: string): Promise<AxiosResponse> {
    debugger;
    return api.delete(`/department/${departmentId}`);
  },

  async addDepartment(departmentObj: Record<string, string | number>): Promise<AxiosResponse> {
    return api.post('/department', departmentObj);
  },

  async updateDepartment(departmentObj: Record<string, string | number>): Promise<AxiosResponse> {
    return api.put('/department', departmentObj);
  },

  async getAll(): Promise<AxiosResponse> {
    debugger;
    return api.get(`/department/all`);
  },
  async getDepartmentByCode(departmentObj: Record<string, string | number>): Promise<AxiosResponse> {
    return api.post('/department/byCode', departmentObj);
  },
};
