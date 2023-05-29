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

    public void setReason() {
        String reason;
        while (true) {
            try {
                System.out.print("- 방문 이유 (100자 이내): ");
                reason = sc.nextLine();
                if (reason.length() <= 100) {
                    this.reason = reason;
                    break;
                } else {
                    throw new InputMismatchException();
                }
            } catch (InputMismatchException e) {
                System.out.println("ERROR! 100자 이내로 입력해주세요.");
            }
        }
    }

    public void setDescript() {
        String descript;
        while (true) {
            try {
                System.out.print("- 진단 내용 (100자 이내): ");
                descript = sc.nextLine();
                if (descript != null) {
                    if (descript.length() <= 100) {
                        this.descript = descript;
                        break;
                    }
                } else {
                    throw new InputMismatchException();
                }
            } catch (InputMismatchException e) {
                System.out.println("ERROR! 100자 이내로 입력해주세요.");
            }
        }
    }

    public void setDisease() {
        String disease;
        while (true) {
            try {
                System.out.print("- 병명 (20자 이내): ");
                disease = sc.nextLine();
                if (disease.length() <= 20) {
                    this.disease = disease;
                    break;
                } else {
                    throw new InputMismatchException();
                }
            } catch (InputMismatchException e) {
                System.out.println("ERROR! 100자 이내로 입력해주세요.");
            }
        }
    }

    public void setH_date() {
        String h_date;
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        while (true) {
            try {
                System.out.println("- 방문 날짜 (ex. 2023-05-28)(오늘이라면 0을 입력하세요.): ");
                h_date = sc.next();
                if (h_date.equals("0")) {
                    this.h_date = today;
                    break;
                } else { // 미래 날짜인지 확인
                    int check = today.compareTo(h_date);
                    if (check < 0) {
                        System.out.println("미래 날짜로 설정할 수 없습니다.");
                        throw new InputMismatchException();
                    } else {
                        this.h_date = h_date;
                        break;
                    }
                }
            } catch (InputMismatchException e) {
                System.out.println("알맞는 값을 입력해주세요.");
            }
        }

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
        System.out.println("--------------------------------------------------");
    }

    @Override
    public void insert() throws SQLException {
        System.out.println("=================== [ 병원 진료 기록 등록 ] ====================");
        new Hospital().select();
        int i = 0;

        while (true) {
            System.out.println("- 병원 번호(NO) 선택  ");
            System.out.println("목록에 병원이 없다면 0을 입력하세요. (병원 등록)");
            System.out.println("--------------------------------------------------");
            try {
                i = Integer.parseInt(sc.nextLine());
                if (i == 0) {   // 목록에 해당 번호가 없다면 다시 입력 받기
                    new Hospital().insert();
                    break;
                } else if (!Main.exist("Hospital", "H#", i)) {
                    System.out.println("목록에 있는 병원의 번호를 입력해주세요.");
                } else {
                    setHosp_id(i);
                    break;
                }
            } catch (Exception e) {
                System.out.println("알맞은 값을 입력해주세요.");
            }
        }

        setReason();
        setDescript();
        setDisease();
        setH_date();
        System.out.println("======================================================\n");

        String sql = "INSERT INTO Hospital_Record (u#, hosp_id, reason, descript, disease, h_date) " +
                "VALUES ('" + Main.myUser.id+ "', '" + hosp_id + "', '" + reason + "', '" +
                descript + "', '" + disease + "', '" + h_date + "');";
//        System.out.println(sql);

        Main.stmt.execute(sql);
        System.out.println("진료 기록 완료");

    }

    @Override
    public void update() throws SQLException {
        int r_num = 0;
        while (true) {
            try {
                System.out.println("=================== [ 병원 진료 기록 수정 ] ====================");
                System.out.print("- 수정할 기록 번호: ");
                r_num = sc.nextInt();
                if (!Main.exist("Hospital_Record", "R#", r_num)) {
                    System.out.println("목록에 있는 병원의 번호를 입력해주세요.");
                }
                break;
            } catch(Exception e) {
                    System.out.println("목록에 존재하는 번호를 입력하세요.");
            }
        }
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
                System.out.print("- 무슨 값으로 수정할까요?: ");
                sc.nextLine();
                String val = sc.nextLine();
                System.out.println("입력 완료");
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
        int r_num=0;
        while (true) {
            try {
                System.out.println("=================== [ 병원 진료 기록 삭제 ] ====================");
                System.out.print("- 삭제할 기록의 번호를 입력하세요. :");
                r_num = sc.nextInt();
                if (!Main.exist("Hospital_Record", "R#", r_num)) {
                    System.out.println("목록에 있는 병원의 번호를 입력해주세요.");
                }
                break;
            } catch (Exception e) {
                System.out.println("목록에 존재하는 번호를 입력하세요.");
            }
        }
        String sql = "DELETE FROM Hospital_Record " + "WHERE R# = " + r_num;

        int res = Main.stmt.executeUpdate(sql);
        if (res > 0) {
            System.out.println("진료기록 삭제가 완료되었습니다.");
        } else {
            System.out.println("진료기록 삭제를 실패하였습니다.");
        }
    }

}
