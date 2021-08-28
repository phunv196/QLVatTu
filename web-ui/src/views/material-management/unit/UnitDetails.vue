<template>
  <div class="m-font-regular">
    <Toast />
    <h4>
      Unit #
      <span style="color: var(--primary-color)">
        {{ recData.UnitId ? recData.UnitId : "NEW" }}
      </span>
    </h4>
    <transition name="p-message">
      <Message v-if="showMessage" severity="error" @close="showMessage = false">
        {{ userMessage }}</Message
      >
    </transition>

    <div>
      <div class="p-mt-1">
        <label class="p-d-inline-block m-label-size-2 p-text-right p-mr-1"
          >Mã đơn vị tính <strong class="p-error">*</strong> </label
        >
        <InputText
          type="text"
          v-model="recData.code"
          class="p-inputtext-sm p-col-8 p-mr-1"
        />
      </div>
      <div class="p-mt-1">
        <label class="p-d-inline-block m-label-size-2 p-text-right p-mr-1"
          >Tên đơn vị tính <strong class="p-error">*</strong>
        </label>
        <InputText type="text" v-model="recData.name" class="p-inputtext-sm p-col-8" />
      </div>
      <div class="p-mt-3 p-d-flex p-ai-center">
        <label class="p-d-inline-block m-label-size-2 p-text-right p-mr-1">
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
import { defineComponent, ref } from "vue";
import UnitApi from "@/api/material-management/unit-api";
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
      const rawUnitObj = JSON.parse(JSON.stringify(recData.value));
      let msg: any[];
      msg = [];
      if (!rawUnitObj.code) {
        msg.push("mã đơn vị tính");
      }
      if (!rawUnitObj.name) {
        msg.push("tên đơn vị tính");
      }
      if (msg.length > 0) {
        userMessage.value =
          "Trường " + msg.join(", ") + " không được để trống!";
        showMessage.value = true;
      } else {
        delete rawUnitObj.index;
        const check = await UnitApi.getUnitByCode(rawUnitObj);
        if (check.data) {
          userMessage.value = "Mã đơn vị tính bị trùng. Vui lòng nhập lại!";
          showMessage.value = true;
        } else {
          let resp;
          if (rawUnitObj.UnitId) {
            resp = await UnitApi.updateUnit(rawUnitObj);
          } else {
            resp = await UnitApi.addUnit(rawUnitObj);
          }
          if (resp.data.msgType === "SUCCESS") {
            toast.add({
              severity: "success",
              summary: rawUnitObj.UnitId ? "Sửa thành công!" : "Thêm mới thành công!",
              detail: `${rawUnitObj.name} (${rawUnitObj.code})`,
              life: 3000,
            });
            if (!rawUnitObj.UnitId) {
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
