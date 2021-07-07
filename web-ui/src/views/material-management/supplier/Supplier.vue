<template>
  <div>
    <ConfirmDialog position="top"></ConfirmDialog>
    <Toast/>
    <Sidebar v-model:visible="showSlideOut" position="right" style="width:700px">
      <SupplierDetails :rec="selectedRec" @cancel="showSlideOut = false" @changed="getData()"
                   :isNew="isNewRec"></SupplierDetails>
    </Sidebar>
    <h3> Quản lý nhà cung cấp </h3>
    <div class="p-d-flex p-flex-row p-mb-1" style="width:1000px">
      <span class="p-input-icon-left">
        <i class="pi pi-search"  style="margin: -6px 10px 0px;"/>
        <InputText type="text" v-model="searchCode" class="p-inputtext-sm" placeholder="Search by code" style="width:180px;margin:1px 10px 0 10px"/>
      </span>
      <span class="p-input-icon-left">
        <i class="pi pi-search" style="margin: -6px 10px 0px;" />
        <InputText type="text" v-model="searchName" class="p-inputtext-sm" placeholder="Search by name" style="width:180px;margin:1px 10px 0 10px"/>
      </span>
      <span class="p-input-icon-left">
        <i class="pi pi-search"  style="margin: -6px 10px 0px;"/>
        <InputText type="text" v-model="searchEmail" class="p-inputtext-sm" placeholder="Search by email" style="width:180px;margin:1px 10px 0 10px"/>
      </span>
      <span class="p-input-icon-left">
        <i class="pi pi-search" style="margin: -6px 10px 0px;" />
        <InputText type="text" v-model="searchPhone" class="p-inputtext-sm" placeholder="Search by phone" style="width:180px;margin:1px 10px 0 10px"/>
      </span>
      <div style="display:inline-block; flex:1"></div>
      <Button icon="pi pi-search" iconPos="right" label="Tìm kiếm" @click="onSearchKeyup()"
              class="p-ml-1 p-button-sm"></Button>
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
      <Column field="supplierId" header="ID nhà sản xuất" headerStyle="width:90px;"></Column>
      <Column field="code" header="Mã nhà cung cấp" headerStyle="width:90px"></Column>
      <Column field="name" header="Tên nhà cung cấp" headerStyle="width:160px"></Column>
      <Column field="email" header="Email" headerStyle="width:160px"></Column>
      <Column field="phone" header="Điện thoại" headerStyle="width:160px"></Column>
      <Column field="address" header="Địa chỉ" headerStyle="width:160px"></Column>
<!--      <Column field="description" header="Ghi chú" headerStyle="width:160px"></Column>-->
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
import SupplierApi from '@/api/material-management/supplier-api'; // eslint-disable-line import/no-cycle
import SupplierDetails from '@/views/material-management/supplier/SupplierDetails.vue';
import { useConfirm } from 'primevue/useconfirm';
import { useToast } from 'primevue/usetoast';
import { debounce } from '@/shared/utils';

export default defineComponent({
  setup(): unknown {
    const searchName = ref('');
    const searchCode = ref('');
    const searchEmail = ref('');
    const searchPhone = ref('');
    const isLoading = ref(false);
    const showSlideOut = ref(false);
    const pageSize = ref(10);
    const totalPages = ref(0);
    const totalRecs = ref(0);
    const selectedRec = ref({});
    const isNewRec = ref(false);
    const isCustomer = ref(false);
    const list = ref([]);
    const confirm = useConfirm();
    const toast = useToast();
    let currentPage = 1;

    const getData = async (page: number, requestedPageSize: number, supplierId = '',code='', name='', email='', phone='') => {
      // isLoading.value = true;
      try {
        debugger
        const resp = await SupplierApi.getSupplier(page, requestedPageSize, supplierId,code,name,email,phone);
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
      debugger;
      confirm.require({
        message: `Do you want to remove ${rec.name} from product catalog ?`,
        header: 'Remove',
        icon: 'pi pi-question-circle',
        acceptIcon: 'pi pi-check',
        accept: async () => {
          try {
            const resp = await SupplierApi.deleteSupplier(rec.supplierId as string);
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

    const onAddClick = () => {
      isNewRec.value = true;
      selectedRec.value = { supplierId: '' };
      showSlideOut.value = true;
    };

    const onDeleteClick = (rec: Record<string, unknown>) => {
      debugger;
      confirmDialog(rec);
    };

    const onEditClick = async (rec: Record<string, unknown>) => {
      showSlideOut.value = true;
      selectedRec.value = rec;
    };

    onMounted(() => {
      getData(0, pageSize.value);
    });

    const onSearchKeyup = debounce(() => getData(
      1, pageSize.value, '', `${searchCode.value}`,
      `${searchName.value}`, `${searchEmail.value}`, `${searchPhone.value}` ), 400);

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
      searchName,
      searchCode,
      searchEmail,
      searchPhone,
      onSearchKeyup
    };
  },
  components: {
    SupplierDetails,
  },
});
</script>
