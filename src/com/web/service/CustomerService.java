package com.web.service;

import java.util.List;

import com.web.dao.entity.Customer;
import com.web.dao.entity.CustomerAttach;
import com.web.dao.model.PageBean;

public interface CustomerService {

	//個案
	public String queryMaxCustomerNo();
	public Customer queryCustomerById(long id);
	public PageBean queryCustomerByPage(String section, String[] sections, String category, String custName, String keyword, int pageSize, int page);
	public int queryCustomerCnt(String[] sections);
	public List<Customer> queryCustomerBySection(String section);
	public void updateCustomer(Customer cust);
	public void deleteCustomer(Customer cust);
	
	//個案檔案
	public CustomerAttach queryCustomerAttachById(long id);
	public List<CustomerAttach> queryCustomerAttachByCustId(long id);
	public void updateCustomerAttach(CustomerAttach ach);
	public void deleteCustomerAttach(CustomerAttach ach);
}
