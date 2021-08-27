<template>
  <div class="m-font-regular">
    <Toast />
    <h4>
      Thẻ kho #
      <span style="color: var(--primary-color)">
        {{ recData.warehouseCardId ? recData.warehouseCardId : "NEW" }}
      </span>
    </h4>
    <transition name="p-message">
      <Message v-if="showMessage" severity="error" @close="showMessage = false">
        {{ userMessage }}</Message
      >
    </transition>
    <div>
      <div class="p-mt-3">
        <label class="p-d-inline-block m-label-size-3 p-text-right p-mr-1"
          >Mã thẻ kho <strong class="p-error">*</strong>
        </label>
        <InputText
          type="text"
          v-model="recData.code"
          class="p-inputtext-sm p-mr-1"
          style="width: 30%"
        />
        <label class="p-d-inline-block m-label-size-3 p-text-right p-mr-1"
          >Tên thẻ kho <strong class="p-error">*</strong>
        </label>
        <InputText
          type="text"
          v-model="recData.name"
          class="p-inputtext-sm"
          style="width: 30%"
        />
      </div>
      <div class="p-mt-3-date">
        <label
          class="p-d-inline-block m-label-size-3 p-text-right p-mr-1"
          style="padding-top: 10px"
          >Ngày lập thẻ kho <strong class="p-error">*</strong>
        </label>
        <Datepicker
          class="p-inputtext-sm"
          style="width: 320px"
          v-model="recData.dateCreated"
          inputFormat="dd/MM/yyy"
        />
      </div>
      <div class="p-mt-3">
        <label
          class="p-d-inline-block m-label-size-3 p-text-right p-mr-1"
          style="padding-top: 10px; margin-right: 10px"
          >Vật tư <strong class="p-error">*</strong>
        </label>
        <Dropdown
          style="width: 30%"
          class="p-inputtext-sm"
          v-model="recData.suppliesId"
          :options="arrSupplies"
          :filter="true"
          :disabled="checkDate"
          :showClear="true"
          optionLabel="name"
          @change="change()"
          optionValue="suppliesId"
          placeholder="--Hãy chọn--"
        />
        <label
          class="p-d-inline-block m-label-size-3 p-text-right p-mr-1"
          style="padding-top: 10px; margin-right: 10px"
          >Kho <strong class="p-error">*</strong>
        </label>
        <Dropdown
          style="width: 30%; left: 5px"
          class="p-inputtext-sm"
          v-model="recData.warehouseId"
          :options="arrWarehouse"
          :filter="true"
          :disabled="checkDate"
          :showClear="true"
          optionLabel="name"
          optionValue="warehouseId"
          placeholder="--Hãy chọn--"
        />
      </div>
      <div class="p-mt-3 p-d-flex p-ai-center">
        <label class="p-d-inline-block m-label-size-3 p-text-right p-mr-1">
          Ghi chú
        </label>
        <textarea
          rows="3"
          v-model="recData.description"
          class="p-inputtext-sm"
          maxlength="500"
          style="width: 72.6%"
        />
      </div>
    </div>
    <div v-if="test1">
      <WarehouseCardFlow
        :requence="recData.warehouseCardId"
        :suppliesId="recData.suppliesId"
      ></WarehouseCardFlow>
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
import WarehouseCardApi from "@/api/material-management/warehouse-card-api";
import { useToast } from "primevue/usetoast";
import WarehouseCardFlow from "@/views/material-management/warehouse-card-flow/WarehouseCardFlow.vue";
export default defineComponent({
  props: {
    rec: {
      type: Object,
      required: true,
    },
    arrWarehouse: [],
    arrSupplies: [],
  },
  setup(props, { emit }): unknown {
    const toast = useToast();
    const showMessage = ref(false);
    const userMessage = ref("");
    const changesApplied = ref(false);
    let test1 = ref(false);
    const recData = ref(JSON.parse(JSON.stringify(props.rec))); // do not create direct refs to props to avoid making changes to props, instead use a cloned value of prop
    let suppliesId = recData.suppliesId == null ? 0 : recData.suppliesId;
    let checkDate = false;
    test1.value = recData.value.suppliesId != null ? true : false;
    const change = async () => {
      // const today = new Date().getDate();
      // const dateCreated = new Date(recData.value.dateCreated).getDate();
      // checkDate = dateCreated == today ? false : true;
      test1.value = false;
      setTimeout(() => {
        return (test1.value = true);
      }, 1);
    };
    const onApplyChanges = async () => {
      const rawWarehouseCardObj = JSON.parse(JSON.stringify(recData.value));
      delete rawWarehouseCardObj.strDateCreated;
      delete rawWarehouseCardObj.index;
      let msg: any[];
      msg = [];
      if (!rawWarehouseCardObj.code) {
        msg.push("mã thẻ kho");
      }
      if (!rawWarehouseCardObj.name) {
        msg.push("tên thẻ kho");
      }
      if (!rawWarehouseCardObj.dateCreated) {
        msg.push("ngày lập thẻ");
      }
      if (!rawWarehouseCardObj.suppliesId) {
        msg.push("vật tư");
      }
      if (!rawWarehouseCardObj.warehouseId) {
        msg.push("kho");
      }
      if (msg.length > 0) {
        userMessage.value = "Trường " + msg.join(", ") + " không được để trống!";
        showMessage.value = true;
      } else {
        const check = await WarehouseCardApi.getWarehouseCardByCode(
          rawWarehouseCardObj
        );
        if (check.data) {
          userMessage.value = "Mã thẻ kho bị trùng. Vui lòng nhập lại!";
          showMessage.value = true;
        } else {
          let resp;
          const checkId = await WarehouseCardApi.checkId(
            rawWarehouseCardObj.warehouseCardId
          );
          if (checkId.data) {
            resp = await WarehouseCardApi.updateWarehouseCard(
              rawWarehouseCardObj
            );
          } else {
            resp = await WarehouseCardApi.addWarehouseCard(rawWarehouseCardObj);
          }
          if (resp.data.msgType === "SUCCESS") {
            toast.add({
              severity: "success",
              summary: rawWarehouseCardObj.warehouseCardId
                ? "Product Updated"
                : "Product Added",
              detail: `${rawWarehouseCardObj.name} (${rawWarehouseCardObj.code})`,
              life: 3000,
            });
            if (!rawWarehouseCardObj.warehouseCardId) {
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
      suppliesId,
      checkDate,
      test1,
      change,
    };
  },
  components: {
    WarehouseCardFlow,
  },
});
</script>
