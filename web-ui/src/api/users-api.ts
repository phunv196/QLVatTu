import api from '@/api/api-service';
import { commitJwtTokenToStore } from '@/shared/utils';
import { AxiosResponse } from 'axios';

export default {
  async login(username: string, password: string): Promise<AxiosResponse> {
    const resp = await api.post('/authenticate/user', { username, password });
    const respData = resp.data.data;
    if (respData) {
      commitJwtTokenToStore(respData.token, respData.userId, respData.loginName, respData.role, respData.fullName);
      return resp;
    }
    return resp.data;
  },

  async addUsers(userObj: Record<string, string|number>): Promise<AxiosResponse> {
    return api.post('/users', userObj);
  },

  async updateUsers(userObj: Record<string, string|number>): Promise<AxiosResponse> {
    return api.put('/users', userObj);
  },

  async getUsersByLoginName(userObj: Record<string, string | number>): Promise<AxiosResponse> {
    return api.post('/users/byLoginName', userObj);
  },

  async changePassword(userObj: Record<string, string | number>): Promise<AxiosResponse> {
    return api.post('/users/changePassword', userObj);
  },

  async getUsers(page = 1, pageSize = 10, userId = "", searchLoginName = "", searchFullName = "",
    searchEmail = "", searchPhone = "", searchRole = "", searchEmployeeId = ""): Promise<AxiosResponse> {
    const qsParams: Record<string, number | string> = {};
    if (userId) { qsParams.userId = userId; }
    if (page) { qsParams.page = page; }
    if (pageSize) { qsParams['page-size'] = pageSize; }
    if (searchLoginName) { qsParams.searchLoginName = searchLoginName; }
    if (searchFullName) { qsParams.searchFullName = searchFullName; }
    if (searchEmail) { qsParams.searchEmail = searchEmail; }
    if (searchPhone) { qsParams.searchPhone = searchPhone; }
    if (searchRole) { qsParams.searchRole = searchRole; }
    if (searchEmployeeId) { qsParams.searchEmployeeId = searchEmployeeId; }
    return api.get('/users', { params: qsParams });
  },

  async export(loginName = "", fullName = "", email = "", phone = "", role = "", employeeId = ""): Promise<AxiosResponse> {
    const qsParams: Record<string, number | string> = {};
    if (loginName) { qsParams.loginName = loginName; }
    if (fullName) { qsParams.fullName = fullName; }
    if (email) { qsParams.email = email; }
    if (phone) { qsParams.phone = phone; }
    if (role) { qsParams.role = role; }
    if (employeeId) { qsParams.employeeId = employeeId; }
    return api.post('/users/export', qsParams );
  },

  async deleteUser(loginName: string): Promise<AxiosResponse> {
    return api.delete(`/users/${loginName}`);
  },

  async resetPasswordUser(userId:number): Promise<AxiosResponse> {
    return api.get(`/users/resetPasswordUser/${userId}`);
  },
  
  async getById(userId: number): Promise<AxiosResponse> {
    return api.get(`/users/byId/${userId}`);
  },

  async downloadTemplate(): Promise<AxiosResponse> {
    return api.get('/users/downloadTemplate',);
  },

  async uploadFile(fileImport: any): Promise<AxiosResponse> {
    return api.post('/users/uploadFile', fileImport);
  },
};
