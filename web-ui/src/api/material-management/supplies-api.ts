import api from '@/api/api-service';
import { AxiosResponse } from 'axios';

export default {
  async getSupplies(
    page = 1,
    pageSize = 20,
    suppliesId = '',
    searchCode = "",
    searchName = "",
    searchSupplier = "",
    searchSpecies = "",
    searchFormPrice = "",
    searchToPrice = "",
    searchQuality = "",
    searchUnit = "",): Promise<AxiosResponse> {
    const qsParams: Record<string, number | string> = {};
    if (page) {
      qsParams.page = page;
    }
    if (pageSize) {
      qsParams['page-size'] = pageSize;
    }
    if (suppliesId) {
      qsParams.id = suppliesId;
    }
    if (searchCode) {
      qsParams.searchCode = searchCode;
    }
    if (searchName) {
      qsParams.searchName = searchName;
    }
    if (searchSupplier) {
      qsParams.searchSupplier = searchSupplier;
    }
    if (searchSpecies) {
      qsParams.searchSpecies = searchSpecies;
    }
    if (searchFormPrice) {
      qsParams.searchFormPrice = searchFormPrice;
    }
    if (searchToPrice) {
      qsParams.searchToPrice = searchToPrice;
    }
    if (searchQuality) {
      qsParams.searchQuality = searchQuality;
    }
    if (searchUnit) {
      qsParams.searchUnit = searchUnit;
    }

    return api.get('/supplies', { params: qsParams });
  },
  async export(
    searchCode = "",
    searchName = "",
    searchSupplier = "",
    searchSpecies = "",
    searchFormPrice = "",
    searchToPrice = "",
    searchQuality = "",
    searchUnit = "",): Promise<AxiosResponse> {
    const qsParams: Record<string, number | string> = {};
    if (searchCode) {
      qsParams.code = searchCode;
    }
    if (searchName) {
      qsParams.name = searchName;
    }
    if (searchSupplier) {
      qsParams.supplierId = searchSupplier;
    }
    if (searchSpecies) {
      qsParams.speciesId = searchSpecies;
    }
    if (searchFormPrice) {
      qsParams.formPrice = searchFormPrice;
    }
    if (searchToPrice) {
      qsParams.toPrice = searchToPrice;
    }
    if (searchQuality) {
      qsParams.qualityId = searchQuality;
    }
    if (searchUnit) {
      qsParams.unit = searchUnit;
    }

    return api.post('/supplies/export', qsParams);
  },

  async deleteSupplies(suppliesId: string): Promise<AxiosResponse> {
    return api.delete(`/supplies/${suppliesId}`);
  },

  async addSupplies(suppliesObj: Record<string, string | number>): Promise<AxiosResponse> {
    return api.post('/supplies', suppliesObj);
  },

  async updateSupplies(suppliesObj: Record<string, string | number>): Promise<AxiosResponse> {
    return api.put('/supplies', suppliesObj);
  },

  async getSuppliesById(suppliesId: string): Promise<AxiosResponse> {
    return api.get(`/supplies/${suppliesId}`);
  },

  async getAll(): Promise<AxiosResponse> {
    return api.get(`/supplies/all`);
  },
  async getSuppliesByCode(suppliesObj: Record<string, string | number>): Promise<AxiosResponse> {
    return api.post('/supplies/byCode', suppliesObj);
  },
};
