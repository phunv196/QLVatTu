<template>
  <div class="m-font-regular">
    <Toast />
    <h4>
      Phiếu nhập kho #
      <span style="color: var(--primary-color)">
        {{ recData.receiptId ? recData.receiptId : "NEW" }}
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
          >Mã phiếu nhập</label
        >
        <InputText
          type="text"
          v-model="recData.code"
          class="p-inputtext-sm p-mr-1"
          style="width: 30%"
        />
        <label class="p-d-inline-block m-label-size-2 p-text-right p-mr-1"
          >Tên phiếu nhập
        </label>
        <InputText
          type="text"
          v-model="recData.name"
          class="p-inputtext-sm"
          style="width: 30%"
        />
      </div>
      <div class="p-mt-3"></div>
      <div class="p-mt-3-date">
        <label
          class="p-d-inline-block m-label-size-2 p-text-right p-mr-1"
          style="padding-top: 10px"
          >Ngày lập phiếu</label
        >
        <Datepicker
          class="p-inputtext-sm"
          style="width: 320px"
          v-model="recData.dateWarehousing"
          inputFormat="dd/MM/yyy"
        />
        <label
          class="p-d-inline-block m-label-size-2 p-text-right p-mr-1"
          style="padding-top: 10px; margin-right: 10px"
          >Kho
        </label>
        <Dropdown
          style="width: 30%; left: 5px"
          class="p-inputtext-sm"
          v-model="recData.warehouseId"
          :options="arrWarehouse"
          :filter="true"
          :showClear="true"
          optionLabel="name"
          optionValue="warehouseId"
        />
      </div>
      <div class="p-mt-3 p-d-flex p-ai-center">
        <label class="p-d-inline-block m-label-size-2 p-text-right p-mr-1">
          Ghi chú
        </label>
        <textarea
          rows="3"
          v-model="recData.description"
          class="p-inputtext-sm"
          maxlength="500"
          style="width: 70%"
        />
      </div>
    </div>
    <ReceiptFlow :requence="recData.receiptId"></ReceiptFlow>

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
import ReceiptApi from "@/api/material-management/receipt-api";
import { useToast } from "primevue/usetoast";
import ReceiptFlow from "@/views/material-management/receipt-flow/ReceiptFlow.vue";
export default defineComponent({
  props: {
    rec: {
      type: Object,
      required: true,
    },
    arrWarehouse: [],
  },

  setup(props, { emit }): unknown {
    const toast = useToast();
    const showMessage = ref(false);
    const userMessage = ref("");
    const changesApplied = ref(false);
    const recData = ref(JSON.parse(JSON.stringify(props.rec))); // do not create direct refs to props to avoid making changes to props, instead use a cloned value of prop

    const onApplyChanges = async () => {
      debugger;
      const rawReceiptObj = JSON.parse(JSON.stringify(recData.value));
      const checkId = await ReceiptApi.checkId(rawReceiptObj.receiptId);
      let msg: any[];
      msg = [];
      if (!rawReceiptObj.code) {
        msg.push("mã phiếu nhập");
      }
      if (!rawReceiptObj.name) {
        msg.push("tên phiếu nhập");
      }
      if (!rawReceiptObj.dateWarehousing) {
        msg.push("ngày lập phiếu");
      }
      if (!rawReceiptObj.warehouseId) {
        msg.push("kho");
      }
      if (msg.length > 0) {
        userMessage.value =
          "Trường " + msg.join(", ") + " không được để trống!";
        showMessage.value = true;
      } else {
        delete rawReceiptObj.index;
        delete rawReceiptObj.strDateWarehousing;
        const check = await ReceiptApi.getReceiptByCode(rawReceiptObj);
        if (check.data) {
          userMessage.value = "Mã phiếu nhập bị trùng. Vui lòng nhập lại!";
          showMessage.value = true;
        } else {
          let resp;
          debugger;
          if (checkId.data) {
            resp = await ReceiptApi.updateReceipt(rawReceiptObj);
          } else {
            resp = await ReceiptApi.addReceipt(rawReceiptObj);
          }
          if (resp.data.msgType === "SUCCESS") {
            toast.add({
              severity: "success",
              summary: rawReceiptObj.receiptId
                ? "Product Updated"
                : "Product Added",
              detail: `${rawReceiptObj.name} (${rawReceiptObj.code})`,
              life: 3000,
            });
            if (!rawReceiptObj.receiptId) {
              recData.value.id = "CREATED";
            }
            changesApplied.value = true;
            emit("changed");
          } else {
            toast.add({
              severity: "error",
              summary: "Error",
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
  components: {
    ReceiptFlow,
  },
});
</script>
