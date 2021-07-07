<template>
  <div class="m-font-regular">
    <Toast/>
    <h4>Phân xưởng # <span style="color:var(--primary-color)"> {{ recData.factoryId ? recData.factoryId : 'NEW' }} </span></h4>
    <span class="m-gray-text">Provide some fake details, the data will be refreshed at certain interval</span>

    <div>
      <div class="p-mt-3">
        <label class="p-d-inline-block m-label-size-3 p-text-right p-mr-1">Mã phân xưởng</label>
        <InputText type="text" v-model="recData.code" class="p-inputtext-sm p-mr-1" style="width:70px"/>
      </div>
      <div class="p-mt-3">
        <label class="p-d-inline-block m-label-size-3 p-text-right p-mr-1">Tên phân xưởng </label>
        <InputText type="text" v-model="recData.name" class="p-inputtext-sm"/>
      </div>
      <div class="p-mt-3">
        <label class="p-d-inline-block m-label-size-3 p-text-right p-mr-1">Email </label>
        <InputText type="email" v-model="recData.email" class="p-inputtext-sm"/>
      </div>
      <div class="p-mt-3">
        <label class="p-d-inline-block m-label-size-3 p-text-right p-mr-1">Địa chỉ </label>
        <InputText type="text" v-model="recData.address" class="p-inputtext-sm"/>
      </div>
      <div class="p-mt-3">
        <label class="p-d-inline-block m-label-size-3 p-text-right p-mr-1">Nhân viên phụ trách </label>
        <Dropdown class="p-inputtext-sm" v-model = "recData.employeeId"
                  :options = arr
                  :filter = true
                  :showClear= true
                  optionLabel="lastName"
                  optionValue="id"/>
      </div>
      <div class="p-mt-3-date">
        <label class="p-d-inline-block m-label-size-3 p-text-right p-mr-1" style=" padding-top: 10px; ">Ngày thi công </label>
        <Datepicker class="p-inputtext-sm" width="30%"
            v-model="recData.dateConstruction"
        />
<!--        <InputText type="text" v-model="recData.strDateConstruction" class="p-inputtext-sm" width="30%"/>-->
      </div>
      <div class="p-mt-3-date">
        <label class="p-d-inline-block m-label-size-3 p-text-right p-mr-1" style=" padding-top: 10px; ">Ngày hoàn thành </label>
        <Datepicker class="p-inputtext-sm" width="30%"
                    v-model="recData.dateFinish"
        />
      </div>
      <div class="p-mt-3">
        <label class="p-d-inline-block m-label-size-3 p-text-right p-mr-1" > Ghi chú </label>
        <textarea rows="3" v-model="recData.description" class="p-inputtext-sm" maxlength="500" style="width: 300px"/>
      </div>
    </div>
    <!--button-->
    <div class="p-mt-2 p-d-flex p-flex-row p-jc-end" style="width:100%">
      <template v-if="changesApplied">
        <Button label="CLOSE" @click="$emit('cancel')" class="p-button-sm"></Button>
      </template>
      <template v-else>
        <Button label="CANCEL" @click="$emit('cancel')" class="p-button-sm p-button-outlined p-mr-1"></Button>
        <Button icon="pi pi-check" iconPos="left" label="APPLY CHANGES" @click="onApplyChanges()"
                class="p-button-sm"></Button>
      </template>
    </div>
  </div>
</template>

<script lang='ts'>
import { defineComponent, ref } from 'vue';
import FactoryApi from '@/api/material-management/factory-api';
import { useToast } from 'primevue/usetoast';
import Datepicker from 'vue3-datepicker';

export default defineComponent({
  props: {
    rec: {
      type: Object,
      required: true
    },
    arr: [],
    itemById: {},
  },

  setup(props, { emit }): unknown {
    const toast = useToast();
    const showMessage = ref(false);
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
      debugger
      const rawFactoryObj = JSON.parse(JSON.stringify(recData.value));
      let resp;
      debugger
      if (rawFactoryObj.factoryId) {
        resp = await FactoryApi.updateFactory(rawFactoryObj);
      } else {
        resp = await FactoryApi.addFactory(rawFactoryObj);
      }
      if (resp.data.msgType === 'SUCCESS') {
        toast.add({
          severity: 'success',
          summary: rawFactoryObj.factoryId ? 'Product Updated' : 'Product Added',
          detail: `${rawFactoryObj.name} (${rawFactoryObj.code})`,
          life: 3000
        });
        if (!rawFactoryObj.factoryId) {
          recData.value.id = 'CREATED';
        }
        changesApplied.value = true;
        emit('changed');
      } else {
        toast.add({
          severity: 'error',
          summary: 'Error',
          detail: resp.data.msg
        });
      }
    };

    const onCancel = () => {
      emit('cancel');
    };

    return {
      showMessage,
      changesApplied,
      recData,
      onApplyChanges,
      onCancel,
    };
  },
  components: {
    Datepicker
  },
});
</script>
