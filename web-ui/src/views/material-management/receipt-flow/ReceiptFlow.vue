<template>
  <div>
    <Toast/>
    <Sidebar v-model:visible="showSlideOut" position="right" style="width:700px">
      <ReceiptFlowDetails :rec="selectedRec" @cancel="showSlideOut = false" @changed="getData()"
                   :arrSupplies="arrSupplies" :arrSupplier="arrSupplier" :isNew="isNewRec"></ReceiptFlowDetails>
    </Sidebar>
    <h3> Danh sách vật tư nhập </h3>
    <div class="p-d-flex p-flex-row p-mb-1" style="width:1000px">
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
      <Column field="receiptFlowId" header="ID" headerStyle="width:50px;"></Column>
      <Column field="suppliesCode" header="Mã vật tư" headerStyle="width:80px"></Column>
      <Column field="suppliesName" header="Tên vật tư" headerStyle="width:160px"></Column>
      <Column field="supplierCode" header="Nhà sản xuất" headerStyle="width:160px"></Column>
      <Column field="speciesName" header="Chủng loại" headerStyle="width:90px"></Column>
      <Column field="amount" header="Số lượng" headerStyle="width:90px"></Column>
      <Column field="suppliesUnit" header="Đơn vị tính" headerStyle="width:90px"></Column>
      <Column field="suppliesPrice" header="Giá" headerStyle="width:90px"></Column>
      <Column field="calculatePrice" header="Thành tiền" headerStyle="width:90px"></Column>
      <Column header="ACTION" headerStyle="width:95px" bodyStyle="padding:3px">
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
import ReceiptFlowApi from '@/api/material-management/receipt-flow-api'; // eslint-disable-line import/no-cycle
import ReceiptFlowDetails from '@/views/material-management/receipt-flow/ReceiptFlowDetails.vue';
import { useConfirm } from 'primevue/useconfirm';
import { useToast } from 'primevue/usetoast';
import SuppliesApi from '@/api/material-management/supplies-api';
import SupplierApi from '@/api/material-management/supplier-api';

export default defineComponent({
  props: {
    requence: {},
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
    const arrSupplies = ref([]);
    const arrSupplier = ref([]);
    const confirm = useConfirm();
    const toast = useToast();
    let currentPage = 1;
    const receiptId = ref(JSON.parse(JSON.stringify(props.requence)));
    const getData = async (page: number, requestedPageSize: number, receiptFlowId = '') => {
      // isLoading.value = true;
      try {
        const resp = await ReceiptFlowApi.getReceiptFlows(page, requestedPageSize, receiptId.value);
        list.value = resp.data.list;
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
      confirm.require({
        message: `Do you want to remove ${rec.name} from product catalog ?`,
        header: 'Remove',
        icon: 'pi pi-question-circle',
        acceptIcon: 'pi pi-check',
        accept: async () => {
          try {
            const resp = await ReceiptFlowApi.deleteReceiptFlow(rec.receiptFlowId as string);
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
      const supp = await SuppliesApi.getAll()
      let itemSupplies: any;
      if(supp.data.list){
        itemSupplies = supp.data.list;
      }
      arrSupplies.value = itemSupplies;

      const sup = await SupplierApi.getAll()
      let itemSupplier: any;
      if(supp.data.list){
        itemSupplier = sup.data.list;
      }
      arrSupplier.value = itemSupplier;

      isNewRec.value = true;
      selectedRec.value = { receiptFlowId: '' , receiptId : receiptId};
      showSlideOut.value = true;
    };

    const onDeleteClick = (rec: Record<string, unknown>) => {
      confirmDialog(rec);
    };

    const onEditClick = async (rec: Record<string, unknown>) => {
      const supp = await SuppliesApi.getAll()
      let itemSupplies: any;
      if(supp.data.list){
        itemSupplies = supp.data.list;
      }
      arrSupplies.value = itemSupplies;

      const sup = await SupplierApi.getAll()
      let itemSupplier: any;
      if(supp.data.list){
        itemSupplier = sup.data.list;
      }
      arrSupplier.value = itemSupplier;

      showSlideOut.value = true;
      selectedRec.value = rec;
    };

    onMounted(() => {
      getData(0, pageSize.value);
    });

    return {
      list,
      arrSupplies,
      arrSupplier,
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
    };
  },
  components: {
    ReceiptFlowDetails,
  },
});
</script>
