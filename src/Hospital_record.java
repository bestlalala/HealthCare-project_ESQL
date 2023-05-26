import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Hospital_record implements DB_func{
    int hosp_id;
    String reason;
    String descript;
    String disease;
    String h_date;
    Scanner sc = new Scanner(System.in);
    ResultSet resultSet;

    public Hospital_record() throws SQLException {
        System.out.println("=================== [ 병원 진료 기록 등록 ] ====================");
        getHospitalList();
        System.out.print("- 병원 id 선택  ");
        System.out.println("목록에 병원이 없다면 0을 입력하세요. (병원 등록)");
        int i = sc.nextInt();
        if (i == 0) {
            new Hospital().insert();
        } else {
            setHosp_id(i);
            System.out.println("- 방문 이유: ");
            setReason(sc.next());
            System.out.println("- 진단 내용: ");
            setDescript(sc.next());
            System.out.println("- 병명: ");
            setDisease(sc.next());
            setH_date(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            System.out.println("======================================================\n");
        }
    }

    private void getHospitalList() throws SQLException {
        System.out.println("[병원 목록]");
        System.out.println("<id>  |   <병원명>   |   <병원종류>   |   <지역>");
        resultSet = Main.stmt.executeQuery("SELECT * FROM Hospital;");

        while(resultSet.next()) {
            System.out.println(resultSet.getInt(1) + "\t|" + resultSet.getString(2)
                    + "\t|" + resultSet.getString(3)+ "\t|" + resultSet.getString(4));
        }
    }

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

    }

    @Override
    public void insert() {
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
    public void update(String id) {

    }

    @Override
    public void delete() throws SQLException {

    }
}
