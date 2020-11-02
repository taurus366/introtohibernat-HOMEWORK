import java.util.Scanner;


public class JpaMain {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        HomeWork hm = new HomeWork();

        hm.createEntityManagerFactory();

        System.out.println("Please write HW number to test:");
        System.out.printf("[%d] Change casing\n" +
                        "[%d] Contains Employee\n" +
                        "[%d] Employees with Salary Over 50 000\n" +
                        "[%d] Employees from Department\n" +
                        "[%d] Adding a New Address and Updating Employee\n" +
                        "[%d] Addresses with Employee Count\n" +
                        "[%d] Get Employee with Project\n" +
                        "[%d] Find Latest 10 Projects\n" +
                        "[%d] Increase Salaries\n" +
                        "[%d] Find Employees by First Name\n" +
                        "[%d] Employees Maximum Salaries\n" +
                        "[%d] Remove Towns\n",
                2,3,4,5,6,7,8,9,10,11,12,13);

        switch (sc.nextLine()){
            case "2":
                hm.changeCasing();
                break;
            case "3":
                System.out.println("Please write employee full name");
                hm.containsEmployee(sc.nextLine());
                break;
            case "4":
                hm.employeesWithSalaryOver50000();
                break;
            case "5":
                hm.employeesFromDepartment();
                break;
            case "6":
                System.out.println("Write employee lastname");
                hm.addingNewAddressAndUpdatingEmployee(sc.nextLine());
                break;
            case "7":
                hm.addressWithEmployeeCount();
                break;
            case "8":
                System.out.println("Please write employee's ID");
                hm.getEmployeeWithProject(Integer.parseInt(sc.nextLine()));
                break;
            case "9":
                hm.findLatest10Projects();
                break;
            case "10":
                break;
            case "11":
                break;
            case "12":
                break;
            case "13":
                break;
        }


//        em.getTransaction().begin();
//        List<Town> list = em.createQuery("SELECT s FROM  Town s",Town.class)
//                .getResultList();
//        list.stream()
//                .filter(town -> town.getName().length()>5)
//                .forEach(town -> town.setName(town.getName().toLowerCase()));
//        em.detach(list);
//        em.getTransaction().commit();
//        em.close();

    }
}
