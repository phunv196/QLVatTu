<template>
  <div class="m-font-regular">
    <Toast />
    <h4>
      Danh mục #
      <span style="color: var(--primary-color)">
        {{ recData.categoryId ? recData.categoryId : "NEW" }}
      </span>
    </h4>
    <transition name="p-message">
      <Message v-if="showMessage" severity="error" @close="showMessage = false">
        {{ userMessage }}</Message
      >
    </transition>
    <div>
      <div class="p-mt-3">
        <label class="p-d-inline-block m-label-size-3 p-text-right p-mr-1"
          >Mã danh mục <strong class="p-error">*</strong>
        </label>
        <InputText
          type="text"
          v-model="recData.code"
          class="p-inputtext-sm p-col-8"
          v-if="!recData.categoryId"
          @input="changeCode()"
        />
        <InputText
          type="text"
          v-model="recData.code"
          class="p-inputtext-sm p-col-8"
          v-if="recData.categoryId"
          disabled
        />
      </div>
      <div class="p-mt-3">
        <label class="p-d-inline-block m-label-size-3 p-text-right p-mr-1"
          >Tên danh mục <strong class="p-error">*</strong>
        </label>
        <InputText
          type="text"
          v-model="recData.name"
          class="p-inputtext-sm p-col-8"
        />
      </div>
      <div class="p-mt-3">
        <label class="p-d-inline-block m-label-size-3 p-text-right p-mr-1"
        >Nhóm danh mục
        </label>
        <Dropdown
          class="p-inputtext-sm"
          style="width: 445px"
          v-model="recData.parentCode"
          :options="categorys"
          :filter="true"
          :showClear="true"
          optionLabel="name"
          optionValue="code"
          placeholder="--Hãy chọn--"
        />
      </div>
      <div class="p-mt-3 p-d-flex p-ai-center">
        <label class="p-d-inline-block m-label-size-3 p-text-right p-mr-1">
          Ghi chú
        </label>
        <textarea
          rows="3"
          v-model="recData.description"
          class="p-inputtext-sm p-col-8"
          maxlength="500"
        />
      </div>
    </div>

    <!--button-->
    <div class="p-mt-2 p-d-flex p-flex-row p-jc-end" style="width: 100%">
      <template v-if="changesApplied">
        <Button
          label="CLOSE"
          @click="$emit('cancel')"
          class="p-button-sm"
        ></Button>
      </template>
      <template v-else>
        <Button
          label="CANCEL"
          @click="$emit('cancel')"
          class="p-button-sm p-button-outlined p-mr-1"
        ></Button>
        <Button
          icon="pi pi-check"
          iconPos="left"
          label="APPLY CHANGES"
          @click="onApplyChanges()"
          class="p-button-sm"
        ></Button>
      </template>
    </div>
  </div>
</template>

<script lang='ts'>
import {defineComponent, onMounted, ref} from "vue";
import CategoryApi from "@/api/category-api";
import { useToast } from "primevue/usetoast";

export default defineComponent({
  props: {
    rec: {
      type: Object,
      required: true,
    },
  },

  setup(props, { emit }): unknown {
    const toast = useToast();
    const showMessage = ref(false);
    const userMessage = ref("");
    const changesApplied = ref(false);
    let categorys = ref([]);
    const recData = ref(JSON.parse(JSON.stringify(props.rec))); // do not create direct refs to props to avoid making changes to props, instead use a cloned value of prop

    const onApplyChanges = async () => {
      const rawCategoryObj = JSON.parse(JSON.stringify(recData.value));
      let msg: any[];
      msg = [];
      if (!rawCategoryObj.code) {
        msg.push("mã danh mục");
      }
      if (!rawCategoryObj.name) {
        msg.push("tên danh mục");
      }
      if (msg.length > 0) {
        userMessage.value =
          "Trường " + msg.join(", ") + " không được để trống!";
        showMessage.value = true;
      } else {
        delete rawCategoryObj.index;
        const check = await CategoryApi.getCategoryByCode(rawCategoryObj);
        if (check.data) {
          userMessage.value = "Mã danh mục bị trùng. Vui lòng nhập lại!";
          showMessage.value = true;
          setTimeout(() => {
            return (showMessage.value = false);
          }, 2000);
          setTimeout(() => {
            return (showMessage.value = false);
          }, 2000);
        } else {
          let resp;
          if (rawCategoryObj.categoryId) {
            resp = await CategoryApi.updateCategory(rawCategoryObj);
          } else {
            resp = await CategoryApi.addCategory(rawCategoryObj);
          }
          if (resp.data.msgType === "SUCCESS") {
            toast.add({
              severity: "success",
              summary: rawCategoryObj.id
                ? "Sửa thành công!"
                : "Thêm mới thành công!",
              detail: `${rawCategoryObj.name} (${rawCategoryObj.code})`,
              life: 3000,
            });
            if (!rawCategoryObj.categoryId) {
              recData.value.id = "CREATED";
            }
            changesApplied.value = true;
            emit("changed");
            setTimeout(() => {
              onCancel();
            }, 500);
          } else {
            toast.add({
              severity: "error",
              summary: "Lỗi xảy ra!",
              detail: resp.data.msg,
            });
          }
        }
      }
    };

    onMounted(() => {
      lstCategory();
    });

    const lstCategory = async () => {
      const resp = await CategoryApi.getListIsNotParentCode({code: recData.value.code});
      let lstCategorys = [];
      if (resp.data) {
        lstCategorys = resp.data.list;
      }
      categorys.value = lstCategorys;
    };

    const onCancel = () => {
      emit("cancel");
    };

    const changeCode = () => {
      recData.value.code = recData.value.code.toUpperCase();
    }

    return {
      showMessage,
      userMessage,
      changesApplied,
      recData,
      onApplyChanges,
      changeCode,
      onCancel,
      categorys,
    };
  },
});
</script>

