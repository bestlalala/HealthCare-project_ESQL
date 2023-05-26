import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Hospital implements DB_func{
    String name;
    String subject;
    String location;
    Scanner sc = new Scanner(System.in);
    ResultSet resultSet = null;     // 출력할 결과

    public Hospital() {
        System.out.println("===================== [ 병원 등록 ] ======================\n");
        System.out.print("- 병원 이름 (50자 이내) : ");
        setName(sc.next());
        System.out.print("- 병원 종류 (20자 이내, ex. general) : ");
        setSubject(sc.next());
        System.out.print("- 지역 : ");
        setLocation(sc.next());
        System.out.println("======================================================\n");
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override   // 전체 병원 조회
    public void select() throws SQLException {
        resultSet = Main.stmt.executeQuery("SELECT * FROM Hospital;");

        while(resultSet.next()) {
            System.out.println(resultSet.getInt(1) + "\t" + resultSet.getString(2)
                    + "\t" + resultSet.getString(3)+ "\t" + resultSet.getString(4));
        }
    }

    @Override
    public void insert() {
        String sql = "INSERT INTO Hospital VALUES ('" + name + "', '" + subject + "', '" + location + "');";
        System.out.println(sql);
        try {
            Main.stmt.execute(sql);
            System.out.println("병원 등록 성공!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(String id) {
    }

    @Override
    public void delete() throws SQLException {

    }
}
