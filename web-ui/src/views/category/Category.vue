<template>
  <div>
    <ConfirmDialog position="top"></ConfirmDialog>
    <Toast />
    <Sidebar
      v-model:visible="showSlideOut"
      position="right"
      style="width: 700px"
    >
      <CategoryDetails
        :rec="selectedRec"
        @cancel="showSlideOut = false"
        @changed="getData(); lstCategory();"
        :isNew="isNewRec"
      ></CategoryDetails>
    </Sidebar>
    <h3>Quản lý danh mục</h3>
    <div class="p-d-flex p-flex-row p-mb-3 p-jc-around" style="width: 1200px">
      <div>
        <label
          class="p-d-inline-block m-label-size-3 p-text-left p-mr-1"
          style="padding-top: 7px"
          >Mã danh mục
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
          class="p-d-inline-block m-label-size-3 p-text-left p-mr-1"
          style="padding-top: 7px"
          >Tên danh mục
        </label>
        <InputText
          type="text"
          v-model="searchName"
          class="p-inputtext-sm"
          style="width: 200px; height: 30px; margin: 1px 0px 0 0px"
        />
      </div>
      <div>
        <label
          class="p-d-inline-block m-label-size-4 p-text-left p-mr-1"
          style="padding-top: 7px"
        >Nhóm danh mục
        </label>
        <Dropdown
          class="p-inputtext-sm"
          style="width: 200px"
          v-model="searchParentCode"
          :options="categorys"
          :filter="true"
          :showClear="true"
          optionLabel="name"
          optionValue="code"
          placeholder="--Hãy chọn--"
        />
      </div>
    </div>
    <div
      class="p-d-flex p-flex-row p-mb-3 p-jc-center"
      style="width: 1000px; margin: 20px 0"
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
      stripedRows showGridlines
      @page="onPageChange($event)"
      class="p-datatable-sm p-datatable-hoverable-rows m-border p-mb-4"
      style="width: 1200px; line-height: 1.3rem; word-wrap: break-word;"
    >
      <Column field="index" header="STT" headerStyle="width:90px;" bodyStyle="text-align-last: center;"></Column>
      <Column
        field="code"
        header="Mã danh mục"
        headerStyle="width:90px"
      ></Column>
      <Column
        field="name"
        header="Tên danh mục"
        headerStyle="width:160px"
      ></Column>
      <Column
        field="parentCode"
        header="Mã nhóm danh mục"
        headerStyle="width:160px"
      ></Column>
      <Column
        field="description"
        header="Ghi chú"
        headerStyle="width:160px"
      ></Column>
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
import CategoryApi from "@/api/category-api"; // eslint-disable-line import/no-cycle
import CategoryDetails from "@/views/category/CategoryDetails.vue";
import { useConfirm } from "primevue/useconfirm";
import { useToast } from "primevue/usetoast";
import { debounce } from "@/shared/utils";
import { async } from "rxjs";

export default defineComponent({
  setup(): unknown {
    const searchName = ref("");
    const searchCode = ref("");
    const searchParentCode = ref(null);
    const isLoading = ref(false);
    const showSlideOut = ref(false);
    const pageSize = ref(10);
    const totalPages = ref(0);
    const totalRecs = ref(0);
    const selectedRec = ref({});
    const isNewRec = ref(false);
    const isCustomer = ref(false);
    const list = ref([]);
    const categorys = ref([]);
    const confirm = useConfirm();
    const toast = useToast();
    let currentPage = 1;

    const getData = async (
      page: number,
      requestedPageSize: number,
      categoryId = "",
      code = "",
      name = "",
      parentCode = ""
    ) => {
      // isLoading.value = true;
      try {
        const resp = await CategoryApi.getCategorys(
          page,
          requestedPageSize,
          categoryId,
          code,
          name,
          parentCode
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
      } catch (err:any) {
        console.log("REST ERROR: %O", err.response ? err.response : err);
        isLoading.value = false;
      }
    };

    const confirmDialog = (rec: Record<string, unknown>) => {
      confirm.require({
        message: `Bạn có muốn xóa bản ghi ${rec.code} không ?`,
        header: "Xác nhận!",
        icon: "pi pi-question-circle",
        acceptIcon: "pi pi-check",
        accept: async () => {
          try {
            const resp = await CategoryApi.deleteCategory(
              rec.categoryId as string
            );
            if (resp.data.msgType === "SUCCESS") {
              getData(currentPage, pageSize.value);
              lstCategory();
              toast.add({
                severity: "success",
                summary: "Xóa bản ghi thành công!",
                life: 3000,
              });
            } else {
              toast.add({
                severity: "error",
                summary: "Quyền truy cập bị từ chối!",
                detail: resp.data.msg,
                life: 3000,
              });
            }
          } catch (e) {
            toast.add({
              severity: "error",
              summary: "Lỗi xảy ra!",
              detail: "Vui lòng liên hệ với quản trị viên!",
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
      selectedRec.value = { categoryId: "" };
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
      lstCategory();
    });

    const onSearchKeyup = debounce(
      async () =>
        await getData(
          1,
          pageSize.value,
          "",
          `${searchCode.value}`,
          `${searchName.value}`,
          `${searchParentCode.value}`
        ),
      400
    );

    const lstCategory = async () => {
      const resp = await CategoryApi.getListIsNotParentCode();
      let lstCategorys = [];
      if (resp.data) {
        lstCategorys = resp.data.list;
      }
      categorys.value = lstCategorys;
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
      lstCategory,
      searchName,
      searchCode,
      searchParentCode,
      onSearchKeyup,
      categorys
    };
  },
  components: {
    CategoryDetails,
  },
});
</script>
