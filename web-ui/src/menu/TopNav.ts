/* eslint-disable object-curly-newline */
export default {
  CUSTOMER: [
    { id: '0', label: 'My Profile', icon: 'user', to: '/home/my-profile' },
    { id: '1', label: 'My Orders', icon: 'shopping-bag', to: '/home/my-orders' },
    { id: '2', label: 'My Cart', icon: 'shopping-cart', to: '/home/my-cart' },
  ],
  SUPPORT: [
    { id: '0', label: 'Trang chủ', icon:'pi pi-home', to: '/home/dashboard' },
    { id: '1', label: 'Quản lý nhập kho', icon:'pi pi-download', to: '/home/receipt' },
    { id: '1', label: 'Quản lý xuất kho', icon:'pi pi-upload', to: '/home/delivery-bill' },
    { id: '1', label: 'Quản lý thẻ kho', icon:'pi pi-credit-card', to: '/home/warehouse-card' },
  ],
  ADMIN: [
    { label: 'Trang chủ', icon:'pi pi-home', to: '/home/dashboard' },
    {
      id: '1',
      label:'Quản lý danh mục',
      to: 'home',
      icon:'pi pi-book',
      items:[
        {
          id: '2',
          label:'Quản lý chất lượng',
          to: '/home/quality',
          icon:'pi pi-check-square',
        },
        {
          id: '2',
          label:'Chủng loại',
          to: '/home/species',
          icon:'pi pi-tags',
        },
        {
          id: '2',
          label:'Đơn vị tính',
          to: '/home/unit',
          icon:'pi pi-calendar-plus',
        },
      ]
    },
    {
      id: '1',
      label:'Quản lý người dùng',
      to: 'home',
      icon:'pi pi-users',
      items:[
        {
          label:'Quản lý user',
          to: '/home/users',
          icon:'pi pi-user',
        },
        {
          id: '2',
          label:'Quản lý nhân viên',
          to: '/home/employees',
          icon:'pi pi-id-card',
        },
        {
          id: '2',
          label:'Quản lý chức vụ',
          to: '/home/position',
          icon:'pi pi-sitemap',
        },
        {
          id: '2',
          label:'Quản lý phòng ban',
          to: '/home/department',
          icon:'pi pi-th-large',
        }
      ]
    },
    {
      id: '1',
      label:'Quản lý kho, vật tư',
      to: 'home',
      icon:'pi pi-inbox',
      items:[
        {
          label:'Quản lý phân xưởng',
          to: '/home/factory',
          icon:'pi pi-ticket',
        },{
          label:'Quản lý nhà cung cấp',
          to: '/home/supplier',
          icon:'pi pi-tablet',
        },{
          label:'Quản lý vật tư',
          to: '/home/supplies',
          icon:'pi pi-inbox',
        },{
          label:'Quản lý kho',
          to: '/home/warehouse',
          icon:'pi pi-home',
        }
      ]
    },
    {
      id: '1',
      label:'Quản lý xuất nhập',
      to: 'home',
      icon:'pi pi-chart-bar',
      items:[
        {
          label:'Quản lý nhập kho',
          to: '/home/receipt',
          icon:'pi pi-sign-in',
        },
        {
          id: '2',
          label:'Quản lý xuất kho',
          to: '/home/delivery-bill',
          icon:'pi pi-sign-out',
        },{
          label:'Quản lý thẻ kho',
          to: '/home/warehouse-card',
          icon:'pi pi-credit-card',
        },
      ]
    },
  ]

};
