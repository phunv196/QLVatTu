import api from '@/api/api-service';
import { AxiosResponse } from 'axios';

export default {
  async getWarehouseCardFlows(page = 1, pageSize = 5, warehouseCardId : number): Promise<AxiosResponse> {
    const qsParams: Record<string, number | string> = {};
    if (page) { qsParams.page = page; }
    if (pageSize) { qsParams['page-size'] = pageSize; }
    if (warehouseCardId) { qsParams.warehouseCardId = warehouseCardId; }
    return api.get('/warehouse_card_flows', { params: qsParams });
  },

  async deleteWarehouseCardFlow(warehouseCardFlowId: string): Promise<AxiosResponse> {
    return api.delete(`/warehouse_card_flows/${warehouseCardFlowId}`);
  },

  async addWarehouseCardFlow(warehouseCardFlowObj: Record<string, string | number>): Promise<AxiosResponse> {
    return api.post('/warehouse_card_flows', warehouseCardFlowObj);
  },

  async updateWarehouseCardFlow(warehouseCardFlowObj: Record<string, string | number>): Promise<AxiosResponse> {
    return api.put('/warehouse_card_flows', warehouseCardFlowObj);
  },
};
