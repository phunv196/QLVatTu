<template>
  <div>
    <ConfirmDialog position="top"></ConfirmDialog>
    <Toast />
    <Sidebar
      v-model:visible="showSlideOut"
      position="right"
      style="width: 1000px"
    >
      <SupplierDetails
        :rec="selectedRec"
        @cancel="showSlideOut = false"
        @changed="getData()"
        :isNew="isNewRec"
      ></SupplierDetails>
    </Sidebar>
    <Dialog v-model:visible="showDialog" style="width: 1000px; height: 650px">
      <template #header>
        <h3>Import nhà cung cấp</h3>
      </template>
      <Import @cancel="showDialog = false" @changed="getData()"> </Import>
    </Dialog>
    <h3>Quản lý nhà cung cấp</h3>
    <div class="p-d-flex p-flex-row p-mb-3 p-jc-around" style="width: 1000px">
      <div>
        <label
          class="p-d-inline-block m-label-size-4 p-text-left p-mr-1"
          style="padding-top: 7px"
          >Mã nhà cung cấp
        </label>
        <InputText
          type="text"
          v-model="searchCode"
          class="p-inputtext-sm"
          style="width: 200px; height: 30px; margin: 1px 0px 0 0px"
        />
      </div>
      <div>
        <label
          class="p-d-inline-block m-label-size-4 p-text-left p-mr-1"
          style="padding-top: 7px"
          >Tên nhà cung cấp
        </label>
        <InputText
          type="text"
          v-model="searchName"
          class="p-inputtext-sm"
          style="width: 200px; height: 30px; margin: 1px 0px 0 0px"
        />
      </div>
    </div>
    <div class="p-d-flex p-flex-row p-mb-3 p-jc-around" style="width: 1000px">
      <div>
        <label
          class="p-d-inline-block m-label-size-4 p-text-left p-mr-1"
          style="padding-top: 7px"
          >Email
        </label>
        <InputText
          type="text"
          v-model="searchEmail"
          class="p-inputtext-sm"
          style="width: 200px; height: 30px; margin: 1px 0px 0 0px"
        />
      </div>
      <div>
        <label
          class="p-d-inline-block m-label-size-4 p-text-left p-mr-1"
          style="padding-top: 7px"
          >Số điện thoại
        </label>
        <InputText
          type="text"
          v-model="searchPhone"
          class="p-inputtext-sm"
          style="width: 200px; height: 30px; margin: 1px 0px 0 0px"
        />
      </div>
    </div>
    <div
      class="p-d-flex p-flex-row p-mb-3 p-jc-center"
      style="width: 1000px; margin: 20px 0"
    >
      <Button
        icon="pi pi-upload"
        iconPos="right"
        label="Import"
        @click="showImport()"
        class="p-ml-1 p-button-sm"
      ></Button>
      <Button
        icon="pi pi-download"
        iconPos="right"
        label="Báo cáo"
        @click="exportExcell()"
        class="p-ml-1 p-button-sm"
      ></Button>
      <Button
        icon="pi pi-search"
        iconPos="right"
        label="Tìm kiếm"
        @click="onSearchKeyup()"
        class="p-ml-1 p-button-sm"
      ></Button>
      <Button
        icon="pi pi-user"
        iconPos="right"
        label="ADD"
        @click="onAddClick()"
        class="p-ml-1 p-button-sm"
      ></Button>
    </div>
    <DataTable
      :value="list"
      :paginator="true"
      :lazy="true"
      :rows="pageSize"
      :totalRecords="totalRecs"
      :loading="isLoading"
      stripedRows
      showGridlines
      @page="onPageChange($event)"
      class="p-datatable-sm p-datatable-hoverable-rows m-border p-mb-4"
      style="width: 1000px"
    >
      <Column
        field="index"
        header="STT"
        headerStyle="width:90px;"
        bodyStyle="text-align-last: center;"
      ></Column>
      <Column
        field="code"
        header="Mã nhà cung cấp"
        headerStyle="width:90px"
      ></Column>
      <Column
        field="name"
        header="Tên nhà cung cấp"
        headerStyle="width:160px"
      ></Column>
      <Column field="email" header="Email" headerStyle="width:160px"></Column>
      <Column
        field="phone"
        header="Điện thoại"
        headerStyle="width:160px"
      ></Column>
      <Column
        field="address"
        header="Địa chỉ"
        headerStyle="width:160px"
      ></Column>
      <!--      <Column field="description" header="Ghi chú" headerStyle="width:160px"></Column>-->
      <Column header="ACTION" headerStyle="width:100px" bodyStyle="padding:3px; text-align: center;">
        <template #body="slotProps">
          <Button
            icon="pi pi-pencil"
            @click="onEditClick(slotProps.data)"
            class="
              p-button-sm p-button-rounded p-button-secondary p-button-text
            "
          />
          <Button
            icon="pi pi-trash"
            @click="onDeleteClick(slotProps.data)"
            class="p-button-sm p-button-rounded p-button-danger p-button-text"
          />
        </template>
      </Column>
    </DataTable>
  </div>
