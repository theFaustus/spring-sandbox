package evil.inc.misc.proxy;

public class Demo {
    public static void main(String[] args) {
        CustomerService customerService = LoggableProxyMaker.proxy(new CustomerService());
        customerService.findCustomerById(1L);
    }
}
