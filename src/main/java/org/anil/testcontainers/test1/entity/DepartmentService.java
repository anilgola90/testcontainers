package org.anil.testcontainers.test1.entity;

import java.util.Optional;

public class DepartmentService {

    public void getDep(Optional<Department> department){
       var employee =  department.get().getEmployees();
        System.out.println(employee.get(0).getEmpName());
    }
}
