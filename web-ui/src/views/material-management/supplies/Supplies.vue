<template>
  <div>
    <ConfirmDialog position="top"></ConfirmDialog>
    <Toast />
    <Sidebar
      v-model:visible="showSlideOut"
      position="right"
      style="width: 700px"
    >
      <SuppliesDetails
        :rec="selectedRec"
        @cancel="showSlideOut = false"
        @changed="getData()"
        :arrQuality="arrQuality"
        :arrSpecies="arrSpecies"
        :arrSupplier="arrSupplier"
        :item="item"
        :isNew="isNewRec"
      ></SuppliesDetails>
    </Sidebar>
    <h3>Quản lý vật tư</h3>
    <div class="p-d-flex p-flex-row p-mb-1 p-jc-around" style="width: 1350px">
      <div>
        <label
          class="p-d-inline-block m-label-size-3 p-text-left p-mr-1"
          style="padding-top: 7px"
          >Tên vật tư
        </label>
        <span class="p-input-icon-left">
          <i class="pi pi-search" style="margin: -6px 10px 0px" />
          <InputText
            type="text"
            v-model="searchCode"
            class="p-inputtext-sm"
            placeholder="Search by code"
            style="width: 200px; height: 30px; margin: 1px 0px 0 0px"
          />
        </span>
      </div>
      <div>
        <label
          class="p-d-inline-block m-label-size-3 p-text-left p-mr-1"
          style="padding-top: 7px"
          >Mã vật tư
        </label>
        <span class="p-input-icon-left">
          <i class="pi pi-search" style="margin: -6px 10px 0px" />
          <InputText
            type="text"
            v-model="searchName"
            class="p-inputtext-sm"
            placeholder="Search by name"
            style="width: 200px; height: 30px; margin: 1px 0px 0 0px"
          />
        </span>
      </div>
      <div>
        <label
          class="p-d-inline-block m-label-size-3 p-text-left p-mr-1"
          style="padding-top: 7px"
          >Nhà cung cấp
        </label>
        <Dropdown
          class="p-inputtext-sm"
          style="width: 200px"
          v-model="searchSupplier"
          :options="[]"
          :filter="true"
          :showClear="true"
          optionLabel="name"
          optionValue="supplierId"
        />
      </div>
      <div>
        <label
          class="p-d-inline-block m-label-size-3 p-text-left p-mr-1"
          style="padding-top: 7px"
          >Chủng loại
        </label>
        <Dropdown
          class="p-inputtext-sm"
          style="width: 200px"
          v-model="searchSupplier"
          :options="[]"
          :filter="true"
          :showClear="true"
          optionLabel="name"
          optionValue="supplierId"
        />
      </div>
    </div>
    <div class="p-d-flex p-flex-row p-mb-1 p-jc-around" style="width: 1350px">
      <div>
        <label
          class="p-d-inline-block m-label-size-3 p-text-left p-mr-1"
          style="padding-top: 7px"
          >Giá từ
        </label>
        <span class="p-input-icon-left">
          <i class="pi pi-search" style="margin: -6px 10px 0px" />
          <InputText
            type="text"
            v-model="searchFormPrice"
            class="p-inputtext-sm"
            style="width: 200px; height: 30px"
          />
        </span>
      </div>
      <div>
        <label
          class="p-d-inline-block m-label-size-3 p-text-left p-mr-1"
          style="padding-top: 7px"
          >Đến
        </label>
        <span class="p-input-icon-left">
          <i class="pi pi-search" style="margin: -6px 10px 0px" />
          <InputText
            type="text"
            v-model="searchToPrice"
            class="p-inputtext-sm"
            style="width: 200px; height: 30px"
          />
        </span>
      </div>
      <div>
        <label
          class="p-d-inline-block m-label-size-3 p-text-left p-mr-1"
          style="padding-top: 7px"
          >Chất lượng
        </label>
        <Dropdown
          class="p-inputtext-sm"
          style="width: 200px"
          v-model="searchSupplier"
          :options="[]"
          :filter="true"
          :showClear="true"
          optionLabel="name"
          optionValue="supplierId"
        />
      </div>
      <div>
        <label
          class="p-d-inline-block m-label-size-3 p-text-left p-mr-1"
          style="padding-top: 7px"
          >Đơn vị tính
        </label>
        <Dropdown
          class="p-inputtext-sm"
          style="width: 200px"
          v-model="searchSupplier"
          :options="[]"
          :filter="true"
          :showClear="true"
          optionLabel="name"
          optionValue="supplierId"
        />
      </div>
    </div>
    <div
      class="p-d-flex p-flex-row p-mb-1 p-jc-center"
      style="width: 1350px; margin: 20px 0"
    >
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
      @page="onPageChange($event)"
      class="p-datatable-sm p-datatable-hoverable-rows m-border p-mb-4"
      style="width: 1350px"
    >
      <Column
        field="suppliesId"
        header="ID vật tư"
        headerStyle="width:90px;"
      ></Column>
      <Column field="code" header="Mã vật tư" headerStyle="width:90px"></Column>
      <Column
        field="name"
        header="Tên vật tư"
        headerStyle="width:160px"
      ></Column>
      <Column
        field="speciesName"
        header="Chủng loại"
        headerStyle="width:160px"
      ></Column>
      <Column
        field="supplierName"
        header="Nhà cung cấp"
        headerStyle="width:160px"
      ></Column>
      <Column
        field="qualityName"
        header="Chất lượng"
        headerStyle="width:160px"
      ></Column>
      <Column
        field="unit"
        header="Đơn vị tính"
        headerStyle="width:160px"
      ></Column>
      <Column
        field="price"
        header="Giá vật tư"
        headerStyle="width:160px"
      ></Column>
      <Column header="ACTION" headerStyle="width:100px" bodyStyle="padding:3px">
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
import SuppliesApi from "@/api/material-management/supplies-api"; // eslint-disable-line import/no-cycle
import SuppliesDetails from "@/views/material-management/supplies/SuppliesDetails.vue";
import { useConfirm } from "primevue/useconfirm";
import { useToast } from "primevue/usetoast";
import SupplierApi from "@/api/material-management/supplier-api";
import QualityApi from "@/api/material-management/quality-api";
import SpeciesApi from "@/api/material-management/species-api";

