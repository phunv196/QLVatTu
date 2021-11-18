<template>
  <div class="m-font-regular">
    <Toast />
    <h4>
      Vật tư #
      <span style="color: var(--primary-color)">
        {{ recData.suppliesId ? recData.suppliesId : "NEW" }}
      </span>
    </h4>
    <transition name="p-message">
      <Message v-if="showMessage" severity="error" @close="showMessage = false">
        {{ userMessage }}</Message
      >
    </transition>
    <div>
      <div class="p-mt-12">
        <label class="p-d-inline-block m-label-size-2 p-text-left p-mr-1"
          >Mã vật tư <strong class="p-error">*</strong>
        </label>
        <InputText
          type="text"
          v-model="recData.code"
          class="p-inputtext-sm p-mr-5"
          style="width: 30%"
        />
        <label class="p-d-inline-block m-label-size-2 p-text-left p-mr-1 p-ml-5"
          >Tên vật tư <strong class="p-error">*</strong>
        </label>
        <InputText
          type="text"
          v-model="recData.name"
          class="p-inputtext-sm"
          style="width: 30%"
        />
      </div>
      <div class="p-mt-3">
        <label class="p-d-inline-block m-label-size-2 p-text-left p-mr-1"
          >Chủng loại
        </label>
        <Dropdown
          style="width: 30%"
          class="p-inputtext-sm p-mr-5"
          v-model="recData.speciesId"
          :options="arrSpecies"
          :filter="true"
          :showClear="true"
          optionLabel="name"
          optionValue="speciesId"
          placeholder="--Hãy chọn--"
        />
        <label class="p-d-inline-block m-label-size-2 p-text-left p-mr-1 p-ml-5"
          >Chất lượng</label
        >
        <Dropdown
          style="width: 30%"
          class="p-inputtext-sm"
          v-model="recData.qualityId"
          :options="arrQuality"
          :filter="true"
          :showClear="true"
          optionLabel="name"
          optionValue="qualityId"
          placeholder="--Hãy chọn--"
        />
      </div>
      <div class="p-mt-3">
        <label class="p-d-inline-block m-label-size-2 p-text-left p-mr-1"
          >Nhà cung cấp <strong class="p-error">*</strong>
        </label>
        <Dropdown
          style="width: 30%"
          class="p-inputtext-sm p-mr-5"
          v-model="recData.supplierId"
          :options="arrSupplier"
          :filter="true"
          :showClear="true"
          optionLabel="name"
          optionValue="supplierId"
          placeholder="--Hãy chọn--"
        />
        <label class="p-d-inline-block m-label-size-2 p-text-left p-mr-1 p-ml-5"
          >Đơn vị tính <strong class="p-error">*</strong>
        </label>
        <Dropdown
          class="p-inputtext-sm"
          style="width: 30%"
          v-model="recData.unitId"
          :options="arrUnit"
          :filter="true"
          :showClear="true"
          optionLabel="name"
          optionValue="unitId"
          placeholder="--Hãy chọn--"
        />
      </div>
      <div class="p-mt-3">
        <label class="p-d-inline-block m-label-size-2 p-text-left p-mr-1"
          >Giá <strong class="p-error">*</strong>
        </label>
        <InputText
          type="text"
          v-model="recData.price"
          class="p-inputtext-sm"
          style="width: 30%"
        />
      </div>
      <div class="p-mt-3 p-d-flex p-ai-center">
        <label class="p-d-inline-block m-label-size-2 p-text-left p-mr-1">
          Ghi chú
        </label>
        <textarea
          rows="3"
          v-model="recData.description"
          class="p-inputtext-sm"
          maxlength="500"
          style="width: 77.5%"
        />
      </div>
    </div>
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
import { defineComponent, ref, onMounted } from "vue";
import SuppliesApi from "@/api/supplies-api";
import { useToast } from "primevue/usetoast";
import SupplierApi from "@/api/supplier-api";
import QualityApi from "@/api/quality-api";
import SpeciesApi from "@/api/species-api";
import unitApi from "@/api/unit-api";

