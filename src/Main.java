import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static Connection con = null;
    public static Statement stmt;
    public static MyUser myUser;
    public static Hospital hospital;
    public static void main(String[] args) throws SQLException {
        makeConnection();
        int input_num = 0;
        Scanner scanner = new Scanner(System.in);

        boolean flag = true;
        while (flag) {
            System.out.println();
            System.out.println("================== 건강 관리 기록 시스템 ==================");
            System.out.println("환영합니다. 원하는 번호를 입력하세요.");
            System.out.println("1. 회원가입    2. 로그인    3. 종료");
            System.out.println("======================================================");

            try {
                input_num = scanner.nextInt();
                if (input_num < 0 || input_num > 3) {
                    throw new Exception("알맞은 값을 입력하세요.");
                }
            }   catch (Exception e) {
                System.out.println("1, 2, 3 중에서 선택하세요.");
            }

            switch (input_num) {
                case 1: // 회원가입
                    myUser = new MyUser();
                    myUser.insert();
                    break;
                case 2: // 로그인
                    System.out.print("- 닉네임: ");
                    String nick = scanner.next();
                    myUser = new MyUser(nick);
                    if (myUser.login()) {
                        flag = false;
                        break;
                    } else {
                        continue;
                    }
                case 3: // 종료
                    System.out.println("\n** 프로그램을 종료합니다. **");
                    System.exit(0);
            }
        }


        while (true) {
            System.out.println("======================================================");
            System.out.println("원하는 번호를 입력하세요.");
            System.out.println("4. 병원 목록    5. 병원 진료 기록    6. 신체 측정 기록");
            System.out.println("7. 건강기록 모아보기    8. 회원 정보 조회     9. 회원 탈퇴");
            System.out.println("10. 종료");
            System.out.println("======================================================\n");

            try {
                input_num = scanner.nextInt();
                if (input_num < 4 || input_num > 10) {
                    throw new InputMismatchException();
                }
            }   catch (InputMismatchException e) {
                System.out.println("4~10 중에서 선택하세요.");
            }

            int num = 0;
            switch (input_num) {
                case 4: // 병원 등록
                    while (true) {
                        System.out.println("==================== [ 병원 목록 ] ===================");
                        System.out.println("원하는 번호를 입력하세요.");
                        System.out.println("1. 등록    2. 돌아가기");
                        System.out.println("========================================================");
                        hospital = new Hospital();
                        hospital.select();

                        try {
                            num = scanner.nextInt();
                            if (num < 1 || num > 2) {
                                throw new InputMismatchException();
                            }
                            break;
                        } catch (InputMismatchException e) {
                            System.out.println("1~2 중에서 선택하세요.");
                        }
                    }

                    switch (num) {
                        case 1:
                            hospital.insert();
                            break;
                        case 2:
                            break;
                    }

                    break;
                case 5: // 병원 진료 기록
                    Hospital_record hospital_record;
                    while (true) {
                        System.out.println("==================== [ 병원 진료 기록 ] ===================");
                        System.out.println("원하는 번호를 입력하세요.");
                        System.out.println("1. 등록    2. 수정    3. 삭제    4. 돌아가기");
                        System.out.println("========================================================");
                        hospital_record = new Hospital_record();
                        hospital_record.select();

                        try {
                            num = scanner.nextInt();
                            if (num < 1 || num > 4) {
                                throw new InputMismatchException();
                            }
                            break;
                        } catch (InputMismatchException e) {
                            System.out.println("1~4 중에서 선택하세요.");
                        }
                    }

                    switch (num) {
                        case 1:
                            hospital_record.insert();
                            break;
                        case 2:
                            hospital_record.update();
                            break;
                        case 3:
                            hospital_record.delete();
                            break;
                        case 4:
                            break;
                    }
                    break;
                case 6: // 신체 측정 기록
                    Physical_info physical_info;
                    while (true) {
                        System.out.println("==================== [ 신체 측정 기록 ] ===================");
                        System.out.println("원하는 번호를 입력하세요.");
                        System.out.println("1. 등록    2. 수정    3. 삭제    4. 돌아가기");
                        System.out.println("========================================================");
                        physical_info = new Physical_info();
                        physical_info.select();
                        try {
                            num = scanner.nextInt();
                            if (num < 1 || num > 4) {
                                throw new InputMismatchException();
                            }
                            break;
                        } catch (InputMismatchException e) {
                            System.out.println("1~4 중에서 선택하세요.");
                        }
                    }

                    switch (num) {
                        case 1:
                            physical_info.insert();
                            break;
                        case 2:
                            physical_info.update();
                            break;
                        case 3:
                            physical_info.delete();
                            break;
                        case 4:
                            break;
                    }
                    break;
                case 7: // 건강기록 모아보기
                    System.out.println("==================== [ 건강 기록 모아보기 ] ===================");
                    System.out.println("|   이름   | 달 | 병원 방문 횟수 |  가장 많이 방문한 병원   |  평균 키  | 평균 체중 |");
                    String sql = "SELECT U.username, MONTH(R.h_date) as month, COUNT(*) as visit_cnt, MAX(H.name) as most_visited_hospital, P2.avg_height, P2.avg_weight\n" +
                            "FROM Hospital_Record R\n" +
                            "    LEFT JOIN (\n" +
                            "        SELECT u#, MONTH(p_date) as month, AVG(P.user_height) as avg_height, AVG(P.user_weight) as avg_weight\n" +
                            "        FROM Physical_info P\n" +
                            "        GROUP BY u#, MONTH(p_date)\n" +
                            "    ) P2 ON MONTH(R.h_date) = P2.[month] AND R.u# = P2.u#\n" +
                            "    , Hospital H, MyUser U\n" +
                            "WHERE R.u# = U.U#\n" +
                            "AND R.hosp_id = H.H#\n" +
                            "AND U.u# = " + myUser.id +
                            "GROUP BY U.username, P2.avg_height, P2.avg_weight, MONTH(h_date);";
                    ResultSet resultSet = Main.stmt.executeQuery(sql);

                    while(resultSet.next()) {
                        System.out.println("| " + resultSet.getString(1) + "  | " + resultSet.getInt(2)
                                + " | " + resultSet.getInt(3)+ " | " + resultSet.getString(4)
                                + " | " + resultSet.getFloat(5) + " | " + resultSet.getFloat(6));
                    }
                    System.out.println("--------------------------------------------------");
                    break;
                case 8: // 회원 정보 조회
                    while (true) {
                        System.out.println("==================== [ 회원 정보 ] ===================");
                        myUser.select();
                        System.out.println("========================================================");
                        System.out.println("원하는 번호를 입력하세요.");
                        System.out.println("1. 회원 정보 수정(이름)    2. 돌아가기");
                        System.out.println("========================================================");
                        int n = 0;
                        try {
                            n = scanner.nextInt();
                            if (n == 1) {
                                myUser.update();
                                break;
                            } else if (n == 2) {
                                break;
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("1, 2 중에서 선택하세요.");
                        }
                    }
                    break;
                case 9: // 회원 탈퇴
                    myUser.delete();
                    System.out.println("\n** 프로그램을 종료합니다. **");
                    System.exit(0);
                    break;
                case 10:
                    System.out.println("\n** 프로그램을 종료합니다. **");
                    System.exit(0);
            }
        }

    }

    public static void makeConnection() {
        ResultSet resultSet = null;
        String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=HealthCare-prj;user=sa;password=Yslee0627@;encrypt=false;";

        // 1.드라이버 로딩
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

        } catch (ClassNotFoundException e) {
            System.err.println(" !! <JDBC 오류> Driver load 오류: " + e.getMessage());
            e.printStackTrace();
        }

        // 2.연결
        try {
            con = DriverManager.getConnection(connectionUrl);
            System.out.println("정상적으로 연결되었습니다.");
            stmt = con.createStatement();

        } catch(SQLException e) {
            System.err.println("con 오류:" + e.getMessage());
            e.printStackTrace();
        }

//        // 3.해제
//        try {
//            if(con != null)
//                con.close();
//        } catch (SQLException e) {}
    }

    public static boolean exist(String table, String attr, int pk) throws SQLException {
        boolean result;
        ResultSet resultSet = Main.stmt.executeQuery("SELECT * FROM " + table + " WHERE " + attr + " = " + pk + ";");
        result = resultSet.next();
        return result;
    }
}
