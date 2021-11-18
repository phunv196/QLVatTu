import api from '@/api/api-service';
import { AxiosResponse } from 'axios';

export default {
  async getWarehouseCards(page = 1, pageSize = 20, warehouseCardId = '', searchCode = "",
    searchName = "", searchEmployee = "", searchWarehouse = "", searchFormDate = "",
    searchToDate = "", searchSupplies = ""): Promise<AxiosResponse> {
    const qsParams: Record<string, number | string> = {};
    if (page) { qsParams.page = page; }
    if (pageSize) { qsParams['page-size'] = pageSize; }
    if (warehouseCardId) { qsParams.id = warehouseCardId; }
    if (searchCode) { qsParams.searchCode = searchCode; }
    if (searchName) { qsParams.searchName = searchName; }
    if (searchEmployee) { qsParams.searchEmployee = searchEmployee; }
    if (searchWarehouse) { qsParams.searchWarehouse = searchWarehouse; }
    if (searchFormDate) { qsParams.searchFormDate = searchFormDate; }
    if (searchToDate) { qsParams.searchToDate = searchToDate; }
    if (searchSupplies) { qsParams.searchSupplies = searchSupplies; }
    return api.get('/warehouse_cards', { params: qsParams });
  },

  async export(
    searchCode = "",
    searchName = "",
    searchEmployee = "",
    searchWarehouse = "",
    searchFormDate = "",
    searchToDate = "",
    searchSupplies = ""): Promise<AxiosResponse> {
    const qsParams: Record<string, number | string> = {};
    if (searchCode) { qsParams.code = searchCode; }
    if (searchName) { qsParams.name = searchName; }
    if (searchEmployee) { qsParams.employeeId = searchEmployee; }
    if (searchWarehouse) { qsParams.warehouseId = searchWarehouse; }
    if (searchFormDate) { qsParams.formDate = searchFormDate; }
    if (searchToDate) { qsParams.toDate = searchToDate; }
    if (searchSupplies) { qsParams.suppliesId = searchSupplies; }
    return api.post('/warehouse_cards/export', qsParams);
  },

  async deleteWarehouseCard(warehouseCardId: string): Promise<AxiosResponse> {
    return api.delete(`/warehouse_cards/${warehouseCardId}`);
  },

  async addWarehouseCard(warehouseCardObj: Record<string, string | number>): Promise<AxiosResponse> {
    return api.post('/warehouse_cards', warehouseCardObj);
  },

  async updateWarehouseCard(warehouseCardObj: Record<string, string | number>): Promise<AxiosResponse> {
    return api.put('/warehouse_cards', warehouseCardObj);
  },

  async getAll(): Promise<AxiosResponse> {
    return api.get(`/warehouse_cards/all`);
  },

  async getSequence(): Promise<AxiosResponse> {
    return api.get(`/warehouse_cards/sequence`);
  },

  async checkId(warehouseCardId: string): Promise<AxiosResponse> {
    return api.get(`/warehouse_cards/equal/${warehouseCardId}`);
  },

  async deleteByWarehouseCardId(warehouseCardId: string): Promise<AxiosResponse> {
    return api.delete(`/warehouse_cards/delete_by_id/${warehouseCardId}`);
  },

  async getWarehouseCardByCode(warehouseCardObj: Record<string, string | number>): Promise<AxiosResponse> {
    return api.post('/warehouse_cards/byCode', warehouseCardObj);
  },

  async downloadFileDocx(deliveryBillId: any): Promise<AxiosResponse> {
    return api.get(`/warehouse_cards/downloadFileDocx/${deliveryBillId}`);
  },

  async getAmountInventory(suppliesId: any): Promise<AxiosResponse> {
    return api.get(`/warehouse_cards/inventory/${suppliesId}`);
  },
};
