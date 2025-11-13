package evil.inc.misc.proxy;


public class CustomerService {

    public Customer findCustomerById(Long id) {
        return new Customer();
    }
}
