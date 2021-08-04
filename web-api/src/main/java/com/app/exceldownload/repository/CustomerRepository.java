package com.app.exceldownload.repository;


import com.app.exceldownload.model.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, Long> {
}