export default defineComponent({
  setup(): unknown {
    const isLoading = ref(false);
    const showSlideOut = ref(false);
    const pageSize = ref(10);
    const totalPages = ref(0);
    const totalRecs = ref(0);
    const selectedRec = ref({});
    const item = ref({});
    const isNewRec = ref(false);
    const isCustomer = ref(false);
    const list = ref([]);
    const arrQuality = ref([]);
    const arrSpecies = ref([]);
    const arrSupplier = ref([]);
    const confirm = useConfirm();
    const toast = useToast();
    let currentPage = 1;

    const getData = async (
      page: number,
      requestedPageSize: number,
      suppliesId = "",
      qualityId = ""
    ) => {
      // isLoading.value = true;
      try {
        debugger;
        const resp = await SuppliesApi.getSupplies(
          page,
          requestedPageSize,
          suppliesId
        );
        list.value = resp.data.list;
        // list.value = resp.data.list.map((v:Record<string, unknown>) => {
        //   const dt1 = new Date(v.dateConstruction as string);
        //   const dt2 = new Date(v.dateFinish as string);
        //   const strDateConstruction = new Intl.DateTimeFormat(['ban', 'id'], { year: 'numeric', month: '2-digit', day: '2-digit' }).format(dt1);
        //   const strDateFinish = new Intl.DateTimeFormat(['ban', 'id'], { year: 'numeric', month: '2-digit', day: '2-digit' }).format(dt2);
        //   return {
        //     ...v,
        //     strDateConstruction,
        //     strDateFinish,
        //   };
        // });
        debugger;
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
      debugger;
      confirm.require({
        message: `Do you want to remove ${rec.name} from product catalog ?`,
        header: "Remove",
        icon: "pi pi-question-circle",
        acceptIcon: "pi pi-check",
        accept: async () => {
          try {
            const resp = await SuppliesApi.deleteSupplies(
              rec.suppliesId as string
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

    const onAddClick = async () => {
      debugger;
      const resQulity = await QualityApi.getAll();
      let qualityItem: any;
      if (resQulity.data) {
        qualityItem = resQulity.data.list;
      }
      arrQuality.value = qualityItem;

      const resSpecies = await SpeciesApi.getAll();
      let speciesItem: any;
      if (resSpecies.data) {
        speciesItem = resSpecies.data.list;
      }
      arrSpecies.value = speciesItem;

      const Supplier = await SupplierApi.getAll();
      let supplierItem: any;
      if (Supplier.data) {
        supplierItem = Supplier.data.list;
      }
      arrSupplier.value = supplierItem;

      isNewRec.value = true;
      selectedRec.value = { suppliesId: "" };
      showSlideOut.value = true;
    };

    const onDeleteClick = (rec: Record<string, unknown>) => {
      debugger;
      confirmDialog(rec);
    };

    const onEditClick = async (rec: Record<string, unknown>) => {
      debugger;
      const resQulity = await QualityApi.getAll();
      let qualityItem: any;
      if (resQulity.data) {
        qualityItem = resQulity.data.list;
      }
      arrQuality.value = qualityItem;

      const resSpecies = await SpeciesApi.getAll();
      let speciesItem: any;
      if (resSpecies.data) {
        speciesItem = resSpecies.data.list;
      }
      arrSpecies.value = speciesItem;

      const Supplier = await SupplierApi.getAll();
      let supplierItem: any;
      if (Supplier.data) {
        supplierItem = Supplier.data.list;
      }
      arrSupplier.value = supplierItem;
      showSlideOut.value = true;
      selectedRec.value = rec;
    };

    onMounted(() => {
      getData(0, pageSize.value);
    });

    return {
      list,
      arrQuality,
      arrSpecies,
      arrSupplier,
      item,
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
    SuppliesDetails,
  },
});
</script>
