package com.tiny.cash.loan.card.net.response.data.order;

import java.io.Serializable;
import java.util.List;

public class ProductList implements Serializable {

    private List<ProductsBean> products;
    private String type;// common 就是普通产品，google是谷歌产品，marketing是营销产品；

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public List<ProductsBean> getProducts() {
        return products;
    }

    public void setProducts(List<ProductsBean> products) {
        this.products = products;
    }

    public static class ProductsBean implements Serializable{

        /**
         * amount : 1500
         * prodId : 201203030000005555
         * prodName : 1500KSH91Product
         * period : 91
         * stage : 2
         */

        private String amount;
        private String prodId;
        private String prodName;
        private String period;
        private String stage;
        private String usable;

        public String getUsable() {
            return usable;
        }

        public void setUsable(String usable) {
            this.usable = usable;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getProdId() {
            return prodId;
        }

        public void setProdId(String prodId) {
            this.prodId = prodId;
        }

        public String getProdName() {
            return prodName;
        }

        public void setProdName(String prodName) {
            this.prodName = prodName;
        }

        public String getPeriod() {
            return period;
        }

        public void setPeriod(String period) {
            this.period = period;
        }

        public String getStage() {
            return stage;
        }

        public void setStage(String stage) {
            this.stage = stage;
        }
    }
}
