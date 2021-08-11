import api from '@/api/api-service';
import { AxiosResponse } from 'axios';

export default {
  async getRoles(page = 1, pageSize = 20, roleId = '', code='', name=''): Promise<AxiosResponse> {
    const qsParams: Record<string, number | string> = {};
    if (page) {
      qsParams.page = page;
    }
    if (pageSize) {
      qsParams['page-size'] = pageSize;
    }
    if (roleId) {
      qsParams.id = roleId;
    }
    if (code) {
      qsParams.code = code;
    }
    if (name) {
      qsParams.name = name;
    }

    return api.get('/roles', { params: qsParams });
  },

  async deleteRole(roleId: string): Promise<AxiosResponse> {
    return api.delete(`/roles/${roleId}`);
  },

  async addRole(roleObj: Record<string, string | number>): Promise<AxiosResponse> {
    return api.post('/roles', roleObj);
  },

  async updateRole(roleObj: Record<string, string | number>): Promise<AxiosResponse> {
    return api.put('/roles', roleObj);
  },

  async getAll(): Promise<AxiosResponse> {
    return api.get(`/roles/all`);
  },

  async getRoleByCode(roleObj: Record<string, string | number>): Promise<AxiosResponse> {
    return api.post('/roles/byCode', roleObj);
  },
};