</template>


<script lang='ts'>
import { ref, onMounted, defineComponent } from "vue";
import SupplierApi from "@/api/material-management/supplier-api"; // eslint-disable-line import/no-cycle
import SupplierDetails from "@/views/material-management/supplier/SupplierDetails.vue";
import Import from "@/views/material-management/supplier/Import.vue";
import { useConfirm } from "primevue/useconfirm";
import { useToast } from "primevue/usetoast";
import { debounce, exportFile } from "@/shared/utils";

export default defineComponent({
  setup(): unknown {
    const searchName = ref("");
    const searchCode = ref("");
    const searchEmail = ref("");
    const searchPhone = ref("");
    const isLoading = ref(false);
    const showSlideOut = ref(false);
    const showDialog = ref(false);
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

    const getData = async (
      page: number,
      requestedPageSize: number,
      supplierId = "",
      code = "",
      name = "",
      email = "",
      phone = ""
    ) => {
      // isLoading.value = true;
      try {
        const resp = await SupplierApi.getSupplier(
          page,
          requestedPageSize,
          supplierId,
          code,
          name,
          email,
          phone
        );
        let i = 1;
        list.value = resp.data.list.map((v: Record<string, unknown>) => {
          let index = 1;
          if (page > 1) {
            index = 10 * (currentPage - 1) + i++;
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
        console.log("REST ERROR: %O", err.response ? err.response : err);
        isLoading.value = false;
      }
    };

    const confirmDialog = (rec: Record<string, unknown>) => {
      confirm.require({
        message: `Do you want to remove ${rec.name} from product catalog ?`,
        header: "Remove",
        icon: "pi pi-question-circle",
        acceptIcon: "pi pi-check",
        accept: async () => {
          try {
            const resp = await SupplierApi.deleteSupplier(
              rec.supplierId as string
            );
            if (resp.data.msgType === "SUCCESS") {
              getData(currentPage, pageSize.value);
              toast.add({
                severity: "success",
                summary: "Successfully Deleted",
                life: 3000,
              });
            } else {
              toast.add({
                severity: "error",
                summary: "Access Denied",
                detail: resp.data.msg,
                life: 3000,
              });
            }
          } catch (e) {
            toast.add({
              severity: "error",
              summary: "Error",
              detail: "Unable to connect to server",
              life: 3000,
            });
          }
        },
        reject: () => {
          console.log("NO");
        },
      });
    };

    const onPageChange = (event: Record<string, unknown>) => {
      if (currentPage !== (event.page as number) + 1) {
        currentPage = (event.page as number) + 1;
        getData(currentPage, pageSize.value);
      }
    };

    const onAddClick = () => {
      isNewRec.value = true;
      selectedRec.value = { supplierId: "" };
      showSlideOut.value = true;
    };

    const onDeleteClick = (rec: Record<string, unknown>) => {
      confirmDialog(rec);
    };

    const onEditClick = async (rec: Record<string, unknown>) => {
      showSlideOut.value = true;
      selectedRec.value = rec;
    };

    onMounted(() => {
      getData(0, pageSize.value);
    });

    const onSearchKeyup = debounce(
      () =>
        getData(
          1,
          pageSize.value,
          "",
          `${searchCode.value}`,
          `${searchName.value}`,
          `${searchEmail.value}`,
          `${searchPhone.value}`
        ),
      400
    );

    const exportExcell = async () => {
      await SupplierApi.export(
        `${searchCode.value}`,
        `${searchName.value}`,
        `${searchEmail.value}`,
        `${searchPhone.value}`
      ).then((res) => {
        const data = res.data.data;
        exportFile(data.data, data.fileName);
      });
    };

    const showImport = () => {
      showDialog.value = true;
    };

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
      onSearchKeyup,
      exportExcell,
      showImport,
      showDialog,
    };
  },
  components: {
    SupplierDetails,
    Import,
  },
});
</script>
