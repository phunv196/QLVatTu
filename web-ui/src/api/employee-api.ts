import api from '@/api/api-service';
import { AxiosResponse } from 'axios';

export default {
  async getEmployees(page = 1, pageSize = 20, employeeId = '',
    searchCode = "",
    searchName = "",
    searchEmail = "",
    searchPhone = "",
    searchDepartment = "",
    searchPosition = ""): Promise<AxiosResponse> {
    const qsParams: Record<string, number | string> = {};
    if (employeeId) { qsParams.id = employeeId; }
    if (page) { qsParams.page = page; }
    if (pageSize) { qsParams['page-size'] = pageSize; }
    if (searchCode) {
      qsParams.searchCode = searchCode;
    }

    if (searchName) {
      qsParams.searchName = searchName;
    }
    if (searchEmail) {
      qsParams.searchEmail = searchEmail;
    }
    if (searchPhone) {
      qsParams.searchPhone = searchPhone;

    }
    if (searchDepartment) {
      qsParams.searchDepartment = searchDepartment;

    }
    if (searchPosition) {
      qsParams.searchPosition = searchPosition;

    }

    return api.get('/employees', { params: qsParams });
  },

  async export(
    code = "",
    fullName = "",
    email = "",
    phone = "",
    departmentId = "",
    positionId = ""): Promise<AxiosResponse> {
    const qsParams: Record<string, number | string> = {};
    if (code) {
      qsParams.code = code;
    }

    if (fullName) {
      qsParams.fullName = fullName;
    }
    if (email) {
      qsParams.email = email;
    }
    if (phone) {
      qsParams.phone = phone;

    }
    if (departmentId) {
      qsParams.departmentId = departmentId;

    }
    if (positionId) {
      qsParams.positionId = positionId;

    }

    return api.post('/employees/export', qsParams);
  },

  async deleteEmployee(employeeId: string): Promise<AxiosResponse> {
    return api.delete(`/employees/${employeeId}`);
  },

  async getEmployeeById(employeeId: string): Promise<AxiosResponse> {
    return api.get(`/employees/getEmployeeById/${employeeId}`);
  },

  async addEmployee(employeeObj: Record<string, string | number>): Promise<AxiosResponse> {
    return api.post('/employees', employeeObj);
  },

  async updateEmployee(employeeObj: Record<string, string | number>): Promise<AxiosResponse> {
    return api.put('/employees', employeeObj);
  },

  async getAll(): Promise<AxiosResponse> {
    return api.get('/employees/all',);
  },

  async getEmployeeByCode(employeeObj: Record<string, string | number>): Promise<AxiosResponse> {
    return api.post('/employees/byCode', employeeObj);
  },

  async dowloadTemplate(): Promise<AxiosResponse> {
    return api.get('/employees/dowloadTemplate',);
  },
  
  async uploadFile(file: any): Promise<AxiosResponse> {
    return api.post('/employees/uploadFile',file);
  },
};
