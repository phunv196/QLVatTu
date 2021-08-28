<template>
  <div class="m-font-regular">
    <Toast />
    <h4>
      Vật tư nhập #
      <span style="color: var(--primary-color)">
        {{ recData.receiptFlowId ? recData.receiptFlowId : "NEW" }}
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
          >Vật tư <strong class="p-error">*</strong>
        </label>
        <Dropdown
          style="width: 40%"
          class="p-inputtext-sm"
          v-model="recData.suppliesId"
          :options="arrSupplies"
          :filter="true"
          :showClear="true"
          optionLabel="name"
          optionValue="suppliesId"
          placeholder="--Hãy chọn--"
        />
      </div>
      <div class="p-mt-3">
        <label class="p-d-inline-block m-label-size-2 p-text-right p-mr-1"
          >Số lượng <strong class="p-error">*</strong>
        </label>
        <InputNumber
          type="text"
          v-model="recData.amount"
          class="p-inputtext-sm"
          style="width: 40%"
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
import ReceiptFlowApi from "@/api/material-management/receipt-flow-api";
import { useToast } from "primevue/usetoast";

export default defineComponent({
  props: {
    rec: {
      type: Object,
      required: true,
    },
    arrSupplies: [],
    arrSupplier: [],
  },

  setup(props, { emit }): unknown {
    const toast = useToast();
    const showMessage = ref(false);
    const userMessage = ref("");
    const changesApplied = ref(false);
    const recData = ref(JSON.parse(JSON.stringify(props.rec))); // do not create direct refs to props to avoid making changes to props, instead use a cloned value of prop

    const onApplyChanges = async () => {
      const rawReceiptFlowObj = JSON.parse(JSON.stringify(recData.value));
      delete rawReceiptFlowObj.index
      let msg: any[];
      msg = [];
      if (!rawReceiptFlowObj.suppliesId) {
        msg.push("vật tư");
      }
      if (!rawReceiptFlowObj.amount) {
        msg.push("số lượng");
      }
      if (msg.length > 0) {
        userMessage.value =
          "Trường " + msg.join(", ") + " không được để trống!";
        showMessage.value = true;
      } else {
        let resp;
        delete rawReceiptFlowObj.index;
        if (rawReceiptFlowObj.receiptFlowId) {
          resp = await ReceiptFlowApi.updateReceiptFlow(rawReceiptFlowObj);
        } else {
          resp = await ReceiptFlowApi.addReceiptFlow(rawReceiptFlowObj);
        }
        if (resp.data.msgType === "SUCCESS") {
          toast.add({
            severity: "success",
            summary: rawReceiptFlowObj.receiptFlowId
              ? "Sửa"
              : "Thêm mới",
            detail: "Thao tác thành công",
            life: 3000,
          });
          if (!rawReceiptFlowObj.receiptFlowId) {
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
