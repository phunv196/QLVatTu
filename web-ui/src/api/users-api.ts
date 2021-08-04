import api from '@/api/api-service';
import { commitJwtTokenToStore } from '@/shared/utils';
import { AxiosResponse } from 'axios';

export default {
  async login(username: string, password: string): Promise<AxiosResponse> {
    const resp = await api.post('/authenticate/user', { username, password });
    const respData = resp.data.data;
    if (respData) {
      commitJwtTokenToStore(respData.token, respData.loginName, respData.role, respData.fullName);
      return resp;
    }
    return Promise.reject(resp.data ? resp.data : resp);
  },

  async registerUser(userObj: Record<string, string|number>): Promise<AxiosResponse> {
    return api.post('/users', userObj);
  },

  async getUsers(page = 1, pageSize = 20, loginName?: string, role?: string): Promise<AxiosResponse> {
    const qsParams: Record<string, number|string> = { };
    if (page) { qsParams.page = page; }
    if (pageSize) { qsParams['page-size'] = pageSize; }
    if (loginName) { qsParams['user-id'] = loginName; }
    if (role) { qsParams.role = role; }
    return api.get('/users', { params: qsParams });
  },

  async deleteUser(loginName: string): Promise<AxiosResponse> {
    return api.delete(`/users/${loginName}`);
  },

  async resetPasswordUser(loginName: string): Promise<AxiosResponse> {
    return api.get(`/users/${loginName}`);
  },
};
