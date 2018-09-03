package pw.io.booker.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pw.io.booker.model.Customer;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Integer> {

}
