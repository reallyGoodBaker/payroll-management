package service;

public class Teacher implements Role {

    static final int basicWorkload = 120;
    static final int basicSalary = 800;

    @Override
    public int calcSalaryAsPrimary(int workload) {
        return basicSalary + (workload - basicWorkload) * 20;
    }

    @Override
    public int calcSalaryAsSecondary() {
        return 0;
    }

    @Override
    public int getRoleId() {
        return 0;
    }
    
}
