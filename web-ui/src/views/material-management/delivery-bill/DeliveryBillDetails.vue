<template>
  <div class="m-font-regular">
    <Toast />
    <h4>
      Phiếu xuất #
      <span style="color: var(--primary-color)">
        {{ recData.deliveryBillId ? recData.deliveryBillId : "NEW" }}
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
          >Mã phiếu xuất <strong class="p-error">*</strong>
        </label>
        <InputText
          type="text"
          v-model="recData.code"
          class="p-inputtext-sm"
          style="width: 30%"
        />
        <label class="p-d-inline-block m-label-size-4 p-text-right p-mr-1"
          >Tên phiếu xuất <strong class="p-error">*</strong>
        </label>
        <InputText
          type="text"
          v-model="recData.name"
          class="p-inputtext-sm p-mr-1"
          style="width: 30%"
        />
      </div>
      <!--      <div class="p-mt-3">-->
      <!--        <label class="p-d-inline-block m-label-size-4 p-text-right p-mr-1">Tên phiếu xuất </label>-->
      <!--        <InputText type="text" v-model="recData.name" class="p-inputtext-sm"/>-->
      <!--      </div>-->
      <div class="p-mt-3-date">
        <label
          class="p-d-inline-block m-label-size-4 p-text-right p-mr-1"
          style="padding-top: 10px"
          >Ngày lập phiếu <strong class="p-error">*</strong>
        </label>
        <Datepicker
          class="p-inputtext-sm"
          style="width: 320px"
          v-model="recData.dateDeliveryBill"
          inputFormat="dd/MM/yyy"
        />
      </div>
      <div class="p-mt-3">
        <label class="p-d-inline-block m-label-size-4 p-text-right p-mr-1"
          >Phân xưởng <strong class="p-error">*</strong>
        </label>
        <Dropdown
          style="width: 30%"
          class="p-inputtext-sm"
          v-model="recData.factoryId"
          :options="arrFactory"
          :filter="true"
          :showClear="true"
          optionLabel="name"
          optionValue="factoryId"
        />

        <label class="p-d-inline-block m-label-size-4 p-text-right p-mr-1"
          >Kho <strong class="p-error">*</strong>
        </label>
        <Dropdown
          style="width: 30%"
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
        <label class="p-d-inline-block m-label-size-4 p-text-right p-mr-1">
          Ghi chú
        </label>
        <textarea
          rows="3"
          v-model="recData.description"
          class="p-inputtext-sm"
          maxlength="500"
          style="width: 74.5%"
        />
      </div>
    </div>
    <DeliveryBillFlow :requence="recData.deliveryBillId"></DeliveryBillFlow>

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

<!--suppress TypeScriptCheckImport -->
<script lang='ts'>
import { defineComponent, ref } from "vue";
import DeliveryBillApi from "@/api/material-management/delivery-bill-api";
import { useToast } from "primevue/usetoast";
import DeliveryBillFlow from "@/views/material-management/delivery-bill-flow/DeliveryBillFlow.vue";

export default defineComponent({
  props: {
    rec: {
      type: Object,
      required: true,
    },
    arrWarehouse: [],
    arrFactory: [],
  },

  setup(props, { emit }): unknown {
    const toast = useToast();
    const showMessage = ref(false);
    const userMessage = ref("");
    const changesApplied = ref(false);
    const recData = ref(JSON.parse(JSON.stringify(props.rec))); // do not create direct refs to props to avoid making changes to props, instead use a cloned value of prop
    const onApplyChanges = async () => {
      const rawDeliveryBillObj = JSON.parse(JSON.stringify(recData.value));
      delete rawDeliveryBillObj.index;
      delete rawDeliveryBillObj.strDateDeliveryBill;
      let msg: any[];
      msg = [];
      if (!rawDeliveryBillObj.code) {
        msg.push("mã phiếu xuất");
      }
      if (!rawDeliveryBillObj.name) {
        msg.push("tên phiếu xuất");
      }
      if (!rawDeliveryBillObj.dateDeliveryBill) {
        msg.push("ngày lập phiếu");
      }
      if (!rawDeliveryBillObj.factoryId) {
        msg.push("phân xưởng");
      }
      if (!rawDeliveryBillObj.warehouseId) {
        msg.push("kho");
      }
      if (msg.length > 0) {
        userMessage.value =
          "Trường " + msg.join(", ") + " không được để trống!";
        showMessage.value = true;
      } else {
        const check = await DeliveryBillApi.getDeliveryBillByCode(
          rawDeliveryBillObj
        );
        if (check.data) {
          userMessage.value = "Mã phiếu xuất bị trùng. Vui lòng nhập lại!";
          showMessage.value = true;
        } else {
          let resp;
          const checkId = await DeliveryBillApi.checkId(
            rawDeliveryBillObj.deliveryBillId
          );
          if (checkId.data) {
            resp = await DeliveryBillApi.updateDeliveryBill(rawDeliveryBillObj);
          } else {
            resp = await DeliveryBillApi.addDeliveryBill(rawDeliveryBillObj);
          }
          if (resp.data.msgType === "SUCCESS") {
            toast.add({
              severity: "success",
              summary: rawDeliveryBillObj.deliveryBillId
                ? "Product Updated"
                : "Product Added",
              detail: `${rawDeliveryBillObj.name} (${rawDeliveryBillObj.code})`,
              life: 3000,
            });
            if (!rawDeliveryBillObj.deliveryBillId) {
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
    DeliveryBillFlow,
  },
});
</script>
