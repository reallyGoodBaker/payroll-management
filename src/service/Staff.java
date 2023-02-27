package service;

import java.io.Serializable;

public class Staff implements Serializable {

    private final static long serialVersionUID = 114516L;

    private Role primaryRole;
    private Role secondaryRole;
    final private String uid;
    private String name;
    private int workload;

    public void setWorkload(int workload) {
        this.workload = workload;
    }

    public Staff(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    void setPrimaryRole(Role role) {
        primaryRole = role;
    }

    Role getPrimaryRole() {
        return primaryRole;
    }

    void setSecondaryRole(Role role) {
        secondaryRole = role;
    }

    Role getSecondaryRole() {
        return secondaryRole;
    }

    int getSalary() {
        int salary = primaryRole.calcSalaryAsPrimary(workload);
        if (secondaryRole != null) {
            salary += secondaryRole.calcSalaryAsSecondary();
        }

        return salary;
    }

    private String addQuotes(String raw) {
        return "\"" + raw + "\"";
    }

    public String toJson() {
        String json = "{"
            + addQuotes("uid") + ":" + addQuotes(uid)
            + "," + addQuotes("name") + ":" + addQuotes(name)
            + "," + addQuotes("workload") + ":" + addQuotes(Integer.toString(workload))
            + "," + addQuotes("primary") + ":" + addQuotes(Integer.toString(primaryRole.getRoleId()))
            + "," + addQuotes("salary") + ":" + addQuotes(Integer.toString(getSalary()))
            + (secondaryRole != null
                ? "," + addQuotes("secondary") + ":" + addQuotes(Integer.toString(secondaryRole.getRoleId()))
                : ",\"secondary\":null")
            + "}";

        return json;
    }

}
