package pw.io.booker.controller;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pw.io.booker.model.Authentication;
import pw.io.booker.model.Customer;
import pw.io.booker.repo.AuthenticationRepository;
import pw.io.booker.repo.CustomerRepository;
import pw.io.booker.service.TokenCreator;

@RestController
@Transactional
@RequestMapping("/customers")
public class CustomerController {

	private CustomerRepository customerRepository;
	private TokenCreator tokenCreator;
	private AuthenticationRepository authenticationRepository;

	public CustomerController(CustomerRepository customerRepository, TokenCreator tokenCreator,
			AuthenticationRepository authenticationRepository) {
		super();
		this.customerRepository = customerRepository;
		this.tokenCreator = tokenCreator;
		this.authenticationRepository = authenticationRepository;
	}

	@PostMapping("/login")
	public String login(@RequestBody Customer customer) {
		Customer cust_new = customerRepository.findByUsernameAndPassword(customer.getUsername(),
				customer.getPassword());
		Authentication auth = new Authentication();
		String token = tokenCreator.encode(cust_new);
		auth.setToken(token);
		auth.setCustomer(cust_new);
		authenticationRepository.save(auth);
		return token;
	}

	@GetMapping
	public List<Customer> getAll(@RequestHeader String token) {
		return (List<Customer>) customerRepository.findAll();
	}

	@PostMapping
	public List<Customer> saveAll(@RequestHeader String token, @RequestBody List<Customer> customers) {
		for (Customer customer : customers) {
			if (customerRepository.findById(customer.getCustomerId()).isPresent()) {
				throw new RuntimeException("Customers already exist");
			}
		}
		return (List<Customer>) customerRepository.saveAll(customers);
	}

	@PutMapping
	public List<Customer> updateAll(@RequestHeader String token, @RequestBody List<Customer> customers) {
		for (Customer customer : customers) {
			if (!customerRepository.findById(customer.getCustomerId()).isPresent()) {
				throw new RuntimeException("Customers should exist first");
			}
		}
		return (List<Customer>) customerRepository.saveAll(customers);
	}

	@DeleteMapping
	public List<Customer> deleteAll(@RequestHeader String token,
			@RequestParam("customerIdList") List<Integer> customerIdList) {
		List<Customer> customerList = (List<Customer>) customerRepository.findAllById(customerIdList);
		customerRepository.deleteAll(customerList);
		return customerList;
	}

	@GetMapping("/{customerId}")
	public Customer getCustomer(@RequestHeader String token, @PathVariable("customerId") int customerId) {
		return customerRepository.findById(customerId).get();
	}

	@PutMapping("/{customerId}")
	public Customer updateCustomer(@RequestHeader String token, @PathVariable("customerId") int customerId,
			@RequestBody Customer customer) {
		if (customerId != customer.getCustomerId()) {
			throw new RuntimeException("Id is not the same with the object id");
		}
		if (!customerRepository.findById(customer.getCustomerId()).isPresent()) {
			throw new RuntimeException("Customers should exist first");
		}
		customer.setCustomerId(customerId);
		return customerRepository.save(customer);
	}

	@DeleteMapping("/{customerId}")
	public Customer deleteCustomer(@RequestHeader String token, @PathVariable("customerId") int customerId) {
		Customer customer = customerRepository.findById(customerId).get();
		customerRepository.delete(customer);
		return customer;
	}

	@DeleteMapping("/logout")
	public void deleteToken(@RequestHeader String token) {
		authenticationRepository.delete((Authentication) authenticationRepository.findByToken(token));
	}
}
