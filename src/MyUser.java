import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class MyUser implements DB_func{
    ResultSet resultSet = null;     // 출력할 결과
    String nickname;
    String username;
    String regist_num;
    String gender;
    Scanner sc = new Scanner(System.in);

    public void setNickname(String nickname) {
        while (true) {
            if (nickname.length() <= 15) {
                this.nickname = nickname;
                break;
            } else {
                System.out.println("ERROR! 15자 이내로 입력해주세요.");
                System.out.print("- 닉네임 (15자 이내) : ");
                nickname = sc.next();
            }
        }
    }

    public void setUsername(String username) {
        while (true) {
            if (username.length() <= 50) {
                this.username = username;
                break;
            } else {
                System.out.println("ERROR! 50자 이내로 입력해주세요.");
                System.out.print("- 이름 (50자 이내) : ");
                username = sc.next();
            }
        }

    }

    public void setRegist_num(String regist_num) {
        while (true) {
            if (regist_num.length() == 7) {
//                int year = Integer.parseInt(regist_num.substring(0, 2));
//                int month = Integer.parseInt(regist_num.substring(2, 4));
//                int day = Integer.parseInt(regist_num.substring(4, 6));
                int gen = Integer.parseInt(regist_num.substring(6, 7));
                if (gen > 0 && gen < 5) {
                    this.regist_num = regist_num;
                    break;
                } else {
                    System.out.println("ERROR! 주민번호가 알맞지 않습니다. 다시 입력해주세요.");
                    System.out.print("- 주민번호 앞 6자리와 뒤 1자리 (ex. 0106274) : ");
                    regist_num = sc.next();
                }
            } else {
                System.out.println("ERROR! 주민번호가 알맞지 않습니다. 다시 입력해주세요.");
                System.out.print("- 주민번호 앞 6자리와 뒤 1자리 (ex. 0106274) : ");
                regist_num = sc.next();
            }
        }
    }

    public void setGender(String gender) {
        while (true) {
            if (gender.equals("F") || gender.equals("M")) {
                this.gender = gender;
                break;
            } else {
                System.out.println("ERROR! 여성은 F, 남성은 M을 입력해주세요.");
                System.out.print("- 성별(F/M): ");
                gender = sc.next();
            }
        }
    }

    public MyUser()  {
        System.out.print("- 닉네임 (15자 이내) : ");
        setNickname(sc.next());
        System.out.print("- 이름 (50자 이내) : ");
        setUsername(sc.next());
        System.out.print("- 주민번호 앞 6자리와 뒤 1자리 (ex. 0106274) : ");
        setRegist_num(sc.next());
        System.out.print("- 성별(F/M): ");
        setGender(sc.next());
        System.out.println("======================================================\n");
    }

    public MyUser(String nickname) {
        this.nickname = nickname;
    }


    @Override
    public void select() throws SQLException {
        resultSet = Main.stmt.executeQuery("SELECT * FROM MyUser;");

        while(resultSet.next()) {
            //getInt(1)은 컬럼의 1번째 값을 Int형으로 가져온다. / getString(2)는 컬럼의 2번째 값을 String형으로 가져온다.
            System.out.println(resultSet.getInt(1) + "\t" + resultSet.getString(2));
        }
    }

    public boolean login() {
        boolean result = false;
        try {
            resultSet = Main.stmt.executeQuery("SELECT * FROM MyUser WHERE nickname = '" + this.nickname + "';");
            while (resultSet.next()) {
                this.username = resultSet.getString(3);
                this.regist_num = resultSet.getString(4);
                this.gender = resultSet.getString(5);
                System.out.println("로그인 성공!");
                result = true;
            }
        } catch(SQLException e) {
            e.printStackTrace();
            System.out.println("로그인 실패");
        }
        return result;
    }

    @Override
    public void insert() {
        String sql = "INSERT INTO MyUser VALUES ('" + nickname + "', '" + username + "', '" + regist_num + "', '" + gender + "');";
        System.out.println(sql);
        try {
            Main.stmt.execute(sql);
            System.out.println("회원가입 성공!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(String id) {
        System.out.println("======================================================\n");
        System.out.print("- 이름: ");
        setUsername(sc.next());
        System.out.println("======================================================\n");

        String sql = "UPDATE MyUser SET username = '" + this.username + "' WHERE nickname = '" + this.nickname +"';";

        try {
            Main.stmt.executeUpdate(sql);
            System.out.println("수정 완료");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete() {

    }
}
