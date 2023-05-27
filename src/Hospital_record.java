import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Hospital_record implements DB_func{
    int hosp_id;
    String reason;
    String descript;
    String disease;
    String h_date;
    Scanner sc = new Scanner(System.in);
    ResultSet resultSet;

    public Hospital_record() {}

    public void setHosp_id(int hosp_id) {
        this.hosp_id = hosp_id;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public void setH_date(String h_date) {
        this.h_date = h_date;
    }

    @Override
    public void select() throws SQLException {
        System.out.println("[병원 진료 기록 목록]");
        System.out.println("| NO |   병원명   |   방문 이유   |   진단 내용   |  병명  |  방문날짜  |");
        String sql = "SELECT R.R#, H.name, R.reason, R.descript, R.disease, R.h_date\n" +
                "FROM Hospital H, Hospital_Record R, MyUser U\n" +
                "WHERE H.H# = R.hosp_id\n" +
                "AND R.u# = U.U#\n" +
                "AND U.U# = " + Main.myUser.id + ";";
        resultSet = Main.stmt.executeQuery(sql);

        while(resultSet.next()) {
            System.out.println(resultSet.getInt(1) + "\t|" + resultSet.getString(2)
                    + "\t|" + resultSet.getString(3)+ "\t|" + resultSet.getString(4)
                    + "\t|" + resultSet.getString(5) + "\t|" + resultSet.getString(6));
        }
    }

    @Override
    public void insert() throws SQLException {
        System.out.println("=================== [ 병원 진료 기록 등록 ] ====================");
        new Hospital().select();
        System.out.print("- 병원 id 선택  ");
        System.out.println("목록에 병원이 없다면 0을 입력하세요. (병원 등록)");
        int i = sc.nextInt();
        if (i == 0) {
            new Hospital().insert();
        } else {
            setHosp_id(i);
            System.out.println("- 방문 이유: ");
            setReason(sc.nextLine());
            System.out.println("- 진단 내용: ");
            setDescript(sc.nextLine());
            System.out.println("- 병명: ");
            setDisease(sc.nextLine());
            setH_date(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            System.out.println("======================================================\n");
        }

        String sql = "INSERT INTO Hospital_Record (u#, hosp_id, descript, disease, h_date)" +
                "VALUES ('" + Main.myUser.id+ "', '" + hosp_id + "', '" +
                descript + "', '" + disease + "', '" + h_date + "');";
        System.out.println(sql);
        try {
            Main.stmt.execute(sql);
            System.out.println("진료 기록 완료");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update() throws SQLException {
        System.out.println("=================== [ 병원 진료 기록 수정 ] ====================");
        System.out.println("- 수정할 기록 번호: ");
        int r_num = sc.nextInt();
        System.out.println("- 수정할 항목의 번호를 입력하세요. : ");
        System.out.println("1. 병원명   2. 방문 이유   3. 진단 내용   4. 병명  ");
        System.out.println("--------------------------------------------------");
        String attr = "";
        String sql = "";

        try {
            int num = sc.nextInt();

            if (num < 1 || num > 5) {
                throw new InputMismatchException();
            }
            switch (num) {
                case 1:
                    attr = "hosp_id";
                    System.out.println("=================== [ 병원 진료 기록 등록 ] ====================");
                    new Hospital().select();
                    System.out.print("- 병원 id 선택  ");
                    int i = sc.nextInt();
                    sql = "UPDATE Hospital_Record SET hosp_id = " + i + "WHERE R# = " + r_num + ";";
                    break;
                case 2:
                    attr = "reason";
                    break;
                case 3:
                    attr = "descript";
                    break;
                case 4:
                    attr = "disease";
                    break;
            }
            if (!attr.equals("hosp_id")) {
                System.out.println("- 무슨 값으로 수정할까요?: ");
                String val = sc.nextLine();
                sql = "UPDATE Hospital_Record SET " + attr + " = '" + val + "' WHERE R# = " + r_num + ";";
            }
        } catch (Exception e) {
            System.out.println("알맞은 값을 입력하세요.");
        }

        Main.stmt.executeUpdate(sql);
        System.out.println("수정 완료");
    }


    @Override
    public void delete() throws SQLException {

    }

}
