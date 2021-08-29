<template>
  <div>
    <ConfirmDialog position="top"></ConfirmDialog>
    <Toast />
    <Sidebar
      v-model:visible="showSlideOut"
      position="right"
      style="width: 1100px"
      @hide="getData()"
    >
      <WarehouseCardDetails
        :rec="selectedRec"
        @cancel="showSlideOut = false; getData()"
        @changed="getData()"
        :arrSupplies="arrSupplies"
        :arrWarehouse="arrWarehouse"
        :isNew="isNewRec"
      ></WarehouseCardDetails>
    </Sidebar>
    <h3>Quản lý thẻ kho</h3>
    <div class="p-d-flex p-flex-row p-mb-3 p-jc-around" style="width: 1500px">
      <div>
        <label
          class="p-d-inline-block m-label-size-3 p-text-left p-mr-1"
          style="padding-top: 7px"
          >Mã thẻ kho
        </label>
        <InputText
          type="text"
          v-model="searchCode"
          class="p-inputtext-sm"
          style="width: 200px; height: 30px; margin: 1px 0px 0 0px"
        />
      </div>
      <div>
        <label
          class="p-d-inline-block m-label-size-3 p-text-left p-mr-1"
          style="padding-top: 7px"
          >Tên thẻ kho
        </label>
        <InputText
          type="text"
          v-model="searchName"
          class="p-inputtext-sm"
          style="width: 200px; height: 30px; margin: 1px 0px 0 0px"
        />
      </div>
      <div>
        <label
          class="p-d-inline-block m-label-size-3 p-text-left p-mr-1"
          style="padding-top: 7px"
          >Vật tư
        </label>
        <Dropdown
          class="p-inputtext-sm"
          style="width: 200px"
          v-model="searchSupplies"
          :options="supplies"
          :filter="true"
          :showClear="true"
          optionLabel="name"
          optionValue="suppliesId"
          placeholder="--Hãy chọn--"
        />
      </div>
      <div>
        <label
          class="p-d-inline-block m-label-size-3 p-text-left p-mr-1"
          style="padding-top: 7px"
          >Kho
        </label>
        <Dropdown
          class="p-inputtext-sm"
          style="width: 200px"
          v-model="searchWarehouse"
          :options="warehouse"
          :filter="true"
          :showClear="true"
          optionLabel="name"
          optionValue="warehouseId"
          placeholder="--Hãy chọn--"
        />
      </div>
    </div>
    <div class="p-d-flex p-flex-row p-mb-3 p-jc-around" style="width: 1500px">
      <div>
        <label
          class="p-d-inline-block m-label-size-3 p-text-left p-mr-1"
          style="padding-top: 7px"
          >Tạo từ ngày
        </label>
        <InputText
          type="date"
          v-model="searchFormDate"
          class="p-inputtext-sm"
          placeholder="dd/mm/yyyy"
          style="width: 200px; height: 30px"
        />
      </div>
      <div>
        <label
          class="p-d-inline-block m-label-size-3 p-text-left p-mr-1"
          style="padding-top: 7px"
          >Đến ngày
        </label>
        <InputText
          type="date"
          v-model="searchToDate"
          class="p-inputtext-sm"
          placeholder="dd/mm/yyyy"
          style="width: 200px; height: 30px"
        />
      </div>
      <div>
        <label
          class="p-d-inline-block m-label-size-3 p-text-left p-mr-1"
          style="padding-top: 7px"
          >Nhân viên
        </label>
        <Dropdown
          class="p-inputtext-sm"
          style="width: 200px"
          v-model="searchEmployee"
          :options="emp"
          :filter="true"
          :showClear="true"
          optionLabel="fullName"
          optionValue="employeeId"
          placeholder="--Hãy chọn--"
        />
      </div>
    </div>
    <div
      class="p-d-flex p-flex-row p-mb-3 p-jc-center"
      style="width: 1500px; margin: 20px 0"
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
      stripedRows showGridlines
      @page="onPageChange($event)"
      class="p-datatable-sm p-datatable-hoverable-rows m-border p-mb-4"
      style="width: 1500px"
    >
      <Column field="index" header="STT" headerStyle="width:70px;" bodyStyle="text-align-last: center;"></Column>
      <Column
        field="code"
        header="Mã thẻ kho"
        headerStyle="width:70px"
      ></Column>
      <Column
        field="name"
        header="Tên thẻ kho"
        headerStyle="width:160px"
      ></Column>
      <Column
        field="strDateCreated"
        header="Ngày tạo thẻ"
        headerStyle="width:120px"
      ></Column>
      <Column
        field="fullName"
        header="Người tạo thẻ kho"
        headerStyle="width:150px"
      ></Column>
      <Column
        field="suppliesCode"
        header="Mã vật tư"
        headerStyle="width:120px"
      ></Column>
      <Column
        field="suppliesName"
        header="Tên vật tư"
        headerStyle="width:120px"
      ></Column>
      <Column
        field="warehouseCode"
        header="Mã kho hàng"
        headerStyle="width:100px"
      ></Column>
      <Column
        field="countReceipt"
        header="Số phiếu nhập"
        headerStyle="width:90px"
      ></Column>
      <Column
        field="countDeliveryBill"
        header="Số phiếu xuất"
        headerStyle="width:90px"
      ></Column>
      <Column
        field="amountReceipt"
        header="Số lượng nhập"
        headerStyle="width:90px"
      ></Column>
      <Column
        field="amountDeliveryBill"
        header="Số lượng xuất"
        headerStyle="width:90px"
      ></Column>
      <Column
        field="amountInventory"
        header="Số lượng tồn"
        headerStyle="width:90px"
      ></Column>
      <Column header="ACTION" headerStyle="width:100px" bodyStyle="padding:3px; text-align: center;">
        <template #body="slotProps">
          <Button
            icon="pi pi-pencil"
            @click="onEditClick(slotProps.data)"
            class="p-button-sm p-button-rounded p-button-secondary p-button-text"
          />
          <Button
            icon="pi pi-trash"
            @click="onDeleteClick(slotProps.data)"
            class="p-button-sm p-button-rounded p-button-danger p-button-text"
          />
          <Button
            icon="pi pi-book"
            @click="onDownloadFileDocx(slotProps.data)"
            class="p-button-sm p-button-rounded p-button-info p-button-text"
          />
        </template>
      </Column>
    </DataTable>
  </div>
