<template>
  <div class="m-font-regular">
    <Toast />
    <h4>
      Dòng thẻ kho #
      <span style="color: var(--primary-color)">
        {{ recData.warehouseCardFlowId ? recData.warehouseCardFlowId : "NEW" }}
      </span>
    </h4>
    <transition name="p-message">
      <Message v-if="showMessage" severity="error" @close="showMessage = false">
        {{ userMessage }}</Message
      >
    </transition>
    <div>
      <div class="p-mt-3" style="text-align: center">
        <SelectButton
          @click="setValue()"
          v-model="recData.type"
          class="p-inputtext-sm p-mr-1 p-d-inline-block"
          :options="[
            { label: 'Phiếu nhập', code: 1 },
            { label: 'Phiếu xuất', code: 2 },
          ]"
          optionLabel="label"
          optionValue="code"
        />
      </div>
      <div
        class="p-mt-3"
        v-if="
          recData.type == 1 ||
          (recData.deliveryBillId && recData.receiptId == null)
        "
      >
        <label
          class="p-d-inline-block m-label-size-2 p-text-right p-mr-1"
          style="padding-top: 10px; margin-right: 10px"
          >Phiếu nhập <strong class="p-error">*</strong>
        </label>
        <Dropdown
          style="width: 76.3%"
          class="p-inputtext-sm"
          v-model="recData.deliveryBillId"
          :options="arrDeliveryBill"
          :filter="true"
          :showClear="true"
          @change="getAmountDelivery()"
          optionLabel="code"
          optionValue="deliveryBillId"
        />
      </div>
      <div
        class="p-mt-3"
        v-if="
          recData.type == 2 ||
          (recData.receiptId && recData.deliveryBillId == null)
        "
      >
        <label
          class="p-d-inline-block m-label-size-2 p-text-right p-mr-1"
          style="padding-top: 10px; margin-right: 10px"
          >Phiếu xuất <strong class="p-error">*</strong>
        </label>
        <Dropdown
          style="width: 76.3%"
          class="p-inputtext-sm"
          v-model="recData.receiptId"
          :options="arrReceipt"
          :filter="true"
          :showClear="true"
          @change="getAmountReceipt()"
          optionLabel="code"
          optionValue="receiptId"
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
          style="width: 76.3%"
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
          style="width: 76.3%"
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
import WarehouseCardFlowApi from "@/api/material-management/warehouse-card-flow-api";
import DeliveryBillFlowApi from "@/api/material-management/delivery-bill-flow-api";
import { useToast } from "primevue/usetoast";
import ReceiptFlowApi from "@/api/material-management/receipt-flow-api";

export default defineComponent({
  props: {
    rec: {
      type: Object,
      required: true,
    },
    arrReceipt: [],
    arrDeliveryBill: [],
    suppliesId: {},
  },

  setup(props, { emit }): unknown {
    const toast = useToast();
    const showMessage = ref(false);
    const userMessage = ref("");
    const changesApplied = ref(false);
    const recData = ref(JSON.parse(JSON.stringify(props.rec))); // do not create direct refs to props to avoid making changes to props, instead use a cloned value of prop
    const suppliesId = ref(JSON.parse(JSON.stringify(props.suppliesId)));
    const onApplyChanges = async () => {
      const rawWarehouseCardFlowObj = JSON.parse(JSON.stringify(recData.value));
      delete rawWarehouseCardFlowObj.index;
      let msg: any[];
      msg = [];
      if (!rawWarehouseCardFlowObj.type) {
        msg.push("type");
      }
      if (
        !rawWarehouseCardFlowObj.deliveryBillId &&
        !rawWarehouseCardFlowObj.receiptId
      ) {
        msg.push("phiếu nhập/phiếu xuất");
      }
      if (!rawWarehouseCardFlowObj.amount) {
        msg.push("số lượng");
      }
      if (msg.length > 0) {
        userMessage.value =
          "Trường " + msg.join(", ") + " không được để trống!";
        showMessage.value = true;
      } else {
        let resp;
        if (rawWarehouseCardFlowObj.warehouseCardFlowId) {
          if (rawWarehouseCardFlowObj.type < 2) {
            rawWarehouseCardFlowObj.receiptId = null;
          } else {
            rawWarehouseCardFlowObj.deliveryBillId = null;
          }
          resp = await WarehouseCardFlowApi.updateWarehouseCardFlow(
            rawWarehouseCardFlowObj
          );
        } else {
          if (rawWarehouseCardFlowObj.type < 2) {
            rawWarehouseCardFlowObj.receiptId = null;
          } else {
            rawWarehouseCardFlowObj.deliveryBillId = null;
          }
          resp = await WarehouseCardFlowApi.addWarehouseCardFlow(
            rawWarehouseCardFlowObj
          );
        }
        if (resp.data.msgType === "SUCCESS") {
          toast.add({
            severity: "success",
            summary: rawWarehouseCardFlowObj.warehouseCardFlowId
              ? "Product Updated"
              : "Product Added",
            detail: `${rawWarehouseCardFlowObj.name} (${rawWarehouseCardFlowObj.code})`,
            life: 3000,
          });
          if (!rawWarehouseCardFlowObj.warehouseCardFlowId) {
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
            summary: "Error",
            detail: resp.data.msg,
          });
        }
      }
    };

    const onCancel = () => {
      emit("cancel");
    };

    const getAmountDelivery = async () => {
      const deliveryBillFlow = await DeliveryBillFlowApi.getDeliveryBillFlow(
        recData.value.deliveryBillId,
        suppliesId.value
      );
      if (
        deliveryBillFlow.data.length > 0 &&
        suppliesId.value > 0 &&
        recData.value.deliveryBillId
      ) {
        const data = deliveryBillFlow.data[0].amount;
        recData.value.amount = data || 0;
      } else {
        recData.value.amount = 0;
      }
    };

    const getAmountReceipt = async () => {
      const receiptFlow = await ReceiptFlowApi.getReceiptFlow(
        recData.value.receiptId,
        suppliesId.value
      );
      if (
        receiptFlow.data.length > 0 &&
        suppliesId.value > 0 &&
        recData.value.receiptId
      ) {
        const data = receiptFlow.data[0].amount;
        recData.value.amount = data || 0;
      } else {
        recData.value.amount = 0;
      }
    };

    const setValue = () => {
      if (recData.type > 1) {
        recData.value.receiptId = null;
        recData.value.amount = null;
      } else {
        recData.value.deliveryBillId = null;
        recData.value.amount = null;
      }
    };

    return {
      showMessage,
      userMessage,
      changesApplied,
      recData,
      onApplyChanges,
      onCancel,
      getAmountDelivery,
      setValue,
      getAmountReceipt,
      suppliesId,
    };
  },
});
</script>
