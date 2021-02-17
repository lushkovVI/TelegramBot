package org.example.entity;

import javax.persistence.*;

@Entity(name = "daily_domains")
@Table(name = "daily_domains",schema="hbt")
public class DailyDomains {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "domain_name")
    private String domainName;
    @Column(name = "domain_price")
    private Long price;

    public DailyDomains(){

    }

    public DailyDomains(String domainName, Long price) {
        this.domainName = domainName;
        this.price = price;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "DailyDomains{" +
                "domainName='" + domainName + '\'' +
                ", price=" + price +
                '}';
    }
}
