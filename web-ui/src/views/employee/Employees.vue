<template>
  <div>
    <ConfirmDialog position="top"></ConfirmDialog>
    <Toast />
    <Sidebar
      v-model:visible="showSlideOut"
      position="right"
      style="width: 1000px"
    >
      <EmployeeDetails
        :rec="selectedRec"
        @cancel="showSlideOut = false"
        @changed="getData()"
        :isNew="isNewRec"
      >
      </EmployeeDetails>
    </Sidebar>
    <Dialog v-model:visible="showDialog" style="width: 1000px; height: 650px">
      <template #header>
        <h3>Import nhân viên</h3>
      </template>
      <Import @cancel="showDialog = false" @changed="getData()"> </Import>
    </Dialog>
    <h3>Quản lý nhân viên</h3>
    <div class="p-d-flex p-flex-row p-mb-3 p-jc-around" style="width: 1150px">
      <div>
        <label
          class="p-d-inline-block m-label-size-3 p-text-left p-mr-1"
          style="padding-top: 7px"
          >Mã nhân viên
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
          >Tên nhân viên
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
    </div>
    <div class="p-d-flex p-flex-row p-mb-3 p-jc-around" style="width: 1150px">
      <div>
        <label
          class="p-d-inline-block m-label-size-3 p-text-left p-mr-1"
          style="padding-top: 7px"
          >Phone
        </label>
        <InputText
          type="text"
          v-model="searchPhone"
          class="p-inputtext-sm"
          style="width: 200px; height: 30px; margin: 1px 0px 0 0px"
        />
      </div>
      <div>
        <label
          class="p-d-inline-block m-label-size-3 p-text-left p-mr-1"
          style="padding-top: 7px"
          >Phòng ban
        </label>
        <Dropdown
          class="p-inputtext-sm"
          style="width: 200px"
          v-model="searchDepartment"
          :options="department"
          :filter="true"
          :showClear="true"
          optionLabel="name"
          optionValue="departmentId"
          placeholder="--Hãy chọn--"
        />
      </div>
      <div>
        <label
          class="p-d-inline-block m-label-size-3 p-text-left p-mr-1"
          style="padding-top: 7px"
          >Chức vụ
        </label>
        <Dropdown
          class="p-inputtext-sm"
          style="width: 200px"
          v-model="searchPosition"
          :options="position"
          :filter="true"
          :showClear="true"
          optionLabel="name"
          optionValue="positionId"
          placeholder="--Hãy chọn--"
        />
      </div>
    </div>
    <div
      class="p-d-flex p-flex-row p-mb-3 p-jc-center"
      style="width: 1150px; margin: 20px 0"
    >
      <Button
        icon="pi pi-upload"
        iconPos="right"
        label="Import"
        @click="showImport()"
        class="p-ml-1 p-button-sm"
      ></Button>
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
      stripedRows
      showGridlines
      @page="onPageChange($event)"
      class="p-datatable-sm p-datatable-hoverable-rows m-border p-mb-4"
      style="width: 1250px"
    >
      <Column
        field="index"
        header="STT"
        headerStyle="width:50px;"
        bodyStyle="text-align-last: center;"
      ></Column>
      <Column
        field="code"
        header="Mã nhân viên"
        headerStyle="width:120px;"
        bodyStyle="text-align-last: center;"
      ></Column>
      <Column field="fullName" header="Họ và tên"></Column>
      <Column
        field="strBitrh"
        header="Ngày sinh"
        bodyStyle="text-align-last: center;"
      ></Column>
      <Column field="sexString" header="Giới tính" headerStyle="width:90px;" bodyStyle="text-align-last: center;"></Column>
      <Column field="phone" header="Điện thoại" headerStyle="width:150px;" bodyStyle="text-align-last: center;"></Column>
      <Column field="email" header="EMAIL" headerStyle="width:210px"></Column>
      <Column
        field="departmentName"
        header="Phòng ban"
        headerStyle="width:160px"
      ></Column>
      <Column
        field="positionName"
        header="Chức vụ"
        headerStyle="width:160px"
      ></Column>
      <Column
        header="ACTION"
        headerStyle="width:100px"
        bodyStyle="padding:3px; text-align: center;"
      >
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
import EmployeeDetails from "@/views/employee/EmployeeDetails.vue";
import Import from "@/views/employee/Import.vue";
import EmployeeApi from "@/api/employee-api"; // eslint-disable-line import/no-cycle
import { useConfirm } from "primevue/useconfirm";
import { useToast } from "primevue/usetoast";
import { debounce, exportFile } from "@/shared/utils";
import positionApi from "@/api/position-api";
import departmentApi from "@/api/department-api";
import employeeApi from "@/api/employee-api";

