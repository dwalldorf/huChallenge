package dwalldorf.hubrickchallenge;

import dwalldorf.hubrickchallenge.model.Employee;
import dwalldorf.hubrickchallenge.repository.CsvRepository;
import dwalldorf.hubrickchallenge.service.CsvService;
import dwalldorf.hubrickchallenge.service.StatsService;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class Application implements CommandLineRunner {

    private final static Logger logger = LoggerFactory.getLogger(Application.class);

    private final static String EXPORT_PATH = "./";

    @Inject
    private CsvRepository csvRepository;

    @Inject
    private CsvService csvService;

    @Inject
    private StatsService statsService;

    public static void main(String args[]) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        List<Employee> employees = csvRepository.getEmployees();
        logger.info("processing {} employees", employees.size());

        exportMedianIncomeByDepartment(employees);
        exportIncome95ByDepartment(employees);
        exportIncomeAverageByAgeRange(employees);
        exportMedianEmployeeAgeByDepartment(employees);
    }

    private void exportMedianIncomeByDepartment(List<Employee> employees) {
        final Path targetFile = Paths.get(EXPORT_PATH, "income-by-department.csv");
        final List<String> headers = Arrays.asList("department", "income");

        List<List<String>> csvData = new ArrayList<>();
        statsService.getMedianIncomeByDepartment(employees)
                    .forEach((k, v) -> csvData.add(Arrays.asList(k.getName(), v.toString())));

        csvService.writeFile(targetFile, headers, csvData);
    }

    private void exportIncomeAverageByAgeRange(List<Employee> employees) {
        final Path targetFile = Paths.get(EXPORT_PATH, "income-average-by-age-range.csv");
        final List<String> headers = Arrays.asList("range", "income average");

        List<List<String>> csvData = new ArrayList<>();
        statsService.getIncomeAverageByAgeRange(employees)
                    .forEach((k, v) -> csvData.add(Arrays.asList(k.toString(), v.toString())));

        csvService.writeFile(targetFile, headers, csvData);
    }

    private void exportIncome95ByDepartment(List<Employee> employees) {
        final Path targetFile = Paths.get(EXPORT_PATH, "income-95-by-department.csv");
        final List<String> headers = Arrays.asList("department", "income");

        List<List<String>> csvData = new ArrayList<>();
        statsService.getIncome95ByDepartment(employees)
                    .forEach((k, v) -> csvData.add(Arrays.asList(k.getName(), v.toString())));

        csvService.writeFile(targetFile, headers, csvData);
    }

    private void exportMedianEmployeeAgeByDepartment(List<Employee> employees) {
        final Path targetFile = Paths.get(EXPORT_PATH, "employee-age-by-department.csv");
        final List<String> headers = Arrays.asList("department", "median age");

        List<List<String>> csvData = new ArrayList<>();
        statsService.getMedianEmployeeAgeByDepartment(employees)
                    .forEach((k, v) -> csvData.add(Arrays.asList(k.getName(), String.valueOf(v))));

        csvService.writeFile(targetFile, headers, csvData);
    }
}
