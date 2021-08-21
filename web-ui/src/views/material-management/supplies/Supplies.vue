<template>
  <div>
    <ConfirmDialog position="top"></ConfirmDialog>
    <Toast />
    <Sidebar
      v-model:visible="showSlideOut"
      position="right"
      style="width: 700px"
    >
      <SuppliesDetails
        :rec="selectedRec"
        @cancel="showSlideOut = false"
        @changed="getData()"
        :isNew="isNewRec"
      ></SuppliesDetails>
    </Sidebar>
    <h3>Quản lý vật tư</h3>
    <div class="p-d-flex p-flex-row p-mb-3 p-jc-around" style="width: 1350px">
      <div>
        <label
          class="p-d-inline-block m-label-size-3 p-text-left p-mr-1"
          style="padding-top: 7px"
          >Mã vật tư
        </label>
        <span class="p-input-icon-left">
          <InputText
            type="text"
            v-model="searchCode"
            class="p-inputtext-sm"
            style="width: 200px; height: 30px; margin: 1px 0px 0 0px"
          />
        </span>
      </div>
      <div>
        <label
          class="p-d-inline-block m-label-size-3 p-text-left p-mr-1"
          style="padding-top: 7px"
          >Tên vật tư
        </label>
        <span class="p-input-icon-left">
          <InputText
            type="text"
            v-model="searchName"
            class="p-inputtext-sm"
            style="width: 200px; height: 30px; margin: 1px 0px 0 0px"
          />
        </span>
      </div>
      <div>
        <label
          class="p-d-inline-block m-label-size-3 p-text-left p-mr-1"
          style="padding-top: 7px"
          >Nhà cung cấp
        </label>
        <Dropdown
          class="p-inputtext-sm"
          style="width: 200px"
          v-model="searchSupplier"
          :options="supplier"
          :filter="true"
          :showClear="true"
          optionLabel="name"
          optionValue="supplierId"
        />
      </div>
      <div>
        <label
          class="p-d-inline-block m-label-size-3 p-text-left p-mr-1"
          style="padding-top: 7px"
          >Chủng loại
        </label>
        <Dropdown
          class="p-inputtext-sm"
          style="width: 200px"
          v-model="searchSpecies"
          :options="species"
          :filter="true"
          :showClear="true"
          optionLabel="name"
          optionValue="speciesId"
        />
      </div>
    </div>
    <div class="p-d-flex p-flex-row p-mb-3 p-jc-around" style="width: 1350px">
      <div>
        <label
          class="p-d-inline-block m-label-size-3 p-text-left p-mr-1"
          style="padding-top: 7px"
          >Giá từ
        </label>
        <span class="p-input-icon-left">
          <InputNumber
            type="text"
            v-model="searchFormPrice"
            class="p-inputtext-sm"
            style="width: 200px; height: 30px"
          />
        </span>
      </div>
      <div>
        <label
          class="p-d-inline-block m-label-size-3 p-text-left p-mr-1"
          style="padding-top: 7px"
          >Giá đến
        </label>
        <span class="p-input-icon-left">
          <InputNumber
            type="text"
            v-model="searchToPrice"
            class="p-inputtext-sm"
            style="width: 200px; height: 30px"
          />
        </span>
      </div>
      <div>
        <label
          class="p-d-inline-block m-label-size-3 p-text-left p-mr-1"
          style="padding-top: 7px"
          >Chất lượng
        </label>
        <Dropdown
          class="p-inputtext-sm"
          style="width: 200px"
          v-model="searchQuality"
          :options="quality"
          :filter="true"
          :showClear="true"
          optionLabel="name"
          optionValue="qualityId"
        />
      </div>
      <div>
        <label
          class="p-d-inline-block m-label-size-3 p-text-left p-mr-1"
          style="padding-top: 7px"
          >Đơn vị tính
        </label>
        <Dropdown
          class="p-inputtext-sm"
          style="width: 200px"
          v-model="searchUnit"
          :options="['Cái', 'Kg', 'Cân', 'Lít', 'Mét', 'Dm', 'Cm']"
          :filter="true"
          :showClear="true"
        />
      </div>
    </div>
    <div
      class="p-d-flex p-flex-row p-mb-3 p-jc-center"
      style="width: 1350px; margin: 20px 0"
    >
      <Button
        icon="pi pi-download"
        iconPos="right"
        label="Báo cáo"
        @click="exportExcell()"
        class="p-ml-1 p-button-sm"
      ></Button>
      <Button
        icon="pi pi-search"
        iconPos="right"
        label="Tìm kiếm"
        @click="onSearchKeyup()"
        class="p-ml-1 p-button-sm"
      ></Button>
      <Button
        icon="pi pi-user"
        iconPos="right"
        label="ADD"
        @click="onAddClick()"
        class="p-ml-1 p-button-sm"
      ></Button>
    </div>
    <DataTable
      :value="list"
      :paginator="true"
      :lazy="true"
      :rows="pageSize"
      :totalRecords="totalRecs"
      :loading="isLoading"
      @page="onPageChange($event)"
      class="p-datatable-sm p-datatable-hoverable-rows m-border p-mb-4"
      style="width: 1350px"
    >
      <Column field="index" header="STT" headerStyle="width:90px;"></Column>
      <Column field="code" header="Mã vật tư" headerStyle="width:90px"></Column>
      <Column
        field="name"
        header="Tên vật tư"
        headerStyle="width:160px"
      ></Column>
      <Column
        field="speciesName"
        header="Chủng loại"
        headerStyle="width:160px"
      ></Column>
      <Column
        field="supplierName"
        header="Nhà cung cấp"
        headerStyle="width:160px"
      ></Column>
      <Column
        field="qualityName"
        header="Chất lượng"
        headerStyle="width:160px"
      ></Column>
      <Column
        field="unit"
        header="Đơn vị tính"
        headerStyle="width:160px"
      ></Column>
      <Column
        field="price"
        header="Giá vật tư"
        headerStyle="width:160px"
      ></Column>
      <Column header="ACTION" headerStyle="width:100px" bodyStyle="padding:3px">
        <template #body="slotProps">
          <Button
            icon="pi pi-pencil"
            @click="onEditClick(slotProps.data)"
            class="
              p-button-sm p-button-rounded p-button-secondary p-button-text
            "
          />
          <Button
            icon="pi pi-trash"
            @click="onDeleteClick(slotProps.data)"
            class="p-button-sm p-button-rounded p-button-danger p-button-text"
          />
        </template>
      </Column>
    </DataTable>
  </div>
