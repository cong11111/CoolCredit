package com.tiny.cash.loan.card.net.response.data.bean;

import java.io.Serializable;
import java.util.List;

/**
 * *作者: jyw
 * 日期：2020/10/14 13:43
 */
public class Common implements Serializable {


    /**
     * dictMap : {"marital":[{"key":"1","val":"Single"},{"key":"2","val":"Married"},{"key":"3","val":"Divorced"},{"key":"4","val":"Widowed"}]}
     * parentId : 0
     */

    private DictMapBean dictMap;
    private String parentId;

    public DictMapBean getDictMap() {
        return dictMap;
    }

    public void setDictMap(DictMapBean dictMap) {
        this.dictMap = dictMap;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public static class DictMapBean implements Serializable{
        /**
         * education:学历等级，
         * salary:工资区间,
         * marital:婚姻状况，
         * relationship:关系，
         * work:工作情况,
         * stateArea:州地区地址联动,
         * state:州，
         * area:地区，
         */
        private List<Education> education;

        private List<Salary> salary;

        private List<Marital> marital;

        private List<Relationship> relationship;

        private List<Work> work;

        private List<State> state;

        private List<Area> area;

        public List<Education> getEducation() {
            return education;
        }

        public void setEducation(List<Education> education) {
            this.education = education;
        }

        public List<Salary> getSalary() {
            return salary;
        }

        public void setSalary(List<Salary> salary) {
            this.salary = salary;
        }

        public List<Marital> getMarital() {
            return marital;
        }

        public void setMarital(List<Marital> marital) {
            this.marital = marital;
        }

        public List<Relationship> getRelationship() {
            return relationship;
        }

        public void setRelationship(List<Relationship> relationship) {
            this.relationship = relationship;
        }

        public List<Work> getWork() {
            return work;
        }

        public void setWork(List<Work> work) {
            this.work = work;
        }

        public List<State> getState() {
            return state;
        }

        public void setState(List<State> state) {
            this.state = state;
        }

        public List<Area> getArea() {
            return area;
        }

        public void setArea(List<Area> area) {
            this.area = area;
        }

        public static class Marital extends KeyValue {
        }

        public static class Education extends KeyValue {
        }

        public static class Salary extends KeyValue {
        }

        public static class Relationship extends KeyValue {
        }

        public static class Work extends KeyValue {
        }

        public static class State extends KeyValue {
        }

        public static class Area extends KeyValue {
        }

        public static class KeyValue implements Serializable{
            /**
             * key : 1
             * val : Single
             */

            private String key;
            private String val;

            public String getKey() {
                return key;
            }

            public void setKey(String key) {
                this.key = key;
            }

            public String getVal() {
                return val;
            }

            public void setVal(String val) {
                this.val = val;
            }
        }
    }
}
