<template>
  <div>
    <ConfirmDialog position="top"></ConfirmDialog>
    <Toast />
    <Sidebar
      v-model:visible="showSlideOut"
      position="right"
      style="width: 1000px"
    >
      <FactoryDetails
        :rec="selectedRec"
        @cancel="showSlideOut = false"
        @changed="getData()"
        :arr="arr"
        :itemById="itemById"
        :isNew="isNewRec"
      ></FactoryDetails>
    </Sidebar>
    <h3>Quản lý phân xưởng</h3>
    <div class="p-d-flex p-flex-row p-mb-3 p-jc-around" style="width: 1350px">
      <div>
        <label
          class="p-d-inline-block m-label-size-3 p-text-left p-mr-1"
          style="padding-top: 7px"
          >Mã PX
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
          >Tên PX
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
          >Email
        </label>
        <InputText
          type="text"
          v-model="searchEmail"
          class="p-inputtext-sm"
          style="width: 200px; height: 30px; margin: 1px 0px 0 0px"
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
        />
      </div>
    </div>
    <div class="p-d-flex p-flex-row p-mb-3 p-jc-around" style="width: 1350px">
      <div>
        <label
          class="p-d-inline-block m-label-size-3 p-text-left p-mr-1"
          style="padding-top: 7px"
          >Thi công từ
        </label>
        <InputText
          type="date"
          v-model="searchFormDate"
          class="p-inputtext-sm"
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
          style="width: 200px; height: 30px"
        />
      </div>
      <div>
        <label
          class="p-d-inline-block m-label-size-3 p-text-left p-mr-1"
          style="padding-top: 7px"
          >Hoàn thành từ
        </label>
        <InputText
          type="date"
          v-model="searchFormSuccessDate"
          class="p-inputtext-sm"
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
          v-model="searchToSuccessDate"
          class="p-inputtext-sm"
          style="width: 200px; height: 30px"
        />
      </div>
    </div>
    <div
      class="p-d-flex p-flex-row p-mb-3 p-jc-center"
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
        field="factoryId"
        header="ID phân xưởng"
        headerStyle="width:90px;"
      ></Column>
      <Column
        field="code"
        header="Mã phân xưởng"
        headerStyle="width:90px"
      ></Column>
      <Column
        field="name"
        header="Tên phân xưởng"
        headerStyle="width:160px"
      ></Column>
      <Column field="email" header="Email" headerStyle="width:160px"></Column>
      <!--      <Column field="address" header="Địa chỉ" headerStyle="width:160px"></Column>-->
      <Column
        field="employeeName"
        header="Người phụ trách"
        headerStyle="width:160px"
      ></Column>
      <Column
        field="strDateConstruction"
        header="Ngày thi công"
        headerStyle="width:160px"
      ></Column>
      <Column
        field="strDateFinish"
        header="Ngày hoàn thành"
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
import FactoryApi from "@/api/material-management/factory-api"; // eslint-disable-line import/no-cycle
import FactoryDetails from "@/views/material-management/factory/FactoryDetails.vue";
import { useConfirm } from "primevue/useconfirm";
import { useToast } from "primevue/usetoast";
import EmployeeApi from "@/api/employee-api";
import { debounce } from "@/shared/utils";

