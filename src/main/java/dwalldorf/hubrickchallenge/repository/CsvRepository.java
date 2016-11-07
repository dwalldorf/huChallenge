package dwalldorf.hubrickchallenge.repository;

import dwalldorf.hubrickchallenge.model.Department;
import dwalldorf.hubrickchallenge.model.Employee;
import dwalldorf.hubrickchallenge.service.CsvService;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CsvRepository {

    private final static Logger logger = LoggerFactory.getLogger(CsvRepository.class);

    private final static String BASE_PATH = "src/main/resources/";
    private final static Path EMPLOYEES_FILE = Paths.get(BASE_PATH, "employees.csv");
    private final static Path DEPARTMENTS_FILE = Paths.get(BASE_PATH, "departments.csv");
    private final static Path AGES_FILE = Paths.get(BASE_PATH, "ages.csv");

    @Inject
    private CsvService csvService;

    public List<Employee> getEmployees() {
        List<Employee> retVal = new ArrayList<>();
        List<Department> departments = getDepartmentsSortedAlphabetically();

        try {
            List<List<String>> employees = csvService.readFile(EMPLOYEES_FILE);
            Map<String, Integer> nameAgeMap = getNameAgeMap();

            employees.forEach(e -> {
                Employee employee = createEmployee(e, nameAgeMap, departments);
                if (employee != null) {
                    retVal.add(employee);
                }
            });
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return retVal;
    }

    private List<Department> getDepartmentsSortedAlphabetically() {
        List<Department> retVal = new ArrayList<>();

        try {
            List<List<String>> departments = csvService.readFile(DEPARTMENTS_FILE);
            departments.forEach(d -> retVal.add(new Department(d.get(0))));
            retVal.sort((d1, d2) -> d1.getName().compareTo(d2.getName()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return retVal;
    }

    private Map<String, Integer> getNameAgeMap() throws IOException {
        Map<String, Integer> retVal = new HashMap<>();
        List<List<String>> agesList = csvService.readFile(AGES_FILE);

        agesList.forEach(a -> retVal.put(a.get(0), Integer.valueOf(a.get(1))));
        return retVal;
    }

    private Employee createEmployee(List<String> employee, Map<String, Integer> nameAgeMap, List<Department> departments) {
        boolean validEmployee = true;
        Employee retVal = null;

        Integer departmentIndex = null;
        String fullName = employee.get(1);
        Department department = null;
        String sex = employee.get(2);
        Integer age = null;
        BigDecimal salary = null;

        try {
            departmentIndex = Integer.valueOf(employee.get(0)) - 1;
        } catch (NumberFormatException e) {
            logger.error("'{}' department could not be converted to number.", fullName);
            validEmployee = false;
        }

        if (departmentIndex < departments.size()) {
            department = departments.get(departmentIndex);
        } else {
            logger.error("'{}' has invalid department index.", fullName);
            validEmployee = false;
        }

        try {
            salary = new BigDecimal(employee.get(3));
        } catch (NumberFormatException ex) {
            logger.error("'{}' has invalid salary format.", fullName);
            validEmployee = false;
        }

        try {
            age = nameAgeMap.get(fullName);
        } catch (NullPointerException e) {
            logger.error("'{}' has no age", fullName);
            validEmployee = false;
        }

        if (validEmployee) {
            retVal = new Employee(fullName, department, sex, age, salary);
        } else {
            logger.error("Ignoring employee '{}' due to errors", fullName);
        }

        return retVal;
    }
}
