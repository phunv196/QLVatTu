<template>
  <div class="m-font-regular">
    <Toast />
    <h4>
      Chất lượng #
      <span style="color: var(--primary-color)">
        {{ recData.qualityId ? recData.qualityId : "NEW" }}
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
          >Mã chất lượng <strong class="p-error">*</strong> </label
        >
        <InputText
          type="text"
          v-model="recData.code"
          class="p-inputtext-sm p-col-8 "
        />
      </div>
      <div class="p-mt-3">
        <label class="p-d-inline-block m-label-size-3 p-text-right p-mr-1"
          >Tên chất lượng <strong class="p-error">*</strong>
        </label>
        <InputText type="text" v-model="recData.name" class="p-inputtext-sm p-col-8" />
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
import { defineComponent, ref } from "vue";
import QualityApi from "@/api/quality-api";
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
    const recData = ref(JSON.parse(JSON.stringify(props.rec))); // do not create direct refs to props to avoid making changes to props, instead use a cloned value of prop

    const onApplyChanges = async () => {
      const rawQualityObj = JSON.parse(JSON.stringify(recData.value));
      let msg: any[];
      msg = [];
      if (!rawQualityObj.code) {
        msg.push("mã chất lượng");
      }
      if (!rawQualityObj.name) {
        msg.push("tên chất lượng");
      }
      if (msg.length > 0) {
        userMessage.value =
          "Trường " + msg.join(", ") + " không được để trống!";
        showMessage.value = true;
      } else {
        delete rawQualityObj.index;
        const check = await QualityApi.getQualityByCode(rawQualityObj);
        if (check.data) {
          userMessage.value = "Mã chất lượng bị trùng. Vui lòng nhập lại!";
          showMessage.value = true;
        } else {
          let resp;
          if (rawQualityObj.qualityId) {
            resp = await QualityApi.updateQuality(rawQualityObj);
          } else {
            resp = await QualityApi.addQuality(rawQualityObj);
          }
          if (resp.data.msgType === "SUCCESS") {
            toast.add({
              severity: "success",
              summary: rawQualityObj.id ? "Sửa thành công!" : "Thêm mới thành công!",
              detail: `${rawQualityObj.name} (${rawQualityObj.code})`,
              life: 3000,
            });
            if (!rawQualityObj.qualityId) {
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

    const onCancel = () => {
      emit("cancel");
    };

    return {
      showMessage,
      userMessage,
      changesApplied,
      recData,
      onApplyChanges,
      onCancel,
    };
  },
});
</script>

