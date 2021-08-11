<template>
  <div class="m-font-regular">
    <Toast />
    <h4>
      Phân xưởng #
      <span style="color: var(--primary-color)">
        {{ recData.factoryId ? recData.factoryId : "NEW" }}
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
          >Mã phân xưởng <strong class="p-error">*</strong>
        </label>
        <InputText
          type="text"
          v-model="recData.code"
          class="p-inputtext-sm p-col-3 p-mr-2"
        />
        <label class="p-d-inline-block m-label-size-4 p-text-right p-mr-1"
          >Tên phân xưởng <strong class="p-error">*</strong>
        </label>
        <InputText
          type="text"
          v-model="recData.name"
          class="p-inputtext-sm p-col-3"
        />
      </div>
      <div class="p-mt-3">
        <label class="p-d-inline-block m-label-size-4 p-text-right p-mr-1"
          >Email <strong class="p-error">*</strong>
        </label>
        <InputText
          type="email"
          v-model="recData.email"
          class="p-inputtext-sm p-col-3 p-mr-2"
        />
        <label class="p-d-inline-block m-label-size-4 p-text-right p-mr-1"
          >Nhân viên phụ trách <strong class="p-error">*</strong>
        </label>
        <Dropdown
          class="p-inputtext-sm"
          style="width: 25%"
          v-model="recData.employeeId"
          :options="arr"
          :filter="true"
          :showClear="true"
          optionLabel="fullName"
          optionValue="employeeId"
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
      <div class="p-mt-3-date">
        <label
          class="p-d-inline-block m-label-size-4 p-text-right p-mr-1"
          style="padding-top: 10px"
          >Ngày thi công <strong class="p-error">*</strong>
        </label>
        <Datepicker
          class="p-inputtext-sm p-col-3 p-mr-1"
          v-model="recData.dateConstruction"
          style="width: 242px"
          inputFormat="dd/MM/yyy"
        />
        <label
          class="p-d-inline-block m-label-size-4 p-text-right"
          style="padding-top: 10px; margin: 0 7px 0 0px"
        >
          Ngày hoàn thành <strong class="p-error">*</strong>
        </label>
        <Datepicker
          class="p-inputtext-sm p-col-3 p-mr-2"
          style="width: 242px"
          v-model="recData.dateFinish"
          inputFormat="dd/MM/yyy"
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
import FactoryApi from "@/api/material-management/factory-api";
import { useToast } from "primevue/usetoast";

export default defineComponent({
  props: {
    rec: {
      type: Object,
      required: true,
    },
    arr: [],
    itemById: {},
  },

  setup(props, { emit }): unknown {
    const toast = useToast();
    const showMessage = ref(false);
    const userMessage = ref("");
    const changesApplied = ref(false);
    const dataRec = ref(JSON.parse(JSON.stringify(props.rec))); // do not create direct refs to props to avoid making changes to props, instead use a cloned value of prop
    const dataItem = ref(JSON.parse(JSON.stringify(props.itemById)));
    let recData: any;
    if (dataItem) {
      recData = dataItem;
    } else {
      recData = dataRec;
    }
    const onApplyChanges = async () => {
      const rawFactoryObj = JSON.parse(JSON.stringify(recData.value));
      delete rawFactoryObj.index;
      delete rawFactoryObj.strDateFinish;
      delete rawFactoryObj.strDateWarehousing;
      let msg: any[];
      msg = [];
      if (!rawFactoryObj.code) {
        msg.push("mã phân xưởng");
      }
      if (!rawFactoryObj.name) {
        msg.push("tên phân xưởng");
      }
      if (!rawFactoryObj.email) {
        msg.push("email");
      }
      if (!rawFactoryObj.employeeId) {
        msg.push("nhân viên phụ trách");
      }
      if (!rawFactoryObj.dateConstruction) {
        msg.push("ngày thi công");
      }
      if (!rawFactoryObj.dateFinish) {
        msg.push("ngày hoàn thành");
      }
      if (msg.length > 0) {
        userMessage.value =
          "Trường " + msg.join(", ") + " không được để trống!";
        showMessage.value = true;
      } else {
        let message = [];
        const check = await FactoryApi.getFactorByCode(rawFactoryObj);
        if (check.data) {
          message.push("mã phân xưởng bị trùng");
        }
        if (rawFactoryObj.dateConstruction > rawFactoryObj.dateFinish) {
          message.push("ngày thi công không thể lớn hơn ngày hoàn thành");
        }
        if (message.length > 0) {
          userMessage.value =
            "Trường " + message.join(", ") + ". Vui lòng nhập lại!";
          showMessage.value = true;
        } else {
          let resp;
          if (rawFactoryObj.factoryId) {
            resp = await FactoryApi.updateFactory(rawFactoryObj);
          } else {
            resp = await FactoryApi.addFactory(rawFactoryObj);
          }
          if (resp.data.msgType === "SUCCESS") {
            toast.add({
              severity: "success",
              summary: rawFactoryObj.factoryId
                ? "Product Updated"
                : "Product Added",
              detail: `${rawFactoryObj.name} (${rawFactoryObj.code})`,
              life: 3000,
            });
            if (!rawFactoryObj.factoryId) {
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
  components: {},
});
</script>
