<template>
  <div>
    <ConfirmDialog position="top"></ConfirmDialog>
    <Toast />
    <Sidebar
      v-model:visible="showSlideOut"
      position="right"
      style="width: 1000px"
    >
      <UserDetails
        :rec="selectedRec"
        @cancel="showSlideOut = false"
        @changed="getData()"
        :isNew="isNewRec"
      >
      </UserDetails>
    </Sidebar>
    <h3>Manage Employees</h3>
    <div class="p-d-flex p-flex-row p-mb-3 p-jc-around" style="width: 1150px">
      <div>
        <label
          class="p-d-inline-block m-label-size-3 p-text-left p-mr-1"
          style="padding-top: 7px"
          >Tên đăng nhập
        </label>
        <InputText
          type="text"
          v-model="searchLoginName"
          class="p-inputtext-sm"
          style="width: 200px; height: 30px; margin: 1px 0px 0 0px"
        />
      </div>
      <div>
        <label
          class="p-d-inline-block m-label-size-3 p-text-left p-mr-1"
          style="padding-top: 7px"
          >Tên đầy đủ
        </label>
        <InputText
          type="text"
          v-model="searchFullName"
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
          >Quyền
        </label>
        <Dropdown
          class="p-inputtext-sm"
          style="width: 200px"
          v-model="searchRole"
          :options="role"
          :filter="true"
          :showClear="true"
          optionLabel="name"
          optionValue="code"
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
          v-model="searchEmployeeId"
          :options="emp"
          :filter="true"
          :showClear="true"
          optionLabel="fullName"
          optionValue="employeeId"
        />
      </div>
    </div>
    <div
      class="p-d-flex p-flex-row p-mb-3 p-jc-center"
      style="width: 1150px; margin: 20px 0"
    >
      <Button
        icon="pi pi-user"
        iconPos="right"
        label="Dowload Template"
        @click="dowloadTemplate()"
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
      style="width: 1250px"
    >
      <Column
        field="index"
        header="Stt"
        headerStyle="max-width: 70px; padding-left: 20px;"
        bodyStyle="max-width: 70px; padding-left: 20px; "
      ></Column>
      <Column
        field="loginName"
        header="Tên đăng nhập"
        headerStyle="min-width:110px;"
        bodyStyle="min-width:110px;"
      ></Column>
      <Column
        field="role"
        header="Quyền"
        headerStyle="min-width:110px"
        bodyStyle="min-width:110px;"
      ></Column>
      <Column
        field="email"
        header="EMAIL"
        headerStyle="min-width:260px"
        bodyStyle="min-width:260px;"
      ></Column>
      <Column
        field="fullName"
        header="Tên đầy đủ"
        headerStyle="min-width:110px"
        bodyStyle="min-width:110px;"
      ></Column>
      <Column
        field="employeeCode"
        header="Mã nhân viên"
        headerStyle="min-width:110px"
        bodyStyle="min-width:110px;"
      ></Column>
      <Column
        header="ACTION"
        headerStyle="min-width:100px"
        bodyStyle="padding:3px; min-width:100px"
      >
        <template #body="slotProps">
          <template
            v-if="
              $store.getters.role === 'ADMIN' ||
              ($store.getters.role == 'SUPPORT' &&
                slotProps.data.role !== 'ADMIN')
            "
          >
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
            <Button
              icon="pi pi-undo"
              @click="onResetPasswordClick(slotProps.data)"
              style="color: darkblue"
              class="p-button-sm p-button-rounded p-button-danger p-button-text"
            />
          </template>
        </template>
      </Column>
    </DataTable>
  </div>
</template>

