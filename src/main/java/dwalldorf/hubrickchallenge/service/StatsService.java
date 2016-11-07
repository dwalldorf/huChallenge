package dwalldorf.hubrickchallenge.service;

import dwalldorf.hubrickchallenge.model.Department;
import dwalldorf.hubrickchallenge.model.Employee;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class StatsService {

    public Map<Department, BigDecimal> getMedianIncomeByDepartment(List<Employee> employees) {
        Map<Department, BigDecimal> retVal = new HashMap<>();
        employees.stream()
                 .sorted((e1, e2) -> e1.getSalary().compareTo(e2.getSalary()))
                 .collect(Collectors.groupingBy(Employee::getDepartment))
                 .forEach((k, v) -> {
                     int middle = v.size() / 2;
                     retVal.put(k, v.get(middle).getSalary());
                 });
        return retVal;
    }

    public Map<Double, Double> getIncomeAverageByAgeRange(List<Employee> employees) {
        Map<Double, Double> retVal = new HashMap<>();

        employees.stream()
                 .collect(Collectors.groupingBy(e -> Math.ceil(e.getAge() / 10)))
                 .forEach((range, employeeList) -> {
                     double rangeAverage = employeeList
                             .stream()
                             .mapToDouble(e -> e.getSalary().doubleValue())
                             .average()
                             .getAsDouble();
                     retVal.put(range, rangeAverage);
                 });

        return retVal;
    }

    public Map<Department, Integer> getMedianEmployeeAgeByDepartment(List<Employee> employees) {
        Map<Department, Integer> retVal = new HashMap<>();
        employees.stream()
                 .sorted((e1, e2) -> e1.getAge().compareTo(e2.getAge()))
                 .collect(Collectors.groupingBy(Employee::getDepartment))
                 .forEach((k, v) -> {
                     int middle = v.size() / 2;
                     retVal.put(k, v.get(middle).getAge());
                 });
        return retVal;
    }

    public Map<Department, BigDecimal> getIncome95ByDepartment(List<Employee> employees) {
        Map<Department, BigDecimal> retVal = new HashMap<>();

        employees.stream()
                 .sorted((e1, e2) -> e1.getSalary().compareTo(e2.getSalary()))
                 .collect(Collectors.groupingBy(Employee::getDepartment))
                 .forEach((k, v) -> {
                     int n = Math.round(v.size() * 95 / 100);
                     retVal.put(k, v.get(n).getSalary());
                 });

        return retVal;
    }
}
