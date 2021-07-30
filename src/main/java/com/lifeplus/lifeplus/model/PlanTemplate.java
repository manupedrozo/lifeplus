package com.lifeplus.lifeplus.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "plan_template")
public class PlanTemplate implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private int id;

    @Column
    private String name;

    @OneToOne
    private User creator;

    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<Routine> routines;

    public PlanTemplate() {
    }

    public PlanTemplate(String name, User creator) {
        this.name = name;
        this.creator = creator;
    }

    public PlanTemplate(String name, User creator, List<Routine> routines) {
        this.name = name;
        this.creator = creator;
        this.routines = routines;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public List<Routine> getRoutines() {
        return routines;
    }

    public void setRoutines(List<Routine> routines) {
        this.routines = routines;
    }

}
