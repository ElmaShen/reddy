package com.web.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.dao.CustomerAttachDAO;
import com.web.dao.CustomerDAO;
import com.web.dao.entity.Customer;
import com.web.dao.entity.CustomerAttach;
import com.web.dao.model.PageBean;
import com.web.service.CustomerService;

@Service("customerService")
public class CustomerServiceImpl implements CustomerService {

	@Autowired
    private CustomerDAO customerDAO;
	
	@Autowired
    private CustomerAttachDAO customerAttachDAO;
	

	@Override
	public String queryMaxCustomerNo(){
		return customerDAO.queryMaxCno();
	}
	
	@Override
	public Customer queryCustomerById(long id){
		return customerDAO.queryById(id);
	}
	
	@Override
	public PageBean queryCustomerByPage(String section, String category, String custName, String keyword, int pageSize, int page){
		int totalCount = customerDAO.getAllRowCount(section, category, custName, keyword);    
        int totalPage = PageBean.countTotalPage(pageSize, totalCount);  
        int offset = PageBean.countOffset(pageSize, page); 
        int currentPage = PageBean.countCurrentPage(page);
        List<Customer> list = customerDAO.queryForPage(section, category, custName, keyword, offset, pageSize);
		
		//Set PageBean
        PageBean pageBean = new PageBean();
        pageBean.setPageSize(pageSize);    
        pageBean.setCurrentPage(currentPage);
        pageBean.setTotalCount(totalCount);
        pageBean.setTotalPage(totalPage);
        pageBean.setList(list);
        pageBean.init();
        return pageBean;
	}
	
	@Override
	public void updateCustomer(Customer cust){
		customerDAO.saveOrUpdate(cust);
	}
	
	@Override
	public void deleteCustomer(Customer cust){
		List<CustomerAttach> list = cust.getAttachs();
		for (CustomerAttach ach : list) {
			customerAttachDAO.delete(ach);
		}
		customerDAO.delete(cust);
	}
	
	@Override
	public CustomerAttach queryCustomerAttachById(long id){
		return customerAttachDAO.queryById(id);
	}
	
	@Override
	public List<CustomerAttach> queryCustomerAttachByCustId(long custId){
		return customerAttachDAO.queryByCustId(custId);
	}
	
	@Override
	public void updateCustomerAttach(CustomerAttach ach){
		customerAttachDAO.saveOrUpdate(ach);
	}
	
	@Override
	public void deleteCustomerAttach(CustomerAttach ach){
		customerAttachDAO.delete(ach);
	}
}
