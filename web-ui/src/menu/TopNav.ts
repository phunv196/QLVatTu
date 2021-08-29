/* eslint-disable object-curly-newline */
export default {
  CUSTOMER: [
    { id: '0', label: 'My Profile', icon: 'user', to: '/home/my-profile' },
    { id: '1', label: 'My Orders', icon: 'shopping-bag', to: '/home/my-orders' },
    { id: '2', label: 'My Cart', icon: 'shopping-cart', to: '/home/my-cart' },
  ],
  SUPPORT: [
    { label: 'Trang chủ', icon:'pi pi-fw pi-home', to: '/home/manage/dashboard' },
    {
      id: '2',
      label:'Quản lý danh mục',
      icon:'pi pi-fw pi-th-large',
      items:[
        {
          label:'Quản lý chất lượng',
          icon:'pi pi-fw pi-filter',
          to: '/home/manage/quality',
        },
        {
          label:'Chủng loại',
          icon:'pi pi-fw pi-filter',
          to: '/home/manage/species'
        },
        {
          label:'Đơn vị tính',
          icon:'pi pi-fw pi-filter',
          to: '/home/manage/unit'
        },
      ]
    },
    {
      id: '2',
      label:'Quản lý người dùng',
      icon:'pi pi-fw pi-th-large',
      items:[
        {
          label:'Quản lý user',
          icon:'pi pi-fw pi-users',
          to: '/home/manage/users',
        },
        {
          label:'Quản lý nhân viên',
          icon:'pi pi-fw pi-users',
          to: '/home/manage/employees'
        },
        {
          label:'Chức vụ',
          icon:'pi pi-fw pi-filter',
          to: '/home/manage/position'
        },
        {
          label:'Phòng ban',
          icon:'pi pi-fw pi-filter',
          to: '/home/manage/department'
        }
      ]
    },
    {
      id: '2',
      label:'Quản lý kho vật tư',
      icon:'pi pi-fw pi-th-large',
      items:[
        {
          label:'Phân xưởng',
          icon:'pi pi-fw pi-home',
          to: '/home/manage/factory'
        },{
          label:'Nhà cung cấp',
          icon:'pi pi-fw pi-home',
          to: '/home/manage/supplier'
        },{
          label:'Vật tư',
          icon:'pi pi-fw pi-inbox',
          to: '/home/manage/supplies'
        },{
          label:'Kho',
          icon:'pi pi-fw pi-home',
          to: '/home/manage/warehouse'
        }
      ]
    },
    {
      id: '2',
      label:'Quản lý xuất nhập',
      icon:'pi pi-fw pi-th-large',
      items:[
        {
          label:'Phiếu nhập kho',
          icon:'pi pi-fw pi-download',
          to: '/home/manage/receipt'
        },
        {
          label:'Phiếu xuất kho',
          icon:'pi pi-fw pi-upload',
          to: '/home/manage/delivery-bill',
        },{
          label:'Thẻ kho',
          icon:'pi pi-fw pi-credit-card',
          to: '/home/manage/warehouse-card'
        },
      ]
    },
  ],
  ADMIN: [
    { label: 'Trang chủ', icon:'pi pi-fw pi-home', to: '/home/manage/dashboard' },
    {
      id: '2',
      label:'Quản lý danh mục',
      icon:'pi pi-fw pi-th-large',
      items:[
        {
          label:'Quản lý chất lượng',
          icon:'pi pi-fw pi-filter',
          to: '/home/manage/quality',
        },
        {
          label:'Chủng loại',
          icon:'pi pi-fw pi-filter',
          to: '/home/manage/species'
        },
        {
          label:'Đơn vị tính',
          icon:'pi pi-fw pi-filter',
          to: '/home/manage/unit'
        },
      ]
    },
    {
      id: '2',
      label:'Quản lý người dùng',
      icon:'pi pi-fw pi-th-large',
      items:[
        {
          label:'Quản lý user',
          icon:'pi pi-fw pi-users',
          to: '/home/manage/users',
        },
        {
          label:'Quản lý nhân viên',
          icon:'pi pi-fw pi-users',
          to: '/home/manage/employees'
        },
        {
          label:'Chức vụ',
          icon:'pi pi-fw pi-filter',
          to: '/home/manage/position'
        },
        {
          label:'Phòng ban',
          icon:'pi pi-fw pi-filter',
          to: '/home/manage/department'
        }
      ]
    },
    {
      id: '2',
      label:'Quản lý kho vật tư',
      icon:'pi pi-fw pi-th-large',
      items:[
        {
          label:'Phân xưởng',
          icon:'pi pi-fw pi-home',
          to: '/home/manage/factory'
        },{
          label:'Nhà cung cấp',
          icon:'pi pi-fw pi-home',
          to: '/home/manage/supplier'
        },{
          label:'Vật tư',
          icon:'pi pi-fw pi-inbox',
          to: '/home/manage/supplies'
        },{
          label:'Kho',
          icon:'pi pi-fw pi-home',
          to: '/home/manage/warehouse'
        }
      ]
    },
    {
      id: '2',
      label:'Quản lý xuất nhập',
      icon:'pi pi-fw pi-th-large',
      items:[
        {
          label:'Phiếu nhập kho',
          icon:'pi pi-fw pi-download',
          to: '/home/manage/receipt'
        },
        {
          label:'Phiếu xuất kho',
          icon:'pi pi-fw pi-upload',
          to: '/home/manage/delivery-bill',
        },{
          label:'Thẻ kho',
          icon:'pi pi-fw pi-credit-card',
          to: '/home/manage/warehouse-card'
        },
      ]
    },
  ]
};