<script lang='ts'>
import { ref, onMounted, defineComponent } from "vue";
import UserDetails from "@/views/material-management/user/UserDetails.vue";
import UsersApi from "@/api/users-api"; // eslint-disable-line import/no-cycle
import { useConfirm } from "primevue/useconfirm";
import { useToast } from "primevue/usetoast";
import { debounce } from "@/shared/utils";
import EmployeeApi from "@/api/employee-api"; // eslint-disable-line import/no-cycle
import RoleApi from "@/api/material-management/role-api"; // eslint-disable-line import/no-cycle

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
    const emp = ref([]);
    const role = ref([]);
    const confirm = useConfirm();
    const toast = useToast();
    let currentPage = 1;
    const userId = ref("");
    const searchLoginName = ref("");
    const searchFullName = ref("");
    const searchEmail = ref("");
    const searchPhone = ref("");
    const searchRole = ref("");
    const searchEmployeeId = ref("");

    const getData = async (
      page: number,
      requestedPageSize: number,
      userId = "",
      searchFullName = "",
      searchLoginName = "",
      searchEmail = "",
      searchPhone = "",
      searchRole = "",
      searchEmployeeId = ""
    ) => {
      // isLoading.value = true;
      searchEmployeeId = searchEmployeeId === "null" ? "0" : searchEmployeeId;
      searchRole = searchRole === "null" ? "" : searchRole;
      try {
        const resp = await UsersApi.getUsers(
          page,
          requestedPageSize,
          userId,
          searchLoginName,
          searchFullName,
          searchEmail,
          searchPhone,
          searchRole,
          searchEmployeeId
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
        message: `Do you want to delete: ${rec.loginName} ?`,
        header: "Delete Confirmation",
        icon: "pi pi-question-circle",
        acceptIcon: "pi pi-check",
        accept: async () => {
          try {
            const resp = await UsersApi.deleteUser(rec.loginName as string);
            if (resp.data.msgType === "SUCCESS") {
              getData(currentPage, pageSize.value);
              toast.add({
                severity: "success",
                summary: "Xóa user thành công",
                life: 3000,
              });
            } else {
              toast.add({
                severity: "error",
                summary: "Thao tác thất bại!",
                detail: resp.data.msg,
                life: 3000,
              });
            }
          } catch (e) {
            toast.add({
              severity: "error",
              summary: "Error",
              detail: "Lỗi xảy ra vui lòng liên hệ với quản trị viên!!",
              life: 3000,
            });
          }
        },
        reject: () => {
          console.log("NO");
        },
      });
    };

    const onDeleteClick = (rec: Record<string, unknown>) => {
      confirmDialog(rec);
    };

    const onResetPasswordClick = async (rec: Record<string, unknown>) => {
      confirmResetPassword(rec);
    };

    const confirmResetPassword = (rec: Record<string, unknown>) => {
      confirm.require({
        message: `Bạn có chắc muốn reset mật khẩu: ${rec.loginName} ?`,
        header: "Reset mật khẩu",
        icon: "pi pi-question-circle",
        acceptIcon: "pi pi-check",
        accept: async () => {
          try {
            const resp = await UsersApi.resetPasswordUser(rec.loginName as string);
            if (resp.data.msgType === "SUCCESS") {
              getData(currentPage, pageSize.value);
              toast.add({
                severity: "success",
                summary:
                  "Bạn đã reset mật khẩu thành công! Mật khẩu mặc định là 123456a@A",
                life: 3000,
              });
            } else {
              toast.add({
                severity: "error",
                summary: "Reset mật khẩu thất bại",
                detail: resp.data.msg,
                life: 3000,
              });
            }
          } catch (e) {
            toast.add({
              severity: "error",
              summary: "Error",
              detail: "Lỗi xảy ra vui lòng liên hệ với quản trị viên!!",
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
          `${searchFullName.value}`,
          `${searchLoginName.value}`,
          `${searchEmail.value}`,
          `${searchPhone.value}`,
          `${searchRole.value}`,
          `${searchEmployeeId.value}`
        );
      }
    };

    const onSearchKeyup = debounce(
      () =>
        getData(
          1,
          pageSize.value,
          "",
          `${searchFullName.value}`,
          `${searchLoginName.value}`,
          `${searchEmail.value}`,
          `${searchPhone.value}`,
          `${searchRole.value}`,
          `${searchEmployeeId.value}`
        ),
      400
    );

    const onAddClick = () => {
      isNewRec.value = true;
      selectedRec.value = { employeeId: "" };
      showSlideOut.value = true;
    };


    const onEditClick = async (rec: Record<string, unknown>) => {
      showSlideOut.value = true;
      selectedRec.value = rec;
    };

    onMounted(async () => {
      getData(1, pageSize.value);
      lstEmp();
      lstRole();
    });

  const lstEmp = async () => {
      const resp = await EmployeeApi.getAll();
      let lstEmps = [];
      if (resp.data) {
        lstEmps = resp.data.list;
      }
      emp.value = lstEmps;
    };

    const lstRole = async () => {
      const resp = await RoleApi.getAll();
      let lstRoles = [];
      if (resp.data) {
        lstRoles = resp.data.list;
      }
      role.value = lstRoles;
    };

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
      onSearchKeyup,
      onAddClick,
      onDeleteClick,
      onEditClick,
      onPageChange,
      getData,
      userId,
      searchLoginName,
      searchFullName,
      searchEmail,
      searchPhone,
      searchRole,
      searchEmployeeId,
      onResetPasswordClick,
      emp,
      role
    };
  },
  components: {
    UserDetails,
  },
});
</script>
<style lang="scss">
@import "~@/assets/styles/_vars.scss";

.sw-text {
  line-height: 24px;
}

.sw-slideout-head {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  display: flex;
  height: 60px;
  padding: 16px;
  box-shadow: 0px 0px 8px 2px #ccc;
  background-color: #fff;
  z-index: 1;
}

.sw-slideout-body {
  margin-top: 92px;
}
</style>