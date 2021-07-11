<template>
  <div>
    <ConfirmDialog position="top"></ConfirmDialog>
    <Toast />
    <Sidebar
      v-model:visible="showSlideOut"
      position="right"
      style="width: 1100px"
    >
      <DeliveryBillDetails
        :rec="selectedRec"
        @cancel="showSlideOut = false"
        @changed="getData()"
        :arrWarehouse="arrWarehouse"
        :arrFactory="arrFactory"
        :isNew="isNewRec"
      ></DeliveryBillDetails>
    </Sidebar>
    <h3>Quản lý phiếu xuất kho</h3>
    <div class="p-d-flex p-flex-row p-mb-1 p-jc-around" style="width: 1350px">
      <div>
        <label
          class="p-d-inline-block m-label-size-3 p-text-left p-mr-1"
          style="padding-top: 7px"
          >Mã phiếu xuất
        </label>
        <span class="p-input-icon-left">
          <i class="pi pi-search" style="margin: -6px 10px 0px" />
          <InputText
            type="text"
            v-model="searchCode"
            class="p-inputtext-sm"
            placeholder="Search by code"
            style="width: 200px; height: 30px; margin: 1px 0px 0 0px"
          />
        </span>
      </div>
      <div>
        <label
          class="p-d-inline-block m-label-size-3 p-text-left p-mr-1"
          style="padding-top: 7px"
          >Tên phiếu xuất
        </label>
        <span class="p-input-icon-left">
          <i class="pi pi-search" style="margin: -6px 10px 0px" />
          <InputText
            type="text"
            v-model="searchName"
            class="p-inputtext-sm"
            placeholder="Search by name"
            style="width: 200px; height: 30px; margin: 1px 0px 0 0px"
          />
        </span>
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
          optionValue="id"
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
        />
      </div>
    </div>
    <div class="p-d-flex p-flex-row p-mb-1 p-jc-around" style="width: 1350px">
      <div>
        <label
          class="p-d-inline-block m-label-size-3 p-text-left p-mr-1"
          style="padding-top: 7px"
          >Ngày lập từ
        </label>
        <span class="p-input-icon-left">
          <i class="pi pi-search" style="margin: -6px 10px 0px" />
          <InputText
            type="date"
            v-model="searchFormDate"
            class="p-inputtext-sm"
            placeholder="dd/mm/yyyy"
            style="width: 200px; height: 30px"
          />
        </span>
      </div>
      <div>
        <label
          class="p-d-inline-block m-label-size-3 p-text-left p-mr-1"
          style="padding-top: 7px"
          >Đến ngày
        </label>
        <span class="p-input-icon-left">
          <i class="pi pi-search" style="margin: -6px 10px 0px" />
          <InputText
            type="date"
            v-model="searchToDate"
            class="p-inputtext-sm"
            placeholder="dd/mm/yyyy"
            style="width: 200px; height: 30px"
          />
        </span>
      </div>
      <div>
        <label
          class="p-d-inline-block m-label-size-3 p-text-left p-mr-1"
          style="padding-top: 7px"
          >Phân xưởng
        </label>
        <Dropdown
          class="p-inputtext-sm"
          style="width: 200px"
          v-model="searchFactory"
          :options="factory"
          :filter="true"
          :showClear="true"
          optionLabel="name"
          optionValue="factoryId"
        />
      </div>
      <div>
        <label
          class="p-d-inline-block m-label-size-3 p-text-left p-mr-1"
          style="padding-top: 7px"
        >
        </label>
        <span class="p-input-icon-left" style="width: 200px; height: 30px">
        </span>
      </div>
    </div>
    <div
      class="p-d-flex p-flex-row p-mb-1 p-jc-center"
      style="width: 1350px; margin: 20px 0"
    >
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
      <Column
        field="deliveryBillId"
        header="ID phiếu xuất"
        headerStyle="width:90px;"
      ></Column>
      <Column
        field="code"
        header="Mã phiếu xuất"
        headerStyle="width:90px"
      ></Column>
      <Column
        field="name"
        header="Tên phiếu xuất"
        headerStyle="width:160px"
      ></Column>
      <Column
        field="strDateDeliveryBill"
        header="Ngày lập phiếu"
        headerStyle="width:160px"
      ></Column>
      <Column
        field="fullName"
        header="Nhân viên"
        headerStyle="width:160px"
      ></Column>
      <Column
        field="warehouseCode"
        header="Mã kho hàng"
        headerStyle="width:160px;"
      ></Column>
      <Column
        field="factoryCode"
        header="Mã phân xưởng"
        headerStyle="width:160px"
      ></Column>
      <Column
        field="sumMoney"
        header="Tổng giao dịch"
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
import DeliveryBillApi from "@/api/material-management/delivery-bill-api"; // eslint-disable-line import/no-cycle
import FactoryApi from "@/api/material-management/factory-api";
import WarehouseApi from "@/api/material-management/warehouse-api";
import DeliveryBillDetails from "@/views/material-management/delivery-bill/DeliveryBillDetails.vue";
import { useConfirm } from "primevue/useconfirm";
import { useToast } from "primevue/usetoast";
import { debounce } from "@/shared/utils";
import EmployeeApi from "@/api/employee-api";

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
    const arrFactory = ref([]);
    const confirm = useConfirm();
    const toast = useToast();
    let currentPage = 1;
    let emp = ref([]);
    let warehouse = ref([]);
    let factory = ref([]);
    let searchFormDate = ref("");
    let searchToDate = ref("");
    let searchName = ref("");
    let searchCode = ref("");
    let searchEmployee = ref("");
    let searchWarehouse = ref("");
    let searchFactory = ref("");
    const getData = async (
      page: number,
      requestedPageSize: number,
      deliveryBillId = "",
      searchCode = "",
      searchName = "",
      searchEmployee = "",
      searchWarehouse = "",
      searchFormDate = "",
      searchToDate = "",
      searchFactory = ""
    ) => {
      searchEmployee = searchEmployee === "null" ? "0" : searchEmployee;
      searchWarehouse = searchWarehouse === "null" ? "0" : searchWarehouse;
      searchFactory = searchFactory === "null" ? "0" : searchFactory;
      try {
        debugger;
        const resp = await DeliveryBillApi.getDeliveryBills(
          page,
          requestedPageSize,
          deliveryBillId,
          searchCode,
          searchName,
          searchEmployee,
          searchWarehouse,
          searchFormDate,
          searchToDate,
          searchFactory
        );
        //list.value = resp.data.list
        list.value = resp.data.list.map((v: Record<string, unknown>) => {
          const dt = new Date(v.dateDeliveryBill as string);
          const strDateDeliveryBill = new Intl.DateTimeFormat(["ban", "id"], {
            year: "numeric",
            month: "2-digit",
            day: "2-digit",
          }).format(dt);
          return {
            ...v,
            strDateDeliveryBill,
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
      debugger;
      confirm.require({
        message: `Do you want to remove ${rec.name} from product catalog ?`,
        header: "Remove",
        icon: "pi pi-question-circle",
        acceptIcon: "pi pi-check",
        accept: async () => {
          try {
            const resp = await DeliveryBillApi.deleteDeliveryBill(
              rec.deliveryBillId as string
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

    const onPageChange = (event: Record<string, unknown>) => {
      if (currentPage !== (event.page as number) + 1) {
        currentPage = (event.page as number) + 1;
        getData(
          currentPage,
          pageSize.value,
          "",
          `${searchName.value}`,
          `${searchCode.value}`,
          `${searchEmployee.value}`,
          `${searchWarehouse.value}`,
          `${searchFormDate.value.toString()}`,
          `${searchToDate.value.toString()}`,
          `${searchFactory.value}`
        );
      }
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
          `${searchFactory.value}`
        ),
      400
    );

    const onAddClick = async () => {
      debugger;
      const today = new Date().getTime();
      const res = await FactoryApi.getAll();
      let factoryItem: any;
      if (res.data.list) {
        factoryItem = res.data.list;
      }
      arrFactory.value = factoryItem;
      const resp = await WarehouseApi.getAll();
      let warehouseItem: any;
      if (resp.data.list) {
        warehouseItem = resp.data.list;
      }
      arrWarehouse.value = warehouseItem;
      const sequence = await DeliveryBillApi.getSequence();
      let sequenceId: any;
      if (sequence.data) {
        sequenceId = sequence.data;
      }
      await DeliveryBillApi.deleteByDeliveryBillId(sequenceId);
      isNewRec.value = true;
      selectedRec.value = {
        deliveryBillId: sequenceId,
        dateDeliveryBill: today,
      };
      showSlideOut.value = true;
    };

    const onDeleteClick = (rec: Record<string, unknown>) => {
      debugger;
      confirmDialog(rec);
    };

    const onEditClick = async (rec: Record<string, unknown>) => {
      const res = await FactoryApi.getAll();
      let factoryItem: any;
      if (res.data.list) {
        factoryItem = res.data.list;
      }
      arrFactory.value = factoryItem;
      const resp = await WarehouseApi.getAll();
      let warehouseItem: any;
      if (resp.data.list) {
        warehouseItem = resp.data.list;
      }
      arrWarehouse.value = warehouseItem;

      showSlideOut.value = true;
      selectedRec.value = rec;
    };

    onMounted(() => {
      getData(0, pageSize.value);
      lstEmp();
      lstWarehouse();
      lstfactory();
    });

    const lstEmp = async () => {
      debugger;
      const resp = await EmployeeApi.getAll();
      debugger;
      let lstEmps = [];
      if (resp.data) {
        lstEmps = resp.data.list;
      }
      emp.value = lstEmps;
    };

    const lstWarehouse = async () => {
      debugger;
      const resp = await WarehouseApi.getAll();
      debugger;
      let lstWarehouses = [];
      if (resp.data) {
        lstWarehouses = resp.data.list;
      }
      warehouse.value = lstWarehouses;
    };

    const lstfactory = async () => {
      debugger;
      const resp = await FactoryApi.getAll();
      debugger;
      let lstfactorys = [];
      if (resp.data) {
        lstfactorys = resp.data.list;
      }
      factory.value = lstfactorys;
    };

    return {
      list,
      arrWarehouse,
      arrFactory,
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
      factory,
      warehouse,
      searchCode,
      searchName,
      searchEmployee,
      searchWarehouse,
      searchFormDate,
      searchToDate,
      searchFactory,
      onSearchKeyup,
    };
  },
  components: {
    DeliveryBillDetails,
  },
});
</script>
