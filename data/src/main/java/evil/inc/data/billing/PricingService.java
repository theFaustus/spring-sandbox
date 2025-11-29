package evil.inc.data.billing;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class PricingService {

    private final BillingCellularPricingProperties props;

    private final Random random = new Random();

    public PricingService(BillingCellularPricingProperties props) {
        this.props = props;
    }

    public float getDataPricing() {
        //simulate retryable exception
        if (this.random.nextInt(1000) % 7 == 0) {
            throw new PricingException("Error while retrieving data pricing");
        }
        return this.props.cellularPricingData();
    }

    public float getCallPricing() {
        return this.props.cellularPricingCall();
    }

    public float getSmsPricing() {
        return this.props.cellularPricingSms();
    }

    public float getSpendingThreshold() {
        return this.props.cellularSpendingThreshold();
    }
}
