import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Physical_info implements DB_func{
    long u_id;
    String measure_date;
    float height;   // 입력 필수
    float weight;   // 입력 필수
    float BMI;
    float waist;
    float fat;
    float muscle;
    int metabolic_rate;
    Scanner sc = new Scanner(System.in);
    ResultSet resultSet;

    public Physical_info() {
        this.u_id = Main.myUser.id;
    }

    public void setMeasure_date(String measure_date) {
        if (measure_date.equals("0")) {
                measure_date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        }
        this.measure_date = measure_date;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public void setBMI(float BMI) {
        this.BMI = BMI;
    }

    public void setWaist(float waist) {
        this.waist = waist;
    }

    public void setFat(float fat) {
        this.fat = fat;
    }

    public void setMuscle(float muscle) {
        this.muscle = muscle;
    }

    public void setMetabolic_rate(int metabolic_rate) {
        this.metabolic_rate = metabolic_rate;
    }

    @Override
    public void select() throws SQLException {
        System.out.println("================== [ 신체 측정 기록 조회 ] ==================");
        System.out.println("| id | 키 |  체중  | BMI |  허리 둘레  |  체지방률  |  골격근량  |  기초대사량  |  측정 날짜  |");
        resultSet = Main.stmt.executeQuery("SELECT * FROM Physical_info WHERE u# = " + u_id + ";");

        while(resultSet.next()) {
            System.out.println(resultSet.getInt(1) + "\t|" + resultSet.getFloat(3)
                    + "\t|" + resultSet.getFloat(4)+ "\t|" + resultSet.getFloat(5)
                    + "\t|" + resultSet.getFloat(6)+ "\t|" + resultSet.getFloat(7)
                    + "\t|" + resultSet.getFloat(8)+ "\t|" + resultSet.getFloat(9)
                    + "\t|" + resultSet.getString(10));
        }
        System.out.println("========================================================");
    }

    @Override
    public void insert() {
        while (true) {
            try {
                System.out.println("=================== [ 신체 측정 정보 등록 ] ====================");
                System.out.print("- 키(cm): ");
                setHeight(sc.nextFloat());
                System.out.println("- 체중(kg): ");
                setWeight(sc.nextFloat());
                System.out.println("- 허리 둘레: ");
                setWaist(sc.nextFloat());
                System.out.println("- 체지방률(%): ");
                setFat(sc.nextFloat());
                System.out.println("- 골격근량(kg): ");
                setMuscle(sc.nextFloat());
                System.out.println("- 기초대사량(kcal): ");
                setMetabolic_rate(sc.nextInt());
                System.out.println("- 측정 날짜 (오늘이라면 0을 입력하세요.): ");
                setMeasure_date(sc.next());
                break;
            } catch (InputMismatchException e) {
                e.printStackTrace();
                System.out.println("알맞는 값을 입력헤주세요.");
            }
        }

        String sql = "INSERT INTO Physical_info (u#, user_height, user_weight, waist, fat, muscle, metabolic_rate, p_date)" +
                "VALUES ('" + u_id+ "', '" + height + "', '" +
                weight + "', '" + waist + "', '" + fat + "', '"+ muscle + "', '" + metabolic_rate + "', '" +measure_date + "');";
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
        System.out.println("=================== [ 신체 측정 기록 수정 ] ====================");
        System.out.println("- 수정할 번호: ");
        int p_num = sc.nextInt();
        System.out.println("- 수정할 항목의 번호를 입력하세요. : ");
        System.out.println("1. 키   2. 체중   3. 허리 둘레   4. 체지방률   5. 골격근량");
        System.out.println("--------------------------------------------------");
        String attr = "";
        try {
            int num = sc.nextInt();

            if (num < 1 || num > 5) {
                throw new InputMismatchException();
            }
            switch (num) {
                case 1:
                    attr = "height";
                    break;
                case 2:
                    attr = "weight";
                    break;
                case 3:
                    attr = "waist";
                    break;
                case 4:
                    attr = "fat";
                    break;
                case 5:
                    attr = "muscle";
                    break;
            }
        } catch (InputMismatchException e) {
            System.out.println("알맞은 번호를 입력하세요.");
        }

        System.out.println("- 무슨 값으로 수정할까요?: ");
        float val = sc.nextInt();

        String sql = "UPDATE Physical_info SET " + attr + " = " + val + "WHERE P# = " + p_num + ";";

        Main.stmt.executeUpdate(sql);
        System.out.println("수정 완료");

    }


    @Override
    public void delete() throws SQLException {
        System.out.println("=================== [ 신체 측정 기록 삭제 ] ====================");
        System.out.println("- 삭제할 기록의 번호를 입력하세요. :");
        int p_num = sc.nextInt();

        String sql = "DELETE FROM Physical_info " + "WHERE P# = " + p_num;

        int res = Main.stmt.executeUpdate(sql);
        if (res > 0) {
            System.out.println("회원 탈퇴가 완료되었습니다.");
        }
    }
}

