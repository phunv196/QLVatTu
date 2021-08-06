<template>
  <div class="m-login">
    <div class="left">
    <div>
      <span class="display:flex" >
        <Logo style="display:inline-block;width:50px;height:50px; margin-bottom:50px;"></Logo>
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
<!--            <Button label="Register" @click="onRegisterClick" class="p-mr-1 p-button-sm p-button-raised" style="border-color:#777; background-color:#777;"/>-->
            <Button label="Login" @click="convertBase64ToExcel" :disabled="loginDisabled" class="p-button-sm p-button-raised" style="width: 30%"/>
          </div>
          <!-- Localization is Disaabled as the vue-i18n is still in beta -->
<!--          <div class="p-d-flex p-flex-row p-jc-end p-mt-2 p-ai-center" >-->
<!--            <div class="p-d-flex" @click="onChangeLangClick()" style="fontSize: .75rem; line-height:1rem; color:var(&#45;&#45;primary-color); cursor:pointer">-->
<!--              <div> CHANGE LANGUAGE {{ t('message.hello') }} </div>-->
<!--              <i class="pi pi-globe p-ml-1"></i>-->
<!--            </div>-->
<!--            <Menu ref="languageMenuEl" :model="languages" :popup="true" class="m-small p-mt-1" style="width:9rem"/>-->
<!--          </div>-->
<!--          <div style="margin-top:100px">-->
<!--            <hr style="border:0; border-top: 1px solid #666;">-->
<!--            <span style="color: #888; font-size:12px"> Use buttons bellow to login as different roles </span>-->
<!--            <div class="p-d-flex p-mt-2">-->
<!--              <Button label="ADMIN" @click="username='admin'; password='password'" class="p-mr-1 m-btn" style="background-color:#adc165; border:0"/>-->
<!--              <Button label="CUSTOMER" @click="username='customer'; password='password'" class="p-mr-1 m-btn" style="background-color:#ffc73b; border:0"/>-->
<!--              <Button label="SUPPORT" @click="username='support'; password='password'" class="m-btn" style="background-color:#ff903e; border:0"/>-->
<!--            </div>-->
<!--            <div class="p-mt-1">-->
<!--              <Button label="API DOCS" @click="onApiDocsClick()" class="p-mr-1 m-btn" style="width:100%; background-color:#777;"/>-->
<!--            </div>-->
<!--          </div>-->
        </div>
      </span>
    </div>
    </div>
    <div class="m-arc">
      <svg width="100%" height="100%"  viewBox="0 0 80 800" preserveAspectRatio="none">
        <path d="M0 0 Q 155 400 0 800" stroke-width="4px"/>
      </svg>
    </div>
    <div class="right">
    </div>
  </div>
</template>

<script lang='ts'>
import { ref, onMounted, defineComponent } from 'vue';
import Logo from '@/components/Logo.vue';
import { useStore } from 'vuex';
import { useRouter } from 'vue-router';
import UsersApi from '@/api/users-api'; // eslint-disable-line import/no-cycle
import { async } from 'rxjs';

export default defineComponent({
  setup(): unknown {
    const router = useRouter();
    const store = useStore();
    const username = ref('admin');
    const password = ref('password');
    const loginDisabled = ref(false);
    const win = window;

    const onLoginClick = async () => {
      loginDisabled.value = true;
      const resp = await UsersApi.login(username.value, password.value);
      loginDisabled.value = false;
      if (resp.data.msgType === 'SUCCESS') {
        router.push('/home');
      } else {
        console.log('Unable to login');
      }
    };

    const onRegisterClick = async () => {
      router.push('/register');
    };

    const onApiDocsClick = () => {
      win.location.replace(`${win.location.origin}/api-docs/index.html`);
      // router.push('/api-docs/index.html');
    };

    onMounted(() => {
      store.commit('baseUrl', 'http://localhost:8080/');
      // store.commit('baseUrl', `${window.location.origin}/`);
    });

    const convertBase64ToExcel = () => {
		var data = "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";
		window.open ("data:application/vnd.ms-excel;base64," + data);
    // var contentType = 'application/vnd.ms-excel';
		// var blob1 = b64toBlob(data, contentType, '');
		// var blobUrl1 = URL.createObjectURL(blob1);
		// window.open(blobUrl1);
		}

	  // const b64toBlob = (b64Data:any, contentType:any , sliceSize:any) => {
		// contentType = contentType || '';
	  // var	sliceSize = sliceSize || 512;
		// var byteCharacters = atob(b64Data);
		// var byteArrays = [];

		// for (var offset = 0; offset < byteCharacters.length; offset += sliceSize) {
		// var slice = byteCharacters.slice(offset, offset + sliceSize);

		// var byteNumbers = new Array(slice.length);
		// for (var i = 0; i < slice.length; i++) {
		// byteNumbers[i] = slice.charCodeAt(i);
		// }

		// var byteArray = new Uint8Array(byteNumbers);

		// byteArrays.push(byteArray);
		// }

		// var blob = new Blob(byteArrays, {type: contentType});
		// return blob;
		// }


    return {
      loginDisabled,
      username,
      password,
      onLoginClick,
      onRegisterClick,
      onApiDocsClick,
      convertBase64ToExcel
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
