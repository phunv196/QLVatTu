<template>
  <div>
    <ConfirmDialog position="top"></ConfirmDialog>
    <Toast/>
    <Sidebar v-model:visible="showSlideOut" position="right" style="width:700px">
      <WarehouseCardFlowDetails :rec="selectedRec" @cancel="showSlideOut = false" @changed="getData()"
                                :suppliesId="suppliesId" :arrReceipt="arrReceipt" :arrDeliveryBill="arrDeliveryBill" :isNew="isNewRec"></WarehouseCardFlowDetails>
    </Sidebar>
    <h3> Danh sách dòng thẻ kho </h3>
    <div class="p-d-flex p-flex-row p-mb-3" style="width:1000px">
      <div style="display:inline-block; flex:1"></div>
      <Button icon="pi pi-user" iconPos="right" label="ADD" @click="onAddClick()"
              class="p-ml-1 p-button-sm"></Button>
    </div>
    <DataTable
      :value="list"
      :paginator="true"
      :lazy="true"
      :rows="pageSize"
      :totalRecords="totalRecs"
      :loading="isLoading"
      @page="onPageChange($event)"
      class="p-datatable-sm p-datatable-hoverable-rows m-border p-mb-4" style="width:1000px">
      <Column field="index" header="STT" headerStyle="width:90px;"></Column>
      <Column field="receiptCode" header="Mã phiếu nhập" headerStyle="width:90px"></Column>
      <Column field="deliveryBillCode" header="Mã phiếu xuất" headerStyle="width:90px"></Column>
      <Column field="type" header="Mã nhân viên" headerStyle="width:90px"></Column>
      <Column field="employeeName" header="Tên nhân viên" headerStyle="width:190px"></Column>
      <Column field="amount" header="Số lượng" headerStyle="width:120px"></Column>
      <Column header="ACTION" headerStyle="width:100px" bodyStyle="padding:3px">
        <template #body="slotProps">
          <Button icon="pi pi-pencil" @click="onEditClick(slotProps.data)"
                  class="p-button-sm p-button-rounded p-button-secondary p-button-text"/>
          <Button icon="pi pi-trash" @click="onDeleteClick(slotProps.data)"
                  class="p-button-sm p-button-rounded p-button-danger p-button-text"/>
        </template>
      </Column>
    </DataTable>
  </div>
</template>


<script lang='ts'>
import { ref, onMounted, defineComponent } from 'vue';
import WarehouseCardFlowApi from '@/api/material-management/warehouse-card-flow-api'; // eslint-disable-line import/no-cycle
import WarehouseCardFlowDetails from '@/views/material-management/warehouse-card-flow/WarehouseCardFlowDetails.vue';
import { useConfirm } from 'primevue/useconfirm';
import { useToast } from 'primevue/usetoast';
import ReceiptApi from '@/api/material-management/receipt-api';
import DeliveryBillApi from '@/api/material-management/delivery-bill-api';

