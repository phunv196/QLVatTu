<template>
  <div class="m-font-regular">
    <Toast />
    <h4>
      Supplier #
      <span style="color: var(--primary-color)">
        {{ recData.supplierId ? recData.supplierId : "NEW" }}
      </span>
    </h4>
    <transition name="p-message">
      <Message v-if="showMessage" severity="error" @close="showMessage = false">
        {{ userMessage }}</Message
      >
    </transition>
    <div>
      <div class="p-mt-3">
        <label class="p-d-inline-block m-label-size-4 p-text-right p-mr-2"
          >Mã nhà cung cấp <strong class="p-error">*</strong>
        </label>
        <InputText
          type="text"
          v-model="recData.code"
          class="p-inputtext-sm p-col-3 p-mr-1"
        />
        <label class="p-d-inline-block m-label-size-4 p-text-right p-mr-2"
          >Tên nhà cung cấp <strong class="p-error">*</strong>
        </label>
        <InputText
          type="text"
          v-model="recData.name"
          class="p-inputtext-sm p-col-3"
        />
      </div>
      <div class="p-mt-3">
        <label class="p-d-inline-block m-label-size-4 p-text-right p-mr-2"
          >Email <strong class="p-error">*</strong>
        </label>
        <InputText
          type="text"
          v-model="recData.email"
          class="p-inputtext-sm p-col-3 p-mr-1"
        />
        <label class="p-d-inline-block m-label-size-4 p-text-right p-mr-2"
          >Số điện thoại <strong class="p-error">*</strong>
        </label>
        <InputText
          type="text"
          v-model="recData.phone"
          class="p-inputtext-sm p-col-3"
        />
      </div>
      <div class="p-mt-3">
        <label class="p-d-inline-block m-label-size-4 p-text-right p-mr-2"
          >Địa chỉ
        </label>
        <InputText
          type="text"
          v-model="recData.address"
          class="p-inputtext-sm p-col-8"
        />
      </div>
      <div class="p-mt-3 p-d-flex p-ai-center">
        <label class="p-d-inline-block m-label-size-4 p-text-right p-mr-2">
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
import SupplierApi from "@/api/material-management/supplier-api";
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
      const rawSupplierObj = JSON.parse(JSON.stringify(recData.value));
      let msg: any[];
      msg = [];
      if (!rawSupplierObj.code) {
        msg.push("mã nhà cung cấp");
      }
      if (!rawSupplierObj.name) {
        msg.push("tên nhà cung cấp");
      }
      if (!rawSupplierObj.email) {
        msg.push("email");
      }
      if (!rawSupplierObj.phone) {
        msg.push("số điện thoại");
      }
      if (msg.length > 0) {
        userMessage.value =
          "Trường " + msg.join(", ") + " không được để trống!";
        showMessage.value = true;
      } else {
        delete rawSupplierObj.index;
        const check = await SupplierApi.getSupplierByCode(rawSupplierObj);
        if (check.data) {
          userMessage.value = "Mã nhà cung cấp bị trùng. Vui lòng nhập lại!";
          showMessage.value = true;
        } else {
          let resp;
          if (rawSupplierObj.supplierId) {
            resp = await SupplierApi.updateSupplier(rawSupplierObj);
          } else {
            resp = await SupplierApi.addSupplier(rawSupplierObj);
          }
          if (resp.data.msgType === "SUCCESS") {
            toast.add({
              severity: "success",
              summary: rawSupplierObj.supplierId
                ? "Product Updated"
                : "Product Added",
              detail: `${rawSupplierObj.name} (${rawSupplierObj.code})`,
              life: 3000,
            });
            if (!rawSupplierObj.supplierId) {
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
});
</script>
