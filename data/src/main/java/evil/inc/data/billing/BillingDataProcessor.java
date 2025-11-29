package evil.inc.data.billing;

import evil.inc.data.model.BillingData;
import evil.inc.data.model.ReportingData;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class BillingDataProcessor implements ItemProcessor<BillingData, ReportingData> {

    private final PricingService pricingService;

    public BillingDataProcessor(PricingService pricingService) {
        this.pricingService = pricingService;
    }

    @Override
    public ReportingData process(BillingData item) {
        float billingTotal = item.dataUsage() * pricingService.getDataPricing() +
                             item.callDuration() * pricingService.getCallPricing() +
                             item.smsCount() * pricingService.getSmsPricing();
        if (billingTotal < pricingService.getSpendingThreshold()) {
            return null;
        }
        return new ReportingData(item, billingTotal);
    }
}
