import java.io.StringWriter;
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

    public void setMeasure_date() {
        String measure_date;
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        while (true) {
            try {
                System.out.println("- 측정 날짜 (ex. 2023-05-28)(오늘이라면 0을 입력하세요.): ");
                measure_date = sc.next();
                if (measure_date.equals("0")) {
                    this.measure_date = today;
                    break;
                } else { // 미래 날짜인지 확인
                    int check = today.compareTo(measure_date);
                    if (check < 0) {
                        System.out.println("미래 날짜로 설정할 수 없습니다.");
                        throw new InputMismatchException();
                    } else {
                        this.measure_date = measure_date;
                        break;
                    }
                }
            } catch (InputMismatchException e) {
                System.out.println("알맞는 값을 입력해주세요.");
            }
        }
    }

    public void setHeight() {
        float height;
        while (true) {
            try {
                System.out.print("- 키(cm): ");
                height = sc.nextFloat();
                if (height > 0) {
                    this.height = height;
                    break;
                } else {
                    throw new InputMismatchException();
                }
            } catch (InputMismatchException e) {
                System.out.println("알맞는 값을 입력하세요.");
            }
        }
    }

    public void setWeight() {
        float weight;
        while (true) {
            try {
                System.out.println("- 체중(kg): ");
                weight = sc.nextFloat();
                if (weight > 0) {
                    this.weight = weight;
                    break;
                } else {
                    throw new InputMismatchException();
                }
            } catch (InputMismatchException e) {
                System.out.println("알맞는 값을 입력하세요.");
            }
        }
    }

    public void setBMI() {
        this.BMI = weight / (height * height);
    }

    public void setWaist() {
        float waist;
        while (true) {
            try {
                System.out.println("- 허리 둘레: ");
                waist = sc.nextFloat();
                if (waist > 0) {
                    this.waist = waist;
                    break;
                } else {
                    throw new InputMismatchException();
                }
            } catch (InputMismatchException e) {
                System.out.println("알맞는 값을 입력하세요.");
            }
        }
    }

    public void setFat() {
        float fat;
        while (true) {
            try {
                System.out.println("- 체지방률(%): ");
                fat = sc.nextFloat();
                if (fat > 0) {
                    this.fat = fat;
                    break;
                } else {
                    throw new InputMismatchException();
                }
            } catch (InputMismatchException e) {
                System.out.println("알맞는 값을 입력하세요.");
            }
        }
    }

    public void setMuscle() {
        float muscle;
        while (true) {
            try {
                System.out.println("- 골격근량(kg): ");
                muscle = sc.nextFloat();
                if (muscle > 0) {
                    this.muscle = muscle;
                    break;
                } else {
                    throw new InputMismatchException();
                }
            } catch (InputMismatchException e) {
                System.out.println("알맞는 값을 입력하세요.");
            }
        }
    }

    public void setMetabolic_rate() {
        int metabolic_rate;
        while (true) {
            try {
                System.out.println("- 기초대사량(kcal): ");
                metabolic_rate = sc.nextInt();
                if (metabolic_rate > 0) {
                    this.metabolic_rate = metabolic_rate;
                    break;
                } else {
                    throw new InputMismatchException();
                }
            } catch (InputMismatchException e) {
                System.out.println("알맞는 값을 입력하세요.");
            }
        }
    }

    @Override
    public void select() throws SQLException {
        System.out.println("================== [ 신체 측정 기록 조회 ] ==================");
        System.out.println("|NO| 키(cm) | 체중(kg) | BMI | 허리 둘레(cm) | 체지방률(%) | 골격근량(kg) | 기초대사량(kcal) |  측정 날짜  |");
        resultSet = Main.stmt.executeQuery("SELECT * FROM Physical_info WHERE u# = " + u_id + " ORDER BY p_date;");

        while(resultSet.next()) {
            System.out.println("| " + resultSet.getInt(1) + " |\t" + resultSet.getFloat(3)
                    + " |\t" + resultSet.getFloat(4)+ " |\t" + resultSet.getFloat(5)
                    + " |\t" + resultSet.getFloat(6)+ " |\t" + resultSet.getFloat(7)
                    + " |\t" + resultSet.getFloat(8)+ " |\t" + resultSet.getFloat(9)
                    + " |\t" + resultSet.getString(10));
        }
        System.out.println("========================================================");
    }

    @Override
    public void insert() throws SQLException{
        System.out.println("=================== [ 신체 측정 정보 등록 ] ====================");
        setHeight();
        setWeight();
        setWaist();
        setFat();
        setMuscle();
        setMetabolic_rate();
        setMeasure_date();
        setBMI();

        String sql = "INSERT INTO Physical_info (u#, user_height, user_weight, waist, fat, muscle, metabolic_rate, p_date)" +
                "VALUES ('" + u_id+ "', '" + height + "', '" +
                weight + "', '" + waist + "', '" + fat + "', '"+ muscle + "', '" + metabolic_rate + "', '" +measure_date + "');";
//        System.out.println(sql);

        Main.stmt.execute(sql);
        System.out.println("진료 기록 완료");

    }

    public boolean exist(String table, String attr, int pk) throws SQLException {
        boolean result;
        resultSet = Main.stmt.executeQuery("SELECT * FROM " + table + " WHERE " + attr + " = " + pk + ";");
        result = resultSet.next();
        return result;
    }

    @Override
    public void update() throws SQLException {
        int p_num;
        String attr = "";
        System.out.println("=================== [ 신체 측정 기록 수정 ] ====================");
        while (true) {
            try {
                System.out.println("- 수정할 번호: ");
                p_num = Integer.parseInt(sc.nextLine());

                if (!exist("Physical_info", "P#", p_num)) {
                    System.out.println("목록에 있는 기록의 번호를 입력해주세요.");
                } else {
                    break;
                }
            } catch (Exception e) {
                System.out.println("알맞은 번호를 입력하세요.");
            }
        }

        int num = 0;
        while (true) {
            try {
                System.out.println("- 수정할 항목의 번호를 입력하세요. : ");
                System.out.println("1. 키   2. 체중   3. 허리 둘레   4. 체지방률   5. 골격근량");
                System.out.println("--------------------------------------------------");
                num = Integer.parseInt(sc.nextLine());
                if (num < 1 || num > 5) {
                    throw new Exception();
                } else {
                    break;
                }
            } catch (Exception e) {
                System.out.println("알맞은 번호를 입력하세요(1~5)");
            }
        }
        switch (num) {
            case 1:
                attr = "user_height";
                break;
            case 2:
                attr = "user_weight";
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
        float val = 0;
        while (true) {
            try {
                System.out.println("- 무슨 값으로 수정할까요?: ");
                val = Float.parseFloat(sc.nextLine());
                if (val > 0) {
                    break;
                } else {
                    throw new Exception("양수를 입력하세요.");
                }
            } catch (Exception e) {
                System.out.println("알맞은 값을 입력하세요.");
            }
        }
        String sql = "UPDATE Physical_info SET " + attr + " = " + val + " WHERE P# = " + p_num + ";";
        System.out.println(sql);
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

