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
            scanner = new Scanner(System.in);

            try {
                input_num = scanner.nextInt();
                if (input_num < 0 || input_num > 3) {
                    throw new InputMismatchException();
                }
            }   catch (InputMismatchException e) {
                System.out.println("1, 2, 3 중에서 선택하세요.");
            }

            switch (input_num) {
                case 1: // 회원가입
                    myUser = new MyUser();
                    myUser.insert();
                    break;
                case 2: // 로그인
                    System.out.print("- 닉네임: ");
                    scanner = new Scanner(System.in);
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
            System.out.println("4. 병원 등록    5. 병원 진료 기록    6. 신체 측정 기록");
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


            switch (input_num) {
                case 4: // 병원 등록
                    hospital = new Hospital();
                    hospital.insert();
                    break;
                case 5: // 병원 진료 기록
                    System.out.println("==================== [ 병원 진료 기록 ] ===================");
                    System.out.println("원하는 번호를 입력하세요.");
                    System.out.println("1. 등록    2. 수정    3. 삭제");
                    System.out.println("========================================================");
                    Hospital_record hospital_record = new Hospital_record();
                    hospital_record.insert();
                    break;
                case 6: // 신체 측정 기록
                    System.out.println("==================== [ 신체 측정 기록 ] ===================");
                    System.out.println("원하는 번호를 입력하세요.");
                    System.out.println("1. 등록    2. 수정    3. 삭제");
                    System.out.println("========================================================");
                    new Physical_info().select();

                    int num = 0;
                    try {
                        num = scanner.nextInt();
                        if (num < 1 || num > 3) {
                            throw new InputMismatchException();
                        }
                    }   catch (InputMismatchException e) {
                        System.out.println("1~3 중에서 선택하세요.");
                    }

                    switch (num) {
                        case 1:
                            Physical_info physical_info = new Physical_info(myUser.id);
                            physical_info.insert();
                            break;
                        case 2:
                            new Physical_info().update();
                            break;
                        case 3:
                            new Physical_info().delete();
                            break;
                    }
                    break;
                case 7: // 건강기록 모아보기
                    break;
                case 8: // 회원 정보 조회
                    System.out.println("==================== [ 회원 정보 ] ===================");
                    myUser.select();
                    System.out.println("========================================================");
                    System.out.println("원하는 번호를 입력하세요.");
                    System.out.println("- 1. 회원 정보 수정하기");
                    System.out.println("- 2. 돌아가기");
                    System.out.println("========================================================");
                    int n = 0;
                    try {
                        n = scanner.nextInt();
                        if (n == 1) {
                            myUser.update();
                        } else if (n == 2) {
                            break;
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("1, 2 중에서 선택하세요.");
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
//            resultSet = stmt.executeQuery("SELECT * FROM MyUser;");
//
//            while(resultSet.next()) {
//                //getInt(1)은 컬럼의 1번째 값을 Int형으로 가져온다. / getString(2)는 컬럼의 2번째 값을 String형으로 가져온다.
//                System.out.println(resultSet.getInt(1) + "\t" + resultSet.getString(2));
//            }

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
}
