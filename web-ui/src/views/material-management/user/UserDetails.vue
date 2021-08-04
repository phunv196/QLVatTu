<template>
  <div class="m-font-regular">
    <BlockUI :blocked="isLoading" :fullScreen="true"></BlockUI>
    <!--    <div v-if="$props.isRegister">-->
    <!--      <h4>USER REGISTRATION</h4>-->
    <!--      <span class="m-gray-text">Provide some fake details, the data will be refreshed at certain interval</span>-->
    <!--    </div>-->
    <div>
      <span class="m-font-bold">USER ID: </span>
      <span style="color: var(--primary-color)">
        {{ $props.rec.loginName ? $props.rec.loginName : "New" }}</span
      >
      &nbsp;
      <!--      <span class="m-font-bold">ROLE: </span> <span style="color: var(&#45;&#45;primary-color)"> {{ $props.rec.role }} </span>-->
    </div>
    <div class="p-mt-4"></div>
    <transition name="p-message">
      <Message v-if="showMessage" severity="info" @close="showMessage = false">
        {{ userMessage }}</Message
      >
    </transition>
    <div>
      <!-- ID & Password -->
      <div v-if="!recData.loginName">
        <label class="p-d-inline-block m-label-size-2 p-text-right p-mr-1"
          >Tên tài khoản
        </label>
        <InputText
          type="text"
          v-model="recData.loginName"
          class="p-inputtext-sm"
        />
      </div>
      <div class="p-mt-3">
        <label class="p-d-inline-block m-label-size-2 p-text-right p-mr-1">
          Mật khẩu
        </label>
        <Password
          v-model="recData.password"
          toggleMask
          class="p-inputtext-sm"
        ></Password>
      </div>
      <div class="p-mt-3">
        <label class="p-d-inline-block m-label-size-2 p-text-right p-mr-1">
          Nhân viên
        </label>
        <Dropdown
          class="p-inputtext-sm"
          style="width: 200px"
          v-model="recData.employeeId"
          :options="emp"
          :filter="true"
          :showClear="true"
          optionLabel="fullName"
          optionValue="employeeId"
        />
      </div>
      <div class="p-mt-3">
        <label class="p-d-inline-block m-label-size-2 p-text-right p-mr-1">
          Quyền tài khoản
        </label>
        <Dropdown
          class="p-inputtext-sm"
          style="width: 200px"
          v-model="recData.role"
          :options="emps"
          :filter="true"
          :showClear="true"
          optionLabel="name"
          optionValue="code"
        />
      </div>
    </div>

    <!--    <div v-if="$props.isRegister" class="p-mt-2 p-d-flex p-flex-row p-jc-end" style="width:100%">-->
    <!--      <Button label="LOGIN" @click="$router.push('/login')" class="p-button-sm p-button-outlined p-mr-1"></Button>-->
    <!--      <Button icon="pi pi-check" iconPos="left" label="REGISTER" @click="onRegister()" class="p-button-sm"></Button>-->
    <!--    </div>-->
    <div class="p-mt-2 p-d-flex p-flex-row p-jc-end" style="width: 100%">
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
    </div>
  </div>
</template>

<script lang='ts'>
import { defineComponent, onMounted, ref } from "vue";
import { useRouter } from "vue-router";
import UsersApi from "@/api/users-api";
import EmployeeApi from "@/api/employee-api";
import RoleApi from "@/api/material-management/role-api";

export default defineComponent({
  props: {
    rec: { type: Object, required: true },
    isCustomer: { type: Boolean, default: true, required: false },
    isRegister: { type: Boolean, default: false, required: false },
  },

  setup(props): unknown {
    const showMessage = ref(false);
    const userMessage = ref("");
    const recData = ref(props.rec);
    const emp = ref([]);
    const emps = ref([]);
    const router = useRouter();

    const onApplyChanges = () => {
      userMessage.value = "Updating User Details is disabled";
      showMessage.value = true;
    };
    const onRegister = async () => {
      debugger;
      const rawUserObj = JSON.parse(JSON.stringify(recData.value));
      const resp = await UsersApi.registerUser(rawUserObj);
      if (resp.data.msgType === "SUCCESS") {
        router.push("/login");
      } else {
        userMessage.value = resp.data.msg; // 'Error during registration';
        showMessage.value = true;
      }
    };

    onMounted(() => {
      lstEmp();
    });

    const lstEmp = async () => {
      debugger;
      const resp = await EmployeeApi.getAll();
      debugger;
      let lstEmps = [];
      if (resp.data) {
        lstEmps = resp.data.list;
      }
      emp.value = lstEmps;

      const resps = await RoleApi.getAll();
      debugger;
      let lstRole = [];
      if (resps.data) {
        lstRole = resps.data.list;
      }
      emps.value = lstRole;
    };

    return {
      router,
      showMessage,
      userMessage,
      recData,
      onApplyChanges,
      onRegister,
      emp,
      emps,
    };
  },
});
/*

import Rest from '@/rest/Rest';
import router from '@/router';
export default {
  props: {
    rec:{type: Object, required:true},
    isCustomer:{type: Boolean, default:true, required:false},
    isNew:{type: Boolean, default:true, required:false},
},
  data:function(){
    return {
      loading : false,
      isEmployee:false,
      userData : this.rec,  // Assign the prop value to data to make it reactive
    }
  },
  methods:{
    onRegister(){
      let me = this;
      Rest.registerUser(me.$data.userData).then(function(resp){
        me.$data.loading=false;
        if (resp.data.msgType==="ERROR"){
          me.$message.error('Unable to register the user with id: '+ me.$data.userData.loginName)
        }
        else{
          router.push("/login");
          me.$message.success('User ID created :' + me.$data.userData.loginName);
        }
      })
      .catch(function(err){
        console.log("REST ERROR: %O", err.response?err.response:err);
        me.$data.loading=false;
      });
    },
    onApplyChanges(){

    },
    onCancel(){
      router.push("/login");
    }
  },
}
*/
</script>
