package service;

public class Execs implements Role {

    static final int basicSalary = 750;

    @Override
    public int calcSalaryAsPrimary(int workload) {
        return basicSalary + calcSalaryAsSecondary();
    }

    @Override
    public int calcSalaryAsSecondary() {
        return 250;
    }

    @Override
    public int getRoleId() {
        return 2;
    }
    
}
