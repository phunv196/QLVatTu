<template>
  <div class="m-font-regular">
    <Toast />
    <div>
      <FileUpload
        name="demo[]"
        url="./upload"
        :maxFileSize="1000000"
        :fileLimit="1"
        :showUploadButton="false"
        :showCancelButton="false"
        :customUpload="true"
        @select="importFile($event)"
      />
    </div>
    <div class="p-mt-2 p-d-flex p-flex-row p-jc-center" style="width: 100%">
      <Button
        icon="pi pi-download"
        iconPos="left"
        label="DownloadTemplate"
        @click="downloadTemplate()"
        class="p-button-sm"
      ></Button>
      <Button
        v-if="$store.getters.role === 'ADMIN'"
        icon="pi pi-send"
        iconPos="left"
        label="Import"
        @click="uploadFile()"
        class="p-button-sm p-ml-2"
      ></Button>
      <Button
        label="CANCEL"
        @click="$emit('cancel')"
        class="p-button-sm p-button-outlined p-ml-2"
      ></Button>
    </div>
    <div class="p-mt-5" v-if="showError">
      <DataTable
        :value="list"
        class="p-datatable-sm p-datatable-hoverable-rows m-border p-mb-4"
        stripedRows
        showGridlines
        scrollable="true"
        scrollHeight="200px"
      >
        <Column
          field="index"
          header="STT"
          headerStyle="max-width:70px; justify-content: center;;"
          bodyStyle="max-width:70px; justify-content: center;"
        ></Column>
        <Column
          field="row"
          header="Hàng"
          headerStyle="max-width:70px; justify-content: center;"
          bodyStyle="max-width:70px; justify-content: center;"
        ></Column>
        <Column
          field="columnLabel"
          header="Cột"
          headerStyle="max-width:70px; justify-content: center;"
          bodyStyle="max-width:70px; justify-content: center;"
        ></Column>
        <Column
          field="description"
          header="Lỗi"
          headerStyle="max-width:550px; justify-content: center;"
          bodyStyle="max-width:550px;"
        ></Column>
        <Column
          field="content"
          header="Giá trị"
          headerStyle="max-width:200px; justify-content: center;"
          bodyStyle="max-width:200px;"
        ></Column>
      </DataTable>
    </div>
  </div>
</template>

<script lang='ts'>
import { defineComponent, onMounted, ref } from "vue";
import { useToast } from "primevue/usetoast";
import { exportFile } from "@/shared/utils";
import usersApi from "@/api/users-api";

export default defineComponent({
  props: {},
  setup(props, { emit }): unknown {
    const toast = useToast();
    const list = ref([]);
    const showError = ref(false);
    const onCancel = () => {
      emit("cancel");
    };

    const fileImport: Record<string, number | string> = {};

    const importFile = (event: any) => {
      fileImport.fileName = event.files[0].name;
      getBase64(event.files[0]).then((res) => {
        let data: any = res;
        fileImport.data = data.slice(37, data.length);
      });
    };

    const getBase64 = (file: any) => {
      return new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = () => resolve(reader.result);
        reader.onerror = (error) => reject(error);
      });
    };

    const uploadFile = () => {
      if (fileImport.data) {
        usersApi.uploadFile(fileImport).then((res) => {
          if (res.data.msgType === "SUCCESS") {
            toast.add({
              severity: "success",
              summary: "Thành công!",
              detail: res.data.msg,
              life: 3000,
            });
            emit("change");
            emit("cancel");
          } else {
            if (res.data.msgType === "ERROR") {
              toast.add({
                severity: "error",
                summary: "Lỗi xảy ra!",
                detail: res.data.msg,
                life: 3000,
              });
            } else {
              toast.add({
                severity: "error",
                summary: "Import thất bại",
                detail:
                  "Dữ liệu import bị lỗi. Vui lòng sửa file và import lại!",
                life: 3000,
              });
              showError.value = true;
              let i = 1;
              list.value = res.data.map((v: Record<string, unknown>) => {
                let index = 1;
                index = i++;
                return {
                  ...v,
                  index,
                };
              });
            }
          }
        });
      } else {
        toast.add({
          severity: "error",
          summary: "Lỗi xảy ra!",
          detail: "Vui lòng chọn file!",
          life: 3000,
        });
      }
    };

    const downloadTemplate = () => {
      usersApi.downloadTemplate().then((res) => {
        const data = res.data.data;
        exportFile(data.data, data.fileName);
      });
    };

    return {
      onCancel,
      importFile,
      downloadTemplate,
      uploadFile,
      list,
      showError,
    };
  },
});
</script>
