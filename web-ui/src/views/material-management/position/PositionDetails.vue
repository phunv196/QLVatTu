<template>
  <div class="m-font-regular">
    <Toast/>
    <h4>Chức vụ # <span style="color:var(--primary-color)"> {{ recData.positionId ? recData.positionId : 'NEW' }} </span></h4>
    <span class="m-gray-text">Provide some fake details, the data will be refreshed at certain interval</span>

    <div>
      <div class="p-mt-3">
        <label class="p-d-inline-block m-label-size-2 p-text-right p-mr-1">Mã chức vụ</label>
        <InputText type="text" v-model="recData.code" class="p-inputtext-sm p-mr-1" style="width:70px"/>
      </div>
      <div class="p-mt-3">
        <label class="p-d-inline-block m-label-size-2 p-text-right p-mr-1">Tên chức vụ </label>
        <InputText type="text" v-model="recData.name" class="p-inputtext-sm"/>
      </div>
      <div class="p-mt-3">
        <label class="p-d-inline-block m-label-size-2 p-text-right p-mr-1"> Ghi chú </label>
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
import PositionApi from '@/api/material-management/position-api';
import { useToast } from 'primevue/usetoast';

export default defineComponent({
  props: {
    rec: {
      type: Object,
      required: true
    },
  },

  setup(props, { emit }): unknown {
    const toast = useToast();
    const showMessage = ref(false);
    const changesApplied = ref(false);
    const recData = ref(JSON.parse(JSON.stringify(props.rec))); // do not create direct refs to props to avoid making changes to props, instead use a cloned value of prop

    const onApplyChanges = async () => {
      debugger
      const rawPositionObj = JSON.parse(JSON.stringify(recData.value));
      let resp;
      debugger
      if (rawPositionObj.positionId) {
        resp = await PositionApi.updatePosition(rawPositionObj);
      } else {
        resp = await PositionApi.addPosition(rawPositionObj);
      }
      if (resp.data.msgType === 'SUCCESS') {
        toast.add({
          severity: 'success',
          summary: rawPositionObj.positionId ? 'Product Updated' : 'Product Added',
          detail: `${rawPositionObj.name} (${rawPositionObj.code})`,
          life: 3000
        });
        if (!rawPositionObj.positionId) {
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
});
</script>
