<template>
  <div class="m-login">
    <div class="left">
    <Toast />
    <div>
      <span class="display:flex" >
        <Logo style="display:inline-block;width:50px;height:50px; margin-bottom:20px;"></Logo>
        <span class="sw-light-text-on-dark" style="font-size:32px">Quản lý kho vật tư</span>
        <div class="p-d-flex p-flex-column p-jc-center p-mt-4 p-ml-6" >
          <span class="p-input-icon-left p-mt-1">
            <i class="pi pi-user" />
            <InputText type="text" v-model="username" placeholder="Username" style="background:#222; color:#bbb; border-color:#555; width:100%; font-size:0.875rem"/>
          </span>
          <span class="p-input-icon-left p-mt-1">
            <i class="pi pi-lock" />
            <InputText type="password" v-model="password" placeholder="Password" style="background:#222; color:#bbb; border-color:#666; width:100%;font-size:0.875rem"/>
          </span>
          <div class="p-d-flex p-flex-row p-jc-center p-mt-3 ">
            <Button label="Login" @click="onLoginClick" class="p-button-sm p-button-raised" style="width: 30%"/>
          </div>
        </div>
      </span>
    </div>
    </div>
    <div class="m-arc">
      <svg width="100%" height="100%"  viewBox="0 0 80 800" preserveAspectRatio="none">
        <path d="M0 0 Q 155 400 0 800" stroke-width="4px"/>
      </svg>
    </div>
  </div>
</template>

<script lang='ts'>
import { ref, onMounted, defineComponent } from 'vue';
import Logo from '@/components/Logo.vue';
import { useStore } from 'vuex';
import { useRouter } from 'vue-router';
import UsersApi from '@/api/users-api'; // eslint-disable-line import/no-cycle
import { useToast } from "primevue/usetoast";

export default defineComponent({
  setup(): unknown {
    const router = useRouter();
    const store = useStore();
    const username = ref('');
    const password = ref('');
    const loginDisabled = ref(false);
    const win = window;
    const toast = useToast();

    const onLoginClick = async () => {
      loginDisabled.value = true;
      const resp = await UsersApi.login(username.value, password.value);
      if (resp) {
        if (resp.data.msgType === 'SUCCESS') {
          router.push('/home');
        }
      } else {
        toast.add({
              severity: "error",
              summary: "Lỗi xảy ra!",
              detail: "Tài khoản hoặc mật khẩu không đúng. Vui lòng nhập lại!",
              life: 3000,
            });
      }
    };

    const onApiDocsClick = () => {
      win.location.replace(`${win.location.origin}/api-docs/index.html`);
      // router.push('/api-docs/index.html');
    };

    onMounted(() => {
      store.commit('baseUrl', 'http://localhost:8080');
      // store.commit('baseUrl', 'http://localhost:8080/ql-vt');
      // store.commit('baseUrl', `${window.location.origin}/`);
    });


    return {
      loginDisabled,
      username,
      password,
      onLoginClick,
      onApiDocsClick,
    };
  },
  components: {
    Logo,
  },
});
</script>

<style lang="scss" scoped>
.m-login {
  display:flex;
  flex-direction: row;
  flex-wrap: nowrap;
  align-items:stretch;
  overflow: hidden;
  width:100%;
  height:100%;
  background-color: var(--bg);
  .left{
    overflow: hidden;
    min-width:320px;
    padding:16px;
    color: var(--fg-alt);
    background-color: var(--bg-alt);
    display:flex;
    justify-content: center;
    flex-direction: column;
  }
  .m-arc {
    width:120px;
    svg {
      fill: var(--bg-alt);
      stroke: var(--accent-color);
    }
  }
  .right {
    display:flex;
    flex-direction: column;
    flex:1;
    justify-content: center;
    align-items: center;
  }
  .m-btn {
    font-size: .7rem;
    font-weight:700;
    max-height:26px;
    color: #333;
    border:0;
  }
}
</style>