export default defineComponent({
  setup(): unknown {
    const isLoading = ref(false);
    const showSlideOut = ref(false);
    const showDialog = ref(false);
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
    const position = ref([]);
    const department = ref([]);
    const searchName = ref("");
    const searchCode = ref("");
    const searchEmail = ref("");
    const searchPhone = ref("");
    const searchDepartment = ref("");
    const searchPosition = ref("");

    const getData = async (
      page: number,
      requestedPageSize: number,
      employeeId = "",
      searchCode = "",
      searchName = "",
      searchEmail = "",
      searchPhone = "",
      searchDepartment = "",
      searchPosition = ""
    ) => {
      // isLoading.value = true;
      searchDepartment = searchDepartment === "null" ? "0" : searchDepartment;
      searchPosition = searchPosition === "null" ? "0" : searchPosition;
      try {
        const resp = await EmployeeApi.getEmployees(
          page,
          requestedPageSize,
          employeeId,
          searchCode,
          searchName,
          searchEmail,
          searchPhone,
          searchDepartment,
          searchPosition
        );
        let i = 1;
        list.value = resp.data.list.map((v: Record<string, unknown>) => {
          let index = 1;
          if (page > 1) {
            index = 10 * (currentPage - 1) + i++;
          } else {
            index = i++;
          }
          let strBitrh = "";
          if (v.birth) {
            const date = new Date(v.birth as string);
            strBitrh = new Intl.DateTimeFormat(["ban", "id"], {
              year: "numeric",
              month: "2-digit",
              day: "2-digit",
            }).format(date);
          }

          return {
            ...v,
            index,
            strBitrh,
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
            const resp = await EmployeeApi.deleteEmployee(
              rec.employeeId as string
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
          `${searchCode.value}`,
          `${searchName.value}`,
          `${searchEmail.value}`,
          `${searchPhone.value}`,
          `${searchDepartment.value}`,
          `${searchPosition.value}`
        );
      }
    };

    const onSearchKeyup = debounce(
      () =>
        getData(
          1,
          pageSize.value,
          "",
          `${searchCode.value}`,
          `${searchName.value}`,
          `${searchEmail.value}`,
          `${searchPhone.value}`,
          `${searchDepartment.value}`,
          `${searchPosition.value}`
        ),
      400
    );

    const onAddClick = () => {
      isNewRec.value = true;
      selectedRec.value = { employeeId: "" };
      showSlideOut.value = true;
    };

    const onDeleteClick = (rec: Record<string, unknown>) => {
      confirmDialog(rec);
    };

    const onEditClick = async (rec: Record<string, unknown>) => {
      showSlideOut.value = true;
      selectedRec.value = rec;
    };

    onMounted(async () => {
      getData(1, pageSize.value);
      await lstPosition();
      await lstDepartment();
    });

    const lstPosition = async () => {
      const resp = await positionApi.getAll();
      let lstPositions = [];
      if (resp.data) {
        lstPositions = resp.data.list;
      }
      position.value = lstPositions;
    };

    const lstDepartment = async () => {
      const resp = await departmentApi.getAll();
      let lstDepartments = [];
      if (resp.data) {
        lstDepartments = resp.data.list;
      }
      department.value = lstDepartments;
    };

    const exportExcell = async () => {
      await employeeApi
        .export(
          `${searchCode.value}`,
          `${searchName.value}`,
          `${searchEmail.value}`,
          `${searchPhone.value}`,
          `${searchDepartment.value}`,
          `${searchPosition.value}`
        )
        .then((res) => {
          const data = res.data.data;
          exportFile(data.data, data.fileName);
        });
    };

    const showImport = () => {
      showDialog.value = true;
    };

    return {
      list,
      isLoading,
      showSlideOut,
      showDialog,
      pageSize,
      totalPages,
      totalRecs,
      selectedRec,
      isNewRec,
      isCustomer,
      onSearchKeyup,
      onAddClick,
      onDeleteClick,
      onEditClick,
      onPageChange,
      getData,
      position,
      department,
      searchName,
      searchCode,
      searchEmail,
      searchPhone,
      searchDepartment,
      searchPosition,
      exportExcell,
      showImport,
    };
  },
  components: {
    EmployeeDetails,
    Import,
  },
});
</script>
