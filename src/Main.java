import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static Connection con = null;
    public static Statement stmt;
    public static MyUser myUser;
    public static void main(String[] args) {
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
            System.out.println("======================================================\n");
            System.out.println("원하는 번호를 입력하세요.");
            System.out.println("4. 병원 등록    5. 병원 진료 기록    6. 오늘의 컨디션");
            System.out.println("7. 건강기록 모아보기    8. 회원 정보(이름) 수정");
            System.out.println("======================================================\n");

            try {
                input_num = scanner.nextInt();
                if (input_num < 4 || input_num > 7) {
                    throw new InputMismatchException();
                }
            }   catch (InputMismatchException e) {
                System.out.println("4~7 중에서 선택하세요.");
            }

            Hospital hospital;
            switch (input_num) {
                case 4: // 병원 등록
                    hospital = new Hospital();
                    hospital.insert();
                    break;
                case 5: // 병원 진료 기록
                    break;
                case 6: // 오늘의 컨디션
                    break;
                case 7: // 건강기록 모아보기
                    break;
                case 8: // 회원 정보 수정
                    myUser.update(myUser.nickname);

                    break;
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
