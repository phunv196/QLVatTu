<template>
  <div class="m-app-header">
    <Logo style="height:36px;width:36px;margin-left:5px"></Logo>
    <div class="m-app-header-label">
      {{label}}
    </div>
    <div class="m-app-header-item-group">
      <ul v-if="items?.length > 1">
        <li v-for="(item, i) of items" :key="i">
          <router-link tag="span" :to="item.to ? item.to : ''" class="m-app-header-item">
              <span :class="item.icon"></span> {{item.label}}
          </router-link>
          <ul v-if="item.items?.length > 1">
              <li v-for="(el, j) of item.items" :key="j">
                <router-link tag="span" :to="el.to" class="m-app-header-el">
                    <span :class="item.icon"></span> {{el.label}}
                </router-link>
            </li>
          </ul>
        </li>
      </ul>
    </div>
    <div style="flex:1"></div>
    <div class="p-d-flex p-flex-row p-jc-end" style="color:var(--fg-alt); font-size:0.75rem">
      <div class="p-as-center p-ai-end p-jc-between p-mr-5" style="font-size: 1rem">
        <Button
        icon="pi pi-send"
        iconPos="right"
        label="Đổi mật khẩu"
        @click="changePassword()"
        style="background: #333333;"
      ></Button>
      </div>
      <div class="p-d-flex p-flex-column p-ai-end p-jc-between p-mr-2">
        <span> {{ $store.getters.role }} </span>
        <span> {{ $store.getters.userName }} </span>
      </div>
      <Button style="align-self: center;" icon="pi pi-sign-out" class="p-button-rounded" @click="$router.push('/login')"/>
    </div>
    <Toast />
    <Dialog header="Đổi mật khẩu" v-model:visible="showSlideOut" :style="{width: '30vw' , height: '30vw', background: '#000000'}">
            <div class="m-font-regular">
              <transition name="p-message">
                <Message v-if="showMessage" severity="info" @close="showMessage = false">
                  {{ userMessage }}</Message
                >
              </transition>
              <div class="p-mt-3">
                <label class="p-d-inline-block m-label-size-5 p-text-left p-mr-1"
                  >Tên đăng nhập: <strong style="font-size: 1rem"> {{ $store.getters.user }} </strong>
                </label>
              </div>
              <div class="p-mt-3">
                <label class="p-d-inline-block m-label-size-3 p-text-left p-mr-1"
                  >Mật khẩu cũ
                </label>
                <InputText
                  type="text"
                  v-model="password"
                  class="p-inputtext-sm p-col-6"
                />
              </div>
              <div class="p-mt-3">
                <label class="p-d-inline-block m-label-size-3 p-text-left p-mr-1"
                  >Mật khẩu mới
                </label>
                <Password v-model="newPassword" toggleMask class="p-inputtext-sm " style="width: 50%;"></Password>
              </div>
              <div class="p-mt-3">
                <label class="p-d-inline-block m-label-size-3 p-text-left p-mr-1"
                  >Nhập lại
                </label>
                <Password v-model="rePassword" toggleMask class="p-inputtext-sm" style="width: 50%;"></Password>
              </div>
            </div>
            <template #footer>
                <Button label="Hủy" icon="pi pi-times" @click="closeBasic" class="p-button-text"/>
                <Button label="Đổi mật khẩu" icon="pi pi-check" @click="updatePassword" autofocus />
            </template>
    </Dialog> 
  </div>
</template>