export default defineComponent({
  setup(): unknown {
    const isLoading = ref(false);
    const showSlideOut = ref(false);
    const pageSize = ref(10);
    const totalPages = ref(0);
    const totalRecs = ref(0);
    const selectedRec = ref({});
    const itemById = ref({});
    const isNewRec = ref(false);
    const isCustomer = ref(false);
    const list = ref([]);
    const arr = ref([]);
    const confirm = useConfirm();
    const toast = useToast();
    let currentPage = 1;
    let emp = ref([]);
    let searchFormSuccessDate = ref("");
    let searchToSuccessDate = ref("");
    let searchFormDate = ref("");
    let searchToDate = ref("");
    let searchName = ref("");
    let searchCode = ref("");
    let searchEmail = ref("");
    let searchEmployee = ref("");

    const getData = async (
      page: number,
      requestedPageSize: number,
      factoryId = "",
      searchCode = "",
      searchName = "",
      searchEmail = "",
      searchEmployee = "",
      searchFormDate = "",
      searchToDate = "",
      searchFormSuccessDate = "",
      searchToSuccessDate = ""
    ) => {
      searchEmployee = searchEmployee === "null" ? "0" : searchEmployee;
      // isLoading.value = true;
      try {
        const resp = await FactoryApi.getFactorys(
          page,
          requestedPageSize,
          factoryId,
          searchCode,
          searchName,
          searchEmail,
          searchEmployee,
          searchFormDate,
          searchToDate,
          searchFormSuccessDate,
          searchToSuccessDate
        );
        let i = 1;
        list.value = resp.data.list.map((v: Record<string, unknown>) => {
          const dt1 = new Date(v.dateConstruction as string);
          const dt2 = new Date(v.dateFinish as string);
          const strDateConstruction = new Intl.DateTimeFormat(["ban", "id"], {
            year: "numeric",
            month: "2-digit",
            day: "2-digit",
          }).format(dt1);
          const strDateFinish = new Intl.DateTimeFormat(["ban", "id"], {
            year: "numeric",
            month: "2-digit",
            day: "2-digit",
          }).format(dt2);
          let index = 1;
          if (page > 1) {
            index = 10 * (currentPage - 1) + i++;
          } else {
            index = i++;
          }
          return {
            ...v,
            strDateConstruction,
            strDateFinish,
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
        message: `Do you want to remove ${rec.name} from product catalog ?`,
        header: "Remove",
        icon: "pi pi-question-circle",
        acceptIcon: "pi pi-check",
        accept: async () => {
          try {
            const resp = await FactoryApi.deleteFactory(
              rec.factoryId as string
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
        getData(currentPage, pageSize.value);
      }
    };

    const onAddClick = async () => {
      const resp = await EmployeeApi.getAll();
      let arrEmp: any;
      if (resp.data) {
        arrEmp = resp.data.list;
      }
      arr.value = arrEmp;
      isNewRec.value = true;
      selectedRec.value = { factoryId: "" };
      showSlideOut.value = true;
    };

    const onDeleteClick = (rec: Record<string, unknown>) => {
      confirmDialog(rec);
    };

    const onEditClick = async (rec: Record<string, unknown>) => {
      const resp = await EmployeeApi.getAll();
      let arrEmp: any;
      if (resp.data) {
        arrEmp = resp.data.list;
      }
      arr.value = arrEmp;
      const recData = ref(JSON.parse(JSON.stringify(rec)));
      const item = await FactoryApi.getById(recData.value.factoryId);
      let factory: any;
      if (item.data) {
        factory = item.data;
      }
      showSlideOut.value = true;
      selectedRec.value = rec;
      itemById.value = factory;
    };

    onMounted(() => {
      getData(0, pageSize.value);
      lstEmp();
    });

    const lstEmp = async () => {
      const resp = await EmployeeApi.getAll();
      let lstEmps = [];
      if (resp.data) {
        lstEmps = resp.data.list;
      }
      emp.value = lstEmps;
    };

    const onSearchKeyup = debounce(async () => {
      let msg = [];
      if (
        Date.parse(`${searchFormDate.value}`) >
        Date.parse(`${searchToDate.value}`)
      ) {
        msg.push("thi công từ ngày phải nhỏ hơn thi công đến ngày");
      }
      if (
        Date.parse(`${searchFormSuccessDate.value}`) >
        Date.parse(`${searchToSuccessDate.value}`)
      ) {
        msg.push("hoàn thành từ ngày phải nhỏ hơn hoàn thành đến ngày");
      }
      if (msg.length > 0) {
        toast.add({
          severity: "warn",
          summary: "Cảnh báo",
          detail: "Trường " + msg.join(", "),
          life: 3000,
        });
      } else {
        await getData(
          1,
          pageSize.value,
          "",
          `${searchCode.value}`,
          `${searchName.value}`,
          `${searchEmail.value}`,
          `${searchEmployee.value}`,
          `${searchFormDate.value}`,
          `${searchToDate.value}`,
          `${searchFormSuccessDate.value}`,
          `${searchToSuccessDate.value}`
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
      arr,
      itemById,
      emp,
      searchFormSuccessDate,
      searchToSuccessDate,
      searchFormDate,
      searchToDate,
      searchName,
      searchCode,
      searchEmail,
      searchEmployee,
      onSearchKeyup,
    };
  },
  components: {
    FactoryDetails,
  },
});
</script>