</template>


<script lang='ts'>
import { ref, onMounted, defineComponent } from "vue";
import WarehouseCardApi from "@/api/material-management/warehouse-card-api"; // eslint-disable-line import/no-cycle
import WarehouseCardDetails from "@/views/material-management/warehouse-card/WarehouseCardDetails.vue";
import { useConfirm } from "primevue/useconfirm";
import { useToast } from "primevue/usetoast";
import SuppliesApi from "@/api/material-management/supplies-api";
import WarehouseApi from "@/api/material-management/warehouse-api";
import EmployeeApi from "@/api/employee-api";
import { debounce, exportFile, exportFileDocx } from "@/shared/utils";
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
    const arrWarehouse = ref([]);
    const arrSupplies = ref([]);
    const confirm = useConfirm();
    const toast = useToast();
    let currentPage = 1;
    let emp = ref([]);
    let supplies = ref([]);
    let warehouse = ref([]);
    let searchFormDate = ref("");
    let searchToDate = ref("");
    let searchName = ref("");
    let searchCode = ref("");
    let searchEmployee = ref("");
    let searchWarehouse = ref("");
    let searchSupplies = ref("");

    const getData = async (
      page: number,
      requestedPageSize: number,
      warehouseCardId = "",
      searchCode = "",
      searchName = "",
      searchEmployee = "",
      searchWarehouse = "",
      searchFormDate = "",
      searchToDate = "",
      searchSupplies = ""
    ) => {
      searchEmployee = searchEmployee === "null" ? "0" : searchEmployee;
      searchWarehouse = searchWarehouse === "null" ? "0" : searchWarehouse;
      searchSupplies = searchSupplies === "null" ? "0" : searchSupplies;
      try {
        const resp = await WarehouseCardApi.getWarehouseCards(
          page,
          requestedPageSize,
          warehouseCardId,
          searchCode,
          searchName,
          searchEmployee,
          searchWarehouse,
          searchFormDate,
          searchToDate,
          searchSupplies
        );
        let i = 1;
        list.value = resp.data.list.map((v: Record<string, unknown>) => {
          const dt = new Date(v.dateCreated as string);
          const strDateCreated = new Intl.DateTimeFormat(["ban", "id"], {
            year: "numeric",
            month: "2-digit",
            day: "2-digit",
          }).format(dt);
          let index = 1;
          if (page > 1) {
            index = 10 * (currentPage - 1) + i++;
          } else {
            index = i++;
          }
          return {
            ...v,
            strDateCreated,
            index,
          };
        });
        // isLoading.value = false;
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
        message: `Bạn có muốn xóa bản ghi ${rec.code} không ?`,
        header: "Xác nhận!",
        icon: "pi pi-question-circle",
        acceptIcon: "pi pi-check",
        accept: async () => {
          try {
            const resp = await WarehouseCardApi.deleteWarehouseCard(
              rec.warehouseCardId as string
            );
            if (resp.data.msgType === "SUCCESS") {
              getData(currentPage, pageSize.value);
              toast.add({
                severity: "success",
                summary: "Xóa bản ghi thành công!",
                life: 3000,
              });
            } else {
              toast.add({
                severity: "error",
                summary: "Quyền truy cập bị từ chối!",
                detail: resp.data.msg,
                life: 3000,
              });
            }
          } catch (e) {
            toast.add({
              severity: "error",
              summary: "Lỗi xảy ra!",
              detail: "Vui lòng liên hệ với quản trị viên!",
              life: 3000,
            });
          }
        },
        reject: () => {
          console.log("NO");
        },
      });
    };

    const onPageChange = (event: Record<string, unknown>) => {
      if (currentPage !== (event.page as number) + 1) {
        currentPage = (event.page as number) + 1;
        getData(
          currentPage,
          pageSize.value,
          `${searchName.value}`,
          `${searchCode.value}`,
          `${searchEmployee.value}`,
          `${searchWarehouse.value}`,
          `${searchFormDate.value.toString()}`,
          `${searchToDate.value.toString()}`,
          `${searchSupplies.value}`
        );
      }
    };

    const onAddClick = async () => {
      const today = new Date().getTime();
      const resp = await WarehouseApi.getAll();
      let warehouseItem: any;
      if (resp.data.list) {
        warehouseItem = resp.data.list;
      }
      arrWarehouse.value = warehouseItem;

      const supp = await SuppliesApi.getAll();
      let itemSupplies: any;
      if (supp.data.list) {
        itemSupplies = supp.data.list;
      }
      arrSupplies.value = itemSupplies;
      const sequence = await WarehouseCardApi.getSequence();
      let sequenceId: any;
      if (sequence.data) {
        sequenceId = sequence.data;
      }
      await WarehouseCardApi.deleteByWarehouseCardId(sequenceId);
      isNewRec.value = true;
      selectedRec.value = { warehouseCardId: sequenceId, dateCreated: today };
      showSlideOut.value = true;
    };

    const onDeleteClick = (rec: Record<string, unknown>) => {
      confirmDialog(rec);
    };

    const onEditClick = async (rec: Record<string, unknown>) => {
      const resp = await WarehouseApi.getAll();
      let warehouseItem: any;
      if (resp.data.list) {
        warehouseItem = resp.data.list;
      }
      arrWarehouse.value = warehouseItem;

      const supp = await SuppliesApi.getAll();
      let itemSupplies: any;
      if (supp.data.list) {
        itemSupplies = supp.data.list;
      }
      arrSupplies.value = itemSupplies;
      showSlideOut.value = true;
      selectedRec.value = rec;
    };

    const onSearchKeyup = debounce(
      () =>
        getData(
          1,
          pageSize.value,
          "",
          `${searchName.value}`,
          `${searchCode.value}`,
          `${searchEmployee.value}`,
          `${searchWarehouse.value}`,
          `${searchFormDate.value.toString()}`,
          `${searchToDate.value.toString()}`,
          `${searchSupplies.value}`
        ),
      400
    );

    const exportExcell = async () => {
      await WarehouseCardApi.export(
        `${searchName.value}`,
          `${searchCode.value}`,
          `${searchEmployee.value}`,
          `${searchWarehouse.value}`,
          `${searchFormDate.value.toString()}`,
          `${searchToDate.value.toString()}`,
          `${searchSupplies.value}`
      ).then((res) => {
        const data = res.data.data;
        exportFile(data.data, data.fileName);
      });
    };

    onMounted(() => {
      getData(0, pageSize.value);
      lstEmp();
      lstWarehouse();
      lstSupplies();
    });

    const lstEmp = async () => {
      const resp = await EmployeeApi.getAll();
      let lstEmps = [];
      if (resp.data) {
        lstEmps = resp.data.list;
      }
      emp.value = lstEmps;
    };

    const lstWarehouse = async () => {
      const resp = await WarehouseApi.getAll();
      let lstWarehouses = [];
      if (resp.data) {
        lstWarehouses = resp.data.list;
      }
      warehouse.value = lstWarehouses;
    };

    const lstSupplies = async () => {
      const resp = await SuppliesApi.getAll();
      let lstSuppliess = [];
      if (resp.data) {
        lstSuppliess = resp.data.list;
      }
      supplies.value = lstSuppliess;
    };

    const onDownloadFileDocx = async (rec: Record<string, unknown>) => {
      WarehouseCardApi.downloadFileDocx(rec.warehouseCardId).then((res) => {
        const data = res.data.data;
        exportFileDocx(data.data, data.fileName);
      });
    };

    return {
      list,
      arrWarehouse,
      arrSupplies,
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
      emp,
      warehouse,
      supplies,
      searchCode,
      searchName,
      searchEmployee,
      searchWarehouse,
      searchFormDate,
      searchToDate,
      searchSupplies,
      onSearchKeyup,
      exportExcell,
      onDownloadFileDocx
    };
  },
  components: {
    WarehouseCardDetails,
  },
});
</script>
