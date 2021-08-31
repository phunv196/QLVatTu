<template>
  <div class="m-font-regular">
    <Toast />
    <h4>
      Department #
      <span style="color: var(--primary-color)">
        {{ recData.departmentId ? recData.departmentId : "NEW" }}
      </span>
    </h4>
    <transition name="p-message">
      <Message v-if="showMessage" severity="error" @close="showMessage = false">
        {{ userMessage }}</Message
      >
    </transition>
    <div>
      <div class="p-mt-3">
        <label class="p-d-inline-block m-label-size-4 p-text-right p-mr-1"
          >Mã nhà phòng ban <strong class="p-error">*</strong>
        </label>
        <InputText
          type="text"
          v-model="recData.code"
          class="p-inputtext-sm p-col-8 p-mr-1"
        />
      </div>
      <div class="p-mt-3">
        <label class="p-d-inline-block m-label-size-4 p-text-right p-mr-1"
          >Tên nhà phòng ban <strong class="p-error">*</strong>
        </label>
        <InputText
          type="text"
          v-model="recData.name"
          class="p-inputtext-sm p-col-8"
        />
      </div>
      <div class="p-mt-3">
        <label class="p-d-inline-block m-label-size-4 p-text-right p-mr-1"
          >Email <strong class="p-error">*</strong>
        </label>
        <InputText
          type="text"
          v-model="recData.email"
          class="p-inputtext-sm p-col-8"
        />
      </div>
      <div class="p-mt-3">
        <label class="p-d-inline-block m-label-size-4 p-text-right p-mr-1"
          >Số điện thoại <strong class="p-error">*</strong>
        </label>
        <InputText
          type="text"
          v-model="recData.phone"
          class="p-inputtext-sm p-col-8"
        />
      </div>
      <div class="p-mt-3">
        <label class="p-d-inline-block m-label-size-4 p-text-right p-mr-1"
          >Địa chỉ
        </label>
        <InputText
          type="text"
          v-model="recData.address"
          class="p-inputtext-sm p-col-8"
        />
      </div>
      <div class="p-mt-3 p-d-flex p-ai-center">
        <label class="p-d-inline-block m-label-size-4 p-text-right p-mr-1">
          Ghi chú
        </label>
        <textarea
          rows="3"
          v-model="recData.description"
          class="p-inputtext-sm p-col-8"
          maxlength="500"
        />
      </div>
    </div>
    <!--button-->
    <div class="p-mt-3 p-d-flex p-flex-row p-jc-end" style="width: 100%">
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
import { defineComponent, ref } from "vue";
import DepartmentApi from "@/api/department-api";
import { useToast } from "primevue/usetoast";

export default defineComponent({
  props: {
    rec: {
      type: Object,
      required: true,
    },
  },

  setup(props, { emit }): unknown {
    const toast = useToast();
    const showMessage = ref(false);
    const userMessage = ref("");
    const changesApplied = ref(false);
    const recData = ref(JSON.parse(JSON.stringify(props.rec))); // do not create direct refs to props to avoid making changes to props, instead use a cloned value of prop

    const onApplyChanges = async () => {
      const rawDepartmentObj = JSON.parse(JSON.stringify(recData.value));
      delete rawDepartmentObj.index;
      let msg = [];
      if (!rawDepartmentObj.code) {
        msg.push("mã phòng ban");
      }
      if (!rawDepartmentObj.name) {
        msg.push("tên phòng ban");
      }
      if (!rawDepartmentObj.email) {
        msg.push("email");
      }
      if (!rawDepartmentObj.phone) {
        msg.push("phone");
      }
      if (msg.length > 0) {
        userMessage.value =
          "Trường " + msg.join(", ") + " không được để trống!";
        showMessage.value = true;
      } else {
        const check = await DepartmentApi.getDepartmentByCode(rawDepartmentObj);
        if (check.data) {
          userMessage.value = "Mã phòng ban bị trùng. Vui lòng nhập lại!";
          showMessage.value = true;
        } else {
          let resp;
          if (rawDepartmentObj.departmentId) {
            resp = await DepartmentApi.updateDepartment(rawDepartmentObj);
          } else {
            resp = await DepartmentApi.addDepartment(rawDepartmentObj);
          }
          if (resp.data.msgType === "SUCCESS") {
            toast.add({
              severity: "success",
              summary: rawDepartmentObj.departmentId
                ? "Sửa thành công!"
                : "Thêm mới thành công!",
              detail: `${rawDepartmentObj.name} (${rawDepartmentObj.code})`,
              life: 3000,
            });
            if (!rawDepartmentObj.departmentId) {
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
              summary: "Lỗi xảy ra",
              detail: resp.data.msg,
            });
          }
        }
      }
    };

    const onCancel = () => {
      emit("cancel");
    };

    return {
      showMessage,
      userMessage,
      changesApplied,
      recData,
      onApplyChanges,
      onCancel,
    };
  },
});
</script>
