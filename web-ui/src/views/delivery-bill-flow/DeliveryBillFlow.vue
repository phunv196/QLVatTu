<template>
  <div>
    <Toast/>
    <Sidebar v-model:visible="showSlideOut" position="right" style="width:700px">
      <DeliveryBillFlowDetails :rec="selectedRec" @cancel="showSlideOut = false" @changed="getData()"
                               :arrSupplies="arrSupplies" :isNew="isNewRec"></DeliveryBillFlowDetails>
    </Sidebar>
    <h3> Danh sách vật tư xuất </h3>
    <div class="p-d-flex p-flex-row p-mb-3" style="width:1000px" v-if="$store.getters.role === 'ADMIN'">
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
      stripedRows showGridlines
      @page="onPageChange($event)"
      class="p-datatable-sm p-datatable-hoverable-rows m-border p-mb-4" style="width:1000px; line-height: 1.3rem; word-wrap: break-word;">
      <Column field="index" header="STT" headerStyle="width:50px;" bodyStyle="text-align-last: center;"></Column>
      <Column field="suppliesCode" header="Mã vật tư" headerStyle="width:90px"></Column>
      <Column field="suppliesName" header="Tên vật tư" headerStyle="width:160px"></Column>
      <Column field="speciesName" header="Chủng loại" headerStyle="width:160px"></Column>
      <Column field="amount" header="Số lượng" headerStyle="width:90px"></Column>
      <Column field="suppliesUnit" header="Đơn vị tính" headerStyle="width:90px"></Column>
      <Column field="suppliesPrice" header="Giá" headerStyle="width:90px"></Column>
      <Column field="calculatePrice" header="Thành tiền" headerStyle="width:90px"></Column>
      <Column header="ACTION" headerStyle="width:100px" bodyStyle="padding:3px;text-align: center;">
        <template #body="slotProps">
          <template  v-if="$store.getters.role === 'ADMIN'">
            <Button icon="pi pi-pencil" @click="onEditClick(slotProps.data)"
                  class="p-button-sm p-button-rounded p-button-secondary p-button-text"/>
            <Button icon="pi pi-trash" @click="onDeleteClick(slotProps.data)"
                  class="p-button-sm p-button-rounded p-button-danger p-button-text"/>
          </template>
          <template v-else>
            <Button
              icon="pi pi-eye"
              @click="onEditClick(slotProps.data)"
              class="p-button-sm p-button-rounded p-button-secondary p-button-text"/>
          </template>
        </template>
      </Column>
    </DataTable>
  </div>
</template>


<script lang='ts'>
import { ref, onMounted, defineComponent } from 'vue';
import DeliveryBillFlowApi from '@/api/delivery-bill-flow-api'; // eslint-disable-line import/no-cycle
import SuppliesApi from '@/api/supplies-api'
import DeliveryBillFlowDetails from '@/views/delivery-bill-flow/DeliveryBillFlowDetails.vue';
import { useConfirm } from 'primevue/useconfirm';
import { useToast } from 'primevue/usetoast';

export default defineComponent({
  props: {
    requence: {},
    warehouseId: Number
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
    const confirm = useConfirm();
    const toast = useToast();
    let currentPage = 1;
    const deliveryBillId = ref(JSON.parse(JSON.stringify(props.requence)));
    const warehouseId = ref(JSON.parse(JSON.stringify(props.warehouseId)));
    const getData = async (page: number, requestedPageSize: number, deliveryBillFlowId = '') => {
      // isLoading.value = true;
      try {
        const resp = await DeliveryBillFlowApi.getDeliveryBillFlows(page, requestedPageSize, deliveryBillId.value);
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
      } catch (err:any) {
        console.log('REST ERROR: %O', err.response ? err.response : err);
        isLoading.value = false;
      }
    };

    const confirmDialog = (rec: Record<string, unknown>) => {
      confirm.require({
        message: `Bạn có muốn xóa bản ghi ${rec.code} không ?`,
        header: "Xác nhận!",
        icon: 'pi pi-question-circle',
        acceptIcon: 'pi pi-check',
        accept: async () => {
          try {
            const resp = await DeliveryBillFlowApi.deleteDeliveryBillFlow(rec.deliveryBillFlowId as string);
            if (resp.data.msgType === 'SUCCESS') {
              getData(currentPage, pageSize.value);
              toast.add({
                severity: 'success',
                summary: 'Xóa bản ghi thành công!',
                life: 3000
              });
            } else {
              toast.add({
                severity: 'error',
                summary: 'Quyền truy cập bị Từ chối!',
                detail: resp.data.msg,
                life: 3000
              });
            }
          } catch (e) {
            toast.add({
              severity: 'error',
              summary: 'Lỗi xảy ra!',
              detail: 'Vui lòng liên hệ quản trị viên!',
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
      const supp = await SuppliesApi.getByWarehouseId(warehouseId.value)
      let itemSupplies: any;
      if(supp.data.list){
        itemSupplies = supp.data.list;
      }
      arrSupplies.value = itemSupplies;
      isNewRec.value = true;
      selectedRec.value = { deliveryBillFlowId: '' , deliveryBillId : deliveryBillId, };
      showSlideOut.value = true;
    };

    const onDeleteClick = (rec: Record<string, unknown>) => {
      confirmDialog(rec);
    };

    const onEditClick = async (rec: Record<string, unknown>) => {
      const supp = await SuppliesApi.getByWarehouseId(warehouseId.value)
      let itemSupplies: any;
      if(supp.data.list){
        itemSupplies = supp.data.list;
      }
      arrSupplies.value = itemSupplies;
      showSlideOut.value = true;
      selectedRec.value = rec;
    };

    onMounted(() => {
      getData(0, pageSize.value);
    });

    return {
      list,
      arrSupplies,
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
    DeliveryBillFlowDetails,
  },
});
</script>
