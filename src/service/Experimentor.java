package service;

public class Experimentor implements Role {

    static final int basicWorkload = 70;
    static final int basicSalary = 650;

    @Override
    public int calcSalaryAsPrimary(int workload) {
        return basicSalary + calcSalaryAsSecondary() + (workload - basicWorkload) * 20;
    }

    @Override
    public int calcSalaryAsSecondary() {
        return 150;
    }

    @Override
    public int getRoleId() {
        return 1;
    }
    
}