</template>


<script lang='ts'>
import { ref, onMounted, defineComponent } from "vue";
import SuppliesApi from "@/api/material-management/supplies-api"; // eslint-disable-line import/no-cycle
import SuppliesDetails from "@/views/material-management/supplies/SuppliesDetails.vue";
import { useConfirm } from "primevue/useconfirm";
import { useToast } from "primevue/usetoast";
import SupplierApi from "@/api/material-management/supplier-api";
import QualityApi from "@/api/material-management/quality-api";
import SpeciesApi from "@/api/material-management/species-api";
import { debounce, exportFile } from "@/shared/utils";

export default defineComponent({
  setup(): unknown {
    const isLoading = ref(false);
    const showSlideOut = ref(false);
    const pageSize = ref(10);
    const totalPages = ref(0);
    const totalRecs = ref(0);
    const selectedRec = ref({});
    const isNewRec = ref(false);
    const isCustomer = ref(false);
    const list = ref([]);
    const confirm = useConfirm();
    const toast = useToast();
    let currentPage = 1;
    let quality = ref([]);
    let species = ref([]);
    let supplier = ref([]);
    let searchName = ref("");
    let searchCode = ref("");
    let searchSupplier = ref("");
    let searchSpecies = ref("");
    let searchFormPrice = ref("");
    let searchToPrice = ref("");
    let searchQuality = ref("");
    let searchUnit = ref("");

    const getData = async (
      page: number,
      requestedPageSize: number,
      suppliesId = "",
      searchCode = "",
      searchName = "",
      searchSupplier = "",
      searchSpecies = "",
      searchFormPrice = "",
      searchToPrice = "",
      searchQuality = "",
      searchUnit = ""
    ) => {
      searchFormPrice = searchFormPrice === "null" ? "0" : searchFormPrice;
      searchToPrice = searchToPrice === "null" ? "0" : searchToPrice;
      try {
        const resp = await SuppliesApi.getSupplies(
          page,
          requestedPageSize,
          suppliesId,
          searchCode,
          searchName,
          searchSupplier,
          searchSpecies,
          searchFormPrice,
          searchToPrice,
          searchQuality,
          searchUnit
        );
        let i = 1;
        list.value = resp.data.list.map((v: Record<string, unknown>) => {
          let index = 1;
          if (page > 1) {
            index = 10 * (currentPage - 1) + i++;
          } else {
            index = i++;
          }
          return {
            ...v,
            index,
          };
        });
        currentPage = resp.data.currentPage;
        totalPages.value = resp.data.totalPages;
        totalRecs.value = resp.data.total;
      } catch (err) {
        console.log("REST ERROR: %O", err.response ? err.response : err);
        isLoading.value = false;
      }
    };

    const confirmDialog = (rec: Record<string, unknown>) => {
      confirm.require({
        message: `Do you want to remove ${rec.name} from product catalog ?`,
        header: "Remove",
        icon: "pi pi-question-circle",
        acceptIcon: "pi pi-check",
        accept: async () => {
          try {
            const resp = await SuppliesApi.deleteSupplies(
              rec.suppliesId as string
            );
            if (resp.data.msgType === "SUCCESS") {
              getData(currentPage, pageSize.value);
              toast.add({
                severity: "success",
                summary: "Successfully Deleted",
                life: 3000,
              });
            } else {
              toast.add({
                severity: "error",
                summary: "Access Denied",
                detail: resp.data.msg,
                life: 3000,
              });
            }
          } catch (e) {
            toast.add({
              severity: "error",
              summary: "Error",
              detail: "Unable to connect to server",
              life: 3000,
            });
          }
        },
        reject: () => {
          console.log("NO");
        },
      });
    };

    const onPageChange = async (event: Record<string, unknown>) => {
      if (currentPage !== (event.page as number) + 1) {
        currentPage = (event.page as number) + 1;
        await getData(
          currentPage,
          pageSize.value,
          "",
          `${searchCode.value}`,
          `${searchName.value}`,
          `${searchSupplier.value}`,
          `${searchSpecies.value}`,
          `${searchFormPrice.value}`,
          `${searchToPrice.value}`,
          `${searchQuality.value}`,
          `${searchUnit.value}`
        );
      }
    };

    const exportExcell = async () => {
      await SuppliesApi.export(
        `${searchCode.value}`,
        `${searchName.value}`,
        `${searchSupplier.value}`,
        `${searchSpecies.value}`,
        `${searchFormPrice.value}`,
        `${searchToPrice.value}`,
        `${searchQuality.value}`,
        `${searchUnit.value}`
      ).then((res) => {
        const data = res.data.data;
        exportFile(data.data, data.fileName);
      });
    };

    const onAddClick = async () => {
      isNewRec.value = true;
      selectedRec.value = { suppliesId: "" };
      showSlideOut.value = true;
    };

    const onDeleteClick = (rec: Record<string, unknown>) => {
      confirmDialog(rec);
    };

    const onEditClick = async (rec: Record<string, unknown>) => {
      showSlideOut.value = true;
      selectedRec.value = rec;
    };

    onMounted(() => {
      getData(0, pageSize.value);
      lstSupplier();
      lstQuality();
      lstSpecies();
    });

    const lstSupplier = async () => {
      const resp = await SupplierApi.getAll();
      let lstSuppliers = [];
      if (resp.data) {
        lstSuppliers = resp.data.list;
      }
      supplier.value = lstSuppliers;
    };

    const lstQuality = async () => {
      const resp = await QualityApi.getAll();
      let lstQualitys = [];
      if (resp.data) {
        lstQualitys = resp.data.list;
      }
      quality.value = lstQualitys;
    };

    const lstSpecies = async () => {
      const resp = await SpeciesApi.getAll();
      let lstSpeciess = [];
      if (resp.data) {
        lstSpeciess = resp.data.list;
      }
      species.value = lstSpeciess;
    };

    const onSearchKeyup = debounce(async () => {
      if (
        parseInt(`${searchFormPrice.value}`) >
        parseInt(`${searchToPrice.value}`) && parseInt(`${searchToPrice.value}`) > 0
      ) {
        toast.add({
          severity: "warn",
          summary: "Cảnh báo",
          detail: "Trường giá từ phải nhỏ hơn trường giá đến",
          life: 3000,
        });
      } else {
        await getData(
          1,
          pageSize.value,
          "",
          `${searchCode.value}`,
          `${searchName.value}`,
          `${searchSupplier.value}`,
          `${searchSpecies.value}`,
          `${searchFormPrice.value}`,
          `${searchToPrice.value}`,
          `${searchQuality.value}`,
          `${searchUnit.value}`
        );
      }
    }, 400);

    return {
      list,
      isLoading,
      showSlideOut,
      pageSize,
      totalPages,
      totalRecs,
      selectedRec,
      isNewRec,
      isCustomer,
      onAddClick,
      onDeleteClick,
      onEditClick,
      onPageChange,
      getData,
      supplier,
      species,
      quality,
      searchCode,
      searchName,
      searchSupplier,
      searchSpecies,
      searchFormPrice,
      searchToPrice,
      searchQuality,
      searchUnit,
      onSearchKeyup,
      exportExcell
    };
  },
  components: {
    SuppliesDetails,
  },
});
</script>
