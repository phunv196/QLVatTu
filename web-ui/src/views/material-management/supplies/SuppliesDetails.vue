<template>
  <div class="m-font-regular">
    <Toast/>
    <h4>Vật tư # <span style="color:var(--primary-color)"> {{ recData.suppliesId ? recData.suppliesId : 'NEW' }} </span></h4>

    <div>
      <div class="p-mt-3">
        <label class="p-d-inline-block m-label-size-2 p-text-left p-mr-1">Mã vật tư</label>
        <InputText type="text" v-model="recData.code" class="p-inputtext-sm p-mr-1" style="width:30%"/>
      </div>
      <div class="p-mt-3">
        <label class="p-d-inline-block m-label-size-2 p-text-left p-mr-1">Tên vvật tư </label>
        <InputText type="text" v-model="recData.name" class="p-inputtext-sm" style="width:30%"/>
      </div>
      <div class="p-mt-3">
        <label class="p-d-inline-block m-label-size-2 p-text-left p-mr-1">Chủng loại </label>
        <Dropdown style="width:30%" class="p-inputtext-sm" v-model = "recData.speciesId"
                  :options = arrSpecies
                  :filter = true
                  :showClear= true
                  optionLabel="name"
                  optionValue="speciesId"/>
      </div>
      <div class="p-mt-3">
        <label class="p-d-inline-block m-label-size-2 p-text-left p-mr-1">Chất lượng</label>
        <Dropdown style="width:30%" class="p-inputtext-sm" v-model = "recData.qualityId"
                  :options = arrQuality
                  :filter = true
                  :showClear= true
                  optionLabel="qualityName"
                  optionValue="qualityId"/>
      </div>
      <div class="p-mt-3">
        <label class="p-d-inline-block m-label-size-2 p-text-left p-mr-1">Nhà cung cấp </label>
        <Dropdown style="width:30%" class="p-inputtext-sm" v-model = "recData.supplierId"
                  :options = arrSupplier
                  :filter = true
                  :showClear= true
                  optionLabel="name"
                  optionValue="supplierId"/>
      </div>
      <div class="p-mt-3">
        <label class="p-d-inline-block m-label-size-2 p-text-left p-mr-1">Đơn vị tính </label>
        <InputText type="text" v-model="recData.unit" class="p-inputtext-sm" style="width:30%"/>
      </div>
      <div class="p-mt-3">
        <label class="p-d-inline-block m-label-size-2 p-text-left p-mr-1">Giá </label>
        <InputText type="text" v-model="recData.price" class="p-inputtext-sm" style="width:30%"/>
      </div>
<!--      <div class="p-mt-3">-->
<!--        <label class="p-d-inline-block m-label-size-2 p-text-left p-mr-1"> Ghi chú </label>-->
<!--        <textarea rows="3" v-model="recData.description" class="p-inputtext-sm" maxlength="500" style="width: 300px"/>-->
<!--      </div>-->
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
import SuppliesApi from '@/api/material-management/supplies-api';
import { useToast } from 'primevue/usetoast';


export default defineComponent({
  props: {
    rec: {
      type: Object,
      required: true
    },
    arrQuality: [],
    arrSpecies: [],
    arrSupplier: [],
    item: {},
  },

  setup(props, { emit }) :unknown {
    debugger
    const toast = useToast();
    const showMessage = ref(false);
    const changesApplied = ref(false);
    const recData = ref(JSON.parse(JSON.stringify(props.rec))); // do not create direct refs to props to avoid making changes to props, instead use a cloned value of prop
    const onApplyChanges = async () => {
      debugger
      const rawSuppliesObj = JSON.parse(JSON.stringify(recData.value));
      let resp;
      if (rawSuppliesObj.suppliesId) {
        resp = await SuppliesApi.updateSupplies(rawSuppliesObj);
      } else {
        resp = await SuppliesApi.addSupplies(rawSuppliesObj);
      }
      if (resp.data.msgType === 'SUCCESS') {
        toast.add({
          severity: 'success',
          summary: rawSuppliesObj.suppliesId ? 'Product Updated' : 'Product Added',
          detail: `${rawSuppliesObj.name} (${rawSuppliesObj.code})`,
          life: 3000
        });
        if (!rawSuppliesObj.suppliesId) {
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
