package com.lifeplus.lifeplus.model.form;

public class PlanTemplateForm {
    private String name;
    private Integer plan;

    public PlanTemplateForm() {
    }

    public PlanTemplateForm(String name) {
        this.name = name;
    }

    public PlanTemplateForm(String name, Integer plan) {
        this.name = name;
        this.plan = plan;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPlan() {
        return plan;
    }

    public void setPlan(Integer plan) {
        this.plan = plan;
    }
}