<script lang=ts>
import {ref, defineComponent } from 'vue';
import Logo from '@/components/Logo.vue';
import Menubar from 'primevue/menubar';
import { useToast } from "primevue/usetoast";
import UsersApi from "@/api/users-api";
export default defineComponent({
  props: {
    label: { type: String, default: 'QLVT' },
    items: { type: Array, required: true },
  },
  components: {
    Logo,
    Menubar,
  },
  setup(): unknown {
    const showSlideOut = ref(false);
    const password = ref('');
    const newPassword = ref('');
    const rePassword = ref('');
    const showMessage = ref(false);
    const userMessage = ref("");
    const toast = useToast();
    const changePassword = () => {
      showSlideOut.value = true;
    }
    const closeBasic = () => {
      showSlideOut.value = false;
    }

    const updatePassword = () => {
      let msg: any[];
      msg = [];

      if (password.value.length < 1 ) {
        msg.push("mật khẩu cũ chưa nhập");
      }

      if (rePassword.value !== newPassword.value) {
        msg.push("mật khẩu mới không khớp");
      }
      if (newPassword.value.length < 8) {
        msg.push("mật khẩu mới phải >= 8 ký tự");
      }
      if (msg.length > 0) {
        userMessage.value = "Trường " + msg.join(", ") + ". Vui lòng nhập lại!";
        showMessage.value = true;
      } else {
        UsersApi.changePassword({password: password.value, newPassword: newPassword.value }).then(res => {
          if (res.data) {
            showSlideOut.value = false;
            toast.add({
              severity: "success",
              summary: "Thành công!",
              detail: "Đổi mật khẩu thành công!",
              life: 3000,
            });
          } else {
            userMessage.value = "Mật khẩu cũ không đúng. Vui lòng nhập lại!";
          }
        });
      }
    }


    return {
      showSlideOut,
      tableData:[],
      selectedRec:{},
      isNew:false,
      changePassword,
      closeBasic,
      updatePassword,
      password,
      showMessage,
      userMessage,
      newPassword,
      rePassword,
    }
  },
});
</script>

<style scoped lang="scss">
@import "~@/assets/styles/_vars.scss";
  $m-header-height: 50px;

  .m-app-header {
    display:flex;
    width:100%;
    height:$m-header-height;
    flex-direction: row;
    align-items: center;
    flex-wrap: nowrap;
    overflow: hidden;
    .m-app-header-item-group {
      ul {
        z-index: 1000000;
        display: flex;
        flex-wrap: nowrap;
        // background: blue;
        width: 100%;
        flex-direction: row;
        align-items: baseline;
        list-style: none;
        li {
          .m-app-header-item {
            display:inline-block;
            cursor: pointer;
            color  :#ccc;
            border : 0;
            margin : 0 3px;
            padding: 0 16px;
            height : 100%;
            font-size:14px;
            line-height: $m-header-height;
            &:hover{
              background-color: #000;
              color:var(--primary-color);
            }
            &.router-link-exact-active{
              color: var(--primary-color);
            }
          }
          &:hover ul{
              display: block;
            }
          ul {
            display: none;
            position: absolute;
            flex-direction: column;
            width: 200px;
            padding: 0;
            margin: 0;
            background: #333;
            margin-top: 9px;
            border-radius: 5px;
            li {
              width: 100%;
              margin: 0px;
              .m-app-header-el {
                display:inline-block;
                cursor: pointer;
                color  :#ccc;
                border : 0;
                margin : 0 3px;
                padding: 0 16px;
                height : 97%;
                min-width: 97%;
                font-size:14px;
                line-height: $m-header-height;
                &:hover{
                  background-color: #000;
                  color:var(--primary-color);
                }
                &.router-link-exact-active{
                  color: var(--primary-color);
                }
              }
            }
            &:before {
                content: "";
                width: 100%;
                height: 30px;
                top: -22px;
                left: 0;
                position: absolute;
            }
            &:after {
                bottom: 100%;
                left: 68px;
                border: solid transparent;
                content: " ";
                height: 0;
                width: 0;
                position: absolute;
                pointer-events: none;
                border-color: rgba(23,158,255,0);
                border-bottom-color: #333;
                border-width: 9px;
            }
          }
        }
      }
    }
  }

  .m-app-header-label{
    display:inline-block;
    color:#fff;
    width:200px;
    padding:0px 8px;
    font-size:20px;
    font-weight: 100;
  }
  a {
    color:var(--alt-fg);
    text-decoration: none;
    &.router-link-exact-active{
      color: var(--primary-color);
      //background-color: rgba(0,0,0,0.3);
    }
  }

</style>