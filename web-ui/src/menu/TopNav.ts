/* eslint-disable object-curly-newline */
export default {
  CUSTOMER: [
    { id: '0', label: 'My Profile', icon: 'user', to: '/home/my-profile' },
    { id: '1', label: 'My Orders', icon: 'shopping-bag', to: '/home/my-orders' },
    { id: '2', label: 'My Cart', icon: 'shopping-cart', to: '/home/my-cart' },
  ],
  SUPPORT: [
    { id: '0', label: 'Trang chủ', icon:'pi pi-fw pi-home', to: '/home/manage/dashboard' },
    {
      id: '1',
      label:'Quản lý danh mục',
      icon:'pi pi-fw pi-th-large',
      items:[
        {
          id: '2',
          label:'Quản lý chất lượng',
          icon:'pi pi-fw pi-filter',
          to: '/home/manage/quality',
        },
        {
          id: '2',
          label:'Chủng loại',
          icon:'pi pi-fw pi-filter',
          to: '/home/manage/species'
        },
        {
          id: '2',
          label:'Đơn vị tính',
          icon:'pi pi-fw pi-filter',
          to: '/home/manage/unit'
        },
      ]
    },
    {
      id: '1',
      label:'Quản lý người dùng',
      icon:'pi pi-fw pi-th-large',
      items:[
        {
          id: '2',
          label:'Quản lý user',
          icon:'pi pi-fw pi-users',
          to: '/home/manage/users',
        },
        {
          id: '2',
          label:'Quản lý nhân viên',
          icon:'pi pi-fw pi-users',
          to: '/home/manage/employees'
        },
        {
          id: '2',
          label:'Chức vụ',
          icon:'pi pi-fw pi-filter',
          to: '/home/manage/position'
        },
        {
          id: '2',
          label:'Phòng ban',
          icon:'pi pi-fw pi-filter',
          to: '/home/manage/department'
        }
      ]
    },
    {
      id: '1',
      label:'Quản lý kho vật tư',
      icon:'pi pi-fw pi-th-large',
      items:[
        {
          id: '2',
          label:'Phân xưởng',
          icon:'pi pi-fw pi-home',
          to: '/home/manage/factory'
        },
        {
          id: '2',
          label:'Nhà cung cấp',
          icon:'pi pi-fw pi-home',
          to: '/home/manage/supplier'
        },
        {
          id: '2',
          label:'Vật tư',
          icon:'pi pi-fw pi-inbox',
          to: '/home/manage/supplies'
        },
        {
          id: '2',
          label:'Kho',
          icon:'pi pi-fw pi-home',
          to: '/home/manage/warehouse'
        }
      ]
    },
    {
      id: '1',
      label:'Quản lý xuất nhập',
      icon:'pi pi-fw pi-th-large',
      items:[
        {
          id: '2',
          label:'Phiếu nhập kho',
          icon:'pi pi-fw pi-download',
          to: '/home/manage/receipt'
        },
        {
          id: '2',
          label:'Phiếu xuất kho',
          icon:'pi pi-fw pi-upload',
          to: '/home/manage/delivery-bill',
        },
        {
          id: '2',
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
      id: '1',
      label:'Quản lý danh mục',
      icon:'pi pi-fw pi-th-large',
      items:[
        {
          id: '2',
          label:'Quản lý chất lượng',
          icon:'pi pi-fw pi-filter',
          to: '/home/manage/quality',
        },
        {
          id: '2',
          label:'Chủng loại',
          icon:'pi pi-fw pi-filter',
          to: '/home/manage/species'
        },
        {
          id: '2',
          label:'Đơn vị tính',
          icon:'pi pi-fw pi-filter',
          to: '/home/manage/unit'
        },
      ]
    },
    {
      id: '1',
      label:'Quản lý người dùng',
      icon:'pi pi-fw pi-th-large',
      items:[
        {
          id: '2',
          label:'Quản lý user',
          icon:'pi pi-fw pi-users',
          to: '/home/manage/users',
        },
        {
          id: '2',
          label:'Quản lý nhân viên',
          icon:'pi pi-fw pi-users',
          to: '/home/manage/employees'
        },
        {
          id: '2',
          label:'Chức vụ',
          icon:'pi pi-fw pi-filter',
          to: '/home/manage/position'
        },
        {
          id: '2',
          label:'Phòng ban',
          icon:'pi pi-fw pi-filter',
          to: '/home/manage/department'
        }
      ]
    },
    {
      id: '1',
      label:'Quản lý kho vật tư',
      icon:'pi pi-fw pi-th-large',
      items:[
        {
          id: '2',
          label:'Phân xưởng',
          icon:'pi pi-fw pi-home',
          to: '/home/manage/factory'
        },
        {
          id: '2',
          label:'Nhà cung cấp',
          icon:'pi pi-fw pi-home',
          to: '/home/manage/supplier'
        },
        {
          id: '2',
          label:'Vật tư',
          icon:'pi pi-fw pi-inbox',
          to: '/home/manage/supplies'
        },
        {
          id: '2',
          label:'Kho',
          icon:'pi pi-fw pi-home',
          to: '/home/manage/warehouse'
        }
      ]
    },
    {
      id: '1',
      label:'Quản lý xuất nhập',
      icon:'pi pi-fw pi-th-large',
      items:[
        {
          id: '2',
          label:'Phiếu nhập kho',
          icon:'pi pi-fw pi-download',
          to: '/home/manage/receipt'
        },
        {
          id: '2',
          label:'Phiếu xuất kho',
          icon:'pi pi-fw pi-upload',
          to: '/home/manage/delivery-bill',
        },
        {
          id: '2',
          label:'Thẻ kho',
          icon:'pi pi-fw pi-credit-card',
          to: '/home/manage/warehouse-card'
        },
      ]
    },
  ]
};
