import entities.Address;
import entities.Employee;
import entities.Town;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.math.BigDecimal;
import java.util.List;

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
    }
}
