<template>
  <div class="m-font-regular">
    <Toast />
    <h4>
      Role #
      <span style="color: var(--primary-color)">
        {{ recData.roleId ? recData.roleId : "NEW" }}
      </span>
    </h4>
    <transition name="p-message">
      <Message v-if="showMessage" severity="error" @close="showMessage = false">
        {{ userMessage }}</Message
      >
    </transition>

    <div>
      <div class="p-mt-1">
        <label class="p-d-inline-block m-label-size-2 p-text-right p-mr-1"
          >Mã quyền</label
        >
        <InputText
          type="text"
          v-model="recData.code"
          class="p-inputtext-sm p-col-8 p-mr-1"
        />
      </div>
      <div class="p-mt-1">
        <label class="p-d-inline-block m-label-size-2 p-text-right p-mr-1"
          >Tên quyền
        </label>
        <InputText type="text" v-model="recData.name" class="p-inputtext-sm p-col-8" />
      </div>
      <div class="p-mt-3 p-d-flex p-ai-center">
        <label class="p-d-inline-block m-label-size-2 p-text-right p-mr-1">
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
import RoleApi from "@/api/material-management/role-api";
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
      debugger;
      const rawRoleObj = JSON.parse(JSON.stringify(recData.value));
      let msg: any[];
      msg = [];
      if (!rawRoleObj.code) {
        msg.push("mã quyền");
      }
      if (!rawRoleObj.name) {
        msg.push("tên quyền");
      }
      if (msg.length > 0) {
        userMessage.value =
          "Trường " + msg.join(", ") + " không được để trống!";
        showMessage.value = true;
      } else {
        delete rawRoleObj.index;
        const check = await RoleApi.getRoleByCode(rawRoleObj);
        if (check.data) {
          userMessage.value = "Mã quyền bị trùng. Vui lòng nhập lại!";
          showMessage.value = true;
        } else {
          let resp;
          debugger;
          if (rawRoleObj.roleId) {
            resp = await RoleApi.updateRole(rawRoleObj);
          } else {
            resp = await RoleApi.addRole(rawRoleObj);
          }
          if (resp.data.msgType === "SUCCESS") {
            toast.add({
              severity: "success",
              summary: rawRoleObj.roleId ? "Product Updated" : "Product Added",
              detail: `${rawRoleObj.name} (${rawRoleObj.code})`,
              life: 3000,
            });
            if (!rawRoleObj.roleId) {
              recData.value.id = "CREATED";
            }
            changesApplied.value = true;
            emit("changed");
          } else {
            toast.add({
              severity: "error",
              summary: "Error",
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
