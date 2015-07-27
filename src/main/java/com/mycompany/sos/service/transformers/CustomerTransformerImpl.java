/**
 * 
 */
package com.mycompany.sos.service.transformers;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mycompany.sos.dao.entities.AddressEntity;
import com.mycompany.sos.dao.entities.CustomerEntity;
import com.mycompany.sos.dao.entities.CustomerPaymentDetailEntity;
import com.mycompany.sos.model.Customer;

/**
 * CustomerTransformerImpl class
 * 
 * @author colin
 *
 */
@Component
public class CustomerTransformerImpl implements CustomerTransformer {

	@Autowired
	private AddressTransformer addressTransformer;
	
	@Autowired
	private CustomerPaymentDetailTransformer customerPaymentDetailTransformer;
	
	@Autowired
	private OrderTransformer orderTransformer;
	
	@Override
	public Customer getDtoFromEntity(CustomerEntity customerEntity) {
		
		Customer customer = new Customer();
		customer.setFirstName(customerEntity.getFirstName());
		customer.setLastName(customerEntity.getLastName());
		customer.setDateOfBirth(customerEntity.getDateOfBirth());
		customer.setEmail(customerEntity.getEmail());
		customer.setAddress(addressTransformer.getDtoFromEntity(customerEntity.getAddress()));
//		customer.setCustomerPaymentDetails(customerPaymentDetailTransformer
//				.getDtoFromEntity(customerEntity.getCustomerPaymentDetail()));
//		
		return customer;
	}

	@Override
	public CustomerEntity getEntityFromDto(Customer customer) {
		CustomerEntity customerEntity = new CustomerEntity();
		customerEntity.setFirstName(customer.getFirstName());
		customerEntity.setLastName(customer.getLastName());
		customerEntity.setDateOfBirth(customer.getDateOfBirth());
		customerEntity.setEmail(customer.getEmail());
		
		CustomerPaymentDetailEntity customerPaymentDetailEntity = customerPaymentDetailTransformer.getEntityFromDto(customer.getCustomerPaymentDetails()); 
		customerPaymentDetailEntity.setCustomer(customerEntity);
		customerEntity.setCustomerPaymentDetail(customerPaymentDetailEntity);		
		
		AddressEntity addressEntity = addressTransformer.getEntityFromDto(customer.getAddress());
		addressEntity.getCustomers().add(customerEntity);
		customerEntity.setAddress(addressEntity);
		
		customerEntity.setOrders(orderTransformer.getEntityListFromDto(customer.getOrders()));
		
		return customerEntity;
	}

	@Override
	public Set<Customer> getDtoListFromEntityList(Set<CustomerEntity> customerEntityList) {
		Set<Customer> customers = new HashSet<Customer>();
		
		for(CustomerEntity customerEntity : customerEntityList) {
			customers.add(getDtoFromEntity(customerEntity));
		}
		
		return customers;
	}

	@Override
	public Set<CustomerEntity> getEntityListFromDtoList(Set<Customer> customers) {
		Set<CustomerEntity> customerEntities = new HashSet<CustomerEntity>();
		for(Customer customer : customers) {
			customerEntities.add(getEntityFromDto(customer));
		}
		
		return customerEntities;
	}

	
}