export default defineComponent({
  props : {
    requence: {},
    suppliesId: {},
  },
  setup(props): unknown {
    const isLoading = ref(false);
    const showSlideOut = ref(false);
    const pageSize = ref(5);
    const totalPages = ref(0);
    const totalRecs = ref(0);
    const selectedRec = ref({});
    const isNewRec = ref(false);
    const isCustomer = ref(false);
    const list = ref([]);
    const arrReceipt = ref([]);
    const arrDeliveryBill = ref([]);
    const suppliesId = ref({});
    const confirm = useConfirm();
    const toast = useToast();
    let currentPage = 1;
    const warehouseCardId = ref(JSON.parse(JSON.stringify(props.requence)));
    const supplies = ref(JSON.parse(JSON.stringify(props.suppliesId)));

    suppliesId.value = supplies.value;

    const getData = async (page: number, requestedPageSize: number, warehouseCardFlowId = '') => {
      // isLoading.value = true;
      try {
        debugger
        const resp = await WarehouseCardFlowApi.getWarehouseCardFlows(page, requestedPageSize, warehouseCardId.value);
        let i = 1;
        list.value = resp.data.list.map((v: Record<string, unknown>) => {
          let index = 1;
          if (page > 1) {
            index = (10 * (currentPage - 1)) / 2 + i++
          } else {
            index = i++;
          }
          return {
            ...v,
            index,
          };
        });
        // isLoading.value = false;
        currentPage = resp.data.currentPage;
        totalPages.value = resp.data.totalPages;
        totalRecs.value = resp.data.total;
      } catch (err) {
        console.log('REST ERROR: %O', err.response ? err.response : err);
        isLoading.value = false;
      }
    };

    const confirmDialog = (rec: Record<string, unknown>) => {
      debugger;
      confirm.require({
        message: `Do you want to remove ${rec.name} from product catalog ?`,
        header: 'Remove',
        icon: 'pi pi-question-circle',
        acceptIcon: 'pi pi-check',
        accept: async () => {
          try {
            const resp = await WarehouseCardFlowApi.deleteWarehouseCardFlow(rec.warehouseCardFlowId as string);
            if (resp.data.msgType === 'SUCCESS') {
              getData(currentPage, pageSize.value);
              toast.add({
                severity: 'success',
                summary: 'Successfully Deleted',
                life: 3000
              });
            } else {
              toast.add({
                severity: 'error',
                summary: 'Access Denied',
                detail: resp.data.msg,
                life: 3000
              });
            }
          } catch (e) {
            toast.add({
              severity: 'error',
              summary: 'Error',
              detail: 'Unable to connect to server',
              life: 3000
            });
          }
        },
        reject: () => {
          console.log('NO');
        },
      });
    };

    const onPageChange = (event: Record<string, unknown>) => {
      if (currentPage !== (event.page as number + 1)) {
        currentPage = event.page as number + 1;
        getData(currentPage, pageSize.value);
      }
    };

    const onAddClick = async () => {
      debugger
      const Rece = await ReceiptApi.getAllBySuppliesId(supplies.value)
      let itemReceipt: any;
      if(Rece.data.list){
        itemReceipt = Rece.data.list;
      }
      arrReceipt.value = itemReceipt;

      const deli = await DeliveryBillApi.getAllBySuppliesId(supplies.value)
      let itemDeliveryBill: any;
      if(deli.data.list){
        itemDeliveryBill = deli.data.list;
      }
      arrDeliveryBill.value = itemDeliveryBill;
      isNewRec.value = true;
      selectedRec.value = { warehouseCardFlowId: '', warehouseCardId: warehouseCardId.value };
      showSlideOut.value = true;
    };

    const onDeleteClick = (rec: Record<string, unknown>) => {
      debugger;
      confirmDialog(rec);
    };

    const onEditClick = async (rec: Record<string, unknown>) => {
      const Rece = await ReceiptApi.getAllBySuppliesId(supplies.value)
      let itemReceipt: any;
      if(Rece.data.list){
        itemReceipt = Rece.data.list;
      }
      arrReceipt.value = itemReceipt;

      const deli = await DeliveryBillApi.getAllBySuppliesId(supplies.value)
      let itemDeliveryBill: any;
      if(deli.data.list){
        itemDeliveryBill = deli.data.list;
      }
      arrDeliveryBill.value = itemDeliveryBill;
      showSlideOut.value = true;
      selectedRec.value = rec;
    };

    onMounted(() => {
      getData(0, pageSize.value);
    });

    return {
      list,
      isLoading,
      showSlideOut,
      pageSize,
      totalPages,
      totalRecs,
      selectedRec,
      isNewRec,
      isCustomer,
      onAddClick,
      onDeleteClick,
      onEditClick,
      onPageChange,
      getData,
      arrReceipt,
      arrDeliveryBill,
      suppliesId,
      //suppDeli
    };
  },
  components: {
    WarehouseCardFlowDetails,
  },
});
</script>