export default defineComponent({
  props: {
    rec: {
      type: Object,
      required: true,
    },
  },

  setup(props, { emit }): unknown {
    const userMessage = ref("");
    const arrQuality = ref([]);
    const arrUnit = ref([]);
    const arrSpecies = ref([]);
    const arrSupplier = ref([]);
    const toast = useToast();
    const showMessage = ref(false);
    const changesApplied = ref(false);
    const recData = ref(JSON.parse(JSON.stringify(props.rec))); // do not create direct refs to props to avoid making changes to props, instead use a cloned value of prop
    const onApplyChanges = async () => {
      const rawSuppliesObj = JSON.parse(JSON.stringify(recData.value));
      let msg: any[];
      msg = [];
      if (!rawSuppliesObj.code) {
        msg.push("mã vât tư");
      }
      if (!rawSuppliesObj.name) {
        msg.push("tên vât tư");
      }
      if (!rawSuppliesObj.supplierId) {
        msg.push("nhà cung cấp");
      }
      if (!rawSuppliesObj.unitId) {
        msg.push("đơn vị tính");
      }
      if (!rawSuppliesObj.price) {
        msg.push("giá vật tư");
      }
      if (msg.length > 0) {
        userMessage.value = "Trường " + msg.join(", ") + "không được để trống!";
        showMessage.value = true;
        setTimeout(() => {
          return (showMessage.value = false);
        }, 2000);
      } else {
        delete rawSuppliesObj.index;
        const check = await SuppliesApi.getSuppliesByCode(rawSuppliesObj);
        if (check.data) {
          userMessage.value = "Mã vật tư bị trùng. Vui lòng nhập lại!";
          showMessage.value = true;
          setTimeout(() => {
            return (showMessage.value = false);
          }, 2000);
        } else {
          let resp;
          if (rawSuppliesObj.suppliesId) {
            resp = await SuppliesApi.updateSupplies(rawSuppliesObj);
          } else {
            resp = await SuppliesApi.addSupplies(rawSuppliesObj);
          }
          if (resp.data.msgType === "SUCCESS") {
            toast.add({
              severity: "success",
              summary: rawSuppliesObj.suppliesId
                ? "Sửa thành công!"
                : "Thêm mới thành công!",
              detail: `${rawSuppliesObj.name} (${rawSuppliesObj.code})`,
              life: 3000,
            });
            if (!rawSuppliesObj.suppliesId) {
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

    onMounted(async () => {
      await lstSupplier();
      await lstQuality();
      await lstSpecies();
      await lstUnit();
    });

    const lstSupplier = async () => {
      const resp = await SupplierApi.getAll();
      let lstSuppliers = [];
      if (resp.data) {
        lstSuppliers = resp.data.list;
      }
      arrSupplier.value = lstSuppliers;
    };

    const lstQuality = async () => {
      const resp = await QualityApi.getAll();
      let lstQualitys = [];
      if (resp.data) {
        lstQualitys = resp.data.list;
      }
      arrQuality.value = lstQualitys;
    };

    const lstUnit = async () => {
      const resp = await unitApi.getAll();
      let lstUnits = [];
      if (resp.data) {
        lstUnits = resp.data.list;
      }
      arrUnit.value = lstUnits;
    };

    const lstSpecies = async () => {
      const resp = await SpeciesApi.getAll();
      let lstSpeciess = [];
      if (resp.data) {
        lstSpeciess = resp.data.list;
      }
      arrSpecies.value = lstSpeciess;
    };

    return {
      arrQuality,
      arrSpecies,
      arrSupplier,
      showMessage,
      changesApplied,
      recData,
      onApplyChanges,
      onCancel,
      userMessage,
      arrUnit,
    };
  },
});
</script>
