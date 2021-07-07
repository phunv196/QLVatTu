<template>
  <div class="m-font-regular">
    <Toast/>
    <h4>Thẻ kho # <span style="color:var(--primary-color)"> {{ recData.warehouseCardId ? recData.warehouseCardId : 'NEW' }} </span></h4>

    <div>
      <div class="p-mt-3">
        <label class="p-d-inline-block m-label-size-2 p-text-right p-mr-1">Mã thẻ kho</label>
        <InputText type="text" v-model="recData.code" class="p-inputtext-sm p-mr-1" style="width:30%"/>
        <label class="p-d-inline-block m-label-size-2 p-text-right p-mr-1">Tên thẻ kho </label>
        <InputText type="text" v-model="recData.name" class="p-inputtext-sm" style="width:30%"/>
      </div>
      <div class="p-mt-3">

      </div>
      <div class="p-mt-3-date">
        <label class="p-d-inline-block m-label-size-2 p-text-right p-mr-1" style=" padding-top: 10px; ">Ngày lập thẻ kho</label>
        <Datepicker class="p-inputtext-sm" style="width:320px"
                    v-model="recData.dateCreated"
                    inputFormat="dd/MM/yyy"

        />
      </div>
      <div class="p-mt-3">
        <label class="p-d-inline-block m-label-size-2 p-text-right p-mr-1" style="padding-top: 10px; margin-right: 10px">Vật tư </label>
        <Dropdown style="width:30%;" class="p-inputtext-sm" v-model = "recData.suppliesId"
                  :options = arrSupplies
                  :filter = true
                  :disabled= checkDate
                  :showClear= true
                  optionLabel="name"
                  @change="change()"
                  optionValue="suppliesId"/>
        <label class="p-d-inline-block m-label-size-2 p-text-right p-mr-1" style="padding-top: 10px; margin-right: 10px">Kho </label>
        <Dropdown style="width:30%; left: 5px;" class="p-inputtext-sm" v-model = "recData.warehouseId"
                  :options = arrWarehouse
                  :filter = true
                  :disabled= checkDate
                  :showClear= true
                  optionLabel="name"
                  optionValue="warehouseId"/>
      </div>
      <div class="p-mt-3">
        <label class="p-d-inline-block m-label-size-2 p-text-right p-mr-1" style="padding-bottom: 20px;"> Ghi chú </label>
        <textarea rows="3" v-model="recData.description" class="p-inputtext-sm" maxlength="500" style="width: 70.2%"/>
      </div>
    </div>
    <div v-if="test1">
      <WarehouseCardFlow :requence=" recData.warehouseCardId"
                         :suppliesId="recData.suppliesId"
      ></WarehouseCardFlow>
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
import WarehouseCardApi from '@/api/material-management/warehouse-card-api';
import { useToast } from 'primevue/usetoast';
import WarehouseCardFlow from '@/views/material-management/warehouse-card-flow/WarehouseCardFlow.vue'
import DeliveryBillApi from '@/api/material-management/delivery-bill-api';
import SuppliesApi from '@/api/material-management/supplies-api';
import WarehouseApi from '@/api/material-management/warehouse-api';
import ReceiptApi from '@/api/material-management/receipt-api';
import { numeric } from 'vuelidate/lib/validators';

export default defineComponent({
  props: {
    rec: {
      type: Object,
      required: true
    },
    arrWarehouse: [],
    arrSupplies: []
  },
  setup(props, { emit }): unknown {
    debugger
    const toast = useToast();
    const showMessage = ref(false);
    const changesApplied = ref(false);
    let test1 = ref(false);
    const recData = ref(JSON.parse(JSON.stringify(props.rec))); // do not create direct refs to props to avoid making changes to props, instead use a cloned value of prop
    let suppliesId = recData.suppliesId == null ? 0  : recData.suppliesId;
    let checkDate = false;
    test1.value = recData.value.suppliesId != null? true : false;
    const change = async () => {
       debugger
      // const today = new Date().getDate();
      // const dateCreated = new Date(recData.value.dateCreated).getDate();
      // checkDate = dateCreated == today ? false : true;
      test1.value = false
      setTimeout(() =>{
       return test1.value = true
      } , 1)

    }
    const onApplyChanges = async () => {
      debugger
      const rawWarehouseCardObj = JSON.parse(JSON.stringify(recData.value));
      let resp;
      debugger
      const  checkId = await WarehouseCardApi.checkId(rawWarehouseCardObj.warehouseCardId);
      if (checkId.data) {
        resp = await WarehouseCardApi.updateWarehouseCard(rawWarehouseCardObj);
      } else {
        resp = await WarehouseCardApi.addWarehouseCard(rawWarehouseCardObj);
      }
      if (resp.data.msgType === 'SUCCESS') {
        toast.add({
          severity: 'success',
          summary: rawWarehouseCardObj.warehouseCardId ? 'Product Updated' : 'Product Added',
          detail: `${rawWarehouseCardObj.name} (${rawWarehouseCardObj.code})`,
          life: 3000
        });
        if (!rawWarehouseCardObj.warehouseCardId) {
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
      suppliesId,
      checkDate,
      test1,
      change
    };
  },
  components: {
    'WarehouseCardFlow': WarehouseCardFlow
  },
});
</script>
