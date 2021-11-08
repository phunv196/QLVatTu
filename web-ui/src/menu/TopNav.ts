/* eslint-disable object-curly-newline */
export default {
  CUSTOMER: [
    { id: '0', label: 'My Profile', icon: 'user', to: '/home/my-profile' },
    { id: '1', label: 'My Orders', icon: 'shopping-bag', to: '/home/my-orders' },
    { id: '2', label: 'My Cart', icon: 'shopping-cart', to: '/home/my-cart' },
  ],
  SUPPORT: [
    { id: '0', label: 'Trang chủ', icon:'pi pi-home', to: '/home/manage/dashboard' },
    {
      id: '1',
      label:'Quản lý danh mục',
      to: 'home',
      items:[
        {
          id: '2',
          label:'Quản lý chất lượng',
          to: '/home/manage/quality',
        },
        {
          id: '2',
          label:'Chủng loại',
          to: '/home/manage/species'
        },
        {
          id: '2',
          label:'Đơn vị tính',
          to: '/home/manage/unit'
        },
      ]
    },
    {
      id: '1',
      label:'Quản lý người dùng',
      to: 'home',
      items:[
        {
          id: '2',
          label:'Quản lý user',
          to: '/home/manage/users',
        },
        {
          id: '2',
          label:'Quản lý nhân viên',
          to: '/home/manage/employees'
        },
        {
          id: '2',
          label:'Chức vụ',
          to: '/home/manage/position'
        },
        {
          id: '2',
          label:'Phòng ban',
          to: '/home/manage/department'
        }
      ]
    },
    {
      id: '1',
      label:'Quản lý kho vật tư',
      to: 'home',
      items:[
        {
          id: '2',
          label:'Phân xưởng',
          to: '/home/manage/factory'
        },
        {
          id: '2',
          label:'Nhà cung cấp',
          to: '/home/manage/supplier'
        },
        {
          id: '2',
          label:'Vật tư',
          to: '/home/manage/supplies'
        },
        {
          id: '2',
          label:'Kho',
          to: '/home/manage/warehouse'
        }
      ]
    },
    {
      id: '1',
      label:'Quản lý xuất nhập',
      to: 'home',
      items:[
        {
          id: '2',
          label:'Phiếu nhập kho',
          to: '/home/manage/receipt'
        },
        {
          id: '2',
          label:'Phiếu xuất kho',
          to: '/home/manage/delivery-bill',
        },
        {
          id: '2',
          label:'Thẻ kho',
          to: '/home/manage/warehouse-card'
        },
      ]
    },
  ],
  ADMIN: [
    { label: 'Trang chủ', icon:'pi pi-home', to: '/home/manage/dashboard' },
    {
      id: '1',
      label:'Quản lý danh mục',
      to: 'home',
      items:[
        {
          id: '2',
          label:'Quản lý chất lượng',
          to: '/home/manage/quality',
        },
        {
          id: '2',
          label:'Chủng loại',
          to: '/home/manage/species'
        },
        {
          id: '2',
          label:'Đơn vị tính',
          to: '/home/manage/unit'
        },
      ]
    },
    {
      id: '1',
      label:'Quản lý người dùng',
      to: 'home',
      items:[
        {
          label:'Quản lý user',
          to: '/home/manage/users',
        },
        {
          id: '2',
          label:'Quản lý nhân viên',
          to: '/home/manage/employees'
        },
        {
          id: '2',
          label:'Chức vụ',
          to: '/home/manage/position'
        },
        {
          id: '2',
          label:'Phòng ban',
          to: '/home/manage/department'
        }
      ]
    },
    {
      id: '1',
      label:'Quản lý kho vật tư',
      to: 'home',
      items:[
        {
          label:'Phân xưởng',
          to: '/home/manage/factory'
        },{
          label:'Nhà cung cấp',
          to: '/home/manage/supplier'
        },{
          label:'Vật tư',
          to: '/home/manage/supplies'
        },{
          label:'Kho',
          to: '/home/manage/warehouse'
        }
      ]
    },
    {
      id: '1',
      label:'Quản lý xuất nhập',
      to: 'home',
      items:[
        {
          label:'Phiếu nhập kho',
          to: '/home/manage/receipt'
        },
        {
          id: '2',
          label:'Phiếu xuất kho',
          to: '/home/manage/delivery-bill',
        },{
          label:'Thẻ kho',
          to: '/home/manage/warehouse-card'
        },
      ]
    },
  ]

};
