<template>
  <div>
    <ConfirmDialog position="top"></ConfirmDialog>
    <Toast />
    <Sidebar
      v-model:visible="showSlideOut"
      position="right"
      style="width: 700px"
    >
      <UserDetails
        :rec="selectedRec"
        :isCustomer="isCustomer"
        :isNew="isNewRec"
        @cancel="showSlideOut = false"
        @changed="getData()"
      ></UserDetails>
    </Sidebar>
    <h3>Manage Users</h3>
    <div class="p-d-flex p-flex-row p-mb-3" style="width: 1300px">
        <InputText
          type="text"
          v-model="searchCode"
          class="p-inputtext-sm"
          style="width: 180px; margin: 1px 10px 0 10px"
        />
        <InputText
          type="text"
          v-model="searchName"
          class="p-inputtext-sm"
          style="width: 180px; margin: 1px 10px 0 10px"
        />
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

      <div style="display: inline-block; flex: 1"></div>
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
      :scrollable="true"
      scrollHeight="500px"
      :loading="isLoading"
      class="p-datatable-sm p-datatable-hoverable-rows m-border"
      style="width: 1300px"
    >
      <Column
        field="index"
        header="Stt"
        headerStyle="max-width: 70px; padding-left: 20px;"
        bodyStyle="max-width: 70px; padding-left: 20px; "
      ></Column>
      <Column
        field="userId"
        header="USER ID"
        :sortable="true"
        headerStyle="min-width:110px;"
        bodyStyle="min-width:110px;"
      ></Column>
      <Column
        field="role"
        header="ROLE"
        :sortable="true"
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
        field="employeeId"
        header="EMPLOYEE ID"
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
        field="fullName"
        header="Tên nhân viên"
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
import CustomerApi from "@/api/customer-api"; // eslint-disable-line import/no-cycle
import EmployeeApi from "@/api/employee-api"; // eslint-disable-line import/no-cycle
import { useConfirm } from "primevue/useconfirm";
import { useToast } from "primevue/usetoast";

