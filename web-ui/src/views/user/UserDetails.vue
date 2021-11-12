<template>
  <div class="m-font-regular">
    <BlockUI :blocked="isLoading" :fullScreen="true"></BlockUI>
 
    <Toast />
    <div>
      <span class="m-font-bold">USER ID: </span>
      <span style="color: var(--primary-color)">
        {{ recData.userId ? recData.userId : "New" }}</span
      >
      &nbsp;
    </div>
    <div class="p-mt-4"></div>
    <transition name="p-message">
      <Message v-if="showMessage" severity="info" @close="showMessage = false">
        {{ userMessage }}</Message
      >
    </transition>
    <div>
      <div class="p-mt-3">
        <label class="p-d-inline-block m-label-size-3 p-text-right p-mr-1"
          >Tên đăng nhập <strong class="p-error">*</strong>
        </label>
        <InputText
          type="text"
          v-model="recData.loginName"
          class="p-inputtext-sm p-col-4"
          v-if="!recData.userId"
        />
        <InputText
          type="text"
          v-model="recData.loginName"
          class="p-inputtext-sm p-col-4"
          v-if="recData.userId"
          disabled
        />
        <label class="p-d-inline-block m-label-size-3 p-text-right p-mr-1"
          >Nhân viên
        </label>
        <Dropdown
          style="width: 33.3333%"
          class="p-inputtext-sm"
          v-model="recData.employeeId"
          :options="emp"
          :filter="true"
          :showClear="true"
          optionLabel="fullName"
          optionValue="employeeId"
          placeholder="--Hãy chọn--"
          @change="getEmployeeById(recData.employeeId)"
        />
      </div>
      <div class="p-mt-3">
        <label class="p-d-inline-block m-label-size-3 p-text-right p-mr-1"
          >Tên đầy đủ <strong class="p-error">*</strong>
        </label>
        <InputText
          type="text"
          v-model="recData.fullName"
          class="p-inputtext-sm p-col-4"
        />
      </div>
      <div class="p-mt-3">
        <label class="p-d-inline-block m-label-size-3 p-text-right p-mr-1"
          >Email
        </label>
        <InputText
          type="text"
          v-model="recData.email"
          class="p-inputtext-sm p-col-4"
        />
        <label class="p-d-inline-block m-label-size-3 p-text-right p-mr-1"
          >Số điện thoại
        </label>
        <InputText
          type="text"
          v-model="recData.phone"
          class="p-inputtext-sm p-col-4"
        />
      </div>
    </div>
    <div class="p-mt-5 p-d-flex p-flex-row p-jc-end" style="width: 100%">
      <template v-if="changesApplied">
        <Button
          label="CLOSE"
          @click="$emit('cancel')"
          class="p-button-sm"
        ></Button>
      </template>
      <template v-else>
        <Button
          label="CANCEL"
          @click="$emit('cancel')"
          class="p-button-sm p-button-outlined p-mr-1"
        ></Button>
        <Button
          v-if="$store.getters.role === 'ADMIN'"
          icon="pi pi-check"
          iconPos="left"
          label="APPLY CHANGES"
          @click="onApplyChanges()"
          class="p-button-sm"
        ></Button>
      </template>
    </div>
  </div>
</template>

<script lang='ts'>
import { defineComponent, onMounted, ref } from "vue";
import { useRouter } from "vue-router";
import UsersApi from "@/api/users-api";
import EmployeeApi from "@/api/employee-api";
import RoleApi from "@/api/role-api";
import { useToast } from "primevue/usetoast";

export default defineComponent({
  props: {
    rec: { type: Object, required: true },
  },

  setup(props, { emit }): unknown {
    const showMessage = ref(false);
    const userMessage = ref("");
    const data = ref(props.rec);
    let recData = ref(props.rec);
    UsersApi.getById(data.value.userId).then((res) => {
      recData.value = res.data;
    });
    const emp = ref([]);
    const role = ref([]);
    const router = useRouter();
    const changesApplied = ref(false);
    const toast = useToast();

    const onApplyChanges = async () => {
      const rawUsersObj = JSON.parse(JSON.stringify(recData.value));
      let msg: any[];
      msg = [];
      if (!rawUsersObj.loginName) {
        msg.push("tên đăng nhập");
      }
      if (!rawUsersObj.fullName) {
        msg.push("tên đầy đủ");
      }
      if (msg.length > 0) {
        userMessage.value = "Trường " + msg.join(", ") + " không được để trống!";
        showMessage.value = true;
      } else {
        delete rawUsersObj.index;
        const check = await UsersApi.getUsersByLoginName(rawUsersObj);
        if (check.data) {
          userMessage.value = "Tên đăng nhập bị trùng. Vui lòng nhập lại!";
          showMessage.value = true;
        } else {
          let resp;
          if (rawUsersObj.userId) {
            resp = await UsersApi.updateUsers(rawUsersObj);
          } else {
            resp = await UsersApi.addUsers(rawUsersObj);
          }
          if (resp.data.msgType === "SUCCESS") {
            toast.add({
              severity: "success",
              summary: rawUsersObj.userId
                ? "Sửa thành công!"
                : "Thêm mới thành công!",
              detail: `${rawUsersObj.loginName} (${rawUsersObj.fullName})`,
              life: 3000,
            });
            if (!rawUsersObj.userId) {
              recData.value.id = "CREATED";
            }
            changesApplied.value = true;
            emit("changed");
            setTimeout(() => {
              onCancel();
            }, 500);
          } else {
            toast.add({
              severity: "error",
              summary: "Lỗi xảy ra!",
              detail: resp.data.msg,
            });
          }
        }
      }
    };

    const getEmployeeById = (employeeId: any) => {
      EmployeeApi.getEmployeeById(employeeId).then((res) => {
        recData.value.fullName = res.data.fullName;
        recData.value.email = res.data.email;
        recData.value.phone = res.data.phone;
      });
    };

    onMounted(() => {
      lstEmp();
    });

    const lstEmp = async () => {
      const resp = await EmployeeApi.getAll();
      let lstEmps = [];
      if (resp.data) {
        lstEmps = resp.data.list;
      }
      emp.value = lstEmps;

      const resps = await RoleApi.getAll();
      let lstRole = [];
      if (resps.data) {
        lstRole = resps.data.list;
      }
      role.value = lstRole;
    };

    const onCancel = () => {
      emit("cancel");
    };

    return {
      router,
      showMessage,
      userMessage,
      recData,
      onCancel,
      onApplyChanges,
      changesApplied,
      emp,
      role,
      getEmployeeById,
    };
  },
});
</script>
