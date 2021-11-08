package com.app.util;


import com.app.model.delivery.DeliveryBillFlowModel;
import com.app.model.delivery.DeliveryBillModel;
import com.app.model.department.DepartmentModel;
import com.app.model.employee.EmployeeModel;
import com.app.model.factory.FactoryModel;
import com.app.model.position.PositionModel;
import com.app.model.quality.QualityModel;
import com.app.model.receipt.ReceiptFlowModel;
import com.app.model.receipt.ReceiptModel;
import com.app.model.role.RoleModel;
import com.app.model.species.SpeciesModel;
import com.app.model.supplier.SupplierModel;
import com.app.model.supplies.SuppliesModel;
import com.app.model.unit.UnitModel;
import com.app.model.user.UserModel;
import com.app.model.warehouse.WarehouseModel;
import com.app.model.warehouseCard.WarehouseCardFlowModel;
import com.app.model.warehouseCard.WarehouseCardModel;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;


public class HibernateUtil {
    private static SessionFactory sessionFactory;
    private static StandardServiceRegistry registry;


    //Hibernate 4 Style
    public static SessionFactory getSessionFactory(){
        // setup the session factory
        if (sessionFactory == null) {
            Configuration configuration = new Configuration();

            //Connection Props
            configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
            configuration.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
            configuration.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/qlvattu?useUnicode=yes&characterEncoding=UTF-8");
            configuration.setProperty("useUnicode", "true");
            configuration.setProperty("characterEncoding", "UTF-8");
            configuration.setProperty("hibernate.connection.release_mode", "auto");
            configuration.setProperty("hibernate.connection.username", "root");
            configuration.setProperty("hibernate.connection.password", "123456a@");
            configuration.setProperty("hibernate.show_sql", "true");
            configuration.setProperty("hibernate.format_sql", "true");

            //Class Mappings
            configuration.addAnnotatedClass(UserModel.class);
            configuration.addAnnotatedClass(EmployeeModel.class);
            configuration.addAnnotatedClass(DeliveryBillFlowModel.class);
            configuration.addAnnotatedClass(DeliveryBillModel.class);
            configuration.addAnnotatedClass(FactoryModel.class);
            configuration.addAnnotatedClass(PositionModel.class);
            configuration.addAnnotatedClass(QualityModel.class);
            configuration.addAnnotatedClass(ReceiptFlowModel.class);
            configuration.addAnnotatedClass(ReceiptModel.class);
            configuration.addAnnotatedClass(RoleModel.class);
            configuration.addAnnotatedClass(SpeciesModel.class);
            configuration.addAnnotatedClass(SupplierModel.class);
            configuration.addAnnotatedClass(SuppliesModel.class);
            configuration.addAnnotatedClass(UnitModel.class);
            configuration.addAnnotatedClass(WarehouseCardFlowModel.class);
            configuration.addAnnotatedClass(WarehouseCardModel.class);
            configuration.addAnnotatedClass(WarehouseModel.class);
            configuration.addAnnotatedClass(DepartmentModel.class);
            //material management
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        }
        return sessionFactory;
    }

    public static Session getSession(){
        return getSessionFactory().openSession();
    }

    public static void shutdown() {
        if (registry != null) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}