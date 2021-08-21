<template>
  <div class="m-font-regular">
    <Toast />
    <h4>
      Nhân viên #
      <span style="color: var(--primary-color)">
        {{ recData.employeeId ? recData.employeeId : "NEW" }}
      </span>
    </h4>
    <transition name="p-message">
      <Message v-if="showMessage" severity="error" @close="showMessage = false">
        {{ userMessage }}</Message
      >
    </transition>
    <div>
      <div class="p-mt-3">
        <label class="p-d-inline-block m-label-size-2 p-text-right p-mr-1"
          >Mã nhân viên <strong class="p-error">*</strong>
        </label>
        <InputText
          type="text"
          v-model="recData.code"
          class="p-inputtext-sm p-col-4"
        />
      </div>
      <div class="p-mt-3">
        <label class="p-d-inline-block m-label-size-2 p-text-right p-mr-1"
          >Họ và đệm <strong class="p-error">*</strong>
        </label>
        <InputText
          type="text"
          v-model="recData.firstName"
          class="p-inputtext-sm p-col-4"
        />
        <label class="p-d-inline-block m-label-size-2 p-text-right p-mr-1"
          >Tên <strong class="p-error">*</strong>
        </label>
        <InputText
          type="text"
          v-model="recData.lastName"
          class="p-inputtext-sm p-col-4"
        />
      </div>
      <div class="p-mt-3 p-d-flex p-ai-center">
        <label class="p-d-inline-block m-label-size-2 p-text-right p-mr-1">
          Ngày sinh <strong class="p-error">*</strong>
        </label>
        <Datepicker
          class="p-inputtext-sm p-mr-1"
          v-model="recData.birth"
          inputFormat="dd/MM/yyy"
          style="height: 1.8rem; width: 323px"
        />
        <label class="p-d-inline-block m-label-size-2 p-text-right p-pr-1">
          Giới tính <strong class="p-error">*</strong>
        </label>
        <SelectButton
          v-model="recData.sex"
          class="p-inputtext-sm p-mr-1 p-d-inline-block"
          :options="[
            { label: 'Nam', code: 1 },
            { label: 'Nữ', code: 2 },
          ]"
          optionLabel="label"
          optionValue="code"
        />
      </div>
      <div class="p-mt-3">
        <label class="p-d-inline-block m-label-size-2 p-text-right p-mr-1">
          Email <strong class="p-error">*</strong>
        </label>
        <InputText
          type="text"
          v-model="recData.email"
          class="p-inputtext-sm p-col-4"
        />
        <label class="p-d-inline-block m-label-size-2 p-text-right p-mr-1">
          Phone
        </label>
        <InputText
          type="text"
          v-model="recData.phone"
          class="p-inputtext-sm p-col-4"
        />
      </div>

      <div class="p-mt-3 p-d-flex p-ai-center">
        <label class="p-d-inline-block m-label-size-2 p-text-right p-mr-1"
          >Chức vụ <strong class="p-error">*</strong>
        </label>
        <Dropdown
          style="width: 33.3333%"
          class="p-inputtext-sm"
          v-model="recData.positionId"
          :options="position"
          :filter="true"
          :showClear="true"
          optionLabel="name"
          optionValue="positionId"
        />
        <label class="p-d-inline-block m-label-size-2 p-text-right p-mr-1"
          >Phòng ban <strong class="p-error">*</strong>
        </label>
        <Dropdown
          style="width: 33.3333%"
          class="p-inputtext-sm"
          v-model="recData.departmentId"
          :options="department"
          :filter="true"
          :showClear="true"
          optionLabel="name"
          optionValue="departmentId"
        />
      </div>
      <div class="p-mt-3">
        <label class="p-d-inline-block m-label-size-2 p-text-right p-mr-1">
          Địa chỉ
        </label>
        <InputText
          type="text"
          v-model="recData.address"
          class="p-inputtext-sm"
          style="width: 77.5%"
        />
      </div>
    </div>
    <div class="p-mt-2 p-d-flex p-flex-row p-jc-end" style="width: 100%">
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
import EmployeeApi from "@/api/employee-api";
import { useToast } from "primevue/usetoast";
import positionApi from "@/api/material-management/position-api";
import departmentApi from "@/api/material-management/department-api";

export default defineComponent({
  props: {
    rec: { type: Object, required: true },
    isNew: { type: Boolean, default: true, required: false },
  },

  setup(props, { emit }): unknown {
    const toast = useToast();
    const position = ref([]);
    const department = ref([]);
    const showMessage = ref(false);
    const userMessage = ref("");
    const changesApplied = ref(false);
    const recData = ref(JSON.parse(JSON.stringify(props.rec))); // do not create direct refs to props to avoid making changes to props, instead use a cloned value of prop

    const onApplyChanges = async () => {
      const rawEmpObj = JSON.parse(JSON.stringify(recData.value));
      delete rawEmpObj.index;
      delete rawEmpObj.strBitrh;
      // rawEmpObj.id = rawEmpObj.employeeId || "";
      // delete rawEmpObj.employeeId;
      // delete rawEmpObj.loginName;
      // delete rawEmpObj.password;
      // delete rawEmpObj.role;
      // delete rawEmpObj.fullName;
      let msg: any[];
      msg = [];
      if (!rawEmpObj.code) {
        msg.push("mã nhân viên");
      }
      if (!rawEmpObj.firstName) {
        msg.push("họ nhân viên");
      }
      if (!rawEmpObj.lastName) {
        msg.push("tên nhân viên");
      }
      if (!rawEmpObj.birth) {
        msg.push("ngày sinh");
      }
      if (!rawEmpObj.sex) {
        msg.push("giới tính");
      }
      if (!rawEmpObj.email) {
        msg.push("email");
      }
      if (!rawEmpObj.positionId) {
        msg.push("chức vụ");
      }
      if (!rawEmpObj.departmentId) {
        msg.push("phòng ban");
      }
      if (msg.length > 0) {
        userMessage.value =
          "Trường " + msg.join(", ") + " không được để trống!";
        showMessage.value = true;
      } else {
        const check = await EmployeeApi.getEmployeeByCode(rawEmpObj);
        if (check.data) {
          userMessage.value = "Mã nhân viên bị trùng. Vui lòng nhập lại!";
          showMessage.value = true;
        } else {
          let resp;
          rawEmpObj.fullName = rawEmpObj.firstName + " " + rawEmpObj.lastName;
          if (rawEmpObj.employeeId) {
            resp = await EmployeeApi.updateEmployee(rawEmpObj);
          } else {
            resp = await EmployeeApi.addEmployee(rawEmpObj);
          }
          if (resp.data.msgType === "SUCCESS") {
            toast.add({
              severity: "success",
              summary: rawEmpObj.employeeId ? "Employee Updated" : "Employee Added",
              detail: `${rawEmpObj.firstName} ${rawEmpObj.lastName}`,
              life: 3000,
            });
            if (!rawEmpObj.employeeId) {
              recData.value.employeeemployeeId = "CREATED";
            }
            changesApplied.value = true;
            emit("changed");
            setTimeout(() => {
              onCancel();
            }, 500);
          } else {
            toast.add({
              severity: "error",
              summary: "Error updating Employee",
              detail: resp.data.msg,
            });
          }
        }
      }
    };

    const onCancel = () => {
      emit("cancel");
    };

    onMounted(async () => {
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

    return {
      showMessage,
      userMessage,
      changesApplied,
      recData,
      onApplyChanges,
      onCancel,
      position,
      department,
    };
  },
});
</script>
