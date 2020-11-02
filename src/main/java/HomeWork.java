import entities.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class HomeWork {
    private EntityManager em;

    public void createEntityManagerFactory() {
        EntityManagerFactory factory =
                Persistence.createEntityManagerFactory("soft_uni");

        this.em = factory.createEntityManager();
    }


    public void changeCasing() {
        List<Town> list = em.createQuery("SELECT s FROM  Town s", Town.class)
                .getResultList();

        em.getTransaction().begin();
        list.forEach(em::detach);

        list.stream()
                .filter(town -> town.getName().length() > 5)
                .forEach(town -> town.setName(town.getName().toLowerCase()));

        list.forEach(em::merge);
        em.getTransaction().commit();
        em.close();
    }

    public void containsEmployee(String employeeName) {

        em.getTransaction().begin();
        List<Employee> employeeList = em.createQuery("SELECT e FROM Employee e WHERE CONCAT(e.firstName,' ',e.lastName) = ?1 ", Employee.class)
                .setParameter(1, employeeName)
                .getResultList();
        em.getTransaction().commit();
        em.close();

        System.out.println(employeeList.size() == 0 ? "No" : "Yes");
    }

    public void employeesWithSalaryOver50000() {

        em.getTransaction().begin();
        List<Employee> employeeList = em.createQuery("SELECT e FROM Employee e WHERE e.salary > :salary", Employee.class)
                .setParameter("salary", BigDecimal.valueOf(50000))
                .getResultList();
        em.getTransaction().commit();
        em.close();
        employeeList.forEach(s -> System.out.println(s.getFirstName()));
    }

    public void employeesFromDepartment() {
        em.getTransaction().begin();
        List<Employee> employeeList = em.createQuery("SELECT e FROM Employee e WHERE e.department.name = :name ORDER BY e.salary, e.id ", Employee.class)
                .setParameter("name", "Research and Development")
                .getResultList();
        em.getTransaction().commit();
        em.close();

        employeeList.forEach(e -> System.out.printf("%s %s from %s - $%.2f\n", e.getFirstName(), e.getLastName(), e.getDepartment().getName(), e.getSalary()));
    }

    public void addingNewAddressAndUpdatingEmployee(String input) {
        Address address = new Address();
        address.setText("Vitoshka 15");


        em.getTransaction().begin();
//
        List<Address> addressList = em.createQuery("SELECT a FROM Address a WHERE a.text = ?1", Address.class)
                .setParameter(1, "Vitoshka 15")
                .setMaxResults(1)
                .getResultList();

        if (addressList.size() == 0) {
            em.persist(address);
        } else {
            address = addressList.get(0);
        }

        List<Employee> employee = em.createQuery("SELECT e FROM Employee e WHERE e.lastName = ?1 ", Employee.class)
                .setParameter(1, input)
                .setMaxResults(1)
                .getResultList();
        System.out.println(employee.size());
        employee.forEach(em::detach);

        Address finalAddress = address;
        employee.forEach(e -> e.setAddress(finalAddress));

        employee.forEach(em::merge);

        em.getTransaction().commit();

        em.close();


    }

    public void addressWithEmployeeCount() {

        em.getTransaction().begin();
        List<Address> addressList = em.createQuery("SELECT a FROM Address a ORDER BY a.employees.size DESC",Address.class)
                .setMaxResults(10)
                .getResultList();
        em.getTransaction().commit();
      addressList.forEach(a -> System.out.printf("%s, %s - %d employees\n",a.getText(),a.getTown().getName(),a.getEmployees().size()));
        em.close();
    }

    public void getEmployeeWithProject(int input) {
        em.getTransaction().begin();
        List<Employee> list = em.createQuery("SELECT e FROM Employee e WHERE e.id = ?1",Employee.class)
                .setParameter(1,input)
                .getResultList();
        em.getTransaction().commit();

        Comparator<Project> compareByProjectName = Comparator.comparing(Project::getName);

        list.forEach(e -> {
            System.out.printf("%s %s - %s\n",e.getFirstName(),e.getLastName(),e.getJobTitle());
            e.getProjects()
                    .stream()
                    .sorted(compareByProjectName)
                    .forEach(p -> System.out.println(p.getName()));
        });
        em.close();


    }

    public void findLatest10Projects() {
        em.getTransaction().begin();
        List<Project> projectList = em.createQuery("SELECT p FROM Project p ORDER BY p.startDate DESC",Project.class)
                .setMaxResults(10)
                .getResultList();

        projectList
                .stream()
                .sorted(Comparator.comparing(Project::getName))
                .forEach(p -> System.out.printf("Project name:%s\n    Project Description:%s\n    Project Start Date:%s\n    Project End Date:%s\n",p.getName(),p.getDescription(),p.getStartDate(),p.getEndDate()));

        em.getTransaction().commit();
        em.close();


    }

    public void increaseSalaries() {
        em.getTransaction().begin();
        List<Employee> employeeList = em.createQuery("SELECT e FROM Employee e WHERE e.department.name IN(?1)",Employee.class)
                .setParameter(1, Arrays.asList("Engineering","Tool Design","Marketing","Information Services"))
                .getResultList();
        BigDecimal bigDecimal = new BigDecimal("1.12");

        employeeList.forEach(em::detach);
        employeeList.forEach(e ->  e.setSalary(bigDecimal.multiply(e.getSalary())));
        employeeList.forEach(em::merge);

        employeeList.forEach(e -> System.out.printf("%s %s ($%.2f)\n",e.getFirstName(),e.getLastName(),e.getSalary()));

        em.getTransaction().commit();
        em.close();
    }

    public void findEmployeesByFirstName(String employeeFirstName) {
        em.getTransaction().begin();
        List<Employee> employeeList = em.createQuery("SELECT e FROM Employee e WHERE e.firstName LIKE ?1",Employee.class)
                .setParameter(1, employeeFirstName+"%")
                .getResultList();

        employeeList.forEach(e -> System.out.printf("%s %s - %s - ($%.2f)\n",e.getFirstName(),e.getLastName(),e.getJobTitle(),e.getSalary()));

        em.getTransaction().commit();
        em.close();
    }
}
