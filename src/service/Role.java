package service;

import java.io.Serializable;

public interface Role extends Serializable {
    static long serialVersionUID = 114515L;
    int calcSalaryAsPrimary(int workload);
    int calcSalaryAsSecondary();
    int getRoleId();
}
