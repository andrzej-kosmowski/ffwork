package domain.user;

import discount.CompanyTierDiscount;
import discount.Discount;

public class CompanyUser extends User {
    private final String companyName;
    private final String taxId;
    private final int tier;

    public CompanyUser(String email, String displayName, String companyName, String taxId, int tier) {
        super(email, displayName);
        this.companyName = companyName;
        this.taxId = taxId;
        this.tier = tier;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getTaxId() {
        return taxId;
    }

    @Override
    public Discount getDiscount() {
        return new CompanyTierDiscount(tier);
    }
}
