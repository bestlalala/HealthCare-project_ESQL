import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Hospital implements DB_func{
    String name;
    String subject;
    String location;
    Scanner sc = new Scanner(System.in);
    public static ResultSet resultSet = null;     // 출력할 결과

    public Hospital() {
    }

    public void setName() {
        String name;
        while (true) {
            try {
                System.out.print("- 병원 이름 (50자 이내) : ");
                name = sc.nextLine();
                if (name.length() <= 50) {
                    this.name = name;
                    break;
                } else {
                    throw new InputMismatchException();
                }
            } catch (InputMismatchException e) {
                System.out.println("알맞은 값을 입력해주세요.");
            }
        }
    }

    public void setSubject() {
        String subject;
        while (true) {
            try {
                System.out.print("- 병원 종류 (20자 이내, ex. general) : ");
                subject = sc.nextLine();
                if (subject.length() <= 20) {
                    this.subject = subject;
                    break;
                } else {
                    throw new InputMismatchException();
                }
            } catch (InputMismatchException e) {
                System.out.println("알맞은 값을 입력해주세요.");
            }
        }
    }

    public void setLocation() {
        String location;
        while (true) {
            try {
                System.out.print("- 지역 (15자 이내) : ");
                location = sc.nextLine();
                if (location.equals("\n") || location.length() <= 15) {
                    this.location = location;
                    break;
                } else {
                    throw new InputMismatchException();
                }
            } catch (InputMismatchException e) {
                System.out.println("알맞은 값을 입력해주세요.");
            }
        }
    }

    @Override   // 전체 병원 조회
    public void select() throws SQLException {
        System.out.println("[병원 목록]");
        System.out.println("|NO|      병원명     |   병원종류   |  지역  |");
        resultSet = Main.stmt.executeQuery("SELECT * FROM Hospital;");

        while(resultSet.next()) {
            System.out.println(resultSet.getInt(1) + "\t|" + resultSet.getString(2)
                    + "\t|" + resultSet.getString(3)+ "\t|" + resultSet.getString(4));
        }
    }

    @Override
    public void insert() throws SQLException {
        System.out.println("===================== [ 병원 등록 ] ======================\n");
        setName();
        setSubject();
        setLocation();
        System.out.println("======================================================\n");

        String sql = "INSERT INTO Hospital VALUES ('" + name + "', '" + subject + "', '" + location + "');";
//        System.out.println(sql);
        Main.stmt.execute(sql);
        System.out.println("병원 등록 성공!");

    }

    @Override
    public void update() throws SQLException {
    }


    @Override
    public void delete() throws SQLException {

    }
}