export default defineComponent({
  setup(): unknown {
    const isLoading = ref(false);
    const showSlideOut = ref(false);
    const selectedRec = ref({});
    const isNewRec = ref(false);
    const isCustomer = ref(false);
    const searchEmployee = ref("");
    const searchId = ref("");
    const list = ref([]);
    let emp = ref([]);
    const confirm = useConfirm();
    const toast = useToast();

    const getData = async () => {
      console.log("Loaded Data");
      isLoading.value = true;
      try {
        let index = 0;
        const resp = await UsersApi.getUsers(0, 1000);
        isLoading.value = false;
        list.value = resp.data.list.map((v: Record<string, unknown>) => {
          index++;
          return {
            ...v,
            index,
          };
        });
      } catch (err) {
        console.log("REST ERROR: %O", err.response ? err.response : err);
        isLoading.value = false;
      }
    };

    const confirmDialog = (rec: Record<string, unknown>) => {
      confirm.require({
        message: `Do you want to delete: ${rec.userId} ?`,
        header: "Delete Confirmation",
        icon: "pi pi-question-circle",
        acceptIcon: "pi pi-check",
        accept: async () => {
          try {
            const resp = await UsersApi.deleteUser(rec.userId as string);
            if (resp.data.msgType === "SUCCESS") {
              getData();
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

    const onAddClick = () => {
      isNewRec.value = true;
      selectedRec.value = { userId: "" };
      showSlideOut.value = true;
    };

    const onDeleteClick = (rec: Record<string, unknown>) => {
      confirmDialog(rec);
    };

    // const onEditClick = async (rec: Record<string, unknown>) => {
    //   debugger
    //   let resp;
    //   try {
    //     if (rec.role === 'SUPPORT' || rec.role === 'ADMIN') {
    //       resp = await EmployeeApi.getEmployees(1, 1, rec.employeeId as string);
    //     }
    //     if (resp?.data.msgType === 'SUCCESS') {
    //       selectedRec.value = { ...rec, ...resp.data.list[0] };
    //       isNewRec.value = false;
    //       showSlideOut.value = true;
    //     }
    //   } catch (e) {
    //     toast.add({
    //       severity: 'error',
    //       summary: 'Error',
    //       detail: 'Unable to connect to server',
    //       life: 3000
    //     });
    //   }
    // };

    const onEditClick = async (rec: Record<string, unknown>) => {
      debugger;
      showSlideOut.value = true;
      selectedRec.value = rec;
    };

    const onResetPasswordClick = async (rec: Record<string, unknown>) => {
      confirmResetPassword(rec);
    };

    const confirmResetPassword = (rec: Record<string, unknown>) => {
      confirm.require({
        message: `Bạn có chắc muốn reset mật khẩu: ${rec.userId} ?`,
        header: "Reset mật khẩu",
        icon: "pi pi-question-circle",
        acceptIcon: "pi pi-check",
        accept: async () => {
          try {
            const resp = await UsersApi.resetPasswordUser(rec.userId as string);
            if (resp.data.msgType === "SUCCESS") {
              getData();
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

    onMounted(() => {
      getData();
      lstEmp();
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

    return {
      list,
      isLoading,
      showSlideOut,
      selectedRec,
      isNewRec,
      isCustomer,
      onAddClick,
      onDeleteClick,
      onEditClick,
      getData,
      searchEmployee,
      searchId,
      emp,
      onResetPasswordClick,
    };
  },
  components: {
    UserDetails,
  },
});

/** import Rest from '@/rest/Rest';
 import store from '@/store';
 import VueSlideoutPanel from 'vue-slideout-panel/src/VueSlideoutPanel'
 import UserDetails from '@/views/UserDetails'
 export default {
  data:function(){
    return {
      loading:false,
      showSlideOut:false,
      tableData:[],
      selectedRec:{},
      isNew:false,
    }
  },
  methods:{
    getData(){
      let me = this;
      console.log("Loaded Data");
      me.$data.loading=true;
      Rest.getUsers(0,1000).then(function(resp){
        me.$data.tableData = resp.data.list;
        me.$data.loading=false;
      })
      .catch(function(err){
        console.log("REST ERROR: %O", err.response?err.response:err);
        me.$data.loading=false;
      });
    },
    onDelete(rec){
      let me = this;
      me.$confirm('The action will remove <ul><li> All orders placed by the customer</li><li> Associated customer details </li> <li> Shopping Cart items by this user </li> </ul>  ', 'Confirm', {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        type: 'warning',
        dangerouslyUseHTMLString:true
      }).then(() => {
        return Rest.deleteUser(rec.userId);
      }).then((resp) => {
        if (resp.data.msgType==="SUCCESS"){
          me.$message({message: 'Successfully deleted', type:'success'});
          me.getData()
        }
        else{
          me.$message({message: 'Unable to delete, this could be due to the product being reffered in existing orders', type:'error', showClose:true, duration:6000});
        }
      })
      .catch((resp) => {
        me.$message({type:'info',message: 'Delete canceled'});
      });
    },
    onEdit(rec){
      let me = this;
      let methodName = "";
      let id ="";
      if (rec.role==="CUSTOMER"){
        methodName="getCustomers"
        id = rec.customerId;
      }
      else if (rec.role==="SUPPORT" || rec.role==="ADMIN"){
        methodName="getEmployees";
        id = rec.employeeId;
      }
      if (!id){
        return;
      }
      Rest[methodName](1,1,id).then(function(resp){
        if (resp.data.msgType==="SUCCESS" && resp.data.list.length === 1){
          me.$data.selectedRec  = {...rec, ...resp.data.list[0]};
          me.$data.isNew = false;
          me.$data.showSlideOut = true;
        }
      })
      })
      .catch(function(err){
        me.$message({ message:'Unable to retrieve selected data', type:'error', showClose:true, duration:6000});
        console.log("REST ERROR: %O", err.response?err.response:err);
      });
    },
    onOpenAddProduct(){
      this.$data.isNew = true;
      this.$data.selectedRec  = {userId:'NEW'};
      this.$data.showSlideOut = true;
    },
    onContinueShopping(){
      console.log("Continue Shopping clicked...")
    },
  },
 mounted(){
    this.getData();
  },
 components: {
    UserDetails,
    VueSlideoutPanel
  },
 }
 */
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
