/* eslint-disable object-curly-newline */
export default {
  CUSTOMER: [
    { id: '0', label: 'My Profile', icon: 'user', to: '/home/my-profile' },
    { id: '1', label: 'My Orders', icon: 'shopping-bag', to: '/home/my-orders' },
    { id: '2', label: 'My Cart', icon: 'shopping-cart', to: '/home/my-cart' },
  ],
  SUPPORT: [
    { id: '0', label: 'Trang chủ', icon:'pi pi-home', to: '/home/dashboard' },
    {
      id: '1',
      label:'Quản lý danh mục',
      to: 'home',
      items:[
        {
          id: '2',
          label:'Quản lý chất lượng',
          to: '/home/quality',
        },
        {
          id: '2',
          label:'Chủng loại',
          to: '/home/species'
        },
        {
          id: '2',
          label:'Đơn vị tính',
          to: '/home/unit'
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
          to: '/home/users',
        },
        {
          id: '2',
          label:'Quản lý nhân viên',
          to: '/home/employees'
        },
        {
          id: '2',
          label:'Chức vụ',
          to: '/home/position'
        },
        {
          id: '2',
          label:'Phòng ban',
          to: '/home/department'
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
          to: '/home/factory'
        },
        {
          id: '2',
          label:'Nhà cung cấp',
          to: '/home/supplier'
        },
        {
          id: '2',
          label:'Vật tư',
          to: '/home/supplies'
        },
        {
          id: '2',
          label:'Kho',
          to: '/home/warehouse'
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
          to: '/home/receipt'
        },
        {
          id: '2',
          label:'Phiếu xuất kho',
          to: '/home/delivery-bill',
        },
        {
          id: '2',
          label:'Thẻ kho',
          to: '/home/warehouse-card'
        },
      ]
    },
  ],
  ADMIN: [
    { label: 'Trang chủ', icon:'pi pi-home', to: '/home/dashboard' },
    {
      id: '1',
      label:'Quản lý danh mục',
      to: 'home',
      items:[
        {
          id: '2',
          label:'Quản lý chất lượng',
          to: '/home/quality',
        },
        {
          id: '2',
          label:'Chủng loại',
          to: '/home/species'
        },
        {
          id: '2',
          label:'Đơn vị tính',
          to: '/home/unit'
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
          to: '/home/users',
        },
        {
          id: '2',
          label:'Quản lý nhân viên',
          to: '/home/employees'
        },
        {
          id: '2',
          label:'Chức vụ',
          to: '/home/position'
        },
        {
          id: '2',
          label:'Phòng ban',
          to: '/home/department'
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
          to: '/home/factory'
        },{
          label:'Nhà cung cấp',
          to: '/home/supplier'
        },{
          label:'Vật tư',
          to: '/home/supplies'
        },{
          label:'Kho',
          to: '/home/warehouse'
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
          to: '/home/receipt'
        },
        {
          id: '2',
          label:'Phiếu xuất kho',
          to: '/home/delivery-bill',
        },{
          label:'Thẻ kho',
          to: '/home/warehouse-card'
        },
      ]
    },
  ]

};
