package evil.inc.data.billing;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "billing")
public record BillingCellularPricingProperties(
        float cellularPricingData,
        float cellularPricingCall,
        float cellularPricingSms,
        float cellularSpendingThreshold
) {
}
