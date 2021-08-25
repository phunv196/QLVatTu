import api from '@/api/api-service';
import { AxiosResponse } from 'axios';

export default {
  async getFactorys(page = 1, pageSize = 20, factoryId = '', searchCode = "",
    searchName = "",
    searchEmail = "",
    searchEmployee = "",
    searchFormDate = "",
    searchToDate = "",
    searchFormSuccessDate = "",
    searchToSuccessDate = ""): Promise<AxiosResponse> {
    const qsParams: Record<string, number | string> = {};
    if (page) {
      qsParams.page = page;
    }
    if (pageSize) {
      qsParams['page-size'] = pageSize;
    }
    if (factoryId) {
      qsParams.id = factoryId;
    }
    if (searchCode) {
      qsParams.searchCode = searchCode;
    }
    if (searchName) {
      qsParams.searchName = searchName;
    }
    if (searchEmail) {
      qsParams.searchEmail = searchEmail;
    }
    if (searchEmployee) {
      qsParams.searchEmployee = searchEmployee;
    }
    if (searchFormDate) {
      qsParams.searchFormDate = searchFormDate;
    }
    if (searchToDate) {
      qsParams.searchToDate = searchToDate;
    }
    if (searchFormSuccessDate) {
      qsParams.searchFormSuccessDate = searchFormSuccessDate;
    }
    if (searchToSuccessDate) {
      qsParams.searchToSuccessDate = searchToSuccessDate;
    }


    return api.get('/factorys', { params: qsParams });
  },

  async export(
    searchCode = "",
    searchName = "",
    searchEmail = "",
    searchEmployee = "",
    searchFormDate = "",
    searchToDate = "",
    searchFormSuccessDate = "",
    searchToSuccessDate = ""): Promise<AxiosResponse> {
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
    if (searchEmployee) {
      qsParams.employeeId = searchEmployee;
    }
    if (searchFormDate) {
      qsParams.formDate = searchFormDate;
    }
    if (searchToDate) {
      qsParams.toDate = searchToDate;
    }
    if (searchFormSuccessDate) {
      qsParams.formSuccessDate = searchFormSuccessDate;
    }
    if (searchToSuccessDate) {
      qsParams.toSuccessDate = searchToSuccessDate;
    }
    return api.post('/factorys/export', qsParams);
  },

  async deleteFactory(factoryId: string): Promise<AxiosResponse> {
    return api.delete(`/factorys/${factoryId}`);
  },

  async addFactory(factoryObj: Record<string, string | number>): Promise<AxiosResponse> {
    return api.post('/factorys', factoryObj);
  },

  async updateFactory(factoryObj: Record<string, string | number>): Promise<AxiosResponse> {
    return api.put('/factorys', factoryObj);
  },

  async getById(factoryId: string): Promise<AxiosResponse> {
    return api.get(`/factorys/${factoryId}`);
  },

  async getAll(): Promise<AxiosResponse> {
    return api.get(`/factorys/all`);
  },

  async getFactorByCode(factoryObj: Record<string, string | number>): Promise<AxiosResponse> {
    return api.post('/factorys/byCode', factoryObj);
  },

  async downloadTemplate(): Promise<AxiosResponse> {
    return api.get('/factorys/downloadTemplate',);
  },

  async uploadFile(fileImport: any): Promise<AxiosResponse> {
    return api.post('/factorys/uploadFile', fileImport);
  },
};
